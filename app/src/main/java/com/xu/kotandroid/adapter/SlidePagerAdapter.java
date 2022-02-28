package com.xu.kotandroid.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @Author Xu
 * Date：2021/7/7 11:03 上午
 * Description：
 */
public class SlidePagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList;

    public SlidePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public SlidePagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public void setData(List<Fragment> fragmentList) {

        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragmentList != null) {
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    public Fragment getFragment(int position) {
        if (!fragmentList.isEmpty()) {
            return fragmentList.get(position);
        }

        return null;
    }

}
