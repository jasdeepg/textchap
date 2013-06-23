/*
 * Copyright 2013 TextChap. 
 * 
 * Abstract: 
 * 
 *     This module implements the main conversation view for a user receiving
 *     advice from a chaperon.
 *           
 * Author: 
 * 
 *     Chris 22-June-2013
 * 
 */

package com.example.textchap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
 
public class SmsReceiver extends BroadcastReceiver {
	public static final String RECEIVE_INTENT = "com.textcap.receive";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle;
		SmsMessage[] msgs;
		Object[] pdus;
		
		bundle = intent.getExtras();
		if (bundle != null) {
			pdus = (Object[])bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int Index = 0; Index < msgs.length; Index += 1) {
				Intent messageIntent;

				msgs[Index] = SmsMessage.createFromPdu((byte[])pdus[Index]);
				messageIntent = new Intent();
				messageIntent.setAction(RECEIVE_INTENT);
				messageIntent.putExtra("from", msgs[Index].getOriginatingAddress());
				messageIntent.putExtra("body", msgs[Index].getMessageBody().toString());
				context.sendBroadcast(messageIntent);
			}			
		}
		
		return;
	}
}
