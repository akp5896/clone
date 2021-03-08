package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    EditText etNewName;
    EditText etNewPass;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etNewName = findViewById(R.id.etNewName);
        etNewPass = findViewById(R.id.etNewPass);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(etNewName.getText().toString());
                user.setPassword(etNewPass.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null)
                        {
                            Toast.makeText(SignupActivity.this, "Cannot create account", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(SignupActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }
}