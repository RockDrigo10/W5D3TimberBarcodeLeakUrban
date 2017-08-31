package com.example.admin.apptimbertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.leakcanary.RefWatcher;
import com.urbanairship.UAirship;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnBarcode,btnLeakCanary,button;
    IntentIntegrator intentIntegrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBarcode = (Button) findViewById(R.id.btnBarcode);
        btnLeakCanary = (Button)findViewById(R.id.btnLeakCanary);
        button = (Button) findViewById(R.id.async_task);
        CrashlyticsCore core = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build(), new Crashlytics());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                //Add the line number to the tag
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        }
        //DEBUG
        Timber.d("Hello");

        //VERBOSE
        Timber.v("Log");
        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("Scan Barcode");
                intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();

            }
        });


        btnLeakCanary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Rotate the phone now and wait, you have 20secs to test this", Toast.LENGTH_SHORT).show();
                MyAsyncTask asyncTask = (MyAsyncTask) new MyAsyncTask().execute(MainActivity.this);
            }
        });
        UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(TAG, "onActivityResult: Cancelled scan");
                Toast.makeText(this, "Cancelled... ", Toast.LENGTH_SHORT).show();
            }else{
                Log.d(TAG, "onActivityResult: Scanned");
                Toast.makeText(this, "Scanned... " + result.getContents().toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,intent);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApp.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
