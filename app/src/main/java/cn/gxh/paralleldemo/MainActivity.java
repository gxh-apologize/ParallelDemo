package cn.gxh.paralleldemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private int[] layouts = {
            R.layout.welcome1,
            R.layout.welcome2,
            R.layout.welcome3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        WelcomePagerAdater adater=new WelcomePagerAdater(getSupportFragmentManager());
        mViewPager.setAdapter(adater);

        WelcomePagerTransformer transformer=new WelcomePagerTransformer();
        mViewPager.setOnPageChangeListener(transformer);
        mViewPager.setPageTransformer(true,transformer);
    }


    class WelcomePagerAdater extends FragmentPagerAdapter {

        public WelcomePagerAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new TranslateFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("layoutId",layouts[position]);
            bundle.putInt("pageIndex",position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }
    }
}
