package com.example.techarent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techarent.R;
import com.example.techarent.adapter.RentalAdapter;
import com.example.techarent.data.DataManager;
import com.example.techarent.model.Rental;

import java.util.List;

public class RentalHistoryFragment extends Fragment implements RentalAdapter.OnRentalActionListener {

    private RecyclerView recyclerView;
    private RentalAdapter adapter;
    private DataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rentals_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataManager = DataManager.getInstance();
        
        recyclerView = view.findViewById(R.id.recyclerViewRentals);
        setupRecyclerView();
        loadRentalHistory();
    }

    private void setupRecyclerView() {
        adapter = new RentalAdapter(false, this); // false for hiding action buttons
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadRentalHistory() {
        List<Rental> rentalHistory = dataManager.getRentalHistory();
        adapter.updateRentals(rentalHistory);
    }

    @Override
    public void onExtendRental(Rental rental) {
        // Not applicable for history
    }

    @Override
    public void onReturnItem(Rental rental) {
        // Not applicable for history
    }
}
