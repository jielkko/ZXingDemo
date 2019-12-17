package com.hjl.zxingdemo;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hjl.zxinglibrary.ui.ScanActivity;
import com.hjl.zxinglibrary.ui.ZxingResultCode;


public class MainActivity extends AppCompatActivity {
    private Button mScanBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScanBtn = (Button) findViewById(R.id.scan_btn);
        mScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //资产扫码
                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(i, ZxingResultCode.scan);

            }
        });
    }
}
