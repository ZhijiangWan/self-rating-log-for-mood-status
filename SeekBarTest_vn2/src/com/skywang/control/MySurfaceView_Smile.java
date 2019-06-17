package com.skywang.control;

import java.util.ArrayList;
import java.util.List;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View.MeasureSpec;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;
public class MySurfaceView_Smile extends SurfaceView implements  Callback {

	private SurfaceHolder mHolder;    //用于控制SurfaceView

	private Canvas mCanvas;    //声明一张画布

	private Paint mPaint, qPaint,nPaint;    //声明两只画笔

	private Path mPath, qPath, tPath;    //声明三条路径

	private int mX, mY;    //用于控制图形的坐标

	//分别 代表贝塞尔曲线的开始坐标,结束坐标,控制点坐标
	private int qStartX, qStartY, qEndX, qEndY, qControlX, qCOntrolY;

	private int screenW, screenH;    //用于屏幕的宽高
	public int final_view_width,final_view_height;

	float screenWidth  ;
	float screenHeight  ; //获得屏幕像素
	float[] array=new float[201];
	int height_f;// y of first node

	//left eye
	float LE_LP_x;//x of left point of left eye
	float LE_LP_y;//y of left point of left eye
	float LE_MP_x;
	float LE_MP_y;
	float LE_RP_x;
	float LE_RP_y;

	//right eye
	float RE_LP_x;//x of left point of right eye
	float RE_LP_y;//y of left point of right eye
	float RE_MP_x;
	float RE_MP_y;
	float RE_RP_x;
	float RE_RP_y;

	//mouth
	float M_LP_x;//x of left point of right eye
	float M_LP_y;//y of left point of right eye
	float M_MP_x;
	float M_MP_y;
	float M_RP_x;
	float M_RP_y;

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

	public MySurfaceView_Smile(Context context) {
		super(context);  
		//获得屏幕的宽和高


	}
	public MySurfaceView_Smile(Context context, AttributeSet attrs) {
		super(context,attrs);  

		mHolder = this.getHolder();    //获得SurfaceHolder对象
		mHolder.addCallback(this);    //添加状态监听

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
		qPaint.setStyle(Style.FILL_AND_STROKE);    //设置画笔风格为描边
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

		height_f=(int) (screenH/2);// y of first node

		height_s=(int) (screenH);// y of first node

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

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//先声明两个int值来表示最终的width和height并给定一个默认的大小
		int width_size  = 200;
		int height_size = 200;
		//使用MeasureSpec分别获取width和height的MODE和SIZE
		int HEIGHT_MODE = MeasureSpec.getMode(heightMeasureSpec);
		int HEIGHT_SIZE = MeasureSpec.getSize(heightMeasureSpec);
		int WIDTH_MODE = MeasureSpec.getMode(widthMeasureSpec);
		int WIDTH_SIZE = MeasureSpec.getSize(widthMeasureSpec);
		if (HEIGHT_MODE == MeasureSpec.EXACTLY) {
			height_size = HEIGHT_SIZE;       //如果测量模式是精确的话 那么就直接使用获取到的值就好了
		}else if (HEIGHT_MODE == MeasureSpec.AT_MOST){  //如果是最大值模式的话 那么久比较获取的和设定的默认值那个小就使用那个.ps:Math.min(int a,int b)是比较数值大小的.
			height_size = Math.min(HEIGHT_SIZE,height_size);
		}
		if (WIDTH_MODE == MeasureSpec.EXACTLY){
			width_size = WIDTH_SIZE;
		}else if (WIDTH_MODE == MeasureSpec.AT_MOST){
			width_size = Math.min(WIDTH_SIZE,width_size);
		}
		//测量完毕后得到的了width和height通过setMeasuredDimension()设置width和height,真正决定具体大小的是setMeasuredDimension()的两个参数.
		setMeasuredDimension(width_size,height_size);
		final_view_height=height_size;
		final_view_width=width_size;
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


		//mCanvas.drawLine(array[0]+40, height_f+30, array[0]+40, height_s-30, qPaint);//the picture should align with the red line

		Paint paint = new Paint();       //实例化Paint
		paint.setColor(Color.BLACK);     //设置圆的颜色
		paint.setAntiAlias(true);        //设置抗锯齿
		paint.setStyle(Paint.Style.STROKE);  //设置样式
		paint.setStrokeWidth(5);
		mCanvas.drawCircle(height_f, height_f, height_f, qPaint);
		//mCanvas.drawCircle(height_f-35, height_f-25, height_f/9, paint);
		//mCanvas.drawCircle(height_f+35, height_f-25, height_f/9, paint);
		//left eye
		Path LE_Path, RE_Path, M_Path;
		LE_LP_x=height_f-75;//x of left point of left eye
		LE_LP_y=height_f-35;//y of left point of left eye
		LE_MP_x=height_f-45;
		LE_MP_y=height_f-75;
		LE_RP_x=height_f-15;
		LE_RP_y=height_f-35;
		LE_Path=new Path();
		LE_Path.moveTo(LE_LP_x, LE_LP_y);
		LE_Path.quadTo(LE_MP_x, LE_MP_y, LE_RP_x, LE_RP_y); 
		mCanvas.drawPath(LE_Path, paint);
		LE_Path.close();
		//right eye
		
		RE_LP_x=height_f+25;//x of left point of right eye
		RE_LP_y=height_f-35;//y of left point of right eye
		RE_MP_x=height_f+55;
		RE_MP_y=height_f-75;
		RE_RP_x=height_f+85;
		RE_RP_y=height_f-35;
		RE_Path=new Path();
		RE_Path.moveTo(RE_LP_x, RE_LP_y);
		RE_Path.quadTo(RE_MP_x, RE_MP_y, RE_RP_x, RE_RP_y); 
		mCanvas.drawPath(RE_Path, paint);
		RE_Path.close();
		//mouth
		M_LP_x=height_f-75;//x of left point of right eye
		M_LP_y=height_f+30;//y of left point of right eye
		M_MP_x=height_f;
		M_MP_y=height_f+85;
		M_RP_x=height_f+85;
		M_RP_y=height_f+30;
		M_Path=new Path();
		M_Path.moveTo(M_LP_x, M_LP_y);
		M_Path.quadTo(M_MP_x, M_MP_y, M_RP_x, M_RP_y); 
		mCanvas.drawPath(M_Path, paint);
		M_Path.close();

	}

	public void canvasMethod_self_def(Canvas mCanvas,int scale,int emotion_polar) {

		Paint paint = new Paint();       //实例化Paint
		paint.setColor(Color.BLACK);     //设置圆的颜色
		paint.setAntiAlias(true);        //设置抗锯齿
		paint.setStyle(Paint.Style.STROKE);  //设置样式
		paint.setStrokeWidth(5);
		mCanvas.drawCircle(height_f, height_f, height_f, qPaint);
		//mCanvas.drawCircle(height_f-35, height_f-25, height_f/9, paint);
		//mCanvas.drawCircle(height_f+35, height_f-25, height_f/9, paint);
		Path LE_Path, RE_Path, M_Path;
		//left eye
		float up_pos=LE_MP_y;
		float down_pos=2*LE_RP_y-LE_MP_y;
		float step=Math.abs(down_pos-up_pos)/100;
		float LE_MP_y_temp=up_pos+step*scale;
		LE_Path=new Path();
		LE_Path.moveTo(LE_LP_x, LE_LP_y);
		LE_Path.quadTo(LE_MP_x, LE_MP_y_temp, LE_RP_x, LE_RP_y); 
		mCanvas.drawPath(LE_Path, paint);
		LE_Path.close();
		//right eye
		up_pos=RE_MP_y;
		down_pos=2*RE_RP_y-RE_MP_y;
		step=Math.abs(down_pos-up_pos)/100;
		float RE_MP_y_temp=up_pos+step*scale;
		RE_Path=new Path();
		RE_Path.moveTo(RE_LP_x, RE_LP_y);
		RE_Path.quadTo(RE_MP_x, RE_MP_y_temp, RE_RP_x, RE_RP_y); 
		mCanvas.drawPath(RE_Path, paint);
		RE_Path.close();
		
		//mouth
		down_pos=M_MP_y;
		up_pos=2*M_RP_y-M_MP_y;
		step=Math.abs(down_pos-up_pos)/100;
		float M_MP_y_temp=down_pos-step*scale;
		M_Path=new Path();
		M_Path.moveTo(M_LP_x, M_LP_y);
		M_Path.quadTo(M_MP_x, M_MP_y_temp, M_RP_x, M_RP_y); 
		mCanvas.drawPath(M_Path, paint);
		M_Path.close();

	}

	public void drawscale(int scale,int emotion_polar){
		mCanvas = mHolder.lockCanvas();    //获得画布对象,开始对画布画画

		mCanvas.drawColor(Color.WHITE);    //设置画布颜色为黑色
		canvasMethod_self_def(mCanvas,scale,emotion_polar);   

		mHolder.unlockCanvasAndPost(mCanvas);    //把画布显示在屏幕上

	}





}