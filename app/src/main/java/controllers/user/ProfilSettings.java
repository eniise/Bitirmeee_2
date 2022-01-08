package controllers.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.permission.VerifiyPermissions;
import utils.server.ServerPOST;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.user.upload.ChangeImageClient;
import utils.user.upload.UploadService;

public class ProfilSettings extends AppCompatActivity implements AsyncResponse {
    private EditText txtPassword1;
    private EditText txtPassword2;
    private Button btnChange;
    Context context;
    private RoundedImageView profileSettingsBack;
    private RoundedImageView imgPreview;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    Button btnChangeImage;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page_settings);
        context = this;
        init();
        VerifiyPermissions.verifyStoragePermissions(this);
    }
    @SuppressLint("IntentReset")
    void init(){
        txtPassword1 = findViewById(R.id.txtRePassword);
        txtPassword2 = findViewById(R.id.txtRePassword2);
        btnChange = findViewById(R.id.btnChange);
        imgPreview = findViewById(R.id.imgPreview);
        profileSettingsBack = findViewById(R.id.profileSettingsBack);
        profileSettingsBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnChangeImage = findViewById(R.id.btnChangeImage);
        btnChangeImage.setOnClickListener(v->{
            if(!btnChangeImage.getText().toString().equals("Upload My Photo")) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }else {
                UploadMyPhoto();
            }
        });
        btnChange.setOnClickListener(v -> {
            if(txtPassword1.getText().length() > 0){
                if(txtPassword2.getText().length() > 0){
                    if(txtPassword2.getText().toString().toLowerCase().equals(txtPassword1.getText().toString().toLowerCase())){
                        StaticData.getUserData().setPassword(txtPassword2.getText().toString());
                        ServerPOST changePassword = new ServerPOST(TransactionTypes.doUserChangePassword);
                        changePassword.delegate = this;
                        changePassword.execute(URLs.ChangePassword(), new Gson().toJson(StaticData.getUserData()));
                    }
                }
            }
        });
    }

    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            if(result.toString().equals("true")){
                new MyAlertDialog(context,"Password change successfully","Your password has been changed.",R.drawable.ic_message_info)
                .ShowMessage()
                .setPositiveButton(R.string.okay,((dialog, which) -> {
                    //..
                })).show();
            }else {
                new MyAlertDialog(context,"Error","Error when changing your password.",R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay,((dialog, which) -> {
                            //..
                        })).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                photo = BitmapFactory.decodeFile(picturePath);
                imgPreview.setImageBitmap(photo);
                btnChangeImage.setText(String.valueOf("Upload My Photo"));
            }
    }
    private void UploadMyPhoto(){
        File file = new File(picturePath);
        RequestBody photoContent = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file",file.getName(),photoContent);
        MultipartBody.Part userId = MultipartBody.Part.createFormData("userId",String.valueOf(StaticData.getUserData().getUserId()));
        UploadService uploadService = ChangeImageClient.getClient().create(UploadService.class);
        uploadService.Upload(photo,userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Successfully",Toast.LENGTH_LONG).show();
            }
        });
    }
}
