package com.example.chamal.booksfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chamal.booksfinder.classes.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.chamal.booksfinder.classes.connectDB.getJsonFromServer;

public class MyBooksActivity extends AppCompatActivity {

    private AppAdapter myBooksAdapter;
    ViewHolder holder;

    ListView myBooksList;

    static String myBooksResponse = "", bookId = "";

    static JSONArray myBbooksJArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        myBooksList = (ListView)findViewById(R.id.my_books_list);

        myBooksAdapter = new AppAdapter();
        myBooksList.setAdapter(myBooksAdapter);

        getMyBookResponse();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mybooks_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mybooks_mrefresh:
                getMyBookResponse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            try{
                if(myBbooksJArray.length() > 0){
                    return myBbooksJArray.length();
                }else{
                    return 0;
                }
            }catch (Exception e){
                return 0;
            }


        }

        @Override
        public Object getItem(int position) {
            try {
                return myBbooksJArray.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, final ViewGroup parent) {

            //ViewHolder holder;
            if (convertView == null) {
                holder = new MyBooksActivity.ViewHolder();
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.adapter_my_books, null);
                holder.bookName = (TextView) convertView
                        .findViewById(R.id.mybooks_book_name_TV);
                holder.bookAuthor = (TextView) convertView
                        .findViewById(R.id.mybooks_book_author_TV);
                holder.bookIsbn = (TextView) convertView
                        .findViewById(R.id.mybooks_book_isbn_TV);
                holder.bookStatus = (TextView) convertView
                        .findViewById(R.id.mybooks_book_status_TV);
                holder.bookRow = (LinearLayout)convertView.findViewById(R.id.my_books_list_ll);
                convertView.setTag(holder);
                holder.bookRow.setId(position);
            } else {
                holder = (MyBooksActivity.ViewHolder) convertView.getTag();
            }

            try {
                JSONObject bookJObj = new JSONObject();
                bookJObj = (JSONObject)myBbooksJArray.getJSONObject(position);

                holder.bookName.setText(bookJObj.getString("bookName").toString());
                holder.bookAuthor.setText(bookJObj.getString("bookAuthor").toString());
                holder.bookIsbn.setText(bookJObj.getString("isbnNum").toString());

                String status = bookJObj.getString("bookAvailability").toString();

                bookId = bookJObj.getString("bookId").toString();

                switch (status){

                    case "1" :
                        holder.bookStatus.setText("Available");
                        holder.bookStatus.setBackgroundColor(Color.parseColor("#2ecc71"));
                        break;
                    case "2" :
                        holder.bookStatus.setText("Booked");
                        holder.bookStatus.setBackgroundColor(Color.parseColor("#3498db"));
                        break;
                    case "3" :
                        holder.bookStatus.setText("Borrowed");
                        holder.bookStatus.setBackgroundColor(Color.parseColor("#d35400"));
                        break;
                }

                holder.bookRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = v.getId();
                        Intent bookProfileIntent =  new Intent(MyBooksActivity.this,BookProfileActivity.class);
                        try {
                            bookProfileIntent.putExtra("bookId",myBbooksJArray.getJSONObject(position).getString("bookId").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(bookProfileIntent);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }

    public class ViewHolder {
        public TextView bookName;
        public TextView bookAuthor;
        public TextView bookIsbn;
        public TextView bookStatus;
        public LinearLayout bookRow;

    }

    public void getMyBookResponse(){

        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        myBooksResponse = getJsonFromServer(config.baseURL+"/MobileGetAllMyBooks?" +
                                "uid="+config.getId()+"");
                        System.out.println(myBooksResponse);
                        myBbooksJArray = new JSONArray(myBooksResponse);
                        myBooksAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }catch (Exception e){

        }

    }
}
