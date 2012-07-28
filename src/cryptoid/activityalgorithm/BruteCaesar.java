package cryptoid.activityalgorithm;

import cryptoid.algorithms.Caesar;
import cryptoid.main.R;
import cryptoid.utils.Utils;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BruteCaesar extends Activity {
	
	//-------global vars -------
	EditText input;
	ProgressBar main_prog, loop_prog;
	ImageView picture_finish;
	Button button_start, button_save;
	TextView result, label_result;
	Activity me;
	BruteForceTask task;
	Boolean isRunning=false;
	//--------------------------
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brute_basic);
        me = this;
        input = (EditText)findViewById(R.id.brute_input_text);
        main_prog = (ProgressBar)findViewById(R.id.progress_bar_horizontal);
        loop_prog = (ProgressBar)findViewById(R.id.progress_bar_loop);
        picture_finish = (ImageView)findViewById(R.id.picture_finished);
        result = (TextView)findViewById(R.id.text_result);
        label_result= (TextView)findViewById(R.id.text_result_title);
        button_save = (Button)findViewById(R.id.button_save);
        
        
        button_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {//to save the report in a file
				String name_report = "BruteForce_Caesar_report-"+android.text.format.DateFormat.format("yyyy-MM-dd_hh_mm", new java.util.Date())+".txt";
		        Utils.saveFile(me,name_report,result.getText().toString());
			}
		});
        button_start = (Button)findViewById(R.id.button_start);
		button_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(input.getText().toString().equals(""))
					Toast.makeText(me, "Nothing to Brute Force", Toast.LENGTH_SHORT).show();
				else {
					if(!(isRunning)) {
						task = new BruteForceTask();//start the thread to brute force
						task.execute();
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
        		else
        			Toast.makeText(this, "Do not contain text", Toast.LENGTH_LONG).show();
        	else
        		Toast.makeText(this, "Is empty", Toast.LENGTH_LONG).show();
        else
        	Toast.makeText(this, "is null", Toast.LENGTH_LONG).show();
        //---------------------------------------------------
    }
    
    
    //----------------------------------------------------------
    //------------------- AsynTask (Thread) --------------------
    //----------------------------------------------------------
	class BruteForceTask extends AsyncTask<Void, String, Void> {
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
			String toBrute=input.getText().toString();
			for (int i=0;i<26 ;i++) {
				if(isCancelled())
					break;
				publishProgress(String.valueOf(i),Caesar.decipher(toBrute, i));
				SystemClock.sleep(500);//to do not overload the phone processor
			}
			
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
			result.append("Shift of "+item[0]+":\n\t"+item[1]+"\n\n");//on each progress update : print the given text
			main_prog.incrementProgressBy(4);//incremente the progressbar
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			Toast.makeText(me, "Done!", Toast.LENGTH_SHORT).show();//restablish widgets in theirs default state..
			button_save.setVisibility(android.view.View.VISIBLE);
			picture_finish.setVisibility(android.view.View.VISIBLE);
			isRunning=false;
			loop_prog.setVisibility(android.view.View.INVISIBLE);
			input.setEnabled(true);
			button_start.setText("Start");
		}
	}
    //----------------------------------------------------------
    //----------------------------------------------------------
    //----------------------------------------------------------
}
