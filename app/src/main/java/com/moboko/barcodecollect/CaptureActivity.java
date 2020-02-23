package com.moboko.barcodecollect;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.moboko.barcodecollect.util.Consts.INSERT_PROC;

public class CaptureActivity extends com.journeyapps.barcodescanner.CaptureActivity {
    Intent intent = new Intent(this, MainActivity.class);


    public void onPointerCaptureChanged() {
        IntentIntegrator integrator = new IntentIntegrator(this);
       // integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0); // Use a specific camera of the device
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jan_cd_capture_activity);

        onPointerCaptureChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                setResult(RESULT_CANCELED, intent);
                finish();
            } else {
                intent.putExtra(INSERT_PROC,result.getContents());
                setResult(RESULT_OK, intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
