package adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.enise.bitirme_2.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

import adapters.util.DeleteCourse;
import adapters.util.MessageShare;
import adapters.util.PopupWindow;
import controllers.CourseEdit;
import controllers.CourseUpload;
import controllers.UserProfil;
import models.trainer.TrainerCourse;
import models.user.UserLikes;
import utils.AsyncResponse;
import adapters.util.ImageDownloaderTask;
import utils.components.MyAlertDialog;
import utils.server.ServerGET;
import utils.server.ServerPOST;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

import static androidx.core.content.ContextCompat.startActivity;

public class TrainerCourseAdapter extends RecyclerView.Adapter<TrainerCourseAdapter.PostsViewHolder> implements AsyncResponse {
    private static ArrayList<TrainerCourse> mUrunlerList;
    private  OnItemClickListener mListener;
    private View _v;
    private int _userId;
    private int _courseId;
    private TrainerCourseAdapter.PostsViewHolder _postsViewHolder;
    private static TrainerCourse course;
    private String mPage;
    private static final int VIEV_TYPE_EDIT = 1;
    private static final int VIEW_TYPE_NORMAL = 2;
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
        public RoundedImageView btnEditCourseDelete;
        public RoundedImageView btnEditCourseEdit;
        public TextView txtEditCourseName;
        public TextView txtEditCourseDetail;
        public TextView txtSeeCount;
        public PostsViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType == VIEW_TYPE_NORMAL){
                mHomeTrainerImage = itemView.findViewById(R.id.homeTrainerImage);
                txtHomeTrainerName = itemView.findViewById(R.id.txtHomeTrainerName);
                txtHomeTrainerDetail = itemView.findViewById(R.id.txtHomeTrainerDetail);
                btnHomeShare = itemView.findViewById(R.id.btnHomeShare);
                btnHomeLike = itemView.findViewById(R.id.btnHomeLike);
                btnHomeStartChat = itemView.findViewById(R.id.btnHomeStartChat);
                txtLikesCount = itemView.findViewById(R.id.txtHomeLikesCount);
                txtSeeCount = itemView.findViewById(R.id.txtSeeCount);
            }else if(viewType == VIEV_TYPE_EDIT){
                btnEditCourseDelete = itemView.findViewById(R.id.btnEditCourseDelete);
                btnEditCourseEdit = itemView.findViewById(R.id.btnEditCourseEdit);
                txtEditCourseName = itemView.findViewById(R.id.txtEditCourseName);
                txtEditCourseDetail = itemView.findViewById(R.id.txtEditCourseDetail);
            }
        }
    }
    public TrainerCourseAdapter(ArrayList<TrainerCourse> exampleList, String page) {
        mPage = page;
        mUrunlerList = exampleList;
    }
    @Override
    public TrainerCourseAdapter.@NotNull PostsViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == VIEW_TYPE_NORMAL){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_course_items, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_course_item, parent, false);
        }
        return new PostsViewHolder(v,viewType);
    }
    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(TrainerCourseAdapter.@NotNull PostsViewHolder holder, int position) {
        TrainerCourse currentItem = mUrunlerList.get(position);
        if(mPage.equals("Home")) {
            _postsViewHolder = holder;
            if(getItemCount() >= 1)  {
                holder.txtSeeCount.setText(String.valueOf(currentItem.getmSeeCount()));
                ServerGET isLikeCourseAsync = new ServerGET(TransactionTypes.isUserCourseLikeControl, holder);
                isLikeCourseAsync.delegate = this;
                isLikeCourseAsync.execute(URLs.isUserLikeCourse(
                        StaticData.getUserData().getUserId(),
                        currentItem.getmId()
                ));
                String detail = currentItem.getmDetail().length() > 100 ? currentItem.getmDetail().substring(0, 100) + "..." : currentItem.getmDetail();
                holder.txtHomeTrainerDetail.setText(detail);
                holder.txtHomeTrainerName.setText(currentItem.getmName());
                holder.txtLikesCount.setText(String.format("%d user like this course", currentItem.getmLikeCount()));
                holder.btnHomeStartChat.setOnClickListener(v -> {
                    new PopupWindow(holder.itemView, holder.itemView.getContext(), currentItem, TransactionTypes.USER_SEE_MAIN, TransactionTypes.LAYOUT_MESSAGE_SEND).onButtonShowPopupWindowClick();
                });
                holder.btnHomeLike.setOnClickListener(v -> {
                    _postsViewHolder = holder;
                    _v = v;
                    if (course == null || course != mUrunlerList.get(holder.getAdapterPosition())) {
                        course = mUrunlerList.get(holder.getAdapterPosition());
                    }
                    ServerPOST postLike = new ServerPOST(null, TransactionTypes.doAddCourseLike, "Send likes..");
                    postLike.delegate = this;
                    postLike.execute(URLs.SendUserCourseLike(StaticData.getUserData().getUserId()), new Gson()
                            .toJson(new UserLikes(0, StaticData.getUserData().getUserId(), course.getmId())));
                    this._userId = StaticData.getUserData().getUserId();
                    this._courseId = course.getmId();
                    holder.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                });
                holder.btnHomeShare.setOnClickListener(v -> {
                    new MessageShare(v, v.getContext(), currentItem, 1, TransactionTypes.LAYOUT_MESSAGE_SHARE).onButtonShowPopupWindowClick();
                });
                holder.mHomeTrainerImage.setOnClickListener(v -> {
                    startActivity(v.getContext(), new Intent(v.getContext(), UserProfil.class).putExtra("userId", currentItem.getmUserId()), Bundle.EMPTY);
                });
                new ImageDownloaderTask(holder.mHomeTrainerImage).execute(currentItem.getmTrainerImage());
            }
        }else if(mPage.equals("Edit")){
            holder.txtEditCourseName.setText(currentItem.getmName());
            holder.txtEditCourseDetail.setText(currentItem.getmDetail());
            holder.btnEditCourseDelete.setOnClickListener(v -> {
                new MyAlertDialog(v.getContext(),"Course delete process","Do you confirm that the deletion is irreversible?", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay,((dialog, which) -> {
                            DeleteCourse _delete = new DeleteCourse(currentItem,v.getContext(),this,mUrunlerList);
                            _delete.Delete();
                        }))
                        .setNegativeButton(R.string.hayir,(((dialog, which) -> {
                            //
                        })))
                        .show();
            });
            holder.btnEditCourseEdit.setOnClickListener(v->{
                startActivity(v.getContext(),
                        new Intent(v.getContext(), CourseUpload.class)
                                .putExtra("userSee",TransactionTypes.TRAINER_SEE_COURSE_EDIT)
                                .putExtra("userId",StaticData.getUserData().getUserId())
                                .putExtra("course", new Gson().toJson(currentItem))
                        ,null);
            });
        }
    }
    @Override
    public int getItemCount() {
        return mUrunlerList.size();
    }
    public void setItems(ArrayList<TrainerCourse> trainerCourses) {
        mUrunlerList = trainerCourses;
    }
    @Override
    public int getItemViewType(int position) {
        return mUrunlerList.get(position).getmViewType();
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
