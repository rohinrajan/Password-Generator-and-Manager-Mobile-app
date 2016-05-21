package com.dbmsproject2.dbmsproject2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ForgotPassPhraseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_phrase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void btnSendPassPhrase(View view){
        SendPassPhraseBackgroundWorker worker = new SendPassPhraseBackgroundWorker();
        EditText websiteTextView = (EditText) findViewById(R.id.etforgotPassPhrase);
        String websiteString = websiteTextView.getText().toString();
        worker.execute("",websiteString);
        //Toast.makeText(getApplicationContext(), "Sent pass phrase to email id..", Toast.LENGTH_SHORT).show();
    }


    public class SendPassPhraseBackgroundWorker extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String retValue = null;
            try{
                String website = params[0];
                //// TODO: 26-11-2015 need to send the userid as well
                URL url = new URL("http://newandroidapp.esy.es/test1/forgot_pass.php");
                URLConnection con = url.openConnection();

                String data = URLEncoder.encode("website", "UTF-8") +
                        "=" + URLEncoder.encode(website,"UTF-8");
                data += "&" + URLEncoder.encode("user_id","UTF-8") +
                        "=" + URLEncoder.encode(LoginActivity.UserID,"UTF-8");

                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer sBuilder = new StringBuffer();
                String line;

                //Read the entire content
                while((line = reader.readLine()) != null){
                    sBuilder.append(line);
                    break;
                }
                retValue = sBuilder.toString();


            }
            catch(Exception ex){
                System.out.println("Exception occurred in teh sendPassPhrase: "+ex);
            }

            return retValue;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }
    }

}
