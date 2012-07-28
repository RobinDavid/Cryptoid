package cryptoid.activityalgorithm;

import cryptoid.algorithms.Polybius;
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

public class MainPolybius extends ActivityGroup implements OnClickListener, TabContentFactory, RadioGroup.OnCheckedChangeListener {
	Button slideHandleButton, start_button, copy_button, import_button, send_button, addcipher_button, addplain_button, analyze_button, back_button, brute_button, about_button ;
	EditText plain_edit_text, cipher_edit_text;
	TabHost tab;
	SlidingDrawer slidingDrawer;
	RelativeLayout layout_main;
	TextView main_title;
	RadioButton radio_cipher, radio_decipher;
	RadioGroup radio_group;
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
        
        //---------------- Setup widget of the frame -----------------
        layout_main = (RelativeLayout)findViewById(R.id.RelativeLayoutMain);
        tab.getTabWidget().setVisibility(android.view.View.GONE);
        plain_edit_text=(EditText)findViewById(R.id.main_basic_plain);
        cipher_edit_text=(EditText)findViewById(R.id.main_basic_cipher);
        main_title = (TextView)findViewById(R.id.main_basic_title);
        main_title.setText("Polybius");
        radio_group= (RadioGroup)findViewById(R.id.main_basic_choice);
        radio_group.setOnCheckedChangeListener(this);
        radio_cipher = (RadioButton)findViewById(R.id.main_basic_radio_cipher);
        radio_decipher = (RadioButton)findViewById(R.id.main_basic_radio_decipher);
        start_button=(Button)findViewById(R.id.main_basic_button_go);
        start_button.setOnClickListener(this);
        //------------------------------------------------------------
        
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
        if(Polybius.analyseAvailable())
        	analyze_button.setEnabled(true);
        if(Polybius.bruteAvailable())
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
				//nothing append because it's not available
				break;
				
			case R.id.algos_button_brute:
				//nothing append because it's not available
				break;
			case R.id.algos_button_send:
				//Call a Utils method to send message by SMS.
			    Utils.chooseTextToSendAndSend(this, plain_edit_text.getText().toString(), cipher_edit_text.getText().toString());
			    break;
			     
			case R.id.algos_button_addcipher:
				//this function allow to add another cipher to the ciphered text, to increase "shadow" of password
				Utils.addCipher(this, cipher_edit_text.getText().toString());
				break;
				
			case R.id.algos_button_addplain:
				//this function allow to try to decipher the plain text with another cipher, (in the case of multi layer ciphering)
				Utils.addPlain(this, plain_edit_text.getText().toString());
				break;
				
			case R.id.algos_button_back:
				Utils.finishMe(this);
				break;
				
			case R.id.algos_button_copy:
				//Copy the text to clipboard, thank's to a Utils method
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
				int text = R.string.mainpolybius_about_text;
				int title = R.string.mainpolybius_about_title;
				Utils.showAbout(this,title,text);
				break;
				
			case R.id.main_basic_button_go:
					if(radio_cipher.isChecked()) {//it's there that the cipher or decipher method is called
						if(!(plain_edit_text.getText().toString().equals("")))
							cipher_edit_text.setText(Polybius.cipher(plain_edit_text.getText().toString()));
					}	
					else if(radio_decipher.isChecked()) {
						if(!(cipher_edit_text.getText().toString().equals("")))
							plain_edit_text.setText(Polybius.decipher(cipher_edit_text.getText().toString()));
					}
				break;
			}
				
	}

	@Override
	public View createTabContent(String tag) {
		return View.inflate(this, R.layout.main_basic, null);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		start_button.performClick();//easier than put the same things than in the OnClick method
	}

	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			//Recieve message from AlertDialog of message import.
			cipher_edit_text.setText(msg.getData().getString("mess"));
		};
	};
}
