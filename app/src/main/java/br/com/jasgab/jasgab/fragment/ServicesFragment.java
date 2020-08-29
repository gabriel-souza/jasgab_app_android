package br.com.jasgab.jasgab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.com.jasgab.jasgab.PlansActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.UnderConstructionActivity;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class ServicesFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_services, container, false);

        setLayout();

        return view;
    }

    private void setLayout(){
        //ACTIONBAR
        JasgabUtils.setActionBar("Serviços", view);

        ImageView services_plan = view.findViewById(R.id.services_plan);
        services_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), PlansActivity.class);
                requireActivity().startActivity(intent);
            }
        });

        //Under Construction
        startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_contracts));
        startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_technology));
        startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_maintenance));
        startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_equipment));
        startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_help));
        startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_shopping));
    }

    private void startUnderConstructionActivity(ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), UnderConstructionActivity.class);
                requireActivity().startActivity(intent);
            }
        });
    }
}
