package com.android.rrotate;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.Handler;

import android.util.DisplayMetrics;

import android.view.WindowManager;
import android.widget.ImageView;


import java.util.HashMap;
import java.util.Map;

/**
 * User: ye.yang
 * Date: 13-5-10
 * Time: 上午10:57
 */
public class MainActivity extends Activity {
    private float earthDegree = 0f;
    private float moonDegree = 0f;

    private Canvas c;
    private Paint p;
    private Point circleCenter = new Point();
    private Handler handler = new Handler();
    private ImageView iv;
    private int radius = 150;
    private int w,h;
    private Bitmap earthBmp,moonBmp;
    private int earthW,earthH;
    private int moonW,moonH;
    Bitmap backgroundBmp = null;
    public static Bitmap[] b = new Bitmap[8];
    int index=0;
    int first=0;
    Vector<MyPoint> vc=new Vector<MyPoint>();
    double wspeed =0;
    int count = 0;
    int  showNum = 20;
    boolean circleFull = false;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
          
            handler.postDelayed(this,(long)(0.01*200000));
           

           // Rect r = new Rect(w/2-earthW/2,h/2-earthH/2,w/2+earthW/2,h/2+earthH/2);
//            if(first!=0){
//            	c.restore();
//            }

            
            
            if(count==20){
            	circleFull = true;
            }
            //c.rotate(earthDegree,circleCenter.x, circleCenter.y);
             c.save();
            if(!circleFull){
            	count = (count+1)%(showNum+1);
	            for(int i=0;i<count;i++){
	            	
		            Bitmap pic = addTextToBitmap(String.valueOf(count-i-1));
		            //c.drawBitmap(pic, null, r, p);  
		            c.drawBitmap(pic, (float) vc.get(i).getX(), (float) vc.get(i).getY(), p);
	            
	            } 
            }
            else{
            	int j = 0;
            	for(int i=0;i<count;i++){
            		 j = (20-i+index)%20;
            		Bitmap pic = addTextToBitmap(String.valueOf(j));
   		            //c.drawBitmap(pic, null, r, p);  
   		            c.drawBitmap(pic, (float) vc.get(i).getX(), (float) vc.get(i).getY(), p);
   	            
            	}
            	index=(index+1)%20;
            }
//            c.restore();
            iv.setImageBitmap(backgroundBmp);
           
           
          //  c.rotate(earthDegree);
//            c.drawBitmap(b[first], circleCenter.x+radius-32, circleCenter.y-32, p);
            
            
            c.restore();
           
            
//            c.rotate(earthDegree,circleCenter.x, circleCenter.y);
//            c.drawBitmap(b[first], circleCenter.x+radius-32, circleCenter.y-32, p);  
            
 //           c.restore();
            
            earthDegree+=45;
            
           
           
          //  p.setStyle(Paint.Style.STROKE);
         //   c.drawRect(r,p);

            //自转一天的角度=公转30天的角度，都是360°，所以简单/30
//            moonDegree-=(1f/10);//除与30太慢了，就/10了
//            Rect rMoon = new Rect(w/2-moonW/2-50,h/2-moonH/2-50,w/2+moonW/2-50,h/2+moonH/2-50);
//            c.save();
//            c.rotate(moonDegree,w/2,h/2);
//            c.drawBitmap(moonBmp,null,rMoon,p);
//            c.restore();
           
          
          
            
            
          
            if(first<7)
            first++;
            else
            	first=0;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        w=getMetrics().get("width");
        h=getMetrics().get("height");
        circleCenter.x = w/2;
        circleCenter.y = h/2;
        earthBmp = zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth),64,64);
      
        moonBmp = BitmapFactory.decodeResource(getResources(), R.drawable.moon);
        earthW = earthBmp.getWidth();
        earthH = earthBmp.getHeight();
        moonW = moonBmp.getWidth();
        moonH = moonBmp.getHeight();
        b[0]= 		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth),64,64);
        b[1]=		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth1),64,64);
        b[2]=		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth2),64,64);
        b[3]=		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth3),64,64);
        b[4]=		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth4),64,64);
        b[5]=		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth5),64,64);
        b[6]=		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth6),64,64);
        b[7]=		zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.earth7),64,64);
        iv = (ImageView)findViewById(R.id.rotate_iv);
        backgroundBmp = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        c = new Canvas(backgroundBmp);
        p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        p.setColor(Color.RED);
        c.drawCircle(circleCenter.x, circleCenter.y, radius, p);
        handler.post(task);
        
        
        double curX =radius;
        double curY = 0;
        wspeed = 2*Math.PI/showNum;
       
        for(int i=0;i<showNum;i++){
        	
        	MyPoint curPoint = new MyPoint(curX+circleCenter.x-32, curY+circleCenter.y-32);
        	vc.add(curPoint);
        	double tempX = curX,tempY = curY;
        	curX = Math.cos(wspeed)*tempX - Math.sin(wspeed)*tempY;
        	curY = Math.cos(wspeed)*tempY + Math.sin(wspeed)*tempX;
        	
        	
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

    private Map<String, Integer> getMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = getWindowManager();
        if(wm == null)
            wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        }
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        resultMap.put("width", width);
        resultMap.put("height", height);
        return resultMap;
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
}
