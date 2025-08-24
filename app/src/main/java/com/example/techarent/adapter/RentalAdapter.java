package com.example.techarent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techarent.R;
import com.example.techarent.model.Rental;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.RentalViewHolder> {

    private List<Rental> rentals = new ArrayList<>();
    private boolean showActionButtons;
    private OnRentalActionListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public interface OnRentalActionListener {
        void onExtendRental(Rental rental);
        void onReturnItem(Rental rental);
    }

    public RentalAdapter(boolean showActionButtons, OnRentalActionListener listener) {
        this.showActionButtons = showActionButtons;
        this.listener = listener;
    }

    public void updateRentals(List<Rental> newRentals) {
        this.rentals.clear();
        if (newRentals != null) {
            this.rentals.addAll(newRentals);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rental, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        Rental rental = rentals.get(position);
        holder.bind(rental, showActionButtons, listener, dateFormat);
    }

    @Override
    public int getItemCount() {
        return rentals.size();
    }

    static class RentalViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRentalItemIcon, tvRentalItemName, tvRentalStartDate,
                       tvRentalEndDate, tvRentalTotalCost, tvRentalStatus;
        private LinearLayout layoutActionButtons;
        private Button btnExtendRental, btnReturnItem;

        public RentalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRentalItemIcon = itemView.findViewById(R.id.tvRentalItemIcon);
            tvRentalItemName = itemView.findViewById(R.id.tvRentalItemName);
            tvRentalStartDate = itemView.findViewById(R.id.tvRentalStartDate);
            tvRentalEndDate = itemView.findViewById(R.id.tvRentalEndDate);
            tvRentalTotalCost = itemView.findViewById(R.id.tvRentalTotalCost);
            tvRentalStatus = itemView.findViewById(R.id.tvRentalStatus);
            layoutActionButtons = itemView.findViewById(R.id.layoutActionButtons);
            btnExtendRental = itemView.findViewById(R.id.btnExtendRental);
            btnReturnItem = itemView.findViewById(R.id.btnReturnItem);
        }

        public void bind(Rental rental, boolean showActionButtons, 
                        OnRentalActionListener listener, SimpleDateFormat dateFormat) {
            tvRentalItemIcon.setText(rental.getCategoryEmoji());
            tvRentalItemName.setText(rental.getDeviceName());
            tvRentalStartDate.setText(dateFormat.format(rental.getStartDate()));
            tvRentalEndDate.setText(dateFormat.format(rental.getEndDate()));
            tvRentalTotalCost.setText(String.format(Locale.getDefault(), "$%.0f", rental.getTotalCost()));

            // Set status
            switch (rental.getStatus()) {
                case ACTIVE:
                    tvRentalStatus.setText("Active");
                    tvRentalStatus.setBackgroundResource(R.drawable.status_active_background);
                    tvRentalStatus.setTextColor(itemView.getContext().getColor(R.color.white));
                    break;
                case COMPLETED:
                    tvRentalStatus.setText("Completed");
                    tvRentalStatus.setBackgroundResource(R.drawable.status_completed_background);
                    tvRentalStatus.setTextColor(itemView.getContext().getColor(R.color.white));
                    break;
                case CANCELLED:
                    tvRentalStatus.setText("Cancelled");
                    tvRentalStatus.setBackgroundResource(R.drawable.status_completed_background);
                    tvRentalStatus.setTextColor(itemView.getContext().getColor(R.color.white));
                    break;
            }

            // Show/hide action buttons
            if (showActionButtons && rental.getStatus() == Rental.RentalStatus.ACTIVE) {
                layoutActionButtons.setVisibility(View.VISIBLE);
                
                btnExtendRental.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onExtendRental(rental);
                    }
                });
                
                btnReturnItem.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onReturnItem(rental);
                    }
                });
            } else {
                layoutActionButtons.setVisibility(View.GONE);
            }
        }
    }
}
