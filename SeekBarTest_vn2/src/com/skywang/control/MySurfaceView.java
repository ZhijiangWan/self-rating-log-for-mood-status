package com.skywang.control;

import java.util.ArrayList;
import java.util.List;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;
import android.graphics.Shader;
public class MySurfaceView extends SurfaceView implements  Callback {

	private SurfaceHolder mHolder;    //用于控制SurfaceView

	private Canvas mCanvas;    //声明一张画布

	private Paint mPaint, qPaint,nPaint,backPaint,backPaint_converse;    //声明两只画笔

	private Path mPath, qPath, tPath;    //声明三条路径

	private int mX, mY;    //用于控制图形的坐标

	//分别 代表贝塞尔曲线的开始坐标,结束坐标,控制点坐标
	private int qStartX, qStartY, qEndX, qEndY, qControlX, qCOntrolY;

	private int screenW, screenH;    //用于屏幕的宽高

	float screenWidth  ;
	float screenHeight  ; //获得屏幕像素
	float[] array=new float[201];
	int height_f;// y of first node

	int height_s;// y of first node
	int height_red;//the height of red line
	int height_pic;
	int width_pic;

	private Thread mThread;    //声明一个线程

	//flag用于线程的标识,xReturn用于标识图形坐标是否返回,cReturn用于标识贝塞尔曲线的控制点坐标是否返回
	private boolean flag, xReturn, cReturn;    

	/**
	 *     构造函数，主要对一些对象初始化
	 */

	public MySurfaceView(Context context) {
		super(context);  
		//获得屏幕的宽和高


	}
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context,attrs);  

		mHolder = this.getHolder();    //获得SurfaceHolder对象
		mHolder.addCallback(this);    //添加状态监听

		backPaint=new Paint();
		backPaint_converse=new Paint();

		mPaint = new Paint();    //创建一个画笔对象
		mPaint.setColor(Color.BLACK);    //设置画笔的颜色为白色
		mPaint.setStrokeWidth((float)3.0);
		mPaint.setTextSize((float)20.0);

		nPaint = new Paint();    //创建一个画笔对象,用于写5个分量
		nPaint.setColor(Color.BLACK);    //设置画笔的颜色为白色
		nPaint.setStrokeWidth((float)3.0);
		nPaint.setTextSize((float)20.0);

		qPaint = new Paint();    //创建一个画笔对象
		qPaint.setAntiAlias(true);    //消除锯齿
		qPaint.setStyle(Style.STROKE);    //设置画笔风格为描边
		qPaint.setStrokeWidth(3);    //设置描边的宽度为3
		qPaint.setColor(Color.YELLOW);    //设置画笔的颜色为绿色

		mPath = new Path();    
		qPath = new Path();
		tPath = new Path();

		// setFocusable(true);    //设置焦点

	}

	/**
	 *     当SurfaceView创建的时候调用
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		//获得屏幕的宽和高
		screenW = this.getWidth();
		screenH = this.getHeight();

		int step=(int) (screenW/100);
		for(int i=0;i<=100;i++){

			array[i]=step*i;

		}//x of first node and second node

		height_f=(int) (screenH/2)+30;// y of first node

		height_s=(int) (screenH/2)-50+30;// y of first node

		//the height of red line
		height_red=height_s-80;
		//the height of picture
		height_pic=height_red-50;



		mDraw();


	}

	/**
	 *     当SurfaceView视图发生改变的时候调用
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 *     当SurfaceView销毁的时候调用
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;    //设置线程的标识为false
	}

	/**
	 *     线程运行的方法,当线程start后执行
	 */


	/**
	 *     自定义的绘图方法
	 */
	public void mDraw() {

		mCanvas = mHolder.lockCanvas();    //获得画布对象,开始对画布画画


		mCanvas.drawColor(Color.WHITE);    //设置画布颜色为黑色

		canvasMethod(mCanvas);    //调用自定义的方法,主要是在传进去的画布对象上画画

		mHolder.unlockCanvasAndPost(mCanvas);    //把画布显示在屏幕上
	}

	public void showText(Canvas canvas, String text,float height,float width){  
		float w;  
		final int len = text.length();  
		//  float py = 0 + top;  
		float py=height;
		for(int i=0; i<len; i ++){  
			char c = text.charAt(i);  
			w = nPaint.measureText(text, i, i+1);//获取字符宽度  
			StringBuffer b = new StringBuffer();  
			b.append(c);  

			if(isChinese(c)){  
				py += w;  

				canvas.drawText(b.toString(), width, py,nPaint); //中文处理方法   
			}
		}  
	}  

	private boolean isChinese(char c) {  
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
			return true;  
		}  
		return false;  
	}  


	/**
	 * 自定义的方法,简单绘制一些基本图形
	 * @param mCanvas
	 * 把图形画在mCanvas上
	 */
	public void canvasMethod(Canvas mCanvas) {

		int colorStart = Color.GREEN;
		//int color1 = Color.GRAY;
		int colorEnd = Color.RED;
		//Paint backPaint=new Paint();
		LinearGradient backGradient = new LinearGradient(0, 0, mCanvas.getWidth(),0 , new int[]{colorStart, colorEnd}, null, Shader.TileMode.CLAMP);
		LinearGradient backGradient_converse = new LinearGradient(0, 0, mCanvas.getWidth(),0 , new int[]{colorEnd,colorStart}, null, Shader.TileMode.CLAMP);
		
		backPaint.setShader(backGradient);
		
		backPaint_converse.setShader(backGradient_converse);

		mCanvas.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), backPaint);

		for(int i=0;i<=100;i++){
			if(i==0){
				//获得icon的Bitmap对象
				// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.too_sad);
				//画图片
				// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
				//drawText(mCanvas,"一点也不", array[i]+40, height_pic, mPaint,90); 
				showText(mCanvas,"一点也不",height_pic, array[i]+25);

			}else if(i==25){
				//获得icon的Bitmap对象
				//Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.very_sad);
				//画图片
				// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
				showText(mCanvas,"有一点",height_pic, array[i]+25);

			}else if(i==50){
				//获得icon的Bitmap对象
				// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.little_sad);
				//画图片
				// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
				showText(mCanvas,"中等",height_pic, array[i]+25);

			}else if(i==75){
				//获得icon的Bitmap对象
				//Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.little_happy);
				//画图片
				// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
				showText(mCanvas,"较多",height_pic, array[i]+25);

			}else if(i==100){
				//获得icon的Bitmap对象
				// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.very_happy);
				//画图片
				//  mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
				showText(mCanvas,"完全如此",height_pic, array[i]+25);

			}

			if(i%25==0){
				int scale=i;
				mCanvas.drawLine(array[i]+40, height_f+30, array[i]+40, height_s-30, mPaint);

				mCanvas.drawText(scale+"", array[i]+25, height_f+70, mPaint);

			}else{
				mCanvas.drawLine(array[i]+40, height_f+10, array[i]+40, height_s-10, mPaint);

			}
		}    
		mCanvas.drawLine(array[0]+40, height_f+30, array[0]+40, height_s-30, qPaint);//the picture should align with the red line

	}

	public void canvasMethod_self_def(Canvas mCanvas,int scale,int emotion_polar) {
		if(emotion_polar==-1){
			mCanvas.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), backPaint);
			for(int i=0;i<=100;i++){
				if(i==0){
					//获得icon的Bitmap对象
					// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.too_sad);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					//drawText(mCanvas,"一点也不", array[i]+40, height_pic, mPaint,90); 
					showText(mCanvas,"一点也不",height_pic, array[i]+25);

				}else if(i==25){
					//获得icon的Bitmap对象
					//Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.very_sad);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"有一点",height_pic, array[i]+25);

				}else if(i==50){
					//获得icon的Bitmap对象
					// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.little_sad);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"中等",height_pic, array[i]+25);

				}else if(i==75){
					//获得icon的Bitmap对象
					//Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.little_happy);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"较多",height_pic, array[i]+25);

				}else if(i==100){
					//获得icon的Bitmap对象
					// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.very_happy);
					//画图片
					//  mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"完全如此",height_pic, array[i]+25);

				}

				if(i%25==0){
					int scale1=i;
					mCanvas.drawLine(array[i]+40, height_f+30, array[i]+40, height_s-30, mPaint);

					mCanvas.drawText(scale1+"", array[i]+25, height_f+70, mPaint);

				}else{
					mCanvas.drawLine(array[i]+40, height_f+10, array[i]+40, height_s-10, mPaint);

				}
			}    
			mCanvas.drawLine(array[scale]+40, height_f+30, array[scale]+40, height_s-30, qPaint);

		}else if(emotion_polar==1){
			mCanvas.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), backPaint_converse);
			for(int i=0;i<=100;i++){
				if(i==0){
					//获得icon的Bitmap对象
					// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.too_sad);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					//drawText(mCanvas,"一点也不", array[i]+40, height_pic, mPaint,90); 
					showText(mCanvas,"一点也不",height_pic, array[i]+25);

				}else if(i==25){
					//获得icon的Bitmap对象
					//Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.very_sad);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"有一点",height_pic, array[i]+25);

				}else if(i==50){
					//获得icon的Bitmap对象
					// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.little_sad);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"中等",height_pic, array[i]+25);

				}else if(i==75){
					//获得icon的Bitmap对象
					//Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.little_happy);
					//画图片
					// mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"较多",height_pic, array[i]+25);

				}else if(i==100){
					//获得icon的Bitmap对象
					// Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.very_happy);
					//画图片
					//  mCanvas.drawBitmap(mBitmap, array[i]+10, height_pic, mPaint);
					showText(mCanvas,"完全如此",height_pic, array[i]+25);

				}

				if(i%25==0){
					int scale1=i;
					mCanvas.drawLine(array[i]+40, height_f+30, array[i]+40, height_s-30, mPaint);

					mCanvas.drawText(scale1+"", array[i]+25, height_f+70, mPaint);

				}else{
					mCanvas.drawLine(array[i]+40, height_f+10, array[i]+40, height_s-10, mPaint);

				}
			}    
			mCanvas.drawLine(array[scale]+40, height_f+30, array[scale]+40, height_s-30, qPaint);
		}


	}

	public void drawscale(int scale,int emotion_polar){
		mCanvas = mHolder.lockCanvas();    //获得画布对象,开始对画布画画

		mCanvas.drawColor(Color.WHITE);    //设置画布颜色为黑色
		canvasMethod_self_def(mCanvas,scale,emotion_polar);   

		mHolder.unlockCanvasAndPost(mCanvas);    //把画布显示在屏幕上

	}





}