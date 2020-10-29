package br.com.jasgab.jasgab.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.CustomerNew;
import br.com.jasgab.jasgab.model_http.ResponseAddress;
import br.com.jasgab.jasgab.model_http.ResponseCustomer;
import br.com.jasgab.jasgab.pattern.MaskType;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.util.MaskUtil;
import br.com.jasgab.jasgab.util.VerifyCpf;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    Auth auth;
    EditText sign_up_name, sign_up_cpf, sign_up_rg, sign_up_born, sign_up_phone, sign_up_email, sign_up_zipCode, sign_up_addressnumber, sign_up_complement;
    Spinner sign_up_due_date, sign_up_payment_method;
    TextView sign_up_address, sign_up_address_complements;
    ProgressBar sign_up_submit_progressbar;
    RelativeLayout sign_up_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        JasgabUtils.hideActionBar(getActionBar());
        JasgabUtils.setActionBar("Cadastro", getWindow().getDecorView(), this);

        run();
    }

    private void run(){
        startConn();
        setLayout();
    }

    private void startConn(){
        auth = AuthDAO.start(this).select();
        if(auth == null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }
    }

    private void setLayout(){
        sign_up_name = findViewById(R.id.sign_up_name);
        sign_up_cpf = findViewById(R.id.sign_up_cpf);
        sign_up_rg = findViewById(R.id.sign_up_rg);
        sign_up_born = findViewById(R.id.sign_up_born);
        sign_up_phone = findViewById(R.id.sign_up_phone);
        sign_up_email = findViewById(R.id.sign_up_email);
        sign_up_zipCode = findViewById(R.id.sign_up_cep);
        sign_up_addressnumber = findViewById(R.id.sign_up_addressnumber);
        sign_up_complement = findViewById(R.id.sign_up_complement);
        sign_up_due_date = findViewById(R.id.sign_up_due_date);
        sign_up_payment_method = findViewById(R.id.sign_up_payment_method);
        sign_up_submit_progressbar = findViewById(R.id.sign_up_submit_progressbar);
        sign_up_submit = findViewById(R.id.sign_up_submit);
        sign_up_address = findViewById(R.id.sign_up_address);
        sign_up_address_complements = findViewById(R.id.sign_up_address_complements);

        //EditText Mask
        sign_up_cpf.addTextChangedListener(MaskUtil.insert(sign_up_cpf, MaskType.CPF));
        sign_up_born.addTextChangedListener(MaskUtil.insert(sign_up_born, MaskType.Born));
        sign_up_phone.addTextChangedListener(MaskUtil.insert(sign_up_phone, MaskType.Phone));
        sign_up_zipCode.addTextChangedListener(MaskUtil.insert(sign_up_zipCode, MaskType.ZipCode));

        //Dropdown menu
        String[] sign_up_due_date_items = new String[]{"10", "20", "01"};
        ArrayAdapter<String> sign_up_due_date_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sign_up_due_date_items);
        sign_up_due_date.setAdapter(sign_up_due_date_adapter);

        String[] sign_up_payment_items = new String[]{"Boleto Itaú", "Boleto Santander", "Na empresa", "Cartão de crédito"};
        ArrayAdapter<String> sign_up_payment_method_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sign_up_payment_items);
        sign_up_payment_method.setAdapter(sign_up_payment_method_adapter);

        //Bold message
        TextView msg_sign_up_terms = findViewById(R.id.msg_sign_up_terms);
        String msg_login_terms_string = this.getResources().getString(R.string.msg_sign_up_terms);
        msg_sign_up_terms.setText(Html.fromHtml(msg_login_terms_string));

        sign_up_zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAddress();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sign_up_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    public void getAddress(){
        String zip_code = sign_up_zipCode.getText().toString();
        if(zip_code.length() != 9){
            return;
        }

        CustomerNew customerNew = new CustomerNew(zip_code);

        try {
            Call<ResponseAddress> call = new JasgabApi().getAddress(customerNew, auth.getToken());
            call.enqueue(new Callback<ResponseAddress>() {
                @Override
                public void onResponse(@NonNull Call<ResponseAddress> call, @NonNull Response<ResponseAddress> response) {
                    ResponseAddress responseAddress = response.body();
                    if (responseAddress != null && responseAddress.isOk()) {
                        addressFound(responseAddress);
                    } else {
                        addressNotFound();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResponseAddress> call, @NonNull Throwable t) {
                    addressNotFound();
                }
            });
        }
        catch (Exception ignored){ }
    }

    private void addressFound(ResponseAddress address){
        sign_up_address.setText(address.getAddress());
        sign_up_address_complements.setText(String.format("%s, %s - %s", address.getDistrict(), address.getCity(), address.getState()));
    }

    private void addressNotFound(){
        sign_up_address.setText(R.string.sign_up_address_notfound);
        sign_up_address_complements.setText("");
    }

    public void signUp() {
        CustomerNew customerNew = new CustomerNew(
                sign_up_addressnumber.getText().toString(),
                sign_up_born.getText().toString(),
                sign_up_complement.getText().toString(),
                sign_up_cpf.getText().toString(),
                sign_up_due_date.getSelectedItem().toString(),
                sign_up_email.getText().toString(),
                sign_up_name.getText().toString(),
                sign_up_payment_method.getSelectedItem().toString(),
                sign_up_phone.getText().toString(),
                "",
                sign_up_rg.getText().toString(),
                sign_up_zipCode.getText().toString());

        if(!dataIsValid(customerNew)){
            return;
        }

        sign_up_submit_progressbar.setVisibility(View.VISIBLE);
        try {
            Call<ResponseCustomer> call = new JasgabApi().customerNew(customerNew, auth.getToken());
            call.enqueue(new Callback<ResponseCustomer>() {
                @Override
                public void onResponse(@NonNull Call<ResponseCustomer> call, @NonNull Response<ResponseCustomer> response) {
                    ResponseCustomer responseCustomer = response.body();
                    sign_up_submit_progressbar.setVisibility(View.INVISIBLE);
                    if (responseCustomer != null) {
                        if (responseCustomer.getStatus()) {
                            success();
                        } else {
                            fail();
                        }
                    } else {
                        fail();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseCustomer> call, @NonNull Throwable t) {
                    fail();
                }

            });

        }
        catch (Exception ignored){ }
    }

    private void success(){
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.putExtra("loading", LoadingActivity.SIGN);
        startActivity(intent);
        finish();
    }

    private void fail(){
        sign_up_name.setText("");
        sign_up_cpf.setText("");
        sign_up_rg.setText("");
        sign_up_born.setText("");
        sign_up_phone.setText("");
        sign_up_email.setText("");
        sign_up_zipCode.setText("");
        sign_up_addressnumber.setText("");
        sign_up_complement.setText("");

        CustomerNew customerNew = new CustomerNew();
        dataIsValid(customerNew);
    }

    private boolean dataIsValid(CustomerNew customerNew){
        boolean isValid = true;

        if(customerNew == null){
            return false;
        }

        if(customerNew.getName() == null || customerNew.getName().length() <= 3){
            setErro(R.id.sign_up_layout_name, R.id.sign_up_title_name);
            isValid = false;
        }

        if(!VerifyCpf.isCPF(customerNew.getCpf())){
            setErro(R.id.sign_up_layout_cpf, R.id.sign_up_title_cpf);
            isValid = false;
        }

        if(customerNew.getRg() == null || customerNew.getRg().length() < 6 || customerNew.getRg().length() > 16){
            setErro(R.id.sign_up_layout_rg, R.id.sign_up_title_rg);
            isValid = false;
        }

        if(customerNew.getBorn() == null || !(customerNew.getBorn().length() == 10)){
            setErro(R.id.sign_up_layout_born, R.id.sign_up_title_born);
            isValid = false;
        }

        if(customerNew.getPhone() == null || customerNew.getPhone().length() == 0){
            setErro(R.id.sign_up_layout_phone, R.id.sign_up_title_phone);
            isValid = false;
        }

        if(customerNew.getZip_code() == null || !(customerNew.getZip_code().length() == 9)){
            setErro(R.id.sign_up_layout_zipCode, R.id.sign_up_title_zipCode);
            isValid = false;
        }

        if(customerNew.getAddress_number() == null || customerNew.getAddress_number().length() == 0){
            setErro(R.id.sign_up_layout_addressNumber, R.id.sign_up_title_addressNumber);
            isValid = false;
        }

        return isValid;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setErro(int layoutId, int textViewId){
        ConstraintLayout layout = findViewById(layoutId);
        TextView textView = findViewById(textViewId);
        layout.setBackground(getResources().getDrawable(R.drawable.background_ed_sign_up_error, null));
        textView.setTextColor(getResources().getColor(R.color.red));
    }
}