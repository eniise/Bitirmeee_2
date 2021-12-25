package models;

import com.google.gson.annotations.SerializedName;

public class ChatDetail {
    @SerializedName("chatId")
    private int ChatId;
    @SerializedName("receiverImage")
    private String ReceiverImageUrl;
    @SerializedName("senderImage")
    private String SenderImageUrl;
    @SerializedName("receiverName")
    private String ReceiverName;
    @SerializedName("senderName")
    private String SenderName;
    @SerializedName("messageType")
    private int MessageType;
    @SerializedName("message")
    private String Message;
    @SerializedName("messageFromId")
    private int MessageFromId;
    @SerializedName("messageDate")
    private String MessageDate;
    public ChatDetail(int chatId, String receiverImageUrl, String senderImageUrl, String receiverName, String senderName, int messageType, String message, int messageFromId, String messageDate) {
        ChatId = chatId;
        ReceiverImageUrl = receiverImageUrl;
        SenderImageUrl = senderImageUrl;
        ReceiverName = receiverName;
        SenderName = senderName;
        MessageType = messageType;
        Message = message;
        MessageFromId = messageFromId;
        MessageDate = messageDate;
    }
    public ChatDetail(String message,String messageDate,int senderId,int messageType){
        this.Message = message;
        this.MessageDate = messageDate;
        this.MessageFromId = senderId;
        this.MessageType = messageType;
    }
    public int getChatId() {
        return ChatId;
    }

    public String getReceiverImageUrl() {
        return ReceiverImageUrl;
    }

    public void setChatId(int chatId) {
        ChatId = chatId;
    }

    public void setReceiverImageUrl(String receiverImageUrl) {
        ReceiverImageUrl = receiverImageUrl;
    }

    public void setSenderImageUrl(String senderImageUrl) {
        SenderImageUrl = senderImageUrl;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSenderImageUrl() {
        return SenderImageUrl;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public String getSenderName() {
        return SenderName;
    }

    public int getMessageType() {
        return MessageType;
    }

    public String getMessage() {
        return Message;
    }

    public int getMessageFromId() {
        return MessageFromId;
    }

    public void setMessageFromId(int messageFromId) {
        MessageFromId = messageFromId;
    }

    public String getMessageDate() {
        return MessageDate;
    }

    public void setMessageDate(String messageDate) {
        MessageDate = messageDate;
    }
}
