package com.dev334.aircache.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.dev334.aircache.R;


public class PaymentActivity extends AppCompatActivity {

    private Button payButton;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        payButton = findViewById(R.id.button_pay);
        amount = "200";
        int price = Math.round(Float.parseFloat(amount));


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}