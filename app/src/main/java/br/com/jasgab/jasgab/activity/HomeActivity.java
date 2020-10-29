package br.com.jasgab.jasgab.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.pattern.StatusLayoutType;
import br.com.jasgab.jasgab.util.CustomTypefaceSpan;
import br.com.jasgab.jasgab.crud.StatusDAO;
import br.com.jasgab.jasgab.fragment.BillsFragment;
import br.com.jasgab.jasgab.fragment.ServicesFragment;
import br.com.jasgab.jasgab.fragment.FAQFragment;
import br.com.jasgab.jasgab.fragment.HomeFragment;
import br.com.jasgab.jasgab.fragment.MoreFragment;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        JasgabUtils.hideActionBar(getActionBar());
        JasgabUtils.checkAPP(this);

        businessRules();
    }

    @Override
    public void onBackPressed() {
        StatusDAO.start(this).insert(StatusLayoutType.Overview);
        startActivity(new Intent(this, HomeActivity.class));
        finishAffinity();
    }

    private void businessRules(){
        bottomNavigation();

        StatusDAO.start(getApplicationContext()).delete();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new HomeFragment()).commit();
    }

    private void bottomNavigation(){
        BottomNavigationView home_nav = findViewById(R.id.home_nav);
        home_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_services:
                        selectedFragment = new ServicesFragment();
                        break;
                    case R.id.nav_bills:
                        selectedFragment = new BillsFragment();
                        break;
                    case R.id.nav_faq:
                        selectedFragment = new FAQFragment();
                        break;
                    case R.id.nav_more:
                        selectedFragment = new MoreFragment();
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, selectedFragment).commit();
                    return true;
                }
                return false;
            }
        });

        Menu m = home_nav.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        if(mi != null) {
            Typeface font = Typeface.createFromAsset(getAssets(), "segoe_ui.ttf");
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0);
            mi.setTitle(mNewTitle);
        }
    }
}
