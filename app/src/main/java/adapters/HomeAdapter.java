package adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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

import org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import java.util.ArrayList;

import models.TrainerCourse;
import models.UserLikes;
import utils.AsyncResponse;
import utils.MyAlertDialog;
import utils.ServerGET;
import utils.ServerPOST;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.PostsViewHolder> implements AsyncResponse {
    private static ArrayList<TrainerCourse> mUrunlerList;
    private  OnItemClickListener mListener;
    private View _v;
    private int userId;
    private int courseId;
    private HomeAdapter.PostsViewHolder postsViewHolder;
    @Override
    public <T> void processFinish(T result) {
        //get process finished class type
        //if user like course && finished class type == holder
        if(mGetClassType(result) == HomeAdapter.PostsViewHolder.class){
            HomeAdapter.PostsViewHolder holder = (HomeAdapter.PostsViewHolder) result;
            holder.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        //else if user not like course && finished class type == arraylist
        else if(mGetClassType(result)  == ArrayList.class){
            ArrayList<T> _tmp = (ArrayList<T>) result;
            boolean _status = (boolean) _tmp.get(0);
            HomeAdapter.PostsViewHolder holder = (HomeAdapter.PostsViewHolder) _tmp.get(1);
            if(!_status)
                holder.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_null_24);
        }
        //else if user liked course and question show "unliked it?" && finished class type == string
        else if(mGetClassType(result) == String.class){
            if (String.valueOf(result).equals("false")) {
                new MyAlertDialog(_v.getContext(),"Already Liked","Unlike it?",R.drawable.ic_baseline_info_24)
                        .ShowMessage()
                        .setNegativeButton("No",(dialog, which) -> {

                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            new UserUnLikeCourse(userId,courseId,postsViewHolder).Unlike();
                        }).show();
            }
        }

    }
    private Object mGetClassType(Object object){
        return object.getClass();
    }
    //unlike course and  real-time ui changed
    private class UserUnLikeCourse implements AsyncResponse{
        private final int userId;
        private final int courseId;
        private final HomeAdapter.PostsViewHolder holder;
        public UserUnLikeCourse(int userId,int courseId,HomeAdapter.PostsViewHolder holder){
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
                PostsViewHolder v = (HomeAdapter.PostsViewHolder) _temp.get(1);
                System.out.println(_result);
                if(_result){
                    v.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_null_24);
                }else {
                    v.btnHomeLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mHomeTrainerImage;
        public TextView txtHomeTrainerName;
        public TextView txtHomeTrainerDetail;
        public ImageButton btnHomeShare;
        public ImageButton btnHomeLike;
        public ImageButton btnHomeStartChat;
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
    public HomeAdapter(ArrayList<TrainerCourse> exampleList) {
        mUrunlerList = exampleList;
    }
    @Override
    public HomeAdapter.@NotNull PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_content_items, parent, false);
        return new PostsViewHolder(v);
    }
    @Override
    public void onBindViewHolder(HomeAdapter.@NotNull PostsViewHolder holder, int position) {
        TrainerCourse currentItem = mUrunlerList.get(position);
        postsViewHolder = holder;
        //test image downloader task method from url
        //@DownloadImageTask class
        ServerGET isLikeCourseAsync = new ServerGET(TransactionTypes.isUserCourseLikeControl,holder);
        isLikeCourseAsync.delegate = this;
        isLikeCourseAsync.execute(URLs.isUserLikeCourse(
                StaticData.getUserData().getUserId(),
                currentItem.getmId()
        ));
        new DownloadImageTask(holder.mHomeTrainerImage).execute("https://img.favpng.com/14/3/22/stock-photography-computer-icons-user-png-favpng-TWgLj8kmcdnekcpWySfpV97h3.jpg");
        String detail = currentItem.getmDetail().length() > 100 ? currentItem.getmDetail().substring(0,100)+"..." : currentItem.getmDetail();
        holder.txtHomeTrainerDetail.setText(detail);
        holder.txtHomeTrainerName.setText(currentItem.getmName());
        holder.txtLikesCount.setText(String.valueOf(currentItem.getmLikeCount()));
        holder.btnHomeStartChat.setOnClickListener(v -> {

        });
        holder.btnHomeLike.setOnClickListener(v -> {
            postsViewHolder = holder;
            _v = v;
            TrainerCourse course = mUrunlerList.get(holder.getAdapterPosition());
            ServerPOST postLike = new ServerPOST(null, TransactionTypes.doAddCourseLike,"Beğeni gönderiliyor..");
            postLike.delegate = this;
            postLike.execute(URLs.SendUserCourseLike(
                    StaticData.getUserData().getUserId()
            ),new Gson().toJson(new UserLikes(
                    0,
                    StaticData.getUserData().getUserId(),
                    course.getmId()
                    )));
            this.userId = StaticData.getUserData().getUserId();
            this.courseId = course.getmId();
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
