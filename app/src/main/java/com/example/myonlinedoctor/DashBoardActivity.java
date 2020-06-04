package com.example.myonlinedoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashBoardActivity extends AppCompatActivity {



    FirebaseAuth firebaseAuth ;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemReselectedListener((BottomNavigationView.OnNavigationItemReselectedListener) selectedListener);

        //Default Fragment
        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            //home Fragment Transaction
                            actionBar.setTitle("Home");
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1, "");
                            ft1.commit();
                            return true;

                        case R.id.nav_profile:
                            //Profile Fragment Transaction
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();
                            return true;

                        case R.id.nav_users:
                            //Users Fragment Transaction
                            actionBar.setTitle("Users");
                            UsersFragment fragment3 = new UsersFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3, "");
                            ft3.commit();
                            return true;
                    }

                    return false;
                }
            };

    private void checkUserStatuse(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if( user != null){


        }
        else {
            startActivity(new Intent(DashBoardActivity.this , MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onStart() {
        checkUserStatuse();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         int id = item.getItemId();
         if( id == R.id.action_logout) {
             firebaseAuth.signOut();
             checkUserStatuse();
         }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
