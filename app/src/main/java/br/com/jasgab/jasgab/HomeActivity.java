package br.com.jasgab.jasgab;

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

import br.com.jasgab.jasgab.api.CustomTypefaceSpan;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.crud.StatusDAO;
import br.com.jasgab.jasgab.fragment.BillsFragment;
import br.com.jasgab.jasgab.fragment.ContractsFragment;
import br.com.jasgab.jasgab.fragment.FAQFragment;
import br.com.jasgab.jasgab.fragment.HomeFragment;
import br.com.jasgab.jasgab.fragment.MoreFragment;

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(CustomerDAO.start(this).selectCustomer() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
            return;
        }

        bottomNavigation();

        if (savedInstanceState == null) {
            StatusDAO.start(getApplicationContext()).delete();
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new HomeFragment()).commit();
        }
    }

    private void bottomNavigation(){
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_home);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_contracts:
                        selectedFragment = new ContractsFragment();
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

        Menu m = bottomNav.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "roboto_regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0);// Use this if you want to center the items
        mi.setTitle(mNewTitle);
    }
}
