package com.example.instagramclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    public static final String TAG = "FEED ACTIVITY";
    public static final int REQUEST_CODE = 56;
    List<Post> posts;
    PostAdapter adapter;
    RecyclerView rvPosts;
    FloatingActionButton btnCompose;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        posts = new ArrayList<>();
        adapter = new PostAdapter(this, posts);
        rvPosts  = findViewById(R.id.rvPosts);
        btnCompose = findViewById(R.id.btnCompose);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setText(String.format(getResources().getString(R.string.logged), ParseUser.getCurrentUser().getUsername()));

        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedActivity.this, MainActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent i = new Intent(FeedActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);
        populateQueryPosts();
        Log.i(TAG, "inside");
    }

    private void populateQueryPosts()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> p, ParseException e) {
                if(e != null)
                {
                    Log.e(TAG, "Issue receiving posts", e);
                    return;
                }
                for(Post i : p)
                {
                    Log.i(TAG, i.getDescription());
                }
                Collections.reverse(p);
                adapter.addAll(p);
                Log.i(TAG, String.valueOf(adapter.getItemCount()));
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            Post post = (Post) data.getExtras().get("post");
            posts.add(0, post);
            adapter.notifyItemInserted(0);
            rvPosts.smoothScrollToPosition(0);
        }
    }
}