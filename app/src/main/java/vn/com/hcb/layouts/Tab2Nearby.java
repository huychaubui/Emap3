package vn.com.hcb.layouts;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.com.hcb.adapter.CustomInforAdapter;
import vn.com.hcb.datn.Detail_service;
import vn.com.hcb.datn.R;
import vn.com.hcb.model.Service;

import static vn.com.hcb.datn.R.id.map;

/**
 * Created by HUYCHAU on 3/9/2017.
 */
public class Tab2Nearby extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2nearby, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                mMap.setMyLocationEnabled(true);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                LocationManager locationManager =(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(criteria, true);
                Location loc = locationManager.getLastKnownLocation(provider);
                mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 17.0f));
                LatLng latLng = mMap.getCameraPosition().target;
                query(latLng, 0.3);
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

//                mMap.clear();

                query(cameraPosition.target, 0.3);
            }
        });
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

    }


    Geocoder geocoder;
    List<Address> addresses;
    private Map<String,Marker> markers = new HashMap<String,Marker>();
    public void getAddress() {
        try {
            geocoder = new Geocoder(this.getContext(), Locale.ENGLISH);
            addresses = geocoder.getFromLocation(21.997458, 105.829224, 1);
            StringBuilder str = new StringBuilder();
            if (geocoder.isPresent()) {
                Toast.makeText(this.getContext(),
                        "geocoder present", Toast.LENGTH_SHORT).show();
                Address returnAddress = addresses.get(0);
                for(int i = 0; i<returnAddress.getMaxAddressLineIndex(); i++){
                    Toast.makeText(this.getContext(), returnAddress.getAddressLine(i).toString(), Toast.LENGTH_SHORT).show();
                }

                String l = returnAddress.getSubLocality();
                String localityString = returnAddress.getLocality();
                String strees = returnAddress.getThoroughfare();
                String dis = returnAddress.getSubThoroughfare();
                String s = returnAddress.getAdminArea();

                String city = returnAddress.getCountryName();
                String region_code = returnAddress.getCountryCode();
                String zipcode = returnAddress.getPostalCode();

                str.append(l + "-" + strees + "- "+ dis+ "- " +s);
            } else {
                Toast.makeText(this.getContext(),
                        "geocoder not present", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
    // TODO Auto-generated catch block
            Log.e("tag", e.getMessage());
        }
    }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("loactions/Quán ăn");
        GeoFire geoFire = new GeoFire(ref);
        ArrayList<LatLng> nearby = new ArrayList<LatLng>();
        public void query(LatLng center, double radius){
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(center.latitude, center.longitude), radius);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, GeoLocation location) {
                    nearby.add(new LatLng(location.latitude, location.longitude));

                    final LatLng loc_marker = new LatLng(location.latitude, location.longitude);
                    final DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference("Service/Quán ăn");
                    serviceRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!= null && loc_marker!= null){
                                Marker marker = mMap.addMarker(new MarkerOptions().position(loc_marker));
                                Service post = dataSnapshot.getValue(Service.class);
                                marker.setTag(post);
                                marker.setTitle(key);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onKeyExited(String key) {
//                    Marker marker = markers.get(key);
//                    if (marker != null) {
//                        marker.remove();
//                        markers.remove(key);
//                    }
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    // Move the marker
//                    Marker marker = markers.get(key);
//                    if (marker != null) {
////                        this.animateMarkerTo(marker, location.latitude, location.longitude);
//                        mMap.addMarker(marker);
//                    }

                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Error")
                            .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });
        }
    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Service service = (Service) marker.getTag();
        mMap.setInfoWindowAdapter(new CustomInforAdapter(getActivity(), service));
        marker.showInfoWindow();

        return false;
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        Service service = (Service) marker.getTag();
        Intent intent = new Intent(rootView.getContext(), Detail_service.class);
        intent.putExtra("Service", service);
        String key = marker.getTitle();
        intent.putExtra("key", key);
        startActivity(intent);

    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        Circle searchCircle;
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        //chuyen doi khi zoom
//        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
//        // radius in km
//        this.geoQuery.setRadius(radius/1000);
    }
}

