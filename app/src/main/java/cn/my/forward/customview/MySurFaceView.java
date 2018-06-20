package cn.my.forward.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by 123456 on 2018/6/6.
 * 自定义个SurfaceView
 */

public class MySurFaceView extends SurfaceView {

    public MySurFaceView(Context context) {
        super(context);
    }

    public MySurFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySurFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void draw(Canvas canvas) {
        Log.e("onDraw", "draw: test");
        Path path = new Path();
        //设置裁剪的圆心，半径
        path.addCircle(getMeasuredHeight() / 2, getMeasuredHeight() / 2, getMeasuredHeight() / 2,
                Path.Direction.CCW);
        //裁剪画布，并设置其填充方式
        canvas.clipPath(path, Region.Op.REPLACE);
        super.draw(canvas);
    }


}
