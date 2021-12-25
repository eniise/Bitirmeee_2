package utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import adapters.TrainerCourseAdapter;

public class ServerPOST extends AsyncTask<String, String, String> {
    private ProgressDialog progressDialog;
    private final int transactionType;
    public AsyncResponse delegate = null;
    @SuppressLint("StaticFieldLeak")
    private TrainerCourseAdapter.PostsViewHolder postsViewHolder;
    public ServerPOST(ProgressDialog progressDialog, int transactionType, String islem) {
        if(progressDialog != null) {
            this.progressDialog = progressDialog;
            this.progressDialog.setMessage(islem + ", lütfen bekleyiniz.");
        }
        this.transactionType = transactionType;
    }
    public ServerPOST(TrainerCourseAdapter.PostsViewHolder v, int transactionType){
        this.transactionType = transactionType;
        this.postsViewHolder = v;
    }
    public ServerPOST(int transactionType){
        this.transactionType = transactionType;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressDialog != null) {
            progressDialog.setMessage("Kayıt tamamlanıyor..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL (strings[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            if(transactionType != TransactionTypes.doUserDeleteMessage) {
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = strings[1].getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                //System.out.println(response.toString());
                return response.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    protected void onPostExecute(String result) {
        if(progressDialog != null){
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        if(result != null) {
            if (result.equals("true")) {
                switch (transactionType) {
                    case TransactionTypes.doRegister:
                        try {
                            delegate.processFinish("true");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case TransactionTypes.doAddCourseLike:
                        try {
                            delegate.processFinish("Like_added");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case TransactionTypes.doUnlikeCourse:
                        ArrayList<Object> _temp = new ArrayList<>();
                        _temp.add(true);
                        _temp.add(postsViewHolder);
                        try {
                            delegate.processFinish(_temp);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case TransactionTypes.doSendMessage:
                        try {
                            delegate.processFinish(true);
                            System.out.println("your message has been send.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    break;
                    case TransactionTypes.doUserDeleteMessage:
                        try {
                            delegate.processFinish(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            else {
                try {
                    delegate.processFinish("false");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                delegate.processFinish("false");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
