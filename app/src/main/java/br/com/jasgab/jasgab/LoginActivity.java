package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

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
import br.com.jasgab.jasgab.api.MaskUtil;
import br.com.jasgab.jasgab.api.VerifyCpf;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.dialog.LoginDialog;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.RequestCustomer;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.pattern.MaskType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private CustomerDAO mCustomerDAO;
    private EditText mEditTextCPF;
    private InputMethodManager mImm;
    private ProgressBar mLoginProgressBar;
    private RelativeLayout mLogin;
    private String mCPF;
    private TextView mTextViewErrorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mCustomerDAO = CustomerDAO.start(this);
        mEditTextCPF = findViewById(R.id.login_cpf);
        mTextViewErrorLogin = findViewById(R.id.login_errorLogin);
        mLoginProgressBar = findViewById(R.id.login_progressBar);
        mLogin = findViewById(R.id.login_login);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        run();
    }

    private void run(){
        ResponseCustomer responseCustomer = mCustomerDAO.select();
        if(responseCustomer != null) {
            end();
        }

        mEditTextCPF.addTextChangedListener(MaskUtil.insert(mEditTextCPF, MaskType.CPF));
        mEditTextCPF.requestFocus();
        if(mImm != null) {
            mImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        mEditTextCPF.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    login(view);
                    return true;
                }
                return false;
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
    }

    public void login(View view) {
        if(mImm != null) {
            mImm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mLoginProgressBar.setVisibility(View.VISIBLE);

        mCPF = mEditTextCPF.getText().toString();
        if(!VerifyCpf.isCPF(mCPF)){
            mTextViewErrorLogin.setVisibility(View.INVISIBLE);
            mLoginProgressBar.setVisibility(View.INVISIBLE);
            mEditTextCPF.setBackground(getDrawable(R.drawable.edittext_white_error));
            return;
        }else{
            mTextViewErrorLogin.setVisibility(View.VISIBLE);
            mEditTextCPF.setBackground(getDrawable(R.drawable.edittext_white));
        }

        Auth auth = AuthDAO.start(this).select();
        if(auth == null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            return;
        }

        Call<ResponseCustomer> call = new JasgabApi().customer(new RequestCustomer(mCPF), auth.getToken());
        call.enqueue(new Callback<ResponseCustomer>() {
            @Override
            public void onResponse(Call<ResponseCustomer> call, Response<ResponseCustomer> response) {
                ResponseCustomer responseCustomer = response.body();
                mLoginProgressBar.setVisibility(View.INVISIBLE);
                if(responseCustomer != null){
                    if(responseCustomer.getStatus()){
                        success(responseCustomer.getCustomer());
                    }else{
                        fail();
                    }
                }
                else{
                    fail();
                }
            }

            @Override
            public void onFailure(Call<ResponseCustomer> call, Throwable t) {
                fail();
            }
        });
    }

    private void success(Customer customer) {
        if(customer != null) {
            customer.setCpf(mCPF);
            mCustomerDAO.insertCustomer(customer);
        }
        end();
    }

    private void fail(){
        LoginDialog dialog = new LoginDialog();
        dialog.show(getSupportFragmentManager(), "customerNotFound");
    }

    private void end(){
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
