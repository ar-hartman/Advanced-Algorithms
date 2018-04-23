package week4;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public abstract class GrahamsScan implements Comparable<Point>{

	public int crossProduct() {
		
		
		
		return -1;
	}
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("resource")
	public static int[][] readFile(String CASE) throws IOException{
		int[][] points = null;
		System.out.println("Test File Selected: " + CASE);
		String fileName = "C:\\Users\\addison\\Desktop\\School\\DePaul\\CSC 421 - Advanced Algorithms\\week 4\\Test Files\\" + CASE + ".txt";
		int counter = 0;
		
		// open file and scanner
		File file = new File(fileName);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(file);
		
		//use scanner to count the number of points in the case file
		while (sc.hasNextLine()) {
			System.out.println(sc.nextLine());
			counter++;
		}
		
		// create a two-dimensional array for the number of points present
		points = new int[counter][2];
		counter = 0;
		
		// new scanner to load points into 2D array
		sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String nextLine = sc.nextLine();
			//System.out.println(nextLine);			

			// strip the row of empty space and add comma to delimit on
			String strippedLine = nextLine.replaceAll(" ", ",");		
			int j = 0;
			int stringLength = strippedLine.length();
			for (int k = 0; k < 2; k++) {
				//count string char location of delimiting comma
				while (strippedLine.charAt(j) != 44) {
					j++;
				}
				// y-coordinate of point. starting index is 1 plus the location of the comma
				if (k == 1) {
					points[counter][k] = Integer.parseInt(strippedLine.substring(j+1, stringLength));
				}
				// x-coordinate of point. starting off at zero until our delimiting comma
				else {
					points[counter][k] = Integer.parseInt(strippedLine.substring(k, j));
				}
			}
			counter ++;				
		}
		// close scanner and return 2D array of X and Y coordinates 
		sc.close();
		return points;
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		// string used to call the case file
		String CASE = "case6";		
		
		// creating 2D array to hold points from case file
		int[][] points = null;
		// call method to populate array from text file
		points = readFile(CASE);
		
		/*
		 * Used to check that points array contains the correctly captured the points from the file
		 */
		for (int i = 0; i < points.length; i++) {			
			System.out.println("X: " + points[i][0] + "\t Y: " + points[i][1]);			
		}
		
		// creating 2D array to hold the lowest point
		int[][] lowestPoint = new int[1][1];
		
		// method call to find the lowest point
		lowestPoint = findLowest(points);
		
		/*
		 * Used to check that we've accurately identified the lowest point
		 */
		System.out.println("Lowest Point: ");
		System.out.println("X: " + lowestPoint[0][0] + "\t Y: " + lowestPoint[0][1]);	
		
		
		int[][] sortedPoints = new int[points.length][2];
		
		sortedPoints = sortPoints(points, lowestPoint);
		
		int uniqueAnglePoints = findNumberOfUniqueAnglePoints(sortedPoints, lowestPoint);
		
		checkNecessaryNumberOfPoints(uniqueAnglePoints);
		
		int[][] uniqueAngleArray = new int[uniqueAnglePoints][2];
		
		uniqueAngleArray = identifyUniqueAnglePoints(sortedPoints, lowestPoint, uniqueAnglePoints);
		
		Stack<Point> convexHullStack = new Stack<Point>();
		
		convexHullStack = computeConvexHull(uniqueAngleArray);
		
		printConvexHull(convexHullStack);
		System.exit(0);

	}

	







	private static void printConvexHull(Stack<Point> convexHullStack) {
		// print the points making up the convex hull		
		
		System.out.println("The points making up the Convex Hull: ");
		for (int i = 0; i < convexHullStack.size(); i++) {
			System.out.println(convexHullStack.elementAt(i).x() + ", " + convexHullStack.elementAt(i).y());
		}
		
	}









	private static Stack<Point> computeConvexHull(int[][] uniqueAngleArray) {
		// TODO Auto-generated method stub
		Stack<Point> stack = new Stack<Point>();
		
		Point[] pointArray = new Point[uniqueAngleArray.length];
		
		for (int i = 0; i < pointArray.length; i++) {
			pointArray[i] = new Point(uniqueAngleArray[i][0], uniqueAngleArray[i][1]);
		}
		
		stack.push(pointArray[0]);
		stack.push(pointArray[1]);
		stack.push(pointArray[2]);
		
		if (pointArray.length == 3) {
			return stack;
		}
		else {
			for (int i = 3; i < pointArray.length; i++) {
				while (orientation(nextToTop(stack), stack.peek(), pointArray[i]) < 1) {
					stack.pop();
				}
				stack.push(pointArray[i]);
			}
		}
		
		return stack;
	}

	private static int orientation(Point nextToTop, Point top, Point point) {
		// checking for left or right turns
		//int turnValue = ((point.x()-nextToTop.x())*(top.y()-nextToTop.y()))-((top.x()-nextToTop.x())*(point.y()-nextToTop.y()));
		int turnValue = ((top.x()-nextToTop.x())*(point.y()-nextToTop.y()))-((point.x()-nextToTop.x())*(top.y()-nextToTop.y()));
		
		return turnValue;
	}









	public static Point nextToTop(Stack<Point> stack) {
		Point P = stack.peek();
		stack.pop();
		Point nextToTop = stack.peek();
		stack.push(P);
		return nextToTop;
	}







	private static void checkNecessaryNumberOfPoints(int uniqueAnglePoints) {
		// check if we have necessary number of points to compute Convex Hull
		
		if (uniqueAnglePoints < 3) {
			System.out.println("Does not meet necessary condition of 3 or more points to compute Convex Hull");
			System.exit(0);
		}
	}









	private static int[][] identifyUniqueAnglePoints(int[][] sortedPoints, int[][] lowestPoint, int uniqueAnglePoints) {
		// this method is intended to populate a new 2D array with points such that no two have identical polar angles
		if (uniqueAnglePoints == sortedPoints.length) {
			return sortedPoints;
		}
		
		
		
		int[][] returnArray = new int[uniqueAnglePoints][2];
		
		// front load the lowest point into the array
		returnArray[0][0] = lowestPoint[0][0];
		returnArray[0][1] = lowestPoint[0][1];
		
		
		int k = 1;
		
		for (int i = 1; i < returnArray.length-1; i++) {

			double xLeft = sortedPoints[k][0];			
			double yLeft = sortedPoints[k][1];
			
			double xRight = sortedPoints[k+1][0];
			double yRight = sortedPoints[k+1][1];
			
			double x1 = lowestPoint[0][0];
			double y1 = lowestPoint[0][1];
			
			double leftPolarAngle = -(xLeft - x1)/(yLeft - y1);
			double rightPolarAngle = -(xRight - x1)/(yRight - y1);
			
			
			if (leftPolarAngle == rightPolarAngle) {
				if (Math.sqrt(Math.pow(xLeft - x1, 2)+Math.pow(yLeft - y1, 2)) < Math.sqrt(Math.pow(xRight - x1, 2)+Math.pow(yRight - y1, 2))) {
					returnArray[i][0] = sortedPoints[k+1][0];
					returnArray[i][1] = sortedPoints[k+1][1];
					k=k+2;
				}
				else {
					returnArray[i][0] = sortedPoints[k][0];
					returnArray[i][1] = sortedPoints[k][1];
					k=k+2;
				}
			}
			else {
				returnArray[i][0] = sortedPoints[k][0];
				returnArray[i][1] = sortedPoints[k][1];
				returnArray[i+1][0] = sortedPoints[k+1][0];
				returnArray[i+1][1] = sortedPoints[k+1][1];
				k++;
			}
			


		}

		
		return returnArray;
	}









	private static int findNumberOfUniqueAnglePoints(int[][] sortedPoints, int[][] lowestPoint) {
		// This method is used to identify the number of points within the sorted points 2D array that have a unique polar angle
		int count = sortedPoints.length;
		for (int i = 1; i < sortedPoints.length-1; i++) {
			double xLeft = sortedPoints[i][0];			
			double yLeft = sortedPoints[i][1];
			
			double xRight = sortedPoints[i+1][0];
			double yRight = sortedPoints[i+1][1];
			
			double x1 = lowestPoint[0][0];
			double y1 = lowestPoint[0][1];
			
			double leftPolarAngle = -(xLeft - x1)/(yLeft - y1);
			double rightPolarAngle = -(xRight - x1)/(yRight - y1);
			
			if (leftPolarAngle == rightPolarAngle) {
				i++;
				count--;
			}
		}
		
		
		return count;
	}









	private static int[][] sortPoints(int[][] points, int[][] lowestPoint) {
		// call method to begin sorting process
		int[][] sortedPoints = sort(points, lowestPoint, 0, points.length-1);
		return sortedPoints;
	}









	private static int[][] sort(int[][] points, int[][] lowestPoint, int left, int right) {
		if (left < right) {
			// find the middle point of the array
			int middle = (left + right)/2;
			
			//sort the right and left subarrays
			sort(points, lowestPoint, left, middle);
			sort(points, lowestPoint, middle+1, right);
			
			// merge the sorted subarrays
			points = merge(points, lowestPoint, left, middle, right);
		}
		return points;
	}









	private static int[][] merge(int[][] points, int[][] lowestPoint, int left, int middle, int right) {
		// find sizes of the two subarrays to be merged
		int n1 = middle - left + 1;
		int n2 = right - middle;
		
		// create two temporary arrays
		int leftTempArray[][] = new int [n1][2];
		int rightTempArray[][] = new int [n2][2];
		
		// copy values to temp arrays
		for (int i = 0; i < n1; i++) {
			leftTempArray[i][0] = points[left+i][0];
			leftTempArray[i][1] = points[left+i][1];
		}
		for (int j = 0; j < n2; j++) {
			rightTempArray[j][0] = points[middle+1+j][0];
			rightTempArray[j][1] = points[middle+1+j][1];
		}
		
		// Lets merge the temporary arrays
		
		// create instance variables to serve as index references
		int i = 0;
		int j = 0;
		
		int k = left;
		while (i < n1 && j < n2) {
			
			
			if (leftTempArray[i][0]==lowestPoint[0][0] &&  leftTempArray[i][1]==lowestPoint[0][1]) {
				points[k][0] = leftTempArray[i][0];
				points[k][1] = leftTempArray[i][1];
				i++;
				k++;
				continue;
			}
			if (rightTempArray[j][0]==lowestPoint[0][0] &&  rightTempArray[j][1]==lowestPoint[0][1]) {
				points[k][0] = rightTempArray[j][0];
				points[k][1] = rightTempArray[j][1];
				j++;
				k++;
				continue;
			}
			
			double xLeft = leftTempArray[i][0];			
			double yLeft = leftTempArray[i][1];
			
			double xRight = rightTempArray[j][0];
			double yRight = rightTempArray[j][1];
			
			double x1 = lowestPoint[0][0];
			double y1 = lowestPoint[0][1];
			
			double leftPolarAngle = -(xLeft - x1)/(yLeft - y1);
			double rightPolarAngle = -(xRight - x1)/(yRight - y1);
			
			// - (x - x1) / (y - y1), where x1,y, are coordinates representing point p0
			if (leftPolarAngle < rightPolarAngle) {
				points[k][0] = leftTempArray[i][0];
				points[k][1] = leftTempArray[i][1];
				i++;
			}
			else if (leftPolarAngle == rightPolarAngle) {
				// if two points have the same polar angle, add them successively. will remove closer point when checking during colinear check
				points[k][0] = leftTempArray[i][0];
				points[k][1] = leftTempArray[i][1];
				points[k+1][0] = rightTempArray[j][0];
				points[k+1][1] = rightTempArray[j][1];
				i++;
				j++;
				k = k+2;
				continue;
			}
			else {
				points[k][0] = rightTempArray[j][0];
				points[k][1] = rightTempArray[j][1];
				j++;
			}
			k++;
		}
		
		// copy the remaining, if any, elements of LeftTempArray
		while (i < n1) {
			points[k][0] = leftTempArray[i][0];
			points[k][1] = leftTempArray[i][1];
			i++;
			k++;
		}
		
		// copy the remaining, if any, elements of RightTempArray
		while (j < n2) {
			points[k][0] = rightTempArray[j][0];
			points[k][1] = rightTempArray[j][1];
			j++;
			k++;
		}
		return points;
	}









	private static int[][] findLowest(int[][] points) {
		
		int[][] lowestPoint = {{points[0][0],points[0][1]}};		// Initially set the lowest point to the first point in the case file
		
		for (int i = 1; i < points.length; i++) {
			if (lowestPoint[0][1] > points[i][1]) {
				lowestPoint[0][1] = points[i][1]; 	// change the y-coordinate to the new lowest point
				lowestPoint[0][0] = points[i][0];	// change the x-coordinate to the new lowest point
			}
			else if (lowestPoint[0][1] == points[i][1]) {	// this is testing to two points with the same y-coordinate, will change to the smaller x-coordinate
				if (lowestPoint[0][0] > points[i][0]) {
					lowestPoint[0][0] = points[i][0];	// change the x-coordinate to the new lowest point
				}
			}
		}
		return lowestPoint;		
	}

}
