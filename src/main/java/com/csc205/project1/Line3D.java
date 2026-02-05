package com.csc205.project1;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a line segment in three-dimensional Cartesian space.
 * 
 * This class encapsulates a line segment defined by two endpoints in 3D space and provides
 * various geometric operations such as length calculation, distance between lines, intersection
 * detection, and parametric point calculations. The class is designed to be immutable to ensure
 * thread-safety and prevent unintended side effects.
 * 
 * Design Patterns and Principles:
 * 
 * 1. COMPOSITION PATTERN:
 *    - Line3D is composed of two Point3D objects rather than inheriting from Point3D
 *    - Demonstrates the "has-a" relationship (a line HAS two points)
 *    - This design is more flexible than inheritance and follows the principle
 *      "favor composition over inheritance"
 *    - Allows for better encapsulation and separation of concerns
 * 
 * 2. VALUE OBJECT PATTERN:
 *    - Similar to Point3D, this class implements the Value Object pattern
 *    - Objects are defined by their attributes (start and end points) rather than identity
 *    - Two Line3D objects with the same endpoints are considered equal
 *    - Immutability ensures thread-safety and prevents defensive copying
 * 
 * 3. FACTORY METHOD PATTERN:
 *    - Provides static factory methods (fromPoints, fromDirectionVector) for flexible construction
 *    - These methods offer more semantic clarity than constructors
 *    - Allows for validation and computation before object creation
 * 
 * 4. STRATEGY PATTERN (Implicit):
 *    - Different distance calculation methods (point-to-line, line-to-line) represent
 *      different strategies for computing distance
 *    - Each method encapsulates a specific algorithm for distance calculation
 * 
 * 5. ENCAPSULATION:
 *    - Private fields with public getters ensure data hiding
 *    - Internal state cannot be modified after construction
 *    - Computed properties (length, direction) are calculated on-demand
 * 
 * 6. SINGLE RESPONSIBILITY PRINCIPLE:
 *    - Each method has one well-defined purpose
 *    - Geometric calculations are separated into distinct methods
 *    - Logging is separated from business logic
 * 
 * Data Structures & Algorithms Foundation:
 * 
 * 1. COMPUTATIONAL GEOMETRY:
 *    - Line-line intersection demonstrates fundamental geometric algorithms
 *    - Closest point calculations use projection formulas from linear algebra
 *    - These algorithms are foundational for:
 *      * Computer graphics (ray tracing, collision detection)
 *      * Robotics (path planning, obstacle avoidance)
 *      * GIS systems (route finding, spatial analysis)
 * 
 * 2. PARAMETRIC REPRESENTATIONS:
 *    - Lines are represented parametrically: P(t) = P0 + t * (P1 - P0)
 *    - This representation is crucial for:
 *      * Animation and interpolation
 *      * Ray casting algorithms
 *      * Bézier curves and splines
 * 
 * 3. VECTOR ALGEBRA:
 *    - Direction vectors, projections, and cross products demonstrate vector operations
 *    - These operations are fundamental to:
 *      * 3D transformations
 *      * Physics simulations
 *      * Computer vision algorithms
 * 
 * 4. OPTIMIZATION ALGORITHMS:
 *    - Shortest distance calculation uses optimization to find minimum distance
 *    - Demonstrates gradient descent concepts and local minima
 *    - Foundation for more complex optimization problems
 * 
 * 5. NUMERICAL STABILITY:
 *    - Careful handling of edge cases (parallel lines, zero-length lines)
 *    - Use of epsilon for floating-point comparisons
 *    - Critical for robust geometric computations
 * 
 * @author Generated Example
 * @version 1.0
 */
public class Line3D {
    
    private static final Logger logger = Logger.getLogger(Line3D.class.getName());
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = 1e-10;
    
    private final Point3D start;
    private final Point3D end;
    
    /**
     * Constructs a new Line3D with the specified start and end points.
     * 
     * This is the primary constructor that creates a line segment between two points in 3D space.
     * The line is directed from the start point to the end point, which matters for certain
     * operations like parametric evaluation and direction calculations.
     * 
     * Example usage:
     * <pre>
     * Point3D p1 = new Point3D(0, 0, 0);
     * Point3D p2 = new Point3D(1, 1, 1);
     * Line3D line = new Line3D(p1, p2);
     * </pre>
     * 
     * @param start the starting point of the line segment
     * @param end the ending point of the line segment
     * @throws NullPointerException if either start or end is null
     * @throws IllegalArgumentException if start and end are the same point (degenerate line)
     */
    public Line3D(Point3D start, Point3D end) {
        if (start == null || end == null) {
            logger.log(Level.SEVERE, "Cannot create Line3D: start or end point is null");
            throw new NullPointerException("Start and end points cannot be null");
        }
        
        if (start.equals(end)) {
            logger.log(Level.SEVERE, "Cannot create Line3D: start and end points are identical at ({0}, {1}, {2})",
                       new Object[]{start.getX(), start.getY(), start.getZ()});
            throw new IllegalArgumentException("Start and end points must be different (degenerate line not allowed)");
        }
        
        this.start = start;
        this.end = end;
        
        logger.log(Level.INFO, "Created Line3D from ({0}, {1}, {2}) to ({3}, {4}, {5})",
                   new Object[]{start.getX(), start.getY(), start.getZ(), 
                               end.getX(), end.getY(), end.getZ()});
    }
    
    /**
     * Creates a Line3D from a starting point and a direction vector.
     * 
     * This factory method provides an alternative way to construct a line using a starting
     * point and a direction vector with a specified length. This is particularly useful in
     * ray tracing, physics simulations, and when working with velocity vectors.
     * 
     * The end point is calculated as: end = start + (direction * length)
     * 
     * Example usage:
     * <pre>
     * Point3D origin = new Point3D(0, 0, 0);
     * Point3D direction = new Point3D(1, 0, 0); // Unit vector along X-axis
     * Line3D line = Line3D.fromDirectionVector(origin, direction, 10.0);
     * </pre>
     * 
     * @param start the starting point of the line
     * @param direction a point representing the direction vector (will be normalized)
     * @param length the desired length of the line segment
     * @return a new Line3D instance
     * @throws NullPointerException if start or direction is null
     * @throws IllegalArgumentException if length is non-positive or direction is zero vector
     */
    public static Line3D fromDirectionVector(Point3D start, Point3D direction, double length) {
        if (start == null || direction == null) {
            logger.log(Level.SEVERE, "Cannot create Line3D from direction: start or direction is null");
            throw new NullPointerException("Start and direction cannot be null");
        }
        
        if (length <= 0) {
            logger.log(Level.SEVERE, "Cannot create Line3D from direction: invalid length {0}", length);
            throw new IllegalArgumentException("Length must be positive");
        }
        
        // Normalize the direction and scale by length
        Point3D normalizedDirection = direction.normalize();
        Point3D scaledDirection = normalizedDirection.scale(length, length, length);
        Point3D end = start.translate(scaledDirection.getX(), scaledDirection.getY(), scaledDirection.getZ());
        
        logger.log(Level.INFO, "Created Line3D from direction vector with start ({0}, {1}, {2}), direction ({3}, {4}, {5}), length {6}",
                   new Object[]{start.getX(), start.getY(), start.getZ(),
                               direction.getX(), direction.getY(), direction.getZ(), length});
        
        return new Line3D(start, end);
    }
    
    /**
     * Creates a Line3D from two points (alias for constructor for API consistency).
     * 
     * This factory method provides a more explicit and semantically clear way to create
     * a line from two points, matching the naming convention of other factory methods.
     * 
     * Example usage:
     * <pre>
     * Point3D p1 = new Point3D(0, 0, 0);
     * Point3D p2 = new Point3D(5, 5, 5);
     * Line3D line = Line3D.fromPoints(p1, p2);
     * </pre>
     * 
     * @param start the starting point
     * @param end the ending point
     * @return a new Line3D instance
     */
    public static Line3D fromPoints(Point3D start, Point3D end) {
        return new Line3D(start, end);
    }
    
    /**
     * Calculates the length of this line segment.
     * 
     * The length is the Euclidean distance between the start and end points. This is
     * equivalent to the magnitude of the direction vector. Line length is fundamental
     * for normalization, scaling, and many geometric calculations.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(3, 4, 0));
     * double length = line.length(); // Returns 5.0
     * </pre>
     * 
     * @return the length of the line segment
     */
    public double length() {
        double len = start.distanceTo(end);
        logger.log(Level.INFO, "Calculated length of line: {0}", len);
        return len;
    }
    
    /**
     * Returns the direction vector of this line.
     * 
     * The direction vector points from the start to the end of the line and has the same
     * magnitude as the line's length. This vector is essential for parametric equations,
     * ray casting, and determining line orientation.
     * 
     * The direction vector is calculated as: end - start
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 6, 8));
     * Point3D direction = line.getDirection(); // Returns (3, 4, 5)
     * </pre>
     * 
     * @return a Point3D representing the direction vector
     */
    public Point3D getDirection() {
        Point3D direction = end.translate(-start.getX(), -start.getY(), -start.getZ());
        logger.log(Level.INFO, "Calculated direction vector: ({0}, {1}, {2})",
                   new Object[]{direction.getX(), direction.getY(), direction.getZ()});
        return direction;
    }
    
    /**
     * Returns a normalized direction vector (unit vector) of this line.
     * 
     * The normalized direction has a magnitude of 1.0 and points in the same direction
     * as the line. Unit direction vectors are crucial for lighting calculations, ray
     * tracing, and any algorithm that needs direction without magnitude.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(3, 4, 0));
     * Point3D unitDir = line.getNormalizedDirection(); // Returns (0.6, 0.8, 0)
     * </pre>
     * 
     * @return a Point3D representing the normalized direction vector
     */
    public Point3D getNormalizedDirection() {
        Point3D normalized = getDirection().normalize();
        logger.log(Level.INFO, "Calculated normalized direction: ({0}, {1}, {2})",
                   new Object[]{normalized.getX(), normalized.getY(), normalized.getZ()});
        return normalized;
    }
    
    /**
     * Returns the midpoint of this line segment.
     * 
     * The midpoint is the point exactly halfway between the start and end points.
     * This is useful for subdividing lines, binary space partitioning, and finding
     * the center of line segments.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(4, 6, 8));
     * Point3D mid = line.getMidpoint(); // Returns (2, 3, 4)
     * </pre>
     * 
     * @return a Point3D representing the midpoint
     */
    public Point3D getMidpoint() {
        Point3D midpoint = start.midpoint(end);
        logger.log(Level.INFO, "Calculated midpoint: ({0}, {1}, {2})",
                   new Object[]{midpoint.getX(), midpoint.getY(), midpoint.getZ()});
        return midpoint;
    }
    
    /**
     * Calculates a point on the line at parameter t.
     * 
     * This method evaluates the parametric equation of the line:
     * P(t) = start + t * (end - start)
     * 
     * When t = 0, returns the start point
     * When t = 1, returns the end point
     * When 0 < t < 1, returns a point on the line segment
     * When t < 0 or t > 1, returns a point on the infinite line extension
     * 
     * This parametric representation is fundamental for:
     * - Interpolation and animation
     * - Ray tracing and ray casting
     * - Collision detection
     * - Bézier curves and splines
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
     * Point3D point = line.getPointAtParameter(0.5); // Returns (5, 0, 0)
     * </pre>
     * 
     * @param t the parameter value (0 = start, 1 = end)
     * @return the point at parameter t on the line
     */
    public Point3D getPointAtParameter(double t) {
        if (t < 0 || t > 1) {
            logger.log(Level.WARNING, "Parameter t={0} is outside [0,1] range, point will be outside line segment", t);
        }
        
        Point3D direction = getDirection();
        Point3D scaled = direction.scale(t, t, t);
        Point3D point = start.translate(scaled.getX(), scaled.getY(), scaled.getZ());
        
        logger.log(Level.INFO, "Calculated point at parameter t={0}: ({1}, {2}, {3})",
                   new Object[]{t, point.getX(), point.getY(), point.getZ()});
        
        return point;
    }
    
    /**
     * Calculates the shortest distance from a point to this line segment.
     * 
     * This method finds the perpendicular distance from a point to the line segment.
     * It handles three cases:
     * 1. The closest point is on the line segment (perpendicular projection falls within segment)
     * 2. The closest point is the start point (projection falls before segment)
     * 3. The closest point is the end point (projection falls after segment)
     * 
     * This algorithm is fundamental for:
     * - Collision detection (distance to obstacles)
     * - Proximity queries in spatial databases
     * - Path planning and navigation
     * - Computer graphics (selection and picking)
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
     * Point3D point = new Point3D(5, 3, 0);
     * double distance = line.distanceToPoint(point); // Returns 3.0
     * </pre>
     * 
     * @param point the point to calculate distance to
     * @return the shortest distance from the point to the line segment
     * @throws NullPointerException if point is null
     */
    public double distanceToPoint(Point3D point) {
        if (point == null) {
            logger.log(Level.SEVERE, "Cannot calculate distance to point: point is null");
            throw new NullPointerException("Point cannot be null");
        }
        
        Point3D direction = getDirection();
        Point3D toPoint = point.translate(-start.getX(), -start.getY(), -start.getZ());
        
        // Calculate the parameter t where the perpendicular from point meets the line
        double directionDotDirection = direction.dotProduct(direction);
        double t = toPoint.dotProduct(direction) / directionDotDirection;
        
        // Clamp t to [0, 1] to stay on the line segment
        t = Math.max(0.0, Math.min(1.0, t));
        
        // Find the closest point on the line segment
        Point3D closestPoint = getPointAtParameter(t);
        double distance = point.distanceTo(closestPoint);
        
        logger.log(Level.INFO, "Calculated distance from point ({0}, {1}, {2}) to line: {3} (closest point at t={4})",
                   new Object[]{point.getX(), point.getY(), point.getZ(), distance, t});
        
        return distance;
    }
    
    /**
     * Finds the closest point on this line segment to a given point.
     * 
     * This method projects the given point onto the line and returns the closest point
     * on the line segment. If the projection falls outside the segment, it returns the
     * appropriate endpoint.
     * 
     * This is useful for:
     * - Snapping points to lines in CAD software
     * - Finding nearest locations on paths
     * - Collision response calculations
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
     * Point3D point = new Point3D(5, 5, 0);
     * Point3D closest = line.closestPointTo(point); // Returns (5, 0, 0)
     * </pre>
     * 
     * @param point the point to find the closest point to
     * @return the closest point on this line segment
     * @throws NullPointerException if point is null
     */
    public Point3D closestPointTo(Point3D point) {
        if (point == null) {
            logger.log(Level.SEVERE, "Cannot find closest point: point is null");
            throw new NullPointerException("Point cannot be null");
        }
        
        Point3D direction = getDirection();
        Point3D toPoint = point.translate(-start.getX(), -start.getY(), -start.getZ());
        
        double directionDotDirection = direction.dotProduct(direction);
        double t = toPoint.dotProduct(direction) / directionDotDirection;
        
        // Clamp t to [0, 1]
        t = Math.max(0.0, Math.min(1.0, t));
        
        Point3D closestPoint = getPointAtParameter(t);
        
        logger.log(Level.INFO, "Found closest point ({0}, {1}, {2}) on line to point ({3}, {4}, {5})",
                   new Object[]{closestPoint.getX(), closestPoint.getY(), closestPoint.getZ(),
                               point.getX(), point.getY(), point.getZ()});
        
        return closestPoint;
    }
    
    /**
     * Calculates the shortest distance between this line segment and another line segment.
     * 
     * This method implements a sophisticated algorithm to find the minimum distance between
     * two line segments in 3D space. It handles several cases:
     * 
     * 1. PARALLEL LINES: If lines are parallel, finds distance from an endpoint of one line
     *    to the other line
     * 2. SKEW LINES: If lines are non-parallel and non-intersecting, finds the shortest
     *    connecting segment using the parametric approach
     * 3. SEGMENT BOUNDARIES: Checks if the closest point calculation falls outside either
     *    segment and adjusts accordingly
     * 
     * The algorithm uses the following approach:
     * - Represents lines parametrically: L1(s) = P1 + s*D1, L2(t) = P2 + t*D2
     * - Solves for parameters s and t that minimize distance
     * - Clamps parameters to [0,1] to respect segment boundaries
     * - Computes final distance between closest points
     * 
     * This is critical for:
     * - Collision detection in robotics and gaming
     * - Clearance calculations in CAD/CAM
     * - Proximity analysis in GIS systems
     * - Path planning and obstacle avoidance
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
     * Line3D line2 = new Line3D(new Point3D(5, 5, 5), new Point3D(5, 5, 15));
     * double distance = line1.shortestDistanceTo(line2); // Returns 5.0
     * </pre>
     * 
     * @param other the other line segment
     * @return the shortest distance between the two line segments
     * @throws NullPointerException if other is null
     */
    public double shortestDistanceTo(Line3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot calculate shortest distance: other line is null");
            throw new NullPointerException("Other line cannot be null");
        }
        
        Point3D d1 = this.getDirection();
        Point3D d2 = other.getDirection();
        Point3D w = this.start.translate(-other.start.getX(), -other.start.getY(), -other.start.getZ());
        
        double a = d1.dotProduct(d1); // Always >= 0
        double b = d1.dotProduct(d2);
        double c = d2.dotProduct(d2); // Always >= 0
        double d = d1.dotProduct(w);
        double e = d2.dotProduct(w);
        
        double denominator = a * c - b * b; // Always >= 0
        
        double s, t;
        
        // Check if lines are parallel
        if (denominator < EPSILON) {
            logger.log(Level.INFO, "Lines are parallel, using endpoint distance calculation");
            s = 0.0;
            t = (b > c) ? (d / b) : (e / c); // Avoid division by zero
            t = Math.max(0.0, Math.min(1.0, t));
        } else {
            // Lines are not parallel, find the closest points
            s = (b * e - c * d) / denominator;
            t = (a * e - b * d) / denominator;
            
            // Clamp s to [0, 1]
            s = Math.max(0.0, Math.min(1.0, s));
            // Clamp t to [0, 1]
            t = Math.max(0.0, Math.min(1.0, t));
            
            // If s was clamped, recompute t
            if (s <= EPSILON || s >= 1.0 - EPSILON) {
                t = Math.max(0.0, Math.min(1.0, (b * s + e) / c));
            }
            // If t was clamped, recompute s
            if (t <= EPSILON || t >= 1.0 - EPSILON) {
                s = Math.max(0.0, Math.min(1.0, (b * t - d) / a));
            }
        }
        
        Point3D point1 = this.getPointAtParameter(s);
        Point3D point2 = other.getPointAtParameter(t);
        
        double distance = point1.distanceTo(point2);
        
        logger.log(Level.INFO, "Calculated shortest distance between lines: {0} (at parameters s={1}, t={2})",
                   new Object[]{distance, s, t});
        
        return distance;
    }
    
    /**
     * Determines if this line segment is parallel to another line segment.
     * 
     * Two lines are parallel if their direction vectors are parallel (or anti-parallel).
     * This is determined by checking if the cross product of the direction vectors is
     * approximately zero.
     * 
     * Mathematically: lines are parallel if ||d1 × d2|| < ε
     * 
     * This check is important for:
     * - Determining if lines can intersect
     * - Optimizing distance calculations
     * - Geometric constraint solving
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(1, 0, 0));
     * Line3D line2 = new Line3D(new Point3D(0, 1, 0), new Point3D(2, 1, 0));
     * boolean parallel = line1.isParallelTo(line2); // Returns true
     * </pre>
     * 
     * @param other the other line segment
     * @return true if the lines are parallel, false otherwise
     * @throws NullPointerException if other is null
     */
    public boolean isParallelTo(Line3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot check parallelism: other line is null");
            throw new NullPointerException("Other line cannot be null");
        }
        
        Point3D d1 = this.getNormalizedDirection();
        Point3D d2 = other.getNormalizedDirection();
        Point3D cross = d1.crossProduct(d2);
        
        double crossMagnitude = cross.magnitude();
        boolean isParallel = crossMagnitude < EPSILON;
        
        logger.log(Level.INFO, "Lines are {0} (cross product magnitude: {1})",
                   new Object[]{isParallel ? "parallel" : "not parallel", crossMagnitude});
        
        return isParallel;
    }
    
    /**
     * Determines if this line segment is perpendicular to another line segment.
     * 
     * Two lines are perpendicular if their direction vectors are orthogonal, which means
     * their dot product is zero (or very close to zero considering floating-point precision).
     * 
     * Mathematically: lines are perpendicular if |d1 · d2| < ε
     * 
     * This is useful for:
     * - Geometric constraint validation
     * - Orthogonal grid construction
     * - Right angle detection
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(1, 0, 0));
     * Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 1, 0));
     * boolean perpendicular = line1.isPerpendicularTo(line2); // Returns true
     * </pre>
     * 
     * @param other the other line segment
     * @return true if the lines are perpendicular, false otherwise
     * @throws NullPointerException if other is null
     */
    public boolean isPerpendicularTo(Line3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot check perpendicularity: other line is null");
            throw new NullPointerException("Other line cannot be null");
        }
        
        Point3D d1 = this.getNormalizedDirection();
        Point3D d2 = other.getNormalizedDirection();
        double dot = Math.abs(d1.dotProduct(d2));
        
        boolean isPerpendicular = dot < EPSILON;
        
        logger.log(Level.INFO, "Lines are {0} (dot product: {1})",
                   new Object[]{isPerpendicular ? "perpendicular" : "not perpendicular", dot});
        
        return isPerpendicular;
    }
    
    /**
     * Checks if a point lies on this line segment.
     * 
     * A point is considered to be on the line segment if:
     * 1. The distance from the point to the line is approximately zero (within EPSILON)
     * 2. The point lies between the start and end points (not on the infinite extension)
     * 
     * This method is useful for:
     * - Validating geometric constraints
     * - Checking if points are collinear
     * - Testing path containment
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
     * Point3D point = new Point3D(5, 0, 0);
     * boolean onLine = line.containsPoint(point); // Returns true
     * </pre>
     * 
     * @param point the point to check
     * @return true if the point lies on the line segment, false otherwise
     * @throws NullPointerException if point is null
     */
    public boolean containsPoint(Point3D point) {
        if (point == null) {
            logger.log(Level.SEVERE, "Cannot check point containment: point is null");
            throw new NullPointerException("Point cannot be null");
        }
        
        double distance = distanceToPoint(point);
        boolean contains = distance < EPSILON;
        
        logger.log(Level.INFO, "Point ({0}, {1}, {2}) is {3} the line (distance: {4})",
                   new Object[]{point.getX(), point.getY(), point.getZ(),
                               contains ? "on" : "not on", distance});
        
        return contains;
    }
    
    /**
     * Calculates the angle between this line and another line in radians.
     * 
     * The angle between two lines is determined by the angle between their direction vectors.
     * This method always returns the acute angle (0 to π/2) regardless of line orientation.
     * 
     * Formula: θ = arccos(|d1 · d2| / (|d1| * |d2|))
     * 
     * This is useful for:
     * - Determining the angle at intersections
     * - Geometric analysis and validation
     * - Angular constraint checking
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(1, 0, 0));
     * Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 1, 0));
     * double angle = line1.angleTo(line2); // Returns π/2 (90 degrees)
     * </pre>
     * 
     * @param other the other line
     * @return the angle between the lines in radians (0 to π/2)
     * @throws NullPointerException if other is null
     */
    public double angleTo(Line3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot calculate angle: other line is null");
            throw new NullPointerException("Other line cannot be null");
        }
        
        Point3D d1 = this.getNormalizedDirection();
        Point3D d2 = other.getNormalizedDirection();
        
        double dot = Math.abs(d1.dotProduct(d2)); // Use absolute value for acute angle
        // Clamp to [-1, 1] to handle floating-point errors
        dot = Math.max(-1.0, Math.min(1.0, dot));
        
        double angle = Math.acos(dot);
        
        logger.log(Level.INFO, "Calculated angle between lines: {0} radians ({1} degrees)",
                   new Object[]{angle, Math.toDegrees(angle)});
        
        return angle;
    }
    
    /**
     * Translates the line by the specified offset.
     * 
     * Moves both endpoints of the line by the same offset vector, effectively translating
     * the entire line segment in 3D space without changing its length or direction.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(5, 0, 0));
     * Line3D moved = line.translate(10, 5, 2); // Moves to [(10,5,2), (15,5,2)]
     * </pre>
     * 
     * @param dx the offset in the x direction
     * @param dy the offset in the y direction
     * @param dz the offset in the z direction
     * @return a new Line3D representing the translated line
     */
    public Line3D translate(double dx, double dy, double dz) {
        Point3D newStart = start.translate(dx, dy, dz);
        Point3D newEnd = end.translate(dx, dy, dz);
        
        logger.log(Level.INFO, "Translated line by offset ({0}, {1}, {2})",
                   new Object[]{dx, dy, dz});
        
        return new Line3D(newStart, newEnd);
    }
    
    /**
     * Scales the line from its start point by the specified factor.
     * 
     * Multiplies the length of the line by the scale factor while keeping the start point
     * fixed. The direction remains the same, but the end point moves along the line.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(5, 0, 0));
     * Line3D scaled = line.scale(2.0); // Creates line from (0,0,0) to (10,0,0)
     * </pre>
     * 
     * @param factor the scale factor
     * @return a new Line3D representing the scaled line
     * @throws IllegalArgumentException if factor is zero or negative
     */
    public Line3D scale(double factor) {
        if (factor <= 0) {
            logger.log(Level.SEVERE, "Cannot scale line: factor must be positive (factor={0})", factor);
            throw new IllegalArgumentException("Scale factor must be positive");
        }
        
        Point3D direction = getDirection();
        Point3D scaledDirection = direction.scale(factor, factor, factor);
        Point3D newEnd = start.translate(scaledDirection.getX(), scaledDirection.getY(), scaledDirection.getZ());
        
        logger.log(Level.INFO, "Scaled line by factor {0}", factor);
        
        return new Line3D(start, newEnd);
    }
    
    /**
     * Reverses the direction of the line.
     * 
     * Swaps the start and end points, effectively reversing the line's direction while
     * maintaining the same geometric line segment.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(5, 5, 5));
     * Line3D reversed = line.reverse(); // Creates line from (5,5,5) to (0,0,0)
     * </pre>
     * 
     * @return a new Line3D with reversed direction
     */
    public Line3D reverse() {
        logger.log(Level.INFO, "Reversed line direction");
        return new Line3D(end, start);
    }
    
    // Getters
    
    /**
     * Returns the starting point of this line segment.
     * 
     * @return the start point
     */
    public Point3D getStart() {
        return start;
    }
    
    /**
     * Returns the ending point of this line segment.
     * 
     * @return the end point
     */
    public Point3D getEnd() {
        return end;
    }
    
    /**
     * Checks if this line equals another object.
     * 
     * Two lines are considered equal if both their start and end points are equal.
     * This implementation properly handles the equals contract and uses Point3D's
     * equals method which accounts for floating-point precision.
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Line3D other = (Line3D) obj;
        boolean isEqual = this.start.equals(other.start) && this.end.equals(other.end);
        
        if (isEqual) {
            logger.log(Level.INFO, "Lines are equal");
        }
        
        return isEqual;
    }
    
    /**
     * Returns a hash code for this line.
     * 
     * The hash code is computed based on the start and end points, ensuring that
     * equal lines have the same hash code as required by the hash code contract.
     * 
     * @return the hash code for this line
     */
    @Override
    public int hashCode() {
        return 31 * start.hashCode() + end.hashCode();
    }
    
    /**
     * Returns a string representation of this line.
     * 
     * Provides a human-readable representation showing the start and end points.
     * 
     * @return a string representation of this line
     */
    @Override
    public String toString() {
        return String.format("Line3D[start=%s, end=%s, length=%.2f]", 
                           start.toString(), end.toString(), length());
    }
}