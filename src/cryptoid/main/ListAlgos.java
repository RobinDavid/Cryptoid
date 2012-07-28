package cryptoid.main;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListAlgos extends ListActivity{
static private ArrayList<Algorithm> my_list=new ArrayList<Algorithm>();
	
	static {
		//we add elements into our list
		my_list.add(new Algorithm(R.string.ascii, R.drawable.asc48_bk,R.string.ascii_class));
		my_list.add(new Algorithm(R.string.base64, R.drawable.b64_48_bk,R.string.base64_class));
		my_list.add(new Algorithm(R.string.caesar, R.drawable.cs48_bk,R.string.caesar_class));
		my_list.add(new Algorithm(R.string.morse, R.drawable.mr48_bk, R.string.morse_class));
		my_list.add(new Algorithm(R.string.nihilist, R.drawable.ni48_bk,R.string.nihilist_class));
		my_list.add(new Algorithm(R.string.polybius, R.drawable.po48_bk, R.string.polybius_class));
		my_list.add(new Algorithm(R.string.vigenere, R.drawable.vg48_bk,R.string.vigenere_class));
	}
	Intent my_intent;
	private static final int CODE_PAGE = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_algos);
		setListAdapter(new AlgorithmAdapter());
		my_intent = new Intent();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v,int position, long id) {
		try {
			//Here we try to find the class to launch it
			my_intent.setClass(this,Class.forName("cryptoid.activityalgorithm."+getString(my_list.get(position)._class)));
			startActivityForResult(my_intent,CODE_PAGE);
		} catch (ClassNotFoundException e) {
		      Toast.makeText(this, "Sorry algorithm not implemented.", 2000).show();
			e.printStackTrace();
		} catch (Exception e) {
			Toast.makeText(this, "Exception: "+e.toString(), 2000).show();
			e.printStackTrace();
		}

	}
	
	static class Algorithm {
		int _name;
		int _picture;
		int _class;
		
		Algorithm(int name, int picture, int classRecieved) {
			this._name=name;
			this._picture=picture;
			this._class=classRecieved;
		}
	}
	
	class AlgorithmAdapter extends ArrayAdapter<Algorithm> {
		AlgorithmAdapter() {
			super(ListAlgos.this, R.layout.row, R.id.name, my_list);
		}
		
		@Override
		public View getView(int position, View convertView,
												ViewGroup parent) {
			AlgorithmWrapper wrapper=null;
			
			if (convertView==null) {
				convertView=getLayoutInflater().inflate(R.layout.row, null);
				wrapper=new AlgorithmWrapper(convertView);
				convertView.setTag(wrapper);
			}
			else {
				wrapper=(AlgorithmWrapper)convertView.getTag();
			}
			
			wrapper.populateFrom(getItem(position));
			
			return(convertView);
		}
	}

	class AlgorithmWrapper {
		private TextView name=null;
		private ImageView picture=null;
		private View row=null;
		
		AlgorithmWrapper(View row) {
			this.row=row;
		}
		
		TextView getName() {
			if (name==null) {
				name=(TextView)row.findViewById(R.id.name);
			}
			
			return(name);
		}
		
		ImageView getPicture() {
			if (picture==null) {
				picture=(ImageView)row.findViewById(R.id.picture);
			}
			
			return(picture);
		}
		
		void populateFrom(Algorithm algo) {
			getName().setText(algo._name);
			getPicture().setImageResource(algo._picture);
		}
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == CODE_PAGE) {
    		if (data != null) {
	            Bundle extra = data.getExtras();
	            if(extra != null) {
		            String type = extra.getString("type");
		            String value = extra.getString("value");
		            my_intent.putExtra("type", type);
		            my_intent.putExtra("value", value);
		            //we put extra to forward messages to others intents
	            }
    		}
    	}
	}
	
}