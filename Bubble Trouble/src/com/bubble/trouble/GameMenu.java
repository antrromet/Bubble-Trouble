package com.bubble.trouble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class GameMenu extends Activity {

	private Button startButton;

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gamemenu);
		getViews();
		setClickListeners();
	}

	private void getViews() {
		startButton = (Button) findViewById(R.id.startButton);
	}

	private void setClickListeners() {
		startButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				intent = new Intent(GameMenu.this, GameScreen.class);
				startActivity(intent);
			}
		});
	}
}
