package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagramclone.Comment;
import com.example.instagramclone.DetailedActivity;
import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class WriteComment extends Fragment {


    EditText etComment;
    Button btnComment;
    Post post;
    public WriteComment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etComment = view.findViewById(R.id.etComment);
        btnComment = view.findViewById(R.id.btnComment);
        post = getArguments().getParcelable("post");


        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if(comment.isEmpty())
                {
                    Toast.makeText(getContext(), "Description is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveComment(comment, currentUser);
            }
        });

    }

    private void saveComment(String content, ParseUser currentUser) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setParseUser(currentUser);
        comment.setCommentTo(post);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null)
                {
                    Log.e(DetailedActivity.TAG, "saving error", e);
                    return;
                }
                etComment.setText("");
            }
        });
    }
}