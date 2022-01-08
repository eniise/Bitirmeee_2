package adapters;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import adapters.util.ImageDownloaderTask;
import adapters.util.MessageShare;
import controllers.pages.ChatPage;
import models.chat.Chat;
import models.chat.ChatContent;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.server.ServerPOST;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class ChatContentAdapter extends RecyclerView.Adapter<ChatContentAdapter.ChatsViewHolder> implements AsyncResponse {
    private static ArrayList<ChatContent> mChatContents;
    private static OnItemClickListener mListener;
    private final String userChatType;
    private static MessageShare mMessageShare;
    private TrainerCourse trainerCourse;
    @Override
    public <T> void processFinish(T result) {
        Toast.makeText(mMessageShare.mView.getContext(),"Your share has been send.",Toast.LENGTH_LONG).show();
        mMessageShare.mPopupWindow.dismiss();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(ChatContentAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    public static class ChatsViewHolder extends RecyclerView.ViewHolder implements AsyncResponse{
        public AppCompatImageView mChatContentReceiverImage;
        public TextView mChatContentReceiverName;
        public TextView mChatContentReceiverLastMessage;
        public AppCompatImageView mChatContentSendMessage;
        public TextView txtMessageDate;
        private String mUserChatType;
        public ChatsViewHolder(View itemView,OnItemClickListener listener,String userChatType) {
            super(itemView);
            mChatContentReceiverImage = itemView.findViewById(R.id.receiverChatContentImage);
            mChatContentReceiverName = itemView.findViewById(R.id.txtChatContentReceiverName);
            mChatContentReceiverLastMessage = itemView.findViewById(R.id.txtChatContentReceiverLastMessage);
            mChatContentSendMessage = itemView.findViewById(R.id.btnChatContentSendMessage);
            txtMessageDate = itemView.findViewById(R.id.txtMessageDate);
            this.mUserChatType = userChatType;
        }
        public ChatsViewHolder(View itemView,OnItemClickListener listener,int viewType,String userChatType){
            super(itemView);
            if(viewType == TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH) {
                mChatContentReceiverImage = itemView.findViewById(R.id.receiverChatContentImage);
                mChatContentReceiverName = itemView.findViewById(R.id.txtChatContentReceiverName);
                mChatContentReceiverLastMessage = itemView.findViewById(R.id.txtChatContentReceiverLastMessage);
                mChatContentSendMessage = itemView.findViewById(R.id.btnChatContentSendMessage);
                txtMessageDate = itemView.findViewById(R.id.txtMessageDate);
                this.mUserChatType =userChatType;
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void SetData(ChatContent contents, TrainerCourse trainerCourse){
            new ImageDownloaderTask(mChatContentReceiverImage).execute(URLs.GetPhoto(contents.getReceiverId()));
            mChatContentReceiverName.setText(String.valueOf(contents.getUserName()));
            mChatContentReceiverLastMessage.setText(String.valueOf(contents.getLastMessage()));
            String _time = DetectSendTime(contents.getLastMessageTime());
            txtMessageDate.setText(_time);
            mChatContentSendMessage.setOnClickListener(v -> {
                if(mUserChatType.equals(TransactionTypes.LAYOUT_MESSAGE_NORMAL)) {
                    v.getContext().startActivity(new Intent(v.getContext(), ChatPage.class)
                    .putExtra("receiverId", contents.getReceiverId())
                    .putExtra("lastMessage", contents.getLastMessage())
                    .putExtra("lastMessageId", contents.getLastMessageId())
                    .putExtra("receiverImage", contents.getUserProfileImageUrl())
                    .putExtra("receiverName", contents.getUserName())
                    .putExtra("startChatDate", contents.getStartChatDate()));
                }else {
                    ServerPOST sendMessage = new ServerPOST(TransactionTypes.doUserSendMessageWithCourse);
                    sendMessage.delegate = this;
                    Chat _chat = new Chat(0,
                            contents.getReceiverId(),
                            StaticData.getUserData().getUserId(),
                            2,"I am sharing the "+trainerCourse.getmName()+" course with you. Click and browse",
                            getDateTime(),
                            StaticData.getUserData().getUserId(), trainerCourse.getmId());
                    sendMessage.execute(URLs.SendMessageWithCourse(),new Gson().toJson(_chat));
                }
            });
        }

        @Override
        public <T> void processFinish(T result) {

            Toast.makeText(mMessageShare.mView.getContext(),"Your share has been send.",Toast.LENGTH_LONG).show();
            mMessageShare.mPopupWindow.dismiss();
        }
    }
    public ChatContentAdapter(ArrayList<ChatContent> exampleList,String userChatType) {
        mChatContents = exampleList;
        this.userChatType = userChatType;
    }
    public ChatContentAdapter(ArrayList<ChatContent> exampleList, String userChatType, TrainerCourse trainerCourse, MessageShare share) {
        mChatContents = exampleList;
        this.userChatType = userChatType;
        this.trainerCourse = trainerCourse;
        this.mMessageShare = share;
    }
    @Override
    public ChatContentAdapter.@NotNull ChatsViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);
        if(viewType == TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH){
            return new ChatsViewHolder(v,mListener,viewType,userChatType);
        }else {
            return new ChatsViewHolder(v,mListener,userChatType);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ChatContentAdapter.@NotNull ChatsViewHolder holder, int position) {
        ChatContent mCurrentItem = mChatContents.get(position);
        if(mCurrentItem.isUserSearch() == 0) {
            new ImageDownloaderTask(holder.mChatContentReceiverImage).execute(URLs.GetPhoto(mCurrentItem.getReceiverId()));
            holder.mChatContentReceiverName.setText(String.valueOf(mCurrentItem.getUserName()));
            holder.mChatContentReceiverLastMessage.setText(String.valueOf(mCurrentItem.getLastMessage()));
            String _time = DetectSendTime(mCurrentItem.getLastMessageTime());
            holder.txtMessageDate.setText(_time);
            holder.mChatContentSendMessage.setOnClickListener(v -> {
                if(userChatType.equals(TransactionTypes.LAYOUT_MESSAGE_NORMAL)) {
                    v.getContext().startActivity(new Intent(v.getContext(), ChatPage.class)
                            .putExtra("receiverId", mCurrentItem.getReceiverId())
                            .putExtra("lastMessage", mCurrentItem.getLastMessage())
                            .putExtra("lastMessageId", mCurrentItem.getLastMessageId())
                            .putExtra("receiverImage", mCurrentItem.getUserProfileImageUrl())
                            .putExtra("receiverName", mCurrentItem.getUserName())
                            .putExtra("startChatDate", mCurrentItem.getStartChatDate()));
                }else {
                    ServerPOST sendMessage = new ServerPOST(TransactionTypes.doUserSendMessageWithCourse);
                    sendMessage.delegate = this;
                    Chat _chat = new Chat(0,
                            mCurrentItem.getReceiverId(),
                            StaticData.getUserData().getUserId(),
                            2,"I am sharing the "+trainerCourse.getmName()+" course with you. Click and browse",
                            getDateTime(),
                            StaticData.getUserData().getUserId(), trainerCourse.getmId());
                    sendMessage.execute(URLs.SendMessageWithCourse(),new Gson().toJson(_chat));
                }
            });
        }else {
            holder.SetData(mChatContents.get(position),trainerCourse);
        }
    }
    @Override
    public int getItemCount() {
        return mChatContents.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatContents.get(position).isUserSearch() == 1) {
            return TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH;
        }
        else
            return TransactionTypes.USER_CHAT_CONTENT_ALREADY_EXIST;
    }

    private static String DetectSendTime(String lastMessageTime){
        String _temp = lastMessageTime.substring(lastMessageTime.indexOf("T")+1,lastMessageTime.length()-1);
        String[] _arr = _temp.split(":");
        return _arr[0]+":"+_arr[1]+"";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String getDateTime(){
        java.util.Date date = new java.util.Date();
        LocalDateTime localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        DateTimeFormatter _formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return _formater.format(localDate);
    }
}
