package aarav.lju.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aarav.lju.app.ui.notice.NoticeAdapter;
import aarav.lju.app.ui.notice.NoticeData;

public class FullImageView extends AppCompatActivity {

    private PhotoView imageView;
    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);

        getSupportActionBar().setTitle(" ");


        imageView = findViewById(R.id.imageView);

        String image = getIntent().getStringExtra("image");

        Glide.with(this).load(image).into(imageView);
    }
}