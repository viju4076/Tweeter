package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserFeed extends AppCompatActivity {
ListView list;
SimpleAdapter simpleAdapter;
ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
       list=findViewById(R.id.list);
       getSupportActionBar().setTitle("User Feed");
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Tweets");
        ArrayList<String> following= (ArrayList<String>)ParseUser.getCurrentUser().get("following");
        Log.i("following",following.size()+"");
      query.whereContainedIn("username",following);
      query.addDescendingOrder("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    Log.i("Objects",objects.size()+"");
                    if(objects.size()>0)
                    {
                        for(ParseObject object:objects)
                        {
                            HashMap<String, String> hashMap = new HashMap<>();
                           String tweet=(String)object.get("tweet");
                           String author=(String)object.get("username");
                            hashMap.put("tweets", tweet);
                            hashMap.put("author", author);
                            arrayList.add(hashMap);

                        }

                        String[] from={"tweets","author"};
                        int [] to={R.id.Tweet,R.id.Author};
                        simpleAdapter=new SimpleAdapter(getApplicationContext(),arrayList,R.layout.feedtable,from,to);
                        list.setAdapter(simpleAdapter);

                    }
                }
            }
        });








    }

}
