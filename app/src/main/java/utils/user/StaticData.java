package utils.user;

import models.user.User;

public final class StaticData {
    private static User userData;

    public static void setUserData(User data){
        userData = data;
    }

    public static User getUserData(){
        return userData;
    }
}
