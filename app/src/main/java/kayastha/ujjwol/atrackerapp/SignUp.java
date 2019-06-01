package kayastha.ujjwol.atrackerapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import kayastha.ujjwol.atrackerapp.R;

public class SignUp extends AppCompatActivity {

    EditText name, email, password, cpassword;
    Button signup;

    //Firebase
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //casting firebase
        firebaseAuth  = FirebaseAuth.getInstance();



        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signin_email);
        password = findViewById(R.id.signin_password);
        cpassword = findViewById(R.id.signup_confirmPassword);

        signup = findViewById(R.id.btn_signUp);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Validate()){

                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "REGISTRATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), SignIn.class));

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
