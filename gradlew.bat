package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.userinfo.userinfo;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;


import io.paperdb.Paper;

public class login extends AppCompatActivity {
Button log;
TextView forgot,admin,notadmin;
EditText id,pass;
private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        log=(Button)findViewById(R.id.log);
        forgot=(TextView) findViewById(R.id.forgot);
        id=(EditText) findViewById(R.id.id);
        pass=(EditText)findViewById(R.id.pass);
        checkBox=(CheckBox) findViewById(R.id.cbox);
        admin=findViewById(R.id.admin);
        notadmin=findViewById(R.id.notadmin);
        Paper.init(this);
        String datapath="user";

      log.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              userlog();
          }
      });



      admin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              log.setText("Admin Login");
              admin.setVisibility(View.INVISIBLE);
              notadmin.setVisibility(View.VISIBLE);
              datapath="admins";
          }
      });



        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.setText("Admin Login");
      