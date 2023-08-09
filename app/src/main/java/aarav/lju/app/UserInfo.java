package aarav.lju.app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import aarav.lju.app.authentication.ForgetPasswordActivity;
import aarav.lju.app.authentication.LoginActivity;
import aarav.lju.app.ui.notice.NoticeData;

public class UserInfo extends AppCompatActivity {

    String image;
    ImageView ivImage;
    TextView tvName, tvEmail, fname, provider, verified;
    CardView btLogout, profileFg, update, verifyEmail, uploadImage, snackbar;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    String name;
    //ArrayList<UserData> list;
    private ProgressDialog pd;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private DatabaseReference reference, databaseReference;
    private StorageReference storageReference;
    String downloadUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ivImage = findViewById(R.id.iv_image);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        btLogout = findViewById(R.id.bt_logout);
        profileFg = findViewById(R.id.profileFg);
        verified = findViewById(R.id.verified);
        //provider = findViewById(R.id.provider);
        verifyEmail = findViewById(R.id.verifyEmail);
        snackbar = findViewById(R.id.snackbar_clear_cache);

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        snackbar.setVisibility(View.GONE);

        snackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAppData();
                Snackbar.make(snackbar, "Message is restored", Snackbar.LENGTH_LONG).show();
            }
        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserInfo.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        // Check condition
        if (firebaseUser != null) {
            // When firebase user is not equal to null
            // Set image on image view
            Glide.with(UserInfo.this)
                    .load(firebaseUser.getPhotoUrl()).placeholder(R.drawable.ic_avtar)
                    .into(ivImage);
            // set name on text view
            tvName.setText(firebaseUser.getDisplayName());
            tvEmail.setText(firebaseUser.getEmail());
            tvName.setText(firebaseAuth.getCurrentUser().getDisplayName());

            ImageView verifyIcon, notVerify;
            verifyIcon = findViewById(R.id.verifyIcon);
            notVerify = findViewById(R.id.notVerified);
            if (firebaseUser.isEmailVerified()){
                verified.setText("Email Verified");
                verifyIcon.setVisibility(View.VISIBLE);
                notVerify.setVisibility(View.GONE);
                verifyEmail.setVisibility(View.GONE);
            }else {
                verified.setText("Email Verification Pending");
                verifyIcon.setVisibility(View.GONE);
                notVerify.setVisibility(View.VISIBLE);

            }


        }




    //getSupportActionBar().setTitle(firebaseUser.getDisplayName());
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.new_lj_logo_removebg);

        profileFg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInfo.this, ForgetPasswordActivity.class);
                startActivity(i);
            }
        });


        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(UserInfo.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out from google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Check condition
                        if (task.isSuccessful()) {
                            // When task is successful
                            // Sign out from firebase
                            firebaseAuth.signOut();

                            // Display Toast
                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();

                            // Finish activity
                            finish();
                        }
                    }
                });
            }
        });



        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            getUserProfile();
        } else {
            // No user is signed in
        }

    }

   private void getUserProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            tvEmail.setText(email);
            tvName.setText(user.getDisplayName());


            //reference = databaseReference.child("name");

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

        }*/

    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear "+packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
