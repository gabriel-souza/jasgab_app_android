package br.com.jasgab.jasgab.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import br.com.jasgab.jasgab.activity.LoadingActivity;
import br.com.jasgab.jasgab.activity.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class MoreFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);

        JasgabUtils.checkAPP(requireActivity());
        JasgabUtils.setActionBarHome("Mais", view, requireContext(), requireActivity());

        setLayout();
        businessRules();
        return view;
    }

    SwitchCompat more_configuration_notification_action;
    ImageView more_account_update_action, more_account_policy_action, more_account_contracts_action, more_account_rating_action;
    TextView more_account_logout_text;
    private void setLayout(){
        more_configuration_notification_action = view.findViewById(R.id.more_configuration_notification_action);
        more_account_update_action = view.findViewById(R.id.more_account_update_action);
        more_account_policy_action = view.findViewById(R.id.more_account_policy_action);
        more_account_contracts_action = view.findViewById(R.id.more_account_contracts_action);
        more_account_rating_action = view.findViewById(R.id.more_account_rating_action);
        more_account_logout_text = view.findViewById(R.id.more_account_logout_text);
    }

    private void businessRules(){
        more_configuration_notification_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO NOTIFICATION
            }
        });

        more_account_update_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), LoadingActivity.class);
                intent.putExtra("loading", LoadingActivity.LOGIN);
                requireActivity().startActivity(intent);
                requireActivity().finishAffinity();
            }
        });

        more_account_policy_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jasgab.com.br/contratos.html"));
                startActivity(browserIntent);
            }
        });

        more_account_contracts_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jasgab.com.br/contratos.html"));
                startActivity(browserIntent);
            }
        });

        more_account_rating_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.google.com/local/writereview?placeid=ChIJKWWNmEJJzpQRJVHkujCBeMQ"));
                startActivity(browserIntent);
            }
        });

        more_account_logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthDAO.start(requireContext()).delete();
                CustomerDAO.start(requireContext()).delete();
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finishAffinity();
            }
        });
    }
}
