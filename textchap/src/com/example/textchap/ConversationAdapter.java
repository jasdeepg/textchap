package com.example.textchap;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
 
public class ConversationAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_USER = 0;
	private static final int VIEW_TYPE_FRIEND = 1;
	private static final int VIEW_TYPE_CHAPERON = 2;
	private static final int VIEW_TYPE_COUNT = 3;
	
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
 
    public ConversationAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    public int getItemViewType(int position) {
        HashMap<String, String> message = new HashMap<String, String>();
        String messageType;
        int type;
    	
        message = data.get(position);
        messageType = message.get("type");
    	if (messageType.equals("user") != false) {
    		type = VIEW_TYPE_USER;
    		
    	} else if (messageType.equals("friend") != false) {
    		type = VIEW_TYPE_FRIEND;

    	} else {
    		
    		assert(messageType.equals("chaperon") != false);

    		type = VIEW_TYPE_CHAPERON;
    	}

    	return type;
    }
    
    public int getViewTypeCount() {
    	return VIEW_TYPE_COUNT;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        HashMap<String, String> message = new HashMap<String, String>();

        message = data.get(position);
        if (convertView == null) {
        	int type;
        	
        	type = this.getItemViewType(position);
        	if (type == VIEW_TYPE_USER) {
        		vi = inflater.inflate(R.layout.user_text, null);

        	} else if (type == VIEW_TYPE_FRIEND) {
        		vi = inflater.inflate(R.layout.friend_text, null);
        		
        	} else {
        		
        		assert(type == VIEW_TYPE_CHAPERON);
        		
        		vi = inflater.inflate(R.layout.chaperon_text,  null);
        	}	
        }
        
        TextView messageBody = (TextView)vi.findViewById(R.id.txtMessage);
        
        // Setting all values in listview
        messageBody.setText(message.get("body"));
        return vi;
    }
}
