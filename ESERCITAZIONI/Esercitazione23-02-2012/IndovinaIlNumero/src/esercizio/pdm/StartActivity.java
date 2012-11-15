package esercizio.pdm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends Activity {
	
		EditText text1;
		EditText text2;
		EditText text3;
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        text1=(EditText)findViewById(R.id.editText1);
        text2=(EditText)findViewById(R.id.editText2);
        text3=(EditText)findViewById(R.id.editText3);
        
        Button btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StartActivity.this,Main.class);
				String iltesto=text1.getText().toString();
				String iltesto2=text2.getText().toString();
				String iltesto3=text3.getText().toString();
				intent.putExtra("iltestonelbox", iltesto);
				intent.putExtra("iltestonelbox2", iltesto2);
				intent.putExtra("Password", iltesto3);
				startActivity(intent);
			}
		});
        
    }
}