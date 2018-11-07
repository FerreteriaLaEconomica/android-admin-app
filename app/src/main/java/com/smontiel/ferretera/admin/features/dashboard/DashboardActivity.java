package com.smontiel.ferretera.admin.features.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
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
import com.smontiel.ferretera.admin.features.show_products.ShowProductsActivity;
import com.smontiel.ferretera.admin.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

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
    User currentUser;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String nombre = bundle.getString(KEY_NOMBRE);
        String apellidos = bundle.getString(KEY_APELLIDOS);
        String email = bundle.getString(KEY_EMAIL);
        boolean isSuperAdmin = bundle.getBoolean(KEY_IS_SUPER_ADMIN);
        currentUser = new User(nombre, apellidos, email, "", "", isSuperAdmin);

        DashboardFragment dashboardFragment = (DashboardFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (dashboardFragment == null) {
            dashboardFragment = DashboardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    dashboardFragment, R.id.contentFrame);
        }

        presenter = new DashboardPresenter(dashboardFragment, Injector.provideAuthClient(),
                Injector.provideSharedPrefs());

        progressDialog = new AlertDialog.Builder(this)
                .setTitle("Cargando datos...")
                .setCancelable(false)
                .setView(new ProgressBar(this))
                .create();
        progressDialog.show();
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
                        Toast.makeText(DashboardActivity.this, s.nombre, Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) drawer.closeDrawer();
        else super.onBackPressed();
    }
}
