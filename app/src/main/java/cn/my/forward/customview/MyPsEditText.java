package cn.my.forward.customview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.my.forward.R;

/**
 * Created by 123456 on 2018/5/5.
 * 自定义密码输入框
 */

public class MyPsEditText extends AppCompatEditText {
    private Drawable mDrawable;
    private Context mContext;

    public MyPsEditText(Context context) {
        super(context);
        mContext = context;
        setDrawable();
    }

    public MyPsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setDrawable();
    }

    private void setDrawable() {
        mDrawable = mContext.getResources().getDrawable(R.drawable.noopeneye);
        setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawable, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventX, eventY)) {
                //显示密码就隐藏 ,隐藏就显示
                if (getTransformationMethod() instanceof HideReturnsTransformationMethod) { //隐藏的
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mDrawable = mContext.getResources().getDrawable(R.drawable.noopeneye);
                    setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawable, null);
                } else {
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mDrawable = mContext.getResources().getDrawable(R.drawable.open);
                    setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawable, null);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
