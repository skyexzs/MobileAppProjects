package com.example.tourismapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainFragment extends Fragment implements TopAdapter.OnTopClickListener, PlacesAdapter.OnPlacesClickListener {
    private static final String ARG_DEST_LIST = "DESTINATION_LIST";
    private static final String ARG_TOP_LIST = "TOP_LIST";

    private List<Destination> topList;
    private List<Destination> destinationList;

    private RecyclerView topRecyclerView;
    private TopAdapter topAdapter;
    private RecyclerView placesRecyclerView;
    private PlacesAdapter placesAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(ArrayList<Destination> destinations, ArrayList<Destination> topList) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEST_LIST, destinations);
        args.putSerializable(ARG_TOP_LIST, topList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.destinationList = ((ArrayList<Destination>) getArguments().getSerializable(ARG_DEST_LIST));
            this.topList = ((ArrayList<Destination>) getArguments().getSerializable(ARG_TOP_LIST));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topRecyclerView = view.findViewById(R.id.topRecyclerView);
        placesRecyclerView = view.findViewById(R.id.placesRecyclerView);

        MainActivity _activity = (MainActivity) getActivity();
        _activity.showMainActionBar();

        setTopAdapter();
        setPlacesAdapter();
    }

    private void setTopAdapter() {
        topAdapter = new TopAdapter(topList, this.getContext(), this);
        topRecyclerView.setAdapter(topAdapter);
        topRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setPlacesAdapter() {
        placesAdapter = new PlacesAdapter(destinationList, this.getContext(), this);
        placesRecyclerView.setAdapter(placesAdapter);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onItemClick(int position) {
        MainActivity _activity = (MainActivity) getActivity();
        _activity.showDescriptionFragment(topList.get(position));
    }

    @Override
    public void onPlacesClick(int position) {
        MainActivity _activity = (MainActivity) getActivity();
        _activity.showDescriptionFragment(destinationList.get(position));
    }
}