package br.com.jasgab.jasgab.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.jasgab.jasgab.MainActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.dialog.StatusOnlineInternetDialog;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.ResponseIsp;
import br.com.jasgab.jasgab.util.InternetUtils;
import br.com.jasgab.jasgab.util.JasgabUtils;
import me.impa.pinger.PingInfo;
import me.impa.pinger.Pinger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusOnlineInternetFragment extends Fragment {

    View mView;
    TextView status_online_internet_isp,
            status_online_internet_nat,
            status_online_internet_ping_gateway,
            status_online_internet_ping_jasgab,
            status_online_internet_ping_google;
    ImageView status_online_internet_question_mark;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_status_online_internet, container, false);
        context = getContext();
        JasgabUtils.checkWifiActivity(context, requireActivity());

        run();

        return mView;
    }

    private void run(){
        getLayout();
        setIspNat();
        startPing();
    }
    public void getLayout(){
        status_online_internet_isp = mView.findViewById(R.id.status_online_internet_isp);
        status_online_internet_nat = mView.findViewById(R.id.status_online_internet_nat);
        status_online_internet_ping_gateway = mView.findViewById(R.id.status_online_internet_ping_gateway);
        status_online_internet_ping_jasgab = mView.findViewById(R.id.status_online_internet_ping_jasgab);
        status_online_internet_ping_google = mView.findViewById(R.id.status_online_internet_ping_google);
        status_online_internet_question_mark = mView.findViewById(R.id.status_online_internet_question_mark);

        status_online_internet_question_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusOnlineInternetDialog dialog = new StatusOnlineInternetDialog();
                dialog.show(requireFragmentManager(), "StatusOnlineInternetDialog");
            }
        });
    }

    private void setIspNat(){
        //TODO REQUEST TO GET ISP

        Auth auth = AuthDAO.start(requireContext()).select();
        if(auth == null){
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finishAffinity();
            return;
        }

        Call<ResponseIsp> call = new JasgabApi().isp(auth.getToken());
        call.enqueue(new Callback<ResponseIsp>() {
            @Override
            public void onResponse(Call<ResponseIsp> call, Response<ResponseIsp> response){
                ResponseIsp responseIsp = response.body();
                if(responseIsp != null){
                    if(responseIsp.getStatus()){
                        status_online_internet_isp.setText(responseIsp.getIsp().getIsp());
                        status_online_internet_nat.setText("NAT: " + responseIsp.getIsp().getNat());
                    }
                }
                //TODO REQUEST ERROR
            }

            @Override
            public void onFailure(Call<ResponseIsp> call, Throwable t) {
                //TODO REQUEST ERROR
            }
        });

        status_online_internet_isp.setText("JASGAB TELECOM");
        status_online_internet_nat.setText("NAT: Privado");
    }

    private void startPing() {
        String gatewayHost = InternetUtils.getWifiGateway(context);

        if(!gatewayHost.equals("0.0.0.0")) {
            Pinger gateway = new Pinger();
            gateway.setOnPingListener(new Pinger.OnPingListener() {

                @Override
                public void OnStart(@NonNull PingInfo pingInfo) {
                    //Log.i("PING", String.format("Pinging %s [%s]", pingInfo.ReverseDns, pingInfo.RemoteIp));
                }

                @Override
                public void OnStop(@NonNull PingInfo pingInfo) {
                    status_online_internet_ping_gateway.post(new Runnable() {
                        public void run() {
                            status_online_internet_ping_gateway.setText("stop");
                        }
                    });
                    //Log.i("PING", "Ping complete");
                }

                @Override
                public void OnSendError(@NonNull PingInfo pingInfo, int sequence) {
                    status_online_internet_ping_gateway.post(new Runnable() {
                        public void run() {
                            status_online_internet_ping_gateway.setText("N/A");
                        }
                    });
                    pingInfo.Pinger.Stop(pingInfo.PingId);
                }

                @Override
                public void OnReplyReceived(@NonNull PingInfo pingInfo, final int sequence, final int timeMs) {
                    status_online_internet_ping_gateway.post(new Runnable() {
                        public void run() {
                            status_online_internet_ping_gateway.setText(timeMs + "ms");
                        }
                    });
                }

                @Override
                public void OnTimeout(@NonNull PingInfo pingInfo, int sequence) {
                    status_online_internet_ping_gateway.setText("timeout");
                    if (sequence >= 10)
                        pingInfo.Pinger.Stop(pingInfo.PingId);
                }

                @Override
                public void OnException(@NonNull PingInfo pingInfo, @NonNull Exception e, boolean isFatal) {
                    status_online_internet_ping_gateway.post(new Runnable() {
                        public void run() {
                            status_online_internet_ping_gateway.setText("N/A");
                        }
                    });
                    pingInfo.Pinger.Stop(pingInfo.PingId);
                }
            });
            gateway.Ping(gatewayHost);
        }else{
            status_online_internet_ping_gateway.setText("não encontrado");
        }

        Pinger jasgab = new Pinger();
        jasgab.setOnPingListener(new Pinger.OnPingListener() {

            @Override
            public void OnStart(@NonNull PingInfo pingInfo) {
                //Log.i("PING", String.format("Pinging %s [%s]", pingInfo.ReverseDns, pingInfo.RemoteIp));
            }

            @Override
            public void OnStop(@NonNull PingInfo pingInfo) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText("stop");
                    }
                });
                //Log.i("PING", "Ping complete");
            }

            @Override
            public void OnSendError(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText("N/A");
                    }
                });
                pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnReplyReceived(@NonNull PingInfo pingInfo, final int sequence, final int timeMs) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText(timeMs+"ms");
                    }
                });
            }

            @Override
            public void OnTimeout(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText("timeout");
                    }
                });
                if (sequence>=10)
                    pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnException(@NonNull PingInfo pingInfo, @NonNull Exception e, boolean isFatal) {
                status_online_internet_ping_jasgab.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_jasgab.setText("N/A");
                    }
                });
                pingInfo.Pinger.Stop(pingInfo.PingId);
            }
        });
        jasgab.Ping("jasgab.com.br");

        Pinger google = new Pinger();
        google.setOnPingListener(new Pinger.OnPingListener() {

            @Override
            public void OnStart(@NonNull PingInfo pingInfo) {
                //Log.i("PING", String.format("Pinging %s [%s]", pingInfo.ReverseDns, pingInfo.RemoteIp));
            }

            @Override
            public void OnStop(@NonNull PingInfo pingInfo) {
                status_online_internet_ping_google.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_gateway.setText("stop");
                    }
                });
                //Log.i("PING", "Ping complete");
            }

            @Override
            public void OnSendError(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_google.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_google.setText("N/A");
                    }
                });
                pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnReplyReceived(@NonNull PingInfo pingInfo, final int sequence, final int timeMs) {
                status_online_internet_ping_google.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_google.setText(timeMs+"ms");
                    }
                });
            }

            @Override
            public void OnTimeout(@NonNull PingInfo pingInfo, int sequence) {
                status_online_internet_ping_google.post(new Runnable() {
                    public void run() {
                        status_online_internet_ping_google.setText("timeout");
                    }
                });
                if (sequence>=10)
                    pingInfo.Pinger.Stop(pingInfo.PingId);
            }

            @Override
            public void OnException(@NonNull PingInfo pingInfo, @NonNull Exception e, boolean isFatal) {
                status_online_internet_ping_google.setText("N/A");
                pingInfo.Pinger.Stop(pingInfo.PingId);
            }
        });
        google.Ping("google.com");
    }
}