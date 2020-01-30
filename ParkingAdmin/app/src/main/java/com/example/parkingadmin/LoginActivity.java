package com.example.parkingadmin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;


public class LoginActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    private EditText userEmail;
    private EditText userPassword;
    private Button userLoginBtn;
    private static final String TAG = "LoginActivity";
//    FirebaseAuth.AuthStateListener stateListener;
//    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        userLoginBtn = findViewById(R.id.userLoginBtn);


        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = userEmail.getText().toString();
                String p = userPassword.getText().toString();
                if (e.isEmpty()) {

                    userEmail.setError("Empty Email not allowed");
                } else if (p.isEmpty()) {

                    userPassword.setError("Empty Password not allowed");
                } else {

                  //  LoginUser(e, p);

                }
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseAuth.addAuthStateListener(stateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        firebaseAuth.removeAuthStateListener(stateListener);
//    }
//
//    private void LoginUser(String email, String pass) {
//
//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
//        progressDialog.setTitle("Validation . . .");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        firebaseAuth.signInWithEmailAndPassword(email, pass
//        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(Task<AuthResult> task) {
//
//                if (task.isSuccessful()) {
//
//                    Toast.makeText(LoginActivity.this, "User Found", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//
//                } else {
//
//                    Toast.makeText(LoginActivity.this, "USer Not Found", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }
//        });
//
//
//    }
}