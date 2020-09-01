package br.com.jasgab.jasgab.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.pattern.PlanType;

public class Plan {

    private String name;
    private String detail;
    private Drawable image;
    private List<String> benefits;
    private Integer planType;

    public Plan(){}

    public Plan(String name, String detail, Drawable image, List<String> benefits, Integer planType){
        this.name = name;
        this.detail = detail;
        this.image = image;
        this.benefits = benefits;
        this.planType = planType;
    }

    public List<String> setBenefits (String one,String two, String three, String four){
        List<String> benefits = new ArrayList<>();
        benefits.add(one);
        benefits.add(two);
        benefits.add(three);
        benefits.add(four);

        return benefits;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public List<Plan> getPlans(Context context){
        List<Plan> plans = new ArrayList<>();

        List<String> benefits_50 = setBenefits(
                context.getResources().getString(R.string.plan_benefits_one_text_enssencial),
                context.getResources().getString(R.string.plan_benefits_two_text_enssencial),
                "50 megas de Download",
                "25 megas de Upload");

        List<String> benefits_100 = setBenefits(
                "Indicado para toda família",
                "Trabalhe e jogue Online",
                "100 megas de Download",
                "50 megas de Upload");

        List<String> benefits_200 = setBenefits(
                "Navegue com toda a Fámilia",
                "Trabalhe e jogue Online",
                "200 megas de Download",
                "100 megas de Upload");

        plans.add(new Plan("PLANO ESSENCIAL", "50 MEGAS", context.getResources().getDrawable(R.drawable.ic_plan_1, null), benefits_50, PlanType.ESSENCIAL));
        plans.add(new Plan("PLANO IDEAL", "100 MEGAS", context.getResources().getDrawable(R.drawable.ic_plan_3, null), benefits_100, PlanType.IDEAL));
        plans.add(new Plan("PLANO ESPECIAL", "200 MEGAS", context.getResources().getDrawable(R.drawable.ic_plan_4, null), benefits_200, PlanType.ESPECIAL));
        plans.add(new Plan("CONSULTE E DESCUBRA", "O melhor plano para você", context.getResources().getDrawable(R.drawable.ic_plan_5, null), benefits_200, PlanType.DEFAULT));

        return plans;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public Drawable getImage() {
        return image;
    }

    public List<String> getBenefits() {
        return benefits;
    }

    public Integer getPlanType() {
        return planType;
    }
}
