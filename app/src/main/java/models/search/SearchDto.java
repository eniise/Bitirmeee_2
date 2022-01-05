package models.search;

import com.google.gson.annotations.SerializedName;

public class SearchDto {
    @SerializedName("max")
    private float max;
    @SerializedName("min")
    private float min;
    @SerializedName("courseName")
    private String courseName;
    @SerializedName("place")
    private String place;
    @SerializedName("district")
    private String district;

    public SearchDto(float max, float min, String courseName, String place, String district) {
        this.max = max;
        this.min = min;
        this.courseName = courseName;
        this.place = place;
        this.district = district;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getPlace() {
        return place;
    }

    public String getDistrict() {
        return district;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
