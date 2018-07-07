package com.example.ariel.loapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

public class casasPuntos extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    static ArrayList<Marker> marcas=new ArrayList<>();
    HashMap<String,LatLng> casas=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casas_puntos);
        run();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
        // mapFragment.getMapAsync((OnMapReadyCallback) this);
        // mapFragment.getMapAsync((OnMapReadyCallback) this);
    }
    private void run() {
        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://192.168.43.207:7777/api/v1.0/home", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                JSONArray aux =timeline;
                try
                {
                    JSONArray jsonArray = timeline;
                    //https://es.androids.help/q28175
                    //ArrayList<String> datos=new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++){
                        // citas.add(llenar(jsonArray.get(i)));
                        //jsonArray.get(i);
                        JSONObject aux2= (JSONObject) jsonArray.get(i);

                        //Toast.makeText(getApplicationContext(),aux2.get("precio")+"",Toast.LENGTH_LONG).show();
                        casas.put((String) aux2.get("_id"),new LatLng((double)aux2.get("lat"),(double) aux2.getDouble("lon")));

                    }

                    /*for(int i=0;i<citas.size();i++){
                        ArrayList<String> x=citas.get(i);
                        Double lat=Double.parseDouble(x.get(9));
                        Double lon=Double.parseDouble(x.get(13));
                        if(x.get(3).equals("venta")){
                            venta.put(x.get(4),new LatLng(lat,lon));
                        }else{
                            alquiler.put(x.get(4),new LatLng(lat,lon));
                        }
                        if(x.get(8).equals("casa")){
                            casa.put(x.get(4),new LatLng(lat,lon));
                        }
                        else{
                            departamento.put(x.get(4),new LatLng(lat,lon));
                        }
                        listar.put(x.get(4),new LatLng(lat,lon));
                    }*/

                }
                catch (Exception e)
                {

                };
            }


        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.casas_puntos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync( this);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*private void eliminar() {
        if(!resul.isEmpty()){
            for(int i=0;i<marcas.size();i++){
                Marker aux=marcas.get(i);
                aux.remove();
            }
        }
    }*/

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng potosi=new LatLng(-19.588530,-65.754172);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(potosi,14));
        googleMap.setOnMarkerClickListener(this);
        // Add a marker in Sydney and move the camera
        //Toast.makeText(getApplicationContext(),casas.size()+"",Toast.LENGTH_SHORT).show();
        Iterator it=casas.keySet().iterator();
        while (it.hasNext()){
            String id= (String) it.next();
            Marker m=mMap.addMarker(new MarkerOptions().position(casas.get(id)).title(id));
            marcas.add(m);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent in=null;
        for(int i=0;i<marcas.size();i++) {
            if (marker.equals(marcas.get(i))) {
                //Toast.makeText(getApplicationContext(),"entra",Toast.LENGTH_SHORT).show();
                in = new Intent(this, detallesCasa.class);
                in.putExtra("latitud", marker.getPosition().latitude+"");
                in.putExtra("longitud", marker.getPosition().longitude+"");
                in.putExtra("id", marker.getTitle()+"");
                //in.putExtra("ema", email);
                startActivity(in);
            }
        }
        return true;
    }
}
