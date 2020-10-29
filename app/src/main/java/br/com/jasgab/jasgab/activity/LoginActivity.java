package br.com.jasgab.jasgab.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.util.MaskUtil;
import br.com.jasgab.jasgab.util.VerifyCpf;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.dialog.LoginDialog;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model_http.RequestCustomer;
import br.com.jasgab.jasgab.model_http.ResponseCustomer;
import br.com.jasgab.jasgab.pattern.MaskType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        JasgabUtils.hideActionBar(getSupportActionBar());
        JasgabUtils.checkInternetActivity(this, this);

        loadLayout();
        businessRules();
    }

    private EditText login_cpf;
    private InputMethodManager manager;
    private ProgressBar login_submit_progressbar;
    private RelativeLayout login_submit;
    private String cpf;
    private TextView login_error, login_sign_up_text, login_terms_text;
    private CheckBox login_terms;
    private void loadLayout(){
        login_submit = findViewById(R.id.login_submit);
        login_cpf = findViewById(R.id.login_cpf);
        login_submit_progressbar = findViewById(R.id.login_submit_progressbar);
        login_terms = findViewById(R.id.login_terms);
        login_error = findViewById(R.id.login_error);
        login_sign_up_text = findViewById(R.id.login_sign_up_text);
        login_terms_text = findViewById(R.id.login_terms_text);

        login_sign_up_text.setText(Html.fromHtml(getResources().getString(R.string.login_sign_up_text)));
        login_terms_text.setText(Html.fromHtml(getResources().getString(R.string.login_terms_text)));
    }

    private void businessRules(){
        if(AuthDAO.start(this).select() == null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            return;
        }

        if(CustomerDAO.start(this).select() != null) {
            loginEnd();
            return;
        }

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(manager != null) {
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        login_cpf.addTextChangedListener(MaskUtil.insert(login_cpf, MaskType.CPF));
        login_cpf.requestFocus();
        login_cpf.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    login(view);
                    return true;
                }
                return false;
            }
        });

        login_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_terms_text.setTextColor(getResources().getColor(R.color.black));
            }
        });

        login_sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        login_terms_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jasgab.com.br/contratos.html"));
                startActivity(browserIntent);
            }
        });

        login_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void login(View view) {
        JasgabUtils.checkInternetActivity(this, this);

        if(manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(!login_terms.isChecked()){
            login_terms_text.setTextColor(getResources().getColor(R.color.red));
            return;
        }

        login_submit_progressbar.setVisibility(View.VISIBLE);

        cpf = login_cpf.getText().toString();
        if(VerifyCpf.isCPF(cpf)){
            login_error.setVisibility(View.INVISIBLE);
            login_cpf.setBackground(getDrawable(R.drawable.edittext_white));
        }else{
            login_submit_progressbar.setVisibility(View.INVISIBLE);
            login_error.setVisibility(View.VISIBLE);
            login_cpf.setBackground(getDrawable(R.drawable.edittext_white_error));
            return;
        }

        try {
            Call<ResponseCustomer> call = new JasgabApi().customer(new RequestCustomer(cpf), AuthDAO.start(this).select().getToken());
            call.enqueue(new Callback<ResponseCustomer>() {
                @Override
                public void onResponse(@NonNull Call<ResponseCustomer> call, @NonNull Response<ResponseCustomer> response) {
                    ResponseCustomer responseCustomer = response.body();
                    login_submit_progressbar.setVisibility(View.INVISIBLE);
                    if (responseCustomer != null && responseCustomer.getStatus() && responseCustomer.getCustomer() != null) {
                        loginSuccess(responseCustomer.getCustomer());
                        return;
                    }
                    loginFail();
                }

                @Override
                public void onFailure(@NonNull Call<ResponseCustomer> call, @NonNull Throwable t) {
                    internetError();
                }
            });

        }
        catch (Exception e){
            internetError();
        }
    }

    private void loginSuccess(Customer customer) {
        customer.setCpf(cpf);
        CustomerDAO.start(this).insertCustomer(customer);
        loginEnd();
    }

    private void loginEnd(){
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.putExtra("loading", LoadingActivity.LOGIN);
        startActivity(intent);
        finishAffinity();
    }

    private void loginFail(){
        LoginDialog dialog = new LoginDialog();
        dialog.show(getSupportFragmentManager(), "");
    }

    private void internetError(){
        startActivity(new Intent(this, NoConnectionActivity.class));
        finishAffinity();
    }
}
