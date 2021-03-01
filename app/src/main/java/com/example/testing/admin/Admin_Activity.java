package com.example.testing.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testing.user.LoginActivity;
import com.example.testing.user.MainActivity;
import com.example.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Admin_Activity extends AppCompatActivity {
    TextView inputadminname,inputadminpass,inputadminnumber;
    String s1,s2,s3;

    public static String datapath="Admins";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_);
        Button register;



            inputadminname = findViewById(R.id.inputname);
            inputadminpass = findViewById(R.id.inputpass);
            inputadminnumber = findViewById(R.id.inputnumber);
            register = findViewById(R.id.reg);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateAccount();
                }
            });


        }

        private void CreateAccount() { s1 = inputadminname.getText().toString();
            s2 = inputadminnumber.getText().toString();
            s3 = inputadminpass.getText().toString().trim();

            if (TextUtils.isEmpty(s1))
                inputadminname.setError("Enter username");


            else if (TextUtils.isEmpty(s2))
                inputadminnumber.setError("Enter phone number");

            else if (TextUtils.isEmpty(s3))
                inputadminpass.setError("Enter password");

            else
                Validate(s1, s2, s3);

        }

        private void Validate(final String name, final String number, final String password) {

            final DatabaseReference databaseReference;
            databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!(dataSnapshot.child(datapath).child(number).exists())){
                        final HashMap<String,Object> admindata=new HashMap<>();
                        admindata.put("name",name);
                        admindata.put("phone",number);
                        admindata.put("password",password);
                        databaseReference.child(datapath).child(number).updateChildren(admindata)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Admin_Activity.this, "Account Created!!!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Admin_Activity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }

                                        else{
                                            Toast.makeText(Admin_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                });

                    }

                    else
                    {  Toast.makeText(Admin_Activity.this, "This " + number + " already exists.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Admin_Activity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Admin_Activity.this, MainActivity.class);
                        startActivity(intent);}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
