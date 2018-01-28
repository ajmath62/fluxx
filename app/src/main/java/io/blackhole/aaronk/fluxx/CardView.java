package io.blackhole.aaronk.fluxx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by AaronK on 2018-01-27.
 */

public class CardView extends View {
    private String name;
    private int type;

    private Paint outerBoxPaint;
    private Paint textPaint;
    private Paint titleBoxPaint;
    private Rect outerBox;
    private Rect titleBox;

    public CardView(Context context, String name, int type) {
        super(context);
        this.name = name;
        this.type = type;
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CardView, 0, 0);
        name = a.getString(R.styleable.CardView_cardName);
        type = a.getInteger(R.styleable.CardView_cardType, 0);
        a.recycle();

        init();
    }

    private void init() {
        Log.d("MainActivity", "Initializing " + name);
        // Canvas
        // AJK TODO use dp instead of px
        outerBox = new Rect(0, 0, 300, 600);
        titleBox = new Rect(3, 3, 297, 100);

        // Paint
        outerBoxPaint = new Paint();
        outerBoxPaint.setStyle(Paint.Style.STROKE);
        outerBoxPaint.setStrokeWidth(7);
        textPaint = new Paint();
        textPaint.setTextSize(60);
        titleBoxPaint = new Paint();

        int color;
        if (type == 0)  // Keeper
            color = 0xFF00FF00;  // green
        else
            color = 0xFF777777;  // gray
        titleBoxPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("MainActivity", "Drawing " + name);
        canvas.drawRect(outerBox, outerBoxPaint);
        canvas.drawRect(titleBox, titleBoxPaint);
        canvas.drawText(name, 15, 70, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("MainActivity", "Measuring " + name);
        setMeasuredDimension(300, 600);
    }
}
