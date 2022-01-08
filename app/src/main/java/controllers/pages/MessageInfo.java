package controllers.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.enise.bitirme_2.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.server.ServerPOST;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class MessageInfo extends AppCompatActivity implements View.OnClickListener, AsyncResponse {
    private Bitmap image;
    private TextView txtStartChatDate;
    private TextView txtUserName;
    private RoundedImageView imgUser;
    private String mUserProfilUrl;
    private int mReceiverId;
    private AppCompatImageView mDeleteMessage;
    private AppCompatImageView imageInfoBack;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail_message_info);
        init();
        System.out.println(mUserProfilUrl);
        new BitmapTask().execute(mUserProfilUrl);
    }
    private void init(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        imgUser = findViewById(R.id.roundInfoUserProfile);
        txtUserName = findViewById(R.id.txtInfoUserName);
        txtStartChatDate = findViewById(R.id.txtInfoMessage);
        mDeleteMessage = findViewById(R.id.deleteMessages);
        mDeleteMessage.setOnClickListener(this::onClick);
        txtUserName.setText(bundle.getString("userName"));
        txtStartChatDate.setText(getStartChatInfo(bundle.getString("startChatDate")));
        mReceiverId = bundle.getInt("receiverUserId");
        mUserProfilUrl = bundle.getString("userProfil");
        imageInfoBack = findViewById(R.id.imageInfoBack);
        imageInfoBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
    private String getStartChatInfo(String date){
        return "You'r start with date : "+date;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.deleteMessages){
            new MyAlertDialog(v.getContext(),"Are you sure?","Deletion cannot be undone.",R.drawable.ic_round_delete_24)
                    .ShowMessage()
                    .setNegativeButton(R.string.hayir, (dialog, which) -> {
                        //user enter NO
                    })
                    .setPositiveButton(R.string.okay, ((dialog, which) -> {
                        //if user enter yes.
                        ServerPOST _post = new ServerPOST(TransactionTypes.doUserDeleteMessage);
                        _post.delegate = this;
                        _post.execute(URLs.DeleteMyMessage(StaticData.getUserData().getUserId(),mReceiverId));

                    }))
                    .show();
        }
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == Boolean.class) {
            if ((boolean) result) {
                new MyAlertDialog(this, "Done.", "Your messages has been deleted.", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay, ((dialog, which) -> {
                            startActivity(new Intent(this, Main.class)
                                    .putExtra(TransactionTypes.USER_COME_BACK,
                                            TransactionTypes.USER_DELETE_MESSAGE));
                            finish();
                        }))
                        .show();
            } else {
                new MyAlertDialog(this, "Error.", "Please try again.", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setPositiveButton(R.string.okay, ((dialog, which) -> {
                            //try again..
                        }))
                        .show();
            }
        }else {
            new MyAlertDialog(this, "Error.", "Please try again.", R.drawable.ic_message_info)
                    .ShowMessage()
                    .setPositiveButton(R.string.okay, ((dialog, which) -> {
                        //try again..
                    }))
                    .show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class BitmapTask extends AsyncTask<String, Bitmap, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            InputStream input = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        protected void onPostExecute(Bitmap result) {
            imgUser.setImageBitmap(result);
        }
    }
}
