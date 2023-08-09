package aarav.lju.app.ebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aarav.lju.app.R;

public class EbookActivity extends AppCompatActivity {

    private RecyclerView ebookRecyclerView;
    private DatabaseReference reference;
    private List<EbookData> list;
    private EbookAdapter adapter;
    EditText search;

    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout shimmerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);


        getSupportActionBar().setTitle("Ebooks");
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ebookRecyclerView = findViewById(R.id.ebookRecycler);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerLayout = findViewById(R.id.shimmerLayout);
        search = findViewById(R.id.searchText);
        reference = FirebaseDatabase.getInstance().getReference().child("Pdf");

        getData();

    }

    /*private void getDynamicLinkFromFirebase() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Log.i("Ebbok Activity", "We have a Dynamic Link.");
                Uri deepLink = null;

                if (pendingDynamicLinkData != null){
                    deepLink = pendingDynamicLinkData.getLink();
                }
                if (deepLink != null){
                    Log.i("Ebook Activity", "Here the Dynamic Link \n" + deepLink.toString());

                    String email = deepLink.getQueryParameter("email");
                    String password = deepLink.getQueryParameter("password");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EbookActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    EbookData data = snapshot.getValue(EbookData.class);
                    list.add(data);
                }
                adapter = new EbookAdapter(EbookActivity.this, list);
                ebookRecyclerView.setLayoutManager(new LinearLayoutManager(EbookActivity.this));
                ebookRecyclerView.setAdapter(adapter);
                shimmerFrameLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EbookActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<EbookData> filterlist = new ArrayList<>();
        for (EbookData item : list) {
            if (item.getPdfTitle().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(item);
            }
        }
        adapter.Filteredlist(filterlist);
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        shimmerFrameLayout.startShimmer();
        super.onResume();
    }
}