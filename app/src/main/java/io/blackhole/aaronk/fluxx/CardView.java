package io.blackhole.aaronk.fluxx;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by AaronK on 2018-01-27.
 */

public class CardView extends View {
    Resources res = getResources();
    final float scale = res.getDisplayMetrics().density;
    final int card_height = dpsToPixels(res.getInteger(R.integer.card_height));
    final int card_width = dpsToPixels(res.getInteger(R.integer.card_width));

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
        // Canvas
        int title_height = card_height / 5;
        int title_width = card_width - 3;  // cause of the outline width
        outerBox = new Rect(0, 0, card_width, card_height);
        titleBox = new Rect(3, 3, title_width, title_height);

        // Paint
        outerBoxPaint = new Paint();
        outerBoxPaint.setStyle(Paint.Style.STROKE);
        outerBoxPaint.setStrokeWidth(7);
        textPaint = new Paint();
        textPaint.setTextSize(35);
        titleBoxPaint = new Paint();

        int color;
        if (type == 0)  // Keeper
            color = 0xFF00FF00;  // green
        else if (type == 1) // Goal
            color = 0xFFFF00FF;  // magenta
        else
            color = 0xFF777777;  // gray
        titleBoxPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(outerBox, outerBoxPaint);
        canvas.drawRect(titleBox, titleBoxPaint);
        canvas.drawText(name, 15, 50, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(card_width, card_height);
    }

    private int dpsToPixels(float dps) {
        // Convert DP units to pixels
        return (int) (dps * scale + 0.5f);
    }
}
