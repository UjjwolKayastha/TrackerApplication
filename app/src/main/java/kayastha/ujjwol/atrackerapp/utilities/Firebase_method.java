package kayastha.ujjwol.atrackerapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import kayastha.ujjwol.atrackerapp.SignIn;
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

    public void register_new_email(final String name, final String email,final String password,final String gender){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("val", "onComplete: TAsk: "+ task.getResult().getUser().getUid());
                        if(!task.isSuccessful()){
                            Toast.makeText(mContext, "REGISTRATION FAILED", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signOut(){
        mAuth.getInstance().signOut();
        Toast.makeText(mContext, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        mContext.startActivity(new Intent(mContext.getApplicationContext(), SignIn.class));
    }

    public void create_new_userData(final String name, final String email,final String password,final String gender, final String userID){
        UserData userData = new UserData(userID, name, email, password, gender);
        mReference.child("Users").child(userID).setValue(userData);
    }

    // userid -> friends -> []
    // mReference.child(userID).child("friends").addValue(friendId)

    public void searchEmail(String email, final ResultCallBack<UserData> callBack){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        callBack.onResult(user.getValue(UserData.class));
                        return;
                    }
                }
                callBack.onResult(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public interface ResultCallBack<T>{
        void onResult(T data);
    }

//    private void fetchUserFromUserId(String userId, final ResultCallBack<UserData> callBack){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//
//        Query query = reference.orderByChild("id").equalTo(userId);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot user : dataSnapshot.getChildren()) {
//                        callBack.onResult(user.getValue(UserData.class));
//                        return;
//                    }
//                }
//
//                callBack.onResult(null);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void userFriends(String id, final ResultCallBack<UserData> callBack){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Friends").child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String friendEmail=dataSnapshot.getValue(String.class);
                searchEmail(friendEmail, callBack);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

