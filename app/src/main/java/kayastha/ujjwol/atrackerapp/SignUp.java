package kayastha.ujjwol.atrackerapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import kayastha.ujjwol.atrackerapp.R;

public class SignUp extends AppCompatActivity {

    CircleImageView profile_image;
    EditText name, email, password, cpassword;
    Button signup;

    TextView actlogin;
    //Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;


    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //creating firebaseauth instance
        firebaseAuth  = FirebaseAuth.getInstance();


        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        cpassword = findViewById(R.id.signup_confirmPassword);

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
                Log.d("val", "onClick: SIGNUP CLICKED");
                String inemail = email.getText().toString().trim();
                String inpassword = password.getText().toString().trim();


                if(Validate()){

                    firebaseAuth.createUserWithEmailAndPassword(inemail,inpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                userID = firebaseAuth.getCurrentUser().getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

//                                HashMap<String, String> hashMap = new HashMap<>();
//                                hashMap.put("id", userID);
//                                hashMap.put("id", userID);
//                                hashMap.put("id", userID);

                                Toast.makeText(SignUp.this, "REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), SignIn.class));


                            }else {
                                // If sign in fails, display a message to the user.
                                Log.w("val", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "REGISTRATION FAILED",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    public boolean Validate(){
        if (TextUtils.isEmpty(name.getText().toString())){
            name.setError("Required Field");
            return false;
        }
        else if (TextUtils.isEmpty(email.getText().toString())){
            email.setError("Required Field");
            return false;
        }
        else if (TextUtils.isEmpty(password.getText().toString())){
            password.setError("Required Field");
            return false;
        }else if (TextUtils.isEmpty(cpassword.getText().toString())){
            cpassword.setError("Required Field");
            return false;
        }

        return true;
    }

}
