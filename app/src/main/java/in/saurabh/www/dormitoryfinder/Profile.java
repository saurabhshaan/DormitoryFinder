package in.saurabh.www.dormitoryfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    private TextView mname,mmobile_num;
    private Button Update;
    SharedPreferences sharedPreferences;
    String mFull_Name,mMobile_Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mname = findViewById(R.id.b);
        mmobile_num = findViewById(R.id.c);
        Update = findViewById(R.id.e);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Update_Profile.class));
            }
        });

        sharedPreferences = getApplicationContext().getSharedPreferences("Profile",0);
        mFull_Name = sharedPreferences.getString("Full_Name",null);
        mMobile_Number = sharedPreferences.getString("Mobile_Number",null);

        mname.setText(mFull_Name);
        mmobile_num.setText(mMobile_Number);

    }
}
