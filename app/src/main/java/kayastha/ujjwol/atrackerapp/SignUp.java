package kayastha.ujjwol.atrackerapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

public class SignUp extends AppCompatActivity {

    CircleImageView profile_image;
    private static final int REQUEST_CODE = 5;

    EditText etname, etemail, etpassword, etcpassword;
    Button signup;

    RadioGroup rgGender;
    RadioButton genderOptions;

    Firebase_method firebase_method;

    TextView actlogin;

    FirebaseDatabase mDatabase;

    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference mReference;

    FirebaseAuth.AuthStateListener mAuthListener;



    public String strName, strEmail, strPassword, strGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        firebase_method = new Firebase_method(this);

        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        etname = findViewById(R.id.signup_name);
        etemail = findViewById(R.id.signup_email);
        etpassword = findViewById(R.id.signup_password);
        etcpassword = findViewById(R.id.signup_confirmPassword);
        rgGender = findViewById(R.id.rg_gender);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                genderOptions = rgGender.findViewById(checkedId); //choose from the buttons
                switch (checkedId){
                    case R.id.rbMale:
                        strGender = genderOptions.getText().toString();
                        break;
                    case R.id.rbFemale:
                        strGender = genderOptions.getText().toString();
                        break;

                        default:
                }
            }
        });

        actlogin = findViewById(R.id.actLogin);
        actlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
            }
        });

        signup = findViewById(R.id.btn_signUp);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               register_new_user();
            }
        });

        setupFirebaseAuthentication();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && requestCode == RESULT_OK){
            Uri imagePath = data.getData();
            CropImage.activity(imagePath).setGuidelines(CropImageView.Guidelines.ON).start(SignUp.this); //to take the image and crop

        }
    }

    private void register_new_user() {

        if(Validate()){
            strEmail = etemail.getText().toString();
            strPassword = etpassword.getText().toString();
            strName = etname.getText().toString();
//            strGender = "Male"; //TEst

            firebase_method.register_new_email(strEmail, strPassword);
        }

    }


    private void setupFirebaseAuthentication(){
        firebaseAuth  = FirebaseAuth.getInstance();
        Log.d("val", "setupFirebaseAuthentication: READY TO SEND DATA");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    String userId = firebaseAuth.getCurrentUser().getUid();

                    Log.d("val", "onAuthStateChanged: userID" + userId);

                    mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            firebase_method.send_new_user_data(strName, strEmail, strPassword, strGender);
                            Toast.makeText(SignUp.this, "REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean Validate(){
        if (TextUtils.isEmpty(etname.getText().toString())){
            etname.setError("Required Field");
            return false;
        }
        else if (TextUtils.isEmpty(etemail.getText().toString())){
            etemail.setError("Required Field");
            return false;
        }
        else if (TextUtils.isEmpty(etpassword.getText().toString())){
            etpassword.setError("Required Field");
            return false;
        }else if (TextUtils.isEmpty(etcpassword.getText().toString())){
            etcpassword.setError("Required Field");
            return false;
        }

        return true;
    }




}


