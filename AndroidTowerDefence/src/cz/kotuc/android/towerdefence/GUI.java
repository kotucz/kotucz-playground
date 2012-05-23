package cz.kotuc.android.towerdefence;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cz.kotuc.android.towerdefence.GameBoard.Patch;
import cz.kotuc.android.towerdefence.GameBoard.Tower;

class GUI implements OnTouchListener {

	/**
	 * 
	 */
	private final GameBoard gameBoard;

	/**
	 * @param gameBoard
	 */
	GUI(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	public GameView view;
	final RectF tower1button = new RectF(500, 380, 600, 480);
	final RectF tower2button = new RectF(600, 380, 700, 480);

	Button sellButton;
	Button tower1Button;

	TextView cashTextView;

	RelativeLayout towerEditView;

	void initViews(View view) {
		this.sellButton = ((Button) view.findViewById(R.id.sellButton));
		sellButton.setOnTouchListener(this);
		this.tower1Button = ((Button) view.findViewById(R.id.gameButton1));
		tower1Button.setOnTouchListener(this);

		this.towerEditView = ((RelativeLayout) view
				.findViewById(R.id.towerEditView));
		this.cashTextView = ((TextView) view.findViewById(R.id.cashTextView));

	}

	float prevx;
	float prevy;

	int events = 0;

	TowerType buildingTower = null;
	Patch selectedPatch = null;

	void showTowerDialog() {
		towerEditView.setVisibility(View.VISIBLE);
	}

	void hideTowerDialog() {
		towerEditView.setVisibility(View.GONE);
	}

	void buildTowerSelected(TowerType type) {
		this.buildingTower = type;
	}

	void submitBuildTower() {
		int price = buildingTower.price;
		if (gameBoard.cash < price) {
			Log.e("GUI", "Low money");
		} else if (selectedPatch != null) {
			selectedPatch.buildTower(buildingTower);
			this.gameBoard.updatePaths();
			this.gameBoard.cash -= price;
		}
		buildingTower = null;
	}

	void sellTower() {
		if (selectedPatch != null) {

			this.gameBoard.cash += selectedPatch.tower.tower.price;

			selectedPatch.sellTower();			
//			selectedPatch.setTower(null);

			this.gameBoard.updatePaths();
		}
		buildingTower = null;
		hideTowerDialog();
	}

	void updateViews() {
		if (selectedPatch != null) {
			sellButton.setText("sell " + selectedPatch.tower);
		}
		cashTextView.setText("$" + gameBoard.cash);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {
			Log.e("GUI", "onTouch " + v + " " + v.getId());
			event.offsetLocation(v.getLeft(), v.getTop());
			switch (v.getId()) {
			case R.id.gameButton1:
				Log.e("GUI", "game button");
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					buildingTower = TowerType.TYPE1;
					return true;
				} else {
					// if (event.getAction() == MotionEvent.ACTION_UP) {
					// Log.e("GUI", "game button 1 up");
					onTouchEvent(event);
					return true;
				}
				// break;

			case R.id.sellButton:
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.e("GUI", "selling tower");
					sellTower();
					return true;
				}
				break;

			}
		} catch (Exception ex) {
			Log.e(GameBoard.TAG, "touch error", ex);
		}
		return false;
	}

	public synchronized boolean onTouchEvent(MotionEvent event) {
		Log.e("GUI", "onTouchEvent ");
		try {
			events++;
			float x = event.getX();
			float y = event.getY();
			selectedPatch = this.gameBoard.vscreen.getTouchField(x, y);
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (tower1button.contains(x, y)) {
					buildingTower = TowerType.TYPE1;
					return true;
				} else if (tower2button.contains(x, y)) {
					buildingTower = TowerType.TYPE2;
					return true;
				}
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (buildingTower == null) {
					this.gameBoard.vscreen.offx += x - prevx;
					this.gameBoard.vscreen.offy += y - prevy;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (buildingTower != null) {
					submitBuildTower();
				} else {
					if (selectedPatch != null) {
						if (selectedPatch.tower != null) {
							Log.e("GB", "tower " + (selectedPatch.tower));
							showTowerDialog();
						}
					}
				}
			}
			prevx = x;
			prevy = y;
			return true; // true if consumed event
		} catch (Exception ex) {
			Log.e(GameBoard.TAG, "touch error", ex);
			return false;
		}
	}

	void draw(Canvas canvas) {
		// updateViews();

		view.handler.sendEmptyMessage(1);

		canvas.drawBitmap(GameBoard.data.towerbm, null, tower1button,
				GameBoard.data.paint);
		canvas.drawBitmap(GameBoard.data.towerbm2, null, tower2button,
				GameBoard.data.paint);
	}

	void drawSelectedTower(Canvas canvas) {

		if (selectedPatch != null) {
			float x = selectedPatch.getpX();
			float y = selectedPatch.getpY();
			canvas.drawOval(new RectF(x - 1, y - 1, x + 2, y + 2),
					gameBoard.data.paint);
			if (buildingTower != null) {
				// if (buildable)
				canvas.drawBitmap(buildingTower.getBitmap(), null, new RectF(
						x - 1, y - 1, x + 2, y + 2), GameBoard.data.paint);
				// } else {
				// showCancelBuilding
				// }
			}
		}

	}

}