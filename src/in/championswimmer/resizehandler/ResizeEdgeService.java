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

    private ResizeObserver mObserver;
    WindowManager wm;
    View viewLeft, viewTop;

    LayoutInflater inflaterLeft, inflaterTop;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Cube26", "onStartCommand called");


        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Cube26", "onCreate called");
        mObserver = new ResizeObserver(new Handler());
        mObserver.observe();


        viewLeft = new View(this);
        viewTop = new View(this);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        inflaterLeft = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        inflaterTop = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        try {
            showEdgesIfRequired();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        Log.d("Cube26", "onDestroy called");

        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Cube26", "onBind called");

        return null;
    }

    public void showEdgesIfRequired() throws Settings.SettingNotFoundException {
        if (Settings.System.getInt(getContentResolver(), "mode_resize") == 1) {

            WindowManager.LayoutParams lpLeft = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY-1,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            WindowManager.LayoutParams lpTop = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY-1,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );

            lpLeft.gravity = Gravity.TOP | Gravity.LEFT;
            lpTop.gravity = Gravity.TOP | Gravity.RIGHT;
            viewLeft = inflaterLeft.inflate(R.layout.left_pane, null);
            viewTop = inflaterTop.inflate(R.layout.top_pane, null);

            wm.addView(viewLeft, lpLeft);
            wm.addView(viewTop, lpTop);
        } else {
            wm.removeView(viewTop);
            wm.removeView(viewLeft);
        }
    }


    public class ResizeObserver extends ContentObserver {

        public ResizeObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            getContentResolver().registerContentObserver(Settings.System.getUriFor(
                    "mode_resize"), false, this);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d("Cube26", "onChange called");



            try {
                showEdgesIfRequired();
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }


        }
    }


}
