package com.example.chamal.booksfinder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chamal.booksfinder.classes.config;

import org.json.JSONObject;

import static com.example.chamal.booksfinder.classes.connectDB.getJsonFromServer;

public class EditProfileActivity extends AppCompatActivity {

    LinearLayout editUserLayout;

    Button editUserBtn;

    static EditText fName, mName, lName,email,telephone,
                    mobile,gender,dob,nation,description,
                    address1,address2,city,state,zip;

    static Spinner genderSpinner;

    static String editUserResponse = "",userData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent editIntent = getIntent();
        userData = editIntent.getStringExtra("userInfo");

        editUserLayout = (LinearLayout)findViewById(R.id.edit_user_ll);

        editUserBtn = (Button)findViewById(R.id.editUserProfileBtn);

        fName = (EditText)findViewById(R.id.firstname_ET);
        mName = (EditText)findViewById(R.id.middlename_ET);
        lName = (EditText)findViewById(R.id.lastname_ET);
        email = (EditText)findViewById(R.id.email_ET);
        telephone = (EditText)findViewById(R.id.telephone_ET);
        mobile = (EditText)findViewById(R.id.mobile_ET);
//        gender = (EditText)findViewById(R.id.gender_ET);
        dob = (EditText)findViewById(R.id.dob_ET);
        nation = (EditText)findViewById(R.id.nation_ET);
        description = (EditText)findViewById(R.id.desc_ET);

        address1 = (EditText)findViewById(R.id.address1_ET);
        address2 = (EditText)findViewById(R.id.address2_ET);
        city = (EditText)findViewById(R.id.city_ET);
        state = (EditText)findViewById(R.id.state_ET);
        zip = (EditText)findViewById(R.id.zip_ET);

        genderSpinner = (Spinner)findViewById(R.id.gender_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        descodeUserDetails(userData);


        editUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editUserLayout.getWindowToken(), 0);
            }
        });

        editUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(fName.getText().toString().trim().isEmpty() || lName.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty() ||
                        mobile.getText().toString().trim().isEmpty())){
                    updateUserProfile();
                }else{
                    Toast.makeText(EditProfileActivity.this,"Mandatory fields cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /**
     * Decode JSON and set place holders
     */
    public void descodeUserDetails(String response){
        try {
            JSONObject userJObj = new JSONObject(response);

            fName.setText(userJObj.getString("firstname"));
            mName.setText(userJObj.getString("middlename"));
            lName.setText(userJObj.getString("lastname"));
            telephone.setText(userJObj.getString("telephone"));
            mobile.setText(userJObj.getString("mobile"));
            email.setText(userJObj.getString("email"));
            dob.setText(userJObj.getString("dob"));
//            gender.setText(userJObj.getString("gender"));
            if(userJObj.getString("gender").equals("Male")){
                genderSpinner.setSelection(0);
            }else if (userJObj.getString("gender").equals("Female")){
                genderSpinner.setSelection(1);
            }

            nation.setText(userJObj.getString("nationality"));
            description.setText(userJObj.getString("description"));

            JSONObject addressJOb = new JSONObject(userJObj.getString("address"));

            address1.setText(addressJOb.getString("address1"));
            address2.setText(addressJOb.getString("address2"));
            city.setText(addressJOb.getString("city"));
            state.setText(addressJOb.getString("state"));
            zip.setText(addressJOb.getString("zip"));


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Update profile
     */
    public void updateUserProfile(){
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        System.out.println("In thread--"+userData);

                        String dataPackage = "uid="+config.getId()+
                                "&fname="+fName.getText().toString()+
                                "&mname="+mName.getText().toString()+
                                "&lname="+lName.getText().toString()+
                                "&telephone="+telephone.getText().toString()+
                                "&mobile="+mobile.getText().toString()+
                                "&email="+email.getText().toString()+
                                "&dob="+dob.getText().toString()+
                                "&gender="+genderSpinner.getSelectedItem().toString()+
                                "&nationality="+nation.getText().toString()+
                                "&userdescription="+description.getText().toString()+
                                "&address1="+address1.getText().toString()+
                                "&address2="+address2.getText().toString()+
                                "&city="+city.getText().toString()+
                                "&state="+state.getText().toString()+
                                "&zipcode="+zip.getText().toString();

                        editUserResponse = getJsonFromServer(config.baseURL+"/MobileUpdateUser?" +dataPackage.replace(" ","%20")+"");

                        JSONObject editResponseJObj = new JSONObject(editUserResponse);

                        switch (editResponseJObj.getString("responseType")){
                            case "1":

                                config.setUid(
                                        editResponseJObj.getString("uid"),editResponseJObj.getString("type"),
                                        editResponseJObj.getString("sid_ssn"),editResponseJObj.getString("email"),
                                        editResponseJObj.getString("firstname"),editResponseJObj.getString("lastname"),
                                        editResponseJObj.getString("mobile")
                                );

                                Intent userProfileIntent = new Intent(EditProfileActivity.this,UserProfileActivity.class);
                                startActivity(userProfileIntent);
                                System.out.println("Profile updated successfully");
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
