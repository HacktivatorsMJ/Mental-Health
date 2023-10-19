package com.hacktivators.mentalhealth;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    CircleImageView profile;
    TextView name,email,dob;

    View ds1,ds2,ds3,ds4,ds5;
    View ss1,ss2,ss3,ss4,ss5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        profile = view.findViewById(R.id.profile);

        name = view.findViewById(R.id.nameBox);
        email = view.findViewById(R.id.emailBox);
        dob = view.findViewById(R.id.dobBox);


        ds1 = view.findViewById(R.id.ds1);
        ds2 = view.findViewById(R.id.ds2);
        ds3 = view.findViewById(R.id.ds3);
        ds4 = view.findViewById(R.id.ds4);
        ds5 = view.findViewById(R.id.ds5);

        ss1 = view.findViewById(R.id.ss1);
        ss2 = view.findViewById(R.id.ss2);
        ss3 = view.findViewById(R.id.ss3);
        ss4 = view.findViewById(R.id.ss4);
        ss5 = view.findViewById(R.id.ss5);


        loadData();



        return view;
    }

    private void loadData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("CheckResult")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //String Username = document.getString("username");
                        String Imageurl = document.getString("imageURL");
                        String username_ = document.getString("username");
                        String email_ = document.getString("email");
                        String dob_ = document.getString("age");

                        name.setText(username_);
                        email.setText(email_);
                        dob.setText(dob_);


                        Glide.with(requireActivity()).load(Imageurl).into(profile);


                    }
                }
            }
        });
    }
}