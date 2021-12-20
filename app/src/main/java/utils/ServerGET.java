package utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import models.TrainerCourse;
import models.User;

public class ServerGET extends AsyncTask<String, String, String> {
    public ProgressDialog progressDialog;
    private final int transactionType;
    public AsyncResponse delegate = null;
    public ServerGET(@Nullable View rootView,ProgressDialog progressDialog, int transactionType, String islem){
        this.progressDialog = progressDialog;
        this.progressDialog.setMessage(islem+", lütfen bekleyiniz.");
        this.transactionType = transactionType;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Giriş yapılıyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if(result != null) {
            if (result.length() > 0) {
                switch (transactionType) {
                    case TransactionTypes.doLogin:
                        User u = new Gson().fromJson(result, User.class);
                        StaticData.setUserData(new User(u.getUserId(), u.getMail(), u.getName(), u.getSurName(), u.getPassword(), u.getRoleID()));
                        delegate.processFinish("true");
                        break;
                    case TransactionTypes.doGetCourses:
                        TrainerCourse[] courses = new Gson().fromJson(result,TrainerCourse[].class);
                        ArrayList<TrainerCourse> tempList = new ArrayList<>(Arrays.asList(courses));
                        delegate.processFinish(tempList);
                    break;
                }
            } else {
                delegate.processFinish("false");
            }
        } else {
            delegate.processFinish("false");
        }
    }
}

