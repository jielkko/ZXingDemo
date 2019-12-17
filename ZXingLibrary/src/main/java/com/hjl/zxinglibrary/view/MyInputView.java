package com.hjl.zxinglibrary.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjl.zxinglibrary.R;


/**
 * 自定义搜索框
 */
public class MyInputView extends LinearLayout {
    private static final String TAG = "SearchView";
    private Context mContext;

    //<!--是否单选 true 单选  false  多选-->
    boolean mIsSingle = true;

    public MyInputView(Context context) {
        super(context);
    }

    public MyInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    public MyInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private LinearLayout mContainer;
    private ImageView mInputLeftIcon;
    private EditText mInputContent;
    private ImageView mInputRightIcon;


    //图标
    Drawable mIcClose;


    private void init(AttributeSet attrs) {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); //第一个参数是宽,第二个参数是高
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.view_my_input, null);
        view.setLayoutParams(lp);
        addView(view);


        Resources resources = mContext.getResources();
        mIcClose = resources.getDrawable(R.drawable.ic_delete_gray);

        mContainer = (LinearLayout) view.findViewById(R.id.container);
        mInputLeftIcon = (ImageView) view.findViewById(R.id.input_left_icon);
        mInputContent = (EditText) view.findViewById(R.id.input_content);
        mInputRightIcon = (ImageView) view.findViewById(R.id.input_right_icon);


        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.customer);


        if (array.getResourceId(R.styleable.customer_mInputLeftIcon, 0) != 0) {
            mInputLeftIcon.setImageResource(array.getResourceId(R.styleable.customer_mInputLeftIcon, 0));
            mInputLeftIcon.setVisibility(VISIBLE);
        }


        //是否只读 true 只读  false  可以输入
        boolean mIsContentShow = array.getBoolean(R.styleable.customer_mIsContentShow, true);
        if (mIsContentShow) {
            mInputContent.setText(array.getString(R.styleable.customer_mContent));
            mInputContent.setVisibility(VISIBLE);
        } else {
            mInputContent.setText(array.getString(R.styleable.customer_mContent));
            mInputContent.setVisibility(GONE);
        }





        mInputContent.setText(array.getString(R.styleable.customer_mContent));
        mInputContent.setHint(array.getString(R.styleable.customer_mHint));


        //监听输入框内容
        mInputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = mInputContent.getText().toString();
                if ("".equals(content)) {
                    mInputRightIcon.setVisibility(GONE);
                } else {
                    mInputRightIcon.setVisibility(VISIBLE);
                }
            }
        });

        //监听搜索
        mInputContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //ToastUtil.showShort(mContext,v.getText().toString());
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemSearch(v.getText().toString());
                    }

                    return true;
                }
                return false;
            }
        });


        //删除按钮点击事件
        mInputRightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputContent.setText("");

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemDelete();
                }
            }
        });


        //是否只读 true 只读  false  可以输入
        mIsClick = array.getBoolean(R.styleable.customer_mIsClick, false);
        mContainer.setEnabled(mIsClick);
        if (!mIsClick) {
            mInputContent.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        }


        array.recycle();


    }

    Boolean mIsClick = false;

    public void setClick(Boolean isClick) {
        mIsClick = isClick;
        mInputContent.setEnabled(mIsClick);
        if (!mIsClick) {
            mInputContent.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        }
    }

    public void setText(String text) {
        mInputContent.setText(text);
    }

    public String getText() {
        return mInputContent.getText().toString();
    }

    //点击
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        /**
         * 返回搜索的内容
         * @param text
         */
        void onItemSearch(String text);

        /**
         * 删除监听
         * @param
         */
        void onItemDelete();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


}
