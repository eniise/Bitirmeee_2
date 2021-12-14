package controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Context context;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        context = this;
    }

    @Override
    public void onMapReady(@NonNull @org.jetbrains.annotations.NotNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng antep = new LatLng(37.0587509, 37.3100968);
        mMap.addMarker(new MarkerOptions()
                .position(antep)
                .title("Gülseren"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(antep));
        mMap.setOnMapClickListener(latLng -> {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                try {
                    String adress = addresses.get(0).getAddressLine(0);
                    String find_ilce = Pattern.compile("[\\,\"]")
                            .split(adress)[2]
                            .split(" ")[2]
                            .split("/")[0];
                    new AlertDialog.Builder(context)
                            .setTitle("İlçe bulundu")
                            .setMessage(find_ilce+" isimli ilçeye tıkladınız, bu ilçedeki öğretmenler anasayfada listelensin mi?")
                            .setPositiveButton(R.string.yonlendir, (dialog, which) -> {
                                Toast.makeText(context,"API geliştireyim seni yönlendiririm.",Toast.LENGTH_LONG).show();
                            })
                            .setNegativeButton(R.string.hayir, ((dialog, which) -> {
                                Toast.makeText(context,"Madem öyle defol başka bir ilçe bul.",Toast.LENGTH_LONG).show();
                            }))
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
                catch (IndexOutOfBoundsException e){
                    Toast.makeText(context,"Tıkladığın yerden ilçe bilgisi alamadım. Başka bir yere tıkla.",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
