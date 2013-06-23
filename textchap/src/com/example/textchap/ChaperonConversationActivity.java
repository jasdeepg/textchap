/*
 * Copyright 2013 TextChap. 
 * 
 * Abstract: 
 * 
 *     This module implements the main conversation view for a chaperon monitoring
 *     a friend's conversation.
 *           
 * Author: 
 * 
 *     Chris 22-June-2013
 * 
 */

package com.example.textchap;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChaperonConversationActivity extends Activity {
	
	Button helpButton;
	Button sendSmsButton;
	EditText phoneNumberText;
	EditText messageText;
	IntentFilter receiveFilter;
	ListView messageListView;
	ConversationAdapter conversationAdapter;
	ArrayList<HashMap<String,String>> messageList;
	String friendNumber = "15555215556";
	String userNumber = "15555215554";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chaperon_conversation);
				
		/*
		 * Initialize the message list.
		 */
		
		messageList = new ArrayList<HashMap<String,String>>();
		
		/*
		 * TODO: Populate the message list based on the supplied conversation ID.
		 */
		
		/*
		 * Initialize the adapter and list view.
		 */
		
		messageListView = (ListView)findViewById(R.id.messageList);
		conversationAdapter = new ConversationAdapter(this, messageList);
		messageListView.setAdapter(conversationAdapter);
		
		/*
		 * Start the SMS receive filter.
		 */
		
		receiveFilter = new IntentFilter();
		receiveFilter.addAction(SmsReceiver.RECEIVE_INTENT);
		
		/*
		 * Initialize the elements for each view item.
		 */
		
		helpButton = (Button)findViewById(R.id.buttonHelp);
		sendSmsButton = (Button)findViewById(R.id.btnSendSMS);
		messageText = (EditText)findViewById(R.id.txtMessage);
				
		/*
		 * Create a click listener for the SMS button.
		 */
		
		sendSmsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String message;
				
				message = messageText.getText().toString();
				if (message.length() > 0) {
					sendSms(userNumber, message);
					
					/*
					 * Clear the message text.
					 */
					
					messageText.setText("");

				} else {
					Toast.makeText(getBaseContext(), 
							       "Please enter both a phone number and a message.", 
							       Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		/*
		 * Create a click listener for the help button.
		 * 
		 * TODO: Make this a slide up menu that goes half way up the screen.
		 * 
		 */
		
		helpButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		return;
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, receiveFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
    
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle;
			HashMap<String,String> messageMap;
			
			bundle = intent.getExtras();
			
			/*
			 * Add the new message to the correct stream based on the user.
			 */

			messageMap = new HashMap<String,String>();
			messageMap.put("body", bundle.getString("body"));			
			if (bundle.getString("from").equals(userNumber)) {
				messageMap.put("type", "user");
				messageList.add(messageMap);
				conversationAdapter.notifyDataSetChanged();
			}
			
			return;
		}
	};
    
	/*
	 * void
	 * sendSms (
	 *     String phoneNumber,
	 *     String message
	 *     );
	 * 
	 * Routine Description:
	 * 
	 *     This routine sends the given message to the given phone number as an SMS.
	 *     
	 * Arguments:
	 * 
	 *     phoneNumber - Supplies the phone number to which the message should be sent.
	 *     
	 *     message - Supplies the message to send the to the given phone number.
	 *     
	 * Return Value:
	 * 
	 *     None.
	 */
	
	private void sendSms(String phoneNumber, String message) {
		//Intent intent;
		//PendingIntent pendingIntent;
		SmsManager sms;
		HashMap<String,String> messageMap;
		
		//intent = new Intent(this, UserConversationActivity.class);
		//pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
		
		/*
		 * Display the send message in the conversation view.
		 * 
		 * TODO: Verify that the message was successfully sent (add "try again" feature).
		 */
		
		messageMap = new HashMap<String,String>();
		messageMap.put("type", "chaperon");
		messageMap.put("body", message);
		messageList.add(messageMap);
		conversationAdapter.notifyDataSetChanged();
		return;
	}	
}
