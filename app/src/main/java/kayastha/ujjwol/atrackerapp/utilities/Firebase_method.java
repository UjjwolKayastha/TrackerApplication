package kayastha.ujjwol.atrackerapp.utilities;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kayastha.ujjwol.atrackerapp.models.UserData;

public class Firebase_method {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    Context mContext;

    String userID;

    public Firebase_method(Context context){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mContext = context;
    }

    public void register_new_email(String strEmail, String strPassword){
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(mContext, "REGISTRATION FAILED", Toast.LENGTH_SHORT).show();
                        }else {
                            userID = mAuth.getCurrentUser().getUid();

                        }
                    }
                });
    }


    public void send_new_user_data(String name, String email, String password, String gender){
        UserData userData = new UserData(name, email, password, gender);
        mReference.child(userID).setValue(userData);

    }
}
