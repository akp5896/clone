package com.example.instagramclone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostAdapter extends ListAdapter<Post, PostAdapter.ViewHolder> {

    List<Post> posts;
    Context context;

    public void addAll(List<Post> postsList)
    {
        posts.addAll(postsList);
        submitList(posts);
    }

    public void clear()
    {
        posts.clear();
        submitList(posts);
    }

    public static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Post>() {
                @Override
                public boolean areItemsTheSame(Post oldItem, Post newItem) {
                    return oldItem.getObjectId().equals(newItem.getObjectId());
                }
                @Override
                public boolean areContentsTheSame(Post oldItem, Post newItem) {
                    return (oldItem.getObjectId().equals(newItem.getObjectId()));
                }
            };

    public PostAdapter() {
        super(DIFF_CALLBACK);

    }

    public PostAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.posts = new ArrayList<>();
        this.context = context;
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
            holder.bind(getItem(position));
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    //@Override
    //public int getItemCount() {
    //    return posts.size();
    //}


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvUsername;
        ImageView ivCapture;
        Button ibHeart;
        TextView tvnumLikes;
        TextView tvPostdescripion;
        TextView tvDate;
        ImageView ivProfpic;
        RelativeLayout body;
        RelativeLayout user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfpic = itemView.findViewById(R.id.ivProfpic);
            tvnumLikes = itemView.findViewById(R.id.numlikes);
            ibHeart = itemView.findViewById(R.id.ibHeart);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivCapture = itemView.findViewById(R.id.ivCapture);
            tvPostdescripion = itemView.findViewById(R.id.tvPostDescription);
            tvDate = itemView.findViewById(R.id.tvCreatedAt);
            body = itemView.findViewById(R.id.rvBody);
            user = itemView.findViewById(R.id.rvUser);

        }

        public void bind(final Post post) throws ParseException {
            post.setParseUser(post.getUser().fetchIfNeeded());
            if(post.getUser() != null && post.getUser().getUsername() == null)
                return;
            tvUsername.setText(post.getUser().getUsername());
            tvnumLikes.setText(String.valueOf(post.getLikes()));
            tvPostdescripion.setText(post.getDescription());
            tvDate.setText(TimeFormatter.getRelativeTime(post.getCreatedAt()));
            try {
                ParseFile p = ((ParseFile) post.getUser().get("picture"));
                if(p != null)
                    Glide.with(context).load(p.getFile()).transform(new CircleCrop()).into(ivProfpic);
                else
                    Glide.with(context).load(R.drawable.ic_launcher_background).transform(new CircleCrop()).into(ivProfpic);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(post.getImage() != null)
            {
                Bitmap takenImage = BitmapFactory.decodeFile(post.getImage().getFile().getAbsolutePath());
                ivCapture.setImageBitmap(takenImage);
            }

            if(!post.isLiked(ParseUser.getCurrentUser()))
                ibHeart.setBackgroundResource(R.drawable.ufi_heart);
            else
                ibHeart.setBackgroundResource(R.drawable.ufi_heart_active);

            ibHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(post.isLiked(ParseUser.getCurrentUser()))
                    {
                        post.removeLikedBy(ParseUser.getCurrentUser().getObjectId());
                        post.setLikes(post.getLikes() - 1);
                        ibHeart.setBackgroundResource(R.drawable.ufi_heart);

                    }
                    else
                    {
                        post.setLikes(post.getLikes() + 1);
                        post.addLikedBy(ParseUser.getCurrentUser().getObjectId());
                        ibHeart.setBackgroundResource(R.drawable.ufi_heart_active);
                    }
                    tvnumLikes.setText(String.valueOf(post.getLikes()));
                    post.saveInBackground();
                }
            });

            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("user", post.getUser());
                    context.startActivity(i);
                }
            });

            body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailedActivity.class);
                    i.putExtra("post", post);
                    context.startActivity(i);
                }
            });
        }

    }
}
