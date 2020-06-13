package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.jasgab.jasgab.R;

public class FirstAccessFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_access, container, false);

        Button start = view.findViewById(R.id.firstAccess_start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdentifyFragment identifyFragment = new IdentifyFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_first_access, identifyFragment)
                        .commit();
            }
        });

        return view;
    }
}
