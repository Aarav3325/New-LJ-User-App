package aarav.lju.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppInfoActivity extends AppCompatActivity {

    private ImageView close;
    private TextView appVersion;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info_updated);

        close = findViewById(R.id.close);
        appVersion = findViewById(R.id.appVersion);

        appVersion.setText("v" + versionName);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AppInfoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

       // FirebaseDatabase.getInstance().getReference().child("User App").setValue("Version 1.0.beta-release");
        //firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("/Version/User App");

        //getVersion();
    }

   /*private void getVersion() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                appVersion.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppInfoActivity.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}