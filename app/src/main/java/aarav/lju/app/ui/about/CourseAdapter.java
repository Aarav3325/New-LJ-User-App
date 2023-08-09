package aarav.lju.app.ui.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import aarav.lju.app.R;

public class CourseAdapter extends PagerAdapter {

    private Context context;
    private List<CourseModal> list;

    public CourseAdapter(Context context, List<CourseModal> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.course_item_layout, container, false);

        ImageView courseIcon;
        TextView courseTitle, courseDes;

        courseIcon = view.findViewById(R.id.courses_icon);
        courseTitle = view.findViewById(R.id.course_title);
        courseDes = view.findViewById(R.id.course_des);

        courseIcon.setImageResource(list.get(position).getImg());

        courseTitle.setText(list.get(position).getTitle());
        courseDes.setText(list.get(position).getDescription());

        container.addView(view, 0);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
