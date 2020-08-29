package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.islamkhsh.CardSliderIndicator;
import com.github.islamkhsh.CardSliderViewPager;
import com.github.islamkhsh.viewpager2.ViewPager2;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.adapter.PlanSlideAdapter;
import br.com.jasgab.jasgab.model.Plan;

public class PlansFragment extends Fragment {
    View mView;
    List<Plan> mPlans;
    Integer mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mView = view;

        createSlider(view);
        Button plans_button_contract = view.findViewById(R.id.plans_button);
        plans_button_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO DO Something with plan (mPosition)
            }
        });

        return view;
    }

    public void createSlider(View view) {

        List<Plan> plans = new Plan().getPlans(requireContext());
        mPlans = plans;


        CardSliderViewPager cardSliderViewPager = (CardSliderViewPager) view.findViewById(R.id.plans_cardslider);
        cardSliderViewPager.setAdapter(new PlanSlideAdapter(plans));
        cardSliderViewPager.setSmallScaleFactor(0.9f);
        cardSliderViewPager.setSmallAlphaFactor(1F);
        cardSliderViewPager.setAutoSlideTime(CardSliderViewPager.STOP_AUTO_SLIDING);
        CardSliderIndicator cardSliderIndicator = (CardSliderIndicator) view.findViewById(R.id.plans_cardslider_indicator);
        cardSliderIndicator.setIndicatorsToShow(CardSliderIndicator.UNLIMITED_INDICATORS);

        cardSliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                changeBenefits(position);
                mPosition = position;
            }
        });

    }

    public void changeBenefits(int position){
        Plan plan = (Plan) mPlans.get(position);

        //Benefits
        TextView plan_benefits_title_one = mView.findViewById(R.id.recommended_plan_benefits_title_one);
        TextView plan_benefits_description_one = mView.findViewById(R.id.recommended_plan_benefits_description_one);
        plan_benefits_title_one.setText(plan.getBenefits().get(0).get(0));
        plan_benefits_description_one.setText(plan.getBenefits().get(0).get(1));

        TextView plan_benefits_title_two = mView.findViewById(R.id.recommended_plan_benefits_title_two);
        TextView plan_benefits_description_two = mView.findViewById(R.id.recommended_plan_benefits_description_two);
        plan_benefits_title_two.setText(plan.getBenefits().get(1).get(0));
        plan_benefits_description_two.setText(plan.getBenefits().get(1).get(1));

        TextView plan_benefits_title_three = mView.findViewById(R.id.recommended_plan_benefits_title_three);
        TextView plan_benefits_description_three = mView.findViewById(R.id.recommended_plan_benefits_description_three);
        plan_benefits_title_three.setText(plan.getBenefits().get(2).get(0));
        plan_benefits_description_three.setText(plan.getBenefits().get(2).get(1));

        TextView plan_benefits_title_four = mView.findViewById(R.id.recommended_plan_benefits_title_four);
        TextView plan_benefits_description_four = mView.findViewById(R.id.recommended_plan_benefits_description_four);
        plan_benefits_title_four.setText(plan.getBenefits().get(3).get(0));
        plan_benefits_description_four.setText(plan.getBenefits().get(3).get(1));
    }
}