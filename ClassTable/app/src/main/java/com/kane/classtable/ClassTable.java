package com.kane.classtable;

import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.kane.module.GetCurrentDate;
import com.kane.module.myPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassTable extends AppCompatActivity {
    private TabLayout tabLayout;//this is to setup the tab on the top of the ViewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_table);


        mViewPagerInit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the ViewPager and add pages into the PagerAdapter
     */
    private void mViewPagerInit() {
        GetCurrentDate getToday = new GetCurrentDate();

/* Initialize the tool bar with tool bar title, sub tilte and logo*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getToday.getWeek());
        toolbar.setSubtitle(getToday.getDate());
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(Color.parseColor("#FF5B5E5A"), Color.parseColor("#FF08DA3C"));

       /* add 3 views into a view list and then send this list to Page adapter */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);


        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new ClassTableFragment());
        mFragments.add( new MemoFragment());
        mFragments.add(new SystemFragment());
        myPagerAdapter pagerAdapter = new myPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);

    }

}
