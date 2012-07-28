package cryptoid.activityalgorithm;

import cryptoid.algorithms.Caesar;
import cryptoid.main.R;
import cryptoid.utils.Utils;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;

public class MainCaesar extends ActivityGroup implements OnClickListener, TabContentFactory, RadioGroup.OnCheckedChangeListener {
	Button slideHandleButton, copy_button, import_button, send_button, addcipher_button, addplain_button, analyze_button, back_button, brute_button, about_button ;
	EditText plain_edit_text, cipher_edit_text;
	TabHost tab;
	SlidingDrawer slidingDrawer;
	RelativeLayout layout_main;
	TextView main_title;
	RadioButton radio_cipher, radio_decipher;
	RadioGroup radio_group;
	Spinner spinner;
	Integer[] items= {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
	String mess ="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        tab = (TabHost)findViewById(R.id.tabhost);
        tab.setup(this.getLocalActivityManager());
        
        //try to create the first tab
        TabHost.TabSpec spec=tab.newTabSpec("Main");
        spec.setContent(this);
        spec.setIndicator("Main",getResources().getDrawable(R.drawable.sun));
        tab.addTab(spec);
        //-------------------
        
        layout_main = (RelativeLayout)findViewById(R.id.RelativeLayoutMain);
        tab.getTabWidget().setVisibility(android.view.View.GONE);
        plain_edit_text=(EditText)findViewById(R.id.main_spinner_plain_text);
        cipher_edit_text=(EditText)findViewById(R.id.main_spinner_cipher_text);
        main_title = (TextView)findViewById(R.id.main_spinner_title);
        main_title.setText("Caesar");
        radio_group= (RadioGroup)findViewById(R.id.main_spinner_choice);
        radio_group.setOnCheckedChangeListener(this);
        radio_cipher = (RadioButton)findViewById(R.id.main_spinner_radio_cipher);
        radio_decipher = (RadioButton)findViewById(R.id.main_spinner_radio_decipher);
        
        //--------------------- Setup the spinner ------------------------
        spinner = (Spinner)findViewById(R.id.main_spinner_spinnerbutton);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        //------------------------------------------------------------------
        
        //---------------- Setup sliding drawer buttons ----------------
        copy_button = (Button)findViewById(R.id.algos_button_copy);
        import_button = (Button)findViewById(R.id.algos_button_import);
        send_button = (Button)findViewById(R.id.algos_button_send);
        addcipher_button = (Button)findViewById(R.id.algos_button_addcipher);
        addplain_button = (Button)findViewById(R.id.algos_button_addplain);
        analyze_button = (Button)findViewById(R.id.algos_button_analyze);
        back_button = (Button)findViewById(R.id.algos_button_back);
        brute_button = (Button)findViewById(R.id.algos_button_brute);
        about_button = (Button)findViewById(R.id.algos_button_about);
        if(Caesar.analyseAvailable())
        	analyze_button.setEnabled(true);
        if(Caesar.bruteAvailable())
        	brute_button.setEnabled(true);      
        copy_button.setOnClickListener(this);
        import_button.setOnClickListener(this);
        send_button.setOnClickListener(this);
        addcipher_button.setOnClickListener(this);
        addplain_button.setOnClickListener(this);
        analyze_button.setOnClickListener(this);
        back_button.setOnClickListener(this);
        brute_button.setOnClickListener(this);
        about_button.setOnClickListener(this);
        //---------------------------------------------------
        
        //----- Retrieve informations from mother class -----
        Bundle extra = getIntent().getExtras();
        if (extra != null)
        	if (!(extra.isEmpty()))
        		if (extra.containsKey("type") && extra.containsKey("value"))
        			if (extra.getString("type").equals("ciphered"))
        				plain_edit_text.setText(extra.getString("value"));
        			else if (extra.getString("type").equals("plain"))
        				cipher_edit_text.setText(extra.getString("value"));
        //---------------------------------------------------
        
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
			case R.id.algos_button_analyze:
				//launch the intent in the new tab !
				tab.getTabWidget().setVisibility(android.view.View.VISIBLE);
				layout_main.setPadding(0, 62, 0, 0);
				try {
				tab.addTab(
						tab.newTabSpec("ana")
						.setIndicator("Analyze",getResources().getDrawable(R.drawable.analyze))
						.setContent(new Intent(this , Class.forName("cryptoid.activityalgorithm.AnalyzeCaesar")).putExtra("text", cipher_edit_text.getText().toString())));
				}
				catch (ClassNotFoundException e) {
				      Toast.makeText(this, "Class not found: "+e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (Exception e) {
					Toast.makeText(this, "Exception: "+e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
				break;
				
			case R.id.algos_button_brute:
				//launch the intent in a new tab!
				tab.getTabWidget().setVisibility(android.view.View.VISIBLE);
				layout_main.setPadding(0, 62, 0, 0);
				try {
				tab.addTab(
						tab.newTabSpec("bru")
						.setIndicator("Brute",getResources().getDrawable(R.drawable.brute))
						.setContent(new Intent(this , Class.forName("cryptoid.activityalgorithm.BruteCaesar")).putExtra("text", cipher_edit_text.getText().toString())));
				}
				catch (ClassNotFoundException e) {
				      Toast.makeText(this, "Class not found: "+e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (Exception e) {
					Toast.makeText(this, "Exception: "+e.toString(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				break;
				
			case R.id.algos_button_send:
			     Utils.chooseTextToSendAndSend(this, plain_edit_text.getText().toString(), cipher_edit_text.getText().toString());
			     break;
			     
			case R.id.algos_button_addcipher:
				//this function allow to add another cipher to the ciphered text, to increase 'protection'
				Utils.addCipher(this, cipher_edit_text.getText().toString());
				break;
				
			case R.id.algos_button_addplain:
				//this function allow to try to uncipher the plain text with another cipher, (in the case of multi layer ciphering)
				Utils.addPlain(this, plain_edit_text.getText().toString());
				break;
				
			case R.id.algos_button_back:
				Utils.finishMe(this);
				break;
				
			case R.id.algos_button_copy:
				Utils.copyToClipboardComplex(this, plain_edit_text.getText().toString(),cipher_edit_text.getText().toString());
				break;
				
			case R.id.algos_button_import:
				Uri SMS_INBOX = Uri.parse("content://sms/inbox");
				final Cursor c;

				c = getContentResolver().query(SMS_INBOX, null ,null, null, "date DESC LIMIT 5");
				startManagingCursor(c);

				mess = Utils.importMessage(this, c, handler);
		        stopManagingCursor(c);
		        break;
		        
			case R.id.algos_button_about:
				int text = R.string.maincaesar_about_text;
				int title = R.string.maincaesar_about_title;
				Utils.showAbout(this,title,text);
				break;
			}
	}

	@Override
	public View createTabContent(String tag) {
		return View.inflate(this, R.layout.main_spinner, null);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		if(radio_cipher.isChecked()) {//Recieve the changement of radiobuttonstate
			cipher_edit_text.setText(Caesar.cipher(plain_edit_text.getText().toString(), Integer.parseInt(spinner.getSelectedItem().toString())));
		}	
		else if(radio_decipher.isChecked()) {
			plain_edit_text.setText(Caesar.decipher(cipher_edit_text.getText().toString(), Integer.parseInt(spinner.getSelectedItem().toString())));
		}
	}
	
	//adapter for the 
	public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
		//Recieve the changement of position of the spinner
	    public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
	      	if(radio_cipher.isChecked()) {
				cipher_edit_text.setText(Caesar.cipher(plain_edit_text.getText().toString(), Integer.parseInt(parent.getItemAtPosition(pos).toString())));
			}	
			else if(radio_decipher.isChecked()) {
				plain_edit_text.setText(Caesar.decipher(cipher_edit_text.getText().toString(), Integer.parseInt(spinner.getSelectedItem().toString())));
			}
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cipher_edit_text.setText(msg.getData().getString("mess"));
		};
	};
}
