package aarav.lju.app.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import aarav.lju.app.MainActivity;
import aarav.lju.app.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText regName, regEmail, regPassword;
    private Button register;
    private String name, email, pass;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private DatabaseReference dbRef;
    private TextView openLog;
    private ProgressDialog pd;
    private ImageView google;
    ImageView btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPass);
        register = findViewById(R.id.register);
        openLog = findViewById(R.id.openLog);
        btSignIn=findViewById(R.id.bt_sign_in);
        pd = new ProgressDialog(this);

        // Initialize sign in options
        // the client-id is copied form
        // google-services.json file
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("830215657972-v9ka9e7ctl7ma6ht0j4cgfs3mn0kt2r2.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(RegisterActivity.this
                ,googleSignInOptions);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize sign in intent
                Intent intent=googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent,100);
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        // Check condition
        if(firebaseUser!=null)
        {
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(RegisterActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }



        openLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }


    private void openLogin() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            openMain()
            ;        }
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void validateData() {
        name = regName.getText().toString();
        email = regEmail.getText().toString();
        pass = regPassword.getText().toString();
        
        if (name.isEmpty()){
            regName.setError("Required");
            regName.requestFocus();
        }else if (email.isEmpty()){
            regEmail.setError("Required");
            regEmail.requestFocus();
        }else if (pass.isEmpty()){
            regPassword.setError("Required");
            regPassword.requestFocus();
        }else {

            createUser();
        }
    }

    private void createUser() {
        pd.setMessage("Registering User");
        pd.show();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    pd.dismiss();
                    uploadData();
                }else {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Error :" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        dbRef = reference.child("Users");
        String key = dbRef.push().getKey();

        HashMap<String, String> user = new HashMap<>();
        user.put("key", key);
        user.put("name", name);
        user.put("email", email);
        user.put("status", "no");

        dbRef.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Welcome To New LJIET"+" "+name, Toast.LENGTH_SHORT).show();

                    updateUser();
                    openMain();
                }else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUser() {

        UserProfileChangeRequest profileChangeRequest = new  UserProfileChangeRequest.Builder().setDisplayName(name).build();
        auth.getCurrentUser().updateProfile(profileChangeRequest);
        openLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if(requestCode==100)
        {
            // When request code is equal to 100
            // Initialize task
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            // check condition
            if(signInAccountTask.isSuccessful())
            {
                // When google sign in successful
                // Initialize string
                String s="Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    // Check condition
                    if(googleSignInAccount!=null)
                    {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        if(task.isSuccessful())
                                        {
                                            // When task is successful
                                            // Redirect to profile activity
                                            startActivity(new Intent(RegisterActivity.this
                                                    ,MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                            // Display Toast
                                            displayToast("Firebase authentication successful");
                                        }
                                        else
                                        {
                                            // When task is unsuccessful
                                            // Display Toast
                                            displayToast("Authentication Failed :"+task.getException()
                                                    .getMessage());
                                        }
                                    }
                                });

                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

}