package com.hjl.zxinglibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjl.zxinglibrary.R;
import com.hjl.zxinglibrary.view.MyInputView;

public class CodeInputActivity extends AppCompatActivity {
    private static final String TAG = "CodeInputActivity";

    private Context mContext;

    private LinearLayout mContentContainer;
    private LinearLayout mMytoolbar;
    private LinearLayout mCancelContainer;
    private LinearLayout mQieContainer;
    private ScrollView mScrollContent;
    private MyInputView mCode;
    private TextView mBtnConfirm;






    private String defultCode = "010120190100001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_input);
        mContext = this;

        mContentContainer = (LinearLayout) findViewById(R.id.content_container);
        mMytoolbar = (LinearLayout) findViewById(R.id.mytoolbar);
        mCancelContainer = (LinearLayout) findViewById(R.id.cancel_container);
        mQieContainer = (LinearLayout) findViewById(R.id.qie_container);
        mScrollContent = (ScrollView) findViewById(R.id.scrollContent);
        mCode = (MyInputView) findViewById(R.id.code);
        mBtnConfirm = (TextView) findViewById(R.id.btn_confirm);


        //监听搜索
   /*     mCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //ToastUtil.showShort(mContext,v.getText().toString());
                    //v.getText().toString()

                    if(mCode.getText().toString().equals("")){
                        showShort("请输入资产条码");
                        return false;
                    }
                    if(mCode.getText().toString().length() != defultCode.length()){
                        showShort("请输入正确的资产条码");
                        return false;
                    }

                    Intent intent = new Intent();
                    intent.putExtra("type", 0);
                    intent.putExtra("code", mCode.getText().toString());
                    setResult(ZxingResultCode.scan, intent);
                    finish();
                    return true;
                }
                return false;
            }
        });*/

        mCode.setOnItemClickListener(new MyInputView.OnItemClickListener() {
            @Override
            public void onItemSearch(String text) {
                Log.d(TAG, "获取搜索内容: " + text);
                if(mCode.getText().toString().equals("")){
                    showShort("请输入资产条码");
                    return;
                }
                if(mCode.getText().toString().length() != defultCode.length()){
                    showShort("请输入正确的资产条码");
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("type", 0);
                intent.putExtra("code", mCode.getText().toString());
                setResult(ZxingResultCode.scan, intent);
                finish();
            }

            @Override
            public void onItemDelete() {
                Log.d(TAG, "删除搜索内容");

            }
        });


        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCode.getText().toString().equals("")){
                    showShort("请输入资产条码");
                    return;
                }
                if(mCode.getText().toString().length() != defultCode.length()){
                    showShort("请输入正确的资产条码");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("type", 0);
                intent.putExtra("code", mCode.getText().toString());
                setResult(ZxingResultCode.scan, intent);
                finish();
            }
        });

        mQieContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCancelContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("code", mCode.getText().toString());
                setResult(ZxingResultCode.scan, intent);
                finish();
            }
        });



    }

    private void showShort(String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

           /* Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);*/

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
