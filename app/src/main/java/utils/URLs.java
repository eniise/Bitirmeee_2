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
    public static String GetChatContent(int userId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/GetMyChatContent?userId="+userId;
    }
    public static String GetCurrentLastMessage(int userId,int receiverId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/GetMyCurrentLastMessage?userId="+userId+"&receiverId="+receiverId;
    }
    public static String GetChatDetail(int userId,int receiverId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/GetChatDetail?userId="+userId+"&receiverId="+receiverId;
    }
    public static String SendMessage(){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/SendMyMessage";
    }
    public static String DeleteMyMessage(int userId,int receiverId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/DeleteMyMessage?userId="+userId+"&receiverId="+receiverId;
    }
    public static String SearchUser(String userName,int userId){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/SearchUser?userName="+userName+"&userId="+userId;
    }
    public static String SendMessageWithCourse(){
        return ServerInfo.Host +
                ServerInfo.PortNumber +
                ServerInfo.API_LEVEL +
                "User/SendMyMessage";
    }
}
