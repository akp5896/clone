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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.File;
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
        TextView tvPostdescripion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivCapture = itemView.findViewById(R.id.ivCapture);
            tvPostdescripion = itemView.findViewById(R.id.tvPostDescription);
        }

        public void bind(final Post post) throws ParseException {
            tvUsername.setText(post.getUser().getUsername());
            tvPostdescripion.setText(post.getDescription());
            if(post.getImage() != null)
            {
                //Log.i("IMAGE ", String.valueOf(post.getImage().getFile() == null));
                //Log.i("IMAGE ", String.valueOf(post.getImage().getUrl() == null));
                //String img = post.getImage().
                //Log.i("","");
                Bitmap takenImage = BitmapFactory.decodeFile(post.getImage().getFile().getAbsolutePath());
                ivCapture.setImageBitmap(takenImage);
            }
        }
    }
}
