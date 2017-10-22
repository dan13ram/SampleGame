package com.dan13ram.samplegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by ramakrishnan.d on 22/10/17.
 */

public class GameplayScene implements Scene {
    private RectPlayer player;
    private Point playerPoint;

    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private boolean gaveOver = false;
    private long gaveOverTime;

    public GameplayScene() {
        player = new RectPlayer(new Rect(100, 100, 200, 200));
        reset();
    }


    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        player.update();
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        obstacleManager.draw(canvas);
        player.draw(canvas);

        if (gaveOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "GAME OVER");
        }
    }

    @Override
    public void update() {
        if (!gaveOver) {
            obstacleManager.update();
            player.update(playerPoint);
            if (obstacleManager.playerCollide(player)) {
                gaveOver = true;
                gaveOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gaveOver && player.getRectangle().contains((int) event.getX(), (int) event.getY())) {
                    movingPlayer = true;
                }
                if (gaveOver && (System.currentTimeMillis() - gaveOverTime) >= 2000) {
                    reset();
                    gaveOver = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!gaveOver && movingPlayer)
                    playerPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }


    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f - r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);

    }
}
