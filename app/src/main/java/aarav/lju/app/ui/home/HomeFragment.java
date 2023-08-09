package aarav.lju.app.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;

import aarav.lju.app.CoursesTab;
import aarav.lju.app.R;
import aarav.lju.app.fragment.OneFragment;
import aarav.lju.app.fragment.TwoFragment;


public class HomeFragment extends Fragment {

    private ImageView map, map2;
    private TextView tabActivity, tabActivity2, tabActivity3, tfirebase;
    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;

    // Urls of our images.
    String url2 = "https://www.ljku.edu.in/web/image/image.slider/38/home_page_banner";
    String url3 = "https://www.ljku.edu.in/web/image/image.slider/32/home_page_banner";
    String url4 = "https://www.ljku.edu.in/web/image/image.slider/34/home_page_banner";
    String url5 = "https://www.ljku.edu.in/web/image/image.slider/36/home_page_banner";
    String url1 = "https://www.ljku.edu.in/web/image/course.program/11/banner_image";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("random");
        myRef.setValue("Test Message");*/
        tfirebase = view.findViewById(R.id.tfirebase);
        tabActivity = view.findViewById(R.id.tabActivity);
        tabActivity2 = view.findViewById(R.id.tabActivity2);
        tabActivity3 = view.findViewById(R.id.tabActivity3);
        SliderView sliderView = view.findViewById(R.id.slider);


        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("Home");

        // initializing our object class variable.

        getData();



        //FirebaseDatabase.getInstance().getReference().child("Home").setValue("New LJIET");
        //FirebaseDatabase.getInstance().getReference().child("Home").setValue(m);

        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("title", tfirebase.getText().toString());

        /*ArrayList<String> a = new ArrayList<>();
        ArrayAdapter adapter1 = new ArrayAdapter<String>(getContext(), R.layout.fragment_home, a);
        tfirebase.setAdapter*/

        tabActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CoursesTab.class));
                /*AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new TwoFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myFragment).addToBackStack(null).commit();*/
            }
        });


        tabActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(getContext(), CoursesTab.class));
            }
        });

        tabActivity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CoursesTab.class));
            }
        });

        /*map= view.findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });*/

        map2= view.findViewById(R.id.map2);
        map2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap2();
            }
        });

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        sliderDataArrayList.add(new SliderData(url4));
        sliderDataArrayList.add(new SliderData(url5));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(4);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        return view;
    }

    private void getData() {
        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String value = snapshot.getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.
                tfirebase.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void openMap2() {
        Uri uri = Uri.parse("https://goo.gl/maps/XL7zVLSwESBAQVtR9");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    /*private void openMap() {
        /*Uri uri = Uri.parse("geo:0, 0?q=LJ University");
        Uri uri = Uri.parse("https://goo.gl/maps/VVAXi4VTWNntBdHT7");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }*/
}



