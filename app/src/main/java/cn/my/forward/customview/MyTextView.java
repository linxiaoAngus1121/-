package cn.my.forward.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by 123456 on 2018/4/15.
 * 自定义星期的TextView
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    public boolean isToday = false;
    private Paint mPaint = new Paint();

    public MyTextView(Context context) {
        super(context);
        intitControl();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intitControl();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intitControl();
    }

    private void intitControl() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isToday) {
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.drawCircle(0, 0, getWidth() / 4, mPaint);
        }
    }
}
