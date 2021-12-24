package fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enise.bitirme_2.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import adapters.ChatContentAdapter;
import adapters.TrainerCourseAdapter;
import models.ChatContent;
import models.TrainerCourse;
import utils.AsyncResponse;
import utils.ServerGET;
import utils.StaticData;
import utils.TransactionTypes;
import utils.URLs;

public class ChatFragment extends Fragment implements AsyncResponse {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_content,container,false);
        view = rootView;
        ServerGET getMyContent = new ServerGET( new ProgressDialog(rootView.getContext()), TransactionTypes.doGetMyChatContent," loading your chats");
        getMyContent.delegate = this;
        getMyContent.execute(URLs.GetChatContent(StaticData.getUserData().getUserId()));
        return rootView;
    }
    @Override
    public <T> void processFinish(T result) {
        ArrayList<ChatContent> _lst = (ArrayList<ChatContent>) result;
        mRecyclerView = view.findViewById(R.id.chatContentRecyler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new ChatContentAdapter(_lst);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
