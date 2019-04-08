package com.example.andre.basicfitness;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public Button register;  //register Button
    private Button login; //login Button
    private EditText email, password; // Edit text for email and password
    private FirebaseAuth firebaseAuth; //google Firebase
    private CheckBox showPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineId();

        firebaseAuth = FirebaseAuth.getInstance();

        //checks if user is login
        if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, main_page1.class);
            MainActivity.this.startActivity(intent);
        }

        //Show/Hide Password When User is Typing
        showPassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //set up listener for register button
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        //sets up listener for login button
       login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
       }

    //opens register page
    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    //Find all xml components
    public void defineId(){
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById((R.id.password));
        login = (Button)findViewById((R.id.login));
        register = (Button)findViewById((R.id.register));
        showPassWord = (CheckBox)findViewById((R.id.showPassword));

    }




   private void userLogin(){

        String acc_email = email.getText().toString();
        String pass = password.getText().toString();

        //check if email and password fields are empt
       if(TextUtils.isEmpty(acc_email)){
            Toast.makeText(this, "Must enter valid email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Must enter valid password", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(acc_email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, main_page1.class);
                            MainActivity.this.startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
