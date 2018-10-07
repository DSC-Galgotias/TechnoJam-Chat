package tech.honeysharma.techbmechat.Chat;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tech.honeysharma.techbmechat.Models.Request;
import tech.honeysharma.techbmechat.R;
import tech.honeysharma.techbmechat.RequestAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    DatabaseReference mDatabaseReference;
    FirebaseDatabase mFirebaseDatabase;
    Request request;
    int i;
    ArrayList<Request> mRequests = new ArrayList<>();
    RequestAdapter mAdapter;
    ArrayList<String> recievedUserID = new ArrayList<>();
    Users users;
    ArrayList<Users> mUsers = new ArrayList<>();
    private RecyclerView recyclerView;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_requests, container, false);



        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        Log.e("RequestFragment"  , "Current userid is : " + FirebaseAuth.getInstance().getCurrentUser().getUid());



        mDatabaseReference.child("Friend_req")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                            Log.e("ReqFragment" , "Key is : " + ds.getKey());

                            request = ds.getValue(Request.class);
                            Log.e("Data is ", "Data is : " +request.getRequest_type());
                            if (request.getRequest_type().equals("received")){

                                recievedUserID.add(ds.getKey());
                                Log.e("RequestFragment" , "User id list is : " + recievedUserID);

                            }
                        }
                        for (  i = 0 ; i<=recievedUserID.size() - 1 ; i++){
                            Log.e("ReqFragment" , "Inside for loop " );
                            mDatabaseReference.child("Users")
                                    .child(recievedUserID.get(i)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    users = dataSnapshot.getValue(Users.class);
                                    mUsers.add(users);
                                    Log.e("RequestFragment" , "After just addition size is : " + mUsers.size());
                                    Log.e("ReqFragment" , "User data is : " + mUsers.get(0).getName());
                                    mAdapter = new RequestAdapter(getContext() , mUsers);
                                    recyclerView.setAdapter(mAdapter);

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                            Log.e("ReqFrGMENT" , "bEFORE FOR LOOP" + mUsers.size());
                        }

                        Log.e("RequestFragment" , "Size of user id list is : " + mUsers.size());

                    }



                    @Override

                    public void onCancelled(@NonNull DatabaseError databaseError) {



                    }

                });





        return view;
    }

}
