package adapters.message;

import models.TrainerCourse;
import utils.AsyncResponse;

public class SendMessageCourse implements AsyncResponse {
    private int mUserId;
    private TrainerCourse mCourse;
    public SendMessageCourse(int userId, TrainerCourse course){
        this.mUserId = userId;
        this.mCourse = course;
    }
    public boolean Send(){
        return true;
    }
    @Override
    public <T> void processFinish(T result) {

    }
}
