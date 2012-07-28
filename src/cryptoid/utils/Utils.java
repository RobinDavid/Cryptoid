package cryptoid.utils;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cryptoid.main.R;

public class Utils {
	static Dialog dialog;
	static String mess;
	public Utils() {}
	
	
	//------------------------ showAbout ----------------------------
	public static void showAbout(Context c, int title_string, int content_string) {
		dialog = new Dialog(c);
        dialog.setContentView(R.layout.about_dialog);
        
        TextView title = (TextView) dialog.findViewById(R.id.about_title);
        title.setText(c.getResources().getString(title_string));
        Button button_ok = (Button) dialog.findViewById(R.id.about_button_ok);
        button_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
        TextView text = (TextView) dialog.findViewById(R.id.about_text);
        text.setText(c.getResources().getString(content_string));
        dialog.show();
	}
	//-----------------------------------------------------------------
	
	
	//------------------- copyToClipboardSimple -------------------
	public static void copyToClipboardSimple(Context c, String toCopy, String toastMessage) {
		ClipboardManager clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE); 
		clipboard.setText(toCopy);
		Toast.makeText(c.getApplicationContext(),toastMessage,Toast.LENGTH_SHORT).show();
	}
	//------------------------------------------------------------
	
	
	//------------------ copyToClipboardComplex ------------------
	public static void copyToClipboardComplex(final Context c, final String plainText, final String cipheredText) {
	//the difference with the simple is that we choose between plain text or ciphered text	
		final ClipboardManager clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE); 
		
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        
        
        builder.setMessage("Which Text do you want to copy ?")
               .setCancelable(false)
               .setPositiveButton("Plain", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   clipboard.setText(plainText);
                	   Toast.makeText(c.getApplicationContext(),"Plain text copied",Toast.LENGTH_SHORT).show();
                   }
               })
               .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dialog.cancel();
                   }
               })
               .setNegativeButton("Ciphered", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   clipboard.setText(cipheredText);
                	   Toast.makeText(c.getApplicationContext(),"Ciphered text copied",Toast.LENGTH_SHORT).show();
                   }
               });
        builder.show();
	}
	//------------------------------------------------------------------------
	
	
	//------------------------------ SaveFile ---------------------------------
	public static void saveFile(final Context c,final String filename, final String content) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Save Report")
        		.setMessage("The report will be saved on the sdcard if it's possible and in the internal memory instead.\nClick yes to continue")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //------- we save the result --------

                	   try {
	                	   if (android.os.Environment.getExternalStorageState().equals( 
	                			   android.os.Environment.MEDIA_MOUNTED)) {
	                		   
	                		   Toast.makeText(c.getApplicationContext(),"We write in sdcard",Toast.LENGTH_SHORT).show();
	                		   FileWriter f = new FileWriter("/sdcard/"+filename);
	                		   f.write(content);
	                		   f.close();
	                	   }
	                	   else {
	                		   FileOutputStream file;
	                		   file = c.openFileOutput(filename, Context.MODE_WORLD_READABLE);
	                		   OutputStreamWriter osw = new OutputStreamWriter(file);
		                       osw.write(content);
		                       osw.flush();
		                       osw.close();
	                	   }

	                       Toast.makeText(c.getApplicationContext(),"File "+filename+" saved with success !",Toast.LENGTH_SHORT).show();
                	   }
                       catch (IOException e) {
                    	   Toast.makeText(c.getApplicationContext(),"Fail to write to file :\n"+e.toString(),Toast.LENGTH_LONG).show();
                       }
                       catch (Exception e) {
                    	   Toast.makeText(c.getApplicationContext(),"Unknown error :\n"+e.toString(),Toast.LENGTH_LONG).show();
                       }
                	   //-----------------------------------
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        builder.show();
	}
	//------------------------------------------------------------------------
	
	//-------------------------- importMessage -------------------------------
	public static String importMessage(final Context con, final Cursor c, final Handler han) {
		
		if(c.moveToFirst()) {//try with cursor != null
			
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(con);
		 	        builder.setTitle("Select a message");
		 	        builder.setCursor(c, new DialogInterface.OnClickListener() {
		 	            public void onClick(DialogInterface dialog, int item) {
		 	            	c.moveToPosition(item);
		 	            	mess = c.getString(c.getColumnIndexOrThrow("body"));
		 	            	//we retrieve the message
		 	            	
		 	            	//convert it to a good type
		 	            	Message infos = new Message();
		 					Bundle infos_to_send = new Bundle();
		 					infos_to_send.putString("mess", mess);
		 					infos.setData(infos_to_send);
		 					
		 					han.sendMessage(infos);//it's the handler which will put the text in the textview
		 	            }
		 	        }, "body");
		 	        builder.show();
		}
		return mess;
	}
	//-------------------------------------------------------------------
	
	
	//----------------- chooseTextToSendAndSend ----------------------------
	public static void chooseTextToSendAndSend(final Activity c, final String plain,final String ciphered) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("Which Text do you want to send ?")
               .setCancelable(false)
               .setPositiveButton("Plain", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   if(plain.equals(""))
                		   Toast.makeText(c.getApplicationContext(),"Nothing to send",Toast.LENGTH_SHORT).show();
                	   else
                		   sendSMS(c, plain);//send the plain text message
                   }
               })
               .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dialog.cancel();
                   }
               })
               .setNegativeButton("Ciphered", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   if(ciphered.equals(""))
                		   Toast.makeText(c.getApplicationContext(),"Nothing to send",Toast.LENGTH_SHORT).show();
                	   else
                		   sendSMS(c, ciphered);//send the ciphered message
                	   
                   }
               });
        builder.show();
	}
	//---------------------------------------------------------------------------
	
	
	
	//------------------------ sendSMS ---------------------------------
	public static void sendSMS(final Activity c, final String mess) {
	     final Cursor cursor = c.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "DISPLAY_NAME ");

	     c.startManagingCursor(cursor);
	     if(cursor.moveToFirst()) {

	    	 	AlertDialog.Builder builder = new AlertDialog.Builder(c);
	 	        builder.setTitle("Select a contact");
	 	        builder.setCursor(cursor, new DialogInterface.OnClickListener() {
	 	            public void onClick(DialogInterface dialog, int item) {
	 	            	cursor.moveToPosition(item);

	 	            	String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
	 	            	
	 	            	//the second request with the LOOKUP_KEY to find phone number
	 	            	String[] whereArgs = new String[] { id, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) };
	 	            	Cursor cur = c.getContentResolver().query(
	 	                      ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	 	                      null,
	 	                      ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ? and "
	 	                              + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?", whereArgs, null);
	 	            	c.startManagingCursor(cur);
	 	              int phoneNumberIndex = cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
	 	             
	 	              String phone_number = "";	

	 	            	
	 	              if(cur.moveToNext())
	 	              		do {
	 	              			phone_number = cur.getString(phoneNumberIndex);//here we get the phone number
	 	              		} while (cur.moveToNext());
	 	              			if (phone_number.equals(""))
	 	              				Toast.makeText(c.getApplicationContext(), "Sending message Failed\nContact do not have mobile phone number", Toast.LENGTH_LONG).show();
	 	              			else {
	 	              				SmsManager smsMgr = SmsManager.getDefault();
	 	              				smsMgr.sendTextMessage(phone_number, null, mess, null, null);
	 	              				Toast.makeText(c.getApplicationContext(),"Message sent :\n"+mess, Toast.LENGTH_SHORT).show();
	 	              			}
	 	              			
	 	                
	 	               c.stopManagingCursor(cur);
	 	            }
	 	        }, ContactsContract.Contacts.DISPLAY_NAME);
	 	        builder.show();
	     }
	     c.stopManagingCursor(cursor);
	}
	//---------------------------------------------------------------------
	
	
	//--------------------- finishMe ---------------------------
	public static void finishMe(Activity a) {
		a.finish();
	}
	//----------------------------------------------------------
	
	
	//-------------------------- addPlain ---------------------------------
	public static void addPlain(Activity a, String text) {
		if(text.equals(""))
			Toast.makeText(a, "No plain text to forward to another uncipher", Toast.LENGTH_LONG).show();
		else {
			a.setResult(Activity.RESULT_OK, new Intent().putExtra("type", "plain").putExtra("value", text));
			a.finish();
		}
	}
	//----------------------------------------------------------------------
	
	//--------------------- addCipher -------------------------------
	public static void addCipher(Activity a,String text) {
		if(text.equals(""))
			Toast.makeText(a, "No cipher text to forward to another cipher", Toast.LENGTH_LONG).show();
		else {
			a.setResult(Activity.RESULT_OK, new Intent().putExtra("type", "ciphered").putExtra("value", text));
			a.finish();
		}
	}
	//---------------------------------------------------------------
}
