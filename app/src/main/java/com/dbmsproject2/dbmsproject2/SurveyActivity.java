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

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class SurveyActivity extends AppCompatActivity {

    TextView surquest1,surquest2,surquest3;
    public static final String NameParam = "NAME";
    public static final String PlaceParam = "PLACE";
    public static final String NumberParam = "NUMBER";
    public static final String WebsiteParam = "WEBSITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SurveyBackgroundWorker backgroundWorker = new SurveyBackgroundWorker();
        backgroundWorker.execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void btnSubmit(View view){
        Toast.makeText(getApplicationContext(),"Submit Clicked",Toast.LENGTH_SHORT).show();
        String website = ((EditText) findViewById(R.id.etWebsite)).getText().toString();
        String[] answers = getSurveyAnswers();
        Intent intent = new Intent(getApplicationContext(), TemplateActivity.class);
        intent.putExtra(NameParam, answers[0]);
        intent.putExtra(PlaceParam, answers[1]);
        intent.putExtra(NumberParam, answers[2]);
        intent.putExtra(WebsiteParam, website);
        startActivity(intent);
    }

    private String[] getSurveyAnswers() {
        String[] retValue = new String[3];
        EditText surveyAns1, surveyAns2, surveyAns3;
        surveyAns1 = (EditText) findViewById(R.id.etSurveyA1);
        surveyAns2 = (EditText) findViewById(R.id.etSurveyA2);
        surveyAns3 = (EditText) findViewById(R.id.etSurveyA3);

        retValue[0] = surveyAns1.getText().toString();
        retValue[1] = surveyAns2.getText().toString();
        retValue[2] = surveyAns3.getText().toString();
        return retValue;
    }

    public class SurveyBackgroundWorker extends AsyncTask<Void, Void, String[]>{

        @Override
        protected String[] doInBackground(Void... params) {
            String[] retValue=null;
            try{
                URL url = new URL("http://newandroidapp.esy.es/test1/survey.php");
                URLConnection con = url.openConnection();

                con.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String query = null;

                while(query == null){
                    query = reader.readLine();
                }
                retValue = query.split(":");
            }
            catch(Exception ex){
                Log.e("Error"," Exception occurred in the SurveyBackgroundWorker");
            }
            return retValue;
        }

        protected void onPostExecute(String[] result){
            super.onPostExecute(result);
            surquest1 = (TextView) findViewById(R.id.tvSurveyQ1);
            surquest2 = (TextView) findViewById(R.id.tvSurveyQ2);
            surquest3 = (TextView) findViewById(R.id.tvSurveyQ3);

            surquest1.setText(result[0]);
            surquest2.setText(result[1]);
            surquest3.setText(result[2]);
        }
    }
}
