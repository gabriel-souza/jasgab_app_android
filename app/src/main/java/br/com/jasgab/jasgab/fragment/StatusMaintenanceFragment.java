package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieResult;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.StatusType;
import br.com.jasgab.jasgab.crud.MaintenanceDAO;
import br.com.jasgab.jasgab.crud.StatusDAO;
import br.com.jasgab.jasgab.model.Maintenance;

public class StatusMaintenanceFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_maintenance, container, false);

        setLayout(view);

        return view;
    }

    private void setLayout(View view){
        //Set ActionBar
        TextView actionbar_text = view.findViewById(R.id.actionbar_text);
        actionbar_text.setText("Manutenção");
        ImageView actionbar_back = view.findViewById(R.id.actionbar_back);
        actionbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusDAO.start(requireContext()).insert(StatusType.Overview);
                OverviewFragment overviewFragment = new OverviewFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, overviewFragment)
                        .commit();
            }
        });

        //Set layout data
        Maintenance maintenance = MaintenanceDAO.start(requireContext()).select();

        if(maintenance != null){
            TextView maintenance_forecast_return = view.findViewById(R.id.maintenance_forecast_return);
            TextView maintenance_message = view.findViewById(R.id.maintenance_message);
            TextView maintenance_title = view.findViewById(R.id.maintenance_title);

            maintenance_forecast_return.setText(String.format("Previsão de retorno as %d horas", maintenance.getForecastReturn()));
            maintenance_message.setText(maintenance.getMessage());
            maintenance_title.setText(maintenance.getTitle());
        }else{
            //TODO GET MAINTENANCE
        }
    }
}
