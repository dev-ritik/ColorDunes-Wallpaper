package com.example.karthik.colorduneswallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by karthik on 6/6/18.
 */

public class LiveTimeWallpaperService extends WallpaperService {

    static boolean numberSign = true, format24 = false, touchEnabled = false;
    static Typeface[] typefaceText = new Typeface[5];
    static int fontIndex = 0, overlayIndex = 0, dividerIndex = 0;
    int xPos = 0, yPos = 0;
    Paint alphaPaint;
    SharedPreferences preferences;
    String time = "";

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends Engine {

        private final Handler handler = new Handler();
        Calendar calendar;
        SimpleDateFormat timeFormat;
        private Paint paint = new Paint();
        private int width;
        private int height;
        private boolean visible = true;
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };


        private WallpaperEngine() {
            preferences = PreferenceManager.getDefaultSharedPreferences(LiveTimeWallpaperService.this);
            Log.i("point 62", "WallpaperEngine");

            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(100);
            paint.setDither(true);

            typefaceText[0] = Typeface.createFromAsset(getAssets(), "Cinzel-Regular.ttf");
            typefaceText[1] = Typeface.createFromAsset(getAssets(), "GloriaHallelujah.ttf");
            typefaceText[2] = Typeface.createFromAsset(getAssets(), "Nexa-Light.otf");
            typefaceText[3] = Typeface.createFromAsset(getAssets(), "Raleway-Light.ttf");
            typefaceText[4] = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

            alphaPaint = new Paint();
            alphaPaint.setAlpha(80);

            handler.post(drawRunner);
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

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.i("point 105", "destroyed");
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
            Log.i("point 113", "changed");
        }


        private void draw() {

            numberSign = preferences.getBoolean("number_sign", true);
            format24 = preferences.getBoolean("24_hr", false);
            fontIndex = Integer.parseInt(preferences.getString("font", "0"));
            paint.setTypeface(typefaceText[fontIndex]);
            overlayIndex = Integer.parseInt(preferences.getString("overlay", "0"));
            dividerIndex = Integer.parseInt(preferences.getString("divider", "0"));
            touchEnabled = preferences.getBoolean("openClock", false);

            calendar = Calendar.getInstance();

            int color = 0;

            if (format24) {
                color = Color.parseColor("#" + ((new SimpleDateFormat("HHmmss")).format(calendar.getTime())));
                timeFormat = new SimpleDateFormat("HH.mm.ss");
            } else {
                color = Color.parseColor("#" + ((new SimpleDateFormat("hhmmss")).format(calendar.getTime())));
                timeFormat = new SimpleDateFormat("hh.mm.ss");
            }

            time = "#" + timeFormat.format(calendar.getTime());


            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    Log.i("point 193", "canvas" + canvas);

                    canvas.drawColor(color);

                    if (numberSign) {
                        xPos = (canvas.getWidth() / 2) - (int) (paint.measureText(time) / 2);
                        yPos = (int) ((canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2));
                        canvas.drawText(time, xPos, yPos, paint);
                    } else {
                        time = time.replaceAll("#", "");
                        xPos = (canvas.getWidth() / 2) - (int) (paint.measureText(time.replaceAll("#", "")) / 2);
                        yPos = (int) ((canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2));
                        canvas.drawText(time, xPos, yPos, paint);
                    }

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

    }

}




