package com.enriquejimenez.minitwitter.ui.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.retrofit.MiniTwitterClient;
import com.enriquejimenez.minitwitter.retrofit.MiniTwitterService;
import com.enriquejimenez.minitwitter.retrofit.request.RequestSignUp;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseAuth;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private TextView goLoginTextView;
    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText passWordEditText;

    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        signUpButton = findViewById(R.id.buttonSignUp);
        goLoginTextView = findViewById(R.id.textViewGoLogin);
        passWordEditText = findViewById(R.id.editTextPassSignUp);
        emailEditText = findViewById(R.id.editTextEmailSignUp);
        userNameEditText = findViewById(R.id.editTextUserNameSignUp);
    }

    private void eventsViews() {
        signUpButton.setOnClickListener(this);
        goLoginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonSignUp:
                doSignUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
        }
    }

    private void doSignUp() {
        String email = emailEditText.getText().toString();
        String passWord = passWordEditText.getText().toString();
        String userName = userNameEditText.getText().toString();

        if(email.isEmpty()){
            emailEditText.setError("El email es requerido");
        }else if(passWord.isEmpty()){
            passWordEditText.setError("La contraseña es requerida");
        }else if(userName.isEmpty()){
            passWordEditText.setError("El nombre de usuario es requerido");
        }else{
            RequestSignUp requestSignUp = new RequestSignUp(userName,email,passWord,"UDEMYANDROID");

            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignUp);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Conectado", Toast.LENGTH_SHORT).show();
                        SharedPreferencesManager.setString(Constants.PREF_TOKEN,response.body().getToken());
                        SharedPreferencesManager.setString(Constants.PREF_USER,response.body().getUsername());
                        SharedPreferencesManager.setString(Constants.PREF_EMAIL,response.body().getEmail());
                        SharedPreferencesManager.setString(Constants.PREF_URL_PHOTO,response.body().getPhotoUrl());
                        SharedPreferencesManager.setString(Constants.PREF_CREATED,response.body().getCreated());
                        SharedPreferencesManager.setBoolean(Constants.PREF_ACTIVE,response.body().getActive());


                        finish();
                        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(SignUpActivity.this, "Algo fué mal, por favor revise sus datos", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Problemas con la conexión", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void goToLogin() {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        finish();
        startActivity(i);
    }
}
