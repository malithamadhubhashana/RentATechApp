package com.example.techarent;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter for displaying user's listed items
 */
public class MyItemsAdapter extends RecyclerView.Adapter<MyItemsAdapter.ViewHolder> {
    
    private Context context;
    private Cursor cursor;
    
    public MyItemsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_listing, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }
        
        // Get data from cursor
        String itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
        String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        String brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"));
        double pricePerDay = cursor.getDouble(cursor.getColumnIndexOrThrow("price_per_day"));
        int isAvailable = cursor.getInt(cursor.getColumnIndexOrThrow("is_available"));
        String createdDate = cursor.getString(cursor.getColumnIndexOrThrow("created_date"));
        
        // Set data to views
        holder.tvItemName.setText(itemName);
        holder.tvCategory.setText(category);
        holder.tvBrand.setText(brand != null ? brand : "");
        holder.tvPrice.setText("$" + String.format("%.2f", pricePerDay) + "/day");
        holder.tvStatus.setText(isAvailable == 1 ? "Available" : "Unavailable");
        holder.tvDate.setText(createdDate);
        
        // Set status color
        holder.tvStatus.setTextColor(context.getResources().getColor(
            isAvailable == 1 ? android.R.color.holo_green_dark : android.R.color.holo_red_dark
        ));
    }
    
    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }
    
    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvCategory, tvBrand, tvPrice, tvStatus, tvDate;
        
        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
