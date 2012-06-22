package com.bubble.trouble;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

public class GameScreen extends Activity implements SensorEventListener {

	public static int tilt;

	private SensorManager sensorManager;
	public static Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.gamescreen);
		initialize();
	}

	private void initialize() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Panel.yArrow = getWindowManager().getDefaultDisplay().getHeight() - 50;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void onSensorChanged(SensorEvent sensorEvent) {
		{
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
				if (sensorEvent.values[1] > 0)
					tilt = -1;
				else if (sensorEvent.values[1] < 0)
					tilt = 1;
				else
					tilt = 0;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}
}
