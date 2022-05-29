package com.michael.pertemuan13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference dRef;
    private String userID;
    private Button btnReturn;
    private TextView toolsName, toolsAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnReturn = findViewById(R.id.btn_return);
        toolsName = findViewById(R.id.tv_toolsUser);
        toolsAge = findViewById(R.id.tv_toolsAge);

        btnReturn.setOnClickListener(view -> {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        dRef = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        dRef.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Data dataProfile = snapshot.getValue(Data.class);
                        if (dataProfile != null) {
                            String username = dataProfile.getUsername();
                            String age = dataProfile.getAge();

                            toolsName.setText(username);
                            toolsAge.setText(age);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileActivity.this, "Error! Not Found!", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

    }
}