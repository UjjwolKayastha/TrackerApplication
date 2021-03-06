package kayastha.ujjwol.atrackerapp.friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;
import kayastha.ujjwol.atrackerapp.utilities.SectionPagerAdapter;


public class Contact extends AppCompatActivity {

    private static final String TAG = "Contact";

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    Firebase_method firebase_method;

    SectionPagerAdapter sectionPagerAdapter;
    ViewPager mViewPager;
    TabLayout mTablayout;


    String uID, uEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        //user
        FirebaseUser mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();
        uEmail = mUser.getEmail();
        Log.d(TAG, "onCreate: Current User: "+ uID);

//        mDatabase = FirebaseDatabase.getInstance().getReference()
//                .child("atrackerapp").child(uID);

        mViewPager = findViewById(R.id.view_pager);
        mTablayout= findViewById(R.id.tabs);

        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sectionPagerAdapter);
        mTablayout.setupWithViewPager(mViewPager);

        firebase_method = new Firebase_method(this);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Contact.this, Search_activity.class));
//                Snackbar.make(view, "SEArCH FRIENDS", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                addData();
            }
        });


    }

    private void addData(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.activity_search_activity, null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText email = myView.findViewById(R.id.etsearch);
        ImageButton save = myView.findViewById(R.id.btn_add_friend);
        ImageButton cancel = myView.findViewById(R.id.btn_cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName = email.getText().toString().trim();

                if (TextUtils.isEmpty(mName)){
                    email.setError("Required Field");
                    return;
                }

                firebase_method.searchEmail(mName, new Firebase_method.ResultCallBack<UserData>() {
                    @Override
                    public void onResult(UserData data) {
                        if(data == null){
                            // invalid email
                            Toast.makeText(Contact.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d("ASDFASDF", "Contact " + data.getId());
                            mReference.child("Friends").child(data.getId()).push().setValue(uEmail);
                            mReference.child("Friends").child(uID).push().setValue(data.getEmail());

                            Toast.makeText(getApplicationContext(), "Friend Added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
