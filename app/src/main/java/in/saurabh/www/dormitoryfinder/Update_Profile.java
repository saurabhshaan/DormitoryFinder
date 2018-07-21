package in.saurabh.www.dormitoryfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Update_Profile extends AppCompatActivity {
    private LogOnPrefManager PrefManager;
    private EditText full_name, mobile_number;
    private String Full_Name, Mobile_Number;
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile);
        full_name = findViewById(R.id.fullname);
        mobile_number = findViewById(R.id.mobile_number);

      /*  PrefManager = new LogOnPrefManager(this);
        if (!PrefManager.IsLogOnFirstTimeLaunch()){
            launchHomeScreen();

        }*/
    }

    private void launchHomeScreen(){
        PrefManager.setFirstLaunch(false);
        startActivity(new Intent(Update_Profile.this,Homepage.class));
        finish();
    }

    public void Update_Profile(View view) {
        if (full_name.getText().toString().length() == 0){
            full_name.setError("Fill it");
            return;
        }if (mobile_number.getText().toString().length() == 0){
            mobile_number.setError("Fill it");
            return;
        }else {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        }
        Full_Name = full_name.getText().toString().trim();
        Mobile_Number = mobile_number.getText().toString().trim();
//        Toast.makeText(this, Full_Name+""+Mobile_Number, Toast.LENGTH_SHORT).show();
        SharedPreferencesMethod();
    }
    public void SharedPreferencesMethod(){
        Full_Name = full_name.getText().toString().trim();
        Mobile_Number = mobile_number.getText().toString().trim();
        shared = getSharedPreferences("Profile",MODE_PRIVATE);

        editor = shared.edit();
        editor.putString("Full_Name",Full_Name);
        editor.putString("Mobile_Number",Mobile_Number);
        editor.commit();

//        Toast.makeText(this, "mobile"+Mobile_Number, Toast.LENGTH_SHORT).show();

        Log.d("TAG",Full_Name+""+Mobile_Number);
        startActivity(new Intent(Update_Profile.this,Homepage.class));
        finish();
    }

}
