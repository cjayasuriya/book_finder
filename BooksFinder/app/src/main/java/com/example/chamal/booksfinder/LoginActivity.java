package com.example.chamal.booksfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chamal.booksfinder.classes.config;
import com.example.chamal.booksfinder.classes.connectDB;

import org.json.JSONObject;

import static com.example.chamal.booksfinder.classes.connectDB.getJsonFromServer;

public class LoginActivity extends AppCompatActivity {

    EditText ssnIdTxt,passwordTxt;
    Button signInBtn;
    LinearLayout loginLayout;

    static ProgressDialog progressDialog;

    static String loginResponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        ssnIdTxt = (EditText)findViewById(R.id.ssnSidTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        signInBtn = (Button)findViewById(R.id.signInBtn);
        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);

        ssnIdTxt.setText("SSN007");
        passwordTxt.setText("Abc@1234");

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginLayout.getWindowToken(), 0);
            }
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginLayout.getWindowToken(), 0);

                if(!(ssnIdTxt.getText().toString().isEmpty() || passwordTxt.getText().toString().isEmpty())){
                    progressDialog = ProgressDialog.show(LoginActivity.this,null, "Please wait...");
                    progressDialog.show();
                    progressDialog.setIndeterminate(true);
                    getSignedIn(ssnIdTxt.getText().toString().trim(),passwordTxt.getText().toString().trim());
                    progressDialog.setIndeterminate(false);
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(LoginActivity.this,"Text fields cannot be empty.",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    /**
     * Get response from MobileSignInServlet
     **/
    public void getSignedIn(final String uname, final String password){
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loginResponse = getJsonFromServer(config.baseURL+"/MobileSignInServlet?useremail="+uname+"&userpassword="+password);
                        System.out.println(loginResponse);
                        signIn(loginResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }catch (Exception e){

        }
    }

    /**
     * Sigin to the application
     **/
    public void signIn(String response){

        try {

            //Decode JSON
            JSONObject responseJsonObject = new JSONObject(response);
            String responseType = responseJsonObject.getString("responseType");

            switch (responseType){
                case "1":
                    //Set user data
                    config.setUid(responseJsonObject.getString("uid"),responseJsonObject.getString("type"),
                            responseJsonObject.getString("sid_ssn"),responseJsonObject.getString("email"),
                            responseJsonObject.getString("firstname"),responseJsonObject.getString("lastname"),responseJsonObject.getString("mobile"));

                    Intent dasboardIntent =  new Intent(LoginActivity.this,DashboardActivity.class);
                    startActivity(dasboardIntent);

                    break;
                case "2":
                    //The user id and password you entered don't match.
                    break;
                case "3":
                    //You've been blocked from this website.
                    break;
                case "4":
                    //We don't recognize that SSN / SID.
                    break;
                case "5":
                    //Text fields cannot be empty.
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
