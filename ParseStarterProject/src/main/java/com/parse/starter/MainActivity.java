/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
EditText username,password;
TextView loginOrSign;
boolean login=true;
Button go;
public void change(View view)
{
  if(login)
  {
    login = false;
    loginOrSign.setText("Or LogIn");
    go.setText("SignUp");

  }
else
  {
    login = true;
    loginOrSign.setText("Or SignUp");
    go.setText("LogIn");
  }

}
public void login(View view)
{
  String user=username.getText().toString();
  String pass=password.getText().toString();
  if(user.equals("")||pass.equals(""))
  {
    Toast.makeText(getApplicationContext(),"Fields cannot be left blank",Toast.LENGTH_SHORT).show();
    return;
  }
  if(login)
  {
    ParseUser.logInInBackground(user, pass, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
       if(e==null)
       {
         Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
         Log.i("User "," Login Successful");
         Intent intent=new Intent(MainActivity.this,userlist.class);
         intent.putExtra("username",ParseUser.getCurrentUser().getUsername());
         startActivity(intent);
         finish();

       }
       else
       {
         Toast.makeText(getApplicationContext(),"Invalid username/password",Toast.LENGTH_SHORT).show();
       }

      }
    });


  }
  else
  {
    ParseUser parseUser=new ParseUser();
    parseUser.setUsername(user);
    parseUser.setPassword(pass);
parseUser.put("following",new ArrayList<String>());

    parseUser.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if(e==null)
        {
          Toast.makeText(getApplicationContext(),"Signup Successful",Toast.LENGTH_SHORT).show();
        }
        else
        {
          Toast.makeText(getApplicationContext(),"Signup Failed",Toast.LENGTH_SHORT).show();

        }
      }
    });

  }



}

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
username=findViewById(R.id.username);
password=findViewById(R.id.password);
loginOrSign=findViewById(R.id.loginOrSign);
go=findViewById(R.id.button);
//ParseUser.logOut();
    getSupportActionBar().setTitle("Twitter Login");

    if(ParseUser.getCurrentUser()!=null)
    {
      Intent intent=new Intent(MainActivity.this,userlist.class);
      intent.putExtra("username",ParseUser.getCurrentUser().getUsername());
      finish();
      startActivity(intent);


    }


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}