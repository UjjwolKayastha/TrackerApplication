package kayastha.ujjwol.atrackerapp;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MyListenerService extends WearableListenerService {
    public static final String TAG = "MyDataMAP....";
    public static final String WEARABLE_DATA_PATH = "/wearable/data/path";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals(WEARABLE_DATA_PATH)){
            final String message = new String(messageEvent.getData());
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.putExtra("message", message);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
            Log.d(TAG, "onMessageReceived: MAIN ACTIVITI HAS BEEN STARTED");
        }else {
            super.onMessageReceived(messageEvent);
        }
    }
}
