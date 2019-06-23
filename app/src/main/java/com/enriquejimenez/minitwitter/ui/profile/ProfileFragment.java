package com.enriquejimenez.minitwitter.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.mvvm.profile.ProfileViewModel;
import com.enriquejimenez.minitwitter.retrofit.request.RequestUserProfile;
import com.enriquejimenez.minitwitter.retrofit.response.ResponseUserProfile;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;
import com.enriquejimenez.minitwitter.utils.Utils;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel profileViewModel;
    ImageView avatarProfileImageView;
    EditText userNameEditText;
    EditText emailEditText;
    EditText webSiteEditText;
    EditText descriptionEditText;
    EditText passwordEditText;
    Button saveButton;
    Button changePasswordButton;
    private boolean loadingData = true;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        setViews(view);
        eventViews();
        setAvatarImageView();

        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(ResponseUserProfile responseUserProfile) {

                if(Utils.isNullOrEmpty(responseUserProfile.getUsername())){
                    userNameEditText.setText(responseUserProfile.getUsername());
                }
                if(Utils.isNullOrEmpty(responseUserProfile.getEmail())){
                    emailEditText.setText(responseUserProfile.getEmail());
                }
                if(Utils.isNullOrEmpty(responseUserProfile.getWebsite())){
                    webSiteEditText.setText(responseUserProfile.getWebsite());
                }
                if(Utils.isNullOrEmpty(responseUserProfile.getDescripcion())){
                    descriptionEditText.setText(responseUserProfile.getDescripcion());
                }
                if(Utils.isNullOrEmpty(responseUserProfile.getPhotoUrl())){
                    Glide.with(getActivity())
                            .load(Constants.PHOTO_URL
                                    + responseUserProfile.getPhotoUrl())
                            .dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(avatarProfileImageView);
                }
                if(!loadingData){
                    saveButton.setEnabled(true);
                    Toast.makeText(getActivity(), "Datos guardados correctamente",Toast.LENGTH_SHORT).show();

                }

            }
        });

        return view;
    }


    private void setViews(View view) {
        avatarProfileImageView = view.findViewById(R.id.imageViewAvatarProfile);
        userNameEditText = view.findViewById(R.id.editTextUserNameProfile);
        emailEditText = view.findViewById(R.id.editTextEmailProfile);
        webSiteEditText = view.findViewById(R.id.editTextWebsiteProfile);
        descriptionEditText = view.findViewById(R.id.editTextDescriptionProfile);
        passwordEditText= view.findViewById(R.id.editTextPasswordProfile);
        saveButton = view.findViewById(R.id.buttonSaveProfile);
        changePasswordButton = view.findViewById(R.id.buttonChangePasswordProfile);
    }

    private void eventViews() {
        saveButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
    }

    private void setAvatarImageView() {
        String photoUrl = SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO);
        if(photoUrl.isEmpty()){
            String photoUser = Constants.PHOTO_URL + photoUrl;
            Glide.with(this)
                    .load(photoUser)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avatarProfileImageView);
        }else{
            Glide.with(this)
                    .load(this.getResources().getDrawable(R.drawable.ic_mini_twitter_perfil))
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avatarProfileImageView);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.buttonSaveProfile:
                saveProfileData();
                break;
            case R.id.buttonChangePasswordProfile:
                Toast.makeText(getActivity(), "Botón cambiar contraseña",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveProfileData() {
        String username = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String descripcion = descriptionEditText.getText().toString();
        String website = webSiteEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(username.isEmpty()){
            userNameEditText.setError("El nombre de usuario es requerido");
        }else if(email.isEmpty()){
            emailEditText.setError("El email es requerido");
        }else if(password.isEmpty()){
            passwordEditText.setError("La contraseña es requerida");
        }else {
            RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
            profileViewModel.updateProfile(requestUserProfile);
            saveButton.setEnabled(false);
            Toast.makeText(getActivity(), "Guardando...",Toast.LENGTH_SHORT).show();
        }




    }
}
