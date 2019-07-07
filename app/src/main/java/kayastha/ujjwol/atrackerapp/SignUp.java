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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import de.hdodenhof.circleimageview.CircleImageView;

import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

public class SignUp extends AppCompatActivity {

    CircleImageView profile_image;
    private static final int Gallery_Pick = 1;

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
    ValueEventListener valueEventListener;

//    private StorageReference userprofileImagereference;



    public String strName, strEmail, strPassword, strGender;

    String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
//        userprofileImagereference = FirebaseStorage.getInstance().getReference().child("Profile Images");

        firebase_method = new Firebase_method(this);
//        currentUserId = firebaseAuth.getCurrentUser().getUid();


//        profile_image = findViewById(R.id.profile_image);
//        profile_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, Gallery_Pick);
//            }
//        });


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

                //firebaseAuth.addAuthStateListener(mAuthListener);
            }
        });
        setupFirebaseAuthentication();



    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null){
//            Uri ImageUri = data.getData();
//
//            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);
//        }
//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if(resultCode == RESULT_OK){
//                Uri resultUri = result.getUri();
//
//                final StorageReference filePath = userprofileImagereference.child(currentUserId + ".jpg");
//                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(SignUp.this, "Image Stored Successfully", Toast.LENGTH_SHORT).show();
//                            UploadTask.TaskSnapshot taskResult = task.getResult();
//                            final String downloadUrl = taskResult.toString();
//
//
//                           Log.d("upload_link", "onComplete: "
//                                   +  taskResult.getMetadata()
//                                   .getReference()
//                                   .getDownloadUrl()
//                                   .toString()
//                                   +" : ");
//
//
//                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Uri downloadUrl = uri;
//                                    Log.d("uri", downloadUrl.toString());
//                                    //Do what you want with the url
//                                }
//                            });
//
//                            mReference.child("ProfileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Log.d("download", "onComplete: "+ downloadUrl);
//                                    if(task.isSuccessful()){
//                                        Toast.makeText(SignUp.this, "Image stored in Firebase", Toast.LENGTH_SHORT).show();
//
//
//                                    } else {
//                                        String message = task.getException().getMessage();
//                                        Toast.makeText(SignUp.this, message, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("failure", "FAILURE: "+ e.getLocalizedMessage());
//
//                    }
//                });  //saves cropped image in firebasestorage
//            } else {
//                Toast.makeText(this, "Image cannot be cropped", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

    private void register_new_user() {

        if(Validate()){
            strEmail = etemail.getText().toString();
            strPassword = etpassword.getText().toString();
            strName = etname.getText().toString();
//            strGender = "Male"; //TEst

            firebase_method.register_new_email(strName, strEmail, strPassword, strGender);
        }

    }


    private void setupFirebaseAuthentication(){
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };





        Log.d("val", "setupFirebaseAuthentication: READY TO SEND DATA");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    firebase_method.create_new_userData(strName, strEmail, strPassword, strGender, userId);

                    Log.d("val", "onAuthStateChanged: userID" + userId);

//                    firebase_method.send_new_user_data(strName, strEmail, strPassword, strGender);
                    Toast.makeText(SignUp.this, "REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(getApplicationContext(), DashBoard.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
        mReference.addListenerForSingleValueEvent(valueEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        mReference.removeEventListener(valueEventListener);

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


