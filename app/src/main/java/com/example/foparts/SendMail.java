package com.example.foparts;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends AsyncTask {
    private static String TAG = "FOParts";
    private Context context;
    private Session session;
    private String email = "kamesh.shankaran@halliburton.com";
    private String subject;
    private String message;

    public SendMail(Context context, String email, String subject, String message){
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    private String from_email = "halauto.foparts@gmail.com";// "skameshh@gmail.com";
    private String pwd = "hvbzudpwcdwzjwzu" ;//"mwfozalgwyzfvdyx";
    @Override
    protected Object doInBackground(Object[] objects) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.user", from_email);

        session = Session.getDefaultInstance(props);

        session.setDebug(true);

        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(from_email));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.addRecipient(Message.RecipientType.CC, new InternetAddress("skameshh@outlook.com"));
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
            Toast.makeText(context, "Submitted failed, Please try again ", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
