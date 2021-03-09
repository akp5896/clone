package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;

public class DetailedActivity extends AppCompatActivity {

    TextView tvUsername;
    ImageView ivCapture;
    TextView tvDate;
    TextView tvPostdescripion;
    RelativeLayout container;
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