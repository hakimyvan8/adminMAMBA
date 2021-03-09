package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAdminsActivity extends AppCompatActivity {

    private TextView backBtn,AdminTv,admin_name,AdminPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admins);
        backBtn = findViewById(R.id.backBtn);
        AdminTv = findViewById(R.id.AdminTv);
        admin_name = findViewById(R.id.admin_name);
        AdminPhone = findViewById(R.id.AdminPhone);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        AdminTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddAdminsActivity.this, RegisterAdminActivity.class);
                startActivity(intent);
            }
        });
    }
}