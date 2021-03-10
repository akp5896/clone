package fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.example.instagramclone.UserPicture;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends PostsFragment {


    private static final int RESULT_LOAD_IMAGE = 900;
    ImageView ivProfilePicture;
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
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        username = view.findViewById(R.id.tvUsername);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null !=data){
            Uri selectedImageUri = data.getData();
            File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

            File f=new File(getContext().getCacheDir(),"file name");
            ivProfilePicture.setImageURI(selectedImageUri);
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



        UserPicture up = new UserPicture();
            up.setImage(new ParseFile(f));
            up.setParseUser(ParseUser.getCurrentUser());
            //user.notify();



            up.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i(TAG, "Image saved!");
                }
            });


        } else {
            Toast.makeText(getContext(), "You have not selected and image", Toast.LENGTH_SHORT).show();
        }
    }


}
