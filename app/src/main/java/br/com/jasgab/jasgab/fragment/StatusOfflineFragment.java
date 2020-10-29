package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.crud.MaintenanceDAO;
import br.com.jasgab.jasgab.model.Maintenance;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class StatusOfflineFragment extends Fragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status_offline, container, false);

        JasgabUtils.checkAPP(requireActivity());
        loadLayout();

        return view;
    }

    private void loadLayout(){
        JasgabUtils.setActionBarHome("Status", view, requireContext(), requireActivity());

        Maintenance maintenance = MaintenanceDAO.start(requireContext()).select();

        TextView status_offline_title = view.findViewById(R.id.status_offline_title);
        TextView status_offline_text = view.findViewById(R.id.status_offline_text);
        TextView status_offline_forecast = view.findViewById(R.id.status_offline_forecast);

        status_offline_title.setText(maintenance.getTitle());
        status_offline_text.setText(maintenance.getMessage());
        status_offline_forecast.setText(String.format(getString(R.string.status_offline_forecast), maintenance.getForecastReturn()));
    }
}
