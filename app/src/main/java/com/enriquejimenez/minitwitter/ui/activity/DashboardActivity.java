package com.enriquejimenez.minitwitter.ui.activity;

import android.os.Bundle;

import com.enriquejimenez.minitwitter.ui.fragment.NewTweetDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.enriquejimenez.minitwitter.R;
import com.enriquejimenez.minitwitter.ui.fragment.TweetListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new TweetListFragment());

                    return true;
                case R.id.navigation_favs:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new TweetListFragment());

                    return true;
                case R.id.navigation_profile:
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new TweetListFragment()).commit();

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTweetDialogFragment dialog = new NewTweetDialogFragment();
                dialog.show(getSupportFragmentManager(), "NewTweetDialogFragmet");
            }
        });

    }

}
