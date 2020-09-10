package br.com.jasgab.jasgab.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieResult;

import br.com.jasgab.jasgab.activity.BillActivity;
import br.com.jasgab.jasgab.activity.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.pattern.AnimationUnlockType;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.pattern.StatusLayoutType;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.crud.StatusDAO;
import br.com.jasgab.jasgab.crud.UnlockDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Connection;
import br.com.jasgab.jasgab.model.CustomerData;
import br.com.jasgab.jasgab.model_http.RequestCustomerUnlock;
import br.com.jasgab.jasgab.model_http.ResponseCustomerUnlock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusBlockedFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status_blocked, container, false);

        blockedAnimation();
        setLayout();

        return view;
    }

    private TextView status_blocked_unlocked_message, blocked_message, status_blocked_balloon, status_blocked_warning;
    private Button blocked_button_pay;
    private Animation animFadeIn, animFadeOut;
    private void setLayout(){
        status_blocked_warning = view.findViewById(R.id.status_blocked_warning);
        status_blocked_unlocked_message = view.findViewById(R.id.status_blocked_unlocked_message);
        blocked_message = view.findViewById(R.id.blocked_message);
        status_blocked_balloon = view.findViewById(R.id.status_blocked_balloon);
        blocked_button_pay = view.findViewById(R.id.blocked_button_pay);

        status_blocked_warning.setText(Html.fromHtml(getResources().getString(R.string.status_blocked_warning)));
        blocked_message.setText(Html.fromHtml(getResources().getString(R.string.blocked_message)));

        JasgabUtils.setActionBarOverview("Status", view, requireContext(), requireActivity());

        blocked_button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusDAO.start(requireContext()).insert(StatusLayoutType.Overview);
                Intent intent = new Intent(requireContext(), BillActivity.class);
                intent.putExtra("bill_position", 0);
                requireActivity().startActivity(intent);
            }
        });

        animFadeIn = AnimationUtils.loadAnimation(requireContext().getApplicationContext(), R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                status_blocked_warning.setVisibility(View.VISIBLE);
                status_blocked_unlocked_message.setVisibility(View.VISIBLE);
                blocked_button_pay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        /*
        animFadeOut = AnimationUtils.loadAnimation(requireContext().getApplicationContext(), R.anim.fade_out);
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                blocked_message.setVisibility(View.INVISIBLE);
                status_blocked_balloon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
         */
    }

    private LottieAnimationView blocked;
    private LottieComposition[] lottieComposition;
    private AnimatorListenerAdapter waiting, waitingRequest, success, successFinal, error;
    private void blockedAnimation(){
        blocked = view.findViewById(R.id.blocked);
        lottieComposition = new LottieComposition[7];

        LottieResult<LottieComposition> lottieBlocked0 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_blocked_0.json");
        LottieResult<LottieComposition> lottieBlocked1 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_blocked_1.json");
        LottieResult<LottieComposition> lottieBlocked2 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_blocked_2.json");
        LottieResult<LottieComposition> lottieBlocked3 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_blocked_3.json");
        LottieResult<LottieComposition> lottieBlocked4 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_blocked_4.json");
        LottieResult<LottieComposition> lottieBlocked5 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_blocked_5.json");
        LottieResult<LottieComposition> lottieBlocked6 = LottieCompositionFactory.fromAssetSync(requireContext(), "avd_blocked_6.json");

        lottieComposition[AnimationUnlockType.Start] = lottieBlocked0.getValue();
        lottieComposition[AnimationUnlockType.Waiting] = lottieBlocked1.getValue();
        lottieComposition[AnimationUnlockType.Unlock] = lottieBlocked2.getValue();
        lottieComposition[AnimationUnlockType.RequestWaiting] = lottieBlocked3.getValue();
        lottieComposition[AnimationUnlockType.Success] = lottieBlocked4.getValue();
        lottieComposition[AnimationUnlockType.SuccessFinal] = lottieBlocked5.getValue();
        lottieComposition[AnimationUnlockType.Error] = lottieBlocked6.getValue();

        waiting = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blocked.setComposition(lottieComposition[AnimationUnlockType.Waiting]);
                blocked.setRepeatCount(LottieDrawable.INFINITE);
                blocked.playAnimation();
            }
        };

        waitingRequest = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blocked.setComposition(lottieComposition[AnimationUnlockType.RequestWaiting]);
                blocked.setRepeatCount(LottieDrawable.INFINITE);
                blocked.playAnimation();
            }
        };

        success = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blocked.setComposition(lottieComposition[AnimationUnlockType.Success]);
                blocked.setRepeatCount(0);
                blocked.playAnimation();
                blocked.addAnimatorListener(successFinal);
            }
        };

        successFinal = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blocked.setComposition(lottieComposition[AnimationUnlockType.SuccessFinal]);
                blocked.setRepeatCount(LottieDrawable.INFINITE);
                blocked.playAnimation();
            }
        };

        error = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blocked.setComposition(lottieComposition[AnimationUnlockType.Error]);
                blocked.setRepeatCount(0);
                blocked.playAnimation();
                blocked.removeAllAnimatorListeners();
                blocked.addAnimatorListener(waiting);
            }
        };


        if(lottieComposition[AnimationUnlockType.Start] != null) {
            blocked.setComposition(lottieComposition[AnimationUnlockType.Start]);
            blocked.playAnimation();
            blocked.removeAllAnimatorListeners();
            blocked.addAnimatorListener(waiting);
        }

        blocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = CustomerDAO.start(requireContext()).select().getCustomerData().getConnections().get(0);
                if(!connection.getBlocked()){
                    Toast.makeText(requireContext(), "Internet já está desbloqueada, por favor reinicie os equipamentos.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(UnlockDAO.start(requireContext()).select()){
                    Toast.makeText(requireContext(), "Desbloqueio não permitido, você só pode desbloquear a conexão apenas uma vez por fatura.", Toast.LENGTH_LONG).show();
                    return;
                }

                blocked.setComposition(lottieComposition[AnimationUnlockType.Unlock]);
                blocked.setRepeatCount(0);
                blocked.playAnimation();
                blocked.addAnimatorListener(waitingRequest);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlock();
                    }
                }, 1500);
            }
        });
    }

    private void unlock(){
        Auth auth = AuthDAO.start(requireContext()).select();
        if(auth == null){
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        CustomerData customerData = CustomerDAO.start(requireContext()).select().getCustomerData();
        for (Connection connection : customerData.getConnections()) {
            final RequestCustomerUnlock requestCustomerUnlock = new RequestCustomerUnlock(connection.getId());

            Call<ResponseCustomerUnlock> call = new JasgabApi().unlock(requestCustomerUnlock, auth.getToken());
            call.enqueue(new Callback<ResponseCustomerUnlock>() {
                @Override
                public void onResponse(Call<ResponseCustomerUnlock> call, Response<ResponseCustomerUnlock> response) {
                    ResponseCustomerUnlock responseCustomerUnlock = response.body();
                    if (responseCustomerUnlock != null) {
                        if (responseCustomerUnlock.getStatus()) {
                            CustomerDAO.start(requireContext()).updateConnectionBlocked(false);
                            blocked.setComposition(lottieComposition[AnimationUnlockType.Success]);
                            blocked.setRepeatCount(0);
                            blocked.playAnimation();
                            blocked.removeAllAnimatorListeners();
                            blocked.addAnimatorListener(successFinal);

                            Bill bill = JasgabUtils.actualBill(CustomerDAO.start(requireContext()).selectBills());
                            int bill_id = 0;
                            if(bill != null){
                                bill_id = bill.getId();
                            }
                            UnlockDAO.start(requireContext()).insert(bill_id);

                            //status_blocked_warning.setVisibility(View.VISIBLE);
                            //status_blocked_unlocked_message.setVisibility(View.VISIBLE);
                            //blocked_button_pay.setVisibility(View.VISIBLE);

                            blocked_message.setVisibility(View.INVISIBLE);
                            status_blocked_balloon.setVisibility(View.INVISIBLE);

                            status_blocked_warning.setAnimation(animFadeIn);
                            status_blocked_unlocked_message.setAnimation(animFadeIn);
                            blocked_button_pay.setAnimation(animFadeIn);

                            //blocked_message.setAnimation(animFadeOut);
                            //status_blocked_balloon.setAnimation(animFadeOut);
                        } else {
                            blocked.removeAllAnimatorListeners();
                            blocked.addAnimatorListener(error);
                            Toast.makeText(requireContext(), "Auto desbloqueio não permitido você só pode desbloquear a conexão apenas uma vez por fatura.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        blocked.removeAllAnimatorListeners();
                        blocked.addAnimatorListener(error);
                        Toast.makeText(requireContext(), "Auto desbloqueio não permitido, entre em contato para saber mais.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseCustomerUnlock> call, Throwable t) {
                    blocked.setComposition(lottieComposition[AnimationUnlockType.Error]);
                    blocked.setRepeatCount(0);
                    blocked.playAnimation();
                    blocked.removeAllAnimatorListeners();
                    blocked.addAnimatorListener(waiting);
                    Toast.makeText(requireContext(), "Desbloqueio não permitido, entre em contato para saber mais.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
