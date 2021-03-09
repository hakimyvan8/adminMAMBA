package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminLoungeActivity extends AppCompatActivity {

    private TextView AddAdmins,manage_users,manage_orders,manage_products,AdminNews;
    private Button Admin_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lounge);
        AddAdmins = findViewById(R.id.AddAdmins);
        manage_users = findViewById(R.id.manage_users);
        manage_orders = findViewById(R.id.manage_orders);
        manage_products = findViewById(R.id.manage_products);
        AdminNews = findViewById(R.id.AdminNews);
        Admin_logout = findViewById(R.id.Admin_logout);

        AddAdmins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoungeActivity.this, AddAdminsActivity.class);
                startActivity(intent);
            }
        });
        manage_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminLoungeActivity.this, ManageUsersActivity.class);
                startActivity(intent);
            }
        });
        manage_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminLoungeActivity.this, ManageOrdersActivity.class);
                startActivity(intent);
            }
        });
        manage_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminLoungeActivity.this, ManageProductsActivity.class);
                startActivity(intent);
            }
        });
        AdminNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoungeActivity.this, AdminNewsActivity.class);
                startActivity(intent);
            }
        });

        Admin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(AdminLoungeActivity.this, MainAdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}