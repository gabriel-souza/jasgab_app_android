package br.com.jasgab.jasgab.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.list.StatusOnlineTabPagerAdapter;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class StatusOnlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_online);
        JasgabUtils.hideActionBar(getSupportActionBar());

        loadLayout();
    }

    private void loadLayout(){
        ImageView status_online_back = findViewById(R.id.status_online_back);
        status_online_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StatusOnlineTabPagerAdapter adapter = new StatusOnlineTabPagerAdapter(this, getSupportFragmentManager());
        ViewPager status_online_tab = findViewById(R.id.status_online_tab);
        status_online_tab.setAdapter(adapter);
        TabLayout status_online_tabs = findViewById(R.id.status_online_tabs);
        status_online_tabs.setupWithViewPager(status_online_tab);

        TabLayout.Tab status_online_overview = status_online_tabs.getTabAt(0);
        TabLayout.Tab status_online_internet = status_online_tabs.getTabAt(1);
        TabLayout.Tab status_online_wifi = status_online_tabs.getTabAt(2);
        TabLayout.Tab status_online_devices = status_online_tabs.getTabAt(3);
        if(status_online_overview != null && status_online_internet != null && status_online_wifi != null && status_online_devices != null){
            status_online_overview.setIcon(R.drawable.status_online_overview);
            status_online_internet.setIcon(R.drawable.status_online_internet);
            status_online_wifi.setIcon(R.drawable.status_online_wifi);
            status_online_devices.setIcon(R.drawable.status_online_devices);
        }
    }
}