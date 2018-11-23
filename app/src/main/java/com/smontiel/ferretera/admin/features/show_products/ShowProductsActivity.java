package com.smontiel.ferretera.admin.features.show_products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.base.ScrollChildSwipeRefreshLayout;
import com.smontiel.ferretera.admin.features.create_product.CreateProductActivity;

import java.util.List;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

public class ShowProductsActivity extends AppCompatActivity implements ShowProductsContract.View {
    public static Intent getStartIntent(Context activity) {
        return new Intent(activity, ShowProductsActivity.class);
    }

    private ShowProductsContract.Presenter presenter = new ShowProductsPresenter(
            this, Injector.provideApiClient());
    private FastItemAdapter<ProductItem> itemAdapter = new FastItemAdapter<>();

    private FloatingActionButton fab;
    private ScrollChildSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View noProductsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        initializeViews();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.products);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this, CommunityMaterial.Icon2.cmd_plus)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(v -> startActivity(CreateProductActivity.getStartIntent(this)));

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
        noProductsView = findViewById(R.id.no_products);
        // Set up progress indicator
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadAllProducts());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(ShowProductsContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        // Make sure setRefreshing() is called after the layout is done with everything else.
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(isLoading));
    }

    @Override
    public void showError(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_error)
                        .colorRes(android.R.color.black))
                .setTitle("Ocurrió un error!")
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create();
        dialog.show();
    }

    @Override
    public void showProductos(List<ProductItem> productos) {
        if (productos.isEmpty()) showNoProductsView();
        else {
            itemAdapter.setNewList(productos);
            recyclerView.setVisibility(View.VISIBLE);
            noProductsView.setVisibility(View.GONE);
        }
    }

    private void showNoProductsView() {
        recyclerView.setVisibility(View.GONE);
        noProductsView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }
}
