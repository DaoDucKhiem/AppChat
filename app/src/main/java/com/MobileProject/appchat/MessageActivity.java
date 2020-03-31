package com.MobileProject.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.MobileProject.appchat.Model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        intent = getIntent();
        String userid = intent.getStringExtra("userid");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);

                }
                else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //layout khi chuyển trạng thái vào cuộc trò chuyện
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    //xử lý button ở menu 2
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.video_call:
                Intent intent_ = new Intent(MessageActivity.this, CallingActivity.class);
                intent_.putExtra("userIdContact", getIntent().getStringExtra("userid"));
                startActivity(intent_);
                finish();
                break;
            default: break;
        }
        return false;
    }
}
