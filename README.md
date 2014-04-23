android-sensor-example
======================

Example code for handling sensor data on Android

## Context

In this Android example we'll try to cover sensor management and all things we believe you need to
know to do so properly. Think of opening sensors for reading but also closing them when done. Once
opened how to handle the data, like smoothing with moving average, triggering stuff on tresholds
and more. And sure we'll have to do some of these tasks in de background so we'll also have to make
sure we don't drain the battery too much.

I'll try to make it usefull also. This will probably turn into a highly configurable app that can
trigger stuff (like wake your device) one certain tresholds. That "wake your device" goal is
something I personally need and didn't find a good working alternative for. If possible I'll see
if the app can do more than waking up.

## Fragments

Some of the fragments in this example app with information on what they do.

### ListSensorFragment

This fragment gives you a list for all sensors that were found on this device. See code for details
on how to retrieve this list. This is where to start, use the sensorManager to get a listing
and go from there.

### SensorDataFragment

For a given sensor display data by listening on data changes. This shows how to open a sensor for
listening for data changes but also how to close it properly when done.

### ScheduleServiceFragment and SensorBackgroundService

A background service using the command pattern (not the binder) to run in the background checking
for sensor values. To safe battery the service reads only one sensor value and then unregisters
itself. The Alarmmanager is used for scheduling the sensor reading interval.

If the sensor value is below a given treshold it will wake up the screen.

To wake up the screen:

    PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
    WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
    wakeLock.acquire();

To release the screen lock:

    KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
    KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
    keyguardLock.disableKeyguard();

And the manifest needs to contain:

    < uses-permission android:name="android.permission.WAKE_LOCK" />
    < uses-permission android:name="android.permission.DISABLE_KEYGUARD" />

## Open Points

* complete sensor listing and data visualisation
* add grahps for data view
* implement the wake device on wave or other use case

## Version History

### 0.1.0

Upcoming, first release

## Resources

* A [quick overview of the available sensors on Android devices](http://developer.android.com/guide/topics/sensors/sensors_overview.html)
. Note that these are the possibly available sensors, still depends on hardware so make sure to
check that:
* [nice article about reading sensors in the background](* http://code.tutsplus.com/tutorials/android-barometer-logger-acquiring-sensor-data--mobile-10558)




