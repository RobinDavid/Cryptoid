package cryptoid.activityalgorithm;

import java.util.ArrayList;

import cryptoid.activityalgorithm.AnalyzeCaesar.AnalyseTask;
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

public class AnalyzeAscii extends Activity {
	//----- sliding drawer vars
	Button button_start, button_save;
	ProgressBar main_prog, loop_prog;
	ImageView picture_finish;
	TextView result_text, label_result;
	EditText input_text;
	Dialog dialog;
	String mess;
	Spinner spinner;
	Activity me;
	AsciiAnalyzeTask task;
	Boolean isRunning=false;
	Integer[] items= {1,2,3,4,5};
	//-------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze_basic_deep);
        me=this;
        result_text = (TextView) findViewById(R.id.text_result);
        input_text = (EditText) findViewById(R.id.analyze_input_text);
        button_start = (Button) findViewById(R.id.button_start);
        main_prog = (ProgressBar)findViewById(R.id.progress_bar_horizontal);
        label_result = (TextView)findViewById(R.id.text_result_title);
        loop_prog = (ProgressBar)findViewById(R.id.progress_bar_loop);
        picture_finish = (ImageView)findViewById(R.id.picture_finished);
        button_save=(Button)findViewById(R.id.button_save);
        button_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name_report = "Analyse_Ascii_report-"+android.text.format.DateFormat.format("yyyy-MM-dd_hh_mm", new java.util.Date())+".txt";
		        Utils.saveFile(me,name_report,result_text.getText().toString());
			}
		});
        button_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(input_text.getText().toString().equals(""))
					Toast.makeText(me, "Nothing to Analyse", Toast.LENGTH_SHORT).show();
				else {
					if(!(isRunning)) {
						task = new AsciiAnalyzeTask();
						task.execute();
					}
					else {
						task.cancel(true);
					}
				}
			}
		});
        
        //-------- setup the deep spinner ---------
        spinner = (Spinner)findViewById(R.id.cryptanalysis_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //-----------------------------------------
        
		//------ Retrieve the text from parent intent ------
        Bundle extra = getIntent().getExtras();
        if (extra != null)
        	if (!(extra.isEmpty()))
        		if (extra.containsKey("text"))
        			input_text.setText(extra.getString("text"));
        //---------------------------------------------------
    }
    
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			input_text.setText(msg.getData().getString("mess"));//recieve imported messages
		};
	};
	
	
	class AsciiAnalyzeTask extends AsyncTask<Void, String, Void> {
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
			button_save.setVisibility(android.view.View.INVISIBLE);
			result_text.setText("");
			isRunning=true;
		}
		
		@Override
		protected Void doInBackground(Void... unused) {
			String toAnalyze=input_text.getText().toString();
			int deep=Integer.parseInt(spinner.getSelectedItem().toString());
			analyze(toAnalyze,0,deep);
			
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
			button_save.setVisibility(android.view.View.VISIBLE);
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
		private void analyze(String raw,int deep, int maxdeep) {
			String pre="";
			String res="";
			
			for (int e=0;e < deep;e++)
					pre+=" ";
			float accuracy = 0;
			publishProgress(pre+" Level "+deep+": "+raw);
			
			if(deep == maxdeep) {
				return;//we leave the method if the deepness is 0
			}
			deep++;

			//Determine the Hexadecimal accuracy
			accuracy = Ascii.isHexString(raw);
			if(accuracy >= 80) {
				publishProgress(pre+"Hexadecimal average \t\t"+accuracy+"%");
				res = Ascii.approximateHexToString(raw);
				analyze(res, deep, maxdeep);
			}
			else
				publishProgress(pre+"Hexadecimal average \t\t"+accuracy+"%\n"+pre+"#stop");
			
			//Determine the Binary accuracy
			accuracy = Ascii.isBinaryString(raw,true);
			if(accuracy >= 80) {
				publishProgress(pre+"Binary average \t\t"+accuracy+"%");
				res = Ascii.approximateBinaryTo(raw, true, false);// may add a comment that we convert in dec and string
				analyze(res, deep,maxdeep);
				res = Ascii.approximateBinaryTo(raw, false, true);
				analyze(res, deep,maxdeep);			
			}
			else
				publishProgress(pre+"Binary average \t\t"+accuracy+"%\n"+pre+"#stop");
			
			
			//Determine the Decimal accuracy
			accuracy = Ascii.isDecString(raw);
			if(accuracy >= 60) {
				publishProgress(pre+"Decimal average \t\t"+accuracy+"%\n"+pre+"It got good chance to be Polybius");
				res = Ascii.approximateDecToString(raw);
				analyze(res,deep,maxdeep);
			}
			else
				publishProgress(pre+"Decimal average \t\t"+accuracy+"%\n"+pre+"#stop");
						
			
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
