package aarav.lju.app.ui.faculty;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aarav.lju.app.R;

public class FacultyFragment extends Fragment {

    private RecyclerView ceDepartment, cseDepartment, cseaiDepartment, itDepartment;
    private LinearLayout ceNoData, cseNoData, cseaiNoData, itNoData;
    private List<TeacherData> list1 ,list2, list3, list4;
    private TeacherAdapter adapter;
    private ProgressBar progressBar;

    private DatabaseReference reference, dbRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);
        ceDepartment = view.findViewById(R.id.ceDepartment);
        cseDepartment = view.findViewById(R.id.cseDepartment);
        cseaiDepartment = view.findViewById(R.id.cseaiDepartment);
        itDepartment = view.findViewById(R.id.itDepartment);

        itNoData = view.findViewById(R.id.itNoData);
        cseNoData = view.findViewById(R.id.cseNoData);
        cseaiNoData = view.findViewById(R.id.cseaiNoData);
        ceNoData = view.findViewById(R.id.ceNoData);
        progressBar = view.findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");

        ceDepartment();
        cseDepartment();
        cseaiDepartment();
        itDepartment();

        return view;
    }

    private void ceDepartment() {

        dbRef = reference.child("CE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    ceNoData.setVisibility(View.VISIBLE);
                    ceDepartment.setVisibility(View.GONE);
                }else {

                    /*ceNoData.setVisibility(View.GONE);
                    ceDepartment.setVisibility(View.VISIBLE);*/
                    ceNoData.setVisibility(View.INVISIBLE);
                    ceDepartment.setVisibility(View.INVISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    ceDepartment.setHasFixedSize(true);
                    ceDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list1, getContext());
                    progressBar.setVisibility(View.GONE);
                    ceDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cseDepartment() {

        dbRef = reference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    cseNoData.setVisibility(View.VISIBLE);
                    cseDepartment.setVisibility(View.GONE);
                }else {

                    cseNoData.setVisibility(View.GONE);
                    cseDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    cseDepartment.setHasFixedSize(true);
                    cseDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list2, getContext());
                    progressBar.setVisibility(View.GONE);
                    cseDepartment.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cseaiDepartment() {

        dbRef = reference.child("CSE(AI-ML)");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    cseaiNoData.setVisibility(View.VISIBLE);
                    cseaiDepartment.setVisibility(View.GONE);
                }else {

                    cseaiNoData.setVisibility(View.GONE);
                    cseaiDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    cseaiDepartment.setHasFixedSize(true);
                    cseaiDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list3, getContext());
                    progressBar.setVisibility(View.GONE);
                    cseaiDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void itDepartment() {

        dbRef = reference.child("IT");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    itNoData.setVisibility(View.INVISIBLE);
                    itDepartment.setVisibility(View.GONE);
                }else {

                    itNoData.setVisibility(View.GONE);
                    itDepartment.setVisibility(View.GONE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    itDepartment.setHasFixedSize(true);
                    itDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    progressBar.setVisibility(View.GONE);
                    itDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}