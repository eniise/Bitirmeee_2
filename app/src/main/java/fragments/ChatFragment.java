package fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import java.util.ArrayList;

import adapters.ChatContentAdapter;
import models.chat.ChatContent;
import utils.AsyncResponse;
import utils.server.ServerGET;
import utils.user.StaticData;
import utils.extras.TransactionTypes;
import utils.extras.URLs;

public class ChatFragment extends Fragment implements AsyncResponse, TextWatcher {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private EditText txtSearchUser;
    ProgressBar chatContent;
    private View searchingView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_content,container,false);
        view = rootView;
        chatContent = rootView.findViewById(R.id.chatContent);
        txtSearchUser = rootView.findViewById(R.id.txtSearchUser);
        txtSearchUser.addTextChangedListener(this);
        searchingView = rootView.findViewById(R.id.searchingView);
        ServerGET getMyContent = new ServerGET(TransactionTypes.doGetMyChatContent);
        getMyContent.delegate = this;
        getMyContent.execute(URLs.GetChatContent(StaticData.getUserData().getUserId()));
        return rootView;
    }
    @Override
    public <T> void processFinish(T result) {
        ArrayList<Object> _tmp = (ArrayList<Object>) result;
        ArrayList<ChatContent> _lst;
        if((int)_tmp.get(0) == TransactionTypes.USER_CHAT_CONTENT_TYPE_SEARCH){
            _lst = (ArrayList<ChatContent>) _tmp.get(1);
            searchingView.setVisibility(View.GONE);
            chatContent.setVisibility(View.GONE);
        }else{
            _lst = (ArrayList<ChatContent>) _tmp.get(1);
            searchingView.setVisibility(View.GONE);
            chatContent.setVisibility(View.GONE);
        }
        mRecyclerView = view.findViewById(R.id.chatContentRecyler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new ChatContentAdapter(_lst,TransactionTypes.LAYOUT_MESSAGE_NORMAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        chatContent.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length()>0) {
            searchingView.setVisibility(View.VISIBLE);
            chatContent.setVisibility(View.VISIBLE);
            ServerGET getMyContent = new ServerGET(TransactionTypes.doUserSearch);
            getMyContent.delegate = this;
            getMyContent.execute(URLs.SearchUser(s.toString(),StaticData.getUserData().getUserId()));
        }else {
            searchingView.setVisibility(View.GONE);
            chatContent.setVisibility(View.VISIBLE);
            ServerGET getMyContent = new ServerGET(TransactionTypes.doGetMyChatContent);
            getMyContent.delegate = this;
            getMyContent.execute(URLs.GetChatContent(StaticData.getUserData().getUserId()));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
