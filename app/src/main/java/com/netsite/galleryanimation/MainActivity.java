package com.netsite.galleryanimation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import coverflow.CoverFlow;
import coverflow.core.PagerContainer;

public class MainActivity extends AppCompatActivity {

    private PagerContainer pagerContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        pagerContainer = (PagerContainer) findViewById(R.id.pager_container);
        pagerContainer.setOverlapEnabled(true);

        final ViewPager viewPager = pagerContainer.getViewPager();
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.setAdapter(pagerAdapter);

        new CoverFlow.Builder().with(viewPager)
                .scale(0.3f)
                .pagerMargin(-200)//todo hard code
                .spaceSize(0)
                .build();

        //Manually setting the first View to be elevated
        viewPager.post(new Runnable() {
            @Override public void run() {
                Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                ViewCompat.setElevation(fragment.getView(), 8.0f);
            }
        });
    }

    public void pagerIn(){
        pagerContainer.postInvalidate();
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            return OverlapFragment.newInstance(DemoData.covers[position]);
        }

        @Override public int getCount() {
            return DemoData.covers.length;
        }
    }
}
