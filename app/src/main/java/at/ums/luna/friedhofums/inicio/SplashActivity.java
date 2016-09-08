package at.ums.luna.friedhofums.inicio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import at.ums.luna.friedhofums.R;
import at.ums.luna.friedhofums.actividades.Preferencias;

public class SplashActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1;
    private static final int CONTACT_REQUEST_CODE = 2;
    private static final int WRITE_REQUEST_CODE = 3;

    private boolean permisoLocation = false;
    private boolean permisoContact = false;
    private boolean permisoWrite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Runnable ejecutable = new Runnable() {
            @Override
            public void run() {
                comprobarPermisos();
            }
        };
        Handler miHandler = new Handler();
        miHandler.postDelayed(ejecutable, 1500);


    }

    private void comprobarPermisos(){

        // Controles UI
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.GET_ACCOUNTS},
                        CONTACT_REQUEST_CODE);
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_REQUEST_CODE);
                } else {
                    ejecutarApp();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos();
                } else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_REQUEST_CODE);
                }
                return;
            }

            case CONTACT_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos();
                } else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.GET_ACCOUNTS},
                            CONTACT_REQUEST_CODE);
                }
                return;
            }

            case WRITE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comprobarPermisos();
                } else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_REQUEST_CODE);
                }
                return;
            }

        }
    }

    private void ejecutarApp() {

        //inicia la actividad principal tras comprobar que la password es correcta
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText etPassword = new EditText(SplashActivity.this);

        etPassword.setHint(this.getString(R.string.password));
        etPassword.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setTitle(this.getString(R.string.password))
                .setCancelable(true)
                .setView(etPassword)
                .setPositiveButton(this.getString(R.string.continuar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String introPass = etPassword.getText().toString();

                                switch (introPass) {
                                    case "0127":
                                        //abre la actividad principal
                                        actividadPrincipal();
                                        finish();
                                        break;
                                    case "2004":
                                        // cambiar preferencias
                                        actividadPrincipal();
                                        abrePreferencias();
                                        finish();
                                        break;
                                    default:
                                        Toast.makeText(SplashActivity.this, R.string.pass_false, Toast.LENGTH_SHORT).show();
                                        ejecutarApp();
                                        break;
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alert.show();



    }

    public void actividadPrincipal(){
        Intent intento = new Intent(this, MainActivity.class);
        startActivity(intento);
    }

    public void abrePreferencias(){
        Intent intento = new Intent(this, Preferencias.class);
        startActivity(intento);
    }

}
