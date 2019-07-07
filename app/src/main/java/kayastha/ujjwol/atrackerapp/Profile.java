package kayastha.ujjwol.atrackerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

public class Profile extends AppCompatActivity {

    ImageView profile_image;
    TextView tv_name, tv_email;
    private static final int Gallery_Pick = 1;

    Button updateProfile, deleteProfile;

    Firebase_method firebase_method;

    FirebaseAuth firebaseAuth;
    DatabaseReference mReference;
    FirebaseDatabase mDatabase;
    private StorageReference userprofileImagereference;


    String currentUserId, currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebase_method = new Firebase_method(this);

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        currentUserId = firebaseAuth.getCurrentUser().getUid();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

        userprofileImagereference = FirebaseStorage.getInstance().getReference().child("Profile Images");

        profile_image = findViewById(R.id.imageView);
        tv_name = findViewById(R.id.textView);
        tv_email = findViewById(R.id.textView2);

        deleteProfile = findViewById(R.id.btndelete);
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Profile.this);
                dialog.setTitle("Are you sure you want to delete your Account??");
                dialog.setTitle("Once deleted your account cannot be recovered!!");

                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mReference.child("Users").child(currentUserId).removeValue();
                                            Toast.makeText(Profile.this, "Account Deleted Successfully!! :(", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), SignUp.class));
                                        }
                                    }
                                });
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        firebase_method.searchEmail(currentUserEmail, new Firebase_method.ResultCallBack<UserData>() {
            @Override
            public void onResult(UserData data) {
                Picasso.with(Profile.this).load(data.getProfileImage()).into(profile_image);
                tv_name.setText(data.getName());
            }
        });

        tv_email.setText(currentUserEmail);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });


        updateProfile = findViewById(R.id.btnUpdateDialog);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog(currentUserId, currentUserEmail);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final StorageReference filePath = userprofileImagereference.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Image Stored Successfully", Toast.LENGTH_SHORT).show();
                            UploadTask.TaskSnapshot taskResult = task.getResult();
                            final String downloadUrl = taskResult.toString();


                            Log.d("upload_link", "onComplete: "
                                    + taskResult.getMetadata()
                                    .getReference()
                                    .getDownloadUrl()
                                    .toString()
                                    + " : ");


                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Log.d("uri", downloadUrl.toString());
                                    //image url
                                    Picasso.with(Profile.this).load(downloadUrl).into(profile_image);


                                    mReference.child("Users").child(currentUserId).child("profileImage").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Log.d("download", "onComplete: " + downloadUrl);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Profile.this, "Image stored in Firebase", Toast.LENGTH_SHORT).show();


                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(Profile.this, message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("failure", "FAILURE: " + e.getLocalizedMessage());

                    }
                });  //saves cropped image in firebasestorage
            } else {
                Toast.makeText(this, "Image cannot be cropped", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showUpdateDialog(final String currentUserId, String currentUserEmail){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_dialog, null);
        builder.setView(view);

        firebase_method.searchEmail(currentUserEmail, new Firebase_method.ResultCallBack<UserData>() {
            @Override
            public void onResult(UserData data) {
                String userName = data.getName();
                final TextView textViewName = view.findViewById(R.id.OldUsernameTextView);
                textViewName.setText(data.getName());
            }
        });

        final Button buttonUPdate = view.findViewById(R.id.updateUsernameButton);

        builder.setTitle("Update Username: " + currentUserEmail);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonUPdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editTextName = view.findViewById(R.id.updateUsernameEditText);

                if(TextUtils.isEmpty(editTextName.getText().toString())){
                    editTextName.setError("Please Enter a Username");
                }

                mReference.child("Users").child(currentUserId).child("name").setValue(editTextName.getText().toString());
                Toast.makeText(Profile.this, "Username Updated Successfully", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });


    }


}
