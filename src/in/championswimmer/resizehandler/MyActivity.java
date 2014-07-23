package in.championswimmer.resizehandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button bOff, bOn;

        bOff = (Button) findViewById(R.id.off_button);
        bOn = (Button) findViewById(R.id.on_button);

        bOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Settings.System.putInt(getContentResolver(), "mode_resize", 0);
                Intent i = new Intent(getApplicationContext(), ResizeEdgeService.class);
                stopService(i);
            }
        });

        bOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Settings.System.putInt(getContentResolver(), "mode_resize", 1);

                Intent i = new Intent(getApplicationContext(), ResizeEdgeService.class);
                startService(i);

            }
        });
    }
}
