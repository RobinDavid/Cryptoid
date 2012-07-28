package cryptoid.cryptanalysis;

import java.util.ArrayList;
import cryptoid.algorithms.Ascii;
import cryptoid.algorithms.Base64Coding;
import cryptoid.algorithms.Caesar;
import cryptoid.algorithms.Morse;
import cryptoid.algorithms.Polybius;
import cryptoid.main.R;
import cryptoid.utils.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class Cryptanalysis extends Activity implements OnClickListener{
	//----- sliding drawer vars
	Button slideHandleButton, button_start, button_copy, button_import, button_save, button_about;
	SlidingDrawer slidingDrawer;
	ProgressBar main_prog, loop_prog;
	ImageView picture_finish;
	TextView result_text, label_result;
	EditText input_text;
	Dialog dialog;
	String mess;
	Spinner spinner;
	Activity me;
	CryptanalysisTask task;
	Boolean isRunning=false;
	Integer[] items= {1,2,3,4,5};
	//-------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cryptanalysis);
        me=this;
        result_text = (TextView) findViewById(R.id.text_result);
        input_text = (EditText) findViewById(R.id.analyze_input_text);
        button_start = (Button) findViewById(R.id.button_start);
        main_prog = (ProgressBar)findViewById(R.id.progress_bar_horizontal);
        label_result = (TextView)findViewById(R.id.text_result_title);
        loop_prog = (ProgressBar)findViewById(R.id.progress_bar_loop);
        picture_finish = (ImageView)findViewById(R.id.picture_finished);
        
        //------------ Setup Sliding drawer widgets -------------
        button_copy = (Button) findViewById(R.id.cryptanalysis_button_copy);
        button_import = (Button) findViewById(R.id.cryptanalysis_button_import);
        button_save = (Button) findViewById(R.id.cryptanalysis_button_save);
        button_about = (Button)findViewById(R.id.cryptanalysis_button_about);
        button_start.setOnClickListener(this);
        button_copy.setOnClickListener(this);
        button_import.setOnClickListener(this);
        button_save.setOnClickListener(this);
        button_about.setOnClickListener(this);
        //------------------------------------------------
        
        //-------------Setup spinner ------
        spinner = (Spinner)findViewById(R.id.cryptanalysis_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //----------------------------
        
        //------------- sliding drawer stuff ----------
		slideHandleButton = (Button) findViewById(R.id.slideHandleButton);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {slideHandleButton.setBackgroundResource(R.drawable.openarrow);}});
		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {slideHandleButton.setBackgroundResource(R.drawable.closearrow);}});
        //---------------------------------------------
    }
    
    
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		
		case R.id.button_start:
			if(input_text.getText().toString().equals(""))
				Toast.makeText(me, "Nothing to Cryptanalyze", Toast.LENGTH_SHORT).show();
			else {
				if(!(isRunning)) {
					task = new CryptanalysisTask(); //launch the cryptanalysis task
					task.execute();
				}
				else {
					task.cancel(true);
				}
			}
			break;
			
		case R.id.cryptanalysis_button_copy:
			Utils.copyToClipboardSimple(this, result_text.getText().toString(),"Report copied to clipboard");
			break;
			
		case R.id.cryptanalysis_button_import:
			Uri SMS_INBOX = Uri.parse("content://sms/inbox");
			final Cursor c;

			c = getContentResolver().query(SMS_INBOX, null ,null, null, "date DESC LIMIT 5");
			startManagingCursor(c);

			mess = Utils.importMessage(this, c, handler);
	        stopManagingCursor(c);
			break;
			
		case R.id.cryptanalysis_button_save:
			String name_report = "Cryptoid_report-"+android.text.format.DateFormat.format("yyyy-MM-dd_hh_mm", new java.util.Date())+".txt";
	        Utils.saveFile(this,name_report,result_text.getText().toString());
			break;
			
		case R.id.cryptanalysis_button_about:
			int text = R.string.cryptanalysis_about_text;
			int title = R.string.cryptanalysis_about_title;
			Utils.showAbout(this,title,text);
			break;
			
		default:
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			input_text.setText(msg.getData().getString("mess"));//recieve imported message
		};
	};
	
	
	class CryptanalysisTask extends AsyncTask<Void, String, Void> {
		@Override
		protected void onPreExecute() {
			input_text.setEnabled(false);
			spinner.setEnabled(false);
			button_start.setText("Cancel");
			main_prog.setVisibility(android.view.View.VISIBLE);
			main_prog.setProgress(0);
			loop_prog.setVisibility(android.view.View.VISIBLE);
			label_result.setVisibility(android.view.View.VISIBLE);
			
			//for the case we relaunch a brute
			picture_finish.setVisibility(android.view.View.INVISIBLE);
			result_text.setText("");
			isRunning=true;
		}
		
		@Override
		protected Void doInBackground(Void... unused) {
			String toAnalyze=input_text.getText().toString();
			int deep=Integer.parseInt(spinner.getSelectedItem().toString());

			cryptanalyse(toAnalyze,0,deep);
			
			return(null);
		}
		
		@Override
		protected void onCancelled() {
			main_prog.setProgress(100);
			Toast.makeText(me, "Cancelled !", Toast.LENGTH_SHORT).show();
			button_start.setText("Start");
			isRunning=false;
			loop_prog.setVisibility(android.view.View.INVISIBLE);
			input_text.setEnabled(true);
			spinner.setEnabled(true);
			button_start.setText("Start");
		}
		
		@Override
		protected void onProgressUpdate(String... item) {
			result_text.append(item[0]+"\n");
			main_prog.incrementProgressBy(2);
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			main_prog.setProgress(100);
			Toast.makeText(me, "Done!", Toast.LENGTH_SHORT).show();
			picture_finish.setVisibility(android.view.View.VISIBLE);
			isRunning=false;
			loop_prog.setVisibility(android.view.View.INVISIBLE);
			input_text.setEnabled(true);
			spinner.setEnabled(true);
			button_start.setText("Start");
		}
		
		//---------------------------------------------------------------------------------------
		//------------------------------------- Cryptanalysis -----------------------------------
		//---------------------------------------------------------------------------------------
		private void cryptanalyse(String raw,int deep, int maxdeep) {
			String pre="";
			String res="";
			
			for (int e=0;e < deep;e++)
					pre+=" ";
			float accuracy = 0;
			publishProgress(pre+" Level "+deep+"/"+maxdeep+": "+raw);
			
			if(deep == maxdeep) {
				return;//we leave the method if the deepness is 0
			}
			deep++;

			//Hexadecimal
			accuracy = Ascii.isHexString(raw);
			if(accuracy >= 80) {
				publishProgress(pre+"Hexadecimal average \t\t"+accuracy+"%");
				res = Ascii.approximateHexToString(raw);
				cryptanalyse(res, deep, maxdeep);
			}
			else
				publishProgress(pre+"Hexadecimal average \t\t"+accuracy+"%\n"+pre+"#stop");
			
			//Binary
			accuracy = Ascii.isBinaryString(raw,true);
			if(accuracy >= 80) {
				publishProgress(pre+"Binary average \t\t"+accuracy+"%");
				res = Ascii.approximateBinaryTo(raw, true, false);// may add a comment that we convert in dec and string
				cryptanalyse(res, deep,maxdeep);
				res = Ascii.approximateBinaryTo(raw, false, true);
				cryptanalyse(res, deep,maxdeep);			
			}
			else
				publishProgress(pre+"Binary average \t\t"+accuracy+"%\n"+pre+"#stop");
			
			//decimal
			accuracy = Ascii.isDecString(raw);
			if(accuracy >= 60) {
				publishProgress(pre+"Decimal average \t\t"+accuracy+"%");
				res = Ascii.approximateDecToString(raw);
				cryptanalyse(res,deep,maxdeep);
				res = Polybius.decipher(raw);//we try the Polybius cipher because the decimal average is good
				cryptanalyse(res,deep,maxdeep);
			}
			else
				publishProgress(pre+"Decimal average \t\t"+accuracy+"%\n"+pre+"#stop");
			
			//Base64
			accuracy = Base64Coding.isBase64String(raw);
			if(accuracy == 100) {
				publishProgress(pre+"Base64 average \t\t"+accuracy+"%");
				try {
				res = Base64Coding.decodeBase64(raw);
				cryptanalyse(res,deep,maxdeep);
				}
				catch(IllegalArgumentException e) {
					publishProgress(pre+"#stop (Bad Base64)");
				}
				catch (Exception e) {
					publishProgress(pre+"#stop (Unknown)");
				}
			}
			else
				publishProgress(pre+"Base64 average \t\t"+accuracy+"%\n"+pre+"#stop");
			
			//Morse
			accuracy = Morse.isMorseString(raw);
			if(accuracy == 100) {
				publishProgress(pre+"Morse average \t\t"+accuracy+"%");
				res = Morse.decipher(raw);
				cryptanalyse(res,deep,maxdeep);
			}
			else
				publishProgress(pre+"Morse average \t\t"+accuracy+"%\n"+pre+"#stop");
			
			//Others algorithms
			publishProgress(pre+"Analyse based on short words :");
			ArrayList<String> list =Caesar.analyzeBasedOnShortWord(raw,pre);
			if(list.isEmpty())
				publishProgress(pre+"#Stop (no single character)");
			else {
				for(int i = 0; i < list.size(); i++) {
					publishProgress(list.get(i));
				}
			}
			publishProgress(pre+"Analyse based on instances :");
			list = Caesar.analyzeBasedOnOccur(raw,pre);
			for(int i = 0; i < list.size(); i++) {
				publishProgress(list.get(i));
			}
			
			
		}
		//-------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------
		
		
		
	}
}
