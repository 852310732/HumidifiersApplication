package com.wlp.humidifier;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public  class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentTitle = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);//添加到List中
        mFragmentTitle.add(title);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
    //这个方法是用来设置tab的name的
    @Override
    public CharSequence getPageTitle(int position) {
        //从添加后的list中取数据并返回
        return mFragmentTitle.get(position);
    }
}



