package models;

import com.google.gson.annotations.SerializedName;

public class CurrentChatLastMessageInfo {
    @SerializedName("lastMessage")
    private String LastMessage;
    @SerializedName("lastMessageId")
    private int LastMessageId;
    @SerializedName("messageFromId")
    private int MessageFromId;
    @SerializedName("type")
    private String Transaction;
    @SerializedName("messageTime")
    private String MessageTime;
    public CurrentChatLastMessageInfo(String lastMessage, int lastMessageId, int messageFromId, String transaction, String messageTime) {
        LastMessage = lastMessage;
        LastMessageId = lastMessageId;
        MessageFromId = messageFromId;
        Transaction = transaction;
        MessageTime = messageTime;
    }
    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public int getLastMessageId() {
        return LastMessageId;
    }

    public void setLastMessageId(int lastMessageId) {
        LastMessageId = lastMessageId;
    }

    public String getTransaction() {
        return Transaction;
    }

    public void setTransaction(String transaction) {
        Transaction = transaction;
    }

    public int getMessageFromId() {
        return MessageFromId;
    }

    public void setMessageFromId(int messageFromId) {
        MessageFromId = messageFromId;
    }

    public String getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(String messageTime) {
        MessageTime = messageTime;
    }
}
