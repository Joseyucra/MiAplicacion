package com.example.ariel.loapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ariel.loapp.utils.ParamsConnection;
import com.example.ariel.loapp.utils.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Camera extends AppCompatActivity implements View.OnClickListener{

    ImageView IMG_CONTAINER;
    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    private int MEDIA_CODE = 123;
    private int CAMERA_CODE = 124;
    private String ABSOULTE_PATH;
    private Context root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        validaPermisos();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        LoadComponents();
    }

    private void LoadComponents() {
        Button media = (Button)this.findViewById(R.id.media);
        Button photo = (Button)this.findViewById(R.id.photo);
        IMG_CONTAINER = (ImageView)this.findViewById(R.id.photoView);
        media.setOnClickListener(this);
        photo.setOnClickListener(this);

        Button send = (Button) this.findViewById(R.id.sendbutton);
        send.setOnClickListener(this);
    }

    private boolean validaPermisos() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);


        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.media) {
            LoadMediaData();
        }
        if(v.getId() == R.id.photo) {
            openCameraIntent();
        }
        if (v.getId() == R.id.sendbutton) {
            try {
                sendPhoto();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendPhoto() throws FileNotFoundException{
        if ( IMG_CONTAINER.getDrawable() != null) {
            if (imageFilePath != null) {
                File file = new File(imageFilePath);
                RequestParams params = new RequestParams();
                params.put("img", file);
                AsyncHttpClient client = new AsyncHttpClient();
                if (UserData.ID != null) {
                    client.post(ParamsConnection.REST_USERIMG_POST + "/" + UserData.ID, params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {

                                String path = response.getString("path");
                                if (path != null) {
                                      Intent profile = new Intent(root, LatLonMaps.class);
                                    root.startActivity(profile);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(root, "No se ha sacado una foto", Toast.LENGTH_LONG).show();

                                // Log.i("ERROR", e.getMessage());
                            }
                        }


                    });

                }
                else{
                    Toast.makeText(this, "No se ha sacado una foto", Toast.LENGTH_LONG).show();

                }
            }
        } else {
            Toast.makeText(this, "No se ha sacado una foto", Toast.LENGTH_LONG).show();
        }
    }

    private void openCameraIntent() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri fileuri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(camera, CAMERA_CODE);
    }
    private  static final int REQUEST_CAPTURE_IMAGE = 100;
    private String imageFilePath;

    private File createFile() {
        File file = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = "";
        if (file.exists()) {
            name = "IMG_" +System.currentTimeMillis()/100+ ".jpg";
        }
        imageFilePath = file.getAbsolutePath() +File.separator + name;
        File fileimg = new File(imageFilePath);
        return fileimg;
    }

    private void LoadMediaData() {
        Intent media = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        media.setType("image/");
        startActivityForResult(media.createChooser(media, "Escoja la Aplicacion"), MEDIA_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MEDIA_CODE) {
            IMG_CONTAINER.setImageURI(data.getData());
        }
        if(requestCode == CAMERA_CODE) {

            loadImageCamera();
        }
    }

    private void loadImageCamera() {
        //File file = new File(imageFilePath);
        Bitmap img = BitmapFactory.decodeFile(imageFilePath);
        if(img != null) {
            IMG_CONTAINER.setImageBitmap(img);

        }
    }
    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHms",
                Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        imageFilePath = image.getAbsolutePath();
        return image;
    }

}
