package br.com.jasgab.jasgab.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import br.com.jasgab.jasgab.R;

public class FeedbackUtils {

    public void success(View v, Context context){
        Snackbar mSnackBar = Snackbar.make(v, "TOP SNACKBAR", Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view.setBackgroundColor(context.getResources().getColor(R.color.green));
        TextView mainTextView = view.findViewById(R.id.snackbar_text);
        mainTextView.setTextColor(context.getResources().getColor(R.color.white));
        mSnackBar.show();
    }
}
