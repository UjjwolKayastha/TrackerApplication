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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn extends AppCompatActivity {

    private static final String TAG = SignIn.class.getClass().getSimpleName();

    EditText email, password;
    Button login;

    TextView actreg;

    private FirebaseAuth firebaseAuth;

    SignInButton googleSignin;
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient mGooogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

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

                if (Validate()) {
                    firebaseAuth.signInWithEmailAndPassword(semail, spassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
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

        googleSignin = findViewById(R.id.signin_google);

        signinGSO();

        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

    }

    //google sign in Option
    private void signinGSO() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGooogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGooogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            Log.d(TAG, googleSignInResult.getStatus().getStatusMessage());

            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {


                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed");
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(getApplicationContext(), DashBoard.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignIn.this, "AUTHENTICATION FAILED", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public boolean Validate() {
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Required Field");
            return false;
        } else if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Required Field");
            return false;
        }

        return true;
    }
}
