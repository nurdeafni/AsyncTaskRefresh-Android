package com.nurde.asynctaskjson;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String id, name, username, email, addr, street, suite, city, zipcode;
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private Button btnGet;
    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    private ArrayList<Users> usersArrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = (Button) findViewById(R.id.btnGet);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeCountainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Async task = new  Async();
                task.execute();
            }
        });


        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Async task = new  Async();
                task.execute();
            }
        });

    }
    public  String loadJSONSFromAsset(){
        String json = null;
        try {
            InputStream is = getAssets().open("users.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer,"UTF-8");
        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public class Async extends AsyncTask<String,String,String> {
        protected void onPreExecute(){
            super.onPreExecute();
            //display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }
        protected String doInBackground(String... param){
            usersArrayList = new ArrayList<>();
            try{
                //get JSONObject from JSON file
                //JSONObject obj = new JSONObject(getData());

                //fetch JSONArray named users
                //JSONArray userArray = obj.getJSONArray("users");
                JSONArray userArray = new JSONArray(getData());
                for (int i = 0; i < userArray.length(); i++) {
                    //create a JSONObject for  fetching single user data
                    JSONObject userDetail = userArray.getJSONObject(i);

                    id = userDetail.getString("id");
                    name = userDetail.getString("name");
                    username = userDetail.getString("username");
                    email = userDetail.getString("email");

                    JSONObject address = userDetail.getJSONObject("address");
                    street = address.getString("street");
                    suite = address.getString("suite");
                    city = address.getString("city");
                    zipcode = address.getString("zipcode");

                    addr = street+", "+suite+", "+city+", "+zipcode;

                    usersArrayList.add(new Users(id,name,username,email,addr));
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
            return null;
            }
            protected void onPostExecute (String result){
                mSwipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                adapter = new UsersAdapter(usersArrayList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
        }
    }
    public String getData(){
        String line = "";
        try{
         //   URL url = new URL ("http://www.mocky.io/v2/5ccabcf56100005900162025");
            URL url = new URL ("https://jsonplaceholder.typicode.com/users");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            return buffer.toString();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (connection != null) connection.disconnect();
            try{
                if(reader != null) reader.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    }
