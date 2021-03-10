package fragments;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.Post;
import com.example.instagramclone.PostAdapter;
import com.example.instagramclone.PostAdapter2;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {


    private static final int RESULT_LOAD_IMAGE = 900;
    private static final String TAG = "PROFILE";
    ImageView ivProfilePicture;
    TextView username;
    Button btnChangePicture;
    List<Post> posts;
    PostAdapter2 adapter2;
    private RecyclerView rvPosts;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected Button btnLogout;

    private SwipeRefreshLayout swipeContainer;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        username = view.findViewById(R.id.tvUsername);

        posts = new ArrayList<>();
        adapter2 = new PostAdapter2(getContext(), posts);

        rvPosts = view.findViewById(R.id.rvPosts);
        btnLogout = view.findViewById(R.id.btnLogout);
        swipeContainer = view.findViewById(R.id.swipeContainer);


        btnChangePicture = view.findViewById(R.id.btnChangePicture);

        btnChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        try {
            ParseFile p = ((ParseFile) ParseUser.getCurrentUser().get("picture"));
            if(p != null)
                Glide.with(this).load(p.getFile()).transform(new CircleCrop()).into(ivProfilePicture);
            else
                Glide.with(this).load(R.drawable.ic_launcher_background).transform(new CircleCrop()).into(ivProfilePicture);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(manager);
        rvPosts.setAdapter(adapter2);

        username.setText(ParseUser.getCurrentUser().getUsername());

        populateQueryPosts();
    }
    public ParseQuery<Post> getQuery()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        return query;
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
                posts.addAll(p);
                adapter2.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null !=data){
            Uri selectedImageUri = data.getData();
            ivProfilePicture.setImageURI(selectedImageUri);

            File f = getFile(selectedImageUri);


            ParseUser user = ParseUser.getCurrentUser();
            user.put("picture", new ParseFile(f));


            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i(TAG, "Image saved!");
                }
            });


        } else {
            Toast.makeText(getContext(), "You have not selected and image", Toast.LENGTH_SHORT).show();
        }
    }

    private File getFile(Uri selectedImageUri) {
        File f=new File(getContext().getCacheDir(),"file name");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Convert bitmap to byte array
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }


}
