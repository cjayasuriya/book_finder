package com.example.chamal.booksfinder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chamal.booksfinder.classes.config;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.chamal.booksfinder.classes.connectDB.getJsonFromServer;

public class BookProfileActivity extends AppCompatActivity {

    static String bookId = "",bookName = "", isbn = "",edition = "",
            author = "", publisher = "",description = "", category = "",status= "";

    static String profileResponse = "", borrowBookResponse = "", intentBookId = "";

    static TextView book_profile_header_name_TV,book_profile_header_isbn_TV,book_category_TV,
            book_edition_TV,book_author_TV,book_publisher_TV,book_desc_TV;

    static LinearLayout book_category_ll,book_edition_ll,book_author_ll,book_publisher_ll,
            book_descm_ll,book_descb_ll;

    static Button statusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);

        Intent bookIntent = getIntent();
        intentBookId = bookIntent.getStringExtra("bookId");

        statusBtn = (Button)findViewById(R.id.statusBtn);

        book_profile_header_name_TV = (TextView)findViewById(R.id.book_profile_header_name_TV);
        book_profile_header_isbn_TV = (TextView)findViewById(R.id.book_profile_header_isbn_TV);
        book_category_TV = (TextView)findViewById(R.id.book_category_TV);
        book_edition_TV = (TextView)findViewById(R.id.book_edition_TV);
        book_author_TV = (TextView)findViewById(R.id.book_author_TV);
        book_publisher_TV = (TextView)findViewById(R.id.book_publisher_TV);
        book_desc_TV = (TextView)findViewById(R.id.book_desc_TV);

        book_category_ll = (LinearLayout)findViewById(R.id.book_category_ll);
        book_edition_ll = (LinearLayout)findViewById(R.id.book_edition_ll);
        book_author_ll = (LinearLayout)findViewById(R.id.book_author_ll);
        book_publisher_ll = (LinearLayout)findViewById(R.id.book_publisher_ll);
        book_descm_ll = (LinearLayout)findViewById(R.id.book_descm_ll);
        book_descb_ll = (LinearLayout)findViewById(R.id.book_descb_ll);

        getBookProfileResponse();

        switch (status){
            case "1":

                statusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bookThisBook();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                break;
        }

    }


    /**
     * Get JSON response
     */
    public static void getBookProfileResponse(){
        try{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Book id"+intentBookId);
                        profileResponse = getJsonFromServer(config.baseURL+"/MobileGetBook?bookId="+intentBookId+"");
                        decoceBookResponse(profileResponse);
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
     * Decoode JSON response
     */
    public static void decoceBookResponse(String response){

        try {

            JSONObject metaJsonObject = new JSONObject(response);

            bookId = metaJsonObject.getString("bookId");
            bookName = metaJsonObject.getString("bookName");
            isbn = metaJsonObject.getString("isbnNum");
            category = metaJsonObject.getString("bookCategory");
            edition = metaJsonObject.getString("bookEdition");
            description = metaJsonObject.getString("description");
            author = metaJsonObject.getString("bookAuthor");
            publisher = metaJsonObject.getString("bookPublisher");
            status = metaJsonObject.getString("bookAvailability");

            setPlaceHolders();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Set place holders
     */
    public static void setPlaceHolders(){

        book_profile_header_name_TV.setText(bookName);
        book_profile_header_isbn_TV.setText(isbn);

        if(!category.isEmpty()){
            book_category_TV.setText(category);
        }else{
            book_category_ll.setVisibility(View.GONE);
        }

        if(!edition.isEmpty()){
            book_edition_TV.setText(edition);
        }else{
            book_edition_TV.setVisibility(View.GONE);
        }

        if(!author.isEmpty()){
            book_author_TV.setText(author);
        }else{
            book_author_ll.setVisibility(View.GONE);
        }

        if(!publisher.isEmpty()){
            book_publisher_TV.setText(publisher);
        }else{
            book_publisher_ll.setVisibility(View.GONE);
        }

        if(!description.isEmpty()){
            book_desc_TV.setText(description);
        }else{
            book_descm_ll.setVisibility(View.GONE);
            book_descb_ll.setVisibility(View.GONE);
        }

        switch (status){
            case "1" :
                statusBtn.setText("Available");
                statusBtn.setBackgroundColor(Color.parseColor("#2ecc71"));
                break;
            case "2" :
                statusBtn.setText("Booked");
                statusBtn.setBackgroundColor(Color.parseColor("#3498db"));
                break;
            case "3" :
                statusBtn.setText("Borrowed");
                statusBtn.setBackgroundColor(Color.parseColor("#d35400"));
                break;
        }

    }

    /**
     * Borrow a book
     */
    public static void bookThisBook(){
        try{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        borrowBookResponse = getJsonFromServer(config.baseURL+"/MobileBorrowBook?" +
                                "bookId="+bookId+
                                "&uid="+config.getId()+
                                "&meta="+"");
                        System.out.println(borrowBookResponse);
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
