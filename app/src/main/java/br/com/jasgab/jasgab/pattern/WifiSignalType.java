package br.com.jasgab.jasgab.pattern;

import android.content.Context;

import br.com.jasgab.jasgab.R;

public class WifiSignalType {
    public final static int Bad = 0;
    public final static int Medio = 1;
    public final static int Good = 2;

    public static String getSinalType(int signalType){
        switch (signalType){
            case Bad:
                return "Ruim";
            case Medio:
                return "Medio";
            case Good:
                return "Bom";
        }
        return "N/A";
    }

    public static int getSinalColor(Context context, int signalType){
        switch (signalType){
            case Bad:
                return context.getResources().getColor(R.color.red);
            case Medio:
                return context.getResources().getColor(R.color.yellow);
            case Good:
                return context.getResources().getColor(R.color.green);
        }
        return context.getResources().getColor(R.color.red);
    }
}
