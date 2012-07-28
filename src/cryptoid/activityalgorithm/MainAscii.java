package cryptoid.activityalgorithm;

import cryptoid.algorithms.Ascii;
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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;

public class MainAscii extends ActivityGroup implements OnClickListener, TabContentFactory {
	
	Button start_button, copy_button, import_button, send_button, addcipher_button, addplain_button, analyze_button, back_button, brute_button, about_button ;
	EditText plain_edit_text, hex_edit_text, dec_edit_text, bin_edit_text;
	TabHost tab;
	RelativeLayout layout_main;
	TextView main_title;
	Spinner spinner;
	String[] items= {"Plain text -> *","Hexadecimal text -> *","Decimal text -> *","Binary text -> *"};
	Activity me = this;
	String mess ="";
	int choice=0;
	
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
        plain_edit_text=(EditText)findViewById(R.id.main_spinner_only_plain_text);
        hex_edit_text=(EditText)findViewById(R.id.main_spinner_only_hex_text);
        dec_edit_text=(EditText)findViewById(R.id.main_spinner_only_dec_text);
        bin_edit_text=(EditText)findViewById(R.id.main_spinner_only_bin_text);
        main_title = (TextView)findViewById(R.id.main_spinner_only_title);
        //-------------------setup a spinner
        main_title.setText("Ascii");
        spinner = (Spinner)findViewById(R.id.main_spinner_only_spinnerbutton);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        //-------------------------
        start_button= (Button)findViewById(R.id.main_spinner_only_button_start);
        start_button.setOnClickListener(this);
 
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
        if(Ascii.analyseAvailable())
        	analyze_button.setEnabled(true);
        if(Ascii.bruteAvailable())
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
        				plain_edit_text.setText(extra.getString("value"));
        //---------------------------------------------------
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
						.setContent(new Intent(this , Class.forName("cryptoid.activityalgorithm.AnalyzeAscii")).putExtra("text", hex_edit_text.getText().toString()+"\n"+dec_edit_text.getText().toString()+"\n"+bin_edit_text.getText().toString())));
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
				//brute force not available
			     break;
			     
			case R.id.algos_button_addcipher:
				chooseWhichOne(3);
				break;
				
			case R.id.algos_button_addplain:
				//this function allow to try to uncipher the plain text with another cipher, (in the case of multi layer ciphering)
				Utils.addPlain(this, plain_edit_text.getText().toString());

				break;
				
			case R.id.algos_button_back:
				Utils.finishMe(this);
				break;
				
			case R.id.algos_button_copy:
				chooseWhichOne(1);
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
				int text = R.string.mainascii_about_text;
				int title = R.string.mainascii_about_title;
				Utils.showAbout(this,title,text);
				break;
				
			case R.id.algos_button_send:
			     chooseWhichOne(2);
			     break;
			     
			case R.id.main_spinner_only_button_start:
				switch(spinner.getSelectedItemPosition()) {
		    	case 0:
		    		//Plain to *
		    		if(plain_edit_text.getText().toString().equals(""))
		    			Toast.makeText(me, "Nothing to do code", Toast.LENGTH_SHORT).show();
		    		else {
		    			hex_edit_text.setText(Ascii.stringToHex(plain_edit_text.getText().toString()));
		    			dec_edit_text.setText(Ascii.stringToDec(plain_edit_text.getText().toString()));
		    			bin_edit_text.setText(Ascii.stringToBinary(plain_edit_text.getText().toString()));
		    		}
		    		break;
		    	case 1:
		    		//hex to *
		    		if(hex_edit_text.getText().toString().equals(""))
		    			Toast.makeText(me, "Nothing to do code", Toast.LENGTH_SHORT).show();
		    		else {
		    			plain_edit_text.setText(Ascii.hexToString(hex_edit_text.getText().toString()));
		    			dec_edit_text.setText(Ascii.hexToDec(hex_edit_text.getText().toString()));
		    			bin_edit_text.setText(Ascii.hexToBinary(hex_edit_text.getText().toString()));
		    		}
		    		break;
		    	case 2:
		    		//Dec to *
		    		if(dec_edit_text.getText().toString().equals(""))
		    			Toast.makeText(me, "Nothing to do code", Toast.LENGTH_SHORT).show();
		    		else {
		    			plain_edit_text.setText(Ascii.decToString(dec_edit_text.getText().toString()));
		    			hex_edit_text.setText(Ascii.decToHex(dec_edit_text.getText().toString()));
		    			bin_edit_text.setText(Ascii.hexToBinary(dec_edit_text.getText().toString()));
		    		}
		    		break;
		    	case 3:
		    		//Dec to *
		    		if(bin_edit_text.getText().toString().equals(""))
		    			Toast.makeText(me, "Nothing to do code", Toast.LENGTH_SHORT).show();
		    		else {
		    			plain_edit_text.setText(Ascii.binaryToString(bin_edit_text.getText().toString()));
		    			hex_edit_text.setText(Ascii.binaryToHex(bin_edit_text.getText().toString()));
		    			dec_edit_text.setText(Ascii.binaryToDec(bin_edit_text.getText().toString()));
		    		}
		    		break;
		    	}
			}
	}

	
	
	@Override
	public View createTabContent(String tag) {
		return View.inflate(this, R.layout.main_spinner_only, null);
	}
	
	public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
	    	start_button.performClick();
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
	
	
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text="";
			switch(choice) {
			case 0: text=plain_edit_text.getText().toString(); break;
			case 1: text=hex_edit_text.getText().toString(); break;
			case 2: text=dec_edit_text.getText().toString(); break;
			case 3: text=bin_edit_text.getText().toString(); break;
			}
			if(msg.what == 1) {//copy clipboard
				Utils.copyToClipboardSimple(me, plain_edit_text.getText().toString(),text);
				//Toast.makeText(me, "Text copied", Toast.LENGTH_SHORT).show();
			}	
			else if(msg.what == 2) {//send sms	
				Utils.chooseTextToSendAndSend(me, plain_edit_text.getText().toString(), text);
			}
			else if(msg.what == 3) {
				Utils.addCipher(me,text);
			}
			else {
				switch(choice) {
				case 0: plain_edit_text.setText(msg.getData().getString("mess")); break;
				case 1: hex_edit_text.setText(msg.getData().getString("mess")); break;
				case 2: dec_edit_text.setText(msg.getData().getString("mess")); break;
				case 3: bin_edit_text.setText(msg.getData().getString("mess")); break;
				}	
			}
		};
	};
	
	
	
	private void chooseWhichOne(final int code) {
		final CharSequence[] items = {"Plain", "Hexadecimal", "Decimal","Binary"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select which text:");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	choice=item;
            	handler.sendEmptyMessage(code);
            }
        });
        builder.show();
	}
}
