package com.smontiel.ferretera.admin.features.dashboard;

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
import com.smontiel.ferretera.admin.data.models.Inventario;

import java.util.List;

/**
 * Created by Salvador Montiel on 23/11/18.
 */
public class InventarioItem extends AbstractItem<InventarioItem, InventarioItem.ViewHolder> {
    public final Inventario inventario;

    public InventarioItem(Inventario inventario) {
        this.inventario = inventario;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Glide.with(holder.itemView.getContext())
                .load(inventario.producto.urlFoto)
                .error(new IconicsDrawable(holder.itemView.getContext())
                        .colorRes(R.color.md_grey_500)
                        .icon(GoogleMaterial.Icon.gmd_shopping_basket))
                .into(holder.icon);
        holder.name.setText(inventario.producto.nombre);
        holder.codigoBarras.setText("Codigo barras: " + inventario.producto.codigoBarras);
        holder.formato.setText("");
        holder.cantidad.setText("Cantidad: " + inventario.cantidad);
        holder.precio.setText("$ " + inventario.producto.precioVenta);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_inventory;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_inventory;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView codigoBarras;
        private TextView formato;
        private TextView cantidad;
        private TextView precio;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.productNameTV);
            codigoBarras = itemView.findViewById(R.id.codigoBarrasTV);
            formato = itemView.findViewById(R.id.formatoTV);
            cantidad = itemView.findViewById(R.id.cantidadTV);
            precio = itemView.findViewById(R.id.precioTV);
        }
    }
}
