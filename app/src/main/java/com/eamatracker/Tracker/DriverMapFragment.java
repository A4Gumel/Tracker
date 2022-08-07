package com.eamatracker.Tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.downloader.Progress;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Account.LaunchActivity;
import com.eamatracker.Models.DriverDetails;
import com.eamatracker.Models.UserDetails;
import com.eamatracker.Models.WarehouseList;
import com.eamatracker.R;
import com.eamatracker.Seller.SellerMainActivity;
import com.eamatracker.Utils.InternetStatus;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;
import com.smarteist.autoimageslider.IndicatorView.animation.data.Value;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "DriverMapFragment";

    private GoogleMap mMap;
    private String loginKey, lang = "en";
    private String username;

    //Location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    SupportMapFragment mapFragment;
    Marker mCurrLocation;
    private FloatingActionButton myLocationFab, driverLocationOffFab;
    private InternetStatus mInternetStatus;
    private String locality;
    private Integer driverId;
    private Dialog alertDialog;
    private boolean isOnline = true;


    //load warehouses
    private double instance = 300.0;
    private static final double LIMIT_RANGE = 500.0;
    private Location previousLocation, currentLocation;


    //Online system
    DatabaseReference onlineRef, currentUserRef, driversLocationRef;
    GeoFire geoFire;
    ValueEventListener onlineEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.exists()) {
//                currentUserRef.onDisconnect().removeValue();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();
        }
        retrieveDriverId();
//        registerOnlineSystem();
        super.onResume();
    }

    private void registerOnlineSystem() {

        onlineRef.addValueEventListener(onlineEventListener);

    }

    @Override
    public void onStart() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();
        }
        retrieveDriverId();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View driverMapView = inflater.inflate(R.layout.fragment_driver_map, container, false);

        retrieveUserToken();
        retrieveDriverId();

        init();

        alertDialog = new Dialog(requireContext());

        mInternetStatus = new InternetStatus(requireContext());

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myLocationFab = driverMapView.findViewById(R.id.myLocationFab);
        driverLocationOffFab  = driverMapView.findViewById(R.id.driverLocationOff);

        if (loginKey != null && !loginKey.isEmpty() && driverId != null && !(driverId == 0)) {

            myLocationFab.setOnClickListener(view -> {

                if (mMap != null) {

                    mMap.setMyLocationEnabled(true);
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(location -> {

                                LatLng userLatLng = new LatLng(location.getLatitude(),
                                        location.getLongitude());
                                Log.d(TAG, userLatLng.toString());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f));
                                Log.d(TAG, "Location updated");

                            })
                            .addOnFailureListener(e -> {

                                Log.e(TAG, "Failed to load user location " + e);
                                showErrorSnackBar(getString(R.string.error_occured));

                            });

                }
            });

        } else {

            sendUserToMainActivity();

        }

        driverLocationOffFab.setOnClickListener(view ->

                {
                    if (isOnline) {

                        showRemoveLocationDialog();
                        driverLocationOffFab.setImageResource(R.drawable.ic_location_24);

                    } else {

                        init();
                        driverLocationOffFab.setImageResource(R.drawable.ic_location_off_24);
                    }
                }
        );

        return driverMapView;
    }

    private void showRemoveLocationDialog() {

        alertDialog.setContentView(R.layout.logout_dialog);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        MaterialButton cancelLogout = alertDialog.findViewById(R.id.cancelLogOut);
        MaterialButton yesLogout = alertDialog.findViewById(R.id.yesLogout);
        TextView title  = alertDialog.findViewById(R.id.logOutTitle);
        title.setText(getString(R.string.remove_loc_confirm));

        alertDialog.setCanceledOnTouchOutside(true);

        cancelLogout.setOnClickListener(v -> alertDialog.dismiss());

        yesLogout.setOnClickListener(v -> {

            alertDialog.dismiss();
            removeDriverLocationUpdate();

        });

        alertDialog.show();

    }

    private void retrieveDriverId() {

        SharedPreferences loginPrefs = this.requireActivity().getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        driverId = loginPrefs.getInt("DRIVER_ID", 0);
        Log.d(TAG, "driverId = " + driverId);

    }

    private void init() {

        checkPermissions();

        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");

        registerOnlineSystem();

        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (loginKey != null && !loginKey.isEmpty() && driverId != null && !(driverId == 0)) {

                    isOnline = true;

                    LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));

                    locationResult.getLastLocation();

                    if (geoFire != null && driverId != null && locationResult != null) {

                        geoFire.setLocation(String.valueOf(driverId), new GeoLocation(locationResult.getLastLocation().getLatitude(),
                                locationResult.getLastLocation().getLongitude()), (key, error) -> {

                            if (error != null) {

                                Log.e(TAG, "Write Failed:\t" + Objects.requireNonNull(error).getMessage());
                                showErrorSnackBar(getString(R.string.failed_update_location));

                            } else {

                                Log.d(TAG, "Write complete: ");
                                showSuccessSnackBar(getString(R.string.you_ar_online));

                            }
                        });


                    } else {

                        init();
                        Log.e(TAG, "geoFire == null || driverId == null || locationResult == null");
                    }


                } else {

                    sendUserToMainActivity();
                }
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();

        } else {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            fusedLocationProviderClient.getLastLocation()
                    .addOnFailureListener(e -> {

                        Log.d(TAG, e.getMessage());
                        showErrorSnackBar(getString(R.string.error_occured));

                    })
                    .addOnSuccessListener(location -> {

                        //After getting location, get city;
                        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

                        List<Address> addressList;

                        try {

                            addressList = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);

                            String locality = addressList.get(0).getAdminArea();
                            Log.d(TAG, "State : " + locality);

                            driversLocationRef = FirebaseDatabase.getInstance()
                                    .getReference("DriversLocation").child(locality);

                            geoFire = new GeoFire(driversLocationRef);

                            fetchWareHouses(locality);
                            updateUserLocation();


                        } catch (IOException e) {

                            showErrorSnackBar(getString(R.string.cannot_get_locality));
                            e.printStackTrace();

                        }


                    });

        }

    }

    private void updateUserLocation() {


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (loginKey != null && !loginKey.isEmpty() && driverId != null && !(driverId == 0)) {

                    LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));


                    geoFire.setLocation(String.valueOf(driverId), new GeoLocation(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude()), (key, error) -> {

                        if (error != null) {

                            Log.e(TAG, "Write Failed:\t" + Objects.requireNonNull(error).getMessage());
                            showErrorSnackBar(getString(R.string.failed_update_location));

                        } else {

                            Log.d(TAG, "Write complete: ");
                            showSuccessSnackBar(getString(R.string.you_ar_online));

                        }
                    });
//


                } else {

                    sendUserToMainActivity();
                }
            }
        };


    }

    private void fetchWareHouses(String locality) {

        Call<WarehouseList> warehouseListCall = RetrofitClient
                .getInstance()
                .getApiService()
                .warehouseList("Token " + loginKey);

        warehouseListCall.enqueue(new Callback<WarehouseList>() {
            @Override
            public void onResponse(Call<WarehouseList> call, Response<WarehouseList> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    WarehouseList warehouseList = response.body();

                    if (Objects.requireNonNull(warehouseList).getCount() == 0) {

                        showErrorSnackBar(getString(R.string.no_warehouse_in_locality));

                    } else if (warehouseList.getCount() >= 1) {


                    }
                }

            }

            @Override
            public void onFailure(Call<WarehouseList> call, Throwable t) {

            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Log.d(TAG, "Map ready");

        mMap = googleMap;

        checkPermissions();

        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            checkPermissions();

                        } else {

                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            myLocationFab.setOnClickListener((view) -> {

                                Log.d(TAG, "Button clicked");

//                                fusedLocationProviderClient.getLastLocation()
//                                        .addOnSuccessListener(location -> {
//
//                                            LatLng userLatLng = new LatLng(location.getLatitude(),
//                                                    location.getLongitude());
//                                            Log.d(TAG, userLatLng.toString());
//                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f));
//                                        })
//                                        .addOnFailureListener(e -> showErrorSnackBar(getString(R.string.error_occured)));
                            });


                        }


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        showErrorSnackBar(getString(R.string.permission_is_required));

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

//        LatLng sydney = new LatLng(11.9380806, 8.6172592);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Market at Mariri."));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    private void showErrorSnackBar(String message) {

        Snackbar errorSnackBarView = Snackbar
                .make(requireView(), message,
                        Snackbar.LENGTH_LONG);
        errorSnackBarView
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.textColor));
        errorSnackBarView
                .setBackgroundTint(requireContext().getColor(R.color.design_default_color_error));
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.colorAccent));

        errorSnackBarView.show();

    }

    private void checkPermissions() {

        Dexter.withContext(requireContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (!report.areAllPermissionsGranted()) {

                    MultiplePermissionsListener snackbarMultiplePermissionsListener =
                            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                                    .with(requireView(), "Location is required")
                                    .withOpenSettingsButton("Settings")
                                    .withCallback(new Snackbar.Callback() {
                                        @Override
                                        public void onShown(Snackbar snackbar) {
                                            // Event handler for when the given Snackbar is visible
                                        }

                                        @Override
                                        public void onDismissed(Snackbar snackbar, int event) {
                                            // Event handler for when the given Snackbar has been dismissed
                                            requireActivity().supportFinishAfterTransition();
                                        }
                                    })
                                    .build();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                           PermissionToken token) {

            }
        }).check();


    }

    private void retrieveUserToken() {

        SharedPreferences loginPrefs = this.requireActivity().getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        loginKey = loginPrefs.getString("USER_TOKEN", null);
        Log.d(TAG, "Login key = " + loginKey);

    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(requireContext(), LaunchActivity.class);

        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        requireActivity().supportFinishAfterTransition();
    }


    private void showSuccessSnackBar(String message) {

        Snackbar errorSnackBarView = Snackbar
                .make(requireView(), message,
                        Snackbar.LENGTH_LONG);
        errorSnackBarView
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.textColor));
        errorSnackBarView
                .setBackgroundTint(requireContext().getColor(R.color.colorSecondary));
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.colorAccent));

        errorSnackBarView.show();

    }


    private void removeDriverLocationUpdate() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        geoFire.removeLocation(String.valueOf(driverId));
        onlineRef.removeEventListener(onlineEventListener);
        isOnline = false;
        showErrorSnackBar(getString(R.string.driver_offline_success));

    }
}