package com.comptech.login.khuranas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comptech.login.khuranas.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText etxtFullName ,etxtPhoneNumber,etxtHomeAddress,etxtCityName;
    private Button shippmentBackbtn,shippmentConfirmBtn;
    private String totalAmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price is :"+totalAmount, Toast.LENGTH_SHORT).show();
        etxtFullName=(EditText)findViewById(R.id.shippment_name);
        etxtPhoneNumber=(EditText)findViewById(R.id.shippment_phone);
        etxtHomeAddress=(EditText)findViewById(R.id.shippment_home_address);
        etxtCityName=(EditText)findViewById(R.id.shippment_city_name);
        shippmentBackbtn=(Button)findViewById(R.id.shippment_back_btn);
        shippmentConfirmBtn=(Button)findViewById(R.id.shippment_confirm_btn);

        shippmentConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check()
    {
        if(TextUtils.isEmpty(etxtFullName.getText().toString()))
        {
            Toast.makeText(this, "Name is Empty", Toast.LENGTH_SHORT).show();
        }
      else if(TextUtils.isEmpty(etxtPhoneNumber.getText().toString()))
        {
            Toast.makeText(this, "Phone number is Empty", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(etxtHomeAddress.getText().toString()))
        {
            Toast.makeText(this, "Home Address is Empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(etxtCityName.getText().toString()))
        {
            Toast.makeText(this, "City Name is Empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            confirmOrder();
        }

    }

    private void confirmOrder()
    {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference OrdersRef=FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String ,Object>orderMap=new HashMap<>();
        orderMap.put("Total Amount",totalAmount);
        orderMap.put("Name",etxtFullName.getText().toString());
        orderMap.put("Phone",etxtPhoneNumber.getText().toString());
        orderMap.put("Address",etxtHomeAddress.getText().toString());
        orderMap.put("City",etxtCityName.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("State","not shipped");

        OrdersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Order will be Placed", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}
