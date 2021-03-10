package com.example.instagramclone;

import androidx.paging.DataSource;

import com.parse.ParseQuery;

public class ParseDataSourceFactory extends DataSource.Factory<Integer, Post> {

    @Override
    public DataSource<Integer, Post> create() {
        ParsePositionalDataSource source = new ParsePositionalDataSource();
        return source;
    }

}
