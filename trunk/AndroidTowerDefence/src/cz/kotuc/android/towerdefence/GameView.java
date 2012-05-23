package cz.kotuc.android.towerdefence;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

/**
 * View that draws, takes keystrokes, etc. for a simple LunarLander game.
 * 
 * Has a mode which RUNNING, PAUSED, etc. Has a x, y, dx, dy, ... capturing the
 * current ship physics. All x/y etc. are measured with (0,0) at the lower left.
 * updatePhysics() advances the physics based on realtime. draw() renders the
 * ship, and does an invalidate() to prompt another draw() as soon as possible
 * by the system.
 */
class GameView extends SurfaceView implements SurfaceHolder.Callback {
	
	SurfaceHolder mSurfaceHolder;
	
	class GameThread extends Thread {
		
		private volatile boolean running = false;		
		
		@Override
		public void run() {
			while (running) {
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						// if (mMode == STATE_RUNNING) {
						//
						// //updatePhysics();
						// }
						board.actAll();
						board.draw(c);

					}
				} catch (Exception ex) {
					Log.e("GT", "try", ex);
//					throw new RuntimeException(ex); 
				} finally {

					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						try {
							mSurfaceHolder.unlockCanvasAndPost(c);
						} catch (Exception ex) {
							Log.e("GT", "draw exception", ex);
						}
					}
				}
				// try {
				// sleep(1);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
			
		}

		/**
		 * Used to signal the thread whether it should be running or not.
		 * Passing true allows the thread to run; passing false will shut it
		 * down if it's already running. Calling start() after this was most
		 * recently called with false will result in an immediate shutdown.
		 * 
		 * @param b
		 *            true to run, false to shut down
		 */
		public synchronized void setRunning(boolean b) {
			running = b;
		}

	}


	/**
	 * Restores game state from the indicated Bundle. Typically called when
	 * the Activity is being restored after having been previously
	 * destroyed.
	 * 
	 * @param savedState
	 *            Bundle containing the game state
	 */
	public synchronized void restoreState(Bundle savedState) {
		synchronized (mSurfaceHolder) {
			// TODO
		}
	}

	
	/**
	 * Dump game state to the provided Bundle. Typically called when the
	 * Activity is being suspended.
	 * 
	 * @return Bundle with this view's state
	 */
	public Bundle saveState(Bundle map) {
		synchronized (mSurfaceHolder) {
			if (map != null) {
				// map.putInt(KEY_DIFFICULTY, Integer.valueOf(mDifficulty));
				// map.putDouble(KEY_X, Double.valueOf(mX));
				// map.putDouble(KEY_Y, Double.valueOf(mY));
				// map.putDouble(KEY_DX, Double.valueOf(mDX));
				// map.putDouble(KEY_DY, Double.valueOf(mDY));
				// map.putDouble(KEY_HEADING, Double.valueOf(mHeading));
				// map.putInt(KEY_LANDER_WIDTH,
				// Integer.valueOf(mLanderWidth));
				// map.putInt(KEY_LANDER_HEIGHT, Integer
				// .valueOf(mLanderHeight));
				// map.putInt(KEY_GOAL_X, Integer.valueOf(mGoalX));
				// map.putInt(KEY_GOAL_SPEED, Integer.valueOf(mGoalSpeed));
				// map.putInt(KEY_GOAL_ANGLE, Integer.valueOf(mGoalAngle));
				// map.putInt(KEY_GOAL_WIDTH, Integer.valueOf(mGoalWidth));
				// map.putInt(KEY_WINS, Integer.valueOf(mWinsInARow));
				// map.putDouble(KEY_FUEL, Double.valueOf(mFuel));
			}
		}
		return map;
	}

	
	/** Handle to the application context, used to e.g. fetch Drawables. */
	// private Context context;

	/** Pointer to the text view to display "Paused.." etc. */
	// private TextView mStatusText;

	/** The thread that actually draws the animation */
	private GameThread thread = null;

	final GameBoard board;
	final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			board.gui.updateViews();
		};
	};

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		try {
			// public LunarView(Context context) {
			// super(context);

			board = new GameBoard(context, 20, 15, this);

			// register our interest in hearing about changes to our surface
			mSurfaceHolder = getHolder();
			mSurfaceHolder.addCallback(this);

			// create thread only; it's started in surfaceCreated()
//			thread = new GameThread(holder, context, new Handler());
			
			
			// {
			// @Override
			// public void handleMessage(Message m) {
			// mStatusText.setVisibility(m.getData().getInt("viz"));
			// mStatusText.setText(m.getData().getString("text"));
			// }
			// });

			setFocusable(true); // make sure we get key events
			requestFocus();

			// setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// Log.e("event", "touchchl");
			// board.onTouchEvent(event);
			// return false;
			// }
			// });

		} catch (Exception ex) {
			Log.e("LW", "init", ex);
			throw new RuntimeException(ex);
		}

	}

	void initButtons() {
		
		board.gui.initViews(getRootView());
		

	}

	/**
	 * Fetches the animation thread corresponding to this LunarView.
	 * 
	 * @return the animation thread
	 */
	public GameThread getThread() {
		return thread;
	}

	/**
	 * Standard override to get key-press events.
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent msg) {
	// return thread.doKeyDown(keyCode, msg);
	// }

	/**
	 * Standard override for key-up. We actually care about these, so we can
	 * turn off the engine or stop rotating.
	 */
	// @Override
	// public boolean onKeyUp(int keyCode, KeyEvent msg) {
	// return thread.doKeyUp(keyCode, msg);
	// }

	/**
	 * Standard window-focus override. Notice focus lost so we can pause on
	 * focus lost. e.g. user switches to take a call.
	 */
	// @Override
	// public void onWindowFocusChanged(boolean hasWindowFocus) {
	// if (!hasWindowFocus) thread.pause();
	// }

	/**
	 * Installs a pointer to the text view used for messages.
	 */
	// public void setTextView(TextView textView) {
	// mStatusText = textView;
	// }

	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// thread.setSurfaceSize(width, height);
	}

	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		// start the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created
		
//		resume();
		
		// thread.setRunning(true);
		// thread.start();
	}

	public void resume() {
		Log.e("GV", "resuming "+thread);		
		if (thread == null || !thread.running) {
			thread = new GameThread();
			thread.setRunning(true);
			thread.start();
		}
	}

	/**
	 * This pauses application not to be confused with ingame pause (0 speed).
	 */
	public void pause() {
		Log.e("GV", "pausing ");
		
		// already paused
		if (thread==null) return; 			
		
		boolean retry = true;
		
		thread.setRunning(false);
		
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		
		thread = null;
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode

		
		pause();
		
		
//		boolean retry = true;
//		thread.setRunning(false);
//		while (retry) {
//			try {
//				thread.join();
//				retry = false;
//			} catch (InterruptedException e) {
//			}
//		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("touch", "event");
		return board.gui.onTouchEvent(event); // true if consumed event
		// return super.onTouchEvent(event);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		Log.e("focus", "" + gainFocus + "" + direction);
		if (!gainFocus) {
			pause();
		}
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// Log.e("touch", "event");
	// return board.onTouchEvent(event); // true if consumed event
	// }

}