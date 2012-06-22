package com.bubble.trouble;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {

	private float xBall;
	private float yBall;
	private double xincordec;
	private double yincordec;
	private int radius;
	private Paint paint;
	private boolean collided;

	Ball(float xBall, float yBall, int radius, Paint paint, double xincordec,
			double yincordec, boolean collided) {
		this.xBall = xBall;
		this.yBall = yBall;
		this.radius = radius;
		this.paint = paint;
		this.xincordec = xincordec;
		this.yincordec = yincordec;
		this.collided = collided;
	}

	public void draw(Canvas canvas) {
		canvas.drawCircle(xBall, yBall, radius, paint);
	}

	public void update() {
		if (xBall >= (Panel.canvasWidth - radius))
			xincordec = -1;
		else if (xBall <= 30)
			xincordec = 1;

		if (yBall >= (Panel.canvasHeight - radius))
			yincordec = -3;
		else if (yBall <= (Panel.canvasHeight - 200))
			yincordec = 3;

		xBall += xincordec;
		yBall += yincordec;
	}

	public boolean checkCollision() {
		if ((Panel.yArrow <= yBall)
				&& (((xBall - radius) == Panel.xTouch) || (xBall + radius) == Panel.xTouch)
				|| (Math.sqrt((Math.pow((xBall - Panel.xTouch), 2))
						+ Math.pow((yBall - Panel.yArrow), 2)) < radius)) {
			GameScreen.vibrator.vibrate(100);
			collided = true;
			Panel.yArrow = -1;
			return collided;

		}
		return false;
	}

	public boolean isCollided() {
		return collided;
	}
}
