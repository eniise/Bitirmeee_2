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

import adapters.HomeAdapter;
import models.TrainerCourse;
import models.User;

public class ServerGET extends AsyncTask<String, String, String> {
    public ProgressDialog progressDialog;
    private final int transactionType;
    public AsyncResponse delegate = null;
    private HomeAdapter.PostsViewHolder holder;
    public ServerGET(ProgressDialog progressDialog, int transactionType, String islem){
        this.progressDialog = progressDialog;
        this.progressDialog.setMessage(islem+", please wait..");
        this.transactionType = transactionType;
    }
    public ServerGET(int transactionType, HomeAdapter.PostsViewHolder holder){
        this.transactionType = transactionType;
        this.holder = holder;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressDialog != null) {
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
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
        if(progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
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
                    case TransactionTypes.doGetLikesCount:
                        //null
                    break;
                    case TransactionTypes.isUserCourseLikeControl:
                        if(result.equals("true") || result.equals("true\n"))
                            delegate.processFinish(holder);
                        else
                        {
                            ArrayList<Object> _tmp = new ArrayList<Object>();
                            _tmp.add(false);
                            _tmp.add(holder);
                            delegate.processFinish(_tmp);
                        }
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

