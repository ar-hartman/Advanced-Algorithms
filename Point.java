package week4;

public class Point {
    private double x;
	private double y;

	public Point(double x, double y) {
    	if (Double.isInfinite(x) || Double.isInfinite(y))
        	throw new IllegalArgumentException("Coordinates must be finite");
    	if (Double.isNaN(x) ||  Double.isNaN(y))
        	throw new IllegalArgumentException("Coordinates cannot be NaN");
    	if (x == 0.0)
        	x = 0.0; // convert -0.0 to +0.0
    	if (y == 0.0)
        	y = 0.0; // convert -0.0 to +0.0
    	this.x = x;
    	this.y = y;
    }
	
	public int x() {
		return (int) x;
	}
	
	public int y() {
		return (int) y;
	}
}
