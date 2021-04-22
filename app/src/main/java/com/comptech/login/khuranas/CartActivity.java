package com.comptech.login.khuranas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comptech.login.khuranas.Model.Cart;
import com.comptech.login.khuranas.Prevalent.Prevalent;
import com.comptech.login.khuranas.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalBtn, Msgtxt;
    DatabaseReference cartListRef;
    private int overallTotalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = (Button) findViewById(R.id.next_btn);
        txtTotalBtn = (TextView) findViewById(R.id.total_price_txt);
        Msgtxt = (TextView) findViewById(R.id.msgtxt);
        Msgtxt.setVisibility(View.GONE);

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTotalBtn.setText("$" + String.valueOf(overallTotalPrice));
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }
    @Override
    protected void onStart() {
            super.onStart();
            checkOrderState();

            FirebaseRecyclerOptions<Cart> options =
                    new FirebaseRecyclerOptions.Builder<Cart>()
                            .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Product"), Cart.class)
                            .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter
       = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText(model.getQuantity());
                holder.txtProductPrice.setText(model.getPrice());

               Picasso.get().load(model.getImage()).into(holder.imgProductImage);

               int oneTypeProductPrice=((Integer.valueOf(model.getPrice())))* Integer.valueOf( model.getQuantity());
               overallTotalPrice=overallTotalPrice+oneTypeProductPrice;


                holder.vRectangle_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
                holder.vRectangle_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Product")
                                .child(model.getPid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(CartActivity.this, "Item will be Removed", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(CartActivity.this,CartActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                });
            }
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout,viewGroup,false);
           CartViewHolder holder=new CartViewHolder(view);

           return holder;
            }

        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private void checkOrderState()
    {

        DatabaseReference ordersRef;
        ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
             if(dataSnapshot.exists())
             {
                 String shippingState="";
                  shippingState=dataSnapshot.child("State").getValue().toString();

                 if(shippingState.equals("shipped"))
                 {
                     txtTotalBtn.setText("Shipping in process");
                     recyclerView.setVisibility(View.GONE);
                     Msgtxt.setVisibility(View.VISIBLE);
                     Msgtxt.setText("Congratulations your order will be shipped.Soon you will recieved at your door step");
                     NextProcessBtn.setVisibility(View.GONE);
                     Toast.makeText(CartActivity.this, "Your Order will be in process.", Toast.LENGTH_SHORT).show();

                 }
                 else if(shippingState.equals("not shipped"))
                 {
                     txtTotalBtn.setText("Shipping not verified");
                     recyclerView.setVisibility(View.GONE);
                     Msgtxt.setVisibility(View.VISIBLE);
                     NextProcessBtn.setVisibility(View.GONE);
                     Toast.makeText(CartActivity.this, "Your Order will be in process.", Toast.LENGTH_SHORT).show();

                 }

             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
