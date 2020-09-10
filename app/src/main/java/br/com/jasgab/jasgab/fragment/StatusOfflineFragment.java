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

        setLayout();

        return view;
    }

    private void setLayout(){
        JasgabUtils.setActionBarOverview("Status", view, requireContext(), requireActivity());

        Maintenance maintenance = MaintenanceDAO.start(requireContext()).select();

        if(maintenance != null){
            TextView maintenance_forecast_return = view.findViewById(R.id.status_offline_forecastreturn);
            TextView maintenance_message = view.findViewById(R.id.status_offline_message);
            TextView maintenance_title = view.findViewById(R.id.status_offline_title);

            maintenance_forecast_return.setText(String.format("Previsão de retorno as %s horas", maintenance.getForecastReturn()));
            maintenance_message.setText(maintenance.getMessage());
            maintenance_title.setText(maintenance.getTitle());
        }else{
            //TODO GET MAINTENANCE
        }
    }
}
