package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.MainActivity;
import com.example.instagramclone.ParseDataSourceFactory;
import com.example.instagramclone.Post;
import com.example.instagramclone.PostAdapter;
import com.example.instagramclone.R;
import com.example.instagramclone.TimeFormatter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PostsFragment extends Fragment {

    public static final String TAG = "Posts fragment";

    private RecyclerView rvPosts;
    protected PostAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected Button btnLogout;
    LiveData<PagedList<Post>> posts;
    private SwipeRefreshLayout swipeContainer;

    public PostsFragment() { }


    public static PostsFragment newInstance(String param1, String param2) {

        return null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        btnLogout = view.findViewById(R.id.btnLogout);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        //posts = new ArrayList<>();
        //adapter = new PostAdapter(getContext(), posts);

        adapter = new PostAdapter(getContext());

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder().setEnablePlaceholders(true)
                        .setPrefetchDistance(10)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        PagedList.Config config = new PagedList.Config.Builder().setPageSize(20).build();

        ParseDataSourceFactory sourceFactory = new ParseDataSourceFactory();

        posts = new LivePagedListBuilder<>(sourceFactory, config).build();



        posts.observe(this, new Observer<PagedList<Post>>() {
            @Override
            public void onChanged(@Nullable PagedList<Post> tweets) {
                adapter.submitList(tweets);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreData();
            }
        };

        rvPosts.addOnScrollListener(scrollListener);



        btnLogout.setText(String.format(getResources().getString(R.string.logged), ParseUser.getCurrentUser().getUsername()));
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("123", "fetching new data");
                populateQueryPosts();

            }
        });

        rvPosts.setAdapter(adapter);

        populateQueryPosts();
    }

    private void loadMoreData() {
        Log.i(TAG, "scolled");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setSkip(adapter.getItemCount());
        query.setLimit(5);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> p, ParseException e) {
                if(e != null)
                {
                    Log.e(TAG, "Issue receiving posts", e);
                    return;
                }
                adapter.addAll(p);
                Log.i(TAG, String.valueOf(adapter.getItemCount()));

                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        populateQueryPosts();
    }

    protected void populateQueryPosts()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(5);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> p, ParseException e) {
                if(e != null)
                {
                    Log.e(TAG, "Issue receiving posts", e);
                    return;
                }

                //for(Post i : p)
                //{
                    //Log.i(TAG, i.getDescription());
                    //Date a = i.getCreatedAt();

                    //Log.i(TAG, x);
                //    Log.i(TAG, "!");
                //}
                //Collections.reverse(p);
                //posts.clear();
                //posts.addAll(p);
                adapter.clear();
                adapter.addAll(p);
                Log.i(TAG, String.valueOf(adapter.getItemCount()));
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}