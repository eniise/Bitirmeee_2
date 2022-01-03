package adapters.util;

import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import adapters.TrainerCourseAdapter;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerPOST;

public class DeleteCourse implements AsyncResponse {
    private TrainerCourse course;
    private Context context;
    private TrainerCourseAdapter adapter;
    private ArrayList<TrainerCourse> _lst;
    public DeleteCourse(TrainerCourse course,Context context,TrainerCourseAdapter adapter,ArrayList<TrainerCourse> lst){
        this.course = course;
        this.context = context;
        this.adapter = adapter;
        this._lst = lst;
    }
    public void Delete(){
        ServerPOST postDelete = new ServerPOST(TransactionTypes.doTrainerDeleteMyCourse);
        postDelete.delegate = this;
        postDelete.execute(URLs.DeleteMyCourse(),new Gson().toJson(course));
    }
    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            if(result.equals("true")){
                new MyAlertDialog(context,"Successfully","Course has been deleted.", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            _lst.remove(course);
                            adapter.setItems(_lst);
                            adapter.notifyDataSetChanged();
                        }))
                        .show();
            }else {
                new MyAlertDialog(context,"Successfully","Course delete process error.", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                        }))
                        .show();
            }
        }
    }
}
