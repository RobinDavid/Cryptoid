package cryptoid.activityalgorithm;

import java.util.ArrayList;

import cryptoid.algorithms.Caesar;
import cryptoid.main.R;
import cryptoid.utils.Utils;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AnalyzeCaesar extends Activity {
	//-------global vars -------
	EditText input;
	ProgressBar main_prog, loop_prog;
	ImageView picture_finish;
	Button button_start, button_save;
	TextView result, label_result;
	Activity me;
	AnalyseTask task;
	Boolean isRunning=false;
	//--------------------------
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze_basic);
        me = this;
        input = (EditText)findViewById(R.id.analyze_input_text);
        main_prog = (ProgressBar)findViewById(R.id.progress_bar_horizontal);
        loop_prog = (ProgressBar)findViewById(R.id.progress_bar_loop);
        picture_finish = (ImageView)findViewById(R.id.picture_finished);
        result = (TextView)findViewById(R.id.text_result);
        label_result= (TextView)findViewById(R.id.text_result_title);
        button_save = (Button)findViewById(R.id.button_save);
        button_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name_report = "Analyse_Caesar_report-"+android.text.format.DateFormat.format("yyyy-MM-dd_hh_mm", new java.util.Date())+".txt";
		        Utils.saveFile(me,name_report,result.getText().toString());
			}
		});
        button_start = (Button)findViewById(R.id.button_start);
		button_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(input.getText().toString().equals(""))
					Toast.makeText(me, "Nothing to Analyse", Toast.LENGTH_SHORT).show();
				else {
					if(!(isRunning)) {
						task = new AnalyseTask();//create the thread
						task.execute();//execute it
					}
					else {
						task.cancel(true);
					}
				}
			}
		});
		
		//------ Retrieve the text from parent intent ------
        Bundle extra = getIntent().getExtras();
        if (extra != null)
        	if (!(extra.isEmpty()))
        		if (extra.containsKey("text"))
        			input.setText(extra.getString("text"));
        //---------------------------------------------------
    }
    
    
	class AnalyseTask extends AsyncTask<Void, String, Void> {
		@Override
		protected void onPreExecute() {
			input.setEnabled(false);
			button_start.setText("Cancel");
			main_prog.setVisibility(android.view.View.VISIBLE);
			main_prog.setProgress(0);
			loop_prog.setVisibility(android.view.View.VISIBLE);
			label_result.setVisibility(android.view.View.VISIBLE);
			
			//for the case we relaunch a brute
			picture_finish.setVisibility(android.view.View.INVISIBLE);
			button_save.setVisibility(android.view.View.INVISIBLE);
			result.setText("");
			isRunning=true;
		}
		
		@Override
		protected Void doInBackground(Void... unused) {
			String toAnalyse=input.getText().toString();
			analyse(toAnalyse);
			return(null);
		}
		
		@Override
		protected void onCancelled() {
			main_prog.setProgress(100);
			Toast.makeText(me, "Cancelled !", Toast.LENGTH_SHORT).show();
			button_start.setText("Start");
			isRunning=false;
			loop_prog.setVisibility(android.view.View.INVISIBLE);
			input.setEnabled(true);
			button_start.setText("Start");
		}
		
		@Override
		protected void onProgressUpdate(String... item) {
			result.append(item[0]+":\n");
			main_prog.incrementProgressBy(20);
			SystemClock.sleep(2000);
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			Toast.makeText(me, "Done!", Toast.LENGTH_SHORT).show();
			button_save.setVisibility(android.view.View.VISIBLE);
			picture_finish.setVisibility(android.view.View.VISIBLE);
			isRunning=false;
			main_prog.setProgress(100);
			loop_prog.setVisibility(android.view.View.INVISIBLE);
			input.setEnabled(true);
			button_start.setText("Start");
		}
		
		private void analyse(String raw) {
			publishProgress("Analyse based on short words :");
			ArrayList<String> list =Caesar.analyzeBasedOnShortWord(raw,"");
			if(list.isEmpty())
				publishProgress("\t#Stop (no single character)");
			else {
				for(int i = 0; i < list.size(); i++) {
					publishProgress(list.get(i));
				}
			}
			publishProgress("\nAnalyse based on instances :");
			list = Caesar.analyzeBasedOnOccur(raw,"");
			for(int i = 0; i < list.size(); i++) {
				publishProgress(list.get(i));
			}
		}
	}
}
