package adapters;

import android.annotation.SuppressLint;
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
import org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import java.util.ArrayList;

import models.TrainerCourse;
import utils.ServerGET;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.PostsViewHolder> {
    private ArrayList<TrainerCourse> mUrunlerList;
    private OnItemClickListener mListener;
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
        new DownloadImageTask(holder.mHomeTrainerImage)
                .execute("https://img.favpng.com/14/3/22/stock-photography-computer-icons-user-png-favpng-TWgLj8kmcdnekcpWySfpV97h3.jpg");
        String detail = currentItem.getmDetail().length() > 40 ? currentItem.getmDetail().substring(0,40)+"..." : currentItem.getmDetail();
        holder.txtHomeTrainerDetail.setText(detail);
        holder.txtHomeTrainerName.setText(currentItem.getmName());
        holder.txtLikesCount.setText(String.valueOf(currentItem.getmLikeCount()));
        holder.btnHomeStartChat.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),"Buraya tıklanınca chat açılacak",Toast.LENGTH_SHORT).show();
        });
        holder.btnHomeLike.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),"Buraya tıklanınca gönderi beğenilecek",Toast.LENGTH_SHORT).show();
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
