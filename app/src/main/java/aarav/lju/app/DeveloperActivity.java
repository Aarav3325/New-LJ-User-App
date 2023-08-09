package aarav.lju.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DeveloperActivity extends AppCompatActivity {

    private TextView mailUs;
    String mail = "mailto:aaravhalvadia@gmail.com";
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);


        mailUs = findViewById(R.id.mailUs);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeveloperActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mail));
                startActivity(i);
            }
        });
    }
}