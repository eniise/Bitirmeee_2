package models.user;

import com.google.gson.annotations.SerializedName;

public class UserLikes {

    @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private int mUserId;
    @SerializedName("courseId")
    private int mCourseId;

    public UserLikes(int id, int mUserId, int mCourseId) {
        this.id = id;
        this.mUserId = mUserId;
        this.mCourseId = mCourseId;
    }
    public UserLikes(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getmCourseId() {
        return mCourseId;
    }

    public void setmCourseId(int mCourseId) {
        this.mCourseId = mCourseId;
    }
}
