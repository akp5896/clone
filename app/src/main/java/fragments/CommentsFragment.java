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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {


    private static final String TAG = "Comments fragment";
    RecyclerView rvComments;
    SwipeRefreshLayout swipeContainer;
    Post post;
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
        post = getArguments().getParcelable("post");

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                populateComments();
            }
        });

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        populateComments();
    }

    protected void populateComments()
    {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_COMMENT_TO, post);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> p, ParseException e) {
                if(e != null)
                {
                    Log.e(TAG, "Issue receiving posts", e);
                    return;
                }
                comments.clear();
                comments.addAll(p);
                //Log.i(TAG, "number of commetns" + String.valueOf(comments.size()));
                adapter.notifyDataSetChanged();
                //Log.i(TAG, "in adapter" + String.valueOf(adapter.getItemCount()));

                swipeContainer.setRefreshing(false);
            }
        });
    }
}