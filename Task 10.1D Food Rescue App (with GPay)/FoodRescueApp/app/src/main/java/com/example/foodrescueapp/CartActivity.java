package com.example.foodrescueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodrescueapp.data.CartDatabase;
import com.example.foodrescueapp.data.UserDatabase;
import com.example.foodrescueapp.model.CartItem;
import com.example.foodrescueapp.util.Authenticator;
import com.example.foodrescueapp.util.PaymentsUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Optional;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartClickListener, CartAdapter.OnDeleteListener {
    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    private View googlePayButton;

    private UserDatabase userDb;
    private CartDatabase cartDb;

    private RecyclerView cartRecyclerView;
    private TextView cartTotalTextView;
    private TextView cartFinalPriceTextView;
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItems;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.setClickable(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Authenticator.checkAuthentication(this);

        userDb = new UserDatabase(this);
        cartDb = new CartDatabase(this);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartTotalTextView = findViewById(R.id.cartTotalTextView);
        cartFinalPriceTextView = findViewById(R.id.cartFinalPriceTextView);
        googlePayButton = findViewById(R.id.cartGPayButton);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setCartAdapter();
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();
    }

    private void setCartAdapter() {
        cartItems = cartDb.fetchAllCartItemsFromUser(Authenticator.getUserEmail(this));

        if (cartItems != null) {
            cartAdapter = new CartAdapter(cartItems, this, this, this);
            cartRecyclerView.setAdapter(cartAdapter);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        refreshPriceText();
    }

    private void refreshPriceText() {
        int total_price = 0;

        if (cartItems != null && cartItems.size() > 0) {
            for (CartItem c : cartItems) {
                total_price += 1;
            }
        }

        cartFinalPriceTextView.setText(String.valueOf(total_price));
    }

    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Google Pay unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, "Billing name: " + billingName,
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    public void onGPayClick(View view) {
        int total_price = 0;

        if (cartItems != null && cartItems.size() > 0) {
            for (CartItem c : cartItems) {
                total_price += 1;
            }
        }

        if (total_price <= 0) {
            Toast.makeText(this, "There is nothing to pay for!", Toast.LENGTH_SHORT).show();
            return;
        }

        requestPayment(view);
    }

    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        try {
            double total_price = 0.00;

            for (CartItem c : cartItems) {
                total_price += 1;
            }

            long totalPriceCents = Math.round(total_price * PaymentsUtil.CENTS_IN_A_UNIT.longValue());

            Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(totalPriceCents);
            if (!paymentDataRequestJson.isPresent()) {
                return;
            }

            PaymentDataRequest request =
                    PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        this, LOAD_PAYMENT_DATA_REQUEST_CODE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCartClick(int position) {

    }

    @Override
    public void onDelete() {
        refreshPriceText();
    }
}