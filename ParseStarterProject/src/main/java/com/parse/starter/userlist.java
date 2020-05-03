package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class userlist extends AppCompatActivity {
    String username;
  ListView list;
  ArrayList<String> user;
  ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.user_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        SparseBooleanArray items=list.getCheckedItemPositions();
        ArrayList<String> following =new ArrayList<String>();
        for(int i=0;i<items.size();i++)
        {
            if(items.get(i))
            {
                Log.i("user",user.get(i));
                following.add(user.get(i));
            }
        }
        ParseUser.getCurrentUser().put("following",following);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(getApplicationContext(),"Following changed",Toast.LENGTH_SHORT);
                    if(item.getItemId()==R.id.logout)
                    {
                        ParseUser.logOut();
                        Intent intent=new Intent(userlist.this,MainActivity.class);
                        intent.putExtra("logout",true);
                        finish();
                        startActivity(intent);

                    }



                }
            }
        });



        if(item.getItemId()==R.id.tweet)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(getApplicationContext());
            alert.setMessage("Enter your tweet");
            alert.setTitle("Tweet");

            alert.setView(edittext);

            alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    //OR
                    String content = edittext.getText().toString();
                    Log.i("tweet",content);
                    ParseObject parseObject=new ParseObject("Tweets");
                    parseObject.put("username",ParseUser.getCurrentUser().getUsername());
                    parseObject.put("tweet",content);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                Toast.makeText(getApplicationContext(),"Your Tweet has been posted",Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Error posting the tweet",Toast.LENGTH_SHORT).show();

                            }
                        }

                    });

                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();



        }
        else if(item.getItemId()==R.id.userFeed)
        {
        }
        else
        {



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        getSupportActionBar().setTitle("User List");
        Intent intent=getIntent();
       username =intent.getStringExtra("username") ;
      user=new ArrayList<String>();
      list=findViewById(R.id.list);
      arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.select_dialog_multichoice,user);
      list.setAdapter(arrayAdapter);
      list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
     Log.i("username",username);
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("username",username);
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseUser object :objects)
                        {
                            user.add(object.getUsername());

                        }
                        ArrayList<String> following=(ArrayList<String>)ParseUser.getCurrentUser().get("following");
                        for(int i=0;i<following.size();i++)
                        {
                            for(int j=0;j<user.size();j++)
                            {

                                if(following.get(i).equals(user.get(j)))
                                {
                                    list.setItemChecked(j,true);
                                }
                            }
                        }


                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }



}
