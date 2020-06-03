package br.com.jasgab.jasgab.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import br.com.jasgab.jasgab.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.api.JasgabUtils;
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
import br.com.jasgab.jasgab.model.Connection;
import br.com.jasgab.jasgab.model.Contract;
import br.com.jasgab.jasgab.model.Maintenance;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.model.ResponseMaintenance;
import br.com.jasgab.jasgab.pattern.StatusType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewFragment extends Fragment {
    ResponseCustomer responseCustomer;

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_overview, container, false);

        responseCustomer = CustomerDAO.start(requireContext()).select();

        statusAnimation(view);
        customerData(view);

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

        return view;
    }

    private void customerData(View view){
        //GET CUSTOMER DATA
        //Customer customer = responseCustomer.getCustomer();
        List<Contract> contracts = responseCustomer.getCustomerData().getContracts();
        if(contracts.size() > 1){
            //TODO CUSTOMER WITH MORE THAN 1 CONTRACT
        }
        Contract contract = contracts.get(0);
        //Connection connection = responseCustomer.getCustomerData().getConnections().get(0);

        Bill bill = JasgabUtils.actualBill(responseCustomer.getCustomerData().getBills());
        int daysToExpire = bill != null ? (int) JasgabUtils.daysToExpire(bill.getExpirationDate()) : -1;
        int daysToExpirePercentage = JasgabUtils.percentageExpireDate(daysToExpire);

        //DISPLAY DATA
        TextView tv_plan = view.findViewById(R.id.tv_home_plan);
        TextView tv_price = view.findViewById(R.id.tv_home_price);
        TextView tv_bill_expiration = view.findViewById(R.id.tv_home_bill_expiration);
        ProgressBar pb_barcode = view.findViewById(R.id.pb_home_barcode);

        tv_plan.setText(contract.getPlan());
        tv_price.setText(String.format("R$ %s mensal", bill.getAmount().toString()));
        tv_bill_expiration.setText(String.format("Vencimento em %d dias", daysToExpire));
        pb_barcode.setProgress(daysToExpirePercentage);

        if(daysToExpirePercentage >= 101){
            pb_barcode.setProgressDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.pb_red_bill_expiration));
            tv_bill_expiration.setText("Vencimento em atraso");
        }
    }

    private void statusAnimation(View view){
        final LottieAnimationView status = view.findViewById(R.id.status);
        final LottieComposition[] lottieComposition = new LottieComposition[3];

        //Carregar e colocar na memoria para iniciar.
        LottieResult<LottieComposition> lottieStatusEsperando = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_waiting.json");
        LottieResult<LottieComposition> lottieStatusClick = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_click.json");
        LottieResult<LottieComposition> lottieStatusCarregando = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_status_loading.json");

        lottieComposition[0] = lottieStatusEsperando.getValue();
        lottieComposition[1] = lottieStatusClick.getValue();
        lottieComposition[2] = lottieStatusCarregando.getValue();

        if(lottieComposition[0] != null) {
            status.setComposition(lottieComposition[0]);
            status.setRepeatCount(LottieDrawable.INFINITE);
            status.playAnimation();
        }

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setComposition(lottieComposition[1]);
                status.setRepeatCount(0);
                status.playAnimation();
                status.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        status.setComposition(lottieComposition[2]);
                        status.setRepeatCount(LottieDrawable.INFINITE);
                        status.playAnimation();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                verifyStatus();
                            }
                        }, 2000);
                    }
                });
            }
        });
    }

    private void verifyStatus() {
        Customer customer = responseCustomer.getCustomer();

        Auth auth = AuthDAO.start(requireContext()).select();
        if(auth == null){
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        RequestStatus requestStatus = new RequestStatus(customer.getId());
        Call<ResponseStatus> call = new JasgabApi().status(requestStatus, auth.getToken());
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
                ResponseStatus responseStatus = response.body();
                if(responseStatus != null){
                    if(responseStatus.getStatus()){
                        switch (responseStatus.getInternetStatus()){
                            case StatusType.Online:
                                startStatusOk();
                                break;
                            case StatusType.Blocked:
                                StatusBlockedFragment statusBlockedFragment = new StatusBlockedFragment();
                                CustomerDAO.start(requireContext()).updateConnectionBlocked(true);
                                StatusDAO.start(requireContext()).insert(StatusLayoutType.Blocked);
                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.home_container, statusBlockedFragment)
                                        .commit();
                                return;
                            case StatusType.Maintenance:
                                checkNeighborhood(responseStatus.getMaintenance());
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
                         StatusMaintenanceFragment statusMaintenanceFragment = new StatusMaintenanceFragment();

                         StatusDAO.start(requireContext()).insert(StatusLayoutType.Maintenance);
                         requireActivity().getSupportFragmentManager()
                                 .beginTransaction()
                                 .replace(R.id.home_container, statusMaintenanceFragment)
                                 .commit();
                         return;
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

    private void startStatusOk(){
        StatusOkFragment statusOkFragment = new StatusOkFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_container, statusOkFragment)
                .addToBackStack(null)
                .commit();
    }
}
