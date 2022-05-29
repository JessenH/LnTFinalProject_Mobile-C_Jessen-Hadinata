package com.michael.pertemuan13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password, age;
    private Button btnRegister;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.et_registerEmail);
        password = findViewById(R.id.et_registerPassword);
        age = findViewById(R.id.et_registerAge);
        btnRegister = findViewById(R.id.btn_register);
        loginText = findViewById(R.id.tv_toLogin);

        btnRegister.setOnClickListener(view -> {
            register();
        });

        loginText.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

    }

    private void register() {
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String usia = age.getText().toString().trim();

        if (user.isEmpty()) {
            email.setError("Email cannot be empty!");
        }
        if (pass.isEmpty()) {
            password.setError("Password cannot be empty!");
        }
        if (usia.isEmpty()) {
            password.setError("Age cannot be empty!");
        }
        if (!user.isEmpty() && !pass.isEmpty() && !usia.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Data data = new Data(user, pass, usia);

                                FirebaseDatabase
                                        .getInstance()
                                        .getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(data)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegisterActivity.this, "Register Successful",Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Register Failed: "+task.getException().getMessage(),Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }
    }
}