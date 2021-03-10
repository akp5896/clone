package com.example.instagramclone;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserPicture")
public class UserPicture extends ParseObject {
    public ParseUser getUser(){return getParseUser("user");}
    public void setParseUser(ParseUser user){put("user", user);}
    public ParseFile getImage()
    {
        return getParseFile("picture");
    }

    public void setImage(ParseFile file)
    {
        put("picture", file);
    }

}
