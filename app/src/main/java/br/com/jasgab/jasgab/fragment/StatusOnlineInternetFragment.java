package br.com.jasgab.jasgab.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.jasgab.jasgab.activity.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.dialog.StatusOnlineInternetDialog;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model_http.ResponseIsp;
import br.com.jasgab.jasgab.util.InternetUtils;
import br.com.jasgab.jasgab.util.JasgabUtils;
import me.impa.pinger.PingInfo;
import me.impa.pinger.Pinger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusOnlineInternetFragment extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status_online_internet, container, false);
        JasgabUtils.checkWifiActivity(requireContext(), requireActivity());

        businessRules();

        return view;
    }

    private void businessRules(){
        setLayout();
        setIspNat();
        startPing();
    }

    TextView status_online_internet_isp,
            status_online_internet_nat,
            status_online_internet_ping_gateway,
            status_online_internet_ping_jasgab,
            status_online_internet_ping_google;
    ImageView status_online_internet_question_mark;
    private void setLayout(){
        status_online_internet_isp = view.findViewById(R.id.status_online_internet_isp);
        status_online_internet_nat = view.findViewById(R.id.status_online_internet_nat);
        status_online_internet_ping_gateway = view.findViewById(R.id.status_online_internet_ping_gateway);
        status_online_internet_ping_jasgab = view.findViewById(R.id.status_online_internet_ping_jasgab);
        status_online_internet_ping_google = view.findViewById(R.id.status_online_internet_ping_google);
        status_online_internet_question_mark = view.findViewById(R.id.status_online_internet_question_mark);

        status_online_internet_question_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusOnlineInternetDialog dialog = new StatusOnlineInternetDialog();
                dialog.show(requireActivity().getSupportFragmentManager(), "StatusOnlineInternetDialog");
            }
        });
    }

    private void setIspNat(){
        Auth auth = AuthDAO.start(requireContext()).select();
        if(auth == null){
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        Call<ResponseIsp> call = new JasgabApi().isp(auth.getToken());
        call.enqueue(new Callback<ResponseIsp>() {
            @Override
            public void onResponse(@NonNull Call<ResponseIsp> call, @NonNull Response<ResponseIsp> response){
                ResponseIsp responseIsp = response.body();
                if(responseIsp != null && responseIsp.getStatus()){
                    status_online_internet_isp.setText(responseIsp.getIsp().getIsp());
                    status_online_internet_nat.setText(String.format("NAT: %s", responseIsp.getIsp().getNat()));
                    return;
                }
                setIspNatErro();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseIsp> call, @NonNull Throwable t) {
                setIspNatErro();
            }
        });
    }

    private void setIspNatErro(){
        status_online_internet_isp.setText(R.string.status_online_internet_isp_default);
        status_online_internet_nat.setText(R.string.status_online_internet_nat_default);
    }

    private void startPing() {
        pingGateway();
        pingJasgab();
        pingGoogle();
    }

    private void pingGateway(){
        String gatewayHost = InternetUtils.getWifiGateway(requireContext());
        if(!gatewayHost.equals("0.0.0.0")) {
            final Pinger gateway = new Pinger();
            gateway.setOnPingListener(new Pinger.OnPingListener() {

                @Override
                public void OnStart(@NonNull PingInfo pingInfo) { }

                @Override
                public void OnStop(@NonNull PingInfo pingInfo) {
                    pingGateway();
                }

                @Override
                public void OnSendError(@NonNull PingInfo pingInfo, int sequence) {
                    status_online_internet_ping_gateway.setText(R.string.status_online_ping_timeout);
                    if (sequence >= 5)
                        pingInfo.Pinger.Stop(pingInfo.PingId);
                }

                @Override
                public void OnReplyReceived(@NonNull PingInfo pingInfo, final int sequence, final int timeMs) {
                    status_online_internet_ping_gateway.post(new Runnable() {
                        @SuppressLint("DefaultLocale")
                        public void run() {
                            status_online_internet_ping_gateway.setText(String.format("Ping %dms", timeMs));
                        }
                    });
                }

                @Override
                public void OnTimeout(@NonNull PingInfo pingInfo, int sequence) {
                    status_online_internet_ping_gateway.setText(R.string.status_online_ping_timeout);
                    if (sequence >= 5)
                        pingInfo.Pinger.Stop(pingInfo.PingId);
                }

                @Override
                public void OnException(@NonNull PingInfo pingInfo, @NonNull Exception e, boolean isFatal) {
                    status_online_internet_ping_gateway.post(new Runnable() {
                        public void run() {
                            status_online_internet_ping_gateway.setText(R.string.status_online_ping_timeout);
                        }
                    });
                    pingGateway();
                }
            });
            gateway.Ping(gatewayHost);
        }else{
            status_online_internet_ping_gateway.setText(R.string.status_online_ping_notfound);
        }
    }

    private void pingJasgab(){
        final Pinger jasgab = new Pinger();
        jasgab.setOnPingListener(new Pinger.OnPingListener() {

            @Override
            public void OnStart(@NonNull PingInfo pingInfo) { }

            @Override
            public void OnStop(@NonNull PingInfo pingInfo) {
                pingJasgab();
            }

            @Override
            public void OnSendError(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText(R.string.status_online_ping_timeout);
                    }
                });
                if (sequence>=5)
                    pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnReplyReceived(@NonNull PingInfo pingInfo, final int sequence, final int timeMs) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    @SuppressLint("DefaultLocale")
                    public void run() {
                        status_online_internet_ping_jasgab.setText(String.format("Ping %dms", timeMs));
                    }
                });
            }

            @Override
            public void OnTimeout(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText(R.string.status_online_ping_timeout);
                    }
                });
                if (sequence>=5)
                    pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnException(@NonNull PingInfo pingInfo, @NonNull Exception e, boolean isFatal) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText(R.string.status_online_ping_timeout);
                    }
                });
                pingJasgab();
            }
        });
        jasgab.Ping("45.7.152.1");
    }

    private void pingGoogle(){
        final Pinger google = new Pinger();
        google.setOnPingListener(new Pinger.OnPingListener() {

            @Override
            public void OnStart(@NonNull PingInfo pingInfo) { }

            @Override
            public void OnStop(@NonNull PingInfo pingInfo) {
                pingGoogle();
            }

            @Override
            public void OnSendError(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_google.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_google.setText(R.string.status_online_ping_timeout);
                    }
                });
                if (sequence>=5)
                    pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnReplyReceived(@NonNull PingInfo pingInfo, final int sequence, final int timeMs) {
                status_online_internet_ping_google.post(new Runnable() {
                    @SuppressLint("DefaultLocale")
                    public void run() {
                        status_online_internet_ping_google.setText(String.format("Ping %dms", timeMs));
                    }
                });
            }

            @Override
            public void OnTimeout(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_google.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_google.setText(R.string.status_online_ping_timeout);
                    }
                });
                if (sequence>=5)
                    pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnException(@NonNull PingInfo pingInfo, @NonNull Exception e, boolean isFatal) {
                status_online_internet_ping_google.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_google.setText(R.string.status_online_ping_timeout);
                    }
                });
                pingGoogle();
            }
        });
        google.Ping("8.8.8.8");
    }
}