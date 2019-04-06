package com.example.andre.basicfitness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class main_page1 extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);

        firebaseAuth = FirebaseAuth.getInstance();

        //if user is not signed in

        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(main_page1.this, MainActivity.class);
            main_page1.this.startActivity(intent);
        }

        //finds button
        logout = (Button) findViewById(R.id.logout);

        //creates listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(main_page1.this, MainActivity.class);
                main_page1.this.startActivity(intent);
        }
        });
    }
}
