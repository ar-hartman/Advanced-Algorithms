package week4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.Stack;

public abstract class GrahamsScan implements Comparable<Point>{
	
	public static void main(String[] args) throws IOException {
		// string used to call the case file
		String CASE = "case2";		
		
		Point[] uploadedPoints = readFile1(CASE);
		/*
		 * Used to check that points array contains the correctly captured the points from the file
		 */
		for (int i = 0; i < uploadedPoints.length; i++) {
			System.out.println("X: " + uploadedPoints[i].x() + "\t Y: " + uploadedPoints[i].y());
		}

		Point lowestPoint1 = null;
		
		lowestPoint1 = findLowestPoint(uploadedPoints);
		/*
		 * Used to check that we've accurately identified the lowest point
		 */
		System.out.println("Lowest point using User define data type: ");
		System.out.println("X: " + lowestPoint1.x() + "\t Y: " + lowestPoint1.y());
		
		Point[] sortedPoints1 = new Point[uploadedPoints.length];
		sortedPoints1 = sortPoints(uploadedPoints, lowestPoint1, 0, uploadedPoints.length-1);
		
		int uniqueAnglePoints1 = findNumberOfUniqueAnglePoints(sortedPoints1, lowestPoint1);

		checkNecessaryNumberOfPoints(uniqueAnglePoints1);		
		
		Point[] uniqueAngleArray1 = new Point[uniqueAnglePoints1];

		uniqueAngleArray1 = identifyUniqueAnglePoints(sortedPoints1, lowestPoint1, uniqueAnglePoints1);

		Stack<Point> convexHullStack1 = new Stack<Point>();
		convexHullStack1 = computeConvexHull(uniqueAngleArray1);

		System.out.println("Convest hull with User defined data type: ");
		printConvexHull(convexHullStack1);

		System.exit(0);
	}

	private static Stack<Point> computeConvexHull(Point[] pointArray) {
		Stack<Point> stack = new Stack<Point>();
		
		stack.push(pointArray[0]);
		stack.push(pointArray[1]);
		stack.push(pointArray[2]);
		
		if (pointArray.length == 3) {
			return stack;
		}
		else {
			for (int i = 3; i < pointArray.length; i++) {
				while (checkTurn(nextToTop(stack), stack.peek(), pointArray[i]) < 1) {
					stack.pop();
				}
				stack.push(pointArray[i]);
			}
		}		
		return stack;
	}




	private static Point[] identifyUniqueAnglePoints(Point[] sortedPoints, Point lowestPoint, int uniqueAnglePoints) {
		// this method is intended to populate a new 2D array with points such that no two have identical polar angles
		if (uniqueAnglePoints == sortedPoints.length) {
			return sortedPoints;
		}
		Point[] returnArray = new Point[uniqueAnglePoints];
			
		// front load the lowest point into the array
		returnArray[0] = lowestPoint;
								
		int k = 1;
				
		for (int i = 1; i < returnArray.length-1; i++) {

			double xLeft = sortedPoints[k].x();			
			double yLeft = sortedPoints[k].y();
					
			double xRight = sortedPoints[k+1].x();
			double yRight = sortedPoints[k+1].y();
					
			double x1 = lowestPoint.x();
			double y1 = lowestPoint.y();
					
			double leftPolarAngle = -(xLeft - x1)/(yLeft - y1);
			double rightPolarAngle = -(xRight - x1)/(yRight - y1);
							
			if (leftPolarAngle == rightPolarAngle) {
				if (Math.sqrt(Math.pow(xLeft - x1, 2)+Math.pow(yLeft - y1, 2)) < Math.sqrt(Math.pow(xRight - x1, 2)+Math.pow(yRight - y1, 2))) {
					returnArray[i] = sortedPoints[k+1];
					k=k+2;
				}
				else {
					returnArray[i] = sortedPoints[k];
					k=k+2;
				}
			}
			else {
				returnArray[i] = sortedPoints[k];
				returnArray[i+1] = sortedPoints[k+1];
				k++;
			}
		}			
		return returnArray;
	}

	private static int findNumberOfUniqueAnglePoints(Point[] sortedPoints, Point lowestPoint) {
		// This method is used to identify the number of points within the sorted points 2D array that have a unique polar angle
		int count = sortedPoints.length;
		for (int i = 1; i < sortedPoints.length-1; i++) {
			double xLeft = sortedPoints[i].x();			
			double yLeft = sortedPoints[i].y();
			
			double xRight = sortedPoints[i+1].x();
			double yRight = sortedPoints[i+1].y();
			
			double x1 = lowestPoint.x();
			double y1 = lowestPoint.y();
			
			double leftPolarAngle = -(xLeft - x1)/(yLeft - y1);
			double rightPolarAngle = -(xRight - x1)/(yRight - y1);
			
			if (leftPolarAngle == rightPolarAngle) {
				i++;
				count--;
			}
		}		
	return count;
	}

	private static Point[] sortPoints(Point[] uploadedPoints, Point lowestPoint, int left, int right) {
		if (left < right) {
			// find the middle point of the array
			int middle = (left + right)/2;
			
			//sort the right and left subarrays
			sortPoints(uploadedPoints, lowestPoint, left, middle);
			sortPoints(uploadedPoints, lowestPoint, middle+1, right);
			
			// merge the sorted subarrays
			uploadedPoints = merge(uploadedPoints, lowestPoint, left, middle, right);
		}
		return uploadedPoints;
	}

	private static Point[] merge(Point[] points, Point lowestPoint, int left, int middle, int right) {
		// find sizes of the two subarrays to be merged
		int n1 = middle - left + 1;
		int n2 = right - middle;
		
		// create two temporary arrays	
		Point[] leftTempArray = new Point[n1];
		Point[] rightTempArray = new Point[n2];
		
		// copy values to temp arrays
		for (int i = 0; i < n1; i++) {
			leftTempArray[i] = points[left+i];
		}
		for (int j = 0; j < n2; j++) {
			rightTempArray[j] = points[middle+1+j];
		}
		
		/*
		 *  Lets merge the temporary arrays		
		 */
		// create instance variables to serve as index references
		int i = 0;
		int j = 0;
		
		int k = left;
		while (i < n1 && j < n2) {					
			if (leftTempArray[i].x()==lowestPoint.x() &&  leftTempArray[i].y()==lowestPoint.y()) {
				points[k] = leftTempArray[i];
				i++;
				k++;
				continue;
			}
			if (rightTempArray[j].x()==lowestPoint.x() &&  rightTempArray[j].y()==lowestPoint.y()) {
				points[k] = rightTempArray[j];
				j++;
				k++;
				continue;
			}			
			double xLeft = leftTempArray[i].x();			
			double yLeft = leftTempArray[i].y();
			
			double xRight = rightTempArray[j].x();
			double yRight = rightTempArray[j].y();
			
			double x1 = lowestPoint.x();
			double y1 = lowestPoint.y();
			
			double leftPolarAngle = -(xLeft - x1)/(yLeft - y1);
			double rightPolarAngle = -(xRight - x1)/(yRight - y1);
			
			// - (x - x1) / (y - y1), where x1,y, are coordinates representing point p0
			if (leftPolarAngle < rightPolarAngle) {
				points[k] = leftTempArray[i];
				i++;
			}
			else if (leftPolarAngle == rightPolarAngle) {
				// if two points have the same polar angle, add them successively. will remove closer point when checking during colinear check
				points[k] = leftTempArray[i];
				points[k+1] = rightTempArray[j];
				i++;
				j++;
				k = k+2;
				continue;
			}
			else {
				points[k] = rightTempArray[j];
				j++;
			}
			k++;
		}
		
		// copy the remaining, if any, elements of LeftTempArray
		while (i < n1) {
			points[k] = leftTempArray[i];
			i++;
			k++;
		}
		
		// copy the remaining, if any, elements of RightTempArray
		while (j < n2) {
			points[k] = rightTempArray[j];
			j++;
			k++;
		}
		return points;
	}

	private static Point findLowestPoint(Point[] uploadedPoints) {
		Point lowestPoint = new Point(uploadedPoints[0].x(), uploadedPoints[0].y());		// Initially set the lowest point to the first point in the case file
		
		for (int i = 1; i < uploadedPoints.length; i++) {
			if (lowestPoint.y() > uploadedPoints[i].y()) {
				lowestPoint = uploadedPoints[i]; 	// change  to the new lowest point
				
			}
			else if (lowestPoint.y() == uploadedPoints[i].y()) {	// this is testing to two points with the same y-coordinate, will change to the smaller x-coordinate
				if (lowestPoint.x() > uploadedPoints[i].x()) {
					lowestPoint = uploadedPoints[i];	// change the x-coordinate to the new lowest point
				}
			}
		}
		return lowestPoint;	
	}

	@SuppressWarnings("resource")
	private static Point[] readFile1(String CASE) throws FileNotFoundException {
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
		
		Point[] pointArray = new Point[counter];
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
			for (int k = 0; k < 1; k++) {
				//count string char location of delimiting comma
				while (strippedLine.charAt(j) != 44) {
					j++;
				}
				pointArray[counter] = new Point(Double.parseDouble(strippedLine.substring(k, j)), Double.parseDouble(strippedLine.substring(j+1, stringLength)));
			}
			counter ++;				
		}
		// close scanner and return 2D array of X and Y coordinates 
		sc.close();
		return pointArray;
	}




	private static void printConvexHull(Stack<Point> convexHullStack) {
		// print the points making up the convex hull		
		
		System.out.println("The points making up the Convex Hull: ");
		for (int i = 0; i < convexHullStack.size(); i++) {
			System.out.println(convexHullStack.elementAt(i).x() + ", " + convexHullStack.elementAt(i).y());
		}
		
	}

	private static int checkTurn(Point nextToTop, Point top, Point point) {
		// checking for left or right turns
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

}
