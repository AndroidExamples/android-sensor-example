package be.hcpl.android.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Example of listening for sensor data
 */
public class SensorDataFragment extends BaseFragment implements SensorEventListener {

    // again the sensor manager
    private SensorManager mSensorManager;

    // fixed single sensor reference
    // TODO get this one from the sensor listing fragment instead
    private Sensor mLight;

    // TODO make speed selectable from UI

    // the textview to show all the received sensor data
    private TextView mSensorDataView;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor_data, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSensorDataView = (TextView)view.findViewById(R.id.text_data);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float lux = event.values[0];
        // Do something with this sensor value.

        // TODO show all data, also for other sensors, append instead of replace
        mSensorDataView.setText(String.valueOf(lux));

        // make sure to never block this method, this will be called on every value update
    }

    @Override
    public void onResume() {
        super.onResume();
        // start listening for new values here
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        // we can use faster data delays like: SENSOR_DELAY_GAME (20,000 microsecond delay),
        // SENSOR_DELAY_UI (60,000 microsecond delay), or SENSOR_DELAY_FASTEST
    }

    @Override
    public void onPause() {
        super.onPause();

        // never forget to unregister
        mSensorManager.unregisterListener(this);
    }
}
