package com.cailucun.slidepuzzlepro;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Menu extends Activity implements OnClickListener{
	private static int RESULT_LOAD_IMAGE = 1;
	Bitmap pic=null;
	static MediaPlayer mp;
	int currentPic=495;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle);
		setContentView(R.layout.activity_menu);
		View button_play = findViewById(R.id.button_play);
		button_play.setOnClickListener(this);
		View button_upload = findViewById(R.id.button_upload);
		button_upload.setOnClickListener(this);
		View button_settings = findViewById(R.id.button_settings);
		button_settings.setOnClickListener(this);
		View button_exit = findViewById(R.id.button_exit);
		button_exit.setOnClickListener(this);
		View button_next=findViewById(R.id.button_next);
		button_next.setOnClickListener(this);
		View button_pre=findViewById(R.id.button_pre);
		button_pre.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		switch (v.getId()) {
		case R.id.button_play:
			// save the bitmap to file pic.jpg
			File cacheDir = getBaseContext().getCacheDir();
            File f = new File(cacheDir, "pic");

            try {
                FileOutputStream out = new FileOutputStream(
                        f);
                pic.compress(
                        Bitmap.CompressFormat.JPEG,
                        100, out);
                out.flush();
                out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
			Intent intent_play = new Intent(this, StartGame.class);
			finish();
			startActivity(intent_play);
			break;
		case R.id.button_next:
			currentPic++;
			switch(currentPic%9){
			case 0:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle);
				break;
			case 1:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle2);
				break;
			case 2:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle3);
				break;
			case 3:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle4);
				break;	
			case 4:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle5);
				break;	
			case 5:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle6);
				break;	
			case 6:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle7);
				break;	
			case 7:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle8);
				break;	
			case 8:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle9);
				break;	
			}	
			imageView.setImageBitmap(pic);			
			break;
		case R.id.button_pre:
			currentPic--;
			switch(currentPic%9){
			case 0:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle);
				break;
			case 1:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle2);
				break;
			case 2:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle3);
				break;
			case 3:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle4);
				break;	
			case 4:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle5);
				break;	
			case 5:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle6);
				break;	
			case 6:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle7);
				break;	
			case 7:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle8);
				break;	
			case 8:
				pic = BitmapFactory.decodeResource(getResources(),R.drawable.puzzle9);
				break;	
			}	
			imageView.setImageBitmap(pic);			
			break; 
		case R.id.button_upload:
			 Intent intent_upload = new Intent(
                     Intent.ACTION_PICK,
                     android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
              
             startActivityForResult(intent_upload, RESULT_LOAD_IMAGE);
             break;
		case R.id.button_settings:
			Intent intent_settings = new Intent(this, Settings.class);
			intent_settings.putExtra("inGame", false);
			finish();
			startActivity(intent_settings);
			break;
		case R.id.button_exit:
			finish();
			System.exit(0);
			break;
		}
	}
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        
       if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
           Uri selectedImage = data.getData();
           String[] filePathColumn = { MediaStore.Images.Media.DATA };

           Cursor cursor = getContentResolver().query(selectedImage,
                   filePathColumn, null, null, null);
           cursor.moveToFirst();

           int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
           String picturePath = cursor.getString(columnIndex);
           cursor.close();
            
           ImageView imageView = (ImageView) findViewById(R.id.imageView);
           pic=BitmapFactory.decodeFile(picturePath);
           imageView.setImageBitmap(pic);
        
       }
       
    
    
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
			intent_about.putExtra("inGame", false);
			finish();
			startActivity(intent_about);
	    	return true;
	    	
	    case R.id.action_settings:
			Intent intent_settings = new Intent(this, Settings.class);
			intent_settings.putExtra("inGame", false);
			finish();
			startActivity(intent_settings);
	    	return true;
	    default:
	    return super.onOptionsItemSelected(item);
		}
	}
	private void showTips(){
			AlertDialog alertDialog = new AlertDialog.Builder(Menu.this)
			.setTitle("")
			.setMessage("Do you want to quit the game?")
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){
			finish();
			}
			}).setNegativeButton(R.string.cancel,
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){
			return;
			}}).create();  
			alertDialog.show(); 
			
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {         	
        		showTips();
            	return false; 
        	
        	
        } 
        return false; 
    }

}
