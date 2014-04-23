package be.hcpl.android.sensors.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * for a background service not linked to an activity it's important to use the command approach
 * instead of the Binder. For starting use the alarmmanager
 */
public class SensorBackgroundService extends Service implements SensorEventListener {

    private static final String TAG = SensorBackgroundService.class.getSimpleName();

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // we need the light sensor
        // TODO make this all configurable
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // ignore this since not linked to an activity
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // for recording of data use an AsyncTask, we just need to compare some values so no
        // background stuff needed for this

        // grab the values
        StringBuilder sb = new StringBuilder();
        for (float value : event.values )
            sb.append(String.valueOf(value)).append(" | ");

        // Log that information for so we can track it in the console (for production code remove
        // this since this will take a lot of resources!!)
        Log.d(TAG, "received sensor valures are: " + sb.toString());

        // if first value is below 10 (TODO make treshold configurable)
        // we need to enable the screen
        if( event.values[0] < 10 ){

            // TODO fix deprecated code here

            // wake screen here
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(getApplicationContext().POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();

            // and release screen lock right away also
            KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(getApplicationContext().KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();
        }

        // stop the sensor and service
        sensorManager.unregisterListener(this);
        stopSelf();
    }




}
