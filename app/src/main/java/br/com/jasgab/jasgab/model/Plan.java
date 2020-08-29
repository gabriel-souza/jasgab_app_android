package br.com.jasgab.jasgab.model;

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
    private List<List<String>> benefits;
    private Integer planType;

    public Plan(){}

    public Plan(String name, String detail, Drawable image, List<List<String>> benefits, Integer planType){
        this.name = name;
        this.detail = detail;
        this.image = image;
        this.benefits = benefits;
        this.planType = planType;
    }

    public List<String> setBenefit(String title, String description){
        List<String> benefit = new ArrayList<>();
        benefit.add(title);
        benefit.add(description);

        return benefit;
    }
    public List<List<String>> setBenefits(List<String> one, List<String> two, List<String> three, List<String> four){
        List<List<String>> benefits = new ArrayList<>();
        benefits.add(one);
        benefits.add(two);
        benefits.add(three);
        benefits.add(four);

        return benefits;
    }

    public List<Plan> getPlans(Context context){
        List<Plan> plans = new ArrayList<>();

        List<List<String>> benefits_50 = setBenefits(
                setBenefit("Titulo 1 - Plano 50 megas", "1 50 - descrição do produto"),
                setBenefit("Titulo 2 - Plano 50 megas", "2 50 - descrição do produto"),
                setBenefit("Titulo 3 - Plano 50 megas", "3 50 - descrição do produto"),
                setBenefit("Titulo 4 - Plano 50 megas", "4 50 - descrição do produto"));

        List<List<String>> benefits_100 = setBenefits(
                setBenefit("Titulo 1 - Plano 100 megas", "1 100 - descrição do produto"),
                setBenefit("Titulo 2 - Plano 100 megas", "2 100 - descrição do produto"),
                setBenefit("Titulo 3 - Plano 100 megas", "3 100 - descrição do produto"),
                setBenefit("Titulo 4 - Plano 100 megas", "4 100 - descrição do produto"));

        List<List<String>> benefits_200 = setBenefits(
                setBenefit("Titulo 1 - Plano 200 megas", "1 200 - descrição do produto"),
                setBenefit("Titulo 2 - Plano 200 megas", "2 200 - descrição do produto"),
                setBenefit("Titulo 3 - Plano 200 megas", "3 200 - descrição do produto"),
                setBenefit("Titulo 4 - Plano 200 megas", "4 200 - descrição do produto"));

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

    public List<List<String>> getBenefits() {
        return benefits;
    }

    public Integer getPlanType() {
        return planType;
    }
}
