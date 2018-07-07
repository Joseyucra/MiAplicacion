package com.example.ariel.loapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ariel.loapp.ListDataSource.OnLoadImage;
import com.example.ariel.loapp.ListDataSource.TaskImg;
import com.example.ariel.loapp.utils.ParamsConnection;
import com.example.ariel.loapp.utils.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FormularioActivity extends AppCompatActivity implements OnLoadImage {

   // Button continue_user;

    private String avatar1, email1, name1;
    private Context root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);


        avatar1 = this.getIntent().getExtras().getString("icon");
        email1 = this.getIntent().getExtras().getString("correo");
        name1 = this.getIntent().getExtras().getString("nombre") ;

        loadComponets();


       /* continue_user= (Button) findViewById(R.id.boton_siguiente_user);
        continue_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opci = new Intent(getApplicationContext(), Pricipal.class);
                startActivity(opci);
            }
        });*/


    }

    private void loadComponets() {


        final EditText nametxt = (EditText) this.findViewById(R.id.nombre);
        final EditText emailtxt = (EditText) this.findViewById(R.id.correo);
        final ImageView img = (ImageView) this.findViewById(R.id.icon);
        final Button btn = (Button)this.findViewById(R.id.boton_siguiente_user);
        final EditText telefono = (EditText) this.findViewById(R.id.telefono);
        final EditText ciudadd = (EditText) this.findViewById(R.id.ciudad);
        final EditText direccion = (EditText) this.findViewById(R.id.direccion);
        final EditText password = (EditText) this.findViewById(R.id.password);


        nametxt.setText(name1);
        emailtxt.setText(email1);
        TaskImg loadimg = new TaskImg();
        loadimg.execute(avatar1);
        loadimg.setLoadImage(img, this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent main = new Intent(root, Pricipal.class);
                root.startActivity(main);
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("nombre", nametxt.getText());
                params.put("correo", emailtxt.getText());
                params.put("numTelefono", telefono.getText());
                params.put("ciudad", ciudadd.getText());
                params.put("direccion", direccion.getText());
                params.put("contraseña", password.getText());


                client.post(ParamsConnection.REST_USER_POST, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String msn = response.getString("msn");
                            String id = response.getString("id");
                            UserData.ID = id;
                            if (msn != null) {
                                Intent prin = new Intent(root, Pricipal.class);
                                root.startActivity(prin);
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
        });

    }

    @Override
    public void setLoadImage(ImageView container, Bitmap img) {

    }
}
