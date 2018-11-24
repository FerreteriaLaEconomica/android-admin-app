package com.smontiel.ferretera.admin.features.show_categories;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.smontiel.ferretera.admin.R;

import java.util.List;

/**
 * Created by Salvador Montiel on 23/11/18.
 */
public class CategoryItem extends AbstractItem<CategoryItem, CategoryItem.ViewHolder> {
    private String name;

    public CategoryItem(String name) {
        this.name = name;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.name.setText(name);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_category;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_category;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTV);
        }
    }
}
