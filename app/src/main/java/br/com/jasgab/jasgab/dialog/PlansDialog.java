package br.com.jasgab.jasgab.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import br.com.jasgab.jasgab.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.CustomerNew;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.util.JasgabUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlansDialog extends DialogFragment {
    private String plan = "";
    private AlertDialog dialog;
    private View view;
    private AlertDialog.Builder builder;
    private ProgressBar dialog_plans_button_progressBar;
    private TextView dialog_plans_title, dialog_plans_sub_title;
    private RelativeLayout dialog_plans_button;
    private ImageView dialog_plans_icon, dialog_plans_success;
    private int PLANIS = 0;
    private static final int CHANGED = 1;


    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = Objects.requireNonNull(requireActivity()).getLayoutInflater();
        builder = new AlertDialog.Builder(getActivity());
        view = inflater.inflate(R.layout.dialog_plans, null);
        builder.setView(view);
        dialog =  builder.create();

        Window window = dialog.getWindow();
        if(window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(layoutParams);
        }

        setLayout();

        return dialog;
    }

    private void setLayout(){
        dialog_plans_title = view.findViewById(R.id.dialog_plans_title);
        dialog_plans_sub_title = view.findViewById(R.id.dialog_plans_sub_title);
        dialog_plans_button_progressBar = view.findViewById(R.id.dialog_plans_button_progressBar);
        dialog_plans_button = view.findViewById(R.id.dialog_plans_button);
        dialog_plans_icon  = view.findViewById(R.id.dialog_plans_icon);
        dialog_plans_success = view.findViewById(R.id.dialog_plans_success);

        Bundle bundle = getArguments();
        if(bundle != null) {
            plan = bundle.getString("plan", "");
        }

        dialog_plans_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewPlan();
            }
        });

        dialog_plans_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PLANIS != CHANGED) {
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    requireActivity().finishAffinity();
                }
            }
        });
    }

    private void setNewPlan(){
        dialog_plans_button.setClickable(false);
        dialog_plans_button_progressBar.setVisibility(View.VISIBLE);

        JasgabUtils.checkInternetActivity(requireContext(), requireActivity());

        Customer customer = CustomerDAO.start(requireContext()).selectCustomer();
        Auth auth = AuthDAO.start(requireContext()).select();
        if(customer == null || auth == null){
            fail();
            return;
        }

        try {
            CustomerNew customerNew = new CustomerNew(customer.getName(), customer.getCpf(), customer.getPhone(), plan);
            Call<ResponseCustomer> call = new JasgabApi().customerNew(customerNew, auth.getToken());
            call.enqueue(new Callback<ResponseCustomer>() {
                @Override
                public void onResponse(Call<ResponseCustomer> call, Response<ResponseCustomer> response) {
                    ResponseCustomer responseCustomer = response.body();
                    dialog_plans_button_progressBar.setVisibility(View.INVISIBLE);
                    if (responseCustomer != null) {
                        if (responseCustomer.getStatus()) {
                            success();
                            return;
                        }
                    }
                    error();
                }

                @Override
                public void onFailure(Call<ResponseCustomer> call, Throwable t) {
                    error();
                }
            });
        }
        catch (Exception ignored){ }
    }

    private void success() {
        dialog_plans_title.setText("Solicitação realizada com sucesso");
        dialog_plans_sub_title.setText("O seu plano será alterado em até 24 horas, e você receberá um boleto atualizado no seu próximo vencimento.");
        dialog_plans_button_progressBar.setVisibility(View.INVISIBLE);
        dialog_plans_button.setVisibility(View.INVISIBLE);
        dialog_plans_success.setVisibility(View.VISIBLE);
    }

    private void error(){
        dialog_plans_title.setText("Oops.");
        dialog_plans_sub_title.setText("Não foi possivel completar a operação, por favor tente novamente! Ou se preferir entre em contato com nosso WhatsApp.");
        dialog_plans_button_progressBar.setVisibility(View.INVISIBLE);
        dialog_plans_button.setVisibility(View.VISIBLE);
        dialog_plans_success.setVisibility(View.INVISIBLE);
    }

    private void fail(){
        startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finishAffinity();
    }
}
