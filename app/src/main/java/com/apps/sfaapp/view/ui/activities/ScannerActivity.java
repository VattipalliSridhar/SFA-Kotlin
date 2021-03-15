package com.apps.sfaapp.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.apps.sfaapp.R;
import com.apps.sfaapp.databinding.ActivityScannerBinding;
import com.apps.sfaapp.databinding.CustomScannerBinding;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.jetbrains.annotations.NotNull;

public class ScannerActivity extends AppCompatActivity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    //  private DecoratedBarcodeView barcodeScannerView;
    // private Button switchFlashlightButton;
    private boolean isFlashLightOn = false;

    private ActivityScannerBinding scannerBinding;
    private CustomScannerBinding customScannerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        scannerBinding = ActivityScannerBinding.inflate(getLayoutInflater());
        customScannerBinding = CustomScannerBinding.bind(scannerBinding.getRoot());
        View view = scannerBinding.getRoot();
        setContentView(view);

        //Initialize barcode scanner view
        // barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        //set torch listener
        scannerBinding.zxingBarcodeScanner.setTorchListener(this);

        //switch flashlight button
        // switchFlashlightButton = (Button) findViewById(R.id.switch_flashlight);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            scannerBinding.switchFlashlight.setVisibility(View.GONE);
        } else {
            scannerBinding.switchFlashlight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchFlashlight();
                }
            });
        }

        //start capture
        capture = new CaptureManager(this, scannerBinding.zxingBarcodeScanner);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (isFlashLightOn) {
            scannerBinding.zxingBarcodeScanner.setTorchOff();
            isFlashLightOn = false;
        } else {
            scannerBinding.zxingBarcodeScanner.setTorchOn();
            isFlashLightOn = true;
        }

    }

    @Override
    public void onTorchOn() {
        scannerBinding.switchFlashlight.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        scannerBinding.switchFlashlight.setText(R.string.turn_on_flashlight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return scannerBinding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}