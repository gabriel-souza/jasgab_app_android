package br.com.jasgab.jasgab.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import br.com.jasgab.jasgab.LoginActivity;
import br.com.jasgab.jasgab.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.StatusOnlineActivity;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.model.Connection;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.RequestStatus;
import br.com.jasgab.jasgab.model.ResponseStatus;
import br.com.jasgab.jasgab.pattern.StatusLayoutType;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.crud.MaintenanceDAO;
import br.com.jasgab.jasgab.crud.StatusDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Contract;
import br.com.jasgab.jasgab.model.Maintenance;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.model.ResponseMaintenance;
import br.com.jasgab.jasgab.pattern.StatusType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewFragment extends Fragment {
    private ResponseCustomer mResponseCustomer;
    private Customer mCustomer;
    private LottieAnimationView mAvdStatus;
    private LottieComposition[] mLottieCompositions;
    private TextView mTextViewOverviewMessage;
    private Integer mStatusType = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_overview, container, false);

        mTextViewOverviewMessage = view.findViewById(R.id.overview_message);
        run(view);
        statusAnimation(view);

        return view;
    }

    private void run(View view){
        JasgabUtils.checkInternetDialog(requireContext(), requireActivity());

        Auth mAuth = AuthDAO.start(requireContext()).select();
        mResponseCustomer = CustomerDAO.start(requireContext()).select();
        if(mAuth == null || mResponseCustomer == null){
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            requireActivity().startActivity(intent);
            requireActivity().finishAffinity();
        }

        mCustomer = mResponseCustomer.getCustomer();
        List<Connection> connections = mResponseCustomer.getCustomerData().getConnections();
        List<Contract> contracts = mResponseCustomer.getCustomerData().getContracts();
        List<Bill> bills = mResponseCustomer.getCustomerData().getBills();

        if(mCustomer == null || connections == null || connections.isEmpty() || contracts == null || contracts.isEmpty() || bills == null || bills.isEmpty()){
            Intent intent = new Intent(requireContext(), MainActivity.class);
            requireActivity().startActivity(intent);
            requireActivity().finishAffinity();
            return;
        }

        if(contracts.size() > 1){
            runSecundary();
            return;
        }

        Contract contract = contracts.get(0);
        if(contract != null){
            TextView textView_Plan = view.findViewById(R.id.overview_plan);
            textView_Plan.setText(contract.getPlan());
        }

        Bill bill = JasgabUtils.actualBill(mResponseCustomer.getCustomerData().getBills());
        if(bill != null){
            TextView textView_price = view.findViewById(R.id.overview_price);
            TextView textView_billExpiration = view.findViewById(R.id.overview_billExpiration);
            ProgressBar progressBar_expireBar = view.findViewById(R.id.overview_expireBar);

            int daysToExpire = JasgabUtils.daysToExpire(bill.getDueDate());
            int daysToExpirePercentage = JasgabUtils.percentageExpireDate(daysToExpire);
            textView_price.setText(String.format("R$ %s mensal", bill.getAmount().toString()));
            textView_billExpiration.setText(String.format("Vencimento em %d dias", daysToExpire));
            progressBar_expireBar.setProgress(daysToExpirePercentage);

            if(daysToExpire == 0){
                textView_billExpiration.setText("Vence hoje");
                progressBar_expireBar.setProgressDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.pb_yellow_bill_expiration));
            }

            if(daysToExpirePercentage >= 101){
                progressBar_expireBar.setProgressDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.pb_red_bill_expiration));
                textView_billExpiration.setText("Vencimento em atraso");
            }
        }

        mTextViewOverviewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment();
            }
        });

        Button overview_bill_button = view.findViewById(R.id.overview_bill_button);
        overview_bill_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillFragment billFragment = new BillFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, billFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void runSecundary(){
        //TODO CUSTOMER WITH MORE THAN 1 CONTRACT
    }

    private void statusAnimation(View view){
        if(view != null) {
            mAvdStatus = view.findViewById(R.id.overview_status);
            mLottieCompositions = new LottieComposition[7];

            LottieResult<LottieComposition> status_0 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_0.json");
            LottieResult<LottieComposition> status_1 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_1.json");
            LottieResult<LottieComposition> status_2 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_2.json");
            LottieResult<LottieComposition> status_3 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_3.json");
            LottieResult<LottieComposition> status_4 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_4.json");
            LottieResult<LottieComposition> status_5 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_5.json");
            LottieResult<LottieComposition> status_6 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_6.json");

            mLottieCompositions[0] = status_0.getValue();
            mLottieCompositions[1] = status_1.getValue();
            mLottieCompositions[2] = status_2.getValue();
            mLottieCompositions[3] = status_3.getValue();
            mLottieCompositions[4] = status_4.getValue();
            mLottieCompositions[5] = status_5.getValue();
            mLottieCompositions[6] = status_6.getValue();

            if (mLottieCompositions[0] != null) {
                mAvdStatus.setComposition(mLottieCompositions[0]);
                mAvdStatus.setRepeatCount(LottieDrawable.INFINITE);
                mAvdStatus.playAnimation();
            }

            mAvdStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startFragment();
                }
            });

            verifyStatus();
        }
    }

    private void startFragment(){
        switch (mStatusType) {
            case StatusType.Online:
                StatusDAO.start(requireContext()).insert(StatusLayoutType.Online);
                startActivity(new Intent(requireContext(), StatusOnlineActivity.class));
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

        Auth auth = AuthDAO.start(requireContext()).select();
        if(auth == null){
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        RequestStatus requestStatus = new RequestStatus(mCustomer.getId());
        Call<ResponseStatus> call = new JasgabApi().status(requestStatus, auth.getToken());
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                ResponseStatus responseStatus = response.body();
                if(responseStatus != null){
                    if(responseStatus.getStatus()){
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
                        return;
                    }
                }
                //TODO REQUEST ERROR
            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                //TODO REQUEST ERROR
            }
        });
    }

    private void startStatusOnline(){
        mTextViewOverviewMessage.setText(getString(R.string.overview_message_online));

        mStatusType = StatusType.Online;
        mAvdStatus.setComposition(mLottieCompositions[1]);
        mAvdStatus.setRepeatCount(0);
        mAvdStatus.playAnimation();
        mAvdStatus.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAvdStatus.setComposition(mLottieCompositions[2]);
                mAvdStatus.setRepeatCount(LottieDrawable.INFINITE);
                mAvdStatus.playAnimation();
            }
        });
    }

    private void startStatusBlocked(){
        mTextViewOverviewMessage.setText(getString(R.string.overview_message_blocked));

        mStatusType = StatusType.Blocked;
        mAvdStatus.setComposition(mLottieCompositions[3]);
        mAvdStatus.setRepeatCount(0);
        mAvdStatus.playAnimation();
        mAvdStatus.removeAllAnimatorListeners();
        mAvdStatus.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAvdStatus.setComposition(mLottieCompositions[4]);
                mAvdStatus.setRepeatCount(LottieDrawable.INFINITE);
                mAvdStatus.playAnimation();
            }
        });

    }

    private void startStatusOffline(ResponseStatus responseStatus){
        mTextViewOverviewMessage.setText(getString(R.string.overview_message_offline));

        checkNeighborhood(responseStatus.getMaintenance());
    }

    private void checkNeighborhood(final Maintenance maintenance){
        Auth auth = AuthDAO.start(requireContext()).select();
        if(auth == null){
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        Call<ResponseMaintenance> call = new JasgabApi().check_neighborhood(auth.getToken());
        call.enqueue(new Callback<ResponseMaintenance>() {
            @Override
            public void onResponse(Call<ResponseMaintenance> call, Response<ResponseMaintenance> response) {
                ResponseMaintenance responseMaintenance = response.body();
                if(responseMaintenance != null) {
                    if (responseMaintenance.getStatus()) {
                        MaintenanceDAO.start(requireContext()).insert(maintenance);

                        mStatusType = StatusType.Maintenance;
                        mAvdStatus.setComposition(mLottieCompositions[5]);
                        mAvdStatus.setRepeatCount(0);
                        mAvdStatus.playAnimation();
                        mAvdStatus.addAnimatorListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mAvdStatus.setComposition(mLottieCompositions[6]);
                                mAvdStatus.setRepeatCount(LottieDrawable.INFINITE);
                                mAvdStatus.playAnimation();
                            }
                        });
                        return;
                    }else{
                        startStatusOnline();
                    }
                }
                //TODO REQUEST ERROR
            }

            @Override
            public void onFailure(Call<ResponseMaintenance> call, Throwable t) {
                //TODO REQUEST ERROR
            }
        });
    }

}
