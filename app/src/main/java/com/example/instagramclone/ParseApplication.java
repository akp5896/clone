package com.example.instagramclone;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(UserPicture.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("uTJg3rpUj5FTCCwAPZQVpLFh2JDHXvI0lcmzrDme")
                .clientKey("S7GWwck6WDsbewOCdzQeagYrQLOw4FaVxcxCRhsT")
                .server("https://parseapi.back4app.com")
                .build()
        );


    }
}
