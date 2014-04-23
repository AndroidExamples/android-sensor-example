package be.hcpl.android.sensors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import be.hcpl.android.sensors.core.BaseFragment;
import be.hcpl.android.sensors.service.SensorBackgroundService;


/**
 * A Fragment for managing the background service
 */
public class ScheduleServiceFragment extends BaseFragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleServiceFragment.
     */
    public static ScheduleServiceFragment newInstance() {
        ScheduleServiceFragment fragment = new ScheduleServiceFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO add current state of service

        // add configuration options for service (interval, treshold, sensor, precision, ...)
        final EditText editMin = (EditText)view.findViewById(R.id.editMin);
        final EditText editMax = (EditText)view.findViewById(R.id.editMax);
        final EditText editInterval = (EditText)view.findViewById(R.id.editInterval);
        final CheckBox chkLogging = (CheckBox)view.findViewById(R.id.chkLogging);

        view.findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get scheduler and prepare intent
                AlarmManager scheduler = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity().getApplicationContext(), SensorBackgroundService.class);

                // add some extras for config
                Bundle args = new Bundle();
                try {
                    float value = Float.parseFloat(editMin.getText().toString());
                    args.putFloat(SensorBackgroundService.KEY_THRESHOLD_MIN_VALUE, value);
                }catch(Exception e){
                    // ignore
                }
                try {
                    float value = Float.parseFloat(editMax.getText().toString());
                    args.putFloat(SensorBackgroundService.KEY_THRESHOLD_MAX_VALUE, value);
                }catch(Exception e){
                    // ignore
                }
                args.putBoolean(SensorBackgroundService.KEY_LOGGING, chkLogging.isChecked());
                intent.putExtras(args);

                // try getting interval option
                long interval;
                try{
                    interval = Long.parseLong(editInterval.getText().toString());
                }catch(Exception e){
                    // use the default in that case
                    interval = 1000L;
                }

                PendingIntent scheduledIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // start the service
                scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, scheduledIntent);
            }
        });

        view.findViewById(R.id.btnStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager scheduler = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(), SensorBackgroundService.class);
                PendingIntent scheduledIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                scheduler.cancel(scheduledIntent);
            }
        });
    }
}
