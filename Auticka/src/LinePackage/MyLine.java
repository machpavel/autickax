package LinePackage;

import com.badlogic.gdx.math.Vector2;


public class MyLine {
	
	float x1;
	float x2;
	float y1;
	float y2;
	
	public MyLine(Vector2 seg1, Vector2 seg2)
	{
		x1 = seg1.x;
		x2 = seg2.x;
		y1 = seg1.y;
		y2 = seg2.y;
	}
	
	public static int relativeCCW(float x1, float y1, float x2, float y2,
			float px, float py) {
		x2 -= x1;
		y2 -= y1;
		px -= x1;
		py -= y1;
		double ccw = px * y2 - py * x2;
		if (ccw == 0.0) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = px * x2 + py * y2;
			if (ccw > 0.0) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				px -= x2;
				py -= y2;
				ccw = px * x2 + py * y2;
				if (ccw < 0.0) {
					ccw = 0.0;
				}
			}
		}
		return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
	}
	
    public boolean intersectsLine(MyLine l) {
        return linesIntersect(l.getX1(), l.getY1(), l.getX2(), l.getY2(),
                              getX1(), getY1(), getX2(), getY2());
    }
    
    public float getX1()
    {
    	return x1;
    }
    
    public float getX2()
    {
    	return x2;
    }
    
    public float getY1()
    {
    	return y1;
    }
    
    public float getY2()
    {
    	return y2;
    }
    
	public static boolean linesIntersect(float x1, float y1, float x2,
			float y2, float x3, float y3, float x4, float y4) {
		return ((relativeCCW(x1, y1, x2, y2, x3, y3)
				* relativeCCW(x1, y1, x2, y2, x4, y4) <= 0) && (relativeCCW(x3,
				y3, x4, y4, x1, y1) * relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
	}
	
	public Vector2 getSeg1()
	{
		return new Vector2(x1,y1);
	}
	
	public Vector2 getSeg2()
	{
		return new Vector2(x2,y2);
	}
	
    
    
}
