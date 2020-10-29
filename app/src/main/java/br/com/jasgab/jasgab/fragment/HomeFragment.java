package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.jasgab.jasgab.R;

public class HomeFragment extends Fragment {
    public final static int OVERVIEW = 0;
    public final static int BLOCKED = 1;
    public final static int MAINTENANCE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        businessRules();

        return view;
    }

    private void businessRules(){
        int status = OVERVIEW;
        Bundle bundle = getArguments();
        if(bundle != null){
            status = bundle.getInt("status", OVERVIEW);
        }

        Fragment fragment;
        switch (status){
            case BLOCKED :
                fragment = new StatusBlockedFragment();
                break;
            case MAINTENANCE :
                fragment = new StatusOfflineFragment();
                break;
            default:
                fragment = new OverviewFragment();
        }

        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).addToBackStack(null).commit();
    }
}