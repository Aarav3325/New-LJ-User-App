package aarav.lju.app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import aarav.lju.app.authentication.LoginActivity;
import aarav.lju.app.ebook.EbookActivity;
import aarav.lju.app.fragment.TwoFragment;
import aarav.lju.app.ui.notice.NoticeData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private ImageView drawerUserImage, userProfile;
    private TextView drawerUserName, drawerUserEmail, drawerVersion;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerVersion = findViewById(R.id.drawerVersion);
        drawerVersion.setText("v" + BuildConfig.VERSION_NAME);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            //drawerVersion.setText(firebaseUser.getDisplayName());
        }



        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/"))
                .setDomainUriPrefix("https://lju.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            startActivity(new Intent(MainActivity.this, EbookActivity.class));
                        }
                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("Notification");
        /*FirebaseMessaging.getInstance().unsubscribeFromTopic("Notification");*/

        auth = FirebaseAuth.getInstance();

                /*getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE |ActionBar.DISPLAY_SHOW_HOME |
                        ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setLogo(R.drawable.new_lj_logo_removebg);*/

        /*getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.frame_layout);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        userProfile = findViewById(R.id.userProfile);

        navigationView.setItemIconTintList(null);
        bottomNavigationView.setItemIconTintList(null);

        if (firebaseUser != null) {
            // When firebase user is not equal to null
            // Set image on image view
            Glide.with(MainActivity.this)
                    .load(firebaseUser.getPhotoUrl()).placeholder(R.drawable.ic_avtar)
                    .into(userProfile);
            // set name on text view

        }


        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserInfo.class);
                startActivity(intent);
                }
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.star, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        if (item.getItemId() == R.id.logout){
            auth.signOut();
            openLogin();
        }

        switch (item.getItemId()){
            case R.id.option_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "LJU User App");
                    i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
                    startActivity(Intent.createChooser(i,"Share With"));
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to share the app", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.app_info:
                startActivity(new Intent(MainActivity.this, AppInfoActivity.class));
                finish();
                break;

            case R.id.developer:
                startActivity(new Intent(MainActivity.this, DeveloperActivity.class));
                finish();
                break;
        }
        return true;
    }

    private void openLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null){
            openLogin();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_developer:
                startActivity(new Intent(this, DeveloperActivity.class));
                finish();
                break;

            case R.id.navigation_video:
                startActivity(new Intent(this, AppInfoActivity.class));
                finish();
                break;

            /*case R.id.navigation_rate:
                Toast.makeText(this, "Rate", Toast.LENGTH_SHORT).show();
                break;*/

            case R.id.navigation_ebook:
                startActivity(new Intent(this, EbookActivity.class));
                break;

            case R.id.navigation_theme:
                startActivity(new Intent(this, UserInfo.class));
                break;

            case R.id.navigation_website:
                Uri uri = Uri.parse("https://www.ljku.edu.in/program/engineering-new-lj-11");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Url not found\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.navigation_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "New LJIET");
                    i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
                    startActivity(Intent.createChooser(i,"Share With"));
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to share the app", Toast.LENGTH_SHORT).show();
                }
                break;

                /*case R.id.navigation_color:

                break;*/



        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
}