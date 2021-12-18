package utils;

import models.User;

public final class StaticData {
    private static User userData;
    public static void setUserData(User data){
        userData = data;
    }
    public static User getUserData(){
        return userData;
    }
}
