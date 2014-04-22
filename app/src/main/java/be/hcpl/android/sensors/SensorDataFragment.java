package be.hcpl.android.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
    private Sensor mCurrentSensor;

    public static final String KEY_SENSOR_TYPE = "selected_sensor_type";

    // TODO make speed selectable from UI, same for moving average window and more

    // the textview to show all the received sensor data
    private TextView mSensorDataView;

    /**
     * helper to create an instance of this fragment with the given arguments bundle
     * @param bundle
     * @return
     */
    public static SensorDataFragment getInstance(Bundle bundle ){
        SensorDataFragment instance = new SensorDataFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // always need a sensor manager
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // retrieve optional information from bundle
        Bundle arguments = getArguments();
        if( arguments != null && arguments.containsKey(KEY_SENSOR_TYPE)) {
            int sensorType = arguments.getInt(KEY_SENSOR_TYPE);
            mCurrentSensor = mSensorManager.getDefaultSensor(sensorType);
        }
        // some default to fallback to
        else {
            mCurrentSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSensorDataView = (TextView)view.findViewById(R.id.text_data_value);

        // show some information about the selected sensor
        TextView textView = (TextView)view.findViewById(R.id.text_selected_sensor_value);
        textView.setText(new StringBuilder("Selected sensor is: ").append(mCurrentSensor));
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
        mSensorDataView.append(new StringBuilder(String.valueOf(lux)).append("\r\n"));

        // make sure to never block this method, this will be called on every value update
    }

    @Override
    public void onResume() {
        super.onResume();
        // start listening for new values here
        mSensorManager.registerListener(this, mCurrentSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
