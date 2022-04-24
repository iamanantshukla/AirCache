package com.dev334.aircache.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dev334.aircache.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;


public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

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
                Checkout checkout=new Checkout();

                checkout.setKeyID("rzp_test_E517qLEzdsjiCV");

                checkout.setImage(R.drawable.bg_pin);

                JSONObject object=new JSONObject();


                try {
                    object.put("name","Dev334");
                    object.put("description","Payment for AirCache");
                    object.put("them e.color","#0093DD");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    object.put("prefill.contact","8529467827");
                    object.put("prefill.email","oneon334@gmail.com");
                    checkout.open(PaymentActivity.this,object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");

        builder.setMessage(s);
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }
}