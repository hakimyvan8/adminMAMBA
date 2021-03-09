package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.Constants.RegisterConstant;
import com.example.admin.Models.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterAdminActivity extends AppCompatActivity {

    private TextView backBtn;
    private EditText SignUp_Admin,signup_department,SignUp_AdminNumber,SignUp_AdminPassword;
    private Button SignUp_AdminRegister;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        backBtn = findViewById(R.id.backBtn);
        SignUp_Admin = findViewById(R.id.SignUp_Admin);
        signup_department = findViewById(R.id.signup_department);
        SignUp_AdminNumber = findViewById(R.id.SignUp_AdminNumber);
        loadingBar = new ProgressDialog(this);
        SignUp_AdminPassword = findViewById(R.id.SignUp_AdminPassword);
        SignUp_AdminRegister = findViewById(R.id.SignUp_AdminRegister);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SignUp_AdminRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAdmin();
            }
        });
        signup_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departmentDialog();
            }
        });
    }
    private void CreateAdmin()
    {

        String name = SignUp_Admin.getText().toString();
        String phone = SignUp_AdminNumber.getText().toString();
        String password = SignUp_AdminPassword .getText().toString();
        String department = signup_department.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Write your Name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Write Your Phone...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Write Your Password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(department)){
            Toast.makeText(this, "Please Write Your Department...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateAccount(name,phone, password,department);
        }
    }

    private void ValidateAccount(String name, String phone, String password, String department)
    {
        final DatabaseReference adminRef;
        adminRef = FirebaseDatabase.getInstance().getReference();

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!(snapshot.child("Administrators").child(phone).exists()))
                {
                    HashMap<String, Object> admindataMap = new HashMap<>();
                    admindataMap.put("name",name);
                    admindataMap.put("phone",phone);
                    admindataMap.put("password",password);
                    admindataMap.put("department",""+department);

                    adminRef.child("Administrators").child(phone).updateChildren(admindataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {

                                        Toast.makeText(RegisterAdminActivity.this, "Congratulations, Your Account has been Created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent=new Intent(RegisterAdminActivity.this, AddAdminsActivity.class);
                                        startActivity(intent);
                                    }
                                    else {

                                        Toast.makeText(RegisterAdminActivity.this, "Network Error: Please Try Again...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
                else {

                    Toast.makeText(RegisterAdminActivity.this, "This" + phone + "already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterAdminActivity.this, "Please Try Another phone", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(RegisterAdminActivity.this, AddAdminsActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void departmentDialog()
    {
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Department Category")
                .setItems(RegisterConstant.departmentCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //get picked category
                        String category = RegisterConstant.departmentCategories[which];

                        //set picked category
                        signup_department.setText(category);
                    }
                })
                .show();
    }
}