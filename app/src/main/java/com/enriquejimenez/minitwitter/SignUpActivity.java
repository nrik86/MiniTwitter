package com.enriquejimenez.minitwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private TextView goLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        signUpButton = findViewById(R.id.buttonSignUp);
        goLoginTextView = findViewById(R.id.textViewGoLogin);

        signUpButton.setOnClickListener(this);
        goLoginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonSignUp:
                break;
            case R.id.textViewGoLogin:
                goToLogin();
        }
    }

    private void goToLogin() {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
