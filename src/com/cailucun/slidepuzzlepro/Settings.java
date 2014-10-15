package com.cailucun.slidepuzzlepro;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

public class Settings extends PreferenceActivity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
	}
	public static String getPicture (Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pictures", "Pure White");
		
	}
	public static String getDifficulty (Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("difficulty", "Easy");
		
	}
	public static String getScale (Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("scales", "3*3");
		
	}
	public static boolean getTouchTone (Context context){
		String onoff=PreferenceManager.getDefaultSharedPreferences(context).getString("touchtone", "On");
		if(onoff.equalsIgnoreCase("On"))
			return true;
		else
			return false;
		
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
        	Intent intent =getIntent();
        	boolean inGame=intent.getBooleanExtra("inGame", false);
        	if(inGame){
        		finish();
        		startActivity(new Intent(this, StartGame.class)); 
            	return false; 
        	}
        	else{
        		finish();
        		startActivity(new Intent(this, Menu.class)); 
            	return false; 
        	}
        } 
        return false; 
    }
	
}
