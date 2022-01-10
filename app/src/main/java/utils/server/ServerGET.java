package utils.server;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import adapters.TrainerCourseAdapter;
import models.chat.ChatContent;
import models.chat.ChatDetail;
import models.chat.CurrentChatLastMessageInfo;
import models.trainer.TrainerCourse;
import models.user.ForgotPassword;
import models.user.User;
import utils.AsyncResponse;
import utils.user.StaticData;
import utils.extras.TransactionTypes;

public class ServerGET extends AsyncTask<String, String, String> {
    public ProgressDialog progressDialog;
    private final int transactionType;
    public AsyncResponse delegate = null;
    private TrainerCourseAdapter.PostsViewHolder holder;
    public ServerGET(ProgressDialog progressDialog, int transactionType, String processName){
        this.progressDialog = progressDialog;
        this.progressDialog.setMessage(processName+", please wait..");
        this.transactionType = transactionType;
    }
    public ServerGET(int transactionType, TrainerCourseAdapter.PostsViewHolder holder){
        this.transactionType = transactionType;
        this.holder = holder;
    }
    public ServerGET(int transactionType){
        this.transactionType = transactionType;
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
            System.out.println(params[0]);
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
                        if(u.getUserId() == 0) {
                            delegate.processFinish("false");
                            break;
                        }
                        StaticData.setUserData(new User(u.getUserId()
                                , u.getMail()
                                , u.getName()
                                , u.getSurName()
                                , u.getPassword()
                                , u.getRoleID()
                                , u.getUserProfileImageUrl()
                                , u.isUserGender()
                                ,u.getUserLikesCount()));
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
                        if(result.equals("true") || result.equals("true\n")) {
                            delegate.processFinish(holder);
                        }
                        else
                        {
                            ArrayList<Object> _tmp = new ArrayList<Object>();
                            _tmp.add(false);
                            _tmp.add(holder);
                                delegate.processFinish(_tmp);

                        }
                    break;
                    case TransactionTypes.doGetMyLikeCourses:
                        TrainerCourse[] myLike = new Gson().fromJson(result,TrainerCourse[].class);
                        ArrayList<TrainerCourse> tempMyLike = new ArrayList<>(Arrays.asList(myLike));
                        delegate.processFinish(tempMyLike);
                        break;
                    case TransactionTypes.doGetMyChatContent:
                        ChatContent[] chatContentsExist = new Gson().fromJson(result,ChatContent[].class);
                        ArrayList<ChatContent> _tempExist = new ArrayList<>(Arrays.asList(chatContentsExist));
                        ArrayList<Object> _returnExist = new ArrayList<>();
                        _returnExist.add(TransactionTypes.USER_CHAT_CONTENT_ALREADY_EXIST);
                        _returnExist.add(_tempExist);
                        delegate.processFinish(_returnExist);
                        break;
                    case TransactionTypes.doGetMyCurrentLastMessage:
                        CurrentChatLastMessageInfo currentChatLastMessageInfos = new Gson().fromJson(result,CurrentChatLastMessageInfo.class);
                            delegate.processFinish(currentChatLastMessageInfos);
                        break;
                    case TransactionTypes.doGetChatDetail:
                         ChatDetail[] chatDetails = new Gson().fromJson(result,ChatDetail[].class);
                         delegate.processFinish(new ArrayList<>(Arrays.asList(chatDetails)));
                    break;
                    case TransactionTypes.doUserSearch:
                        ChatContent[] chatContentsSearch = new Gson().fromJson(result,ChatContent[].class);
                        ArrayList<ChatContent> _tempSearch = new ArrayList<>(Arrays.asList(chatContentsSearch));
                        ArrayList<Object> _returnSearch = new ArrayList<>();
                        _returnSearch.add(TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH);
                        _returnSearch.add(_tempSearch);
                        delegate.processFinish(_returnSearch);
                    break;
                    case TransactionTypes.doUserClickChatDetailCourse:
                        TrainerCourse course = new Gson().fromJson(result,TrainerCourse.class);
                        delegate.processFinish(course);
                    break;
                    case TransactionTypes.doUserGetProfile:
                        TrainerCourse[] profil = new Gson().fromJson(result, TrainerCourse[].class);
                        ArrayList<TrainerCourse> _tmp = new ArrayList<>(Arrays.asList(profil));
                        delegate.processFinish(_tmp);
                    break;
                    case TransactionTypes.doTrainerGetMyUploads:
                        TrainerCourse[] myCourses = new Gson().fromJson(result,TrainerCourse[].class);
                        ArrayList<TrainerCourse> _coursetemp = new ArrayList<>(Arrays.asList(myCourses));
                        delegate.processFinish(_coursetemp);
                        break;
                    case TransactionTypes.doSearch:
                        delegate.processFinish(result);
                        break;
                    case TransactionTypes.doReSendMailCode:
                        ForgotPassword forgotPassword = new Gson().fromJson(result,ForgotPassword.class);
                        delegate.processFinish(forgotPassword);
                        break;
                    case TransactionTypes.doForgotPassword:
                        delegate.processFinish(result);
                        break;
                    case TransactionTypes.doUserSearchCourse:
                        TrainerCourse[] courses_ = new Gson().fromJson(result,TrainerCourse[].class);
                        ArrayList<TrainerCourse> _courses = new ArrayList<>(Arrays.asList(courses_));
                        delegate.processFinish(_courses);
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

