package utils;

public class URLs {
    public static final String LoginURL = ServerInfo.Host+
                                          ServerInfo.PortNumber+
                                          ServerInfo.API_LEVEL+"User/LoginUser?";
    public static final String RegisterURL = ServerInfo.Host+
                                             ServerInfo.PortNumber+
                                             ServerInfo.API_LEVEL+"User/RegisterUser";
    public static String GetCourses(int userID){
        return new String(ServerInfo.Host+
                ServerInfo.PortNumber+
                ServerInfo.API_LEVEL+"TrainerCourse/GetCourses?queryID="+userID);
    }
    public static String GetCourseLikes(int queryId,int courseId){
        return new String (ServerInfo.Host+
                ServerInfo.PortNumber+
                ServerInfo.API_LEVEL+
                "TrainerCourse/GetCourseLikes?queryID="+queryId+"&courseId="+courseId);
    }
}
