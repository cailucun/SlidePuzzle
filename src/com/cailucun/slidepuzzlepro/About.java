package com.cailucun.slidepuzzlepro;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;

public class About extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);

	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
        	boolean inGame=getIntent().getBooleanExtra("inGame", false);
        	if(inGame){
        		Intent intent_game=new Intent(this,StartGame.class);
        		finish();
        		startActivity(intent_game); 
        	}
        	else{
        		Intent intent_menu=new Intent(this,Menu.class);
        		finish();
        		startActivity(intent_menu); 
        	}
            	return false; 
        	
        	
        } 
        return false; 
    }

}
