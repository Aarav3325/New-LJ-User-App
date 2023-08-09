package aarav.lju.app.ui.gallery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aarav.lju.app.R;

public class GalleryFragment extends Fragment {

    RecyclerView orRecycler, nvRecycler, othRecycler;
    GalleryAdapter adapter;

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        orRecycler = view.findViewById(R.id.orRecycler);
        nvRecycler = view.findViewById(R.id.nvRecycler);
        othRecycler = view.findViewById(R.id.othRecycler);

        reference = FirebaseDatabase.getInstance().getReference().child("gallery");

        getOrImage();
        getNvImage();
        getOthImage();


        return view;
    }

    private void getOthImage() {
        reference.child("Other Events").addValueEventListener(new ValueEventListener() {

            List<String> imageList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String data = (String) snapshot.getValue();
                    imageList.add(data);
                }

                adapter = new GalleryAdapter(getContext(), imageList);
                othRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
                othRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNvImage() {
        reference.child("Navratri Celebrations").addValueEventListener(new ValueEventListener() {

            List<String> imageList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> imageList =  new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String data = (String) snapshot.getValue();
                    imageList.add(data);
                }

                adapter = new GalleryAdapter(getContext(), imageList);
                nvRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
                nvRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrImage() {

        reference.child("Orientation").addValueEventListener(new ValueEventListener() {

            List<String> imageList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String data = (String) snapshot.getValue();
                    imageList.add(data);
                }

                adapter = new GalleryAdapter(getContext(), imageList);
                orRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
                orRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}