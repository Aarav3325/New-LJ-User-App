package aarav.lju.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import aarav.lju.app.databinding.ActivityCoursesTabBinding;
import aarav.lju.app.fragment.OneFragment;
import aarav.lju.app.fragment.TwoFragment;

public class CoursesTab extends AppCompatActivity {

    private ActivityCoursesTabBinding binding;
    private CoursesTab activity;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_courses_tab);

        getSupportActionBar().setTitle("Courses Offered");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.new_lj_logo_removebg);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_courses_tab);
        activity = this;

        initView();
    }

    private void initView() {
        setupViewPager(binding.viewPager);
//        binding.tabLayout.setupWithViewPager(binding.viewPager);


        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position)-> {tab.setText(adapter.mFragmentTitleList.get(position));
//                tab.setCustomView(R.layout.custom_tab);
                }).attach();

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++){

            TextView tv = (TextView) LayoutInflater.from(activity)
                    .inflate(R.layout.custom_tab, null);

            binding.tabLayout.getTabAt(i).setCustomView(tv);
        }
    }

    private void setupViewPager(ViewPager2 viewPager) {
        adapter =new ViewPagerAdapter(activity.getSupportFragmentManager(),
                activity.getLifecycle()     );
        adapter.addFragment(new OneFragment(), "CSE");
        adapter.addFragment(new TwoFragment(), "CSE(AI-ML)");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }



    class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String>mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }
}