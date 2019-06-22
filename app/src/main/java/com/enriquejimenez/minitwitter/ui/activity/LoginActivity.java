package com.enriquejimenez.minitwitter.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.retrofit.MiniTwitterClient;
import com.enriquejimenez.minitwitter.retrofit.MiniTwitterService;
import com.enriquejimenez.minitwitter.retrofit.request.RequestLogin;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseAuth;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private TextView goSignUpTextView;

    private EditText emailEditText;
    private EditText passWordEditText;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        retrofitInit();

        setViews();
        eventsViews();
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }


    private void setViews() {
        loginButton = findViewById(R.id.buttonLogin);
        goSignUpTextView = findViewById(R.id.textViewGoSignUp);
        passWordEditText = findViewById(R.id.editTextPassLogin);
        emailEditText = findViewById(R.id.editTextEmailLogin);
    }

    private void eventsViews() {
        loginButton.setOnClickListener(this);
        goSignUpTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonLogin:
                doLogin();
                break;
            case R.id.textViewGoSignUp:
                goToSignUp();
        }
    }

    private void doLogin() {
        String email = emailEditText.getText().toString();
        String passWord = passWordEditText.getText().toString();

        if(email.isEmpty()){
            emailEditText.setError("El email es requerido");
        }else if(passWord.isEmpty()){
            passWordEditText.setError("La contraseña es requerida");
        }else{
            RequestLogin requestLogin = new RequestLogin(email,passWord);

            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Conectado", Toast.LENGTH_SHORT).show();
                        SharedPreferencesManager.setString(Constants.PREF_TOKEN,response.body().getToken());
                        SharedPreferencesManager.setString(Constants.PREF_USER,response.body().getUsername());
                        SharedPreferencesManager.setString(Constants.PREF_EMAIL,response.body().getEmail());
                        SharedPreferencesManager.setString(Constants.PREF_URL_PHOTO,response.body().getPhotoUrl());
                        SharedPreferencesManager.setString(Constants.PREF_CREATED,response.body().getCreated());
                        SharedPreferencesManager.setBoolean(Constants.PREF_ACTIVE,response.body().getActive());
                        finish();
                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(LoginActivity.this, "Algo fué mal, por favor revise sus datos", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Problemas con la conexión", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void goToSignUp() {
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        finish();
        startActivity(i);
    }
}
