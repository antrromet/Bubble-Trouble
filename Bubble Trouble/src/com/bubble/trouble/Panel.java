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
	private Paint blackpaint;
	private Paint bluepaint;
	private Paint redPaint;
	private Matrix matrix;
	private Path path;
	private Bitmap background;

	private static boolean fireArrow;
	public static float xTouch;
	public static float yArrow;
	public static int canvasHeight;
	public static int canvasWidth;
	private static int pos;

	private static Ball testBall, testBall1;

	public Panel(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		canvasthread = new CanvasThread(getHolder(), this);
		setFocusable(true);

		blackpaint = new Paint();
		blackpaint.setColor(Color.BLACK);
		blackpaint.setStyle(Paint.Style.STROKE);
		blackpaint.setStrokeWidth(2);

		bluepaint = new Paint();
		bluepaint.setColor(Color.BLUE);

		redPaint = new Paint();
		redPaint.setColor(Color.RED);
		redPaint.setStyle(Paint.Style.STROKE);
		redPaint.setStrokeWidth(2);

		matrix = new Matrix();
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.gridbackground);

		testBall = new Ball(50, 125, 50, bluepaint, 1, 2);
		testBall1 = new Ball(30, 100, 30, redPaint, 1, 3);

		fireArrow = false;
	}

	public Panel(Context context) {
		super(context);
		getHolder().addCallback(this);
		canvasthread = new CanvasThread(getHolder(), this);
		setFocusable(true);

		blackpaint = new Paint();
		blackpaint.setColor(Color.BLACK);

		bluepaint = new Paint();
		bluepaint.setColor(Color.BLUE);

		redPaint = new Paint();
		redPaint.setColor(Color.RED);
		redPaint.setStyle(Paint.Style.STROKE);
		redPaint.setStrokeWidth(2);

		matrix = new Matrix();
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.gridbackground);

		testBall = new Ball(50, 125, 50, bluepaint, 1, 2);
		testBall1 = new Ball(30, 100, 30, redPaint, 1, 3);

		fireArrow = false;
	}

	@Override
	public void onDraw(Canvas canvas) {
		background = Bitmap.createScaledBitmap(background, canvasWidth,
				canvasHeight, true);
		canvas.drawBitmap(background, matrix, null);
		drawStickMan(canvas);
		testBall.draw(canvas);
		testBall1.draw(canvas);
		testBall.checkCollision();
		if (fireArrow)
			drawArrow(canvas);
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
				xTouch = pos;
			break;
		}
		return true;

	}

	public static void update() {
		testBall.update();
		testBall1.update();
		if (fireArrow)
			updateArrow();
	}

	private static void updateArrow() {
		yArrow -= 7.5;
		if (yArrow <= 0) {
			fireArrow = false;
			yArrow = canvasHeight - 50;
		}
	}

	private void drawStickMan(Canvas canvas) {
		pos += GameScreen.tilt * 5;
		if (pos < 0)
			pos = 0;
		else if (pos > canvasWidth)
			pos = canvasWidth;
		canvas.drawCircle(pos, canvasHeight - 50, 10, blackpaint);
		canvas.drawRect(pos - 1, canvasHeight - 40, pos + 1, canvasHeight,
				blackpaint);
		canvas.drawRect(pos - 5, canvasHeight - 30, pos + 5, canvasHeight - 29,
				blackpaint);
	}

	private void drawArrow(Canvas canvas) {
		path = new Path();
		path.moveTo(0, -10);
		path.lineTo(5, 0);
		path.lineTo(-5, 0);
		path.close();
		path.offset(xTouch, yArrow);
		canvas.drawRect(xTouch - 1, yArrow, xTouch + 1, canvasHeight, bluepaint);
		canvas.drawPath(path, bluepaint);
	}
}