package br.com.jasgab.jasgab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        Button services_plan = view.findViewById(R.id.services_plan);
        services_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), PlansActivity.class);
                requireActivity().startActivity(intent);
            }
        });

        Button services_invite = view.findViewById(R.id.services_invite);
        services_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "JASGAB TELECOM");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Estou compartilhando com você o APP da JASGAB");
                startActivity(Intent.createChooser(sharingIntent, "Compartilhar"));
            }
        });

        //Under Construction
        //startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_technology));
        //startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_maintenance));
        startUnderConstructionActivity((Button) view.findViewById(R.id.services_wifiplus));
        //startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_help));
        startUnderConstructionActivity((ImageView) view.findViewById(R.id.services_equipment));
    }

    private void startUnderConstructionActivity(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), UnderConstructionActivity.class);
                requireActivity().startActivity(intent);
            }
        });
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
