package models.user;

import com.google.gson.annotations.SerializedName;

public class ForgotPassword {
    @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private int UserId;
    @SerializedName("code")
    private String code;
    @SerializedName("date")
    private String date;
    @SerializedName("password")
    private String password;

    public ForgotPassword(int id, int userId, String code, String date, String password) {
        this.id = id;
        this.UserId = userId;
        this.code = code;
        this.date = date;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return UserId;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
