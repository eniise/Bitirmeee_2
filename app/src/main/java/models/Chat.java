package models;

import com.google.gson.annotations.SerializedName;

public class Chat {
    @SerializedName("id")
    private int id;
    @SerializedName("receiverId")
    private int ReceiverId;
    @SerializedName("senderId")
    private int SenderId;
    @SerializedName("type")
    private int Type;
    @SerializedName("message")
    private String Message;
    @SerializedName("messageDate")
    private String MessageDate;
    @SerializedName("messageFromId")
    private int messageFromId;
    public Chat(int id, int receiverId, int senderId, int type, String message, String messageDate, int messageFromId) {
        this.id = id;
        ReceiverId = receiverId;
        SenderId = senderId;
        Type = type;
        Message = message;
        MessageDate = messageDate;
        this.messageFromId = messageFromId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(int receiverId) {
        ReceiverId = receiverId;
    }

    public int getSenderId() {
        return SenderId;
    }

    public void setSenderId(int senderId) {
        SenderId = senderId;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageDate() {
        return MessageDate;
    }

    public void setMessageDate(String messageDate) {
        MessageDate = messageDate;
    }

    public int getMessageFromId() {
        return messageFromId;
    }

    public void setMessageFromId(int messageFromId) {
        this.messageFromId = messageFromId;
    }
}
