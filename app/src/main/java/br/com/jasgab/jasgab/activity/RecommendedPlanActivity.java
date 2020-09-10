package br.com.jasgab.jasgab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.dialog.PlansDialog;
import br.com.jasgab.jasgab.model.Plan;
import br.com.jasgab.jasgab.pattern.PlanType;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class RecommendedPlanActivity extends AppCompatActivity {
    Plan plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_plan);
        getSupportActionBar().hide();

        Integer planType = getIntent().getIntExtra("PlanType", PlanType.ESSENCIAL);
        plan = getRecommendedPlan(planType);
        setLayout();
    }

    private Plan getRecommendedPlan(Integer planType){
        List<Plan> plans = new Plan().getPlans(this);

        for(Plan plan : plans){
            if(plan.getPlanType().equals(planType)){
                return plan;
            }
        }

        return plans.get(0);
    }

    private void setLayout(){
        //ACTIONBAR
        JasgabUtils.setActionBar("Mudar de Plano", getWindow().getDecorView(), this);

        //BUTTON
        Button recommended_plan_button = findViewById(R.id.recommendedplan_submit);
        recommended_plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlansDialog dialog = new PlansDialog();
                Bundle bundle = new Bundle();
                bundle.putString("plan", plan.getName());
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "");
            }
        });

        //PLAN DETAILS
        ImageView recommended_plan_img = findViewById(R.id.recommendedplan_card_img);
        TextView recommended_plan_title = findViewById(R.id.recommendedplan_card_title);
        TextView recommended_plan_detail = findViewById(R.id.recommendedplan_card_subtitle);

        //PLAN BENEFITS
        TextView recommended_plan_benefits_title_one = findViewById(R.id.recommendedplan_benefits_one_text);
        TextView recommended_plan_benefits_title_two = findViewById(R.id.recommendedplan_benefits_two_text);
        TextView recommended_plan_benefits_title_three = findViewById(R.id.recommendedplan_benefits_three_text);
        TextView recommended_plan_benefits_title_four = findViewById(R.id.recommendedplan_benefits_four_text);

        //SET LAYOUT
        recommended_plan_img.setBackground(plan.getImage());
        recommended_plan_title.setText(plan.getName());
        recommended_plan_detail.setText(plan.getDetail());

        recommended_plan_benefits_title_one.setText(plan.getBenefits().get(0));

        recommended_plan_benefits_title_two.setText(plan.getBenefits().get(1));

        recommended_plan_benefits_title_three.setText(plan.getBenefits().get(2));

        recommended_plan_benefits_title_four.setText(plan.getBenefits().get(3));
    }
}