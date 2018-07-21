package in.saurabh.www.dormitoryfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PostRoom extends AppCompatActivity {
    private EditText mName,mAddress,mMobile,mCity;
    private ImageView imageView;
    private String Mname,Maddress,Mmobile,Mcity;
    private static int RESULT_LOAD_IMG = 1;
    String encodedImage, imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room);

        mName = findViewById(R.id.fullname);
        mAddress = findViewById(R.id.completeaddress);
        mMobile = findViewById(R.id.mobilenumber);
        mCity = findViewById(R.id.city);
        imageView = findViewById(R.id.image);
    }
    public void browseImage(View v) {

// Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra("crop", "true");
        galleryIntent.putExtra("outputX", 200);
        galleryIntent.putExtra("outputY", 260);
        galleryIntent.putExtra("aspectX", 1);
        galleryIntent.putExtra("aspectY", 1);
        galleryIntent.putExtra("scale", true);
        galleryIntent.putExtra("return-data", true);
// Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                if (cursor != null) {
                    cursor.moveToFirst();
                }

                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                }
                if (cursor != null) {
                    imgDecodableString = cursor.getString(columnIndex);
                }
                if (cursor != null) {
                    cursor.close();
                }
                // Set the Image in ImageView after decoding the String
                imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                //imageUploadSTART

                Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object//0 for low quality
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                Toast.makeText(PostRoom.this, "ImageSet", Toast.LENGTH_SHORT).show();
                Toast.makeText(PostRoom.this, "Wait for moment ....", Toast.LENGTH_SHORT).show();
                Log.d("error", "images" + encodedImage);
                //close
            }
        } catch (Exception e) {
            Toast.makeText(this, "Problem Detected!", Toast.LENGTH_LONG).show();
        }
    }

    public void Submit(View view) {
        if (mName.getText().toString().length()==0){
            mName.setError("please fill");
            return;
        }if (mAddress.getText().toString().length()==0){
            mAddress.setError("please fill");
            return;
        }if (mCity.getText().toString().length()==0){
            mCity.setError("please fill");
            return;
        }if (mMobile.getText().toString().length()==0){
            mMobile.setError("please fill");
            return;
        }else {
            Toast.makeText(this, "Fields validated", Toast.LENGTH_SHORT).show();
        }
        Mname = mName.getText().toString().trim();
        Maddress = mAddress.getText().toString().trim();
        Mcity = mCity.getText().toString().trim();
        Mmobile = mMobile.getText().toString().trim();

        if (isOnline()){
            String method = "Post_Room";
            PostRoomBackgrnd postRoomBackgrnd = new PostRoomBackgrnd(PostRoom.this);
            postRoomBackgrnd.execute(method,Mname,Maddress,Mcity,Mmobile,encodedImage);
        }
    }
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public class PostRoomBackgrnd extends AsyncTask<String,Void,String>{
        Context ctx;

        public PostRoomBackgrnd(Context ctx1){
            this.ctx = ctx1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://172.28.172.2:8080/DormetoryFinder/PostRoom.php";

            String method = params[0];
            if (method.equals("Post_Room")){
                String Full_Name = params[1];
                String Address = params[2];
                String City = params[3];
                String Mobile = params[4];
                String Image = params[5];

                try{
                    URL url = new URL(reg_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    Log.d("TAG", "open url connection");
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    Log.d("TAG", "buffered writer");
                    // encode data

                    String OpenConn = URLEncoder.encode("Full_Name", "UTF-8") + "=" + URLEncoder.encode(Full_Name, "UTF-8") + "&" +
                            URLEncoder.encode("Address", "UTF-8") + "=" + URLEncoder.encode(Address, "UTF-8") + "&" +
                            URLEncoder.encode("City", "UTF-8") + "=" + URLEncoder.encode(City, "UTF-8")+"&"+
                            URLEncoder.encode("Mobile", "UTF-8") + "=" + URLEncoder.encode(Mobile, "UTF-8")+"&"+
                            URLEncoder.encode("Image", "UTF-8") + "=" + URLEncoder.encode(Image, "UTF-8");
                    bufferedWriter.write(OpenConn);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    Log.d("TAG", "buffer writer close");
                    os.close();
                    // get Reponce from server
                    InputStream is = httpURLConnection.getInputStream();
                    Log.d("TAG", "debug");
                    is.close();
                    return "Success";
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(ctx, "Successfully Posted", Toast.LENGTH_SHORT).show();
        }
    }
}