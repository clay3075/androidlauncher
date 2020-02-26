package com.example.testlauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {
    final List<ImageButton> activity_launchers = new ArrayList<ImageButton>();
    final long APPS_PER_ROW = 5;

    public LauncherActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);

        generateApps();
    }

    public void generateApps() {
        PackageManager pm = this.getPackageManager();
        List<ApplicationInfo> activities = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        TableLayout appTray = (TableLayout) findViewById(R.id.app_tray);

        for (int appIndex = 0; appIndex < activities.size(); appIndex++) {
            ApplicationInfo appInfo = activities.get(appIndex);
            Intent launchIntent = pm.getLaunchIntentForPackage(appInfo.packageName);
            if (launchIntent != null && launchIntent.getCategories().contains(Intent.CATEGORY_LAUNCHER)) {
                ImageButton newLauncher = new ImageButton(this);
                newLauncher.setBackgroundColor(Color.TRANSPARENT);
                Drawable icon = appInfo.loadIcon(getPackageManager());
                setOnClickEvent(newLauncher, appInfo);
                newLauncher.setImageDrawable(icon);
                addAppToAppTray(newLauncher);
                activity_launchers.add(newLauncher);
            }
        }
    }

    public void addAppToAppTray(ImageButton app) {
        TableLayout appTray = (TableLayout) findViewById(R.id.app_tray);

        TableRow row;
        if (appTray.getChildCount() == 0) {
            row = new TableRow(this);
            appTray.addView(row);
        } else {
            row = (TableRow) appTray.getChildAt(appTray.getChildCount() - 1);
        }
        if (row.getChildCount() == APPS_PER_ROW) {
            row = new TableRow(this);
            appTray.addView(row);
        }
        row.setGravity(Gravity.CENTER);
        row.setPadding(20, 20, 20, 20);

        row.addView(app);
    }

    public void setOnClickEvent(ImageButton button, final ApplicationInfo applicationInfo) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(applicationInfo.packageName);
                startActivity(launchIntent);
            }
        });
    }

}
