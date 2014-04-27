package be.hcpl.android.sensors.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * for a background service not linked to an activity it's important to use the command approach
 * instead of the Binder. For starting use the alarm manager
 */
public class SensorBackgroundService extends Service implements SensorEventListener {

    /**
     * a tag for logging
     */
    private static final String TAG = SensorBackgroundService.class.getSimpleName();

    /**
     * again we need the sensor manager and sensor reference
     */
    private SensorManager mSensorManager = null;

    /**
     * an optional flag for logging
     */
    private boolean mLogging = false;

    /**
     * an interval for the sensor value reading;
     */
    private long mInterval;

    /**
     * treshold values
     */
    private float mThresholdMin, mThresholdMax;

    public static final String KEY_SENSOR_TYPE = "sensor_type";

    public static final String KEY_THRESHOLD_MIN_VALUE = "threshold_min_value";

    public static final String KEY_THRESHOLD_MAX_VALUE = "threshold_max_value";

    public static final String KEY_LOGGING = "logging";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get sensor manager on starting the service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // have a default sensor configured
        int sensorType = Sensor.TYPE_LIGHT;

        Bundle args = intent.getExtras();

        // get some properties from the intent
        if (args != null) {

            // set sensortype from bundle
            if (args.containsKey(KEY_SENSOR_TYPE))
                sensorType = args.getInt(KEY_SENSOR_TYPE);

            // optional logging
            mLogging = args.getBoolean(KEY_LOGGING);

            // treshold values
            // since we want to take them into account only when configured use min and max
            // values for the type to disable
            mThresholdMin = args.containsKey(KEY_THRESHOLD_MIN_VALUE) ? args.getFloat(KEY_THRESHOLD_MIN_VALUE) : Float.MIN_VALUE;
            mThresholdMax = args.containsKey(KEY_THRESHOLD_MAX_VALUE) ? args.getFloat(KEY_THRESHOLD_MAX_VALUE) : Float.MAX_VALUE;
        }

        // we need the light sensor
        Sensor sensor = mSensorManager.getDefaultSensor(sensorType);

        // TODO we could have the sensor reading delay configurable also though that won't do much
        // in this use case since we work with the alarm manager
        mSensorManager.registerListener(this, sensor,
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

        // Log that information for so we can track it in the console (for production code remove
        // this since this will take a lot of resources!!)
        if (mLogging) {

            // grab the values
            StringBuilder sb = new StringBuilder();
            for (float value : event.values)
                sb.append(String.valueOf(value)).append(" | ");

            Log.d(TAG, "received sensor valures are: " + sb.toString());
        }

        // get the value
        // TODO we could make the value index also configurable, make it simple for now
        float sensorValue = event.values[0];

        // if first value is below min or above max threshold but only when configured
        // we need to enable the screen
        if (sensorValue < mThresholdMin || sensorValue > mThresholdMax) {

            // TODO we could even make the actions configurable...

            // wake screen here
            PowerManager pm = (PowerManager) getSystemService(getApplicationContext().POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
            wl.acquire();
            //..screen will stay on during this section..
            wl.release();
        }

        // stop the sensor and service
        mSensorManager.unregisterListener(this);
        stopSelf();
    }


}
