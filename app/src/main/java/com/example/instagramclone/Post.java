package com.example.instagramclone;

import android.text.PrecomputedText;

import androidx.annotation.NonNull;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LIKESCOUNT = "likes";
    public static final String KEY_LIKED = "liked";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";

    public Post(){}

    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }

    public boolean isLiked(ParseUser user){
        ArrayList<String> a = getLikedBy();
        if(a == null)
            return  false;
        return a.contains(user.getObjectId());
    }

    public void setLiked(boolean val)
    {
        put(KEY_LIKED, val);
    }

    public int getLikes(){return getInt(KEY_LIKESCOUNT);}

    public void addLikedBy(String s)
    {
        ArrayList<String> a = getLikedBy();
        if(a == null) {
            a = new ArrayList<>();
        }
        a.add(s);
        put("likesBy", a);
    }

    public void removeLikedBy(String s)
    {
        ArrayList<String> a = getLikedBy();
        if(a == null)
            return;
        a.remove(s);
        put("likesBy", a);
    }

    public ArrayList<String> getLikedBy(){return (ArrayList<String>) get("likesBy");}

    public void setLikes(int value){put(KEY_LIKESCOUNT, value);}

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
