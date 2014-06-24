package wil.mirk.compmovil.parandroid.app;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class newAlert extends ActionBarActivity {

    String _receptor;
    String _nroreceptor;
    EditText _cuerpo;
    Button _botonSiguiente;
    CheckBox _smsCheck, _mailCheck, _gpsCheck;
    Long _tiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alert);


        _receptor = "darkwilsor@gmail.com";
        _nroreceptor = "01164209786";

        _mailCheck = (CheckBox) findViewById(R.id.mail_check);
        _smsCheck = (CheckBox) findViewById(R.id.sms_check);
        _gpsCheck = (CheckBox) findViewById(R.id.ubicacion);

        _cuerpo = (EditText) findViewById(R.id.cuerpo);
        _botonSiguiente = (Button) findViewById(R.id.boton_siguiente);

        _tiempo = (long) 6000;





        _botonSiguiente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                hacerSonarAlarmaEn();

            }
        });










    }

    protected void agregarUbicacion(){

/*        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criterio = new Criteria();
        String provider = mLocationManager.getBestProvider(criterio, false);
        Location location = mLocationManager.getLastKnownLocation(provider);

        String _ubicacion = location.toString();

        _cuerpo.setText(_cuerpo.getText() + "Mi ultima ubicacion es :" +  _ubicacion);*/



    }

    protected void enviarSMS() {
        Log.i("Send SMS", "");

        String phoneNo = _nroreceptor;
        String message = _cuerpo.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    protected void enviarMail() {

        String[] recipients = {_receptor};
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        // prompts email clients only
        email.setType("message/rfc822");

        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, "alerta");
        email.putExtra(Intent.EXTRA_TEXT, _cuerpo.getText().toString());



        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Seleccionar Cliente de mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(newAlert.this, "No hay cliente de mail instalado",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class ejecutarAlarma extends AsyncTask  {

        @Override
        protected Object doInBackground(Object[] objects) {


                    if(_gpsCheck.isChecked()){
                        agregarUbicacion();
                    }

                    if (_mailCheck.isChecked()) {
                        enviarMail();
                    }

                    if (_smsCheck.isChecked()){
                        enviarSMS();
                    }

            return null;
        }





    }


    public void hacerSonarAlarmaEn() {
        final Handler _handler = new Handler();
        Timer _timer = new Timer();
        TimerTask _ejecutarAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                _handler.post(new Runnable() {
                    public void run() {
                        try {
                            ejecutarAlarma alarmaAEjecutar = new ejecutarAlarma();
                            // ejecuta la asyntask creada
                            alarmaAEjecutar.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        _timer.schedule(_ejecutarAsynchronousTask, _tiempo); //tiempo en ms
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
