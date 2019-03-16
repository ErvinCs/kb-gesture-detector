package com.kb_p_d.csoka.kb_patter_detector.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class HUD2Service extends Service {
    HUDView mView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG).show();
        mView = new HUDView(this);


        /*
         Window type: Application overlay windows are displayed above all activity windows
         (types between FIRST_APPLICATION_WINDOW and LAST_APPLICATION_WINDOW)
         but below critical system windows like the status bar or IME.
         */
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                1,
                1,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                // | FLAG_LAYOUT_IN_SCREEN
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.END | Gravity.TOP;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_LONG).show();
        if (mView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView = null;
        }
    }


    class HUDView extends ViewGroup {
        private Paint mLoadPaint;

        private Path drawPath;
        private Paint drawPaint,canvasPaint;
        private Bitmap canvasBitmap;
        private Canvas drawCanvas;

        public HUDView(Context context) {
            super(context);
            Toast.makeText(getContext(), "HUDView", Toast.LENGTH_LONG).show();
            setUpDrawing();

//            mLoadPaint = new Paint();
//            mLoadPaint.setAntiAlias(true);
//            mLoadPaint.setTextSize(10);
//            mLoadPaint.setARGB(255, 255, 0, 0);
        }

        private void setUpDrawing() {
            drawPath = new Path();
            drawPaint = new Paint();
            drawPaint.setColor(Color.parseColor("#ff0000"));
            drawPaint.setAntiAlias(true);
            drawPaint.setStrokeWidth(20);
            drawPaint.setStyle(Paint.Style.STROKE);
            //drawPaint.setStrokeJoin(Paint.Join.ROUND);
            drawPaint.setStrokeCap(Paint.Cap.ROUND);
            canvasPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
            drawCanvas=new Canvas(canvasBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Toast.makeText(getBaseContext(), "onDraw - HUD", Toast.LENGTH_LONG).show();
            super.onDraw(canvas);
            canvas.drawBitmap(canvasBitmap,0,0,canvasPaint);
            canvas.drawPath(drawPath,drawPaint);

            //canvas.drawText("Hello World", 5, 15, mLoadPaint);
        }

        @Override
        protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //return super.onTouchEvent(event);
            Toast.makeText(getContext(), "onTouchEvent", Toast.LENGTH_LONG).show();
            int action=event.getAction();
            float touchX=event.getX();
            float touchY=event.getY();
            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX,touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX,touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawPath.lineTo(touchX,touchY);
                    drawCanvas.drawPath(drawPath,drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
            invalidate();
            return true;
        }
    }
}
