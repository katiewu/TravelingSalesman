package edu.upenn.cis573.travelingsalesman;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jingyuan on 9/24/15.
 */
public class Stroke {
    List<Point> points;
    Paint paint;
    boolean isValidStroke;

    public Stroke(){
        this.points = new ArrayList<Point>();
        this.paint = new Paint();
        this.isValidStroke = false;
    }

    public void addPoint(Point p){
        points.add(p);
    }

    public void clearPoint(){
        points.clear();
    }

    public void setValidStroke(boolean valid){
        isValidStroke = valid;
    }

    public boolean isValid(){
        return isValidStroke;
    }

    public void draw(int color, int width, Canvas canvas){
        if(isValid()) {
            if (points.size() > 1) {
                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i + 1);

                    paint.setColor(color);
                    paint.setStrokeWidth(width);
                    canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
                }
            }
        }
    }

}
