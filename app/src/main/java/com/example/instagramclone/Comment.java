package com.example.instagramclone;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.lang.reflect.Array;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_CONTENT = "content";
    public static final String KEY_USER = "user";
    public static final String KEY_COMMENT_TO = "comment_to";

    public String getContent()
    {
        return getString(KEY_CONTENT);
    }

    public void setContent(String description)
    {
        put(KEY_CONTENT, description);
    }

    public Post getCommentTo()
    {
        return (Post) getParseObject(KEY_COMMENT_TO);
    }


    public void setCommentTo(Post post)
    {
        put(KEY_COMMENT_TO, post);
    }

    public ParseUser getUser()
    {
        return  getParseUser(KEY_USER);
    }

    public void setParseUser(ParseUser user)
    {
        put(KEY_USER, user);
    }
}
