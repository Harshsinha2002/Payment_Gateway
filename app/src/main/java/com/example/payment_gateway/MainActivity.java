package com.example.payment_gateway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    EditText Name ;
    EditText Email_ID;
    EditText Phone_No;
    EditText Adults_Count;
    ProgressBar progressBar;

    TextView Total_amount;
    TextView paymentTxt;

    String name, email, phone_no, adult_count;
    String total_Amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = findViewById(R.id.name_txt);
        Email_ID = findViewById(R.id.email_txt);
        Phone_No = findViewById(R.id.phoneNo_txt);
        Button Get_Amt = findViewById(R.id.get_amt_btn);
        Adults_Count = findViewById(R.id.No_of_Adults_txt);
        progressBar  = findViewById(R.id.progression_bar);
        Total_amount  = findViewById(R.id.total_Amount);

        Checkout.preload(getApplicationContext());
        paymentTxt = findViewById(R.id.payment_txt);
        Button paymentBtn = findViewById(R.id.payment_btn);

        Get_Amt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                adult_count = Adults_Count.getText().toString();
                if(TextUtils.isEmpty(adult_count))
                {
                    Adults_Count.setError("Required!!");
                    progressBar.setVisibility(View.GONE);
                }

                else
                {
                    int total = Integer.parseInt(adult_count) * 100;
                    Total_amount.setText(Integer.toString(total));
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                String Amount = Total_amount.getText().toString();
                if(TextUtils.isEmpty(Amount))
                {
                    Toast.makeText(MainActivity.this, "ERROR!!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    startPayment();
                }
            }
        });
    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_IdUsGFjdy0i2oR");

        name = Name.getText().toString();
        email = Email_ID.getText().toString();
        phone_no = Phone_No.getText().toString();
        String Amount = Total_amount.getText().toString();
        int Total = Integer.parseInt(Amount) * 100;
        total_Amt = Integer.toString(Total);

        if(name.isEmpty() || email.isEmpty() || phone_no.isEmpty() || total_Amt.isEmpty())
        {
            Toast.makeText(MainActivity.this, "PLEASE COMPLETE ALL THE FIELD!!" , Toast.LENGTH_LONG).show();
        }


        else
        {
            /**
             * Set your logo here
             */
            checkout.setImage(R.drawable.img);

            /**
             * Reference to current activity
             */
            final Activity activity = this;

            /**
             * Pass your payment options to the Razorpay Checkout as a JSONObject
             */
            try {
                JSONObject options = new JSONObject();

                options.put("name", "DELHI TOURISM");
                options.put("description", "Reference No. #123456");
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
                //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("amount", total_Amt);//pass amount in currency subunits (in cents)
                options.put("prefill.email", email);
                options.put("prefill.contact", phone_no);
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch (Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        }
    }

    @Override
    public void onPaymentSuccess(String s)
    {
        paymentTxt.setText("Sucessful Payment ID : " + s);
    }

    @Override
    public void onPaymentError(int i, String s)
    {
        Toast.makeText(MainActivity.this, "Payment Failure", Toast.LENGTH_LONG);
    }
}