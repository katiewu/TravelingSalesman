package edu.upenn.cis573.travelingsalesman;

import android.graphics.Point;

/**
 * Created by Jingyuan on 9/24/15.
 */
public class LineSegment {
    private Point start;
    private Point end;

    public LineSegment(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    public Point getStart(){
        return start;
    }

    public Point getEnd(){
        return end;
    }

    public static double distance(Point p1, Point p2){
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist;
    }

}
