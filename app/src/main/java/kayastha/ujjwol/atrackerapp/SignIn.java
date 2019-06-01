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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private static final String TAG = SignIn.class.getClass().getSimpleName();

    EditText email, password;
    Button login;

    TextView actreg;



    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth  = FirebaseAuth.getInstance();

        email = findViewById(R.id.signin_email);
        password = findViewById(R.id.signin_password);

        actreg = findViewById(R.id.actReg);
        actreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

        login = findViewById(R.id.btn_signIn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String semail, spassword;
                semail = email.getText().toString();
                spassword = password.getText().toString();

                if(Validate()){
                    firebaseAuth.signInWithEmailAndPassword(semail, spassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(getApplicationContext(), DashBoard.class));
                                        Log.d(TAG, "onComplete: LOGGED IN SUCCESSFULLY!");
                                        Toast.makeText(SignIn.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignIn.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

    }

    public boolean Validate(){
       if (TextUtils.isEmpty(email.getText().toString())){
            email.setError("Required Field");
            return false;
        }
        else if (TextUtils.isEmpty(password.getText().toString())){
            password.setError("Required Field");
            return false;
        }

        return true;
    }
}
