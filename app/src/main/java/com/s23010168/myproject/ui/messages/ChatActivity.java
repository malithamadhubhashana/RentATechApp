package com.s23010168.myproject.ui.messages;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView tvChatTitle;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private MaterialButton btnSend;
    private ImageView ivBack;
    
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages;
    private String ownerId, ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ownerId = getIntent().getStringExtra("owner_id");
        ownerName = getIntent().getStringExtra("owner_name");
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadChatHistory();
    }

    private void initViews() {
        tvChatTitle = findViewById(R.id.tv_chat_title);
        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        ivBack = findViewById(R.id.iv_back);
    }

    private void setupRecyclerView() {
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Messages start from bottom
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(chatAdapter);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadChatHistory() {
        // Set chat title
        tvChatTitle.setText("Chat with " + ownerName);
        
        // For demo purposes, create sample messages
        createSampleMessages();
        
        // In a real app, you would load from database
        // repository.getChatMessages(ownerId).observe(this, chatMessages -> {
        //     messages.clear();
        //     messages.addAll(chatMessages);
        //     chatAdapter.notifyDataSetChanged();
        //     scrollToBottom();
        // });
    }

    private void createSampleMessages() {
        messages.clear();
        
        // Add sample messages
        messages.add(new ChatMessage("Hi! I'm interested in your property.", false, System.currentTimeMillis() - 3600000));
        messages.add(new ChatMessage("Hello! Thanks for your interest. The property is still available.", true, System.currentTimeMillis() - 3000000));
        messages.add(new ChatMessage("Great! Can I schedule a viewing?", false, System.currentTimeMillis() - 2400000));
        messages.add(new ChatMessage("Of course! How about tomorrow at 2 PM?", true, System.currentTimeMillis() - 1800000));
        messages.add(new ChatMessage("Perfect! I'll see you then.", false, System.currentTimeMillis() - 1200000));
        
        chatAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        
        if (!TextUtils.isEmpty(messageText)) {
            // Create new message (sent by current user)
            ChatMessage newMessage = new ChatMessage(messageText, false, System.currentTimeMillis());
            messages.add(newMessage);
            
            // Clear input field
            etMessage.setText("");
            
            // Update adapter
            chatAdapter.notifyItemInserted(messages.size() - 1);
            
            // Scroll to bottom
            scrollToBottom();
            
            // TODO: Save message to database
            // repository.saveMessage(newMessage);
            
            // Simulate reply after 2 seconds
            simulateReply();
        }
    }

    private void simulateReply() {
        btnSend.postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] replies = {
                    "Thanks for your message!",
                    "I'll get back to you soon.",
                    "That sounds good!",
                    "Let me check and get back to you.",
                    "Perfect timing!"
                };
                
                String randomReply = replies[(int) (Math.random() * replies.length)];
                ChatMessage replyMessage = new ChatMessage(randomReply, true, System.currentTimeMillis());
                messages.add(replyMessage);
                
                chatAdapter.notifyItemInserted(messages.size() - 1);
                scrollToBottom();
            }
        }, 2000);
    }

    private void scrollToBottom() {
        if (messages.size() > 0) {
            rvMessages.smoothScrollToPosition(messages.size() - 1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Chat message model class
    public static class ChatMessage {
        private String message;
        private boolean isFromOwner;
        private long timestamp;

        public ChatMessage(String message, boolean isFromOwner, long timestamp) {
            this.message = message;
            this.isFromOwner = isFromOwner;
            this.timestamp = timestamp;
        }

        public String getMessage() { return message; }
        public boolean isFromOwner() { return isFromOwner; }
        public long getTimestamp() { return timestamp; }
    }
} 