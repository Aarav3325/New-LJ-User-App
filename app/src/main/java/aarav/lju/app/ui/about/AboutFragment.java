package aarav.lju.app.ui.about;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import aarav.lju.app.CoursesTab;
import aarav.lju.app.R;

public class AboutFragment extends Fragment {

    private ViewPager viewPager;
    private CourseAdapter adapter;
    private List<CourseModal> list;
    /*private Button nav;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        list = new ArrayList<>();
        list.add(new CourseModal(R.drawable.ic_baseline_computer_24, "Computer Science & Engineering", "The program focuses on providing the students with the necessary skills to be effective contributors in a quality-oriented."));
        list.add(new CourseModal(R.drawable.ic_baseline_computer_24, "Computer Science & Engineering AI-ML", "The curriculum of this course is specially designed to teach the students various computer algorithms and applications that improves the accuracy of the devices beyond limits."));
        /*list.add(new CourseModal(R.drawable.ic_baseline_computer_24, "Computer Engineering", "The curriculum of this course is specially designed to teach the students various computer algorithms and applications that improves the accuracy of the devices beyond limits."));
*/
        adapter = new CourseAdapter(getContext(), list);

        viewPager = view.findViewById(R.id.courses);

        /*nav = view.findViewById(R.id.nav);

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });*/

        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CoursesTab.class));
            }
        });


        viewPager.setAdapter(adapter);

        ImageView imageView = view.findViewById(R.id.college_image);

        Glide.with(getContext()).load("https://www.ljku.edu.in/web/image/course.program/11/banner_image").into(imageView);

        return view;
    }
}