package com.tracker.covid_19tracker.ui.fragments;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import java.util.Stack;

public class VisualTracker extends View {

    private int width;
    private int height;
    private Bitmap bitmap;
    private Canvas canvas;
    private Context context;
    private Paint paint;
    private Stack<Point> track;

    public VisualTracker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.paint = new Paint();
        this.track = new Stack<>();

        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point point : track){
            canvas.drawPoint(point.x, point.y, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(bitmap);
    }

    public void moveCursor(double deltaX, double deltaY){
        Point last = null;
        if (!track.isEmpty()){
            last = track.peek();
        }

        // If first point, start in middle of screen. Else, move cursor relative to last location.
        if (last == null){
            track.push(new Point(width / 2, height / 2));
        } else {
            track.push(new Point((int) (last.x + deltaX), (int) (last.y + deltaY)));
        }
    }

}
