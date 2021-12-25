package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.enise.bitirme_2.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

import models.TrainerCourse;
import models.UserLikes;
import utils.AsyncResponse;
import adapters.util.ImageDownloaderTask;
import utils.MyAlertDialog;
import utils.ServerGET;
import utils.ServerPOST;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class TrainerCourseAdapter extends RecyclerView.Adapter<TrainerCourseAdapter.PostsViewHolder> implements AsyncResponse {
    private static ArrayList<TrainerCourse> mUrunlerList;
    private  OnItemClickListener mListener;
    private View _v;
    private int _userId;
    private int _courseId;
    private TrainerCourseAdapter.PostsViewHolder _postsViewHolder;
    private static TrainerCourse course;
    private String mPage;
    @Override
    public <T> void processFinish(T result) {
        //if user like course && finished class type == holder set ui favorite
        if(mGetClassType(result) == TrainerCourseAdapter.PostsViewHolder.class){
            TrainerCourseAdapter.PostsViewHolder holder = (TrainerCourseAdapter.PostsViewHolder) result;
            holder.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        //else if user not like course && finished class type == arraylist set ui favorite null
        else if(mGetClassType(result)  == ArrayList.class){
            ArrayList<T> _tmp = (ArrayList<T>) result;
            boolean _status = (boolean) _tmp.get(0);
            TrainerCourseAdapter.PostsViewHolder holder = (TrainerCourseAdapter.PostsViewHolder) _tmp.get(1);
            if(!_status)
                holder.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_null_24);
        }
        //else if user liked course and question show "unliked it?" && finished class type == string
        else if(mGetClassType(result) == String.class){
            //if user like it course question show "unlike it?"
            if (String.valueOf(result).equals("false")) {
                new MyAlertDialog(_v.getContext(),"Already Like it","Unlike it?",R.drawable.ic_baseline_info_24)
                        .ShowMessage()
                        .setNegativeButton("No",(dialog, which) -> {

                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            new UserUnLikeCourse(_userId,_courseId,_postsViewHolder).Unlike();
                        }).show();
            }
            //if user don't like course and like it course
            else if(String.valueOf(result).equals("Like_added")){
                int getLike = course.getmLikeCount()+1;
                _postsViewHolder.txtLikesCount.setText(getLike+" user like this course");
                course.setmLikeCount(getLike);
            }
        }

    }
    private Object mGetClassType(Object object){
        return object.getClass();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mHomeTrainerImage;
        public TextView txtHomeTrainerName;
        public TextView txtHomeTrainerDetail;
        public RoundedImageView btnHomeShare;
        public RoundedImageView btnHomeLike;
        public RoundedImageView btnHomeStartChat;
        public TextView txtLikesCount;
        public PostsViewHolder(View itemView) {
            super(itemView);
            mHomeTrainerImage = itemView.findViewById(R.id.homeTrainerImage);
            txtHomeTrainerName = itemView.findViewById(R.id.txtHomeTrainerName);
            txtHomeTrainerDetail = itemView.findViewById(R.id.txtHomeTrainerDetail);
            btnHomeShare = itemView.findViewById(R.id.btnHomeShare);
            btnHomeLike = itemView.findViewById(R.id.btnHomeLike);
            btnHomeStartChat = itemView.findViewById(R.id.btnHomeStartChat);
            txtLikesCount = itemView.findViewById(R.id.txtHomeLikesCount);
        }
    }
    public TrainerCourseAdapter(ArrayList<TrainerCourse> exampleList, String page) {
        mPage = page;
        mUrunlerList = exampleList;
    }
    @Override
    public TrainerCourseAdapter.@NotNull PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_course_items, parent, false);
        return new PostsViewHolder(v);
    }
    @Override
    public void onBindViewHolder(TrainerCourseAdapter.@NotNull PostsViewHolder holder, int position) {
        TrainerCourse currentItem = mUrunlerList.get(position);
        _postsViewHolder = holder;
        ServerGET isLikeCourseAsync = new ServerGET(TransactionTypes.isUserCourseLikeControl,holder);
        isLikeCourseAsync.delegate = this;
        isLikeCourseAsync.execute(URLs.isUserLikeCourse(
                StaticData.getUserData().getUserId(),
                currentItem.getmId()
        ));
        new ImageDownloaderTask(holder.mHomeTrainerImage).execute("https://img.favpng.com/14/3/22/stock-photography-computer-icons-user-png-favpng-TWgLj8kmcdnekcpWySfpV97h3.jpg");
        String detail = currentItem.getmDetail().length() > 100 ? currentItem.getmDetail().substring(0,100)+"..." : currentItem.getmDetail();
        holder.txtHomeTrainerDetail.setText(detail);
        holder.txtHomeTrainerName.setText(currentItem.getmName());
        holder.txtLikesCount.setText(currentItem.getmLikeCount()+" user like this course");
        holder.btnHomeStartChat.setOnClickListener(v -> {

        });
        holder.btnHomeLike.setOnClickListener(v -> {
            _postsViewHolder = holder;
            _v = v;
            if(course == null || course != mUrunlerList.get(holder.getAdapterPosition())) {
                course = mUrunlerList.get(holder.getAdapterPosition());
            }
            ServerPOST postLike = new ServerPOST(null, TransactionTypes.doAddCourseLike,"Send likes..");
            postLike.delegate = this;
            postLike.execute(URLs.SendUserCourseLike(StaticData.getUserData().getUserId()),new Gson()
                    .toJson(new UserLikes( 0,StaticData.getUserData().getUserId(),course.getmId())));
            this._userId = StaticData.getUserData().getUserId();
            this._courseId = course.getmId();
             holder.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_24);
        });
        holder.btnHomeShare.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),"Buraya tıklanınca gönderi paylaşılacak",Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public int getItemCount() {
        return mUrunlerList.size();
    }


    //unlike course and  real-time ui changed
    private class UserUnLikeCourse implements AsyncResponse{
        private final int userId;
        private final int courseId;
        private final TrainerCourseAdapter.PostsViewHolder holder;
        public UserUnLikeCourse(int userId, int courseId, TrainerCourseAdapter.PostsViewHolder holder){
            this.userId = userId;
            this.courseId = courseId;
            this.holder = holder;
        }
        public void Unlike(){
            ServerPOST _unlikeCourse = new ServerPOST(holder,TransactionTypes.doUnlikeCourse);
            _unlikeCourse.delegate = this;
            _unlikeCourse.execute(URLs.UserUnlikeCourse(userId,courseId),"");
        }
        @Override
        public <T> void processFinish(T result) {
            if(mGetClassType(result) == ArrayList.class){
                ArrayList<T> _temp = (ArrayList<T>) result;
                boolean _result = (boolean) _temp.get(0);
                PostsViewHolder v = (TrainerCourseAdapter.PostsViewHolder) _temp.get(1);
                System.out.println(_result);
                if(_result){
                    v.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_null_24);
                    int getLike = course.getmLikeCount()-1;
                    v.txtLikesCount.setText(getLike+" user like this course");
                    course.setmLikeCount(getLike);
                }else {
                    v.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        }
    }
}
