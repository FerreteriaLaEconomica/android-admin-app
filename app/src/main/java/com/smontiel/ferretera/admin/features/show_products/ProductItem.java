package com.smontiel.ferretera.admin.features.show_products;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.models.Producto;

import java.util.List;

/**
 * Created by Salvador Montiel on 4/11/18.
 */
public class ProductItem extends AbstractItem<ProductItem, ProductItem.ViewHolder> {
    public final Producto product;

    public ProductItem(Producto product) {
        this.product = product;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Glide.with(holder.itemView.getContext())
                .load(product.urlFoto)
                .error(new IconicsDrawable(holder.itemView.getContext())
                        .colorRes(R.color.md_grey_500)
                        .icon(GoogleMaterial.Icon.gmd_shopping_basket))
                .into(holder.icon);
        holder.name.setText(product.nombre);
        holder.codigoBarras.setText("Codigo barras: " + product.codigoBarras);
        holder.formato.setText("");
        holder.categoria.setText(product.categoria);
        holder.precio.setText("$ " + product.precioVenta);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_product;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_product;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView codigoBarras;
        private TextView formato;
        private TextView categoria;
        private TextView precio;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.productNameTV);
            codigoBarras = itemView.findViewById(R.id.codigoBarrasTV);
            formato = itemView.findViewById(R.id.formatoTV);
            categoria = itemView.findViewById(R.id.categoriaTV);
            precio = itemView.findViewById(R.id.precioTV);
        }
    }
}
