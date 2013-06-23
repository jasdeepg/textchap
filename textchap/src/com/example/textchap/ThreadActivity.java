package com.example.textchap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadActivity extends ListActivity {
	public static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	String new_var = "";
	static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
		"Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
		"Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    	    
	    setListAdapter(new ArrayAdapter<String>(this, R.layout.list_fruit,FRUITS));
	    ListView ll = getListView();

		ll.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});

	    Intent intent = getIntent();
	    Bundle b = intent.getBundleExtra("vars");
	    
	    String test[] = new String[2];
	    
	    test[0] = b.getString("new_var");
	    test[1] = b.getString("new_var2");
	    
	    //app_running(test);
	}
	
	public void app_running(String[] test){
	    TextView t = new TextView(this);
	    TextView t2 = new TextView(this);
	    
	    t.setText("Which thread do you need help with?");
	    t2.setText(test[1]);
	    
	    //ll.setOrientation(LinearLayout.VERTICAL);
	    
	    //ll.addView(t);
	    //ll.addView(t2);
	    	  
	    String WHERE_CONDITION = "thread_id = 7";
	    
	    // grab the SMS inbox handle
	    Cursor cursor = getContentResolver().query(SMS_INBOX, 
	    		new String[] { "_id", "thread_id", "address", "person", "date", "body" }, 
	    		WHERE_CONDITION, 	
	    		null, 
	    		"date DESC");
	    // move to the front of it
	    cursor.moveToFirst();
	    
	    List<Long> threadId_check = new ArrayList<Long>();
	    
	    // iterate through the inbox and grab the message components
	    // create listeners associated with each unique threadId
	    do{
	    	  String msgData = "";
	    	  
	          long messageId = cursor.getLong(0);
	          long threadId = cursor.getLong(1);
	          String address = cursor.getString(2)	;
	          long contactId = cursor.getLong(3);
	          String contactId_string = String.valueOf(contactId);
	          long timestamp = cursor.getLong(4);
	
	          String body = cursor.getString(5);
	          
	          if (!threadId_check.contains(threadId)){
	        	  threadId_check.add(threadId);
		   	      TextView msg_text = new TextView(this);
		          msgData = threadId + " " + timestamp + " " + body + "\n";
		          msg_text.setText(msgData);
		          //ll.addView(msg_text);
	          }
	    }while(cursor.moveToNext());

	    //this.setContentView(ll);
	}
	
}
