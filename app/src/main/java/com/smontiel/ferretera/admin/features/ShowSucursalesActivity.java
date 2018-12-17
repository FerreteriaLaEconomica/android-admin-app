package com.smontiel.ferretera.admin.features;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smontiel.ferretera.admin.R;

public class ShowSucursalesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sucursales);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng tantoyuca = new LatLng(21.35064, -98.2257);
        mMap.addMarker(new MarkerOptions().position(tantoyuca).title("Tantoyuca"));
        LatLng sanSebas = new LatLng(21.214410, -98.129040);
        mMap.addMarker(new MarkerOptions().position(sanSebas).title("Sucursal San Sebastián"));

        LatLng cancun = new LatLng(21.356105, -98.227937);
        mMap.addMarker(new MarkerOptions().position(cancun).title("Sucursal Cancún"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tantoyuca));
    }
}
