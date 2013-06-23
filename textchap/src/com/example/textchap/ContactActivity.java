package com.example.textchap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        
        List<String> idList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        
        if (cur.getCount() > 0) {
        	while (cur.moveToNext()) {
        		String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
        		
        		idList.add(id);
        		
        		String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		
        		nameList.add(name);
        		
        		if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	        	// may not need the phone # -- pass the contact_id
	        	}
	    	}
        }
        
	    setListAdapter(new ArrayAdapter<String>(this, R.layout.contacts_list_item,nameList));
	    ListView contact_list = getListView();

		contact_list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			    new HitServer().execute("1");
			    // When clicked go to next activity and pass threadId
			    /*
			     * Intent intent = new Intent(this, ChatActivity);
			     * intent.putExtra("thread_id", threadId_check[position]);
			     * startActivity(intent);
			     */
			}
		});

	    // TODO Auto-generated method stub
	}
	
	private class HitServer extends AsyncTask<String, URL, Integer> {
	    @Override
	     protected void onPostExecute(Integer result) {
	         //This gets called on the interface (main) thread!
		    Toast.makeText(getApplicationContext(),"222", Toast.LENGTH_SHORT).show();
	    }

		@Override
		protected Integer doInBackground(String... params) {
			// Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://respondto.it/textchap");
		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("id", "12345"));
		        nameValuePairs.add(new BasicNameValuePair("num_passed", params[0]));
		        nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);

			   
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
		    
	    	return null;
		}
	 }
	
}
