package edu.com.medicalapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.com.medicalapp.Models.maincat.CategoryDetailData;
import edu.com.medicalapp.R;

/**
 * Created by rbpatel on 9/29/2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    private Context applicationContext;
    private CategoryDetailData categoryDetailData;
    OnCategoryClick onUserClickCallback;

    public CourseListAdapter(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public CourseListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_row, viewGroup, false);
        return new CourseListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CourseListAdapter.ViewHolder holder, final int position) {


        holder.title.setText("" + categoryDetailData.getDetails().get(holder.getAdapterPosition()).getCatName());
        if (categoryDetailData.getDetails().get(holder.getAdapterPosition()) != null
                && categoryDetailData.getDetails().get(holder.getAdapterPosition()).getSubCat() != null
                && categoryDetailData.getDetails().get(holder.getAdapterPosition()).getSubCat().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            String subcats = "";
            for (int i = 0; i < categoryDetailData.getDetails().get(holder.getAdapterPosition()).getSubCat().size(); i++) {
                stringBuilder.append(categoryDetailData.getDetails().get(holder.getAdapterPosition()).getSubCat().get(i).getSubCatName() + "/");
            }
            subcats = stringBuilder.substring(0, stringBuilder.length() - 1);
            holder.desc.setText("" + subcats);

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onUserClickCallback != null) {
                        if (!(Integer.parseInt(categoryDetailData.getDetails().get(holder.getAdapterPosition()).getCatId())>3))
                        onUserClickCallback.onCateClick(categoryDetailData.getDetails().get(holder.getAdapterPosition()).getCatId());
                    }

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (categoryDetailData != null && categoryDetailData.getDetails() != null) {
            return categoryDetailData.getDetails().size();
        } else {
            return 0;
        }
    }

    public void setData(CategoryDetailData CourseLists) {
        this.categoryDetailData = CourseLists;
    }

    public void setListener(OnCategoryClick onUserClickCallback) {
        this.onUserClickCallback = onUserClickCallback;
    }


    /**
     * View Holder for CONFIG LIST
     */

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.linearNeet_Ss)
        LinearLayout linearLayout;

        @BindView(R.id.title)
        TextView title;


        @BindView(R.id.desc)
        TextView desc;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnCategoryClick {
        public void onCateClick(String id);
    }

}
