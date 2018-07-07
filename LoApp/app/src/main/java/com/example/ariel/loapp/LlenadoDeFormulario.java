package com.example.ariel.loapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.ariel.LoApp.utils.ParamsConnection;
//import com.example.ariel.formulario.utils.UserData;
import com.example.ariel.loapp.utils.ParamsConnection;
import com.example.ariel.loapp.utils.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LlenadoDeFormulario extends AppCompatActivity implements View.OnClickListener {

private Context root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root =this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenado_de_formulario);
        LoadComponents();
    }

    private void LoadComponents() {
        Button btn = (Button)this.findViewById(R.id.sendbtn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextView tipo = (TextView) this.findViewById(R.id.id_tipo);
        TextView estado = (TextView) this.findViewById(R.id.id_estado);
        TextView precio = (TextView) this.findViewById(R.id.id_precio);
        TextView ciudad = (TextView) this.findViewById(R.id.id_cuidad);
        TextView descripcion = (TextView) this.findViewById(R.id.id_descripionde);
        TextView cuartos = (TextView) this.findViewById(R.id.habitacion);
        TextView bano = (TextView) this.findViewById(R.id.banos);
        TextView superfi = (TextView) this.findViewById(R.id.superficie);
        TextView lat = (TextView) this.findViewById(R.id.lat);
        TextView lon = (TextView) this.findViewById(R.id.lon);



        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("tipo", tipo.getText());
        params.put("estado", estado.getText());
        params.put("precio", precio.getText());
        params.put("ciudad", ciudad.getText());
        params.put("descripcion", descripcion.getText());
        params.put("cantCuartos", cuartos.getText());
        params.put("cantBaños", bano.getText());
        params.put("superficie", superfi.getText());
        params.put("lat", lat.getText());
        params.put("lon", lon.getText());

        client.post(ParamsConnection.REST_HOME_POST, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msn = response.getString("msn");
                    String id = response.getString("id");
                    UserData.ID = id;
                    if (msn != null) {
                        Intent camera = new Intent(root, Camera.class);
                        root.startActivity(camera);
                    } else {
                        Toast.makeText(root, "ERROR AL enviar los datos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //AsyncHttpClient.log.w(LOG_TAG, "onSuccess(int, Header[], JSONObject) was not overriden, but callback was received");
            }
        });



    }
}