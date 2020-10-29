package br.com.jasgab.jasgab.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieResult;


import java.util.List;

import br.com.jasgab.jasgab.activity.BillActivity;
import br.com.jasgab.jasgab.activity.HelpActivity;
import br.com.jasgab.jasgab.activity.LoginActivity;
import br.com.jasgab.jasgab.activity.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.activity.NoConnectionActivity;
import br.com.jasgab.jasgab.activity.ProfileActivity;
import br.com.jasgab.jasgab.activity.StatusOnlineActivity;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.dialog.NoWifiDialog;
import br.com.jasgab.jasgab.model_http.RequestCheckNeighborhood;
import br.com.jasgab.jasgab.model_http.ResponseDefault;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.model.Connection;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model_http.RequestStatus;
import br.com.jasgab.jasgab.model_http.ResponseStatus;
import br.com.jasgab.jasgab.pattern.StatusLayoutType;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.crud.MaintenanceDAO;
import br.com.jasgab.jasgab.crud.StatusDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Contract;
import br.com.jasgab.jasgab.model.Maintenance;
import br.com.jasgab.jasgab.model_http.ResponseCustomer;
import br.com.jasgab.jasgab.model_http.ResponseMaintenance;
import br.com.jasgab.jasgab.pattern.StatusType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewFragment extends Fragment {

    private View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_overview, container, false);

        JasgabUtils.checkAPP(requireActivity());

        setLayout();
        loadStatusAvd();

        businessRules();
        verifyStatus();

        return view;
    }

    private TextView overview_status_text;
    private ImageView overview_profile;
    private void setLayout(){
        overview_status_text = view.findViewById(R.id.overview_status_text);
        overview_profile = view.findViewById(R.id.overview_profile);
    }

    private void businessRules(){
        JasgabUtils.checkInternetActivity(requireContext(), requireActivity());

        ResponseCustomer responseCustomer = CustomerDAO.start(requireContext()).select();
        if(responseCustomer == null){
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            requireActivity().startActivity(intent);
            requireActivity().finishAffinity();
            return;
        }

        Customer customer = responseCustomer.getCustomer();
        List<Connection> connections = responseCustomer.getCustomerData().getConnections();
        List<Contract> contracts = responseCustomer.getCustomerData().getContracts();
        List<Bill> bills = responseCustomer.getCustomerData().getBills();

        if(customer == null || connections == null || connections.isEmpty() || contracts == null || contracts.isEmpty() || bills == null || bills.isEmpty()){
            CustomerDAO.start(requireContext()).delete();
            requireActivity().startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        Contract contract = contracts.get(0);
        if(contract != null){
            TextView textView_Plan = view.findViewById(R.id.overview_plan);
            textView_Plan.setText(contract.getPlan());
        }

        Bill bill = JasgabUtils.orderBills(responseCustomer.getCustomerData().getBills()).get(0);
        if(bill != null){
            TextView overview_price = view.findViewById(R.id.overview_price);
            TextView overview_duedate = view.findViewById(R.id.overview_duedate);
            ProgressBar overview_expireBar = view.findViewById(R.id.overview_expireBar);

            int daysToExpire = JasgabUtils.daysToExpire(bill.getDueDate());
            int daysToExpirePercentage = JasgabUtils.percentageExpireDate(daysToExpire);
            overview_price.setText(String.format(getString(R.string.overview_price), bill.getAmount().toString().replace(".",",")));
            overview_duedate.setText(String.format(getString(R.string.overview_duedate), daysToExpire));
            overview_expireBar.setProgress(daysToExpirePercentage);

            if(daysToExpire == 0){
                overview_duedate.setText(R.string.overview_duedate_today);
                overview_expireBar.setProgressDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.overview_expirebar_yellow));
            }

            if(daysToExpirePercentage >= 101){
                overview_expireBar.setProgressDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.overview_expirebar_red));
                overview_duedate.setText(R.string.overview_duedate_expired);
            }

            overview_duedate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireContext(), BillActivity.class);
                    intent.putExtra("bill_position", 0);
                    requireContext().startActivity(intent);
                }
            });

            overview_expireBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireContext(), BillActivity.class);
                    intent.putExtra("bill_position", 0);
                    requireContext().startActivity(intent);
                }
            });
        }

        overview_status_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStatus();
            }
        });

        Button overview_bill_button = view.findViewById(R.id.overview_bill_button);
        overview_bill_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), BillActivity.class);
                intent.putExtra("bill_position", 0);
                requireContext().startActivity(intent);
            }
        });

        Button overview_button_help = view.findViewById(R.id.overview_button_help);
        overview_button_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), HelpActivity.class);
                requireContext().startActivity(intent);
            }
        });

        overview_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().startActivity(new Intent(requireContext(), ProfileActivity.class));
            }
        });
    }

    private LottieAnimationView overview_status;
    private LottieComposition[] lottieCompositions;
    private void loadStatusAvd(){
        overview_status = view.findViewById(R.id.overview_status);
        lottieCompositions = new LottieComposition[7];

        LottieResult<LottieComposition> status_0 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_0.json");
        LottieResult<LottieComposition> status_1 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_1.json");
        LottieResult<LottieComposition> status_2 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_2.json");
        LottieResult<LottieComposition> status_3 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_3.json");
        LottieResult<LottieComposition> status_4 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_4.json");
        LottieResult<LottieComposition> status_5 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_5.json");
        LottieResult<LottieComposition> status_6 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_6.json");

        lottieCompositions[0] = status_0.getValue();
        lottieCompositions[1] = status_1.getValue();
        lottieCompositions[2] = status_2.getValue();
        lottieCompositions[3] = status_3.getValue();
        lottieCompositions[4] = status_4.getValue();
        lottieCompositions[5] = status_5.getValue();
        lottieCompositions[6] = status_6.getValue();

        if (lottieCompositions[0] != null) {
            overview_status.setComposition(lottieCompositions[0]);
            overview_status.setRepeatCount(LottieDrawable.INFINITE);
            overview_status.playAnimation();
        }

        overview_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStatus();
            }
        });
    }

    private Integer statusType = -1;
    private void startStatus(){
        switch (statusType) {
            case StatusType.Online:
                if(JasgabUtils.checkWifi(requireContext())) {
                    StatusDAO.start(requireContext()).insert(StatusLayoutType.Online);
                    startActivity(new Intent(requireContext(), StatusOnlineActivity.class));
                }else{
                    NoWifiDialog dialog = new NoWifiDialog();
                    dialog.show(requireActivity().getSupportFragmentManager(), "");
                }
                break;
            case StatusType.Blocked:
                StatusDAO.start(requireContext()).insert(StatusLayoutType.Blocked);
                StatusBlockedFragment statusBlockedFragment = new StatusBlockedFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, statusBlockedFragment)
                        .commit();
                break;
            case StatusType.Maintenance:
                StatusDAO.start(requireContext()).insert(StatusLayoutType.Maintenance);
                StatusOfflineFragment statusOfflineFragment = new StatusOfflineFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, statusOfflineFragment)
                        .commit();
                break;
        }
    }

    private void verifyStatus() {
        RequestStatus requestStatus = new RequestStatus(CustomerDAO.start(requireContext()).selectCustomer().getId());
        Call<ResponseStatus> call = JasgabApi.status(requestStatus, AuthDAO.start(requireContext()).select().getToken());
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(@NonNull Call<ResponseStatus> call, @NonNull Response<ResponseStatus> response) {
                ResponseStatus responseStatus = response.body();
                if(responseStatus != null && responseStatus.getStatus()){
                    switch (responseStatus.getInternetStatus()){
                        case StatusType.Online:
                            startStatusOnline();
                            break;
                        case StatusType.Blocked:
                            startStatusBlocked();
                            break;
                        case StatusType.Maintenance:
                            startStatusOffline(responseStatus);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseStatus> call, @NonNull Throwable t) {
                internetError();
            }
        });
    }

    private void startStatusOnline(){
        overview_status_text.setText(getString(R.string.overview_message_online));

        statusType = StatusType.Online;
        overview_status.setComposition(lottieCompositions[1]);
        overview_status.setRepeatCount(0);
        overview_status.playAnimation();
        overview_status.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                overview_status.setComposition(lottieCompositions[2]);
                overview_status.setRepeatCount(LottieDrawable.INFINITE);
                overview_status.playAnimation();
            }
        });
    }

    private void startStatusBlocked(){
        overview_status_text.setText(getString(R.string.overview_message_blocked));

        statusType = StatusType.Blocked;
        overview_status.setComposition(lottieCompositions[3]);
        overview_status.setRepeatCount(0);
        overview_status.playAnimation();
        overview_status.removeAllAnimatorListeners();
        overview_status.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                overview_status.setComposition(lottieCompositions[4]);
                overview_status.setRepeatCount(LottieDrawable.INFINITE);
                overview_status.playAnimation();
            }
        });

    }

    private void startStatusOffline(ResponseStatus responseStatus){
        overview_status_text.setText(getString(R.string.overview_message_offline));

        checkNeighborhood(responseStatus.getMaintenance());
    }

    private void checkNeighborhood(final Maintenance maintenance){
        Auth auth = AuthDAO.start(requireContext()).select();
        Customer customer = CustomerDAO.start(requireContext()).selectCustomer();
        if(auth == null || customer == null){
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        Call<ResponseDefault> call = new JasgabApi().check_neighborhood(new RequestCheckNeighborhood(customer.getNeighborhood()), auth.getToken());
        call.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDefault> call, @NonNull Response<ResponseDefault> response) {
                ResponseDefault responseDefault = response.body();
                if(responseDefault != null && responseDefault.getStatus()) {
                    MaintenanceDAO.start(requireContext()).insert(maintenance);
                    statusType = StatusType.Maintenance;
                    overview_status.setComposition(lottieCompositions[5]);
                    overview_status.setRepeatCount(0);
                    overview_status.playAnimation();
                    overview_status.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            overview_status.setComposition(lottieCompositions[6]);
                            overview_status.setRepeatCount(LottieDrawable.INFINITE);
                            overview_status.playAnimation();
                        }
                    });
                }else{
                    startStatusOnline();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseDefault> call, @NonNull Throwable t) {
                internetError();
            }
        });
    }

    private void internetError(){
        startActivity(new Intent(requireContext(), NoConnectionActivity.class));
        requireActivity().finishAffinity();
    }
}