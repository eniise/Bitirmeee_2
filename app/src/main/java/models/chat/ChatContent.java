package models.chat;

import com.google.gson.annotations.SerializedName;

public class ChatContent {
    @SerializedName("id")
    private int ChatContentId;
    @SerializedName("userProfileImageUrl")
    private String UserProfileImageUrl;
    @SerializedName("userName")
    private String UserName;
    @SerializedName("lastMessage")
    private String LastMessage;
    @SerializedName("receiverId")
    private int ReceiverId;
    @SerializedName("lastMessageId")
    private int LastMessageId;
    @SerializedName("lastMessageTime")
    private String LastMessageTime;
    @SerializedName("startChatDate")
    private String StartChatDate;
    @SerializedName("isUserSearch")
    private int isUserSearch;
    public ChatContent(int chatContentId, String userProfileImageUrl, String userName, String lastMessage, int receiverId, int lastMessageId, String lastMessageTime, String startChatDate, int isUserSearch) {
        ChatContentId = chatContentId;
        UserProfileImageUrl = userProfileImageUrl;
        UserName = userName;
        LastMessage = lastMessage;
        ReceiverId = receiverId;
        LastMessageId = lastMessageId;
        LastMessageTime = lastMessageTime;
        StartChatDate = startChatDate;
        this.isUserSearch = isUserSearch;
    }


    public int getChatContentId() {
        return ChatContentId;
    }

    public void setChatContentId(int chatContentId) {
        ChatContentId = chatContentId;
    }

    public String getUserProfileImageUrl() {
        return UserProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        UserProfileImageUrl = userProfileImageUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public int getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(int receiverId) {
        ReceiverId = receiverId;
    }

    public int getLastMessageId() {
        return LastMessageId;
    }

    public void setLastMessageId(int lastMessageId) {
        LastMessageId = lastMessageId;
    }

    public String getLastMessageTime() {
        return LastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        LastMessageTime = lastMessageTime;
    }

    public String getStartChatDate() {
        return StartChatDate;
    }

    public void setStartChatDate(String startChatDate) {
        StartChatDate = startChatDate;
    }

    public int isUserSearch() {
        return isUserSearch;
    }

    public void setUserSearch(int userSearch) {
        isUserSearch = userSearch;
    }
}
