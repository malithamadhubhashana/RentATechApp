package com.s23010168.myproject.ui.listing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s23010168.myproject.R;
import com.s23010168.myproject.models.Listing;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ListingViewHolder> {

    private List<Listing> listings;
    private OnListingClickListener listener;

    public interface OnListingClickListener {
        void onListingClick(Listing listing);
    }

    public ListingsAdapter(List<Listing> listings, OnListingClickListener listener) {
        this.listings = listings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listing, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listings.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public void updateListings(List<Listing> newListings) {
        this.listings.clear();
        this.listings.addAll(newListings);
        notifyDataSetChanged();
    }

    class ListingViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivListingImage;
        private TextView tvTitle, tvLocation, tvPrice, tvRating, tvReviewCount;
        private RatingBar ratingBar;

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivListingImage = itemView.findViewById(R.id.iv_listing_image);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviewCount = itemView.findViewById(R.id.tv_review_count);
            ratingBar = itemView.findViewById(R.id.rating_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onListingClick(listings.get(position));
                    }
                }
            });
        }

        public void bind(Listing listing) {
            tvTitle.setText(listing.getTitle());
            tvLocation.setText(listing.getLocation());
            tvPrice.setText("$" + String.format("%.0f", listing.getPrice()) + "/month");
            
            if (listing.getRating() > 0) {
                ratingBar.setRating(listing.getRating());
                tvRating.setText(String.format("%.1f", listing.getRating()));
                tvReviewCount.setText("(" + listing.getReviewCount() + ")");
                ratingBar.setVisibility(View.VISIBLE);
                tvRating.setVisibility(View.VISIBLE);
                tvReviewCount.setVisibility(View.VISIBLE);
            } else {
                ratingBar.setVisibility(View.GONE);
                tvRating.setVisibility(View.GONE);
                tvReviewCount.setVisibility(View.GONE);
            }

            // TODO: Load image using Glide or Picasso
            // For now, set a placeholder
            ivListingImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
} 