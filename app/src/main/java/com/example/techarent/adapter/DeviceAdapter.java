package com.example.techarent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techarent.R;
import com.example.techarent.model.TechDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<TechDevice> devices = new ArrayList<>();
    private OnDeviceClickListener listener;

    public interface OnDeviceClickListener {
        void onDeviceClick(TechDevice device);
        void onRentClick(TechDevice device);
    }

    public DeviceAdapter(OnDeviceClickListener listener) {
        this.listener = listener;
    }

    public void updateDevices(List<TechDevice> newDevices) {
        this.devices.clear();
        if (newDevices != null) {
            this.devices.addAll(newDevices);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tech_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        TechDevice device = devices.get(position);
        holder.bind(device, listener);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemIcon, tvItemName, tvItemDescription, tvItemPrice, 
                       tvAvailabilityStatus;
        private View viewAvailabilityIndicator;
        private Button btnRentItem;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemIcon = itemView.findViewById(R.id.tvItemIcon);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvAvailabilityStatus = itemView.findViewById(R.id.tvAvailabilityStatus);
            viewAvailabilityIndicator = itemView.findViewById(R.id.viewAvailabilityIndicator);
            btnRentItem = itemView.findViewById(R.id.btnRentItem);
        }

        public void bind(TechDevice device, OnDeviceClickListener listener) {
            tvItemIcon.setText(device.getCategoryEmoji());
            tvItemName.setText(device.getName());
            tvItemDescription.setText(device.getDescription());
            tvItemPrice.setText(String.format(Locale.getDefault(), "$%.0f", device.getPricePerDay()));

            // Set availability status
            if (device.isAvailable()) {
                tvAvailabilityStatus.setText(R.string.available);
                tvAvailabilityStatus.setTextColor(itemView.getContext().getColor(R.color.accent_green));
                viewAvailabilityIndicator.setBackgroundResource(R.drawable.circle_green);
                btnRentItem.setEnabled(true);
                btnRentItem.setText(R.string.rent_now);
                btnRentItem.setBackgroundTintList(
                    itemView.getContext().getColorStateList(R.color.primary_blue));
            } else {
                tvAvailabilityStatus.setText(R.string.unavailable);
                tvAvailabilityStatus.setTextColor(itemView.getContext().getColor(R.color.accent_red));
                viewAvailabilityIndicator.setBackgroundResource(R.drawable.circle_red);
                btnRentItem.setEnabled(false);
                btnRentItem.setText("Unavailable");
                btnRentItem.setBackgroundTintList(
                    itemView.getContext().getColorStateList(R.color.text_hint));
            }

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeviceClick(device);
                }
            });

            btnRentItem.setOnClickListener(v -> {
                if (listener != null && device.isAvailable()) {
                    listener.onRentClick(device);
                }
            });
        }
    }
}
