package cryptoid.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EntryPoint extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	Button algo_button, analyze_button;
	Button main_picture;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main_picture = (Button)findViewById(R.id.mainpicture);
        algo_button = (Button)findViewById(R.id.button_algorithms);
        analyze_button = (Button)findViewById(R.id.button_cryptanalysis);
        algo_button.setOnClickListener(this);
        analyze_button.setOnClickListener (this);
        main_picture.setOnClickListener(this);
    }
   
    @Override
	public void onClick(View v) {
		try {
			switch(v.getId()) {
				case R.id.button_algorithms:
					startActivity(new Intent(this,ListAlgos.class));
					break;
				case R.id.button_cryptanalysis:
					startActivity(new Intent(this,Class.forName("cryptoid.cryptanalysis.Cryptanalysis")));
					break;
				case R.id.mainpicture:
					Toast.makeText(this, "Picture clicked !", 2000).show();
					break;
				default:
					throw new RuntimeException("Error :Button ID unknown");
			}
		}
		catch (ClassNotFoundException e) {
		      Toast.makeText(this, "Class not found: "+e.toString(), 2000).show();
				e.printStackTrace();
		}
		catch (Exception e) {
				Toast.makeText(this, "Exception: "+e.toString(), 2000).show();
				e.printStackTrace();
		}
	}
}