package kayastha.ujjwol.atrackerapp.friends;

import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import kayastha.ujjwol.atrackerapp.R;

public class Search_activity extends AppCompatActivity {

    private String mCurrent_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);

        mCurrent_status = "not_friends";


    }


}
