package edu.upenn.cis573.travelingsalesman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class GameView extends View {

//    protected ArrayList<Integer> xCoords = new ArrayList<Integer>();
//    protected ArrayList<Integer> yCoords = new ArrayList<Integer>();

//    protected ArrayList<Point> points = new ArrayList<Point>();
    protected Stroke stroke = new Stroke();

    protected Segments segments = new Segments();

//    protected ArrayList<Point[]> segments = new ArrayList<Point[]>();
    private Point firstPoint;
    protected Point[] mapPoints;
    protected int spinnerNum;
    protected int attempt = 0;
//    protected boolean isValidStroke = false;
    protected static final Point[] mapPositions;

    // these points are all hardcoded to fit the UPenn campus map on a Nexus 5
    static {
        mapPositions = new Point[13];
        mapPositions[0] = new Point(475, 134);
        mapPositions[1] = new Point(141, 271);
        mapPositions[2] = new Point(272, 518);
        mapPositions[3] = new Point(509, 636);
        mapPositions[4] = new Point(1324, 402);
        mapPositions[5] = new Point(1452, 243);
        mapPositions[6] = new Point(1667, 253);
        mapPositions[7] = new Point(750,  670);
        mapPositions[8] = new Point(1020, 380);
        mapPositions[9] = new Point(870, 250);
        mapPositions[10] = new Point(540, 477);
        mapPositions[11] = new Point(828, 424);
        mapPositions[12] = new Point(1427, 66);
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public static double calculatePathDistance(ArrayList<Point> points) {

        double total = 0;
        // get the distance between the intermediate points
        for (int i = 0; i < points.size()-1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            double dist = LineSegment.distance(p1, p2);
            total += dist;
        }

        // then need to go back to the beginning
        Point p1 = points.get(points.size()-1);
        Point p2 = points.get(0);
        double dist = LineSegment.distance(p1, p2);
        total += dist;

        return total;

    }

    protected void init() {
        GameActivity activity = (GameActivity)getContext();
        spinnerNum = activity.getNumLocations();

        setBackgroundResource(R.drawable.campus);

        Log.v("GAME VIEW", "init");

        mapPoints = new Point[spinnerNum];

        // This selects several indexes of locations randomly and put them into an array, which size is spinnerNum.
        Set set = new HashSet();
        Random rn = new Random();
        for (int i = 0; i < spinnerNum; i++) {
            int randomNum = rn.nextInt(mapPositions.length);
            while (set.contains(randomNum)) {
                randomNum = rn.nextInt(mapPositions.length);
            }
            set.add(randomNum);
            mapPoints[i] = mapPositions[randomNum];
        }
    }


    public void clearSegments(){
        segments.clear();
    }

    public int segmentsSize(){
        return segments.size();
    }

    public void removeLastSegment(){
        segments.removeLast();
    }

    /*
     * This method is automatically invoked when the View is displayed.
     * It is also called after you call "invalidate" on this object.
     */
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        // draws the stroke
        stroke.draw(Color.YELLOW, 10, canvas);
//        if(isValidStroke) {
//            if (points.size() > 1) {
//                for (int i = 0; i < points.size() - 1; i++) {
//                    Point p1 = points.get(i);
//                    Point p2 = points.get(i + 1);
//
//                    paint.setColor(Color.YELLOW);
//                    paint.setStrokeWidth(10);
//                    canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
//                }
//            }
//        }

        // draws the line segments
        segments.draw(Color.RED, 10, canvas);
//        for (int i = 0; i < segments.size(); i++) {
//            Point[] points = segments.get(i);
//            paint.setColor(Color.RED);
//            paint.setStrokeWidth(10);
//            canvas.drawLine(points[0].x, points[0].y, points[1].x, points[1].y, paint);
//        }

        // draws the points on the map
        paint.setColor(Color.RED);

        for (int i = 0; i < mapPoints.length; i++) {
            int x = mapPoints[i].x;
            int y = mapPoints[i].y;
            canvas.drawRect(x, y, x+20, y+20, paint);
        }

        // detects whether the segments form a circuit - but there's a bug!
        boolean isCircuit = segments.isCircuit();
//        boolean isCircuit = true;
//        HashMap<Point, Integer> connections = new HashMap<Point, Integer>();
//        for (Point[] pair : segments) {
//            Point p1 = pair[0];
//            Point p2 = pair[1];
//            Integer value = connections.get(p1);
//            if (value == null)
//                value = 0;
//            value++;
//            connections.put(p1, value);
//
//            value = connections.get(p2);
//            if (value == null)
//                value = 0;
//            value++;
//            connections.put(p2, value);
//        }
//
//        if (segment.size() == 0) {
//            isCircuit = false;
//        } else {
//            for (int v : connections.values()) {
//                if (v != 2) {
//                    isCircuit = false;
//                    break;
//                }
//            }
//        }


        // see if user has solved the problem
        if ((segments.size() == mapPoints.length) && isCircuit) {
            ArrayList<Point> shortestPath = ShortestPath.shortestPath(mapPoints);
            double shortestPathLength = calculatePathDistance(shortestPath);

            double myPathLength = segments.calculateSegment();

            Log.v("RESULT", "Shortest path length is " + shortestPathLength + "; my path is " + myPathLength);

            double diff = shortestPathLength - myPathLength;
            if (Math.abs(diff) < 0.01) {
                Toast.makeText(getContext(), "You found the shortest path!", Toast.LENGTH_LONG).show();
                attempt = 0;
            }
            else {
                attempt++;
                // after the 3rd failed attempt, show the solution
                if (attempt >= 3) {
                    // draw the solution
                    for (int i = 0; i < shortestPath.size() - 1; i++) {
                        Point a = shortestPath.get(i);
                        Point b = shortestPath.get(i + 1);
                        paint.setColor(Color.YELLOW);
                        paint.setStrokeWidth(10);
                        canvas.drawLine(a.x+10, a.y+10, b.x+10, b.y+10, paint);
                    }
                    Point a = shortestPath.get(shortestPath.size()-1);
                    Point b = shortestPath.get(0);
                    paint.setColor(Color.YELLOW);
                    paint.setStrokeWidth(10);
                    canvas.drawLine(a.x+10, a.y+10, b.x+10, b.y+10, paint);

                    Toast.makeText(getContext(), "Nope, sorry! Here's the solution.", Toast.LENGTH_LONG).show();
                }
                else {
                    int offset = (int) (Math.abs(diff) / shortestPathLength * 100);
                    // so that we don't say that the path is 0% too long
                    if (offset == 0) {
                        offset = 1;
                    }
                    Toast.makeText(getContext(), "Nope, not quite! Your path is about " + offset + "% too long.", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (segments.size() == mapPoints.length && !isCircuit) {
            Toast.makeText(getContext(), "That's not a circuit! Select Clear from the menu and start over", Toast.LENGTH_LONG).show();
        }

    }

    /*
     * This method is automatically called when the user touches the screen.
     */
    public boolean onTouchEvent(MotionEvent event){
        Point p = new Point();
        p.x = ((int)event.getX());
        p.y = ((int)event.getY());

        if(event.getAction() == MotionEvent.ACTION_DOWN){

            for (int i = 0; i < mapPoints.length; i++) {
//                double dx = p.x - mapPoints[i].x;
//                double dy = p.y - mapPoints[i].y;
//                double dist = Math.sqrt(dx*dx + dy*dy);
                double dist = LineSegment.distance(p, mapPoints[i]);
                if (dist < 30) {
                    // the "+10" part is a bit of a fudge factor because the point itself is the
                    // upper-left corner of the little red box but we want the center
                    p.x = mapPoints[i].x+10;
                    p.y = mapPoints[i].y+10;

                    stroke.addPoint(p);
//                    points.add(p);

                    firstPoint = p;

                    stroke.setValidStroke(true);
//                    isValidStroke = true;
                    break;
                }
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (stroke.isValid()) {
                stroke.addPoint(p);
//                points.add(p);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (stroke.isValid()) {
                stroke.clearPoint();
//                points.clear();
                // only add the segment if the release point is within 30 of any of the other points
                for (int i = 0; i < mapPoints.length; i++) {
//                    double dx = p.x - mapPoints[i].x;
//                    double dy = p.y - mapPoints[i].y;
//                    double dist = Math.sqrt(dx * dx + dy * dy);
                    double dist = LineSegment.distance(p, mapPoints[i]);
                    if (dist < 30) {
                        p.x = mapPoints[i].x + 10;
                        p.y = mapPoints[i].y + 10;

                        if (firstPoint.x != p.x && firstPoint.y != p.y) {
                            LineSegment line = new LineSegment(firstPoint, p);
                            segments.add(line);
//                            segments.add(points);
                        }
                        break;
                    }
                }
            }
            stroke.setValidStroke(false);
//            isValidStroke = false;
        }
        else {
            return false;
        }


        // forces a redraw of the View
        invalidate();

        return true;
    }



}
