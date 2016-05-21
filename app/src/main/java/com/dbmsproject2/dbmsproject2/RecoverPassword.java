package com.dbmsproject2.dbmsproject2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class RecoverPassword extends AppCompatActivity {

    public static final String WebsiteMSg = "WEBSITE";
    public static final String PasswordMsg = "PASSWORD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
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

    public void btnrevPassword(View view){
        String webParam = ((EditText) findViewById(R.id.etregWebsite)).getText().toString();
        String phraseParam = ((EditText) findViewById(R.id.etregPassPhrase)).getText().toString();
        RecoverPasswordBackgroundWorker worker = new RecoverPasswordBackgroundWorker();
        worker.execute(LoginActivity.UserID,webParam,phraseParam);
    }



    public class RecoverPasswordBackgroundWorker extends AsyncTask<String,Void,String> {
        String returnValue = null;
        @Override
        protected String doInBackground(String... params) {
            try{
                String userParam = params[0];
                String websiteParam = params[1];
                String phraseParam = params[2];

                URL url = new URL("http://newandroidapp.esy.es/insert_password.php");
                URLConnection con = url.openConnection();

                String data = URLEncoder.encode("user_id", "UTF-8") +
                        "=" + URLEncoder.encode(userParam,"UTF-8");
                data += "&" + URLEncoder.encode("website","UTF-8") +
                        "=" + URLEncoder.encode(websiteParam,"UTF-8");
                data += "&" + URLEncoder.encode("pass_phrase","UTF-8") +
                        "=" + URLEncoder.encode(phraseParam,"UTF-8");
                data += "&" + URLEncoder.encode("value","UTF-8") +
                        "=" + URLEncoder.encode("value","UTF-8");

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
                returnValue = sBuilder.toString();
            }
            catch(Exception ex){
                Log.e("Error", "Exception occurred in the TemplateBackgroundWorker: " + ex);
            }
            return returnValue;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),PasswordShowActivity.class);
            String wbSite = ((EditText)findViewById(R.id.etregWebsite)).getText().toString();
            intent.putExtra(WebsiteMSg,wbSite);
            intent.putExtra(PasswordMsg, result);
            startActivity(intent);

        }

    }
    public void btnforgotPassPhrase(View view){
        Intent intent = new Intent(getApplicationContext(),ForgotPassPhraseActivity.class );
        startActivity(intent);
    }
}
