package com.example.andre.basicfitness;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText email, password;
    private Button register;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        defineId();

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    //Upload data to google firebase

                    String acc_email = email.getText().toString().trim();
                    String pass = password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(acc_email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this,"Registration Success",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                Register.this.startActivity(intent);
                            }
                            else{
                                Toast.makeText(Register.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    public void defineId(){

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById((R.id.password));
        register = (Button)findViewById((R.id.login));
    }

    private Boolean validate(){
        Boolean initial = false;

        String acc_email = email.getText().toString();
        String pass = password.getText().toString();

        if( acc_email.isEmpty() && pass.isEmpty())
        {
            Toast.makeText(this,"Missing Information: Cannot Sign In",Toast.LENGTH_SHORT).show();
        }

        else
        {
            initial = true;
        }

        return initial;
    }

    public void openMain() {
        Intent intent = new Intent(Register.this, main_page1.class);
        Register.this.startActivity(intent);
    }

}