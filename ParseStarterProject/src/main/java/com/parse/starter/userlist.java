package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class userlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        getSupportActionBar().setTitle("User List");
        Intent intent=getIntent();
        String username=intent.getStringExtra("username") ;
     Log.i("username",username);
    }
}
