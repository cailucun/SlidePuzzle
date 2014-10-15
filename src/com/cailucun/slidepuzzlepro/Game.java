package com.cailucun.slidepuzzlepro;



import android.app.Activity;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import android.media.MediaPlayer;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

import java.util.Random;



//define the chunk class
class Piece{
	public Bitmap image=null;
	public int index;
	Piece(Piece p){
	image=p.image;
	index=p.index;
	}
	Piece(Bitmap image,int index){
		this.image=image;
		this.index=index;
	}
	Bitmap getImage(){
		return image;
	}
}
public class Game extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	//Define the scale of the Board 3*3/4*4
	int scale=3;
	String difficulty="Easy";
	boolean touchtone=true;
	static MediaPlayer mp;
	
	//Painting related data
	SurfaceHolder mSurfaceHolder = null;
    public static Paint sPaint = null;
    public static Canvas sCanvas = null;
	float width=getWidth();
	float height=getHeight();
	Paint mPaint;
	
	//picture attributes
	public Bitmap pic=null;
	
	Bitmap blankPic=null;
	Bitmap lastPic=null;
	Bitmap infoback=null;
	Bitmap background=null;
	Bitmap restart=null;
	
	int picX;
	int picY;
	int picHeight;
	int picWidth;

	//Game Control
	Thread aThread;
	Context context;
	static Game sInstance = null;
	public boolean loop;
	boolean isOver=false;
	boolean startover=false;
	
	

	//Game storage
	ArrayList<Piece> list;
	int selX;
	int selY;
	int count=0;
	long startTime;
	long currentTime;
	

	//Constructor
	public Game(Activity activity,float screenWidth,float screenHeight,int scale,String difficulty,String back,boolean touchtone) {
		super(activity);
		this.scale=scale;
		this.difficulty=difficulty;
		this.touchtone=touchtone;
		context=activity;
		width = screenWidth;
		height = screenHeight;	
		loop=true;
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		pic = BitmapFactory.decodeResource(getResources(),
				R.drawable.puzzle3);
		pic=Bitmap.createScaledBitmap(pic, (int)width*7/8, (int)height*7/12, true);
		
		restart = BitmapFactory.decodeResource(getResources(),
				R.drawable.restart);
		restart=Bitmap.createScaledBitmap(restart, (int)width*14/20, (int)height*1/21, true);
		
		infoback=BitmapFactory.decodeResource(getResources(),R.drawable.tag);
		infoback=Bitmap.createScaledBitmap(infoback, (int)width*26/32, (int)height*9/64, true);
		
		if(back.equalsIgnoreCase("Stripes"))
			background=BitmapFactory.decodeResource(getResources(),R.drawable.style1);
		else if(back.equalsIgnoreCase("Fabric"))
			background=BitmapFactory.decodeResource(getResources(),R.drawable.style2);
		else if(back.equalsIgnoreCase("Fabric2"))
			background=BitmapFactory.decodeResource(getResources(),R.drawable.style3);
		else if(back.equalsIgnoreCase("Squares"))
			background=BitmapFactory.decodeResource(getResources(),R.drawable.style4);
		else
			background=BitmapFactory.decodeResource(getResources(),R.drawable.blank);
		
		background=Bitmap.createScaledBitmap(background, (int)width, (int)height, true);
		

		
		
		
		picX= (int) (width/16);
		picY=(int) (height*2/8);
		list=new ArrayList<Piece>();
		picHeight=pic.getHeight();
		picWidth=pic.getWidth();
		blankPic=Bitmap.createBitmap(background,0,0,Math.round(picWidth/scale), Math.round(picHeight/scale));
		//initialize the chunks
		for(int i=0;i<scale;i++){//each row
			for(int j=0;j<scale;j++)//each column
			{
				
					Bitmap chunk=Bitmap.createBitmap(pic, Math.round(j*picWidth/scale),Math.round(i*picHeight/scale) ,Math.round(picWidth/scale), Math.round(picHeight/scale));
					list.add(new Piece(chunk,scale*i+j));
				
			}
			
		}
		
			lastPic=list.get(scale*scale-1).getImage();
			list.set(scale*scale-1,new Piece(blankPic,scale*scale-1));
		//randomize the permutation of chunks
		random();
		
    	
	}
	
	public void setScale(String s){
		if(s=="3*3")
			scale=3;
		else if(s=="4*4")
			scale=4;
		else if(s=="5*5")
			scale=5;
	}
	public void setDifficulty(String d){
		if(d=="Easy")
			;
		else if(d=="Veteran")
			;
		
	}
	//set the picture of playing
	public void setPic(Bitmap picture){
		if(picture!=null){
			pic=Bitmap.createScaledBitmap(picture, (int)width*7/8, (int)height*7/12, true);
			picWidth=pic.getWidth();
			picHeight=pic.getHeight();
		
			
			list.clear();
			for(int i=0;i<scale;i++){//each row
				for(int j=0;j<scale;j++)//each column
				{

						Bitmap chunk=Bitmap.createBitmap(pic, j*picWidth/scale,i*picHeight/scale ,picWidth/scale, picHeight/scale);
						list.add(new Piece(chunk,scale*i+j));

				}
				
			}
			
				lastPic=list.get(scale*scale-1).getImage();
				list.set(scale*scale-1,new Piece(blankPic,scale*scale-1));
			
			random();
			
		}
			
	}
	//return a Game instance for the StartGame Activity
	public static void init(Activity mActivity, int screenWidth,
	    int screenHeight,int scale,String difficulty,String back,boolean touchtone) {
	    sInstance = new Game(mActivity, screenWidth, screenHeight,scale,difficulty,back,touchtone);
	}
	    
	public static Game getInstance() {
		return sInstance;
	}
	
	private void random(){
		Random r=new Random();
		int direction;
		int X=scale-1;
		int Y=scale-1;
		
		for(int i=0;i<scale*scale*20;i++){
			direction=r.nextInt(4);// get the random direction of the blank chunk 0 for up, 1 for down, 2 for left and 3 for right
			switch(direction){
				case 0:// UP
					if(X-1>=0){
						replace(X,Y,X-1,Y);
						X=X-1;
					}
						break;
				case 1://DOWN
					if(X+1<scale){
						replace(X,Y,X+1,Y);
						X=X+1;
					}
						break;
				case 2://LEFT
					if(Y-1>=0){
						replace(X,Y,X,Y-1);
						Y=Y-1;
					}
						break;
				case 3://RIGHT
					if(Y+1<scale){
						replace(X,Y,X,Y+1);
						Y=Y+1;
					}
						break;
			}
		}
		count=0;
		startTime=System.currentTimeMillis();

	}
	private void replace(int x1,int y1,int x2,int y2){
		
		int indexa=x1*scale+y1;
		int indexb=x2*scale+y2;
		Piece temp=new Piece(list.get(indexa));
		list.set(indexa, new Piece(list.get(indexb)));
		list.set(indexb, temp);
		count++;
	}
	
	//refresh and draw the current permutation of chunks when moving
	protected void draw(){
		mPaint = new Paint();
		Paint blue = new Paint(Paint.ANTI_ALIAS_FLAG);
		blue.setColor(getResources().getColor(R.color.blue));
		blue.setStyle(Style.FILL);
		blue.setTextSize(picWidth * 0.05f);
		//blue.setTextScaleX(width / height);
		blue.setTextAlign(Paint.Align.CENTER);
		Paint hintNum = new Paint(Paint.ANTI_ALIAS_FLAG);
		hintNum.setColor(getResources().getColor(R.color.black));
		hintNum.setStyle(Style.FILL);
		hintNum.setTextSize(picWidth * 0.05f);
		hintNum.setTextAlign(Paint.Align.CENTER);

		//sCanvas.drawColor( 0, PorterDuff.Mode.CLEAR ); 
		sCanvas.drawBitmap(background, 0, 0,mPaint);
		

		
		for(int i=0;i<scale;i++){//each row
			for(int j=0;j<scale;j++)//each column
			{	
				// Weird Adjustment!
				if((scale==3&&i==2)||(scale==4&&i>=2))
					sCanvas.drawBitmap(list.get(i*scale+j).getImage(),picX+j*picWidth/scale,-1+picY+i*picHeight/scale,mPaint);
				else
					sCanvas.drawBitmap(list.get(i*scale+j).getImage(),picX+j*picWidth/scale,picY+i*picHeight/scale,mPaint);
				
				if(difficulty.equalsIgnoreCase("Easy")&&list.get(i*scale+j).index!=scale*scale-1){
					sCanvas.drawText(""+(list.get(i*scale+j).index+1),picX+j*picWidth/scale+picX/2,picY+i*picHeight/scale+picY/6,hintNum);
					
				}
			}
			
		}
		sCanvas.drawBitmap(infoback, (int)width*3/33, (int)height*1/32, mPaint);
		
		sCanvas.drawText("Count: "+count,(int)width*18/40,(int)height*25/224,blue);
		if(isOver==false)
			currentTime=System.currentTimeMillis();
		int elapsedSeconds = (int)(currentTime - startTime)/1000;
		int elapsedMinites=elapsedSeconds/60;
		elapsedSeconds=elapsedSeconds-elapsedMinites*60;
		if(elapsedSeconds<10)
			sCanvas.drawText("Time: "+elapsedMinites+":0"+elapsedSeconds,(int)width*7/10,(int)height*25/224,blue);
		else
			sCanvas.drawText("Time: "+elapsedMinites+":"+elapsedSeconds,(int)width*7/10,(int)height*25/224,blue);
		if(isOver==true){
			
			sCanvas.drawBitmap(restart, (int)width*4/27, (int)height*6/34, mPaint);
			
			if(scale!=5)
				sCanvas.drawBitmap(lastPic,picX+picWidth*(scale-1)/scale,-1+picY+picHeight*(scale-1)/scale,mPaint);
			else
				sCanvas.drawBitmap(lastPic,picX+picWidth*(scale-1)/scale,picY+picHeight*(scale-1)/scale,mPaint);
				
			}
			
	}
	//thread control method
	@Override
	public void run() {
    	while (loop) {
    	    try {
    	    	synchronized (mSurfaceHolder) {
 	    		sCanvas = mSurfaceHolder.lockCanvas();
 	    		draw();
 	    		mSurfaceHolder.unlockCanvasAndPost(sCanvas);
 	    	 }
    	    	 Thread.sleep(33);
        	   }catch (Exception e) {

    	    		//((Activity) context).finish();

    	     }
    	}
	    	
	}
	//check if the player finished the Game
	private boolean isDone(){
		for(int i=0;i<scale*scale;i++){
			if(list.get(i).index!=i)
				return false;
		}
		return true;
	}
	private void restart(){
		random();
		isOver=false;
	}
    public void play(Context context,int resource){
    	stop(context);
    	//if(Prefs.getMusic(context)){
    	mp=MediaPlayer.create(context,resource);
    	mp.setLooping(false);
    	mp.start();
    	//}
    }
    public void stop(Context context){
    	if(mp!=null){
    		mp.stop();
    		mp.release();
    		mp=null;
    	}
    }
	//Screen Event listener
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		else{
			selY=Math.round(event.getX()-picX)/(picWidth/scale);
			selX=Math.round(event.getY()-picY)/(picHeight/scale);
			//restart Button
			//restart=Bitmap.createScaledBitmap(restart, (int)width*7/10, (int)height*1/21, true);
			//sCanvas.drawBitmap(restart, (int)width*4/27, (int)height*3/17, mPaint);
			if(isOver==true&&event.getY()<=(height*80/357)&&event.getY()>(height*4/26)&&event.getX()<(width*23/27)&&event.getX()>(width*14/30)){
				restart();
				return true;
			}				
			if(isOver==false&&event.getX()>picX&&selX<scale&&event.getY()>picY&&selY<scale){
				//play touch tone
				if(touchtone==true)
					play(getContext(),R.raw.alarm);
				if(selX-1>=0&&list.get((selX-1)*scale+selY).index==scale*scale-1)//UP
					replace(selX,selY,selX-1,selY);
				else if(selX+1<scale&&list.get((selX+1)*scale+selY).index==scale*scale-1)//DOWN
					replace(selX,selY,selX+1,selY);
				else if(selY-1>=0&&list.get(selX*scale+selY-1).index==scale*scale-1)//LEFT
					replace(selX,selY,selX,selY-1);
				else if(selY+1<scale&&list.get(selX*scale+selY+1).index==scale*scale-1)//RIGHT
					replace(selX,selY,selX,selY+1);
				
				if(isDone())
					isOver=true;
				return true;
			}
			else{
				//implement anther click action here
				return false;
			}
		}
	}


	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		
		aThread=new Thread(this);
    	aThread.start();
		
	}




	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		loop = false;
		Thread dummy=aThread;
		
		aThread=null;
		dummy.interrupt();
		
	}

}
