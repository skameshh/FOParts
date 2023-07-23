package com.example.foparts;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "FOParts";
    private Button btnSubmit;
    public static EditText txtConfirmationNumber;
    private TextView lblMainTitle = null;
    private EditText txtVal2;
    private TextView lblMessage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtConfirmationNumber = findViewById(R.id.txtConfirmationNumber);
        txtVal2 = txtConfirmationNumber;
        lblMainTitle = findViewById(R.id.lblMainTitle);
        lblMainTitle.setText(Html.fromHtml(getString(R.string.title_main)));
        lblMessage = findViewById(R.id.lblMessage);
        lblMessage.setText("");

        doLoadFragment();

        try {
            Intent intent = getIntent();
            String conf_number = intent.getStringExtra("conf_num");
            txtConfirmationNumber.setText(conf_number);
        }catch (Exception ee){
            Log.v(TAG,"Error in "+ee.getLocalizedMessage());
        }

       //boolean bb = com.hal.mfgsystems.Global.checkNetwork(this);

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

    public void startStatusActivity(View view){
        try {
            lblMessage.setText("");
            String conf_nm =  txtConfirmationNumber.getText().toString();
            if(conf_nm.length()<8){
                Toast.makeText(this, "Confirmation number should not be less than 8 chars ", Toast.LENGTH_LONG).show();
                txtConfirmationNumber.setText("");
                txtConfirmationNumber.requestFocus();
                lblMessage.setText("Confirmation number format error");
                return;
            }

            if(conf_nm.length()>10){
                Toast.makeText(this, "Confirmation number should not be greater than 8 chars ", Toast.LENGTH_LONG).show();
                txtConfirmationNumber.setText("");
                txtConfirmationNumber.requestFocus();
                lblMessage.setText("Confirmation number format error");
                return;
            }

            Intent intent = new Intent(MainActivity .this, StatusUpdateActivity.class);
            intent.putExtra("conf_num", txtConfirmationNumber.getText().toString());
            startActivity(intent);
        }catch(Exception e){
            Log.v(TAG,"Error in "+e.getLocalizedMessage());
        }
    }

    public void startScanner(View view){
        try {
            Intent intent = new Intent(MainActivity.this, ScanActivityForLogin.class);
            startActivity(intent);
        }catch(Exception e){
            Log.v(TAG,"Error in "+e.getLocalizedMessage());
        }
    }



    private String getStrDate(){
        String pattern = "ddMMyyyy-HHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    private void doSendEmail_OLD(){
        // Recipient's email ID needs to be mentioned.
        String to = "skameshh@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "foparts@halliburton.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException e) {
            e.printStackTrace();
            Log.v(TAG,"Error in doEmail "+e.getLocalizedMessage());
            Toast.makeText(this, "Submit failed "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Session session;
    private String to_email="kamesh.shankaran@halliburton.com";
    private String subject = "Android email test  "+getStrDate();
    private String message = "This is my test message 2";
    private void doSendGmail_OLD(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("skameshh@gmail.com", "mwfozalgwyzfvdyx");
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress("skameshh@gmail.com"));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email));
            mm.setSubject(subject);
            mm.setText(message);
            Transport.send(mm);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }


    }


    public void doEmail(View view){
        try {
          String val1 =   txtConfirmationNumber.getText().toString();
          String val2 =   txtVal2.getText().toString();
          subject = "[POParts]-"+getStrDate();
          message  = "val1:"+val1 +"\n val2:"+val2 +"\n";

          Log.v(TAG,message);
            //Send from Gmail
           SendMail sm = new SendMail(this, to_email, subject, message);
            sm.execute();

           // SendHalEmail sm = new SendHalEmail(this, to_email, subject, message);
           // sm.execute();

            //Email


        }catch(Exception e){
            e.printStackTrace();
            Log.v(TAG,"Error in doEmail "+e.getLocalizedMessage());
            Toast.makeText(this, "Submit failed "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        //clear contents
        txtConfirmationNumber.setText("");
        txtVal2.setText("");
        Toast.makeText(this, "Submitted successfully ", Toast.LENGTH_LONG).show();
    }
}