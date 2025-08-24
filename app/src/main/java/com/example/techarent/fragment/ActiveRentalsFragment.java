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

public class ActiveRentalsFragment extends Fragment implements RentalAdapter.OnRentalActionListener {

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
        loadActiveRentals();
    }

    private void setupRecyclerView() {
        adapter = new RentalAdapter(true, this); // true for showing action buttons
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadActiveRentals() {
        List<Rental> activeRentals = dataManager.getActiveRentals();
        adapter.updateRentals(activeRentals);
    }

    @Override
    public void onExtendRental(Rental rental) {
        // Handle rental extension
        // In a real app, this would open a dialog or new screen
        if (getContext() != null) {
            android.widget.Toast.makeText(getContext(), 
                "Extend rental for " + rental.getDeviceName(), 
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReturnItem(Rental rental) {
        // Handle item return
        rental.setStatus(Rental.RentalStatus.COMPLETED);
        loadActiveRentals(); // Refresh the list
        
        if (getContext() != null) {
            android.widget.Toast.makeText(getContext(), 
                rental.getDeviceName() + " returned successfully!", 
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}
