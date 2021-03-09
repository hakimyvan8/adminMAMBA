package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.Constants.ProductCategory;
import com.example.admin.Constants.QuantityCategory;
import com.example.admin.Constants.RegisterConstant;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity {

    private String CategoryName,Description,Pname,Price,Quantity, saveCurrentDate,saveCurrentTime;
    private Button Addproduct;
    private CircleImageView circularImage;
    private TextView backBtn,product_Category,product_Quantity;
    private ProgressDialog loadingBar;
    private EditText product_name, product_Price,product_Description;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        product_name = findViewById(R.id.product_name);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        product_Quantity = findViewById(R.id.product_Quantity);
        product_Price = findViewById(R.id.product_Price);
        circularImage = findViewById(R.id.circularImage);
        product_Description = findViewById(R.id.product_Description);
        backBtn = findViewById(R.id.backBtn);
        product_Category = findViewById(R.id.product_Category);
        Addproduct = findViewById(R.id.Addproduct);
        loadingBar = new ProgressDialog(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        circularImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        Addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
        product_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });
        product_Quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityDialog();
            }
        });
    }

    private void quantityDialog()
    {
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantity Category")
                .setItems(QuantityCategory.quantityCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //get picked category
                        String Qcategory = QuantityCategory.quantityCategories[which];

                        //set picked category
                        product_Quantity.setText(Qcategory);
                    }
                })
                .show();
    }

    private void categoryDialog()
    {
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category")
                .setItems(ProductCategory.productCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //get picked category
                        String category = ProductCategory.productCategories[which];

                        //set picked category
                        product_Category.setText(category);
                    }
                })
                .show();
    }

    private void OpenGallery()
    {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            circularImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData()
    {

        Description = product_Description.getText().toString();
        Pname = product_name.getText().toString();
        Price = product_Price.getText().toString();
        Quantity = product_Quantity.getText().toString();
        CategoryName = product_Category.getText().toString();

        if (ImageUri ==null)
        {
            Toast.makeText(this, "Product Image is Required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write Product Description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write Product Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write Product Price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Quantity))
        {
            Toast.makeText(this, "Please select Quantity", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(CategoryName))
        {
            Toast.makeText(this, "Please Select Product Category", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {

        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Dear Admin, Please wait while we are adding the New Product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath =ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filepath.putFile(ImageUri);

        //Upload Picture to the Database
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) 
            {
                Toast.makeText(AddProductActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AddProductActivity.this, "getting Product Image Url Successfully", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }

                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase()
    {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category",""+CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("quantity",""+Quantity);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AddProductActivity.this,ManageProductsActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AddProductActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}