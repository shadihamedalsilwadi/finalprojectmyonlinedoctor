package com.example.myonlinedoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmailET , mPasswordET ;
    Button mRegisterBtn ;
    TextView mHaveaccountTV;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        mEmailET = findViewById(R.id.emailET);
        mPasswordET = findViewById(R.id.passET);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mHaveaccountTV=findViewById(R.id.have_accountTV);

        mAuth = FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailET.getText().toString().trim();
                String password = mPasswordET.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailET.setError("Invalid Email");
                    mEmailET.setFocusable(true);
                }
                else if(password.length()<6){
                    mPasswordET.setError("Password length at least 6 characters");
                    mPasswordET.setFocusable(true);
                }
                else {
                    registeruser(email,password);
                }
            }
        });

        mHaveaccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this ,LoginActivity.class));
                finish();
            }
        });



    }

    private void registeruser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success,
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Get user email and uid from auth
                            String email = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object,String> hashMap = new HashMap<>();
                            //put info in hash map
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            hashMap.put("cover","");
                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //path to store user data name "Users"
                            DatabaseReference reference = database.getReference("Users");
                            //put data within hash map in database
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Registered .. \n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this , DashBoardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                   progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
        public boolean onSupportNavigateUp(){
        onBackPressed(); //go previoue activity
        return super.onSupportNavigateUp();
        }

}
