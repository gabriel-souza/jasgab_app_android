package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.StatusType;
import br.com.jasgab.jasgab.crud.StatusDAO;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Integer status = StatusDAO.start(requireContext()).select();

        Fragment fragment;
        switch (status){
            case StatusType.Blocked :
                fragment = new StatusBlockedFragment();
                break;
            case StatusType.Maintenance :
                fragment = new StatusMaintenanceFragment();
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