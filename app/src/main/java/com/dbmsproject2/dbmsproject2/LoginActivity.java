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
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText Username, Password;

    String username, password;
    public static String UserID;
    class loginHelper extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String paramUsername = params[0];
            String paramPassword = params[1];
            String returnValue = null;
            try{
                URL url = new URL("http://newandroidapp.esy.es/test1/login.php");
                URLConnection con = url.openConnection();

                String data = URLEncoder.encode("username","UTF-8") +
                        "=" + URLEncoder.encode(paramUsername,"UTF-8");
                data += "&" + URLEncoder.encode("password","UTF-8") +
                         "=" + URLEncoder.encode(paramPassword,"UTF-8");

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
            catch (Exception ex){
                Log.e("Error","Exception occurred in Background worker: "+ex.getMessage());
            }
            return returnValue;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            String[] data = result.split(":");
            if(data[0].equals("Successful")) {
                Toast.makeText(getApplicationContext(),"Welcome  back!....",Toast.LENGTH_SHORT).show();
                UserID = data[1];
                Intent intent = new Intent(getApplicationContext(), PurposeActivity.class);
                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(),"Unable to login....Please check",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View view) {
            //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
            //}
        //});
    }

    // Login button event click handler
    public void btnLogin(View view){
        Username = (EditText) findViewById(R.id.etUsername);
        Password = (EditText) findViewById(R.id.etPassword);
        username = Username.getText().toString();
        password = Password.getText().toString();
        ValidateUserCredentails(username, password);
    }

    // Method to validate the user credentials
    private void ValidateUserCredentails(String username, String password) {
        if (username.length() == 0 || password.length() == 0){
            Toast.makeText(getApplicationContext(),"Cannot have blank username",Toast.LENGTH_LONG).show();
        }
        else{
            loginHelper helper = new loginHelper();
            helper.execute(username,password);
        }
    }

    //Register button event click handler
    public void btnRegister(View view){
        Intent intent = new Intent(this, RegisterationActivity.class);
        startActivity(intent);
    }
}
