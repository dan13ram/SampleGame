package com.dan13ram.samplegame;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by ramakrishnan.d on 22/10/17.
 */

public interface Scene {
    public void draw(Canvas canvas);
    public void update();
    public void terminate();
    public void receiveTouch(MotionEvent event);
}
