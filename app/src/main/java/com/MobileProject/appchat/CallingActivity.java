package com.MobileProject.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CallingActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView nameUserContact;
    private ImageView btn_call, btn_cancel;

    private String receiverUserId, receiverUserName, receiverUserImage;
    private String senderUserId, senderUserName, senderUserImage;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUserId = getIntent().getExtras().get("userIdContact").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        profileImage = findViewById(R.id.imageCalling);
        nameUserContact = findViewById(R.id.name_calling);
        btn_call = findViewById(R.id.make_call);
        btn_cancel = findViewById(R.id.cancel_call);

        getAndSetUserProfileInfo();
    }

    private void getAndSetUserProfileInfo() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(receiverUserId).exists()) {
                    receiverUserImage = dataSnapshot.child(receiverUserId).child("imageURL").getValue().toString();
                    receiverUserName = dataSnapshot.child(receiverUserId).child("username").getValue().toString();

                    nameUserContact.setText(receiverUserName);
                    Picasso.get().load(receiverUserImage).placeholder(R.drawable.prof_image).into(profileImage);
                }

                if (dataSnapshot.child(senderUserId).exists()) {
                    senderUserImage = dataSnapshot.child(senderUserId).child("imageURL").getValue().toString();
                    senderUserName = dataSnapshot.child(senderUserId).child("username").getValue().toString();

                    nameUserContact.setText(senderUserName);
                    Picasso.get().load(senderUserImage).placeholder(R.drawable.prof_image).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
