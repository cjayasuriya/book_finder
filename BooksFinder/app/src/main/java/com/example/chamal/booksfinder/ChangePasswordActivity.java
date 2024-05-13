package com.example.chamal.booksfinder;

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

import static com.example.chamal.booksfinder.classes.connectDB.getJsonFromServer;

public class ChangePasswordActivity extends AppCompatActivity {

    LinearLayout change_password_ll;
    EditText oldPassword, newPassword, retypePassword;
    Button changePasswordBtn;

    static String changePasswordResponse = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        change_password_ll = (LinearLayout)findViewById(R.id.change_password_ll);
        oldPassword = (EditText)findViewById(R.id.old_password_ET);
        newPassword = (EditText)findViewById(R.id.new_password_ET);
        retypePassword = (EditText)findViewById(R.id.retype_password_ET);
        changePasswordBtn = (Button)findViewById(R.id.change_password_BTN);

        change_password_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(change_password_ll.getWindowToken(), 0);
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(change_password_ll.getWindowToken(), 0);

                if(!(oldPassword.getText().toString().isEmpty() || newPassword.getText().toString().isEmpty() || retypePassword.getText().toString().isEmpty())){

                    if((newPassword.getText().toString().equals(retypePassword.getText().toString()))){
                        changePassword(oldPassword.getText().toString(),newPassword.getText().toString(),retypePassword.getText().toString());
                    }else{
                        Toast.makeText(ChangePasswordActivity.this,"Entered new passwords don't match.",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ChangePasswordActivity.this,"Fields cannot be empty.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void changePassword(final String oPassword, final String nPassword, final String rPassword){
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        changePasswordResponse = getJsonFromServer(config.baseURL+"/MobileUpdatePassword?" +
                                "uid="+config.getId()+
                                "&oldPassword="+oPassword+
                                "&newPassword="+nPassword+
                                "&newRetypePassword="+rPassword+"");

                        switch (changePasswordResponse){
                            case "1":
                                System.out.println("Password successfully updated");
                                Intent userProfileIntent =  new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
                                startActivity(userProfileIntent);
                                break;
                            case "2":
                                System.out.println("Newly entered passwords don't match");
                                break;
                            case "3":
                                System.out.println("The old password you entered dont match, try again.");
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }catch (Exception e){

        }
    }

}
