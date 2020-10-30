package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class FAQFragment extends Fragment {
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faq, container, false);

        JasgabUtils.checkAPP(requireActivity());
        JasgabUtils.setActionBarHome("Dúvida", view, requireContext(), requireActivity());

        return view;
    }


}
