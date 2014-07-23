package in.championswimmer.resizehandler;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.*;


/**
 * Created by arnav on 8/7/14.
 */
public class ResizeEdgeService extends Service {

    WindowManager wm;
    LeftPane viewLeft;
    View viewTop;

    LayoutInflater inflaterLeft, inflaterTop;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Cube26", "onStartCommand called");


        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        viewTop = new View(this);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        inflaterLeft = LayoutInflater.from(this);
        inflaterTop = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        try {
            showEdgesIfRequired(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.d("Cube26", "onDestroy called");
        try {
            showEdgesIfRequired(false);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Cube26", "onBind called");

        return null;
    }

    public void showEdgesIfRequired(boolean show) throws Settings.SettingNotFoundException {
        if (show) {

            WindowManager.LayoutParams lpLeft = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_TOAST,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            WindowManager.LayoutParams lpTop = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_TOAST,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );

            lpLeft.gravity = Gravity.TOP | Gravity.LEFT;
            lpTop.gravity = Gravity.TOP | Gravity.RIGHT;
            viewLeft = (LeftPane) inflaterLeft.inflate(R.layout.left_pane, null);
            viewTop = inflaterTop.inflate(R.layout.top_pane, null);

            wm.addView(viewLeft, lpLeft);
            viewLeft.generateRecentList();
            //wm.addView(viewTop, lpTop);
        } else {
            //wm.removeView(viewTop);
            wm.removeView(viewLeft);
        }
    }


}
