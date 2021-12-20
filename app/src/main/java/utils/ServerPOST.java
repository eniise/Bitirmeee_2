package utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import models.User;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerPOST extends AsyncTask<String, String, String> {
    public ProgressDialog progressDialog;
    private final int transactionType;
    public AsyncResponse delegate = null;
    private final OkHttpClient client = new OkHttpClient();
    public ServerPOST(ProgressDialog progressDialog, int transactionType, String islem) {
        this.progressDialog = progressDialog;
        this.progressDialog.setMessage(islem+", lütfen bekleyiniz.");
        this.transactionType = transactionType;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Kayıt tamamlanıyor..");
        progressDialog.setCancelable(false);
        progressDialog.show();
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
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = strings[1].getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                return response.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if(result != null) {
            if (result.equals("true")) {
                switch (transactionType) {
                    case TransactionTypes.doRegister:
                        delegate.processFinish("true");
                        break;
                }
            }
            else {
                delegate.processFinish("false");
            }
        }
        else {
            delegate.processFinish("false");
        }
    }
}
