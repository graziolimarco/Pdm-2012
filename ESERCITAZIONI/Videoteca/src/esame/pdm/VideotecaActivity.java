package esame.pdm;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class VideotecaActivity extends Activity{
    /** Called when the activity is first created. */
	
	ListView lv;
	ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv=(ListView)findViewById(R.id.listView1);
        adapter=new ArrayAdapter<String>(VideotecaActivity.this,R.layout.row, R.id.rowtext1);
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(VideotecaActivity.this, Second.class);
				i.putExtra("film", (String)arg0.getItemAtPosition(arg2));		
				startActivity(i);
			}
        	
		});
        
        XmlResourceParser parser = getResources().getXml(R.xml.film);
		
        try {
				int eventType = parser.getEventType();
				while (eventType != XmlResourceParser.END_DOCUMENT) {
				
					if (eventType == XmlResourceParser.START_TAG) {
						String tagName = parser.getName();
						if ("film".equals(tagName)){
							eventType = parser.next();
							if(eventType == XmlResourceParser.TEXT){
							String elementValue = parser.getText();
							String titolo = elementValue;
							Log.d("XML PARSER",titolo);
							adapter.add("FILM:"+titolo);
						}
					}
					
					
				}
				eventType = parser.next();
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
		
    }

}