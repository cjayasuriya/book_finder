package com.example.chamal.booksfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chamal.booksfinder.classes.config;

import org.json.JSONObject;

import static com.example.chamal.booksfinder.classes.connectDB.getJsonFromServer;

public class UserProfileActivity extends AppCompatActivity {

    static TextView profile_header_name_TV,profile_header_sid_TV;
    static TextView profile_fullname_TV,profile_email_TV,profile_mobile_TV,profile_telephone_TV,
                    profile_GENDER_TV,profile_address1_TV, profile_address2_TV,profile_city_TV,
                    profile_state_TV,profile_zip_TV,profile_country_TV,profile_nation_TV,
                    profile_department_TV,profile_desc_TV,profile_dob_TV;

    static LinearLayout profile_email_ll,profile_mobile_ll,profile_telephone_ll,
                        profile_dob_ll,profile_gender_ll,profile_address_main_ll,
                        profile_address_1n2_ll,profile_citynstate_ll,profile_zipncountry_ll,
                        profile_nation_ll,profile_department_ll,profile_descm_ll,profile_descb_ll;

    static String profileResponse = "";

    static String mname = "", telephone = "", gender = "", desc = "", dob = "", nation = "",
            department = "",address1 = "", address2 = "", city = "", state = "", zip = "", country = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        /**
         * TEXT VIEWS
         */
        profile_header_name_TV = (TextView)findViewById(R.id.profile_header_name_TV);
        profile_header_sid_TV = (TextView)findViewById(R.id.profile_header_sid_TV);

        profile_fullname_TV = (TextView)findViewById(R.id.profile_fullname_TV);
        profile_email_TV = (TextView)findViewById(R.id.profile_email_TV);
        profile_mobile_TV = (TextView)findViewById(R.id.profile_mobile_TV);
        profile_telephone_TV = (TextView)findViewById(R.id.profile_telephone_TV);
        profile_dob_TV = (TextView)findViewById(R.id.profile_dob_TV);
        profile_GENDER_TV = (TextView)findViewById(R.id.profile_gender_TV);
        profile_nation_TV = (TextView)findViewById(R.id.profile_nation_TV);
        profile_department_TV = (TextView)findViewById(R.id.profile_department_TV);
        profile_desc_TV = (TextView)findViewById(R.id.profile_desc_TV);

        profile_address1_TV = (TextView)findViewById(R.id.profile_address1_TV);
        profile_address2_TV = (TextView)findViewById(R.id.profile_address2_TV);
        profile_city_TV = (TextView)findViewById(R.id.profile_city_TV);
        profile_state_TV = (TextView)findViewById(R.id.profile_state_TV);
        profile_zip_TV = (TextView)findViewById(R.id.profile_zip_TV);
        profile_country_TV = (TextView)findViewById(R.id.profile_country_TV);

        /**
         * LINEAR LAYOUTS
         */
        profile_email_ll = (LinearLayout)findViewById(R.id.profile_email_ll);
        profile_mobile_ll = (LinearLayout)findViewById(R.id.profile_mobile_ll);
        profile_telephone_ll = (LinearLayout)findViewById(R.id.profile_telephone_ll);
        profile_dob_ll = (LinearLayout)findViewById(R.id.profile_dob_ll);
        profile_gender_ll = (LinearLayout)findViewById(R.id.profile_gender_ll);
        profile_department_ll = (LinearLayout)findViewById(R.id.profile_department_ll);
        profile_nation_ll = (LinearLayout)findViewById(R.id.profile_nation_ll);
        profile_descm_ll = (LinearLayout)findViewById(R.id.profile_descm_ll);
        profile_descb_ll = (LinearLayout)findViewById(R.id.profile_descb_ll);

        profile_address_main_ll = (LinearLayout)findViewById(R.id.profile_address_main_ll);
        profile_address_1n2_ll = (LinearLayout)findViewById(R.id.profile_address_1n2_ll);
        profile_citynstate_ll = (LinearLayout)findViewById(R.id.profile_citynstate_ll);
        profile_zipncountry_ll = (LinearLayout)findViewById(R.id.profile_zipncountry_ll);

        getProfileResponse();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.medit_profile:
                Intent editProfileIntent = new Intent(UserProfileActivity.this,EditProfileActivity.class);
                editProfileIntent.putExtra("userInfo",profileResponse);
                startActivity(editProfileIntent);
                return true;
            case R.id.mchange_password:
                Intent changePasswordIntent = new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
                startActivity(changePasswordIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Get response from MobileGetUser
     */
    public static void getProfileResponse(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        profileResponse = getJsonFromServer(config.baseURL+"/MobileGetUser?uid="+config.getId());
                        decodeResponse(profileResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Decode JSON response
     */
    public static void decodeResponse(String response){

        //Decode JSON
        try {
            JSONObject metaJsonObject = new JSONObject(response);
            mname = metaJsonObject.getString("middlename");
            telephone = metaJsonObject.getString("telephone");
            gender = metaJsonObject.getString("gender");
            desc = metaJsonObject.getString("description");
            dob = metaJsonObject.getString("dob");
            nation = metaJsonObject.getString("nationality");
            department = metaJsonObject.getString("department");

            JSONObject addressJsonObject = new JSONObject(metaJsonObject.getString("address"));
            address1 = addressJsonObject.getString("address1");
            address2 = addressJsonObject.getString("address2");
            city = addressJsonObject.getString("city");
            state = addressJsonObject.getString("state");
            zip = addressJsonObject.getString("zip");
            country = addressJsonObject.getString("country");

            setPlaceHolders();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Set placeholders
     */
    public static void setPlaceHolders(){

        //Set headers
        profile_header_name_TV.setText(config.getFName() +" "+ config.getLName());
        profile_header_sid_TV.setText(config.getSid());

        profile_fullname_TV.setText(config.getFName() +" "+ mname +" "+config.getLName());
        profile_mobile_TV.setText(config.getMobile());
        profile_email_TV.setText(config.getEmail());

        if(!telephone.isEmpty()){
            profile_telephone_TV.setText(telephone);
        }else{
            profile_telephone_ll.setVisibility(View.GONE);
        }

        if(!dob.isEmpty()){
            profile_dob_TV.setText(dob);
        }else{
            profile_dob_ll.setVisibility(View.GONE);
        }

        if(!gender.isEmpty()){
            profile_GENDER_TV.setText(gender);
        }else{
            profile_gender_ll.setVisibility(View.GONE);
        }

        if(!nation.isEmpty()){
            profile_nation_TV.setText(nation);
        }else{
            profile_nation_ll.setVisibility(View.GONE);
        }

        if(!department.isEmpty()){
            profile_department_TV.setText(nation);
        }else{
            profile_department_ll.setVisibility(View.GONE);
        }

        if(!desc.isEmpty()){
            profile_desc_TV.setText(desc);
        }else{
            profile_descm_ll.setVisibility(View.GONE);
            profile_descb_ll.setVisibility(View.GONE);
        }

        if(address1.isEmpty() && address2.isEmpty() && state.isEmpty() && city.isEmpty() && zip.isEmpty() && country.isEmpty()) {
            profile_address_main_ll.setVisibility(View.GONE);
        }

        if((address1.isEmpty() && address2.isEmpty())){
            profile_address_1n2_ll.setVisibility(View.GONE);
        }

        if (!address1.isEmpty()){
            profile_address1_TV.setText(address1);
        }else{
            profile_address1_TV.setVisibility(View.GONE);
        }

        if (!address2.isEmpty()){
            profile_address2_TV.setText(", "+address2);
        }else{
            profile_address2_TV.setVisibility(View.GONE);
        }

        if (city.isEmpty() && state.isEmpty()){
            profile_citynstate_ll.setVisibility(View.GONE);
        }

        if (!city.isEmpty()){
            profile_city_TV.setText(city);
        }else {
            profile_city_TV.setVisibility(View.GONE);
        }

        if (!state.isEmpty()){
            profile_state_TV.setText(", "+state);
        }else {
            profile_state_TV.setVisibility(View.GONE);
        }

        if (zip.isEmpty() && country.isEmpty()){
            profile_zipncountry_ll.setVisibility(View.GONE);
        }

        if (!zip.isEmpty()){
            profile_zip_TV.setText(zip);
        }else {
            profile_zip_TV.setVisibility(View.GONE);
        }

        if (!country.isEmpty()){
            profile_country_TV.setText(country);
        }else {
            profile_country_TV.setVisibility(View.GONE);
        }


    }
}
