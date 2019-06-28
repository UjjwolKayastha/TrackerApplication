package kayastha.ujjwol.atrackerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;


public class Contact extends AppCompatActivity {

    Firebase_method firebase_method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        firebase_method = new Firebase_method(this);

        firebase_method.searchEmail("anup@gmail.com", new Firebase_method.ResultCallBack<UserData>() {
            @Override
            public void onResult(UserData data) {
                if(data ==null){
                    // invalid email
                }else {
//                    Log.d("ASDFASDF", "Contant " + data.getEmail());
                }
            }
        });
    }
}
