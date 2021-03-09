package com.example.instagramclone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    List<Post> posts;
    Context context;

    public void addAll(List<Post> postsList)
    {
        posts.addAll(postsList);
    }

    public PostAdapter(Context context, List<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.bind(posts.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvUsername;
        ImageView ivCapture;
        TextView tvDate;
        TextView tvPostdescripion;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivCapture = itemView.findViewById(R.id.ivCapture);
            tvPostdescripion = itemView.findViewById(R.id.tvPostDescription);
            tvDate = itemView.findViewById(R.id.tvCreatedAt);
            container = itemView.findViewById(R.id.rvContainer);
        }

        public void bind(final Post post) throws ParseException {
            tvUsername.setText(post.getUser().getUsername());
            tvPostdescripion.setText(post.getDescription());
            tvDate.setText(getRelativeTime(post.getCreatedAt()));
            if(post.getImage() != null)
            {
                Bitmap takenImage = BitmapFactory.decodeFile(post.getImage().getFile().getAbsolutePath());
                ivCapture.setImageBitmap(takenImage);
            }

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Post", tvPostdescripion.getText().toString() + " clicked");
                }
            });
        }

        private String getRelativeTime(Date date)
        {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
            return TimeFormatter.getTimeDifference(format.format(date));
        }
    }
}
