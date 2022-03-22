package com.mercure.app.ui.remote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{

    private float centerX, centerY, baseRadius, hatRadius;
    private JoystickListener joystickCallback;

    private void setupDimensions()
    {
        centerX    = getWidth() / 2;
        centerY    = getHeight() /2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius  = Math.min(getWidth(), getHeight()) / 5;
    }

    public JoystickView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);

        if(context instanceof JoystickListener)
        {
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);

        if(context instanceof JoystickListener)
        {
            joystickCallback = (JoystickListener) context;
        }
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);

        if(context instanceof JoystickListener)
        {
            joystickCallback = (JoystickListener) context;
        }
    }

    private void drawJoystick(float x, float y)
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(255,50,50,50);
            canvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255,198,0,0);
            canvas.drawCircle(x, y, hatRadius, colors);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2)
    {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)
    {

    }

    public boolean onTouch(View view, MotionEvent event)
    {
        if(view.equals(this))
        {
            if(event.getAction() != event.ACTION_UP)
            {
                float deplacement = (float) Math.sqrt(Math.pow(event.getX() - centerX , 2) +
                        Math.pow(event.getY() - centerY, 2));

                if(deplacement < baseRadius)
                    drawJoystick(event.getX(), event.getY());
                else{
                    float ratio = baseRadius / deplacement;
                    float contraintX = centerX + (event.getX() - centerX) * ratio;
                    float contraintY = centerY + (event.getY() - centerY) * ratio;
                    drawJoystick(contraintX, contraintY);
                    joystickCallback.onJoystickMoved((contraintX-centerX)/baseRadius,
                            (contraintY-centerY)/baseRadius, getId());
                }
            }
            else{
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0,
                        0, getId());
            }
        }

        return true;
    }

    public interface JoystickListener
    {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}
