package com.s23010168.myproject.ui.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.s23010168.myproject.R;
import com.s23010168.myproject.ui.messages.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private RecyclerView rvConversations;
    private ConversationsAdapter conversationsAdapter;
    private List<Conversation> conversations;
    private TextView tvEmptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadSampleData();
        
        return view;
    }

    private void initViews(View view) {
        rvConversations = view.findViewById(R.id.rv_conversations);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
    }

    private void setupRecyclerView() {
        conversations = new ArrayList<>();
        conversationsAdapter = new ConversationsAdapter(conversations, new ConversationsAdapter.OnConversationClickListener() {
            @Override
            public void onConversationClick(Conversation conversation) {
                // Navigate to chat activity
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("owner_id", conversation.getOwnerId());
                intent.putExtra("owner_name", conversation.getOwnerName());
                startActivity(intent);
            }
        });
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvConversations.setLayoutManager(layoutManager);
        rvConversations.setAdapter(conversationsAdapter);
    }

    private void loadSampleData() {
        // Add sample conversations
        conversations.clear();
        conversations.add(new Conversation("1", "Sarah Johnson", "Hi! I'm interested in your property.", "2:30 PM", "1"));
        conversations.add(new Conversation("2", "Mike Chen", "Is the apartment still available?", "1:15 PM", "2"));
        conversations.add(new Conversation("3", "Emily Davis", "Can I schedule a viewing?", "11:45 AM", "3"));
        conversations.add(new Conversation("4", "David Wilson", "Thanks for the quick response!", "10:20 AM", "4"));
        
        conversationsAdapter.notifyDataSetChanged();
        
        // Show/hide empty state
        if (conversations.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvConversations.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvConversations.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to this fragment
        loadSampleData();
    }

    // Conversation model class
    public static class Conversation {
        private String id;
        private String ownerName;
        private String lastMessage;
        private String timestamp;
        private String ownerId;

        public Conversation(String id, String ownerName, String lastMessage, String timestamp, String ownerId) {
            this.id = id;
            this.ownerName = ownerName;
            this.lastMessage = lastMessage;
            this.timestamp = timestamp;
            this.ownerId = ownerId;
        }

        public String getId() { return id; }
        public String getOwnerName() { return ownerName; }
        public String getLastMessage() { return lastMessage; }
        public String getTimestamp() { return timestamp; }
        public String getOwnerId() { return ownerId; }
    }

    // Conversations Adapter
    public static class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {
        
        private List<Conversation> conversations;
        private OnConversationClickListener listener;

        public interface OnConversationClickListener {
            void onConversationClick(Conversation conversation);
        }

        public ConversationsAdapter(List<Conversation> conversations, OnConversationClickListener listener) {
            this.conversations = conversations;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_conversation, parent, false);
            return new ConversationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
            Conversation conversation = conversations.get(position);
            holder.bind(conversation);
        }

        @Override
        public int getItemCount() {
            return conversations.size();
        }

        class ConversationViewHolder extends RecyclerView.ViewHolder {
            private TextView tvOwnerName, tvLastMessage, tvTimestamp;

            public ConversationViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOwnerName = itemView.findViewById(R.id.tv_owner_name);
                tvLastMessage = itemView.findViewById(R.id.tv_last_message);
                tvTimestamp = itemView.findViewById(R.id.tv_timestamp);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onConversationClick(conversations.get(position));
                        }
                    }
                });
            }

            public void bind(Conversation conversation) {
                tvOwnerName.setText(conversation.getOwnerName());
                tvLastMessage.setText(conversation.getLastMessage());
                tvTimestamp.setText(conversation.getTimestamp());
            }
        }
    }
} 