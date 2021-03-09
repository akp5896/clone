package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


    ImageView profilePicture;
    TextView username;
    Button btnChangePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePicture = view.findViewById(R.id.ivProfilePicture);
        username = view.findViewById(R.id.tvUsername);
        btnChangePicture = view.findViewById(R.id.btnChangePicture);

        username.setText(ParseUser.getCurrentUser().getUsername());
    }

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
