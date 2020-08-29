package br.com.jasgab.jasgab.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.fragment.StatusOnlineDevicesFragment;
import br.com.jasgab.jasgab.fragment.StatusOnlineInternetFragment;
import br.com.jasgab.jasgab.fragment.StatusOnlineOverviewFragment;
import br.com.jasgab.jasgab.fragment.StatusOnlineWifiFragment;

public class StatusOnlineTabPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public StatusOnlineTabPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new StatusOnlineOverviewFragment();

        switch (position){
            case 0:
                fragment = new StatusOnlineOverviewFragment();
                break;
            case 1:
                fragment = new StatusOnlineInternetFragment();
                break;
            case 2:
                fragment = new StatusOnlineWifiFragment();
                break;
            case 3:
                fragment = new StatusOnlineDevicesFragment();
                break;
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return 4;
    }
}