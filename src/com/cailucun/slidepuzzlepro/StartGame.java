package com.cailucun.slidepuzzlepro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;



import android.app.Activity;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;


public class StartGame extends Activity{
	private static final String TAG = "Funny Slide Puzzle";
	private Game game;
	MediaPlayer mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
	
		Display display = getWindowManager().getDefaultDisplay();
		String scaleString=Settings.getScale(getApplicationContext());
		int scale=3;
		if(scaleString.equalsIgnoreCase("3*3"))
			scale=3;
		else if(scaleString.equalsIgnoreCase("4*4"))
			scale=4;
		else if(scaleString.equalsIgnoreCase("5*5"))
			scale=5;
		String back=Settings.getPicture(getApplicationContext());
		String difficulty=Settings.getDifficulty(getApplicationContext());
		boolean touchtone=Settings.getTouchTone(getApplicationContext());
		Game.init(this, display.getWidth(), display.getHeight(),scale,difficulty,back,touchtone);
		game = Game.getInstance();
		//get the pic Bitmap from file
		
		 File cacheDir = getBaseContext().getCacheDir();
		 
		    File f = new File(cacheDir, "pic");     
		    FileInputStream fis = null;
		    if(f!=null)
		    try {
		        fis = new FileInputStream(f);
		    } catch (FileNotFoundException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		   Bitmap picture;
		   
			if(fis!=null){
				picture = BitmapFactory.decodeStream(fis);
				game.setPic(picture);
		}

	
		setContentView(game);
		game.requestFocus();
	}
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu,menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
		switch (item.getItemId()) {
	    case R.id.about:
	    	Intent intent_about = new Intent(this, About.class);
			intent_about.putExtra("inGame", true);
			finish();
			startActivity(intent_about);
	    	return true;
	    	
	    case R.id.action_settings:
	    	
	    	Intent intent_settings = new Intent(this, Settings.class);
	    	intent_settings.putExtra("inGame", true);
	    	startActivity(intent_settings);
	    	return true;
	    default:
	    return super.onOptionsItemSelected(item);
	}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
        		finish();
        		startActivity(new Intent(this, Menu.class)); 
            	return false; 
        	
        	
        } 
        return false; 
    }

}