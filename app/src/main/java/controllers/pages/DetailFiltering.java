package controllers.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.enise.bitirme_2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import models.search.SearchDto;
import models.trainer.TrainerCourse;
import utils.AsyncResponse;
import utils.components.MyAlertDialog;
import utils.extras.TransactionTypes;
import utils.extras.URLs;
import utils.server.ServerPOST;

public class DetailFiltering extends AppCompatActivity implements AsyncResponse,OnMapReadyCallback {
    private RoundedImageView btnSearchBack;
    private GoogleMap mMap;
    private Switch swStudentSearch;
    private Switch swOtherPlaceSearch;
    private Switch swOnlineSearch;
    private Switch swHomeSearch;
    private EditText txtSearchMax;
    private EditText txtSearchMin;
    private RoundedImageView btnCostSearch;
    private RoundedImageView btnCourseNameSearch;
    private RoundedImageView btnPlaceSearch;
    private EditText txtSearchName;
    Context context;
    private String districtName = "No";
    private TextView txSearchtDistrictName;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_filtering);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        context = this;
        init();
    }
    void init(){
        txSearchtDistrictName = findViewById(R.id.txSearchtDistrictName);
        txSearchtDistrictName.setOnClickListener(v -> {
            if(districtName.length()>0) {
                districtName = "";
                txSearchtDistrictName.setText("");
            }
        });
        btnSearchBack = findViewById(R.id.btnSearchBack);
        btnPlaceSearch = findViewById(R.id.btnPlaceSearch);
        btnSearchBack.setOnClickListener(v -> {
            onBackPressed();
        });
        swStudentSearch = findViewById(R.id.swStudentSearch);
        swOtherPlaceSearch = findViewById(R.id.swOtherPlaceSearch);
        swOnlineSearch = findViewById(R.id.swOnlineSearch);
        swHomeSearch = findViewById(R.id.swHomeSearch);
        txtSearchMax = findViewById(R.id.txtSearchMax);
        txtSearchMin = findViewById(R.id.txtSearchMin);
        txtSearchName = findViewById(R.id.txtSearchName);
        btnCourseNameSearch = findViewById(R.id.btnCourseNameSearch);
        btnCourseNameSearch.setOnClickListener(v->{
            btnCourseNameSearch.setEnabled(false);
            ServerPOST nameSearch = new ServerPOST(TransactionTypes.doSearch);
            nameSearch.delegate = this;
            if (txtSearchName.getText().length()>0) {
                SearchDto searchDto = new SearchDto(0,0,txtSearchName.getText().toString(),"",districtName.length()>0 ? districtName : "");
                nameSearch.execute(URLs.SearchName(),new Gson().toJson(searchDto));
            } else {
                new MyAlertDialog(this, "Not null", "Please enter course name", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay, ((dialog, which) -> {

                        })).show();
            }
        });
        btnCostSearch = findViewById(R.id.btnCostSearch);
        btnCostSearch.setOnClickListener(v -> {
            btnCostSearch.setEnabled(false);
            ServerPOST searchCost = new ServerPOST(TransactionTypes.doSearch);
            searchCost.delegate = this;
            if (txtSearchMax.getText().length() > 0 && txtSearchMin.getText().length() > 0) {
                SearchDto searchDto = new SearchDto(Float.parseFloat(txtSearchMax.getText().toString()),Float.parseFloat(txtSearchMin.getText().toString()),"","",districtName.length()>2 ? districtName : "");
                searchCost.execute(URLs.SearchCost(),new Gson().toJson(searchDto));
            } else {
                new MyAlertDialog(this, "Not null", "Please enter cost max and min", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay, ((dialog, which) -> {

                        })).show();
            }
        });
        btnPlaceSearch.setOnClickListener(v->{
            btnPlaceSearch.setEnabled(false);
            ServerPOST searchPlace = new ServerPOST(TransactionTypes.doSearch);
            searchPlace.delegate = this;
            if (DetectHastag().length()>0) {
                SearchDto searchDto = new SearchDto(0,0,"",DetectHastag(),districtName.length()>0 ? districtName : "");
                searchPlace.execute(URLs.SearchPlace(),new Gson().toJson(searchDto));
            } else {
                new MyAlertDialog(this, "Not null", "Please select place", R.drawable.ic_message_info)
                        .ShowMessage()
                        .setNegativeButton(R.string.okay, ((dialog, which) -> {

                        })).show();
            }
        });
    }
    private String DetectHastag(){
        List<String> _tmp = new ArrayList<>();
        if(swOnlineSearch.isChecked())
            _tmp.add("Online");
        if(swHomeSearch.isChecked())
            _tmp.add("Trainer Home");
        if(swOtherPlaceSearch.isChecked())
            _tmp.add("Other place");
        if(swStudentSearch.isChecked())
            _tmp.add("Student home");
        String s = _tmp.toString();//%20
        String z = s.replace("[","").replace("]","");
        String x = z.replace(" ","%20");
        return x;
    }
    @Override
    public <T> void processFinish(T result) {
        if(result.getClass() == String.class){
            TrainerCourse[] courses = null;
            try {
                courses = new Gson().fromJson(String.valueOf(result), TrainerCourse[].class);
                if(courses.length > 0) {
                    startActivity(new Intent(this, SearchResultPage.class).putExtra("result", String.valueOf(result)));
                    @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"Found!", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this, findViewById(android.R.id.content), "Not Found!", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
            }catch (JsonSyntaxException ex){
                ex.printStackTrace();
                @SuppressLint("ResourceType") Snackbar snackbar = Snackbar.make(this,findViewById(android.R.id.content),"Not Found!", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.show();
            }
        }
        btnPlaceSearch.setEnabled(true);
        btnCostSearch.setEnabled(true);
        btnCourseNameSearch.setEnabled(true);
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng antep = new LatLng(37.0587509, 37.3100968);
        mMap.addMarker(new MarkerOptions()
                .position(antep)
                .title("Gaziantep"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(antep));
        mMap.setOnMapClickListener(latLng -> {
            try {
                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                GetDistrict(gcd,latLng.latitude,latLng.longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void GetDistrict(Geocoder gcd,double lat, double lon){
        try {
            List<Address> addresses = gcd.getFromLocation(lat, lon, 1);
            String find_ilce = Pattern.compile("[\\,\"]")
                    .split(addresses.get(0).getAddressLine(0))[2]
                    .split(" ")[2]
                    .split("/")[0];
            new AlertDialog.Builder(context)
                    .setTitle("District found")
                    .setMessage(find_ilce+" -> Save selected district information to search details??")
                    .setPositiveButton(R.string.yonlendir, (dialog, which) -> {
                        //ServerGET serverGET = new ServerGET(TransactionTypes.doSearch);
                        //serverGET.delegate = this;
                        //serverGET.execute(URLs.SearchDistrict(find_ilce));
                        this.districtName = find_ilce;
                        txSearchtDistrictName.setText(find_ilce);
                    })
                    .setNegativeButton(R.string.hayir, ((dialog, which) -> {

                    }))
                    .setIcon(R.drawable.ic_baseline_info_24)
                    .show();
        }
        catch (IndexOutOfBoundsException | IOException e){
            Toast.makeText(context,"I can't get district information from where you click.",Toast.LENGTH_LONG).show();
        }
    }
}
