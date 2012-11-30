package esame.pdm;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Second extends Activity{
    
    ListView lv;
	ArrayAdapter<String>adapter;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	   
	        
	        String tit = getIntent().getStringExtra("film");
	        
	        lv=(ListView)findViewById(R.id.listView1);
	        adapter=new ArrayAdapter<String>(Second.this,R.layout.row, R.id.rowtext1);
	        lv.setAdapter(adapter);
	        String tit2 = tit.split(":")[1];
	        
	        XmlResourceParser parser = getResources().getXml(R.xml.film);
	        
	        try{
	        
	        	int eventType = parser.getEventType();
	        	while(eventType != XmlResourceParser.END_DOCUMENT){
	        		
	        		if (eventType == XmlResourceParser.START_TAG){
	        			 String tagName = parser.getName(); 
	        				if ("film".equals(tagName)){
	    						eventType = parser.next();
	    						if(eventType == XmlResourceParser.TEXT){
	    							String elementValue = parser.getText();
	    							if(elementValue.equals(tit2)){
	    							String titolo = elementValue;
	    							Log.d("XML PARSER2",titolo);
	    							adapter.add("FILM:"+titolo);
	    							
	    							eventType = parser.next();
		    						if (eventType == XmlResourceParser.START_TAG){
		    							String tagName1 = parser.getName();
		    							if("regia".equals(tagName1)){
		    								eventType = parser.next();
		    								if(eventType == XmlResourceParser.TEXT){
		    									String regia = parser.getText();
		    									adapter.add("REGIA:"+regia);
		    									
		    									eventType = parser.next();
		    									eventType = XmlResourceParser.END_TAG;
		    									eventType = parser.next();
		    		    						if (eventType == XmlResourceParser.START_TAG){
		    		    							String tagName2 = parser.getName();
		    		    							if("data".equals(tagName2)){
		    		    								eventType = parser.next();
		    		    								if(eventType == XmlResourceParser.TEXT){
		    		    									String data = parser.getText();
		    		    									adapter.add("DATA:"+data);
		    		    									
		    		    									eventType = parser.next();
		    		    									eventType = XmlResourceParser.END_TAG;
		    		    									eventType = parser.next();
		    		    		    						if (eventType == XmlResourceParser.START_TAG){
		    		    		    							String tagName3 = parser.getName();
		    		    		    							if("trama".equals(tagName3)){
		    		    		    								eventType = parser.next();
		    		    		    								if(eventType == XmlResourceParser.TEXT){
		    		    		    									String trama = parser.getText();
		    		    		    									adapter.add("TRAMA:"+trama);
		    		    		    								}
		    		    		    							}
		    		    		    						}
		    		    								}
		    		    							}
		    		    						}
		    								}
		    							}
		    						}
	    						}
	    							
	    					}			
	    				}
	        	}
	        		eventType = parser.next();
	        }
	        			
	        		
	        }catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}    
	        
	 }

}
