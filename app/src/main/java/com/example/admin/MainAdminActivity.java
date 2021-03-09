package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.Models.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAdminActivity extends AppCompatActivity {
    private EditText Admin_number, Admin_Password;
    private Button Admin_login;
    private ProgressDialog loadingBar;
    private String parentDbName = "Administrators";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        Admin_number = findViewById(R.id.Admin_number);
        Admin_Password = findViewById(R.id.Admin_Password);
        loadingBar = new ProgressDialog(this);
        Admin_login = findViewById(R.id.Admin_login);
        loadingBar = new ProgressDialog(this);
        Admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {

        String phone = Admin_number.getText().toString();
        String password = Admin_Password.getText().toString();

        if (TextUtils.isEmpty(Admin_number.getText().toString())) {
            Admin_number.setError("Please write valid phone number");
        } else if (TextUtils.isEmpty(Admin_Password.getText().toString())) {
            Admin_Password.setError("Please write valid Password");
        } else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(String phone, String password)
    {
        final DatabaseReference myRef;
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child(parentDbName).child(phone).exists())
                {
                    Admin adminData = snapshot.child(parentDbName).child(phone).getValue(Admin.class);
                    if (adminData.getPhone().equals(phone))
                    {
                        if (adminData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Administrators"))
                            {

                                Intent intent = new Intent(MainAdminActivity.this, AdminLoungeActivity.class);
                                startActivity(intent);
                            }

                            else {

                                Toast.makeText(MainAdminActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    }
                    else {
                        Toast.makeText(MainAdminActivity.this, "Account with this" + phone + "number do not exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Intent intent = new Intent(MainAdminActivity.this, MainAdminActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
