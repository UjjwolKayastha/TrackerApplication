package kayastha.ujjwol.atrackerapp.message;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import kayastha.ujjwol.atrackerapp.DashBoard;
import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.createChannel.CreateChannel;

//import android.util.Log;
//import android.widget.EditText;
//import androidx.annotation.Nullable;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.wearable.MessageApi;
//import com.google.android.gms.wearable.Node;
//import com.google.android.gms.wearable.NodeApi;
//import com.google.android.gms.wearable.Wearable;

public class Message extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_message;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton, submitButton;
    EmojIconActions emojIconActions;

    ImageButton back;

    private NotificationManagerCompat notificationManagerCompat;



//    GoogleApiClient googleApiClient = null;
//
//    public static final String TAG = "MyDataMAP....";
//    public static final String WEARABLE_DATA_PATH = "/wearable/data/path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        activity_message = findViewById(R.id.activity_message);

        //Add Emoji
        emojiButton = findViewById(R.id.emoji_button);
        submitButton = findViewById(R.id.submit_button);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_message,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class) );
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Messages").push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                DisplayNotification();

                emojiconEditText.setText("");
                emojiconEditText.requestFocus();

            }
        });
        //Check if not sign-in then navigate Signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_message,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            //Load content
            displayChatMessage();
        }

        notificationManagerCompat = NotificationManagerCompat.from(this);

        CreateChannel createChannel = new CreateChannel(this);
        createChannel.createChannel();

//        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
//        builder.addApi(Wearable.API);
//        builder.addConnectionCallbacks(this);
//        builder.addOnConnectionFailedListener(this);
//        googleApiClient = builder.build();


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        googleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop() {
//        if(googleApiClient != null && googleApiClient.isConnected()){
//            googleApiClient.disconnect();
//        }
//        super.onStop();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        sendMessage();
//    }
//
//
//    public void sendMessage(){
//        if(googleApiClient.isConnected()){
//            String message = ((EditText)findViewById(R.id.text)).getText().toString();
//            if(message == null || message.equalsIgnoreCase("")){
//                message="HEY THERE DELILAH";
//            }
//            new SendMessageToDataLayer(WEARABLE_DATA_PATH, message).start();  //this calls the run method in the class
//        }else{
//
//        }
//    }
//
//    public void sendMessageOnClick(View view){
//        sendMessage();
//    }
//
//    public class  SendMessageToDataLayer extends Thread {
//        String path;
//        String message;
//
//        public SendMessageToDataLayer(String path, String message) {
//            this.path = path;
//            this.message = message;
//        }
//
//        @Override
//        public void run() {  //we send message using this method
//            NodeApi.GetConnectedNodesResult nodesList = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
//            for(Node node: nodesList.getNodes()){
//                MessageApi.SendMessageResult messageResult = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), path, message.getBytes()).await();
//                if(messageResult.getStatus().isSuccess()){
//
//                    Log.d(TAG, "run: Message sent successfully to "+ node.getDisplayName());
//                    Log.d(TAG, "run: Message NODE ID"+ node.getId());
//                    Log.d(TAG, "run: Message sent successfully of SIZE "+ nodesList.getNodes().size());
//                }else{
//                    Log.d(TAG, "run: ERROR");
//                }
//            }
//        }
//    }


//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }

//    @Override
//    public void onConnectionFailed(@androidx.annotation.NonNull ConnectionResult connectionResult) {
//
//    }

    private void DisplayNotification() {
        Notification notification = new NotificationCompat.Builder(this, CreateChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.message)
                .setContentTitle("New Message")
                .setContentText(emojiconEditText.getText().toString())
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1, notification);
    }

    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference("Messages")) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                messageText = (BubbleTextView) v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };

        listOfMessage.setAdapter(adapter);
    }
}
