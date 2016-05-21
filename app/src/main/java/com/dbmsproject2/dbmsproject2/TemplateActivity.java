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
import android.widget.Button;
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
import java.util.Random;

public class TemplateActivity extends AppCompatActivity {

    String nameAnswer, placeAnswer, numberAnswer, website;
    TextView passPhrase;
    final int NOOFRANDOMNUMBERSUSED = 2;
    final String SEPERATOR = ":";

    String fillerWords[] = new String[] {"with" ,"on","in","where","from","if"};
    char[] specialCharacters = new char[] {'&','!','#','%','^','*'};

    String finalPasswordGenerated;
    GenerateTemplateBackgroundWorker backgroundWorker;
    String tempID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        nameAnswer = intent.getStringExtra(SurveyActivity.NameParam);
        placeAnswer = intent.getStringExtra(SurveyActivity.PlaceParam);
        numberAnswer = intent.getStringExtra(SurveyActivity.NumberParam);
        website = intent.getStringExtra(SurveyActivity.WebsiteParam);

        backgroundWorker = new GenerateTemplateBackgroundWorker();
        backgroundWorker.execute(nameAnswer,placeAnswer,numberAnswer);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    //Method to get the position from the list
    private boolean isFillerWord(String word){
        boolean retValue = false;
        try{
            for(String filler: fillerWords){
                if (filler.equalsIgnoreCase(word)){
                    retValue = true;
                    break;
                }
            }
        }
        catch(Exception ex){
            System.out.println("Exception occurred in the isFillerWord: " +ex);
        }
        return retValue;
    }


    //Method to get the special character based on the position from the filler words
    private char getSpecialCharacter(int position){
        return specialCharacters[position];
    }

    //Method to generate random numbers based on the max limit
    private int generateRandomNumber(int maxLimit){
        Random rand = new Random();
        return rand.nextInt(maxLimit);
    }



    public void btnGenPassword(View view){
        boolean isCapital = false;
        String newPassword = new String();
        char value;
        int[] phraseNumber = new int[NOOFRANDOMNUMBERSUSED];
        try{
            String phrase = passPhrase.getText().toString();
            String[] words = phrase.split(" ");
            int capitalNumber = generateRandomNumber(words.length - 1); //no of words in the phrase
            for (int outIterator =0; outIterator< words.length; outIterator++){
                String word = words[outIterator];

                if(outIterator == capitalNumber)
                    isCapital = true;

                //Check to see if the word is a filler word or not
                if (isFillerWord(word)){
                    //check to see if the capital number is the same postion as the filler word then we need to recalculate the
                    // capital letter
                    if (outIterator == capitalNumber)
                        capitalNumber = generateRandomNumber(words.length-1);
                    else{
                        value = getSpecialCharacter(generateRandomNumber(specialCharacters.length -1));
                        newPassword +=value;
                    }
                }
                else{
                    // Iterate and get two randomly selected characters to be appended to the string
                    for(int iterator=0; iterator <NOOFRANDOMNUMBERSUSED ; iterator ++){
                        int pos = generateRandomNumber(word.length()-1);
                        value = word.charAt(pos); // extract the password based on the position
                        if (Character.isDigit(value)) //if the character is a digit then add it the phrasenumber array
                            phraseNumber[iterator] = Character.getNumericValue(value);
                        else{
                            if(isCapital){
                                newPassword+= Character.toUpperCase(value);
                                isCapital = false;
                            }
                            else
                                newPassword+=value;
                        }
                    }
                }
            }
            //appending the numbers to the first and the last of the new password string
            newPassword = phraseNumber[0] + newPassword + phraseNumber[1];
            TextView finalPassword =(TextView) findViewById(R.id.tvFinalPassword);
            finalPassword.setText(newPassword);
            finalPasswordGenerated = newPassword;
        }
        catch(Exception ex){
            Log.e("Error","Exception occurred in the btnGenPassword: " + ex.getMessage());
        }
    }

    public void btnsavePassword(View view) {
        try{
            String phrase = passPhrase.getText().toString();
            //Background worker to save the template
            SaveTemplateBackGroundWorker tempWorker = new SaveTemplateBackGroundWorker();
            tempWorker.execute(phrase,tempID);
            //BackGround Worker to save the password
            SavePasswordBackgroundWorker saveWorker = new SavePasswordBackgroundWorker();
            saveWorker.execute(LoginActivity.UserID,website,finalPasswordGenerated,tempID);

        }
        catch(Exception ex){
            Log.e("Error","Exception occurred in the btnsavePassword: " +ex.getMessage());
        }
    }

    public void btnGenerateTemplate(View view){
        try{
            //Generate a new Phrase for the user
            Toast.makeText(getApplicationContext(),"Generating a new Phrase",Toast.LENGTH_LONG).show();
            backgroundWorker = new GenerateTemplateBackgroundWorker();
            backgroundWorker.execute(nameAnswer, placeAnswer, numberAnswer);
        }
        catch(Exception ex){
            Log.e("Error","Exception has occurred in the btnGenerateTemplate: " +ex.getMessage());
        }
    }

    public class GenerateTemplateBackgroundWorker extends AsyncTask<String,Void,String>{
        String returnValue = null;
        @Override
        protected String doInBackground(String... params) {
            try{
                String nameParam = params[0];
                String placeParam = params[1];
                String numberParam = params[2];

                URL url = new URL("http://newandroidapp.esy.es/test1/GenerateTemplate.php");
                URLConnection con = url.openConnection();

                String data = URLEncoder.encode("name_ques","UTF-8") +
                        "=" + URLEncoder.encode(nameParam,"UTF-8");
                data += "&" + URLEncoder.encode("place_ques","UTF-8") +
                        "=" + URLEncoder.encode(placeParam,"UTF-8");
                data += "&" + URLEncoder.encode("num_ques","UTF-8") +
                        "=" + URLEncoder.encode(numberParam,"UTF-8");

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
                Log.e("Error","Exception occurred in the TemplateBackgroundWorker: "+ex);
            }
            return returnValue;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String[] data = result.split(":");
            String phrase = data[0];
            phrase = phrase.replace("\t","");
            tempID = data[1];
            passPhrase = (TextView) findViewById(R.id.tvAnsPassPhrase);
            passPhrase.setText(phrase);
        }
    }


    public class SaveTemplateBackGroundWorker extends AsyncTask<String,Void,String> {
        String retValue = null;
        @Override
        protected String doInBackground(String... params) {
            try{
                String passPhraseParam = params[0];
                String tempIDParam = params[1];

                URL url = new URL("http://newandroidapp.esy.es/test1/SaveTemplate.php");
                URLConnection con = url.openConnection();

                String data = URLEncoder.encode("Template","UTF-8") +
                        "=" + URLEncoder.encode(passPhraseParam,"UTF-8");
                data += URLEncoder.encode("tempID","UTF-8") +
                        "=" + URLEncoder.encode(tempIDParam,"UTF-8");
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
                Log.e("ERROR","Exception occured in the SaveTemplateBackgroundWorker: "+ex.getMessage());
            }
            return retValue;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
        }
    }

    public class SavePasswordBackgroundWorker extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String retValue =null;
            try{
                String userIDParam = params[0];
                String websiteParam = params[1];
                String newPasswordParam = params[2];
                String templateParam = params[3];

                URL url = new URL("http://newandroidapp.esy.es/test1/SavePassword.php");
                URLConnection con = url.openConnection();

                String data = URLEncoder.encode("userID", "UTF-8") +
                        "=" + URLEncoder.encode(userIDParam,"UTF-8");
                data += "&" + URLEncoder.encode("website","UTF-8") +
                        "=" + URLEncoder.encode(websiteParam,"UTF-8");
                data += "&" + URLEncoder.encode("password","UTF-8") +
                        "=" + URLEncoder.encode(newPasswordParam,"UTF-8");
                data += "&" + URLEncoder.encode("template","UTF-8") +
                        "=" + URLEncoder.encode(templateParam,"UTF-8");

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
                System.out.println("Exception in SavePasswordBackgroundWorker: " + ex);
            }
            return  retValue;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        }

    }

}
