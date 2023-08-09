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

import aarav.lju.app.MainActivity;
import aarav.lju.app.R;

public class LoginActivity extends AppCompatActivity {

    private TextView openReg, openForgetPass;
    private EditText logEmail, logPassword;
    private Button loginBtn;
    private String email, password;
    private FirebaseAuth auth;
    private ProgressDialog pd;
    ImageView btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        auth = FirebaseAuth.getInstance();

        openReg = findViewById(R.id.openReg);
        logEmail = findViewById(R.id.logEmail);
        logPassword = findViewById(R.id.logPass);
        loginBtn = findViewById(R.id.loginBtn);
        openForgetPass = findViewById(R.id.openForgetPass);
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
        googleSignInClient= GoogleSignIn.getClient(LoginActivity.this
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
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


        openReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        openForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });
    }

    private void validateData() {
        email = logEmail.getText().toString();
        password = logPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
        }else {

            loginUser();
        }
    }

    private void loginUser() {
        pd.setMessage("Logging In");
        pd.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    pd.dismiss();
                    openMain();
                }else {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "Error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openMain() {
        FirebaseUser loginUser = FirebaseAuth.getInstance().getCurrentUser();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
        Toast.makeText(LoginActivity.this, "Welcome To New LJIET"+" "+loginUser.getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    private void openRegister() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        pd.setMessage("Logging In");
        pd.show();
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
                pd.dismiss();
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
                        pd.setMessage("Logging In");
                        pd.show();
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if(task.isSuccessful())
                                        {
                                            // When task is successful
                                            // Redirect to profile activity
                                            startActivity(new Intent(LoginActivity.this
                                                    ,MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                            // Display Toast
                                            pd.dismiss();
                                            displayToast("Welcome back"+" "+user.getDisplayName());
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