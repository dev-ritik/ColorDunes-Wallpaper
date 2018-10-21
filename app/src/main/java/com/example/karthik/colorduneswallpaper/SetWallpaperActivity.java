package com.example.karthik.colorduneswallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class SetWallpaperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Raleway-Light.ttf");

        final Button button = findViewById(R.id.button);
        button.setTypeface(typeface);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(SetWallpaperActivity.this, LiveTimeWallpaperService.class));
                startActivity(intent);
            }
        });

//                Intent intent;

// try the new Jelly Bean direct android wallpaper chooser first
//                try {
//                    ComponentName component = new ComponentName(getPackageName(), getPackageName() + ".LiveWallpaperService");
//                    intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
//                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component);
//                    startActivity(intent);
//                } catch (android.content.ActivityNotFoundException e3) {
//                    // try the generic android wallpaper chooser next
//                    try {
//                        intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
//                        startActivity(intent);
//                    } catch (android.content.ActivityNotFoundException e2) {
//                        // that failed, let's try the nook intent
//                        try {
//                            intent = new Intent();
//                            intent.setAction("com.bn.nook.CHANGE_WALLPAPER");
//                            startActivity(intent);
//                        } catch (android.content.ActivityNotFoundException e) {
//                            // everything failed, let's notify the user
//                            Toast.makeText(SetWallpaperActivity.this
//                                    , "failed!!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//            }
//        });
        TextView textView = findViewById(R.id.textView);

        textView.setTypeface(typeface);


//
//
    }

}
