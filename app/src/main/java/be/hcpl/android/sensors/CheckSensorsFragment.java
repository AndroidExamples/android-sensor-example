package be.hcpl.android.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * A fragment that checks all available sensors for this device
 */
public class CheckSensorsFragment extends BaseFragment {


    // you'll always need a reference to the sensorManager
    private SensorManager mSensorManager;

    // and we will populate this list
    private List<Sensor> deviceSensors;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we need to retrieve the system service on the parent activity
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        // this is how to list all sensors
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // check for a specific type of sensor
        //if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            // Success! There's a magnetometer.
        //}
        //else {
            // Failure! No magnetometer.
        //}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_sensors, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO list the information here
        // get the listview from our layout
        ListView listView = (ListView)view.findViewById(R.id.list_sensors);

        // and populate it with the most basic view available for listviews, a single text view
        // only made final so we can refer to it in our anonymuous innerclass for clickListener impl
        final ArrayAdapter<Sensor> listAdapter = new ArrayAdapter<Sensor>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, deviceSensors.toArray(new Sensor[deviceSensors.size()]));
        // set it to our listview
        listView.setAdapter(listAdapter);

        // on click we want to open the sensor data fragment with information about the selected
        // sensor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sensor sensor = (Sensor)listAdapter.getItem(i);

            }
        });
    }
}
