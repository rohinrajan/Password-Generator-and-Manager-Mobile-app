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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class RegisterationActivity extends AppCompatActivity {

    final String Format = "UTF-8";
    public class RegisterBackgroundWorker extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String retValue=null;

            String paramName = params[0];
            String paramEmail = params[1];
            String paramUserName = params[2];
            String paramNewPassword = params[3];

            try{
                URL url = new URL("http://newandroidapp.esy.es/test1/register.php");
                URLConnection con = url.openConnection();

                String query = generatePostRequest("Name",paramName,"Email_Id",paramEmail,
                        "username",paramUserName,"password",paramNewPassword);
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(query);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                query = null;

                while(query == null){
                    query = reader.readLine();
                }
                retValue = query;
            }
            catch(Exception ex){
                Log.e("Error","exception occurred in RegisterationActivity: " +ex);
            }
            return retValue;
        }

        private String generatePostRequest(String... params) {
            StringBuilder strbuilder = new StringBuilder();
            try{
                for(int it = 0; it< params.length; it+=2){
                    String data = URLEncoder.encode(params[it], Format);
                    data+= "=" + URLEncoder.encode(params[it+1],Format);
                    strbuilder.append(data);
                    strbuilder.append("&");
                }
            }
            catch (Exception ex){
                Log.e("Error","Exception occurred in generatePostRequest " +ex);
            }
            return strbuilder.toString();
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            if (result.equalsIgnoreCase("Registered Successfully")){
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
    }

    //Registration button click event handler
    public void btnNewUser(View view){
        String Name = ((EditText)findViewById(R.id.etName)).getText().toString();
        String Email = ((EditText)findViewById(R.id.etEmailID)).getText().toString();
        String Username = ((EditText)findViewById(R.id.etUsername)).getText().toString();
        String Password = ((EditText)findViewById(R.id.etNewPassword)).getText().toString();

        RegisterBackgroundWorker worker = new RegisterBackgroundWorker();
        worker.execute(Name,Email,Username,Password);
        

    }
}
