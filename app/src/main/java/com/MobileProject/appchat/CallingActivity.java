package com.MobileProject.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView nameUserContact;
    private ImageView btn_call, btn_cancel;

    private String receiverUserId, receiverUserName, receiverUserImage;
    private String senderUserId, senderUserName, senderUserImage, checker = "", callingID="", ringingID="";
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

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                cancelCallingUser();
            }
        });

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

//                    nameUserContact.setText(senderUserName);
//                    Picasso.get().load(senderUserImage).placeholder(R.drawable.prof_image).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        userRef.child(receiverUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!checker.equals("clicked") && !dataSnapshot.hasChild("Calling") && !dataSnapshot.hasChild("Ringing")) {
                            final HashMap<String, Object> callingInfo = new HashMap<>();
                            callingInfo.put("calling", receiverUserId);

                            userRef.child(senderUserId)
                                    .child("Calling")
                                    .updateChildren(callingInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                final HashMap<String, Object> ringingInfo = new HashMap<>();
                                                ringingInfo.put("ringing", senderUserId);

                                                userRef.child(receiverUserId)
                                                        .child("Ringing")
                                                        .updateChildren(ringingInfo);
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(senderUserId).hasChild("Ringing") && !dataSnapshot.child(senderUserId).hasChild("Calling")) {
                    btn_call.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cancelCallingUser() {
        //for sender
        userRef.child(senderUserId).child("Calling").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("calling")) {
                    callingID = dataSnapshot.child("calling").getValue().toString();

                    userRef.child(callingID).child("Ringing")
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()) {
                                       userRef.child(senderUserId).child("Calling")
                                               .removeValue()
                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                      startActivity(new Intent(CallingActivity.this, MainActivity.class));
                                                      finish();
                                                   }
                                               });
                                   }
                                }
                            });
                }
                else {
                    startActivity(new Intent(CallingActivity.this, MainActivity.class));
                    fileList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //for receiver
        userRef.child(senderUserId).child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("ringing")) {
                    ringingID = dataSnapshot.child("ringing").getValue().toString();

                    userRef.child(ringingID).child("Calling")
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        userRef.child(senderUserId).child("Ringing")
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        startActivity(new Intent(CallingActivity.this, MainActivity.class));
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            });
                }
                else {
                    startActivity(new Intent(CallingActivity.this, MainActivity.class));
                    fileList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
