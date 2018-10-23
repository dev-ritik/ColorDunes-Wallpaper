package com.example.karthik.colorduneswallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by karthik on 6/6/18.
 */

public class LiveTimeWallpaperService extends WallpaperService {

    static boolean format24 = false;
    int xPos = 0, yPos = 0;
    SharedPreferences preferences;
    String time = "";

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends Engine {

        private final Handler handler = new Handler();
        Calendar calendar;
        private Paint paint = new Paint();
        private boolean visible = true;
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };


        private WallpaperEngine() {
            preferences = PreferenceManager.getDefaultSharedPreferences(LiveTimeWallpaperService.this);

            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(100);
            paint.setDither(true);

            handler.post(drawRunner);
        }


        private void draw() {
            format24 = preferences.getBoolean("24_hr", false);

            calendar = Calendar.getInstance();

            int color;

            if (format24) {
                color = Color.parseColor("#" + ((new SimpleDateFormat("HHmmss")).format(calendar.getTime())));
                time = new SimpleDateFormat("HH.mm.ss").format(calendar.getTime());

            } else {
                color = Color.parseColor("#" + ((new SimpleDateFormat("hhmmss")).format(calendar.getTime())));
                time = new SimpleDateFormat("hh.mm.ss").format(calendar.getTime());
            }

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {

                    canvas.drawColor(color);

                    xPos = (canvas.getWidth() / 2) - (int) (paint.measureText(time) / 2);
                    yPos = (int) ((canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2));
                    canvas.drawText(time, xPos, yPos, paint);

                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 1000);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

    }

}