package com.enriquejimenez.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.mvvm.profile.ProfileViewModel;
import com.enriquejimenez.minitwitter.ui.profile.ProfileFragment;
import com.enriquejimenez.minitwitter.ui.tweets.fragment.NewTweetDialogFragment;
import com.enriquejimenez.minitwitter.ui.tweets.fragment.TweetListFragment;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private FloatingActionButton floatingActionButton;
    private ImageView avatarImageView;
    private TextView titleAppBar;
    ProfileViewModel profileViewModel;
    

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_ALL);
                    floatingActionButton.show();
                    titleAppBar.setText(getResources().getString(R.string.toolbar_text_home));
                    break;
                case R.id.navigation_favs:
                    fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_FAVS);
                    floatingActionButton.hide();
                    titleAppBar.setText(getResources().getString(R.string.toolbar_text_favs));
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    floatingActionButton.hide();
                    titleAppBar.setText(getResources().getString(R.string.toolbar_text_profile));
                    break;
            }
            if(fragment != null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,
                                fragment)
                        .commit();
                return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().hide();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer,
                        TweetListFragment.newInstance(Constants.TWEET_LIST_ALL))
                .commit();

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        setViews();
        setEvents();
    }

    private void setViews() {
        floatingActionButton = findViewById(R.id.fab);
        avatarImageView = findViewById(R.id.imageViewUserToolbar);
        titleAppBar = findViewById(R.id.textViewTitleToolBar);


        // Seteamos la imagen del usuario de perfil
        String photoUrl = SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO);
        if(!photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constants.PHOTO_URL + photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(avatarImageView);
        }

        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String photo) {
                Glide.with(DashboardActivity.this)
                        .load(Constants.PHOTO_URL + photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(avatarImageView);
            }
        });

        /*if(!SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO).isEmpty()) {
            String photoUser = Constants.PHOTO_URL + SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO);
            Glide.with(this)
                    .load(photoUser)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avatarImageView);
        }else{
            Glide.with(this)
                    .load(this.getResources().getDrawable(R.drawable.ic_mini_twitter_perfil))
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(avatarImageView);
        }*/
    }
    public void setEvents(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTweetDialogFragment dialog = new NewTweetDialogFragment();
                dialog.show(getSupportFragmentManager(), "NewTweetDialogFragmet");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if(requestCode == Constants.SELECT_PHOTO_GALLERY) {
                if(data != null) {
                    Uri imagenSeleccionada = data.getData(); // content://gallery/photos/..
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imagenSeleccionada,
                            filePathColumn, null, null, null);
                    if(cursor != null) {
                        cursor.moveToFirst();
                        // "filename" = filePathColumn[0]
                        int imagenIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String fotoPath = cursor.getString(imagenIndex);
                        profileViewModel.uploadPhoto(fotoPath);
                        cursor.close();
                    }

                }
            }
        }
        /*//PARA RECOGER LA URL DEL PATH DE LA FOTO SELECCIONADA
        if(resultCode != RESULT_CANCELED){
            if(requestCode == Constants.SELECT_PHOTO_GALLERY){
                if(data != null){
                    //PARA RESOLVER QUE PARTE DEL PATH TENGO QUE COGER PARA
                    //SUBIR LA FOTO
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(
                            selectedImage,filePathColumn
                            ,null,null,null);
                    if(cursor != null){
                        cursor.moveToFirst();
                        int imageIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String photoPath = cursor.getString(imageIndex).trim();
                        profileViewModel.uploadPhoto(photoPath);
                        cursor.close();
                    }
                }
            }
        }*/
    }

    //PERMISSION LISTENER

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        //Abrimos selecciónde fotos de la gallery
        Intent selectPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPhoto, Constants.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Toast.makeText(DashboardActivity.this, "Es necesario que aceptes el permiso para poder subir la fotografía", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }
}
