package br.com.jasgab.jasgab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.islamkhsh.CardSliderIndicator;
import com.github.islamkhsh.CardSliderViewPager;
import com.github.islamkhsh.viewpager2.ViewPager2;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.dialog.PlansDialog;
import br.com.jasgab.jasgab.model.Plan;
import br.com.jasgab.jasgab.list.PlanSlideAdapter;
import br.com.jasgab.jasgab.pattern.PlanType;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class PlansActivity extends AppCompatActivity {

    List<Plan> mPlans;
    Context mContext;
    int plan_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        JasgabUtils.hideActionBar(getSupportActionBar());
        mContext = this;
        mPlans = new Plan().getPlans(this);

        setLayout();
    }

    private void setLayout(){
        //ACTIONBAR
        JasgabUtils.setActionBar("Mudar de Plano", getWindow().getDecorView(), this);

        //LAYOUT
        createSlider();
        Button plans_button_contract = findViewById(R.id.plans_submit);
        plans_button_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlans.get(plan_position).getPlanType() == PlanType.DEFAULT) {
                    startActivity(new Intent(mContext, FormRecommendedPlanActivity.class));
                    return;
                }

                PlansDialog dialog = new PlansDialog();
                Bundle bundle = new Bundle();
                bundle.putString("plan", mPlans.get(plan_position).getName());
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "");
            }
        });


    }

    private void createSlider() {
        CardSliderViewPager cardSliderViewPager = findViewById(R.id.plans_cardslider);
        cardSliderViewPager.setAdapter(new PlanSlideAdapter(mPlans));
        cardSliderViewPager.setSmallScaleFactor(0.9f);
        cardSliderViewPager.setSmallAlphaFactor(1F);
        cardSliderViewPager.setAutoSlideTime(CardSliderViewPager.STOP_AUTO_SLIDING);
        CardSliderIndicator cardSliderIndicator = findViewById(R.id.plans_indicator);
        cardSliderIndicator.setIndicatorsToShow(5);

        cardSliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                changeBenefits(position);
            }
        });
    }

    private void changeBenefits(int position){
        plan_position = position;
        TextView plans_benefits = findViewById(R.id.plans_benefits_text);
        LinearLayout plans_benefits_container = findViewById(R.id.plans_benefits);
        LottieAnimationView recommended_plan = findViewById(R.id.plan_avd);
        TextView plans_button = findViewById(R.id.plans_submit);

        if(position < (mPlans.size() - 1 )) {
            plans_benefits.setVisibility(View.VISIBLE);
            plans_benefits_container.setVisibility(View.VISIBLE);
            recommended_plan.setVisibility(View.INVISIBLE);
            plans_button.setText("ALTERAR PLANO");

            Plan plan = mPlans.get(position);

            //Benefits
            TextView plan_benefits_title_one = findViewById(R.id.plans_benefits_one_text);
            plan_benefits_title_one.setText(plan.getBenefits().get(0));

            TextView plan_benefits_title_two = findViewById(R.id.plans_benefits_two_text);
            plan_benefits_title_two.setText(plan.getBenefits().get(1));

            TextView plan_benefits_title_three = findViewById(R.id.plans_benefits_three_text);
            plan_benefits_title_three.setText(plan.getBenefits().get(2));

            TextView plan_benefits_title_four = findViewById(R.id.plans_benefits_four_text);
            plan_benefits_title_four.setText(plan.getBenefits().get(3));

        }else{
            plans_benefits.setVisibility(View.INVISIBLE);
            plans_benefits_container.setVisibility(View.INVISIBLE);
            recommended_plan.setVisibility(View.VISIBLE);
            plans_button.setText("CONTINUAR");
        }
    }
}