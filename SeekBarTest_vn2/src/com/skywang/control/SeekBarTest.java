package com.skywang.control;





import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class SeekBarTest extends Activity implements SeekBar.OnSeekBarChangeListener{
	private static final String TAG = "SKYWANG";

	// 闕ｳ蟇ゑｿｽ諛�ｽｳ�ｽｻ謇域ｻ�ｽｻ蛟ｩ�ｽｮ�ｽ､SeekBar遯ｶ譎擾ｽｯ�ｽｹ陟守坩蝎ｪTextView
	private TextView mTvDef;
	// 闕ｳ蟇ゑｿｽ諛��ｿｽ�ｽｪ陞ｳ螢ｻ�ｽｹ蜚�eekBar遯ｶ譎擾ｽｯ�ｽｹ陟守坩蝎ｪTextView
	private TextView mTvSelf;
	// 遯ｶ諛�ｽｳ�ｽｻ謇域ｻ�ｽｻ蛟ｩ�ｽｮ�ｽ､SeekBar遯ｶ�ｿｽ
	private SeekBar mSeekBarDef;
	// 遯ｶ諛��ｿｽ�ｽｪ陞ｳ螢ｻ�ｽｹ蜚�eekBar遯ｶ�ｿｽ
	private SeekBar mSeekBarSelf;
	float screenWidth  ;
	float screenHeight  ; //获得屏幕像素
	MySurfaceView sfv;
	MySurfaceView_Smile sfv_smile;
	SurfaceHolder sfh;
	Button item_next;
	Button item_pre;
	TextView item_name;
	public String Data_Save_Path=null;
	BufferedWriter result = null;
	int time;
	Boolean flag=false;
	String[] content={"紧张的","轻松愉快的"};
	int[] emotion_polar={-1,1};
	int content_len=content.length;
	Map<String,Integer> scale_result=new HashMap<String,Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_bar_test);
		Intent intent=this.getIntent();
		Data_Save_Path=intent.getStringExtra("Data_Path");
		time=0;
		// 闕ｳ蟇ゑｿｽ諛�ｽｳ�ｽｻ謇域ｻ�ｽｻ蛟ｩ�ｽｮ�ｽ､SeekBar遯ｶ譎擾ｽｯ�ｽｹ陟守坩蝎ｪTextView
		mTvDef = (TextView) findViewById(R.id.tv_def);
		// 遯ｶ諛�ｽｳ�ｽｻ謇域ｻ�ｽｻ蛟ｩ�ｽｮ�ｽ､SeekBar遯ｶ�ｿｽ
		mSeekBarDef = (SeekBar) findViewById(R.id.seekbar_def);
		//mSeekBarDef.setMax(200); 
		mSeekBarDef.setOnSeekBarChangeListener(this);
		sfv=(MySurfaceView) findViewById(R.id.view3d);
		sfv_smile=(MySurfaceView_Smile) findViewById(R.id.view3d_smile);
		
		WindowManager wm = this.getWindowManager();
		 
		     int width = wm.getDefaultDisplay().getWidth();
		     int height = wm.getDefaultDisplay().getHeight();
		//setView(sfv_smile);
		
		
		item_next=(Button) findViewById(R.id.btn_searchDev);
		item_pre=(Button) findViewById(R.id.btn_pre);
		item_name=(TextView) findViewById(R.id.item_name);
		item_name.setText(content[0]);
		item_next.setOnClickListener(new SaveButtonListener()); 
		item_pre.setOnClickListener(new SaveButtonListener()); 
		// 闕ｳ蟇ゑｿｽ諛��ｿｽ�ｽｪ陞ｳ螢ｻ�ｽｹ蜚�eekBar遯ｶ譎擾ｽｯ�ｽｹ陟守坩蝎ｪTextView
		//mTvSelf = (TextView) findViewById(R.id.tv_self);
		// 遯ｶ諛��ｿｽ�ｽｪ陞ｳ螢ｻ�ｽｹ蜚�eekBar遯ｶ�ｿｽ
		//mSeekBarSelf = (SeekBar) findViewById(R.id.seekbar_self);
		//mSeekBarSelf.setOnSeekBarChangeListener(this);
		//setContentView(new MySurfaceView(this));
		//set the width and height of textview
		 
		RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) item_name.getLayoutParams(); //取控件textView当前的布局参数  
		linearParams.height = (int) (height/5);// 控件的高强制设成20  
		  
		linearParams.width = (int) (width/2);// 控件的宽强制设成30   
		  
		item_name.setLayoutParams(linearParams); //使设置好的布局参数应用到控件 



	}	
	
	 void setView(MySurfaceView_Smile view) {

        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) view.getLayoutParams(); 
        int textview_height=item_name.getHeight();
		int textview_width=item_name.getWidth();
		linearParams.width = textview_width ;
		linearParams.height = textview_height ;
        view.setLayoutParams(linearParams);
    }



	class SaveButtonListener implements android.view.View.OnClickListener 

	{ 
		@Override 
		public  void onClick(View v) 
		{ 
			if((flag==true)&&(v.getId()==R.id.btn_searchDev)){
		
		
				
					time++;
					if(time>content_len-1){
						//Toast.makeText(getApplicationContext(), "自评量表完成，请退出应用程序", Toast.LENGTH_SHORT).show();

					}else{
						String key=(String) item_name.getText();
						int value=mSeekBarDef.getProgress();

						scale_result.put(key, value);
						item_name.setText(content[time]);
						mSeekBarDef.setProgress(0);
						
						


					}

					if(time==content_len){
						time--;
						String key=(String) item_name.getText();
						int value=mSeekBarDef.getProgress();

						scale_result.put(key, value);
						save_data();
					}
					flag=false;

			}else if(v.getId()==R.id.btn_pre){
				time--;
				if(time<0){
					time=0;
					Toast.makeText(getApplicationContext(), "操作不允许，请点击下一项按钮", Toast.LENGTH_SHORT).show();

				}else{
					item_name.setText(content[time]);
					mSeekBarDef.setProgress(0);
				}
				
			}else{
				Toast.makeText(getApplicationContext(), "操作不允许，请滑动滑块完成量表", Toast.LENGTH_SHORT).show();

			}
			


		}        

	}
	/*
	 * SeekBar陋帶㊧�ｽｭ�ｽ｢雋雁｣ｼ蜍倬�ｧ�ｿｽ陜玲ｫ�ｽｰ�ｿｽ陷�ｽｽ隰ｨ�ｽｰ
	 */
	private void save_data(){//save item name and slide value
		new AlertDialog.Builder(SeekBarTest.this).setTitle("系统提示")//设置对话框标题  

		.setMessage("点击确定保存数据后自动退出应用程序！")//设置显示的内容  

		.setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮  



			@Override  

			public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  

				// TODO Auto-generated method stub 
				try {
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){	
						File sdCardDir = Environment.getExternalStorageDirectory();
						
						result = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sdCardDir.getCanonicalPath() +Data_Save_Path+'/'+"scale_data.txt", true)));//ﾗｷｼﾓﾐｴ
						
							
							for (Map.Entry<String, Integer> entry : scale_result.entrySet()) {  
								  
							   // System.out.println("分量名 ： " + entry.getKey() + ", 数值： " + entry.getValue());  
							    result.write("分量名：" + entry.getKey() + ",数值：" + entry.getValue()+ "\n");
							  
							}  
							
						
						
						//Log.e("saving data?", "yes");
						result.close();

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				


				finish();  

			}  

		}).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮  



			@Override  

			public void onClick(DialogInterface dialog, int which) {//响应事件  

				// TODO Auto-generated method stub  

				Log.i("alertdialog"," 请保存数据！");  

			}  

		}).show();//在按键响应事件中显示此对话框  



	}  


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	/*
	 * SeekBar陟托ｿｽ陝句玄�ｽｻ螢ｼ蜍倬�ｧ�ｿｽ陜玲ｫ�ｽｰ�ｿｽ陷�ｽｽ隰ｨ�ｽｰ
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	/*
	 * SeekBar雋雁｣ｼ蜍倩ｭ鯉ｽｶ騾ｧ�ｿｽ陜玲ｫ�ｽｰ�ｿｽ陷�ｽｽ隰ｨ�ｽｰ
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		Log.d(TAG, "seekid:"+seekBar.getId()+", progess"+progress);
		switch(seekBar.getId()) {
		case R.id.seekbar_def:{
			// 髫ｶ�ｽｾ驗ゑｽｮ遯ｶ諛会ｽｸ螳茨ｽｳ�ｽｻ謇域ｻ�ｽｻ蛟ｩ�ｽｮ�ｽ､SeekBar陝�ｽｹ陟守坩蝎ｪTextView遯ｶ譎牙飭陋滂ｽｼ
			//mTvDef.setText(getResources().getString(R.string.text_def)+" : "+String.valueOf(seekBar.getProgress()));
			mTvDef.setText(getResources().getString(R.string.text_def)+" : ");
			sfv.drawscale(seekBar.getProgress(),emotion_polar[time]);
			sfv_smile.drawscale(seekBar.getProgress(),emotion_polar[time]);
			
			flag=true;
			break;
		}

		default:
			break;
		}
	}
}
