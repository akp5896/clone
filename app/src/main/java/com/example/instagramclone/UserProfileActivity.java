package com.example.instagramclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "USER PROFILE ACTIVITY";
    ImageView ivProfilePicture;
    TextView username;
    private RecyclerView rvPosts;
    protected PostAdapter2 adapter;
    private SwipeRefreshLayout swipeContainer;
    //LiveData<PagedList<Post>> posts;
    protected List<Post> posts;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user = (ParseUser)getIntent().getExtras().get("user");

        rvPosts = findViewById(R.id.rvPosts);
        swipeContainer = findViewById(R.id.swipeContainer);
        //posts = new ArrayList<>();
        posts = new ArrayList<>();
        adapter = new PostAdapter2(this, posts);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                populateQueryPosts();

            }
        });



        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(UserProfileActivity.this, 2));


        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        username = findViewById(R.id.tvUsername);
        MainActivity.SavePicture(ivProfilePicture, this, ((ParseFile) user.get("picture")));

        username.setText(user.getUsername());
        populateQueryPosts();

    }


    public ParseQuery<Post> getQuery()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        return query;
    }

    private void loadMoreData() {
        Log.i(TAG, "scolled");
        ParseQuery<Post> query = getQuery();
        query.setSkip(adapter.getItemCount());

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

    protected void populateQueryPosts() {
        ParseQuery<Post> query = getQuery();

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
                //    Log.i(TAG, i.getDescription());
                //}
                //Collections.reverse(p);
                //posts.addAll(p);
                //Log.i(TAG, String.valueOf(adapter.getItemCount()));
                //adapter.notifyDataSetChanged();
                adapter.clear();
                adapter.addAll(p);
                Log.i(TAG, String.valueOf(adapter.getItemCount()));
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}