package com.bytedance.clockapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    private static final float DEFAULT_HOUR_NEEDLE_WIDTH = 0.020f;
    private static final float DEFAULT_MINUTE_NEEDLE_WIDTH = 0.020f;
    private static final float DEFAULT_SECOND_NEEDLE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    public final static int TIME_REFRESH = 10;
    public final static int CLOCK_BREAK = -1;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor;
    private int centerOuterColor;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;


    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        size = Math.min(widthWithoutPadding, heightWithoutPadding);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;

        new TimeThread().start();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = Math.min(getWidth(), getHeight());

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }

    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
        textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.05f);
        textPaint.setColor(hoursValuesColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int offsetY = (int) ((bottom - top) / 2 - bottom);

        int r = mCenterX - (int)(mWidth * 0.1f);

        for(int i = 30; i <= FULL_ANGLE; i += 30){

            int drawX = (int) (mCenterX + r * Math.cos(Math.toRadians(i)));
            int drawY = (int) (mCenterY + r * Math.sin(Math.toRadians(i)) + offsetY);

            int number = (int) (i / 30 + 3);
            if(number > 12) number -= 12;
            canvas.drawText(number + "", drawX, drawY, textPaint);
        }

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        // Default Color:
        // - secondsNeedleColor
        // - hoursNeedleColor
        // - minutesNeedleColor
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // hour needle
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_HOUR_NEEDLE_WIDTH);
        paint.setColor(hoursNeedleColor);

        int rIn = (int) (mWidth * 0.03f);
        int rOut = rIn + (int) (mWidth * 0.20f);
        int angle = (int) ((hour / 12.0 * 360) + (minute / 60.0 * 30)
                + (second / 60.0 * 0.5)) - RIGHT_ANGLE;

        int startX = (int) (mCenterX + rIn * Math.cos(Math.toRadians(angle)));
        int startY = (int) (mCenterY + rIn * Math.sin(Math.toRadians(angle)));

        int stopX = (int) (mCenterX + rOut * Math.cos(Math.toRadians(angle)));
        int stopY = (int) (mCenterY + rOut * Math.sin(Math.toRadians(angle)));

        canvas.drawLine(startX, startY, stopX, stopY, paint);

        // minute needle
        paint.setStrokeWidth(mWidth * DEFAULT_MINUTE_NEEDLE_WIDTH);
        paint.setColor(minutesNeedleColor);

        rOut = rIn + (int) (mWidth * 0.25f);
        angle = (int) ((minute / 60.0f * 360) + (second / 60.0 * 6)) - RIGHT_ANGLE;

        startX = (int) (mCenterX + rIn * Math.cos(Math.toRadians(angle)));
        startY = (int) (mCenterY + rIn * Math.sin(Math.toRadians(angle)));

        stopX = (int) (mCenterX + rOut * Math.cos(Math.toRadians(angle)));
        stopY = (int) (mCenterY + rOut * Math.sin(Math.toRadians(angle)));

        canvas.drawLine(startX, startY, stopX, stopY, paint);

        // second needle
        paint.setStrokeWidth(mWidth * DEFAULT_SECOND_NEEDLE_WIDTH);
        paint.setColor(secondsNeedleColor);

        rOut = rIn + (int) (mWidth * 0.30f);
        angle = (int) (second / 60.0 * 360) - RIGHT_ANGLE;

        startX = (int) (mCenterX + rIn * Math.cos(Math.toRadians(angle)));
        startY = (int) (mCenterY + rIn * Math.sin(Math.toRadians(angle)));

        stopX = (int) (mCenterX + rOut * Math.cos(Math.toRadians(angle)));
        stopY = (int) (mCenterY + rOut * Math.sin(Math.toRadians(angle)));

        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        // Default Color:
        // - centerInnerColor
        // - centerOuterColor
        Paint paint = new Paint();

        float radius = mWidth * 0.02f;
        paint.setStrokeWidth(mWidth * 0.01f);
        paint.setAntiAlias(true);

        paint.setColor(centerInnerColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX, mCenterY, radius, paint);

        paint.setColor(centerOuterColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX, mCenterY, radius, paint);
    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_REFRESH:
                    invalidate();
                    break;
                case CLOCK_BREAK:
                    Log.i("Clock","Clock break down");
                    break;
            }
        }
    };

    class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                try{
                    Thread.sleep(1000);
                    mHandler.sendEmptyMessage(TIME_REFRESH);
                }catch (Throwable t){
                    mHandler.sendEmptyMessage(CLOCK_BREAK);
                }
            }
        }
    }

}