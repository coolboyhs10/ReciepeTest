package com.example.reciepetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registerBtn;
    private EditText email;
    private EditText password;
    private TextView loginText;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

       if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),firstActivity.class));
       }

        progressDialog = new ProgressDialog(this);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginText = (TextView) findViewById(R.id.loginText);

        registerBtn.setOnClickListener(this);
        loginText.setOnClickListener(this);
    }

    private void registerUser(){
        String emailStr =  email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if(TextUtils.isEmpty(emailStr)){
            Toast.makeText(this,"Please Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passwordStr)){
            Toast.makeText(this,"Please Enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user ... ");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emailStr,passwordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),firstActivity.class));
                            Toast.makeText(MainActivity.this,"REGISTRATION SUCCESS",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this,"COULD NOT REGISTER",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == registerBtn){
            registerUser();
        }
        if(v == loginText){
            finish();
            startActivity(new Intent(getApplicationContext(),loginActivity.class));
        }
    }
}
