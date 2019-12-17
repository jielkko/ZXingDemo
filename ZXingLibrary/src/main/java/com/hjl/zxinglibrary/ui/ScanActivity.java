package com.hjl.zxinglibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.hjl.zxinglibrary.ZXingView;
import com.hjl.zxinglibrary.R;
import com.hjl.zxinglibrary.utils.PonWindowUtil;
import com.hjl.zxinglibrary.utils.statusBar.Eyes;
import com.hjl.zxinglibrary.utils.statusBar.ScanStatusBarUtil;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ScanActivity extends AppCompatActivity {
    private static final String TAG = "ScanActivity";



    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY1 = 6661;


    private Context mContext;

    private ZXingView mZxingView;
    private LinearLayout mMytoolbar;
    private LinearLayout mBtnCancel;
    private TextView mScanHint;
    private LinearLayout mBottomContainer;
    private LinearLayout mBtnCodeInput;
    private LinearLayout mBtnOpenLight;
    private LinearLayout mBtnCloseLight;



    private String defultCode = "010120190100001";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mContext = this;

        Eyes.translucentStatusBar(this, true);

        mZxingView = (ZXingView) findViewById(R.id.zxing_view);
        mMytoolbar = (LinearLayout) findViewById(R.id.mytoolbar);
        mBtnCancel = (LinearLayout) findViewById(R.id.btn_cancel);
        mScanHint = (TextView) findViewById(R.id.scan_hint);
        mBottomContainer = (LinearLayout) findViewById(R.id.bottom_container);
        mBtnCodeInput = (LinearLayout) findViewById(R.id.btn_code_input);
        mBtnOpenLight = (LinearLayout) findViewById(R.id.btn_open_light);
        mBtnCloseLight = (LinearLayout) findViewById(R.id.btn_close_light);


        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //mZXingView.setDelegate(this);
        mZxingView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.i(TAG, "result:" + result);

                mScanHint.setText("扫描结果为：" + result);
                mZxingView.closeFlashlight(); // 关闭闪光灯
                vibrate();


               /* if(result.length() != defultCode.length()){
                    showShort("请扫描正确的资产条码");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            //execute the task
                            mZxingView.startSpot(); // 开始识别
                        }
                    }, 2 * 1000);
                    return;
                }*/


                //获取到条码
                Intent intent = new Intent();
                intent.putExtra("type", 0);
                intent.putExtra("code", result);
                setResult(ZxingResultCode.scan, intent);
                finish();
            }

            @Override
            public void onCameraAmbientBrightnessChanged(boolean isDark) {
                // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
                String tipText = mZxingView.getScanBoxView().getTipText();
                String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
                if (isDark) {
                    if (!tipText.contains(ambientBrightnessTip)) {
                        mZxingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
                    }
                } else {
                    if (tipText.contains(ambientBrightnessTip)) {
                        tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                        mZxingView.getScanBoxView().setTipText(tipText);
                    }
                }
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                Log.e(TAG, "打开相机出错");
            }
        });


        mBtnCodeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZxingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框


                Intent i = new Intent(ScanActivity.this, CodeInputActivity.class);
                startActivityForResult(i, ZxingResultCode.scan);
            }
        });

        mBtnOpenLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZxingView.openFlashlight(); // 打开闪光灯

                mBtnOpenLight.setVisibility(View.GONE);
                mBtnCloseLight.setVisibility(View.VISIBLE);
            }
        });

        mBtnCloseLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZxingView.closeFlashlight(); // 关闭闪光灯
                mBtnCloseLight.setVisibility(View.GONE);
                mBtnOpenLight.setVisibility(View.VISIBLE);

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        mZxingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZxingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
        mZxingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
        List<BarcodeFormat> formatList = new ArrayList<>();
        formatList.add(BarcodeFormat.QR_CODE);
        hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
        hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
        //mZxingView.setType(BarcodeType.ALL, hintMap); // 自定义识别的类型

        mZxingView.setType(BarcodeType.TWO_DIMENSION, hintMap); // 自定义识别的类型
        mZxingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZxingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZxingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //人员选择
        if (requestCode == ZxingResultCode.scan) {

            if (data != null) {
                int type = data.getIntExtra("type",1);
                String code = data.getStringExtra("code");
                if (type == 0) {
                    //获取到条码
                    Intent intent = new Intent();
                    intent.putExtra("type", 0);
                    intent.putExtra("code", code);
                    setResult(ZxingResultCode.scan, intent);
                    finish();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("type", 1);
                    intent.putExtra("code", code);
                    setResult(ZxingResultCode.scan, intent);
                    finish();
                }

            }

        }

    }

    private PopupWindow mPopupWindow;

    private void showDialog(View v) {

        if (mPopupWindow == null) {
            // get the height of screen
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenHeight = metrics.heightPixels;
            // 一个自定义的布局，作为显示的内容
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.pop_code_input, null);


            LinearLayout mContentContainer = (LinearLayout) view.findViewById(R.id.content_container);
            LinearLayout mMytoolbar = (LinearLayout) view.findViewById(R.id.mytoolbar);
            LinearLayout mCancelContainer = (LinearLayout) view.findViewById(R.id.cancel_container);
            LinearLayout mQieContainer = (LinearLayout) view.findViewById(R.id.qie_container);
            ScrollView  mScrollContent = (ScrollView) view.findViewById(R.id.scrollContent);

            mScrollContent.setVerticalScrollBarEnabled(false);
            mScrollContent.setHorizontalScrollBarEnabled(false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //layoutParams.setMargins(0, ScanStatusBarUtil.getStatusBarHeight(mContext), 0, 0);//4个参数按顺序分别是左上右下
            mContentContainer.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(0, ScanStatusBarUtil.getStatusBarHeight(mContext), 0, 0);//4个参数按顺序分别是左上右下
            mMytoolbar.setLayoutParams(layoutParams2);
            mCancelContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();

                }
            });

            mQieContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });


            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                   /* WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getWindow().setAttributes(lp);*/

                    mZxingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                }
            });
            //mPopupWindow.setClippingEnabled(false);
            mPopupWindow.setAnimationStyle(R.style.animTranslate);


            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //ToastUtil.showLong(PonWindowUtil.getNavigationBarHeight(mContext)+"");
                if(PonWindowUtil.getNavigationBarHeight(mContext)>0){
                    try {
                        Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                        mLayoutInScreen.setAccessible(true);
                        mLayoutInScreen.set(mPopupWindow, true);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }

            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
            // 我觉得这里是API的一个bug
            //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        }


        // 设置好参数之后再show
        //mPopupWindow.showAsDropDown(view);
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
     /*   WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);*/
    }



    private void showShort(String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }



}
