package com.smontiel.ferretera.admin.features.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.models.Sucursal;
import com.smontiel.ferretera.admin.data.models.User;
import com.smontiel.ferretera.admin.features.buscar_codigo_barras.BuscarCodigoActivity;
import com.smontiel.ferretera.admin.features.buscar_codigo_barras.RxSearch;
import com.smontiel.ferretera.admin.features.create_sucursal.CreateSucursalActivity;
import com.smontiel.ferretera.admin.features.show_categories.ShowCategoriesActivity;
import com.smontiel.ferretera.admin.features.show_products.ShowProductsActivity;
import com.smontiel.ferretera.admin.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class DashboardActivity extends AppCompatActivity {
    private static final String KEY_NOMBRE = "KEY_NOMBRE";
    private static final String KEY_APELLIDOS = "KEY_APELLIDOS";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_URL_FOTO = "KEY_URL_FOTO";
    private static final String KEY_TELEFONO = "KEY_TELEFONO";
    private static final String KEY_IS_SUPER_ADMIN = "KEY_IS_SUPER_ADMIN";

    public static Intent getIntent(Context activity, User user) {
        Intent i = new Intent(activity, DashboardActivity.class);
        i.putExtra(KEY_NOMBRE, user.nombre);
        i.putExtra(KEY_APELLIDOS, user.apellidos);
        i.putExtra(KEY_EMAIL, user.email);
        i.putExtra(KEY_URL_FOTO, user.urlFoto);
        i.putExtra(KEY_TELEFONO, user.telefono);
        i.putExtra(KEY_IS_SUPER_ADMIN, user.isSuperAdmin);
        return i;
    }

    private DashboardPresenter presenter;
    Drawer drawer;
    private Toolbar toolbar;
    private CollapsingToolbarLayout ctl;
    private MaterialSearchView searchView;
    User currentUser;

    private AlertDialog progressDialog;
    private DashboardFragment dashboardFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        toolbar = findViewById(R.id.toolbar);
        ctl = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        ctl.setTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        String nombre = bundle.getString(KEY_NOMBRE);
        String apellidos = bundle.getString(KEY_APELLIDOS);
        String email = bundle.getString(KEY_EMAIL);
        boolean isSuperAdmin = bundle.getBoolean(KEY_IS_SUPER_ADMIN);
        currentUser = new User(nombre, apellidos, email, "", "", isSuperAdmin);

        dashboardFragment = (DashboardFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (dashboardFragment == null) {
            dashboardFragment = DashboardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    dashboardFragment, R.id.contentFrame);
        }

        presenter = new DashboardPresenter(dashboardFragment, Injector.provideAuthClient(),
                Injector.provideApiClient(), Injector.provideSharedPrefs());

        progressDialog = new AlertDialog.Builder(this)
                .setTitle("Cargando datos...")
                .setCancelable(false)
                .setView(new ProgressBar(this))
                .create();
        progressDialog.show();

        searchView = findViewById(R.id.search_view);
        RxSearch.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dashboardFragment::filterItems);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(getAccountHeader())
                .withSelectedItemByPosition(-1)
                .withCloseOnClick(true)
                .build();
    }

    void updateDrawerItems(List<Sucursal> sucursales) {
        progressDialog.dismiss();
        List<IDrawerItem> items = new ArrayList<>();
        for (Sucursal s : sucursales) {
            items.add(new PrimaryDrawerItem()
                    .withIdentifier(s.id)
                    .withName(s.nombre)
                    .withIcon(GoogleMaterial.Icon.gmd_local_convenience_store)
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        presenter.loadInventoryBySucursalId(s.id);
                        getSupportActionBar().setSubtitle(s.nombre + " - Inventario");
                        drawer.closeDrawer();
                        return true;
                    })
            );
        }
        if (currentUser.isSuperAdmin) {
            items.add(new PrimaryDrawerItem()
                    .withName("Agregar sucursal")
                    .withIcon(CommunityMaterial.Icon2.cmd_plus)
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        startActivity(CreateSucursalActivity.getStartIntent(this));
                        drawer.closeDrawer();
                        return true;
                    })
            );
        }
        drawer.removeAllItems();
        drawer.addItems(drawerItems(items).toArray(new IDrawerItem[0]));
        drawer.addStickyFooterItem(new PrimaryDrawerItem()
                .withName("Cerrar sesión")
                .withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    drawer.closeDrawer();
                    presenter.logOut();
                    finish();
                    return true;
                }));
        if (sucursales.size() > 0) drawer.setSelection(sucursales.get(0).id, true);
    }

    private List<IDrawerItem> drawerItems(List<IDrawerItem> sucursalItems) {
        List<IDrawerItem> items = new ArrayList<>();
        items.add(new SectionDrawerItem().withName("Sucursales"));
        items.addAll(sucursalItems);
        if (currentUser.isSuperAdmin) {
            items.add(new SectionDrawerItem().withName("Super Admin"));
            items.add(new PrimaryDrawerItem()
                    .withName("Categorías")
                    .withIcon(MaterialDesignIconic.Icon.gmi_collection_bookmark)
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        startActivity(ShowCategoriesActivity.getStartIntent(DashboardActivity.this));
                        return false;
                    })
            );
            items.add(new PrimaryDrawerItem()
                    .withName("Productos")
                    .withIcon(MaterialDesignIconic.Icon.gmi_apps)
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        startActivity(ShowProductsActivity.getStartIntent(DashboardActivity.this));
                        return false;
                    })
            );
        }
        items.add(new DividerDrawerItem());
        return items;
    }

    private AccountHeader getAccountHeader() {
        return new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                        .withIcon(R.drawable.profile)
                        .withName(currentUser.nombre + " " + currentUser.apellidos)
                        .withEmail(currentUser.email))
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(menu.getItem(0));
        menu.getItem(1).setIcon(new IconicsDrawable(this, CommunityMaterial.Icon.cmd_barcode_scan).actionBar().colorRes(R.color.md_white_1000));
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (2018) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra("BARCODE");
                    searchView.showSearch();
                    searchView.setQuery(returnValue, false);
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.barcode_item:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions();
                    }
                }
                startActivityForResult(new Intent(this, BuscarCodigoActivity.class), 2018);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPermissions() {
        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Permiso de cámara")
                        .withMessage("Necesito el permiso de acceso a la cámara para capturar el código de barras")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(dialogPermissionListener)
                .check();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) drawer.closeDrawer();
        else super.onBackPressed();
    }
}
