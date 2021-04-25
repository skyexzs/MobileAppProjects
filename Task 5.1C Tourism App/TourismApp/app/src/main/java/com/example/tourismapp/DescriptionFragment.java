package com.example.tourismapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DescriptionFragment extends Fragment {
    private static final String ARG_DEST_ITEM = "DESTINATION_ITEM";

    private Destination destination;

    private ImageView descriptionImageView;
    private TextView descriptionTitleTextView;
    private TextView descriptionRatingTextView;
    private RatingBar descriptionRatingBar;
    private TextView descriptionDescriptionTextView;

    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance(Destination destination) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEST_ITEM, destination);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.destination = (Destination) getArguments().getSerializable(ARG_DEST_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionImageView = view.findViewById(R.id.descriptionImageView);
        descriptionTitleTextView = view.findViewById(R.id.descriptionTitleTextView);
        descriptionRatingTextView = view.findViewById(R.id.descriptionRatingTextView);
        descriptionRatingBar = view.findViewById(R.id.descriptionRatingBar);
        descriptionDescriptionTextView = view.findViewById(R.id.descriptionDescriptionTextView);

        setViewContent();
    }

    private void setViewContent() {
        descriptionImageView.setImageResource(destination.getImage());
        descriptionTitleTextView.setText(destination.getTitle());
        descriptionRatingTextView.setText(String.valueOf((destination.getRating())));
        descriptionRatingBar.setRating(destination.getRating());
        descriptionDescriptionTextView.setText(destination.getDescription());
    }
}