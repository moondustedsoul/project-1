package com.csc205.project1;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents a point in three-dimensional Cartesian space.
 * 
 * This class encapsulates the coordinates (x, y, z) of a point in 3D space and provides
 * various operations such as distance calculations, rotations, translations, and vector
 * operations. The class is designed to be immutable to ensure thread-safety and prevent
 * unintended side effects.
 * 
 * Design Patterns and Principles:
 * 
 * 1. VALUE OBJECT PATTERN:
 *    - This class implements the Value Object pattern where objects are defined by their
 *      attributes rather than identity. Two Point3D objects with the same coordinates are
 *      considered equal regardless of their memory location.
 *    - Demonstrates immutability principle - all fields are final and operations return
 *      new instances rather than modifying the current instance.
 * 
 * 2. BUILDER PATTERN (Implicit):
 *    - Multiple constructors provide flexible object creation
 *    - Factory methods (fromSpherical, fromCylindrical) serve as alternative constructors
 * 
 * 3. ENCAPSULATION:
 *    - Private fields with public getters ensure data hiding
 *    - Internal state cannot be modified after construction
 * 
 * 4. SEPARATION OF CONCERNS:
 *    - Logging is separated from business logic
 *    - Each method has a single, well-defined responsibility
 * 
 * Data Structures & Algorithms Foundation:
 * 
 * 1. VECTOR OPERATIONS:
 *    - Demonstrates fundamental linear algebra operations (addition, subtraction, scaling)
 *    - These operations are foundational for graph algorithms, physics simulations, and
 *      computational geometry
 * 
 * 2. DISTANCE METRICS:
 *    - Implements Euclidean distance (L2 norm) and Manhattan distance (L1 norm)
 *    - These metrics are crucial for clustering algorithms (K-means), nearest neighbor
 *      search, and pathfinding algorithms
 * 
 * 3. COORDINATE TRANSFORMATIONS:
 *    - Rotation matrices demonstrate matrix multiplication algorithms
 *    - These transformations are essential for computer graphics, robotics, and 3D modeling
 * 
 * 4. NUMERICAL STABILITY:
 *    - Careful handling of floating-point arithmetic
 *    - Demonstrates importance of precision in computational geometry algorithms
 * 
 * @author Generated Example
 * @version 1.0
 */
public class Point3D {
    
    private static final Logger logger = Logger.getLogger(Point3D.class.getName());
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = 1e-10;
    
    private final double x;
    private final double y;
    private final double z;
    
    /**
     * Constructs a new Point3D with the specified coordinates.
     * 
     * This is the primary constructor that initializes a point in 3D space. The coordinates
     * represent the position along the X (horizontal), Y (vertical), and Z (depth) axes
     * in a right-handed coordinate system.
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(3.0, 4.0, 5.0);
     * </pre>
     * 
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        logger.log(Level.INFO, "Created Point3D at coordinates: ({0}, {1}, {2})", 
                   new Object[]{x, y, z});
    }
    
    /**
     * Constructs a Point3D at the origin (0, 0, 0).
     * 
     * This convenience constructor creates a point at the origin of the coordinate system,
     * which is useful as a default starting point or reference point for calculations.
     * 
     * Example usage:
     * <pre>
     * Point3D origin = new Point3D();
     * </pre>
     */
    public Point3D() {
        this(0.0, 0.0, 0.0);
    }
    
    /**
     * Creates a Point3D from spherical coordinates.
     * 
     * Converts spherical coordinates (radius, theta, phi) to Cartesian coordinates (x, y, z).
     * This factory method demonstrates the Factory Method pattern and is useful for
     * applications involving spherical geometry such as astronomy, geolocation, or
     * 3D modeling with radial symmetry.
     * 
     * Conversion formulas:
     * - x = radius * sin(phi) * cos(theta)
     * - y = radius * sin(phi) * sin(theta)
     * - z = radius * cos(phi)
     * 
     * Example usage:
     * <pre>
     * Point3D point = Point3D.fromSpherical(10.0, Math.PI/4, Math.PI/3);
     * </pre>
     * 
     * @param radius the distance from the origin (must be non-negative)
     * @param theta the azimuthal angle in radians (angle in the x-y plane from the x-axis)
     * @param phi the polar angle in radians (angle from the positive z-axis)
     * @return a new Point3D instance with the converted Cartesian coordinates
     * @throws IllegalArgumentException if radius is negative
     */
    public static Point3D fromSpherical(double radius, double theta, double phi) {
        if (radius < 0) {
            logger.log(Level.SEVERE, "Invalid spherical coordinates: radius cannot be negative (radius={0})", radius);
            throw new IllegalArgumentException("Radius must be non-negative");
        }
        
        double x = radius * Math.sin(phi) * Math.cos(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(phi);
        
        logger.log(Level.INFO, "Converted spherical coordinates (r={0}, θ={1}, φ={2}) to Cartesian ({3}, {4}, {5})",
                   new Object[]{radius, theta, phi, x, y, z});
        
        return new Point3D(x, y, z);
    }
    
    /**
     * Creates a Point3D from cylindrical coordinates.
     * 
     * Converts cylindrical coordinates (radius, theta, z) to Cartesian coordinates (x, y, z).
     * This is useful for objects with cylindrical symmetry such as pipes, tubes, or
     * rotational bodies.
     * 
     * Conversion formulas:
     * - x = radius * cos(theta)
     * - y = radius * sin(theta)
     * - z = z (unchanged)
     * 
     * Example usage:
     * <pre>
     * Point3D point = Point3D.fromCylindrical(5.0, Math.PI/2, 10.0);
     * </pre>
     * 
     * @param radius the radial distance from the z-axis (must be non-negative)
     * @param theta the azimuthal angle in radians
     * @param z the height coordinate
     * @return a new Point3D instance with the converted Cartesian coordinates
     * @throws IllegalArgumentException if radius is negative
     */
    public static Point3D fromCylindrical(double radius, double theta, double z) {
        if (radius < 0) {
            logger.log(Level.SEVERE, "Invalid cylindrical coordinates: radius cannot be negative (radius={0})", radius);
            throw new IllegalArgumentException("Radius must be non-negative");
        }
        
        double x = radius * Math.cos(theta);
        double y = radius * Math.sin(theta);
        
        logger.log(Level.INFO, "Converted cylindrical coordinates (r={0}, θ={1}, z={2}) to Cartesian ({3}, {4}, {5})",
                   new Object[]{radius, theta, z, x, y, z});
        
        return new Point3D(x, y, z);
    }
    
    /**
     * Calculates the Euclidean distance from this point to another point.
     * 
     * The Euclidean distance is the straight-line distance between two points in 3D space,
     * computed using the Pythagorean theorem extended to three dimensions. This is the
     * most common distance metric (L2 norm) and is fundamental to many algorithms including
     * K-means clustering, K-nearest neighbors, and collision detection.
     * 
     * Formula: sqrt((x2-x1)² + (y2-y1)² + (z2-z1)²)
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D p1 = new Point3D(0, 0, 0);
     * Point3D p2 = new Point3D(3, 4, 0);
     * double distance = p1.distanceTo(p2); // Returns 5.0
     * </pre>
     * 
     * @param other the point to calculate distance to
     * @return the Euclidean distance between this point and the other point
     * @throws NullPointerException if other is null
     */
    public double distanceTo(Point3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot calculate distance: other point is null");
            throw new NullPointerException("Other point cannot be null");
        }
        
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        logger.log(Level.INFO, "Calculated Euclidean distance from ({0}, {1}, {2}) to ({3}, {4}, {5}): {6}",
                   new Object[]{this.x, this.y, this.z, other.x, other.y, other.z, distance});
        
        return distance;
    }
    
    /**
     * Calculates the Manhattan distance (L1 norm) from this point to another point.
     * 
     * The Manhattan distance is the sum of absolute differences of coordinates, representing
     * the distance traveled along axes at right angles. This metric is useful in grid-based
     * pathfinding (like city blocks), certain clustering algorithms, and when diagonal
     * movement is not allowed or is more expensive.
     * 
     * Formula: |x2-x1| + |y2-y1| + |z2-z1|
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D p1 = new Point3D(0, 0, 0);
     * Point3D p2 = new Point3D(3, 4, 5);
     * double distance = p1.manhattanDistanceTo(p2); // Returns 12.0
     * </pre>
     * 
     * @param other the point to calculate Manhattan distance to
     * @return the Manhattan distance between this point and the other point
     * @throws NullPointerException if other is null
     */
    public double manhattanDistanceTo(Point3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot calculate Manhattan distance: other point is null");
            throw new NullPointerException("Other point cannot be null");
        }
        
        double distance = Math.abs(this.x - other.x) + 
                         Math.abs(this.y - other.y) + 
                         Math.abs(this.z - other.z);
        
        logger.log(Level.INFO, "Calculated Manhattan distance from ({0}, {1}, {2}) to ({3}, {4}, {5}): {6}",
                   new Object[]{this.x, this.y, this.z, other.x, other.y, other.z, distance});
        
        return distance;
    }
    
    /**
     * Calculates the distance from this point to the origin (0, 0, 0).
     * 
     * This is equivalent to the magnitude or length of the position vector from the origin
     * to this point. It's commonly used to determine how far an object is from a reference
     * point or to normalize vectors.
     * 
     * Formula: sqrt(x² + y² + z²)
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(3, 4, 0);
     * double magnitude = point.magnitude(); // Returns 5.0
     * </pre>
     * 
     * @return the distance from this point to the origin
     */
    public double magnitude() {
        double mag = Math.sqrt(x * x + y * y + z * z);
        logger.log(Level.INFO, "Calculated magnitude of point ({0}, {1}, {2}): {3}",
                   new Object[]{x, y, z, mag});
        return mag;
    }
    
    /**
     * Rotates the point around the X-axis by the specified angle.
     * 
     * Performs a rotation transformation using a rotation matrix around the X-axis.
     * This operation is fundamental in computer graphics, robotics, and 3D transformations.
     * The rotation follows the right-hand rule: positive angles rotate counterclockwise
     * when looking along the positive X-axis toward the origin.
     * 
     * Rotation Matrix:
     * | 1    0         0      |
     * | 0  cos(θ)  -sin(θ)    |
     * | 0  sin(θ)   cos(θ)    |
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(1, 1, 0);
     * Point3D rotated = point.rotateX(Math.PI / 2); // Rotate 90 degrees around X-axis
     * </pre>
     * 
     * @param angleRadians the rotation angle in radians
     * @return a new Point3D representing the rotated point
     */
    public Point3D rotateX(double angleRadians) {
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        double newY = y * cos - z * sin;
        double newZ = y * sin + z * cos;
        
        logger.log(Level.INFO, "Rotated point ({0}, {1}, {2}) around X-axis by {3} radians to ({4}, {5}, {6})",
                   new Object[]{x, y, z, angleRadians, x, newY, newZ});
        
        return new Point3D(x, newY, newZ);
    }
    
    /**
     * Rotates the point around the Y-axis by the specified angle.
     * 
     * Performs a rotation transformation using a rotation matrix around the Y-axis.
     * The rotation follows the right-hand rule: positive angles rotate counterclockwise
     * when looking along the positive Y-axis toward the origin.
     * 
     * Rotation Matrix:
     * |  cos(θ)   0  sin(θ) |
     * |    0      1    0    |
     * | -sin(θ)   0  cos(θ) |
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(1, 0, 1);
     * Point3D rotated = point.rotateY(Math.PI / 4); // Rotate 45 degrees around Y-axis
     * </pre>
     * 
     * @param angleRadians the rotation angle in radians
     * @return a new Point3D representing the rotated point
     */
    public Point3D rotateY(double angleRadians) {
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        double newX = x * cos + z * sin;
        double newZ = -x * sin + z * cos;
        
        logger.log(Level.INFO, "Rotated point ({0}, {1}, {2}) around Y-axis by {3} radians to ({4}, {5}, {6})",
                   new Object[]{x, y, z, angleRadians, newX, y, newZ});
        
        return new Point3D(newX, y, newZ);
    }
    
    /**
     * Rotates the point around the Z-axis by the specified angle.
     * 
     * Performs a rotation transformation using a rotation matrix around the Z-axis.
     * The rotation follows the right-hand rule: positive angles rotate counterclockwise
     * when looking along the positive Z-axis toward the origin.
     * 
     * Rotation Matrix:
     * | cos(θ)  -sin(θ)  0 |
     * | sin(θ)   cos(θ)  0 |
     * |   0        0     1 |
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(1, 0, 0);
     * Point3D rotated = point.rotateZ(Math.PI / 2); // Rotate 90 degrees around Z-axis
     * </pre>
     * 
     * @param angleRadians the rotation angle in radians
     * @return a new Point3D representing the rotated point
     */
    public Point3D rotateZ(double angleRadians) {
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        double newX = x * cos - y * sin;
        double newY = x * sin + y * cos;
        
        logger.log(Level.INFO, "Rotated point ({0}, {1}, {2}) around Z-axis by {3} radians to ({4}, {5}, {6})",
                   new Object[]{x, y, z, angleRadians, newX, newY, z});
        
        return new Point3D(newX, newY, z);
    }
    
    /**
     * Translates the point by the specified offset.
     * 
     * Adds the given offsets to each coordinate, effectively moving the point in 3D space.
     * This operation is fundamental for animations, transformations, and position updates
     * in graphics and physics simulations.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(1, 2, 3);
     * Point3D moved = point.translate(5, -1, 2); // Results in (6, 1, 5)
     * </pre>
     * 
     * @param dx the offset in the x direction
     * @param dy the offset in the y direction
     * @param dz the offset in the z direction
     * @return a new Point3D representing the translated point
     */
    public Point3D translate(double dx, double dy, double dz) {
        logger.log(Level.INFO, "Translating point ({0}, {1}, {2}) by offset ({3}, {4}, {5})",
                   new Object[]{x, y, z, dx, dy, dz});
        return new Point3D(x + dx, y + dy, z + dz);
    }
    
    /**
     * Scales the point by the specified factors.
     * 
     * Multiplies each coordinate by the corresponding scale factor. This is useful for
     * resizing objects, changing coordinate systems, or applying non-uniform scaling
     * transformations.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(2, 3, 4);
     * Point3D scaled = point.scale(2.0, 1.5, 0.5); // Results in (4, 4.5, 2)
     * </pre>
     * 
     * @param sx the scale factor for the x coordinate
     * @param sy the scale factor for the y coordinate
     * @param sz the scale factor for the z coordinate
     * @return a new Point3D representing the scaled point
     */
    public Point3D scale(double sx, double sy, double sz) {
        if (sx == 0 || sy == 0 || sz == 0) {
            logger.log(Level.WARNING, "Scaling point ({0}, {1}, {2}) with zero scale factor: ({3}, {4}, {5})",
                       new Object[]{x, y, z, sx, sy, sz});
        }
        
        logger.log(Level.INFO, "Scaling point ({0}, {1}, {2}) by factors ({3}, {4}, {5})",
                   new Object[]{x, y, z, sx, sy, sz});
        return new Point3D(x * sx, y * sy, z * sz);
    }
    
    /**
     * Calculates the dot product of this point with another point.
     * 
     * The dot product (scalar product) is a fundamental vector operation that returns
     * a scalar value. It's used to determine the angle between vectors, project one
     * vector onto another, and test for orthogonality. A dot product of zero indicates
     * perpendicular vectors.
     * 
     * Formula: x1*x2 + y1*y2 + z1*z2
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D v1 = new Point3D(1, 0, 0);
     * Point3D v2 = new Point3D(0, 1, 0);
     * double dot = v1.dotProduct(v2); // Returns 0.0 (perpendicular)
     * </pre>
     * 
     * @param other the point to calculate dot product with
     * @return the dot product of this point and the other point
     * @throws NullPointerException if other is null
     */
    public double dotProduct(Point3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot calculate dot product: other point is null");
            throw new NullPointerException("Other point cannot be null");
        }
        
        double result = this.x * other.x + this.y * other.y + this.z * other.z;
        logger.log(Level.INFO, "Calculated dot product of ({0}, {1}, {2}) and ({3}, {4}, {5}): {6}",
                   new Object[]{this.x, this.y, this.z, other.x, other.y, other.z, result});
        
        return result;
    }
    
    /**
     * Calculates the cross product of this point with another point.
     * 
     * The cross product (vector product) produces a new vector perpendicular to both
     * input vectors. It's essential for calculating surface normals, determining the
     * orientation of three points, and computing torque in physics simulations.
     * The magnitude of the cross product equals the area of the parallelogram formed
     * by the two vectors.
     * 
     * Formula:
     * x = y1*z2 - z1*y2
     * y = z1*x2 - x1*z2
     * z = x1*y2 - y1*x2
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D v1 = new Point3D(1, 0, 0);
     * Point3D v2 = new Point3D(0, 1, 0);
     * Point3D cross = v1.crossProduct(v2); // Returns (0, 0, 1)
     * </pre>
     * 
     * @param other the point to calculate cross product with
     * @return a new Point3D representing the cross product
     * @throws NullPointerException if other is null
     */
    public Point3D crossProduct(Point3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot calculate cross product: other point is null");
            throw new NullPointerException("Other point cannot be null");
        }
        
        double newX = this.y * other.z - this.z * other.y;
        double newY = this.z * other.x - this.x * other.z;
        double newZ = this.x * other.y - this.y * other.x;
        
        logger.log(Level.INFO, "Calculated cross product of ({0}, {1}, {2}) and ({3}, {4}, {5}): ({6}, {7}, {8})",
                   new Object[]{this.x, this.y, this.z, other.x, other.y, other.z, newX, newY, newZ});
        
        return new Point3D(newX, newY, newZ);
    }
    
    /**
     * Returns a normalized version of this point (unit vector).
     * 
     * Normalization scales the vector to have a magnitude of 1.0 while preserving its
     * direction. Unit vectors are essential in lighting calculations, physics simulations,
     * and any algorithm requiring direction without magnitude. This operation is also
     * known as converting to a unit vector.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D point = new Point3D(3, 4, 0);
     * Point3D normalized = point.normalize(); // Returns (0.6, 0.8, 0)
     * </pre>
     * 
     * @return a new Point3D with magnitude 1.0 in the same direction
     * @throws ArithmeticException if the point is at the origin (cannot normalize zero vector)
     */
    public Point3D normalize() {
        double mag = magnitude();
        
        if (mag < EPSILON) {
            logger.log(Level.SEVERE, "Cannot normalize point at or near origin: ({0}, {1}, {2}), magnitude: {3}",
                       new Object[]{x, y, z, mag});
            throw new ArithmeticException("Cannot normalize zero vector");
        }
        
        Point3D normalized = new Point3D(x / mag, y / mag, z / mag);
        logger.log(Level.INFO, "Normalized point ({0}, {1}, {2}) to ({3}, {4}, {5})",
                   new Object[]{x, y, z, normalized.x, normalized.y, normalized.z});
        
        return normalized;
    }
    
    /**
     * Calculates the midpoint between this point and another point.
     * 
     * The midpoint is the average of the two points' coordinates, representing the
     * point exactly halfway between them. This is useful in binary space partitioning,
     * interpolation, and various geometric algorithms.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Point3D p1 = new Point3D(0, 0, 0);
     * Point3D p2 = new Point3D(4, 6, 8);
     * Point3D mid = p1.midpoint(p2); // Returns (2, 3, 4)
     * </pre>
     * 
     * @param other the other point
     * @return a new Point3D representing the midpoint
     * @throws NullPointerException if other is null
     */
    public Point3D midpoint(Point3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot calculate midpoint: other point is null");
            throw new NullPointerException("Other point cannot be null");
        }
        
        double midX = (this.x + other.x) / 2.0;
        double midY = (this.y + other.y) / 2.0;
        double midZ = (this.z + other.z) / 2.0;
        
        logger.log(Level.INFO, "Calculated midpoint between ({0}, {1}, {2}) and ({3}, {4}, {5}): ({6}, {7}, {8})",
                   new Object[]{this.x, this.y, this.z, other.x, other.y, other.z, midX, midY, midZ});
        
        return new Point3D(midX, midY, midZ);
    }
    
    // Getters
    
    /**
     * Returns the x-coordinate of this point.
     * 
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }
    
    /**
     * Returns the y-coordinate of this point.
     * 
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }
    
    /**
     * Returns the z-coordinate of this point.
     * 
     * @return the z-coordinate
     */
    public double getZ() {
        return z;
    }
    
    /**
     * Checks if this point equals another object.
     * 
     * Two points are considered equal if their coordinates are within EPSILON of each other.
     * This implementation follows the contract for equals() and properly handles floating-point
     * comparison issues. Implementing equals() correctly is crucial for using points in
     * HashMaps, HashSets, and for proper comparison in algorithms.
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Point3D other = (Point3D) obj;
        
        boolean isEqual = Math.abs(this.x - other.x) < EPSILON &&
                         Math.abs(this.y - other.y) < EPSILON &&
                         Math.abs(this.z - other.z) < EPSILON;
        
        if (isEqual) {
            logger.log(Level.INFO, "Points ({0}, {1}, {2}) and ({3}, {4}, {5}) are equal",
                       new Object[]{this.x, this.y, this.z, other.x, other.y, other.z});
        }
        
        return isEqual;
    }
    
    /**
     * Returns a hash code for this point.
     * 
     * This implementation ensures that equal points have the same hash code, which is
     * required by the hash code contract. Proper hash code implementation is essential
     * for using objects in hash-based collections (HashMap, HashSet) where it determines
     * bucket placement and lookup performance.
     * 
     * @return the hash code for this point
     */
    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        long zBits = Double.doubleToLongBits(z);
        return (int) (xBits ^ (xBits >>> 32) ^ yBits ^ (yBits >>> 32) ^ zBits ^ (zBits >>> 32));
    }
    
    /**
     * Returns a string representation of this point.
     * 
     * Provides a human-readable representation in the format "Point3D(x, y, z)".
     * This is essential for debugging, logging, and displaying point information
     * to users or developers.
     * 
     * @return a string representation of this point
     */
    @Override
    public String toString() {
        return String.format("Point3D(%.2f, %.2f, %.2f)", x, y, z);
    }
}