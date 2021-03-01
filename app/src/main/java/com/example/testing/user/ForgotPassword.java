package com.example.testing.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testing.Prevalent.Prevalent;
import com.example.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {
    private String check = "";
    private TextView tv4, ques;
    private EditText tv, tv2, tv31;
    private Button tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tv = findViewById(R.id.tv2);
        tv2 = findViewById(R.id.tv4);
        tv31 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv4 = findViewById(R.id.tv1);
        ques = findViewById(R.id.tv3);
        check = getIntent().getStringExtra("check");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (check.equals("settings")) {
            tv4.setText("Security Questions");
            tv.setVisibility(View.GONE);
            ques.setText("Set the answers for these questions...");
            tv6.setText("SET");

            displayAns();

            tv6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                setques();
                }
            });
        }
        else if (check.equals("login")) {
            tv6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forgotpassword();
                }
            });
        }
    }


    private  void setques(){

        String ans1 = tv2.getText().toString().toLowerCase();
        String ans2 = tv31.getText().toString().toLowerCase();


        if (ans1.equals("") || ans2.equals(""))
            Toast.makeText(ForgotPassword.this, "Answer the following questions please..", Toast.LENGTH_SHORT).show();

        else {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("answer1", ans1);
            hashMap.put("answer2", ans2);

            databaseReference.child("Security_Questions").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Answers has been set...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this, HomeActivity.class));
                    }

                }
            });
        }}










    private void displayAns() {

             DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
            databaseReference.child("Security_Questions").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String ques1 = dataSnapshot.child("answer1").getValue().toString();
                        String ques2 = dataSnapshot.child("answer2").getValue().toString();
                        tv2.setText(ques1);
                        tv31.setText(ques2);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



        private void forgotpassword(){

            if(TextUtils.isEmpty(tv.getText().toString())){
                Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            }
            final String ans1 = tv2.getText().toString().toLowerCase();
            final String ans2 = tv31.getText().toString().toLowerCase();
            final String phone = tv.getText().toString();
            final  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String uphone=dataSnapshot.child("phone").getValue().toString();
                        if(phone.equals(uphone)){

                            dataSnapshot.hasChild("Security_Questions");
                            String ques1 = dataSnapshot.child("Security_Questions").child("answer1").getValue().toString();
                            String ques2 = dataSnapshot.child("Security_Questions").child("answer2").getValue().toString();
                                    if(!ques1.equals(ans1)){
                                        Toast.makeText(ForgotPassword.this, "Answer of question 1 is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                    else  if(!ques2.equals(ans2)){
                                        Toast.makeText(ForgotPassword.this, "Answer of question 2 is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        AlertDialog.Builder builder=new AlertDialog.Builder(ForgotPassword.this);
                                        builder.setTitle("Change Password");
                                        final EditText ed=new EditText(ForgotPassword.this);
                                        ed.setHint("Enter new password");
                                        builder.setView(ed);
                                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (!ed.getText().toString().equals("")) {

                                                    databaseReference.child("password").setValue(ed.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(ForgotPassword.this, "Password has been reset", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder.show();
                                    }

                        }
                    }
                    else{
                        Toast.makeText(ForgotPassword.this, "This Phone Number do not exists", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
