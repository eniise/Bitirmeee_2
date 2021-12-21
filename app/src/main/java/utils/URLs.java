package utils;

public class URLs {
    public static final String LoginURL = ServerInfo.Host+
                                          ServerInfo.PortNumber+
                                          ServerInfo.API_LEVEL+"User/LoginUser?";
    public static final String RegisterURL = ServerInfo.Host+
                                             ServerInfo.PortNumber+
                                             ServerInfo.API_LEVEL+"User/RegisterUser";
    public static String GetCourses(int userID){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL + "TrainerCourse/GetCourses?queryID=" + userID;
    }
    public static String GetCourseLikes(int queryId,int courseId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "TrainerCourse/GetCourseLikes?queryID=" + queryId + "&courseId=" + courseId;
    }
    public static String SendUserCourseLike(int queryId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "TrainerCourse/AddLike?queryId="+queryId;
    }
    public static String isUserLikeCourse(int userId,int courseId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "TrainerCourse/LikeControl?userId="+userId+"&courseId="+courseId;
    }
    public static String UserUnlikeCourse(int userId,int courseId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "TrainerCourse/UnlikeCourse?userId="+userId+"&courseId="+courseId;
    }
    public static String GetUserLikeCourses(int userId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/MyLikeCourse?userId="+userId;
    }
}
