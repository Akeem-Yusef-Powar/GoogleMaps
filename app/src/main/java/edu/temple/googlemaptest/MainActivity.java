package edu.temple.googlemaptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter,GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {
    MapView mapView;
    Marker marker;
    GoogleMap gMap;
    CameraUpdate cam;
    Button button;
    TextView markerLocation,llat,llong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        markerLocation = findViewById( R.id.markerDetails);
        markerLocation.setText(getString(R.string.marker_details));
        llat = findViewById(R.id.llat);
        llong = findViewById(R.id.llong);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng location = new LatLng(34.343434,45.2323232); // arbitrary coordinates, will change to current location when location service is implemented

                cam = CameraUpdateFactory.newLatLngZoom(location,10);
                if(gMap!= null)
                    gMap.animateCamera(cam);
                if(marker == null){
                    marker = gMap.addMarker(new MarkerOptions().position(location)
                            .title("New Marker"));
                } else{
                    marker.setPosition(location);
                }

                getMarkerDetials(marker);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
       mapView.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMarkerDragListener(this);
        gMap.setOnMapLongClickListener(this);

    }
//---------------------------------------------------------
    @Override
    public View getInfoWindow(Marker marker) {
        return  null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
//-----------------------------------------------------------
    @Override
    public void onMarkerDragStart(Marker marker) {
        getMarkerDetials(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        getMarkerDetials(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        getMarkerDetials(marker);
    }
//---------------------------------------------------------
    @Override
    public void onMapLongClick(LatLng latLng) {
        cam = CameraUpdateFactory.newLatLngZoom(latLng,10);
        gMap.animateCamera(cam);

        if(marker == null){
           marker = gMap.addMarker(new MarkerOptions().position(latLng)
                    .title("test"));
        } else{
            marker.setPosition(latLng);
        }
        getMarkerDetials(marker);

    }

    private void getMarkerDetials(Marker marker){

        llat.setText(String.valueOf(marker.getPosition().latitude));
        llong.setText(String.valueOf(marker.getPosition().longitude));

        Geocoder geoCoder = new Geocoder(this);

        try {
            List<Address> addresses = geoCoder.getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude,1);
            markerLocation.setText(addresses.get(0).getAddressLine(0));
            Log.d("marker address:",addresses.get(0).getAddressLine(0));

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}