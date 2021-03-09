package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.Models.AdminProducts;
import com.example.admin.ViewHolder.AdminProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ManageProductsActivity extends AppCompatActivity {

    private TextView AddProductTv;
    private RecyclerView recyclerView;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        setContentView(R.layout.activity_manage_products);
        recyclerView = findViewById(R.id.AdminPorfileRv);
        recyclerView.setHasFixedSize(true);
        AddProductTv = findViewById(R.id.AddProductTv);

        AddProductTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProductsActivity.this,AddProductActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AdminProducts> options =
                new FirebaseRecyclerOptions.Builder<AdminProducts>()
                        .setQuery(ProductsRef, AdminProducts.class)
                        .build();
        FirebaseRecyclerAdapter<AdminProducts, AdminProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminProducts, AdminProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position, @NonNull final AdminProducts model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductCategory.setText(model.getCategory());
                        holder.product_Quantity.setText(model.getQuantity());
                        holder.txtProductPrice.setText(model.getPrice() + "RWF");
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(ManageProductsActivity.this, EditProductActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_admin_layout,parent,false);
                        AdminProductViewHolder holder = new AdminProductViewHolder(view);
                        return holder;

                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}