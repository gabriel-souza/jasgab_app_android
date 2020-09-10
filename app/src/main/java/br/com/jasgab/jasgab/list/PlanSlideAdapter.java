package br.com.jasgab.jasgab.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.islamkhsh.CardSliderAdapter;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.model.Plan;

public final class PlanSlideAdapter extends CardSliderAdapter {
    private final List<Plan> plans;

    public void bindVH(PlanSlideAdapter.PlanViewHolder holder, int position) {
        Plan plan = (Plan) this.plans.get(position);
        View view = holder.itemView;

        //Slide
        ImageView plans_slide_img = view.findViewById(R.id.plans_slide_img);
        TextView plans_slide_plan = view.findViewById(R.id.plans_slide_plan);
        TextView plans_slide_plan_detail = view.findViewById(R.id.plans_slide_plan_detail);
        plans_slide_img.setBackground(plan.getImage());
        plans_slide_plan.setText(plan.getName());
        plans_slide_plan_detail.setText(plan.getDetail());
    }

    public void bindVH(RecyclerView.ViewHolder viewHolder, int viewType) {
        this.bindVH((PlanSlideAdapter.PlanViewHolder)viewHolder, viewType);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_plan_slider, viewGroup, false);

        return new PlanViewHolder(view);
    }

    public int getItemCount() {
        return this.plans.size();
    }

    public PlanSlideAdapter(List<Plan> plans) {
        this.plans = plans;
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder{
        PlanViewHolder(View view) {
            super(view);
        }
    }
}
