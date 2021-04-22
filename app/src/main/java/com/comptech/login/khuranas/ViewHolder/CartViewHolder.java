package com.comptech.login.khuranas.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.comptech.login.khuranas.Interface.ItemClickListner;
import com.comptech.login.khuranas.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtProductID;
    public View vRectangle_delete,vRectangle_edit;
    public ImageView imgProductImage;

    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_view_product_name);
        vRectangle_delete=itemView.findViewById(R.id.rectangle_for_delete);
        vRectangle_edit=itemView.findViewById(R.id.rectangle_for_edit);
        txtProductPrice=itemView.findViewById(R.id.cart_view_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_view_show_quantity);
        imgProductImage=(ImageView)itemView.findViewById (R.id.cart_view_product_image);


    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.itemClickListner = listner;
    }

    @Override
    public void onClick(View view)
    {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }
}

