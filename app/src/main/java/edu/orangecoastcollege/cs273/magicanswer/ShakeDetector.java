package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Ethan!
 */

public class ShakeDetector implements SensorEventListener {

    // Constant to represent shake threshold.
    // Get a physical device and adjust this for yourself!
    private static final float SHAKE_THRESHOLD = 25f;

    // Constant to represent forced pause between shakes (in milliseconds)
    private static final int SHAKE_TIME_LAPSE = 2000;

    // Variable to represent the last time the event triggered
    private long timeOfLastShake;

    // Listener to register onShake events
    private OnShakeListener shakeListener;

    // Constructor to make new ShakeDetector passing an OnShakeListener as an arg
    public ShakeDetector(OnShakeListener listener)
    {
        shakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event){

        // Determine if the event is an accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            // Get x, y, and z values when this event occurs.
            // x, y, and z values are stored as floats in the array values[] in the SensorEvent.

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Compare the 3 floats against... uh... gravity.
            // Because Android doesn't know crap besides gravity.
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            // Compare sum of squares
            // We use a double here to store more precision
            double vector = Math.pow(gForceX, 2.0) + Math.pow(gForceY, 2.0) + Math.pow(gForceZ, 2.0);

            // Compute gForce overall.
            float gForce = (float)Math.sqrt(vector);

            if (gForce > SHAKE_THRESHOLD) {
                // Get the current time
                long currentTime = System.currentTimeMillis();

                // Check if the time difference between right now and the last shake
                // is at least as long as SHAKE_TIME_LAPSE.
                if (currentTime - timeOfLastShake > SHAKE_TIME_LAPSE){
                    timeOfLastShake = currentTime;

                    // Register a shake event!
                    shakeListener.onShake();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    // Define new interface (method for other classes to implement) called onShake().
    // It's the responsibility of the MagicAnswerActivity to implement onShake().
    public interface OnShakeListener{
        void onShake();
    }
}
