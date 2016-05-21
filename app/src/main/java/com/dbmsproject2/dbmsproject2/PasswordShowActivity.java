package com.dbmsproject2.dbmsproject2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;

public class PasswordShowActivity extends AppCompatActivity {

    private String website, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        website = intent.getStringExtra(RecoverPassword.WebsiteMSg);
        password = intent.getStringExtra(RecoverPassword.PasswordMsg);
        
        setContentView(R.layout.activity_password_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView webTV =  (TextView) findViewById(R.id.tvWebsite1);
        webTV.setText(website);

        TextView pssWd = (TextView) findViewById(R.id.tvPassword1);
        pssWd.setText(password);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
