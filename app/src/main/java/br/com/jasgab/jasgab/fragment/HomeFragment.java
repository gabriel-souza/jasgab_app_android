package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.pattern.StatusLayoutType;
import br.com.jasgab.jasgab.crud.StatusDAO;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Integer status = StatusDAO.start(requireContext()).select();

        Fragment fragment;
        switch (status){
            case StatusLayoutType.Blocked :
                fragment = new StatusBlockedFragment();
                break;
            case StatusLayoutType.Maintenance :
                fragment = new StatusOfflineFragment();
                break;
            default:
                fragment = new OverviewFragment();
        }

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_container, fragment)
                .addToBackStack(null)
                .commit();

        return view;
    }
}