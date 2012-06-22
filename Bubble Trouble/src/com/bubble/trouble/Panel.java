package com.bubble.trouble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {
	private CanvasThread canvasthread;
	private Paint blackPaint;
	private Paint bluePaint;
	private Paint redPaint;
	private Matrix matrix;
	private Path path;
	private Bitmap background;

	private static boolean fireArrow;
	public static float xTouch;
	public static float yArrow;
	public static int canvasHeight;
	public static int canvasWidth;
	private static int stickmanPosition;

	private static Ball ball;

	public Panel(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		canvasthread = new CanvasThread(getHolder(), this);
		setFocusable(true);

		blackPaint = new Paint();
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Paint.Style.STROKE);
		blackPaint.setStrokeWidth(2);

		bluePaint = new Paint();
		bluePaint.setColor(Color.BLUE);

		redPaint = new Paint();
		redPaint.setColor(Color.RED);
		redPaint.setStyle(Paint.Style.STROKE);
		redPaint.setStrokeWidth(2);

		matrix = new Matrix();
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.gridbackground);

		ball = new Ball(50, 125, 50, redPaint, 1, 2, false);

		fireArrow = false;
	}

	@Override
	public void onDraw(Canvas canvas) {
		background = Bitmap.createScaledBitmap(background, canvasWidth,
				canvasHeight, true);
		canvas.drawBitmap(background, matrix, null);
		drawStickMan(canvas);
		if (!ball.isCollided()) {
			ball.draw(canvas);
		}
		if (fireArrow) {
			drawArrow(canvas);
			if (!ball.isCollided())
				ball.checkCollision();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!canvasthread.isRunning()) {
			canvasthread = new CanvasThread(getHolder(), this);
			canvasthread.setRunning(true);
			canvasthread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		canvasthread.setRunning(false);
		while (retry) {
			try {
				canvasthread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		canvasWidth = w;
		canvasHeight = h;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fireArrow = true;
			if (yArrow == (canvasHeight - 50))
				xTouch = stickmanPosition;
			break;
		}
		return true;

	}

	public static void update() {
		if (!ball.isCollided())
			ball.update();
		if (fireArrow)
			updateArrow();
	}

	private static void updateArrow() {
		yArrow -= 10;
		if (yArrow <= 0) {
			fireArrow = false;
			yArrow = canvasHeight - 50;
		}
	}

	private void drawStickMan(Canvas canvas) {
		stickmanPosition += GameScreen.tilt * 5;
		if (stickmanPosition < 0)
			stickmanPosition = 0;
		else if (stickmanPosition > canvasWidth)
			stickmanPosition = canvasWidth;
		canvas.drawCircle(stickmanPosition, canvasHeight - 50, 10, blackPaint);
		canvas.drawRect(stickmanPosition - 1, canvasHeight - 40,
				stickmanPosition + 1, canvasHeight, blackPaint);
		canvas.drawRect(stickmanPosition - 5, canvasHeight - 30,
				stickmanPosition + 5, canvasHeight - 29, blackPaint);
	}

	private void drawArrow(Canvas canvas) {
		path = new Path();
		path.moveTo(0, -10);
		path.lineTo(5, 0);
		path.lineTo(-5, 0);
		path.close();
		path.offset(xTouch, yArrow);
		canvas.drawRect(xTouch - 1, yArrow, xTouch + 1, canvasHeight, bluePaint);
		canvas.drawPath(path, bluePaint);
	}
}