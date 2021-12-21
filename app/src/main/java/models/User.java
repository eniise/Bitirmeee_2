package models;

import com.google.gson.annotations.SerializedName;

public final class User {
    @SerializedName("id")
    private int UserId;
    @SerializedName("mail")
    private String Mail;
    @SerializedName("name")
    private String Name;
    @SerializedName("surName")
    private String SurName;
    @SerializedName("password")
    private String Password;
    @SerializedName("roleId")
    private int RoleID;
    @SerializedName("profileImage")
    private String UserProfileImageUrl;
    @SerializedName("gender")
    private boolean UserGender;
    @SerializedName("likesCount")
    private int UserLikesCount;
    public User(int userId, String mail, String name, String surName, String password, int roleID, String userProfileImageUrl, boolean userGender,int userLikesCount){
        UserId = userId;
        Mail = mail;
        Name = name;
        SurName = surName;
        Password = password;
        RoleID = roleID;
        UserProfileImageUrl = userProfileImageUrl;
        UserGender = userGender;
        UserLikesCount = userLikesCount;
    }
    public User(){

    }
    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }
    public String getPassword(){return Password; }
    public void setPassword(String password){ Password = password; }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        RoleID = roleID;
    }

    public String getUserProfileImageUrl() {
        return UserProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        UserProfileImageUrl = userProfileImageUrl;
    }

    public boolean isUserGender() {
        return UserGender;
    }

    public void setUserGender(boolean userGender) {
        UserGender = userGender;
    }

    public int getUserLikesCount() {
        return UserLikesCount;
    }

    public void setUserLikesCount(int userLikesCount) {
        UserLikesCount = userLikesCount;
    }
}
