package com.example.parktaeim.seoulwithyou.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.parktaeim.seoulwithyou.Adapter.ViewPagerAdapter;
import com.example.parktaeim.seoulwithyou.R;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        tabLayout.addTab(tabLayout.newTab().setText("음식"));
        tabLayout.addTab(tabLayout.newTab().setText("전통/역사"));
        tabLayout.addTab(tabLayout.newTab().setText("현대"));
        tabLayout.addTab(tabLayout.newTab().setText("예술/문화"));
        tabLayout.addTab(tabLayout.newTab().setText("힐링"));

        Log.d(getPackageName(), "packageName");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}