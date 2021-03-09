package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {





    @Override
    protected void populateQueryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
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
                //    Log.i(TAG, i.getDescription());
                //}
                //Collections.reverse(p);
                posts.addAll(p);
                Log.i(TAG, String.valueOf(adapter.getItemCount()));
                adapter.notifyDataSetChanged();
            }
        });
    }
}
