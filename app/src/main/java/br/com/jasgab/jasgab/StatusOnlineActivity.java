package br.com.jasgab.jasgab;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import br.com.jasgab.jasgab.adapter.StatusOnlineTabPagerAdapter;

public class StatusOnlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_online);
        StatusOnlineTabPagerAdapter statusOnlineTabPagerAdapter = new StatusOnlineTabPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(statusOnlineTabPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_status_online_overview);
        tabs.getTabAt(1).setIcon(R.drawable.ic_internet);
        tabs.getTabAt(2).setIcon(R.drawable.ic_wifi);
        tabs.getTabAt(3).setIcon(R.drawable.ic_devices);
    }
}