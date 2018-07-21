package in.saurabh.www.dormitoryfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search extends AppCompatActivity {
    private EditText city;
    private String CITY;
    RecyclerView mRVFishPrice;
    Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        city = findViewById(R.id.searchcity);
    }

    public void Seacrh_City(View view) {
        if (city.getText().toString().length() == 0) {
            city.setError("please fill");
            return;
        } else {
            CITY = city.getText().toString().trim();

            if (isOnline()) {
                new AsyncFetch().execute(CITY);

            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(Main2Activity.this);
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            //   pdLoading.setMessage("\tLoading...");
            // pdLoading.setCancelable(false);
            //pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            Log.d("TAG", "hp befor url call");
            String url1 = "http://172.28.172.2:8080/DormetoryFinder/json.php";
            Log.d("TAG", "hp after url call");
            String City= params[0];
            try {

                Log.d("TAG", "open url connection");
                URL url = new URL(url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setReadTimeout(20 * 1000);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //encode data before send it
                //no space permiteted in equals sign
                String data = URLEncoder.encode("CITY", "UTF-8") + "=" + URLEncoder.encode(City, "UTF-8") ;

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream input = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                input.close();
                httpURLConnection.disconnect();
                // Pass data to onPostExecute method
                return result.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            List<UserData> data=new ArrayList<>();
            //this method will be running on UI thread

            //   pdLoading.dismiss();
            //   List<DataFish> data=new ArrayList<>();

            //  pdLoading.dismiss();
            Log.d("TAG",result);
            try {
                Log.d("TAG",result);
                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){

                    JSONObject json_data = jArray.getJSONObject(i);
                    UserData ob=new UserData();

                    ob.Full_name=json_data.getString("Full_Name");
                    ob.Complete_Address= json_data.getString("Address");
                    ob.City= json_data.getString("City");
                    ob.Mobile= json_data.getString("Mobile");
//                    ob.Image= json_data.getString("Image");

                    Log.d("TAG",json_data.getString("Full_Name"));
                    Log.d("TAG",json_data.getString("Address"));
                    Log.d("TAG",json_data.getString("City"));
                    Log.d("TAG",json_data.getString("Mobile"));
//                    Log.d("TAG",json_data.getString("Image"));
                    data.add(ob);
                    Log.d("TAG","Hello"+data.add(ob));

                }

                mAdapter = new Adapter(Search.this, data);
                // Setup and Handover data to recyclerview
                mRVFishPrice = findViewById(R.id.fishPriceList);

                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(Search.this));

            } catch (JSONException e) {
                Toast.makeText(Search.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}