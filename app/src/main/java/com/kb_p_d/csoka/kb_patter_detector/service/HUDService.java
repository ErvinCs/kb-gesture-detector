package com.kb_p_d.csoka.kb_patter_detector.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import com.kb_p_d.csoka.kb_patter_detector.R;

//TODO - make it work
public class HUDService extends Service {
    HUDView mView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(),"onDestroy", Toast.LENGTH_LONG).show();
        if(mView != null)
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HUDService:", "onCreate");
        Toast.makeText(getBaseContext(),"onCreate", Toast.LENGTH_LONG).show();

        final Bitmap kangoo = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_circle_black_24dp);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                kangoo.getWidth(),
                kangoo.getHeight(),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.BOTTOM;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        mView = new HUDView(this,kangoo);

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                //Log.e("kordinatlar", arg1.getX()+":"+arg1.getY()+":"+display.getHeight()+":"+kangoo.getHeight());
                if(arg1.getX()<kangoo.getWidth() & arg1.getY()>0)
                {
                    Log.d("tıklandı", "touch me");
                }
                return false;
            }

        });

        wm.addView(mView, params);
    }

    class HUDView extends ViewGroup {
        Bitmap kangoo;

        public HUDView(Context context, Bitmap kangoo) {
            super(context);
            this.kangoo = kangoo;
        }

        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            // delete below line if you want transparent back color, but to understand the sizes use back color
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(kangoo, 0, 0, null);
            //canvas.drawText("Hello World", 5, 15, mLoadPaint);
        }

        protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        }

        public boolean onTouchEvent(MotionEvent event) {
            //return super.onTouchEvent(event);
            // Toast.makeText(getContext(),"onTouchEvent", Toast.LENGTH_LONG).show();
            return true;
        }
    }
}
