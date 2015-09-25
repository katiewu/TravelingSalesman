package edu.upenn.cis573.travelingsalesman;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Jingyuan on 9/24/15.
 */
public class Segments {

    private ArrayList<LineSegment> segments;
    private Paint paint;

    public Segments(){
        segments = new ArrayList<LineSegment>();
        paint = new Paint();
    }

    public void draw(int color, int width, Canvas canvas){
        for (int i = 0; i < segments.size(); i++) {
            LineSegment points = segments.get(i);
            paint.setColor(color);
            paint.setStrokeWidth(width);
            canvas.drawLine(points.getStart().x, points.getStart().y, points.getEnd().x, points.getEnd().y, paint);
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

    public void add(LineSegment lineSegment){
        segments.add(lineSegment);
    }

    public boolean isCircuit(){
        HashMap<Point, Integer> connections = new HashMap<Point, Integer>();
        for (LineSegment line : segments) {
            Point p1 = line.getStart();
            Point p2 = line.getEnd();
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

        HashSet<Integer> set = new HashSet<Integer>();
        Point start = segments.get(0).getStart();
        Point end = segments.get(0).getEnd();
        set.add(0);
        while(true){
            start = end;
            end = null;
            for(int i=0;i<segments.size();i++)
            {
                if(set.contains(i)) continue;
                else{
                    LineSegment line = segments.get(i);
                    if(line.getStart().equals(start)){
                        end = line.getEnd();
                        set.add(i);
                        break;
                    }
                    else if(line.getEnd().equals(start)){
                        end = line.getStart();
                        set.add(i);
                        break;
                    }
                }
            }
            if(end == null) break;
        }
        if(set.size() != segments.size()) return false;
        return true;
    }

    public double calculateSegment(){
        double myPathLength = 0;
        for (LineSegment pair : segments) {
            myPathLength += LineSegment.distance(pair.getStart(), pair.getEnd());
        }
        return myPathLength;
    }


}
