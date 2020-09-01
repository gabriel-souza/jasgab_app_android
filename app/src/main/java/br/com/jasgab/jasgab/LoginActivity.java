package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Objects;

import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.dialog.NoConnectionDialog;
import br.com.jasgab.jasgab.util.MaskUtil;
import br.com.jasgab.jasgab.util.VerifyCpf;
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

    private CustomerDAO customerDAO;
    private EditText ed_cpf;
    private InputMethodManager manager;
    private ProgressBar pb_login;
    private RelativeLayout login_submit;
    private String cpf;
    private TextView msg_login_cpf_wrong, msg_login_sign_up, msg_login_terms;
    private CheckBox cb_login_terms;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        customerDAO = CustomerDAO.start(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        login_submit = findViewById(R.id.login_submit);

        ed_cpf = findViewById(R.id.login_cpf);

        pb_login = findViewById(R.id.login_submit_progressbar);

        cb_login_terms = findViewById(R.id.login_terms);

        msg_login_terms = findViewById(R.id.msg_login_terms);
        msg_login_sign_up = findViewById(R.id.msg_login_sign_up);
        msg_login_cpf_wrong = findViewById(R.id.login_error);

        msg_login_sign_up.setText(Html.fromHtml(this.getResources().getString(R.string.msg_login_sign_up)));


        String msg_login_terms_string = this.getResources().getString(R.string.msg_login_terms);
        msg_login_terms.setText(Html.fromHtml(msg_login_terms_string));

        run();
    }

    private void run(){
        auth = AuthDAO.start(this).select();
        if(auth == null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            return;
        }

        ResponseCustomer responseCustomer = customerDAO.select();
        if(responseCustomer != null) {
            end();
        }

        ed_cpf.addTextChangedListener(MaskUtil.insert(ed_cpf, MaskType.CPF));
        ed_cpf.requestFocus();
        if(manager != null) {
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        ed_cpf.setOnKeyListener(new View.OnKeyListener()
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

        cb_login_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_login_terms.setTextColor(getResources().getColor(R.color.black));
            }
        });

        msg_login_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        msg_login_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void login(View view) {
        if(manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(!cb_login_terms.isChecked()){
            msg_login_terms.setTextColor(getResources().getColor(R.color.red));
            return;
        }

        pb_login.setVisibility(View.VISIBLE);

        cpf = ed_cpf.getText().toString();
        if(!VerifyCpf.isCPF(cpf)){
            pb_login.setVisibility(View.INVISIBLE);
            msg_login_cpf_wrong.setVisibility(View.VISIBLE);
            ed_cpf.setBackground(getDrawable(R.drawable.edittext_white_error));
            return;
        }else{
            msg_login_cpf_wrong.setVisibility(View.INVISIBLE);
            ed_cpf.setBackground(getDrawable(R.drawable.edittext_white));
        }

        try {
            Call<ResponseCustomer> call = new JasgabApi().customer(new RequestCustomer(cpf), auth.getToken());
            call.enqueue(new Callback<ResponseCustomer>() {
                @Override
                public void onResponse(Call<ResponseCustomer> call, Response<ResponseCustomer> response) {
                    ResponseCustomer responseCustomer = response.body();
                    pb_login.setVisibility(View.INVISIBLE);
                    if (responseCustomer != null) {
                        if (responseCustomer.getStatus()) {
                            success(responseCustomer.getCustomer());
                        } else {
                            fail();
                        }
                    } else {
                        fail();
                    }
                }

                @Override
                public void onFailure(Call<ResponseCustomer> call, Throwable t) {
                    internetErro();
                }

            });

        }
        catch (Exception ignored){ }
    }

    private void success(Customer customer) {
        if(customer != null) {
            customer.setCpf(cpf);
            customerDAO.insertCustomer(customer);
        }
        end();
    }

    private void fail(){
        LoginDialog dialog = new LoginDialog();
        dialog.show(getSupportFragmentManager(), "customerNotFound");
    }

    private void internetErro(){
        NoConnectionDialog dialog = new NoConnectionDialog();
        dialog.show(getSupportFragmentManager(), "");
    }

    private void end(){
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.putExtra("loading", LoadingActivity.LOGIN);
        startActivity(intent);
        finishAffinity();
    }
}
