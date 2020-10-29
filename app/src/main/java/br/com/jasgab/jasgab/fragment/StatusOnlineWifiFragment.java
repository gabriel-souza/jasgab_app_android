package br.com.jasgab.jasgab.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import net.cachapa.expandablelayout.ExpandableLayout;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.pattern.WifiSignalType;
import br.com.jasgab.jasgab.util.InternetUtils;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class StatusOnlineWifiFragment extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status_online_wifi, container, false);
        JasgabUtils.checkWifiActivity(requireContext(), requireActivity());

        setLayuot();
        businessRules();

        return view;
    }

    ConstraintLayout status_online_wifi_expand;
    ExpandableLayout status_online_wifi_expandable;
    ProgressBar status_online_wifi_signal;
    TextView status_online_wifi_signal_title, status_online_wifi_warning_text;
    ImageView status_online_wifi_speedtest;
    private void setLayuot(){
        status_online_wifi_expand = view.findViewById(R.id.status_online_wifi_expand);
        status_online_wifi_expandable = view.findViewById(R.id.status_online_wifi_expandable);
        status_online_wifi_signal = view.findViewById(R.id.status_online_wifi_signal);
        status_online_wifi_signal_title = view.findViewById(R.id.status_online_wifi_signal_title);
        status_online_wifi_warning_text = view.findViewById(R.id.status_online_wifi_warning_text);
        status_online_wifi_speedtest = view.findViewById(R.id.status_online_wifi_speedtest);

        YouTubePlayerView status_online_wifi_video = view.findViewById(R.id.status_online_wifi_video);
        getLifecycle().addObserver(status_online_wifi_video);
    }

    private void businessRules(){
        int signal = InternetUtils.getWifiSignalLevel(requireContext());
        int signalStatus = InternetUtils.getWifiSignalStatus(signal);

        status_online_wifi_signal.setProgress(signal);
        switch (signalStatus){
            case WifiSignalType.Bad:
                setSignalText(status_online_wifi_signal_title, "Ruim");
                setSignalProgress(status_online_wifi_signal, R.drawable.pb_wifi_signal_bad);
                setAlertText(status_online_wifi_warning_text,getResources().getString(R.string.status_online_wifi_warning_text_bad));
                setSpeedVisible(status_online_wifi_speedtest, View.INVISIBLE);
                break;
            case WifiSignalType.Medio:
                setSignalText(status_online_wifi_signal_title,"Médio");
                setSignalProgress(status_online_wifi_signal, R.drawable.pb_wifi_signal_medio);
                setAlertText(status_online_wifi_warning_text,getResources().getString(R.string.status_online_wifi_warning_text_bad));
                setSpeedVisible(status_online_wifi_speedtest, View.INVISIBLE);
                break;
            case WifiSignalType.Good:
                setSignalText(status_online_wifi_signal_title,"Bom");
                setSignalProgress(status_online_wifi_signal, R.drawable.pb_wifi_signal_good);
                setAlertText(status_online_wifi_warning_text,getResources().getString(R.string.status_online_wifi_warning_text_good));
                setSpeedVisible(status_online_wifi_speedtest, View.VISIBLE);
                break;
        }

        status_online_wifi_speedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://teste.jasgab.com.br")));
            }
        });

        status_online_wifi_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_online_wifi_expandable.isExpanded()){
                    status_online_wifi_expandable.collapse();
                } else{
                    status_online_wifi_expandable.expand();
                }
            }
        });
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

    private void setAlertText(final TextView textView, final String text){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(Html.fromHtml(text));
            }
        });
    }
}