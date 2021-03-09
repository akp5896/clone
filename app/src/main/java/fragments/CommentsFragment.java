package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Comment;
import com.example.instagramclone.CommentAdapter;
import com.example.instagramclone.Post;
import com.example.instagramclone.PostAdapter;
import com.example.instagramclone.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {


    private static final String TAG = "Comments fragment";
    RecyclerView rvComments;
    SwipeRefreshLayout swipeContainer;
    protected List<Comment> comments;
    CommentAdapter adapter;

    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvComments = view.findViewById(R.id.rvComments);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        comments = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), comments);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
          //      populateQueryPosts();
            }
        });

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        //populateQueryPosts();
    }
}