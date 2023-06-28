package com.example.foparts;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendHalEmail extends AsyncTask {
    private static String TAG = "FOParts";
    private Context context;
    private Session session;
    private String email;
    private String subject;
    private String message;

    public SendHalEmail(Context context, String email, String subject, String message){
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    private String from_email = "foparts@halliburton.com";
    private String pwd = "mwfozalgwyzfvdyx";
    @Override
    protected Object doInBackground(Object[] objects) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.corp.halliburton.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.user", "kamesh.shankaran@halliburton.com");

        session = Session.getDefaultInstance(props);

        session.setDebug(true);

        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(from_email));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);
            //Transport.send(mm);

            Transport t = session.getTransport("smtp");
            t.connect(from_email, pwd);
            t.sendMessage(mm, mm.getAllRecipients());
            t.close();

            Log.v(TAG,"\nSend email success \n");
        }
        catch (MessagingException e) {
            e.printStackTrace();
            Log.v(TAG,"Error in doEmail "+e.getLocalizedMessage());
        }
        return null;
    }

}
