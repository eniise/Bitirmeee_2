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
    public User(int userId,String mail,String name,String surName,String password,int roleID){
        UserId = userId;
        Mail = mail;
        Name = name;
        SurName = surName;
        Password = password;
        RoleID = roleID;
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
}
