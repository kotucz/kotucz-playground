package cz.kotuc.test.augmented;

import java.util.Arrays;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

public class MySensorListener implements android.hardware.SensorEventListener {

	String TAG = "Sensors";
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mCompass;
	private Sensor mRotationVectorSensor;

	float[] gravity = new float[] { 0, 0, -1 };
	float[] magnetic = new float[] { 0, 1, 0 };
	
	final float[] mRotationMatrix = new float[16];

	public MySensorListener(Activity act) {
		mSensorManager = (SensorManager) act
				.getSystemService(Activity.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mRotationVectorSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ROTATION_VECTOR);
	}

	void startListening() {
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mCompass,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
		// SensorManager.SENSOR_DELAY_FASTEST);
	}

	void stopListening() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			gravity = event.values;
			Log.d("Sensors", "accelerometer " + Arrays.toString(gravity));
			break;

		case Sensor.TYPE_MAGNETIC_FIELD:
			magnetic = event.values;
			Log.d("Sensors", "magnetic " + Arrays.toString(magnetic));
			break;
		case Sensor.TYPE_ROTATION_VECTOR:
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix , event.values);
            break;
		}

	}

}
