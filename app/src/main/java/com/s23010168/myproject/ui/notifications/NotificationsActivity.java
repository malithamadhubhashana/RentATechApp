package com.s23010168.myproject.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.s23010168.myproject.R;
import com.s23010168.myproject.ui.booking.BookingActivity;
import com.s23010168.myproject.ui.listing.ListingDetailsActivity;
import com.s23010168.myproject.ui.messages.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private NotificationsAdapter notificationsAdapter;
    private List<Notification> notifications;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initViews();
        setupRecyclerView();
        loadNotifications();
    }

    private void initViews() {
        rvNotifications = findViewById(R.id.rv_notifications);
        tvEmptyState = findViewById(R.id.tv_empty_state);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupRecyclerView() {
        notifications = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(notifications, new NotificationsAdapter.OnNotificationClickListener() {
            @Override
            public void onNotificationClick(Notification notification) {
                handleNotificationClick(notification);
            }
        });
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvNotifications.setLayoutManager(layoutManager);
        rvNotifications.setAdapter(notificationsAdapter);
    }

    private void loadNotifications() {
        // Add sample notifications
        notifications.clear();
        notifications.add(new Notification("1", "New Booking Request", 
            "Sarah Johnson wants to book your Downtown Apartment", 
            "2 hours ago", NotificationType.BOOKING, "listing_1"));
        notifications.add(new Notification("2", "New Message", 
            "Mike Chen sent you a message about the Beach House", 
            "1 hour ago", NotificationType.MESSAGE, "chat_1"));
        notifications.add(new Notification("3", "Booking Confirmed", 
            "Your booking for Mountain Cabin has been confirmed", 
            "3 hours ago", NotificationType.BOOKING, "booking_1"));
        notifications.add(new Notification("4", "New Review", 
            "Emily Davis left a 5-star review for your property", 
            "5 hours ago", NotificationType.REVIEW, "review_1"));
        notifications.add(new Notification("5", "System Update", 
            "New features available! Check out the updated map view", 
            "1 day ago", NotificationType.SYSTEM, null));
        
        notificationsAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (notifications.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
        }
    }

    private void handleNotificationClick(Notification notification) {
        switch (notification.getType()) {
            case BOOKING:
                // Navigate to booking details
                Intent bookingIntent = new Intent(this, BookingActivity.class);
                bookingIntent.putExtra("booking_id", notification.getReferenceId());
                startActivity(bookingIntent);
                break;
            case MESSAGE:
                // Navigate to chat
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra("chat_id", notification.getReferenceId());
                startActivity(chatIntent);
                break;
            case REVIEW:
                // Navigate to listing details to see review
                Intent listingIntent = new Intent(this, ListingDetailsActivity.class);
                listingIntent.putExtra("listing_id", notification.getReferenceId());
                startActivity(listingIntent);
                break;
            case SYSTEM:
                // Show system update dialog or navigate to settings
                // For now, just show a toast
                break;
        }
        
        // Mark notification as read
        markNotificationAsRead(notification.getId());
    }

    private void markNotificationAsRead(String notificationId) {
        // TODO: Update notification status in database
        // For now, just remove from the list
        notifications.removeIf(n -> n.getId().equals(notificationId));
        notificationsAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Notification model class
    public static class Notification {
        private String id;
        private String title;
        private String message;
        private String timestamp;
        private NotificationType type;
        private String referenceId;

        public Notification(String id, String title, String message, String timestamp, 
                          NotificationType type, String referenceId) {
            this.id = id;
            this.title = title;
            this.message = message;
            this.timestamp = timestamp;
            this.type = type;
            this.referenceId = referenceId;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getTimestamp() { return timestamp; }
        public NotificationType getType() { return type; }
        public String getReferenceId() { return referenceId; }
    }

    public enum NotificationType {
        BOOKING, MESSAGE, REVIEW, SYSTEM
    }
} 