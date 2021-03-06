package models.user;

import com.google.gson.annotations.SerializedName;

public class Profil {
    @SerializedName("id")
    private int mId;
    @SerializedName("userId")
    private int mUserId;
    @SerializedName("name")
    private String mName;
    @SerializedName("types")
    private String mTypes;
    @SerializedName("timeIntervals")
    private String mTimeIntervals;
    @SerializedName("days")
    private String mDays;
    @SerializedName("district")
    private String mDistrict;
    @SerializedName("cost")
    private double mCost;
    @SerializedName("detail")
    private String mDetail;
    @SerializedName("likeCount")
    private int mLikeCount;
    @SerializedName("trainerImage")
    private String mTrainerImage;
    @SerializedName("profileImage")
    private String mProfileImage;
    @SerializedName("userName")
    private String mUserName;
    @SerializedName("userLikeCount")
    private int LikeCount;
    @SerializedName("seeCount")
    private int seeCount;

    public Profil(int mId, int mUserId, String mName, String mTypes, String mTimeIntervals, String mDays, String mDistrict, double mCost, String mDetail, int mLikeCount, String mTrainerImage, String mProfileImage, String mUserName, int likeCount, int seeCount) {
        this.mId = mId;
        this.mUserId = mUserId;
        this.mName = mName;
        this.mTypes = mTypes;
        this.mTimeIntervals = mTimeIntervals;
        this.mDays = mDays;
        this.mDistrict = mDistrict;
        this.mCost = mCost;
        this.mDetail = mDetail;
        this.mLikeCount = mLikeCount;
        this.mTrainerImage = mTrainerImage;
        this.mProfileImage = mProfileImage;
        this.mUserName = mUserName;
        this.LikeCount = likeCount;
        this.seeCount = seeCount;
    }
    public Profil(){

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTypes() {
        return mTypes;
    }

    public void setmTypes(String mTypes) {
        this.mTypes = mTypes;
    }

    public String getmTimeIntervals() {
        return mTimeIntervals;
    }

    public void setmTimeIntervals(String mTimeIntervals) {
        this.mTimeIntervals = mTimeIntervals;
    }

    public String getmDays() {
        return mDays;
    }

    public void setmDays(String mDays) {
        this.mDays = mDays;
    }

    public String getmDistrict() {
        return mDistrict;
    }

    public void setmDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
    }

    public double getmCost() {
        return mCost;
    }

    public void setmCost(double mCost) {
        this.mCost = mCost;
    }

    public String getmDetail() {
        return mDetail;
    }

    public void setmDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public int getmLikeCount() {
        return mLikeCount;
    }

    public void setmLikeCount(int mLikeCount) {
        this.mLikeCount = mLikeCount;
    }

    public String getmTrainerImage() {
        return mTrainerImage;
    }

    public void setmTrainerImage(String mTrainerImage) {
        this.mTrainerImage = mTrainerImage;
    }

    public String getmProfileImage() {
        return mProfileImage;
    }

    public void setmProfileImage(String mProfileImage) {
        this.mProfileImage = mProfileImage;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }

    public int getSeeCount() {
        return seeCount;
    }

    public void setSeeCount(int seeCount) {
        this.seeCount = seeCount;
    }
}
