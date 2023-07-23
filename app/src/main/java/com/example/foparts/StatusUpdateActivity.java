package com.example.foparts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.Session;

public class StatusUpdateActivity extends AppCompatActivity {
    private static String TAG = "FOParts";
    private String conf_number = "";
    private TextView lblConfirmNumber = null;
    private TextView lblTitle, lblNetworkStatus = null;
    private Button btnSubmit = null;

    private Switch switchPartReceived,switchProcessStarted, switchProcessFinished, switchQCFinished, switchSentDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_update);
        lblConfirmNumber = findViewById(R.id.lblConfirmNumber);
        doLoadFragment();
        lblTitle = findViewById(R.id.lblTitle);
        lblTitle.setText(Html.fromHtml(getString(R.string.title_update_part_status)));
        lblNetworkStatus = findViewById(R.id.lblNetworkStatus);
        lblNetworkStatus.setText("");
        btnSubmit = findViewById(R.id.btnSubmit);
        initSwitchUI();

        try {
            Intent intent = getIntent();
            conf_number = intent.getStringExtra("conf_num");
            lblConfirmNumber.setText(conf_number);
        }catch (Exception ee){
            Log.v(TAG,"Error in "+ee.getLocalizedMessage());
        }

        if(!isConecctedToInternet()){
            Toast.makeText(this, "Need interent connection to submit ", Toast.LENGTH_LONG).show();
            btnSubmit.setTextColor(Color.RED);
            lblNetworkStatus.setTextColor(Color.RED);
            lblNetworkStatus.setText("Network Failed");
        }else{
            Toast.makeText(this, " Internet seems ok  ", Toast.LENGTH_LONG).show();
        }

    }

    private void initSwitchUI(){

        switchPartReceived = findViewById(R.id.switchPartReceived);
        switchProcessStarted = findViewById(R.id.switchProcessStarted);
        switchProcessFinished = findViewById(R.id.switchProcessFinished);
        switchQCFinished = findViewById(R.id.switchQCFinished);
        switchSentDelivery = findViewById(R.id.switchSentDelivery);

        switchSentDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchSentDelivery.isChecked()){
                    step5True();
                }

            }
        });

        switchQCFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchQCFinished.isChecked()){
                    step4True();
                }
            }
        });

        switchProcessFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchProcessFinished.isChecked()){
                    step3True();
                }
            }
        });

        switchProcessStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchProcessStarted.isChecked()){
                    step2True();
                }
            }
        });

        switchPartReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void step2True(){
        switchPartReceived.setChecked(true);
    }

    private void step3True(){
        switchProcessStarted.setChecked(true);
        switchPartReceived.setChecked(true);
    }

    private void step4True(){
        switchProcessFinished.setChecked(true);
        switchProcessStarted.setChecked(true);
        switchPartReceived.setChecked(true);
    }
    private void step5True(){
        switchQCFinished.setChecked(true);
        switchProcessFinished.setChecked(true);
        switchProcessStarted.setChecked(true);
        switchPartReceived.setChecked(true);
    }

    private Session session;
    private String to_email="kamesh.shankaran@halliburton.com";
    private String subject = "";
    private String message = "";

    public void doEmail(View view){
        try {
            String val1 =   lblConfirmNumber.getText().toString();
            String val2 =   lblConfirmNumber.getText().toString();
            subject = "[FOParts]-"+getStrDate();
            message  = "val1:"+val1 +"\n val2:"+val2 +"\n";

            String step1_part_received = "OFF";
            String step2_proc_started = "OFF";
            String step3_proc_finished = "OFF";
            String step4_qc_finished = "OFF";
            String step5_sent_delivery = "OFF";

            Log.v(TAG,message);
//switchPartReceived,switchProcessStarted, switchProcessFinished, switchQCFinished, switchSentDelivery
            if (switchPartReceived.isChecked()){
                step1_part_received = "ON";
            }

            if (switchProcessStarted.isChecked()){
                step2_proc_started = "ON";
            }

            if (switchProcessFinished.isChecked()){
                step3_proc_finished = "ON";
            }

            if (switchQCFinished.isChecked()){
                step4_qc_finished = "ON";
            }

            if (switchSentDelivery.isChecked()){
                step5_sent_delivery = "ON";
            }
            message  = "confnum:"+conf_number
                    +"\n step1:"+step1_part_received
                    +"\n step2:"+step2_proc_started
                    +"\n step3:"+step3_proc_finished
                    +"\n step4:"+step4_qc_finished
                    +"\n step5:"+step5_sent_delivery
                    +"\n";

            if(checkNetwork()){
                btnSubmit.setTextColor(Color.WHITE);
                //Send from Gmail
                SendMail sm = new SendMail(this, to_email, subject, message);
                sm.execute();

                Toast.makeText(this, "Submitted successfully ", Toast.LENGTH_LONG).show();

                gotoMainActivity();
            }else{
                Toast.makeText(this, "Need interent connection to submit ", Toast.LENGTH_LONG).show();
                btnSubmit.setTextColor(Color.RED);
                lblNetworkStatus.setTextColor(Color.RED);
                lblNetworkStatus.setText("Network Failed");
            }

        }catch(Exception e){
            e.printStackTrace();
            Log.v(TAG,"Error in doEmail "+e.getLocalizedMessage());
            Toast.makeText(this, "Submit failed "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return;
        }

    }

    public boolean isConecctedToInternet() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkNetwork(){
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

            if(connected){
                //check whether got internet
                connected =  isConecctedToInternet();
            }

        }catch (Exception ee){
            Toast.makeText(this, "network connection failed "+ee.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        return connected;
    }

    private String getStrDate(){
        String pattern = "ddMMyyyy-HHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    private void doLoadFragment(){
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.frameLayout, new HeaderFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }



    public void gotoMainActivity(){
        try {
            Intent intent = new Intent(StatusUpdateActivity.this, MainActivity.class);
            intent.putExtra("conf_num", "");
            startActivity(intent);
        }catch(Exception e){
            Log.v(TAG,"Error in "+e.getLocalizedMessage());
        }
    }

}