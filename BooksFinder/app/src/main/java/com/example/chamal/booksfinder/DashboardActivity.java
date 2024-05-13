package com.example.chamal.booksfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chamal.booksfinder.classes.config;

public class DashboardActivity extends AppCompatActivity {

    TextView userName;
    //Toolbar toolbar;
    Button goToBooksBtn, goToMyBooksBtn, goToProfileBtn, signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        goToBooksBtn = (Button)findViewById(R.id.goToBooksBtn);
        goToMyBooksBtn = (Button)findViewById(R.id.goToMyBooksBtn);
        goToProfileBtn = (Button)findViewById(R.id.goToProfileBtn);
        signOutBtn = (Button)findViewById(R.id.goToLogoutBtn);

        userName = (TextView) findViewById(R.id.userNameDbTxt);
        userName.setText(config.getLName().toString());

        //Go to books
        goToBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent booksListIntent = new Intent(DashboardActivity.this,BooksActivity.class);
                startActivity(booksListIntent);
            }
        });

        //Go to my books
        goToMyBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myBooksListIntent = new Intent(DashboardActivity.this,MyBooksActivity.class);
                startActivity(myBooksListIntent);

            }
        });

        //Go to profile
        goToProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(DashboardActivity.this,UserProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        //Sign out
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setUid("","","","","","","");
                Intent signOutIntent = new Intent(DashboardActivity.this,LoginActivity.class);
                startActivity(signOutIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
