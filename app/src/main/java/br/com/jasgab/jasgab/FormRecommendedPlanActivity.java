package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import br.com.jasgab.jasgab.pattern.PlanType;
import br.com.jasgab.jasgab.util.JasgabUtils;
import io.apptik.widget.MultiSlider;

public class FormRecommendedPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_recommended_plan);
        JasgabUtils.hideActionBar(getSupportActionBar());

        setLayout();
    }

    private TextView first_step_number,
            first_step_counter,
            second_step_number,
            third_step_number,
            third_step_counter,
            fourth_step_number,
            fourth_step_counter;

    private CheckBox second_step_iptv;
    private CheckBox second_step_filmeonline;

    private int devicesConnected, devicesWatching, devicesVideoGame;

    private void setLayout(){
        //ACTIONBAR
        JasgabUtils.setActionBar("Descubra o melhor plano", getWindow().getDecorView(), this);

        //LAYOUT
        first_step_number = findViewById(R.id.formrecommendedplan_form_one_number);
        first_step_counter = findViewById(R.id.formrecommendedplan_form_one_counter);
        MultiSlider first_step_slider = findViewById(R.id.formrecommendedplan_form_one_slider);
        first_step_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                first_step_counter.setText(String.format("Pessoas: %d", value));
                if(value == 10){
                    first_step_counter.setText(String.format("Pessoas: %d ou mais", value));
                }
                first_step_number.setText("");
                first_step_number.setBackground(getDrawable(R.drawable.ic_recommended_plan_check_dot));
                devicesConnected = value;
            }
        });

        second_step_number = findViewById(R.id.formrecommendedplan_form_two_number);
        CheckBox second_step_youtube = findViewById(R.id.formrecommendedplan_form_two_youtube);
        CheckBox second_step_netflix = findViewById(R.id.formrecommendedplan_form_two_netflix);
        second_step_iptv = findViewById(R.id.formrecommendedplan_form_two_iptv);
        second_step_filmeonline = findViewById(R.id.formrecommendedplan_form_two_filmesonline);
        onClickChangeSecondStepNumber(second_step_youtube);
        onClickChangeSecondStepNumber(second_step_netflix);
        onClickChangeSecondStepNumber(second_step_iptv);
        onClickChangeSecondStepNumber(second_step_filmeonline);

        third_step_number = findViewById(R.id.formrecommendedplan_form_number);
        third_step_counter = findViewById(R.id.formrecommendedplan_form_counter);
        MultiSlider third_step_slider = findViewById(R.id.formrecommendedplan_form_slider);
        third_step_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                third_step_counter.setText(String.format("Pessoas: %d", value));
                if(value == 10){
                    third_step_counter.setText(String.format("Pessoas: %d ou mais", value));
                }
                third_step_number.setText("");
                third_step_number.setBackground(getDrawable(R.drawable.ic_recommended_plan_check_dot));
                devicesWatching = value;
            }
        });

        fourth_step_number = findViewById(R.id.formrecommendedplan_form_four_number);
        fourth_step_counter = findViewById(R.id.formrecommendedplan_form_four_counter);
        MultiSlider fourth_step_slider = findViewById(R.id.formrecommendedplan_form_four_slider);
        fourth_step_slider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                fourth_step_counter.setText(String.format("Pessoas: %d", value));
                if(value == 4){
                    fourth_step_counter.setText(String.format("Pessoas: %d ou mais", value));
                }
                fourth_step_number.setText("");
                fourth_step_number.setBackground(getDrawable(R.drawable.ic_recommended_plan_check_dot));
                devicesVideoGame = value;
            }
        });

        Button recommended_plan_button = findViewById(R.id.recommendedplan_submit);
        recommended_plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getingBestPlan();
            }
        });
    }

    private void onClickChangeSecondStepNumber(CheckBox checkBox){
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second_step_number.setText("");
                second_step_number.setBackground(getDrawable(R.drawable.ic_recommended_plan_check_dot));
            }
        });
    }

    private void getingBestPlan(){
        int planType = PlanType.ESSENCIAL;
        boolean iptv = second_step_iptv.isChecked();
        boolean filmeOnline = second_step_filmeonline.isChecked();

        if(devicesConnected >= 5 || devicesWatching >= 4 || iptv || filmeOnline || devicesVideoGame >= 2){
            planType = PlanType.IDEAL;
        }

        if(devicesConnected >= 9 || devicesWatching >= 7 || devicesVideoGame >3) {
            planType = PlanType.ESPECIAL;
        }

        Intent intent = new Intent(this, RecommendedPlanActivity.class);
        intent.putExtra("PlanType", planType);
        startActivity(intent);
        finish();
    }
}