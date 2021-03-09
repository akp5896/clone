package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import fragments.CommentsFragment;
import fragments.ComposeFragment;
import fragments.PostsFragment;
import fragments.ProfileFragment;
import fragments.WriteComment;

public class DetailedActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    public static final String TAG = "Detailed activity";
    TextView tvUsername;
    ImageView ivCapture;
    TextView tvDate;
    TextView tvPostdescripion;
    RelativeLayout container;
    BottomNavigationView bottomNavigationView;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        post = (Post)getIntent().getExtras().get("post");

        tvUsername = findViewById(R.id.tvUsername);
        ivCapture = findViewById(R.id.ivCapture);
        tvPostdescripion = findViewById(R.id.tvPostDescription);
        tvDate = findViewById(R.id.tvCreatedAt);
        container = findViewById(R.id.rvContainer);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Log.i(TAG, String.valueOf(item.getItemId()));
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", post);
                switch (item.getItemId()) {
                    case R.id.action_add:
                        fragment = new WriteComment();
                        fragment.setArguments(bundle);
                        Log.i(TAG, "HOME");
                        break;
                    case R.id.action_comment:
                    default:
                        fragment = new CommentsFragment();
                        fragment.setArguments(bundle);
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_add);


        tvUsername.setText(post.getUser().getUsername());
        tvPostdescripion.setText(post.getDescription());
        tvDate.setText(TimeFormatter.getRelativeTime(post.getCreatedAt()));
        if(post.getImage() != null)
        {
            Bitmap takenImage = null;
            try {
                takenImage = BitmapFactory.decodeFile(post.getImage().getFile().getAbsolutePath());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ivCapture.setImageBitmap(takenImage);
        }
    }


}