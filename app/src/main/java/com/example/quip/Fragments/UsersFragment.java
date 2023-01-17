package com.example.quip.Fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.quip.Adapter.UserAdapter;
import com.example.quip.Model.User;
import com.example.quip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    EditText search_users;
//    Cursor cursor;
//    String phone;
//
//    ArrayList<UserObject> userList, contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

//        contactList= new ArrayList<>();
//        userList= new ArrayList<>();


        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsers(String toString) {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(toString)
                .endAt(toString+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue( User.class );
                    if (user.getId()!=null && !user.getId().equals( fuser.getUid() )) {
                        mUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter( getContext(), mUsers, false );
                recyclerView.setAdapter( userAdapter );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readUsers() {

//        cursor = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//
//        while (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//
//            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//
//            if (phone.length() <= 10) {
//                continue;
//            }
//
//            if (!String.valueOf(phone.charAt(0)).equals("+")) {
//                phone = "+88" + phone;
//            }
//            phone = phone.replace(" ", "");
//            phone = phone.replace("-", "");
//            phone = phone.replace("(", "");
//            phone = phone.replace(")", "");
//
//            final UserObject mContact = new UserObject("", name, phone);
//            contactList.add(mContact);


            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
//            Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
              mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(search_users.getText().toString().equals("")){
                        mUsers.clear();

//                    if (dataSnapshot.exists()) {

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            User user = childSnapshot.getValue( User.class );
                            if (user.getId()!=null && !user.getId().equals( firebaseUser.getUid() )) {
                                mUsers.add(user);
                            }

                        }
                        userAdapter = new UserAdapter( getContext(), mUsers, false );
                        recyclerView.setAdapter( userAdapter );


                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

//        cursor.close();
//    }
}