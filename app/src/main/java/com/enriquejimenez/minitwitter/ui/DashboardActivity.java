package com.enriquejimenez.minitwitter.ui;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.enriquejimenez.minitwitter.ui.profile.ProfileFragment;
import com.enriquejimenez.minitwitter.ui.tweets.fragment.NewTweetDialogFragment;
import com.enriquejimenez.minitwitter.ui.tweets.fragment.TweetListFragment;
import com.enriquejimenez.minitwitter.utils.Constants;
import com.enriquejimenez.minitwitter.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.enriquejimenez.minitwitter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private ImageView avatarImageView;
    private TextView titleAppBar;

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

        setViews();
        setEvents();
    }

    private void setViews() {
        floatingActionButton = findViewById(R.id.fab);
        avatarImageView = findViewById(R.id.imageViewUserToolbar);
        titleAppBar = findViewById(R.id.textViewTitleToolBar);

        if(!SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO).isEmpty()) {
            String photoUser = Constants.PHOTO_URL + SharedPreferencesManager.getString(Constants.PREF_URL_PHOTO);
            Glide.with(this).load(photoUser).into(avatarImageView);
        }else{
            Glide.with(this)
                    .load(this.getResources().getDrawable(R.drawable.ic_mini_twitter_perfil))
                    .into(avatarImageView);
        }
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

}
