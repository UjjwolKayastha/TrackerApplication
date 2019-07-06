package kayastha.ujjwol.atrackerapp.friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

import static android.support.constraint.Constraints.TAG;

public class Friends_tab extends Fragment {
    private RecyclerView recyclerView;

    private String name;
    private String description;
    String uID;
    Firebase_method firebase_method;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        mAuth = FirebaseAuth.getInstance();

        firebase_method = new Firebase_method(getActivity());


        //user
        FirebaseUser mUser = mAuth.getCurrentUser();
        uID = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("All Data").child(uID);

        recyclerView = root.findViewById(R.id.recycler_friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        return root;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebase_method.userFriends(uID, new Firebase_method.ResultCallBack<UserData>(){
            @Override
            public void onResult(UserData data) {

                List<String> friends = null;
                friends.add(data.toString());
                Log.d("fff", "onResult: " + friends);
                Toast.makeText(getActivity(), "FRIENDS"+ friends, Toast.LENGTH_SHORT).show();
            }
//            public void onResult(UserData friendata){
//                adapter.addItem(friendData);
//
//            }
        });

    }

    //retrieve data from database
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerAdapter<UserData, MyViewHolder> adapter=new FirebaseRecyclerAdapter<UserData, MyViewHolder>(
//                UserData.class,
//                R.layout.friend_list_design,
//                MyViewHolder.class,
//                mDatabase
//        ) {
//
//            protected void populateViewHolder(MyViewHolder viewHolder, final UserData model, final int position) {
//
//                viewHolder.setDate(model.getDate());
//                viewHolder.setName(model.getName());
//                viewHolder.setDescription(model.getDescription());
//
//                viewHolder.view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        post_key = getRef(position).getKey();
//                        name = model.getName();
//                        description = model.getDescription();
//
////                        updateData();
//                    }
//                });
//            }
//        };
//        recyclerView.setAdapter(adapter);
//
//    }

    //new class for recycler view
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setName(String name){
            TextView mName = view.findViewById(R.id.nameItem);
            mName.setText(name);
        }

        public void setDescription(String description){
            TextView mDescription = view.findViewById(R.id.descItem);
            mDescription.setText(description);
        }

        public void setDate(String date){
            TextView mDate = view.findViewById(R.id.date);
            mDate.setText(date);
        }
    }

}
