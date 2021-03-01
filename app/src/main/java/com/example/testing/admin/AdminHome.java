package com.example.testing.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testing.R;
import com.example.testing.user.HomeActivity;
import com.example.testing.user.MainActivity;

public class AdminHome extends AppCompatActivity {
    private Button logoutadmin,checkorder,update,checkapprove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


           logoutadmin=findViewById(R.id.logoutadmin);
        checkorder=findViewById(R.id.checkorder);
        update=findViewById(R.id.updatechanges);
        checkapprove=findViewById(R.id.checkapprove);



              logoutadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHome.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHome.this, ADmin_New_Orders.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHome.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });


        checkapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminHome.this, CheckApproveProducts.class);
               /* intent.putExtra("Admin", "Admin");*/
                startActivity(intent);
            }
        });

    }
}