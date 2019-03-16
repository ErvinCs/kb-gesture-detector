package com.kb_p_d.csoka.kb_patter_detector.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.kb_p_d.csoka.kb_patter_detector.Code;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class HUD2Service extends Service {
    private final String TAG = "HUD2Serv: ";

    HUDView mView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getBaseContext(), "HUD Created", Toast.LENGTH_LONG).show();
        mView = new HUDView(this);


        /*
         Window type: Application overlay windows are displayed above all activity windows
         (types between FIRST_APPLICATION_WINDOW and LAST_APPLICATION_WINDOW)
         but below critical system windows like the status bar or IME.
         */

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                // | FLAG_LAYOUT_IN_SCREEN
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.END | Gravity.BOTTOM;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), "HUD Destroyed", Toast.LENGTH_LONG).show();
        if (mView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView = null;
        }
    }


    //TODO
    class HUDView extends ViewGroup {
        private final String TAG = "HUDView: ";
        private final String COLOR_CODE = "#87CEFA";

        private Path drawPath;
        private Paint drawPaint,canvasPaint;
        private Bitmap canvasBitmap;
        private Canvas drawCanvas;

        public HUDView(Context context) {
            super(context);
            Toast.makeText(getContext(), "HUDView", Toast.LENGTH_SHORT).show();
            setUpDrawing();
        }

        private void setUpDrawing() {
            drawPath = new Path();
            drawPaint = new Paint();
            drawPaint.setColor(Color.parseColor(COLOR_CODE));
            drawPaint.setAntiAlias(true);
            drawPaint.setStrokeWidth(15);
            drawPaint.setStyle(Paint.Style.STROKE);
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
            //Toast.makeText(getBaseContext(), "onDraw - HUD", Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getContext(), "onTouchEvent", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onTouchEvent");

            canvasBitmap.eraseColor(Color.WHITE);

            int action = event.getAction();
            float touchX = event.getX();
            float touchY = event.getY();
            switch (action)
            {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX,touchY);

                    Log.d(TAG, "ACTION_DOWN");
                    //Toast.makeText(getContext(), "ACTION_DOWN", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX,touchY);

                    Log.d(TAG, "ACTION_MOVE");
                    //Toast.makeText(getContext(), "ACTION_MOVE", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "ACTION_UP");
                    //Toast.makeText(getContext(), "ACTION_UP", Toast.LENGTH_SHORT).show();

                    drawPath.lineTo(touchX,touchY);
                    drawCanvas.drawPath(drawPath,drawPaint);
                    savePattern();
                    drawPath.reset();
                    break;
                default:
                    drawPath.reset();
                    Log.d(TAG, "Returned false");
                    return false;
            }
            Log.d(TAG, "Invalidated");
            invalidate();
            return true;
        }

        private void savePattern() {
            try {
                // Create a new bitmap
                //val bitmap = Bitmap.createBitmap(drawingCacheBitmap)

                //TODO - to kb-pat-det/captured

                // Get image file save path and name.
                String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM" + File.separator + Code.STORAGE_PATH_HUD.getKey();
                File folder = new File(filePath);
                if(!folder.exists())
                    folder.mkdirs();

                DateFormat dateFormat = DateFormat.getDateTimeInstance();
                Date date = new Date();
                filePath += File.separator + dateFormat.format(date).toString().trim().replaceAll("[ :]*", "") + ".png";


                File file = new File(filePath);
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                // Compress bitmap to png image.
                canvasBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                // Flush bitmap to image file.
                fileOutputStream.flush();

                // Close the output stream.
                fileOutputStream.close();

                canvasBitmap.eraseColor(Color.WHITE);

                Toast.makeText(super.getContext(), "Pattern saved", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
                ex.printStackTrace();
            }

        }
    }
}
