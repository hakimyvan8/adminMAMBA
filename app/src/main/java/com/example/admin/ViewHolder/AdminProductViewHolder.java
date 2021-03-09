package com.example.admin.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Interface.ItemClickListener;
import com.example.admin.R;

public class AdminProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductCategory,product_Quantity,units;
    public ImageView imageView,next;
    public ItemClickListener listener;

    public AdminProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.item_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        units = itemView.findViewById(R.id.units);
        txtProductCategory= (TextView) itemView.findViewById(R.id.item_Category);
        txtProductPrice = (TextView) itemView.findViewById(R.id.item_price);
        product_Quantity = itemView.findViewById(R.id.product_Quantity);
        next = itemView.findViewById(R.id.next);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
