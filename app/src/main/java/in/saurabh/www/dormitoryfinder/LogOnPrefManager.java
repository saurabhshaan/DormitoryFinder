package in.saurabh.www.dormitoryfinder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 1/2/18.
 */

public class LogOnPrefManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharededitor;
    Context con1;
    int PRIVATE_MODE1 = 0;
    private static final String PREF_NAME1 = "LOGON";
    private static final String IS_LOG_ON_FIRST_TIME_LAUNCH = "IsLogOnFirstTimeLaunch";

    public LogOnPrefManager(Context context){
        this.con1 = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME1,PRIVATE_MODE1);
        sharededitor = sharedPreferences.edit();
    }
    public void setFirstLaunch(boolean firstLaunch){
        sharededitor.putBoolean(IS_LOG_ON_FIRST_TIME_LAUNCH,firstLaunch);
        sharededitor.commit();
    }
    public boolean IsLogOnFirstTimeLaunch(){
        return sharedPreferences.getBoolean(IS_LOG_ON_FIRST_TIME_LAUNCH,true);
    }
}
