package aarav.lju.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import aarav.lju.app.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextView openLog;
    private Button forgetBtn;
    private EditText txtEmail;
    private String email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        auth = FirebaseAuth.getInstance();

        openLog = findViewById(R.id.openLog);
        txtEmail = findViewById(R.id.forEmail);
        forgetBtn = findViewById(R.id.forgetBtn);

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        openLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    private void validateData() {
        email = txtEmail.getText().toString();
        if (email.isEmpty()){
            txtEmail.setError("Required");
            txtEmail.requestFocus();
        }else {
            forgetPassword();
        }
    }

    private void forgetPassword() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar.make(forgetBtn, "Check spam folder too", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(ForgetPasswordActivity.this, "Reset Link Sent On Your Email.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}