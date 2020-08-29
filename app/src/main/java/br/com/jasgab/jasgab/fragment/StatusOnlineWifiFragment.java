package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.pattern.WifiSignalType;
import br.com.jasgab.jasgab.util.InternetUtils;

public class StatusOnlineWifiFragment extends Fragment {
    View mView;

    VideoView status_online_wifi_video;
    ConstraintLayout status_online_wifi_expand;
    ExpandableLayout status_online_wifi_expandable;
    ProgressBar status_online_wifi_signal;
    TextView status_online_wifi_signal_title;
    ImageView status_online_wifi_speedtest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_status_online_wifi, container, false);

        run();

        return mView;
    }

    private void run(){
        getLayout();
        setFun();
        setVideoPlayer();
    }

    private void getLayout(){
        status_online_wifi_expand = mView.findViewById(R.id.status_online_wifi_expand);
        status_online_wifi_video = mView.findViewById(R.id.status_online_wifi_video);
        status_online_wifi_expandable = mView.findViewById(R.id.status_online_wifi_expandable);
        status_online_wifi_signal = mView.findViewById(R.id.status_online_wifi_signal);
        status_online_wifi_signal_title = mView.findViewById(R.id.status_online_wifi_signal_title);
        status_online_wifi_speedtest = mView.findViewById(R.id.status_online_wifi_speedtest);
    }

    private void setFun(){
        setSignalWifi();
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSignalWifi();
                handler.postDelayed(this, 10000);
            }
        }, 10000);*/

        status_online_wifi_speedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO GO TO INTERNET PAGE SPEEDTEST
            }
        });

        status_online_wifi_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_online_wifi_expandable.isExpanded()){
                    status_online_wifi_expandable.collapse();
                }else{
                    status_online_wifi_expandable.expand();
                } 
            }
        });
    }

    private void setSignalWifi(){
        int signal = InternetUtils.getWifiSignalLevel(requireContext());
        int signalStatus = InternetUtils.getWifiSignalStatus(signal);

        status_online_wifi_signal.setProgress(signal);
        switch (signalStatus){
            case WifiSignalType.Bad:
                setSignalText(status_online_wifi_signal_title, "Ruim");
                setSignalProgress(status_online_wifi_signal, R.drawable.pb_wifi_signal_bad);
                setSpeedVisible(status_online_wifi_speedtest,View.INVISIBLE);
                break;
            case WifiSignalType.Medio:
                setSignalText(status_online_wifi_signal_title,"Médio");
                setSignalProgress(status_online_wifi_signal, R.drawable.pb_wifi_signal_medio);
                setSpeedVisible(status_online_wifi_speedtest,View.INVISIBLE);
                break;
            case WifiSignalType.Good:
                setSignalText(status_online_wifi_signal_title,"Bom");
                setSignalProgress(status_online_wifi_signal, R.drawable.pb_wifi_signal_good);
                setSpeedVisible(status_online_wifi_speedtest,View.VISIBLE);
                break;
        }
    }

    private void setSpeedVisible(final ImageView imageView, final int is){
        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(is);
            }
        });
    }

    private void setSignalProgress(final ProgressBar progressBar, final int drawable){
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgressDrawable(ContextCompat.getDrawable(requireActivity(), drawable));
            }
        });
    }

    private void setSignalText(final TextView textView, final String text){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

    private void setVideoPlayer(){
        status_online_wifi_video.setVideoPath("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4");
        MediaController mediaController = new MediaController(requireContext());
        status_online_wifi_video.setMediaController(mediaController);
        mediaController.setAnchorView(status_online_wifi_video);
    }
}