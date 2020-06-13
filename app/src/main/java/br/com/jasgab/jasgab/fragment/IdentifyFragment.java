package br.com.jasgab.jasgab.fragment;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.jasgab.jasgab.LoginActivity;
import br.com.jasgab.jasgab.R;

public class IdentifyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identify, container, false);

        Button button_yes = view.findViewById(R.id.identify_yes);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return view;
    }
}
