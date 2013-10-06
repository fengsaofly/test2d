package com.android.test2d;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window; 
import android.view.SurfaceHolder.Callback;  
public class MainActivity extends Activity {
	int width = 0;
	int height = 0;
	
	MySurfaceView mv;   
	Bitmap tmp;  
	  
	    /** Called when the activity is first created. */  
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        // setContentView(R.layout.main);  
	       
	        mv = new MySurfaceView(this);  
	        setContentView(mv);  
	        
//	        Timer timer = new Timer();  
//	        timer.scheduleAtFixedRate(new MyTask(), 1, 1);  
//	        timer.schedule(new MyTask(), 100000);//只执行一次，1s后执行
	        
//	        timer.schedule(new MyTask(), delay)

	         
	    }  

//	    private class MyTask extends TimerTask {  
//	        @Override  
//	        public void run() {  
//	            mv.postInvalidate(); 
//	            System.out.println("Time`s up!");
//	        }  
//	    }  
	}  

 class MySurfaceView extends SurfaceView implements Runnable, Callback {  
    private SurfaceHolder mHolder; // 用于控制SurfaceView  
    private Thread t; // 声明一条线程  
    private volatile boolean flag; // 线程运行的标识，用于控制线程  
    private Canvas mCanvas; // 声明一张画布  
    private Paint p; // 声明一支画笔   
    private Timer timer;
  private int width;
  private int height;
  private int radius = 150;
  private int step = 20;
  private int pixel = 64;
  private int speed =1;
  boolean halfCircle=false;
  int left = 0;
  int top = 0;
  double angle = 0;
  
  int index=0;
  private Point circleCenter = new Point();
  
    public MySurfaceView(Context context) {  
        super(context);  
//        this.timer = timer;
        mHolder = getHolder(); // 获得SurfaceHolder对象  
        mHolder.addCallback(this); // 为SurfaceView添加状态监听  
        p = new Paint(); // 创建一个画笔对象  
        p.setColor(Color.WHITE); // 设置画笔的颜色为白色  
        setFocusable(true); // 设置焦点  
        
       DisplayMetrics dm = new DisplayMetrics();
       ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
       width = dm.widthPixels;    //得到宽度
       height = dm.heightPixels;  //得到高度
       circleCenter.x = width/2;
       circleCenter.y = height/2;
    }  
  
    /** 
     * 当SurfaceView创建的时候，调用此函数 
     */  
    @Override  
    public void surfaceCreated(SurfaceHolder holder) {  
        t = new Thread(this); // 创建一个线程对象  
        flag = true; // 把线程运行的标识设置成true  
        t.start(); // 启动线程  
        
        
    }  
  
    /** 
     * 当SurfaceView的视图发生改变的时候，调用此函数 
     */  
    @Override  
    public void surfaceChanged(SurfaceHolder holder, int format, int width,  
            int height) {  
    }  
  
    /** 
     * 当SurfaceView销毁的时候，调用此函数 
     */  
    @Override  
    public void surfaceDestroyed(SurfaceHolder holder) {  
        flag = false; // 把线程运行的标识设置成false  
        mCanvas.restore();//销毁保存
        mHolder.removeCallback(this);  
    }  
  
    /** 
     * 当屏幕被触摸时调用 
     */  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
  
        return true;  
    }  
  
    /** 
     * 当用户按键时调用 
     */  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
  
    @Override  
    public boolean onKeyUp(int keyCode, KeyEvent event) {  
        surfaceDestroyed(mHolder);  
        return super.onKeyDown(keyCode, event);  
    }  
  
    @Override  
    public void run() {  
        while (flag) {  
            try {  
            	
                synchronized (mHolder) {  
//                	if(Math.abs((angle-Math.PI*2))<0.1){
//                		System.out.println("break");
//                		flag=false;
//                		
//                		break;
//                	}
                    Thread.sleep(3000); // 让线程休息100毫秒  
                   // ClearDraw();//清屏
                    Draw(); // 调用自定义画画方法  
                }  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            } finally {  
                if (mCanvas != null) {  
                    // mHolder.unlockCanvasAndPost(mCanvas);//结束锁定画图，并提交改变。  
  
                }  
            }  
        }  
    }  
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {  
	   int width = bitmap.getWidth();  
	   int height = bitmap.getHeight();  
	   Matrix matrix = new Matrix();  
	   float scaleWidht = ((float) w / width);  
	   float scaleHeight = ((float) h / height);  
	   matrix.postScale(scaleWidht, scaleHeight);  
	   Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);  
	   return newbmp;  
	} 
    /** 
     * 自定义一个方法，在画布上画一个圆 
     */  
    protected void Draw() {  
        mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画  
        mCanvas.save();//保存
        if (mCanvas != null) {  
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
            paint.setColor(Color.WHITE);  
            paint.setStrokeWidth(5);  
            /*设置paint的 style 为STROKE：空心*/
            paint.setStyle(Paint.Style.STROKE);  
            
            
            
            Bitmap pic = addTextToBitmap(String.valueOf(index));// ((BitmapDrawable) getResources().getDrawable(  
                 //  R.drawable.cube)).getBitmap();  
            
         
            left = (int)(radius*Math.cos(angle)-0)+circleCenter.x;
            
            top = (int)(radius*Math.sin(angle)-0)+circleCenter.y;
//            mCanvas.drawBitmap(zoomBitmap(pic, 64, 64),0,0, mPaint);
            
            mCanvas.drawCircle(circleCenter.x, circleCenter.y, radius, paint);
            mCanvas.drawBitmap(pic,left-pixel/2, top-pixel/2, paint);  
            mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
            
            angle+=Math.PI/4;
            index = (index+1)%8;
            
        }  
    }  
    public Bitmap addTextToBitmap(String str){
    	
    	Bitmap bmp  = Bitmap.createBitmap(64,64, Bitmap.Config.ARGB_8888); //图象大小要根据文字大小算下,以和文本长度对应
    	
    	Canvas canvasTemp = new Canvas(bmp);
    	canvasTemp.drawColor(Color.WHITE); 
    	Paint p = new Paint(); 
    	String familyName ="宋体"; 
    	Typeface font = Typeface.create(familyName,Typeface.BOLD); 
    	p.setColor(Color.RED); p.setTypeface(font); 
    	p.setTextSize(22); 
    	canvasTemp.drawText(str,20,50,p); 
    	
    	return bmp;
    }
    void ClearDraw() {  
        Canvas canvas = mHolder.lockCanvas(null);  
        canvas.drawColor(Color.BLACK);// 清除画布  
        mHolder.unlockCanvasAndPost(canvas);  
  
    } 
}  