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

import java.util.ArrayList;
import java.util.List;

import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.models.UserData;
import kayastha.ujjwol.atrackerapp.utilities.Firebase_method;

import static android.support.constraint.Constraints.TAG;

public class Friends_tab extends Fragment {
    private RecyclerView recyclerView;

    private String name;
//    private String email;
    String uID, uEmail;
    Firebase_method firebase_method;
    RecyclerViewAdapter mAdapter;
    List<UserData> mDataList;

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
        uEmail = mUser.getEmail();

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(uID);

        recyclerView = root.findViewById(R.id.recycler_friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mDataList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(getContext(), mDataList);
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebase_method.userFriends(uID, new Firebase_method.ResultCallBack<UserData>(){
            @Override
            public void onResult(UserData data) {
                mDataList.add(data);
                mAdapter.notifyItemInserted(mDataList.size()-1);
            }
        });

    }


}
