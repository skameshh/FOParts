package com.example.foparts;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivityForLogin extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private static String TAG = "HALAssetScanActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scan);
        try {
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);                // Set the scanner view as the content view
        }catch (Exception e){
            Log.v(TAG,e.getLocalizedMessage());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            // Do something with the result here
            Log.v(TAG, rawResult.getText()); // Prints scan results
            Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
           // MainActivity.txtCode.setText(rawResult.getText());
            //MainActivity.addStockUI();

            String bar = rawResult.getText();
            String ss = bar.substring(0,2);
            if(ss.equals("00")){
                bar = bar.substring(2);
            }
            MainActivity.txtConfirmationNumber.setText(bar);
            onBackPressed();
            // If you would like to resume scanning, call this method below:
            //mScannerView.resumeCameraPreview(this);
        }catch (Exception e){
            Log.v(TAG,e.getLocalizedMessage());
        }
    }
}
