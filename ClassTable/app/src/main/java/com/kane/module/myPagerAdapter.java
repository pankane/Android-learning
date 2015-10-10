package com.kane.module;

/**
 * Created by kane on 2015/10/10.
 */


import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.view.View;


public class myPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;//to store the fragments  into this list
    private List<String> mTitleList = new ArrayList<>();//to store tab title into this list


    public myPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        mTitleList.add("课程表");
        mTitleList.add("备忘录");
        mTitleList.add("系统");
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
