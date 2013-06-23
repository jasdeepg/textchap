package com.example.textchap;

import java.io.IOException;
import java.security.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DemoActivity extends Activity {
	
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME =
            "onServerExpirationTimeMs";
    ///fdsf
    ///fd
    /**
     * Default lifespan (7 days) of a reservation until it is considered expired.
     */
    public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;

    /**
     * Substitute you own sender ID here.
     */
    String SENDER_ID = "331655500019";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    TextView gcm_display;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.gcm_view);
        gcm_display = (TextView) findViewById(R.id.gcm_text);

        context = getApplicationContext();
        regid = getRegistrationId(context);

        if (regid.length() == 0) {
            registerBackground();
        }
        gcm = GoogleCloudMessaging.getInstance(this);
	}
	
	/**
	 * Gets the current registration id for application on GCM service.
	 * <p>
	 * If result is empty, the registration has failed.
	 *
	 * @return registration id, or empty string if the registration is not
	 *         complete.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.length() == 0) {
	        Log.v(TAG, "Registration not found.");
	        return "";
	    }
	    // check if app was updated; if so, it must clear registration id to
	    // avoid a race condition if GCM sends a message
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion || isRegistrationExpired()) {
	        Log.v(TAG, "App version changed or registration expired.");
	        return "";
	    }
	    return registrationId;
	}


	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    return getSharedPreferences(DemoActivity.class.getSimpleName(), 
	            Context.MODE_PRIVATE);
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}

	/**
	 * Checks if the registration has expired.
	 *
	 * <p>To avoid the scenario where the device sends the registration to the
	 * server but the server loses it, the app developer may choose to re-register
	 * after REGISTRATION_EXPIRY_TIME_MS.
	 *
	 * @return true if the registration has expired.
	 */
	private boolean isRegistrationExpired() {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    // checks if the information is not stale
	    long expirationTime =
	            prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
	    return System.currentTimeMillis() > expirationTime;
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration id, app versionCode, and expiration time in the 
	 * application's shared preferences.
	 */
	private void registerBackground() {
	    new AsyncTask<Object, Object, Object>() {
	        @Override
	        protected Object doInBackground(Object... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration id=" + regid;

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the message
	                // using the 'from' address in the message.

	                // Save the regid - no need to register again.
	                setRegistrationId(context, regid);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(Object msg) {
	            gcm_display.append(msg + "\n");
	        }

	    }.execute(null, null, null);
	}
	
	/**
	 * Stores the registration id, app versionCode, and expiration time in the
	 * application's {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration id
	 */
	private void setRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.v(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    long expirationTime = System.currentTimeMillis() + REGISTRATION_EXPIRY_TIME_MS;

	    //Log.v(TAG, "Setting registration expiry time to " + new Timestamp(expirationTime));
	    editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
	    editor.commit();
	}

	public void onClick(final View view) {
	    if (view == findViewById(R.id.gcm_send)) {
	        new AsyncTask<Object, Object, Object>() {
	            @Override
	            protected Object doInBackground(Object... params) {
	                String msg = "";
	                try {
	                    Bundle data = new Bundle();
	                    data.putString("hello", "World");
	                    String id = Integer.toString(msgId.incrementAndGet());
	                    gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
	                    msg = "Sent message";
	                } catch (IOException ex) {
	                    msg = "Error :" + ex.getMessage();
	                }
	                return msg;
	            }

	            @Override
	            protected void onPostExecute(Object msg) {
	                gcm_display.append(msg + "\n");
	            }
	        }.execute(null, null, null);
	    } else if (view == findViewById(R.id.gcm_clear)) {
	        gcm_display.setText("");
	    } 
	}
	
	/**
	 * Handling of GCM messages.
	 */
	public class GcmBroadcastReceiver extends BroadcastReceiver {
	    static final String TAG = "GCMDemo";
	    public static final int NOTIFICATION_ID = 1;
	    private NotificationManager mNotificationManager;
	    NotificationCompat.Builder builder;
	    Context ctx;
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
	        ctx = context;
	        String messageType = gcm.getMessageType(intent);
	        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
	            sendNotification("Send error: " + intent.getExtras().toString());
	        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
	            sendNotification("Deleted messages on server: " +
	                    intent.getExtras().toString());
	        } else {
	            sendNotification("Received: " + intent.getExtras().toString());
	        }
	        setResultCode(Activity.RESULT_OK);
	    }

	    // Put the GCM message into a notification and post it.
	    private void sendNotification(String msg) {
	        mNotificationManager = (NotificationManager)
	                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

	        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
	                new Intent(ctx, DemoActivity.class), 0);

	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(ctx)
	        .setSmallIcon(android.R.drawable.ic_dialog_info)
	        .setContentTitle("GCM Notification")
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        .setContentText(msg);

	        mBuilder.setContentIntent(contentIntent);
	        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	    }
	}

}
