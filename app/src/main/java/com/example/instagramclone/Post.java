package com.example.instagramclone;

import androidx.annotation.NonNull;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";

    public Post(){}

    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage()
    {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile file)
    {
        put(KEY_IMAGE, file);
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
