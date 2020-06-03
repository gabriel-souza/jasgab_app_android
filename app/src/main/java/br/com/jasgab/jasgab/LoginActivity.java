package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.pattern.MaskType;
import br.com.jasgab.jasgab.api.MaskUtil;
import br.com.jasgab.jasgab.api.VerifyCpf;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.dialog.LoginDialog;

import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.RequestCustomer;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText ed_cpf;
    private TextView tv_errorLogin;
    private CustomerDAO customerDAO;
    private String cpf;
    private Context context;
    private ProgressBar loginProgressBar;
    private RelativeLayout loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();
        context = this;

        customerDAO = CustomerDAO.start(context);

        ed_cpf = findViewById(R.id.ed_cpf);
        tv_errorLogin = findViewById(R.id.tv_errorLogin);
        loginProgressBar = findViewById(R.id.login_button_progressBar);
        loginButton = findViewById(R.id.login_button);

        ed_cpf.addTextChangedListener(MaskUtil.insert(ed_cpf, MaskType.CPF));

        ResponseCustomer responseCustomer = customerDAO.select();
        if(responseCustomer != null) {
            customerFound();
        }

        ed_cpf.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        ed_cpf.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    acessar(view);
                    return true;
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessar(v);
            }
        });


    }

    public void acessar(final View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        loginProgressBar.setVisibility(View.VISIBLE);
        cpf = ed_cpf.getText().toString();
        if(!VerifyCpf.isCPF(cpf)){
            tv_errorLogin.setText(getString(R.string.tv_cpfdonotexist));
            ed_cpf.setBackground(getDrawable(R.drawable.edittext_white_error));
            loginProgressBar.setVisibility(View.INVISIBLE);
            return;
        }else{
            tv_errorLogin.setText(getString(R.string.tv_vazia));
            ed_cpf.setBackground(getDrawable(R.drawable.edittext_white));
        }

        Auth auth = AuthDAO.start(this).select();
        if(auth == null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            return;
        }

        final RequestCustomer requestCustomer = new RequestCustomer(cpf);

        Call<ResponseCustomer> call = new JasgabApi().customer(requestCustomer, auth.getToken());
        call.enqueue(new Callback<ResponseCustomer>() {
            @Override
            public void onResponse(Call<ResponseCustomer> call, Response<ResponseCustomer> response) {
                ResponseCustomer responseCustomer = response.body();
                loginProgressBar.setVisibility(View.INVISIBLE);
                if(responseCustomer != null){
                    if(responseCustomer.getStatus()){
                        Customer customer = responseCustomer.getCustomer();
                        customer.setCpf(cpf);
                        CustomerDAO.start(context).insertCustomer(customer);
                        customerFound();
                    }else{
                        customerNotFound();
                    }
                }
                else{
                    customerNotFound();
                }
            }

            @Override
            public void onFailure(Call<ResponseCustomer> call, Throwable t) {
                customerNotFound();
            }
        });
    }

    private void customerFound() {
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void customerNotFound(){
        LoginDialog dialog = new LoginDialog();
        dialog.show(getSupportFragmentManager(), "customerNotFound");
    }
}
