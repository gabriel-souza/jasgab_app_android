package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.model.Device;

public class FAQFragment extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    ArrayList<Device> mHosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_duvida, container, false);

        return view;
    }

}
