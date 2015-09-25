package edu.upenn.cis573.travelingsalesman;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jingyuan on 9/24/15.
 */
public class Segments {

    private ArrayList<Point[]> segments;
    private Paint paint;

    public Segments(){
        segments = new ArrayList<Point[]>();
        paint = new Paint();
    }

    public void draw(int color, int width, Canvas canvas){
        for (int i = 0; i < segments.size(); i++) {
            Point[] points = segments.get(i);
            paint.setColor(color);
            paint.setStrokeWidth(width);
            canvas.drawLine(points[0].x, points[0].y, points[1].x, points[1].y, paint);
        }
    }

    public void clear(){
        segments.clear();
    }

    public int size(){
        return segments.size();
    }

    public void removeLast(){
        segments.remove(size() - 1);
    }

    public void add(Point[] points){
        segments.add(points);
    }

    public boolean isCircuit(){
        HashMap<Point, Integer> connections = new HashMap<Point, Integer>();
        for (Point[] pair : segments) {
            Point p1 = pair[0];
            Point p2 = pair[1];
            Integer value = connections.get(p1);
            if (value == null)
                value = 0;
            value++;
            connections.put(p1, value);

            value = connections.get(p2);
            if (value == null)
                value = 0;
            value++;
            connections.put(p2, value);
        }

        if (size() == 0) {
            return false;
        } else {
            for (int v : connections.values()) {
                if (v != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    public double calculateSegment(){
        double myPathLength = 0;
        for (Point[] pair : segments) {
            Point p1 = pair[0];
            Point p2 = pair[1];
            double dx = p1.x - p2.x;
            double dy = p1.y - p2.y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            myPathLength += dist;
        }
        return myPathLength;
    }


}
