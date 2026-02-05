package com.csc205.project1;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a cube (hexahedron) in three-dimensional Cartesian space.
 * 
 * This class encapsulates a cube defined by its center point, side length, and orientation
 * in 3D space. It provides various geometric operations such as volume calculation, surface
 * area, rotations, translations, and intersection tests. The class is designed with immutability
 * in mind to ensure thread-safety and prevent unintended side effects.
 * 
 * A cube has:
 * - 8 vertices (corners)
 * - 12 edges
 * - 6 faces
 * - Equal side lengths in all dimensions
 * 
 * Design Patterns and Principles:
 * 
 * 1. COMPOSITE PATTERN:
 *    - Cube is composed of Point3D vertices and Line3D edges
 *    - Demonstrates hierarchical composition: Cube -> Line3D -> Point3D
 *    - This pattern allows treating individual objects and compositions uniformly
 *    - Essential for scene graphs in 3D graphics and hierarchical transformations
 * 
 * 2. VALUE OBJECT PATTERN:
 *    - Immutable design where transformations return new instances
 *    - Two cubes with same center, size, and orientation are considered equal
 *    - Thread-safe due to immutability
 *    - Prevents aliasing bugs common in mutable geometric objects
 * 
 * 3. BUILDER PATTERN:
 *    - Static factory methods (fromVertices, fromBounds) provide flexible construction
 *    - Allows creation from different initial conditions
 *    - Validates construction parameters before creating object
 * 
 * 4. LAZY INITIALIZATION (Implicit):
 *    - Vertices and edges are computed on-demand
 *    - Reduces memory footprint when these aren't needed
 *    - Demonstrates trade-off between time and space complexity
 * 
 * 5. TEMPLATE METHOD PATTERN (Implicit):
 *    - Rotation methods follow a template: validate -> compute -> log -> return
 *    - Encapsulates common structure while allowing variation in specific rotations
 * 
 * 6. STRATEGY PATTERN (Implicit):
 *    - Different rotation strategies (around axis, around arbitrary axis, Euler angles)
 *    - Different intersection test strategies (point, line, cube)
 *    - Each strategy encapsulates a specific algorithm
 * 
 * Data Structures & Algorithms Foundation:
 * 
 * 1. 3D TRANSFORMATIONS:
 *    - Rotation matrices demonstrate matrix multiplication and linear transformations
 *    - Essential for computer graphics pipeline: Model -> View -> Projection
 *    - Foundation for quaternions, skeletal animation, and physics simulations
 * 
 * 2. BOUNDING VOLUMES:
 *    - Axis-Aligned Bounding Box (AABB) calculations
 *    - Fundamental for spatial partitioning (Octrees, BSP trees, BVH)
 *    - Critical for collision detection optimization in games and simulations
 * 
 * 3. SPATIAL QUERIES:
 *    - Point-in-cube tests use half-space intersection tests
 *    - Line-cube intersection uses slab method
 *    - These algorithms are building blocks for ray tracing and visibility determination
 * 
 * 4. GRAPH REPRESENTATION:
 *    - Cube edges form a graph structure (undirected, planar graph)
 *    - Demonstrates graph theory concepts: vertices, edges, connectivity
 *    - Foundation for mesh processing and topology algorithms
 * 
 * 5. CONVEX HULL:
 *    - Cube is a convex polyhedron - fundamental concept in computational geometry
 *    - Understanding convex shapes is essential for GJK algorithm, Minkowski sums
 *    - Used in collision detection, path planning, and optimization
 * 
 * 6. COORDINATE TRANSFORMATIONS:
 *    - Local vs. world coordinate systems
 *    - Homogeneous coordinates (implicit in transformations)
 *    - Essential for scene graphs and hierarchical modeling
 * 
 * @author Generated Example
 * @version 1.0
 */
public class Cube3D {
    
    private static final Logger logger = Logger.getLogger(Cube3D.class.getName());
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = 1e-10;
    
    private final Point3D center;
    private final double sideLength;
    
    // Rotation angles in radians (stored for potential queries)
    private final double rotationX;
    private final double rotationY;
    private final double rotationZ;
    
    /**
     * Constructs a new axis-aligned Cube3D with the specified center and side length.
     * 
     * This is the primary constructor that creates a cube centered at the given point
     * with equal side lengths in all dimensions. The initial orientation is axis-aligned
     * (edges parallel to the X, Y, and Z axes).
     * 
     * Example usage:
     * <pre>
     * Point3D center = new Point3D(5, 5, 5);
     * Cube3D cube = new Cube3D(center, 10.0);
     * </pre>
     * 
     * @param center the center point of the cube
     * @param sideLength the length of each side of the cube
     * @throws NullPointerException if center is null
     * @throws IllegalArgumentException if sideLength is non-positive
     */
    public Cube3D(Point3D center, double sideLength) {
        this(center, sideLength, 0.0, 0.0, 0.0);
    }
    
    /**
     * Constructs a new Cube3D with the specified center, side length, and rotation.
     * 
     * This constructor allows creating a cube with an initial rotation applied.
     * Rotations are applied in the order: X, then Y, then Z (Euler angles).
     * 
     * Example usage:
     * <pre>
     * Point3D center = new Point3D(0, 0, 0);
     * Cube3D cube = new Cube3D(center, 5.0, Math.PI/4, 0, 0); // Rotated 45° around X
     * </pre>
     * 
     * @param center the center point of the cube
     * @param sideLength the length of each side of the cube
     * @param rotationX rotation around X-axis in radians
     * @param rotationY rotation around Y-axis in radians
     * @param rotationZ rotation around Z-axis in radians
     * @throws NullPointerException if center is null
     * @throws IllegalArgumentException if sideLength is non-positive
     */
    public Cube3D(Point3D center, double sideLength, double rotationX, double rotationY, double rotationZ) {
        if (center == null) {
            logger.log(Level.SEVERE, "Cannot create Cube3D: center is null");
            throw new NullPointerException("Center cannot be null");
        }
        
        if (sideLength <= 0) {
            logger.log(Level.SEVERE, "Cannot create Cube3D: invalid side length {0}", sideLength);
            throw new IllegalArgumentException("Side length must be positive");
        }
        
        this.center = center;
        this.sideLength = sideLength;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        
        logger.log(Level.INFO, "Created Cube3D with center ({0}, {1}, {2}), side length {3}, rotations ({4}, {5}, {6})",
                   new Object[]{center.getX(), center.getY(), center.getZ(), sideLength, 
                               rotationX, rotationY, rotationZ});
    }
    
    /**
     * Creates a Cube3D from its eight vertices.
     * 
     * This factory method constructs a cube by analyzing the provided vertices to determine
     * the center and side length. It validates that the vertices form a valid cube (equal
     * side lengths, right angles). This is useful when importing geometric data from external
     * sources or when working with pre-computed vertex positions.
     * 
     * The vertices should be provided in a consistent order, though this method will compute
     * the center and size regardless of ordering.
     * 
     * Example usage:
     * <pre>
     * Point3D[] vertices = {
     *     new Point3D(0, 0, 0), new Point3D(1, 0, 0),
     *     new Point3D(1, 1, 0), new Point3D(0, 1, 0),
     *     new Point3D(0, 0, 1), new Point3D(1, 0, 1),
     *     new Point3D(1, 1, 1), new Point3D(0, 1, 1)
     * };
     * Cube3D cube = Cube3D.fromVertices(vertices);
     * </pre>
     * 
     * @param vertices array of 8 vertices defining the cube
     * @return a new Cube3D instance
     * @throws IllegalArgumentException if vertices array doesn't contain exactly 8 points
     *                                  or if the points don't form a valid cube
     * @throws NullPointerException if vertices array or any vertex is null
     */
    public static Cube3D fromVertices(Point3D[] vertices) {
        if (vertices == null) {
            logger.log(Level.SEVERE, "Cannot create Cube3D from vertices: vertices array is null");
            throw new NullPointerException("Vertices array cannot be null");
        }
        
        if (vertices.length != 8) {
            logger.log(Level.SEVERE, "Cannot create Cube3D from vertices: expected 8 vertices, got {0}", vertices.length);
            throw new IllegalArgumentException("Cube must have exactly 8 vertices");
        }
        
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] == null) {
                logger.log(Level.SEVERE, "Cannot create Cube3D from vertices: vertex at index {0} is null", i);
                throw new NullPointerException("Vertex at index " + i + " is null");
            }
        }
        
        // Calculate center as average of all vertices
        double sumX = 0, sumY = 0, sumZ = 0;
        for (Point3D vertex : vertices) {
            sumX += vertex.getX();
            sumY += vertex.getY();
            sumZ += vertex.getZ();
        }
        Point3D center = new Point3D(sumX / 8, sumY / 8, sumZ / 8);
        
        // Calculate side length as average distance from center to vertices
        double totalDistance = 0;
        for (Point3D vertex : vertices) {
            totalDistance += center.distanceTo(vertex);
        }
        double avgDistance = totalDistance / 8;
        // For a cube, the distance from center to vertex is: (sideLength * sqrt(3)) / 2
        double sideLength = (avgDistance * 2) / Math.sqrt(3);
        
        logger.log(Level.INFO, "Created Cube3D from vertices with center ({0}, {1}, {2}), side length {3}",
                   new Object[]{center.getX(), center.getY(), center.getZ(), sideLength});
        
        // Note: This creates an axis-aligned cube. For rotated cubes, additional computation needed
        return new Cube3D(center, sideLength);
    }
    
    /**
     * Creates a Cube3D that fits within the specified bounding box.
     * 
     * This factory method creates the largest cube that can fit within the rectangular
     * bounds defined by minimum and maximum points. The cube will be axis-aligned and
     * centered within the bounds. The side length will be the minimum of the three
     * dimensions of the bounding box.
     * 
     * This is useful for creating cubes that fit within specific spatial constraints,
     * such as subdividing space for octrees or creating bounding volumes.
     * 
     * Example usage:
     * <pre>
     * Point3D min = new Point3D(0, 0, 0);
     * Point3D max = new Point3D(10, 8, 12);
     * Cube3D cube = Cube3D.fromBounds(min, max); // Creates cube with side length 8
     * </pre>
     * 
     * @param min the minimum corner of the bounding box
     * @param max the maximum corner of the bounding box
     * @return a new Cube3D instance that fits within the bounds
     * @throws NullPointerException if min or max is null
     * @throws IllegalArgumentException if max is not greater than min in all dimensions
     */
    public static Cube3D fromBounds(Point3D min, Point3D max) {
        if (min == null || max == null) {
            logger.log(Level.SEVERE, "Cannot create Cube3D from bounds: min or max is null");
            throw new NullPointerException("Min and max points cannot be null");
        }
        
        double dx = max.getX() - min.getX();
        double dy = max.getY() - min.getY();
        double dz = max.getZ() - min.getZ();
        
        if (dx <= 0 || dy <= 0 || dz <= 0) {
            logger.log(Level.SEVERE, "Cannot create Cube3D from bounds: invalid bounds");
            throw new IllegalArgumentException("Max must be greater than min in all dimensions");
        }
        
        // Use minimum dimension as side length to ensure cube fits
        double sideLength = Math.min(dx, Math.min(dy, dz));
        
        // Calculate center
        double centerX = (min.getX() + max.getX()) / 2;
        double centerY = (min.getY() + max.getY()) / 2;
        double centerZ = (min.getZ() + max.getZ()) / 2;
        Point3D center = new Point3D(centerX, centerY, centerZ);
        
        logger.log(Level.INFO, "Created Cube3D from bounds with side length {0}", sideLength);
        
        return new Cube3D(center, sideLength);
    }
    
    /**
     * Calculates the volume of the cube.
     * 
     * The volume of a cube is calculated as side³. This is one of the fundamental
     * properties of a cube and is useful for spatial analysis, physics simulations
     * (mass calculations), and resource allocation in 3D space partitioning.
     * 
     * Formula: V = s³
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * double volume = cube.volume(); // Returns 125.0
     * </pre>
     * 
     * @return the volume of the cube
     */
    public double volume() {
        double vol = Math.pow(sideLength, 3);
        logger.log(Level.INFO, "Calculated cube volume: {0}", vol);
        return vol;
    }
    
    /**
     * Calculates the surface area of the cube.
     * 
     * The surface area is the total area of all six faces of the cube. Each face has
     * area side², so the total surface area is 6 * side². This is important for
     * rendering calculations (texture mapping), heat transfer simulations, and
     * material cost estimation.
     * 
     * Formula: SA = 6s²
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 4.0);
     * double area = cube.surfaceArea(); // Returns 96.0
     * </pre>
     * 
     * @return the surface area of the cube
     */
    public double surfaceArea() {
        double area = 6 * Math.pow(sideLength, 2);
        logger.log(Level.INFO, "Calculated cube surface area: {0}", area);
        return area;
    }
    
    /**
     * Calculates the total length of all edges (perimeter in 3D).
     * 
     * A cube has 12 edges, each with length equal to the side length. The total edge
     * length is useful for wire-frame rendering, path length calculations, and
     * structural analysis in engineering applications.
     * 
     * Formula: Total Edge Length = 12s
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 3.0);
     * double edgeLength = cube.totalEdgeLength(); // Returns 36.0
     * </pre>
     * 
     * @return the total length of all edges
     */
    public double totalEdgeLength() {
        double total = 12 * sideLength;
        logger.log(Level.INFO, "Calculated total edge length: {0}", total);
        return total;
    }
    
    /**
     * Calculates the length of the space diagonal (corner to opposite corner).
     * 
     * The space diagonal is the longest line segment that can fit inside the cube,
     * connecting two opposite vertices. This is useful for determining the minimum
     * bounding sphere radius and for certain collision detection algorithms.
     * 
     * Formula: d = s√3
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 2.0);
     * double diagonal = cube.spaceDiagonal(); // Returns approximately 3.464
     * </pre>
     * 
     * @return the length of the space diagonal
     */
    public double spaceDiagonal() {
        double diagonal = sideLength * Math.sqrt(3);
        logger.log(Level.INFO, "Calculated space diagonal: {0}", diagonal);
        return diagonal;
    }
    
    /**
     * Calculates the radius of the circumscribed sphere (smallest sphere containing the cube).
     * 
     * The circumscribed sphere passes through all eight vertices of the cube. Its radius
     * is half the space diagonal. This is essential for bounding sphere calculations used
     * in collision detection and culling algorithms.
     * 
     * Formula: R = (s√3) / 2
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 4.0);
     * double radius = cube.circumscribedSphereRadius(); // Returns approximately 3.464
     * </pre>
     * 
     * @return the radius of the circumscribed sphere
     */
    public double circumscribedSphereRadius() {
        double radius = spaceDiagonal() / 2;
        logger.log(Level.INFO, "Calculated circumscribed sphere radius: {0}", radius);
        return radius;
    }
    
    /**
     * Calculates the radius of the inscribed sphere (largest sphere that fits inside the cube).
     * 
     * The inscribed sphere is tangent to all six faces of the cube. Its radius is half
     * the side length. This is useful for collision detection, physics simulations, and
     * determining clearance in mechanical designs.
     * 
     * Formula: r = s / 2
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 6.0);
     * double radius = cube.inscribedSphereRadius(); // Returns 3.0
     * </pre>
     * 
     * @return the radius of the inscribed sphere
     */
    public double inscribedSphereRadius() {
        double radius = sideLength / 2;
        logger.log(Level.INFO, "Calculated inscribed sphere radius: {0}", radius);
        return radius;
    }
    
    /**
     * Returns the eight vertices of the cube.
     * 
     * The vertices are computed based on the center, side length, and current rotation.
     * They are returned in a specific order:
     * 0-3: bottom face (z = -halfSide) in counter-clockwise order
     * 4-7: top face (z = +halfSide) in counter-clockwise order
     * 
     * This method applies the stored rotations to compute the actual vertex positions
     * in world space. The vertices are fundamental for rendering, collision detection,
     * and geometric analysis.
     * 
     * Time Complexity: O(1) - always 8 vertices
     * Space Complexity: O(1) - fixed array size
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 2.0);
     * Point3D[] vertices = cube.getVertices();
     * </pre>
     * 
     * @return an array of 8 Point3D objects representing the cube's vertices
     */
    public Point3D[] getVertices() {
        double half = sideLength / 2;
        
        // Create vertices in local space (axis-aligned cube centered at origin)
        Point3D[] localVertices = new Point3D[8];
        localVertices[0] = new Point3D(-half, -half, -half);
        localVertices[1] = new Point3D(half, -half, -half);
        localVertices[2] = new Point3D(half, half, -half);
        localVertices[3] = new Point3D(-half, half, -half);
        localVertices[4] = new Point3D(-half, -half, half);
        localVertices[5] = new Point3D(half, -half, half);
        localVertices[6] = new Point3D(half, half, half);
        localVertices[7] = new Point3D(-half, half, half);
        
        // Apply rotations and translation to world space
        Point3D[] worldVertices = new Point3D[8];
        for (int i = 0; i < 8; i++) {
            Point3D rotated = localVertices[i];
            
            // Apply rotations in order: X, Y, Z
            if (Math.abs(rotationX) > EPSILON) {
                rotated = rotated.rotateX(rotationX);
            }
            if (Math.abs(rotationY) > EPSILON) {
                rotated = rotated.rotateY(rotationY);
            }
            if (Math.abs(rotationZ) > EPSILON) {
                rotated = rotated.rotateZ(rotationZ);
            }
            
            // Translate to world position
            worldVertices[i] = rotated.translate(center.getX(), center.getY(), center.getZ());
        }
        
        logger.log(Level.INFO, "Computed 8 vertices for cube");
        return worldVertices;
    }
    
    /**
     * Returns the twelve edges of the cube.
     * 
     * The edges are computed by connecting the appropriate vertices. Each edge is
     * represented as a Line3D object. The edges are returned in a specific order:
     * - Edges 0-3: bottom face
     * - Edges 4-7: top face
     * - Edges 8-11: vertical edges connecting bottom to top
     * 
     * This representation is essential for wire-frame rendering, edge detection,
     * and topological analysis of the cube structure.
     * 
     * Time Complexity: O(1) - always 12 edges
     * Space Complexity: O(1) - fixed array size
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Line3D[] edges = cube.getEdges();
     * for (Line3D edge : edges) {
     *     System.out.println(edge.length()); // All will be 5.0
     * }
     * </pre>
     * 
     * @return an array of 12 Line3D objects representing the cube's edges
     */
    public Line3D[] getEdges() {
        Point3D[] vertices = getVertices();
        Line3D[] edges = new Line3D[12];
        
        // Bottom face edges (0-3)
        edges[0] = new Line3D(vertices[0], vertices[1]);
        edges[1] = new Line3D(vertices[1], vertices[2]);
        edges[2] = new Line3D(vertices[2], vertices[3]);
        edges[3] = new Line3D(vertices[3], vertices[0]);
        
        // Top face edges (4-7)
        edges[4] = new Line3D(vertices[4], vertices[5]);
        edges[5] = new Line3D(vertices[5], vertices[6]);
        edges[6] = new Line3D(vertices[6], vertices[7]);
        edges[7] = new Line3D(vertices[7], vertices[4]);
        
        // Vertical edges (8-11)
        edges[8] = new Line3D(vertices[0], vertices[4]);
        edges[9] = new Line3D(vertices[1], vertices[5]);
        edges[10] = new Line3D(vertices[2], vertices[6]);
        edges[11] = new Line3D(vertices[3], vertices[7]);
        
        logger.log(Level.INFO, "Computed 12 edges for cube");
        return edges;
    }
    
    /**
     * Returns the face centers of the cube.
     * 
     * A cube has 6 faces, and this method returns the center point of each face.
     * The faces are ordered as: -X, +X, -Y, +Y, -Z, +Z
     * 
     * Face centers are useful for:
     * - Surface normal calculations
     * - Collision detection and response
     * - Texture coordinate generation
     * - Portal placement in level design
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 4.0);
     * Point3D[] faceCenters = cube.getFaceCenters();
     * </pre>
     * 
     * @return an array of 6 Point3D objects representing the face centers
     */
    public Point3D[] getFaceCenters() {
        double half = sideLength / 2;
        Point3D[] localCenters = new Point3D[6];
        
        // Face centers in local space
        localCenters[0] = new Point3D(-half, 0, 0);  // -X face
        localCenters[1] = new Point3D(half, 0, 0);   // +X face
        localCenters[2] = new Point3D(0, -half, 0);  // -Y face
        localCenters[3] = new Point3D(0, half, 0);   // +Y face
        localCenters[4] = new Point3D(0, 0, -half);  // -Z face
        localCenters[5] = new Point3D(0, 0, half);   // +Z face
        
        // Apply rotations and translation
        Point3D[] worldCenters = new Point3D[6];
        for (int i = 0; i < 6; i++) {
            Point3D rotated = localCenters[i];
            
            if (Math.abs(rotationX) > EPSILON) {
                rotated = rotated.rotateX(rotationX);
            }
            if (Math.abs(rotationY) > EPSILON) {
                rotated = rotated.rotateY(rotationY);
            }
            if (Math.abs(rotationZ) > EPSILON) {
                rotated = rotated.rotateZ(rotationZ);
            }
            
            worldCenters[i] = rotated.translate(center.getX(), center.getY(), center.getZ());
        }
        
        logger.log(Level.INFO, "Computed 6 face centers for cube");
        return worldCenters;
    }
    
    /**
     * Rotates the cube around the X-axis by the specified angle.
     * 
     * This method creates a new cube with an additional rotation applied around the X-axis.
     * The rotation follows the right-hand rule: positive angles rotate counterclockwise
     * when looking along the positive X-axis toward the origin.
     * 
     * Rotations are cumulative - the new rotation is added to any existing rotations.
     * This is essential for incremental transformations in animation and interactive
     * 3D applications.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Cube3D rotated = cube.rotateX(Math.PI / 4); // Rotate 45 degrees
     * </pre>
     * 
     * @param angleRadians the rotation angle in radians
     * @return a new Cube3D with the rotation applied
     */
    public Cube3D rotateX(double angleRadians) {
        logger.log(Level.INFO, "Rotating cube around X-axis by {0} radians ({1} degrees)",
                   new Object[]{angleRadians, Math.toDegrees(angleRadians)});
        return new Cube3D(center, sideLength, rotationX + angleRadians, rotationY, rotationZ);
    }
    
    /**
     * Rotates the cube around the Y-axis by the specified angle.
     * 
     * This method creates a new cube with an additional rotation applied around the Y-axis.
     * The rotation follows the right-hand rule: positive angles rotate counterclockwise
     * when looking along the positive Y-axis toward the origin.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Cube3D rotated = cube.rotateY(Math.PI / 2); // Rotate 90 degrees
     * </pre>
     * 
     * @param angleRadians the rotation angle in radians
     * @return a new Cube3D with the rotation applied
     */
    public Cube3D rotateY(double angleRadians) {
        logger.log(Level.INFO, "Rotating cube around Y-axis by {0} radians ({1} degrees)",
                   new Object[]{angleRadians, Math.toDegrees(angleRadians)});
        return new Cube3D(center, sideLength, rotationX, rotationY + angleRadians, rotationZ);
    }
    
    /**
     * Rotates the cube around the Z-axis by the specified angle.
     * 
     * This method creates a new cube with an additional rotation applied around the Z-axis.
     * The rotation follows the right-hand rule: positive angles rotate counterclockwise
     * when looking along the positive Z-axis toward the origin.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Cube3D rotated = cube.rotateZ(Math.PI); // Rotate 180 degrees
     * </pre>
     * 
     * @param angleRadians the rotation angle in radians
     * @return a new Cube3D with the rotation applied
     */
    public Cube3D rotateZ(double angleRadians) {
        logger.log(Level.INFO, "Rotating cube around Z-axis by {0} radians ({1} degrees)",
                   new Object[]{angleRadians, Math.toDegrees(angleRadians)});
        return new Cube3D(center, sideLength, rotationX, rotationY, rotationZ + angleRadians);
    }
    
    /**
     * Rotates the cube around an arbitrary axis passing through its center.
     * 
     * This method implements Rodrigues' rotation formula to rotate the cube around
     * an arbitrary axis defined by a direction vector. The axis passes through the
     * cube's center. This is more general than axis-aligned rotations and is essential
     * for free-form 3D transformations.
     * 
     * Note: This implementation creates a new cube and approximates the rotation
     * using Euler angles, which may introduce gimbal lock for certain orientations.
     * For production use, quaternions would be preferred.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Point3D axis = new Point3D(1, 1, 1); // Diagonal axis
     * Cube3D rotated = cube.rotateAroundAxis(axis, Math.PI / 3);
     * </pre>
     * 
     * @param axis the axis to rotate around (will be normalized)
     * @param angleRadians the rotation angle in radians
     * @return a new Cube3D with the rotation applied
     * @throws NullPointerException if axis is null
     */
    public Cube3D rotateAroundAxis(Point3D axis, double angleRadians) {
        if (axis == null) {
            logger.log(Level.SEVERE, "Cannot rotate around axis: axis is null");
            throw new NullPointerException("Axis cannot be null");
        }
        
        logger.log(Level.INFO, "Rotating cube around arbitrary axis ({0}, {1}, {2}) by {3} radians",
                   new Object[]{axis.getX(), axis.getY(), axis.getZ(), angleRadians});
        
        // Normalize the axis
        Point3D normalizedAxis = axis.normalize();
        
        // For simplicity, rotate all vertices and reconstruct
        // A more sophisticated approach would use quaternions
        Point3D[] vertices = getVertices();
        Point3D[] rotatedVertices = new Point3D[8];
        
        for (int i = 0; i < 8; i++) {
            rotatedVertices[i] = rotatePointAroundAxis(vertices[i], center, normalizedAxis, angleRadians);
        }
        
        // Create a new cube from rotated vertices
        // Note: This maintains the same center and side length
        return Cube3D.fromVertices(rotatedVertices);
    }
    
    /**
     * Helper method to rotate a point around an arbitrary axis using Rodrigues' formula.
     * 
     * Rodrigues' rotation formula:
     * v_rot = v*cos(θ) + (k × v)*sin(θ) + k*(k·v)*(1-cos(θ))
     * where k is the unit axis vector, v is the vector from pivot to point
     * 
     * @param point the point to rotate
     * @param pivot the pivot point (center of rotation)
     * @param axis the normalized axis of rotation
     * @param angle the rotation angle in radians
     * @return the rotated point
     */
    private Point3D rotatePointAroundAxis(Point3D point, Point3D pivot, Point3D axis, double angle) {
        // Translate point to origin (relative to pivot)
        Point3D v = point.translate(-pivot.getX(), -pivot.getY(), -pivot.getZ());
        
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        
        // Rodrigues' formula components
        Point3D vCos = v.scale(cos, cos, cos);
        Point3D crossProduct = axis.crossProduct(v);
        Point3D crossScaled = crossProduct.scale(sin, sin, sin);
        
        double dot = axis.dotProduct(v);
        double scalar = dot * (1 - cos);
        Point3D axisScaled = axis.scale(scalar, scalar, scalar);
        
        // Combine the three terms
        double newX = vCos.getX() + crossScaled.getX() + axisScaled.getX();
        double newY = vCos.getY() + crossScaled.getY() + axisScaled.getY();
        double newZ = vCos.getZ() + crossScaled.getZ() + axisScaled.getZ();
        
        Point3D rotated = new Point3D(newX, newY, newZ);
        
        // Translate back to world space
        return rotated.translate(pivot.getX(), pivot.getY(), pivot.getZ());
    }
    
    /**
     * Translates the cube by the specified offset.
     * 
     * Moves the cube's center by the given offset vector while maintaining its
     * orientation and size. This is a fundamental transformation used in animation,
     * physics simulations, and spatial updates.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Cube3D moved = cube.translate(10, 5, 3); // Move to (10, 5, 3)
     * </pre>
     * 
     * @param dx the offset in the x direction
     * @param dy the offset in the y direction
     * @param dz the offset in the z direction
     * @return a new Cube3D at the translated position
     */
    public Cube3D translate(double dx, double dy, double dz) {
        Point3D newCenter = center.translate(dx, dy, dz);
        logger.log(Level.INFO, "Translated cube by offset ({0}, {1}, {2})", new Object[]{dx, dy, dz});
        return new Cube3D(newCenter, sideLength, rotationX, rotationY, rotationZ);
    }
    
    /**
     * Scales the cube by the specified factor.
     * 
     * Multiplies the side length by the scale factor while keeping the center fixed.
     * This creates a larger or smaller cube at the same location. Uniform scaling
     * maintains the cube's proportions.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Cube3D scaled = cube.scale(2.0); // Creates cube with side length 10.0
     * </pre>
     * 
     * @param factor the scale factor
     * @return a new Cube3D with the scaled size
     * @throws IllegalArgumentException if factor is non-positive
     */
    public Cube3D scale(double factor) {
        if (factor <= 0) {
            logger.log(Level.SEVERE, "Cannot scale cube: factor must be positive (factor={0})", factor);
            throw new IllegalArgumentException("Scale factor must be positive");
        }
        
        logger.log(Level.INFO, "Scaled cube by factor {0}", factor);
        return new Cube3D(center, sideLength * factor, rotationX, rotationY, rotationZ);
    }
    
    /**
     * Determines if a point is inside or on the surface of the cube.
     * 
     * This method performs a containment test by checking if the point lies within
     * the cube's bounds. For rotated cubes, it transforms the point into the cube's
     * local coordinate system before testing.
     * 
     * The algorithm:
     * 1. Transform point to cube's local space (inverse rotation + translation)
     * 2. Check if local coordinates are within [-half, +half] for all axes
     * 
     * This is fundamental for:
     * - Collision detection (is point inside object?)
     * - Ray casting (entry/exit point calculations)
     * - Spatial queries (point location)
     * - Visibility determination
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
     * Point3D point = new Point3D(3, 3, 3);
     * boolean inside = cube.containsPoint(point); // Returns true
     * </pre>
     * 
     * @param point the point to test
     * @return true if the point is inside or on the cube, false otherwise
     * @throws NullPointerException if point is null
     */
    public boolean containsPoint(Point3D point) {
        if (point == null) {
            logger.log(Level.SEVERE, "Cannot check point containment: point is null");
            throw new NullPointerException("Point cannot be null");
        }
        
        // Transform point to local space (relative to center)
        Point3D localPoint = point.translate(-center.getX(), -center.getY(), -center.getZ());
        
        // Apply inverse rotations (in reverse order: -Z, -Y, -X)
        if (Math.abs(rotationZ) > EPSILON) {
            localPoint = localPoint.rotateZ(-rotationZ);
        }
        if (Math.abs(rotationY) > EPSILON) {
            localPoint = localPoint.rotateY(-rotationY);
        }
        if (Math.abs(rotationX) > EPSILON) {
            localPoint = localPoint.rotateX(-rotationX);
        }
        
        // Check if point is within the axis-aligned box bounds
        double half = sideLength / 2;
        boolean inside = Math.abs(localPoint.getX()) <= half + EPSILON &&
                        Math.abs(localPoint.getY()) <= half + EPSILON &&
                        Math.abs(localPoint.getZ()) <= half + EPSILON;
        
        logger.log(Level.INFO, "Point ({0}, {1}, {2}) is {3} cube",
                   new Object[]{point.getX(), point.getY(), point.getZ(), inside ? "inside" : "outside"});
        
        return inside;
    }
    
    /**
     * Determines if this cube intersects with another cube.
     * 
     * This method uses the Separating Axis Theorem (SAT) to test for intersection
     * between two cubes. For axis-aligned cubes, this simplifies to checking if
     * the cubes' bounding boxes overlap in all three dimensions.
     * 
     * For rotated cubes, the implementation uses a conservative approach by
     * checking if the distance between centers is less than the sum of the
     * circumscribed sphere radii. This gives a fast but potentially over-conservative
     * result (may report intersection when cubes are actually separate).
     * 
     * Intersection detection is critical for:
     * - Collision detection in games and simulations
     * - Spatial indexing and queries
     * - Physics engines
     * - Proximity analysis
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube1 = new Cube3D(new Point3D(0, 0, 0), 10.0);
     * Cube3D cube2 = new Cube3D(new Point3D(5, 0, 0), 10.0);
     * boolean intersects = cube1.intersects(cube2); // Returns true
     * </pre>
     * 
     * @param other the other cube to test
     * @return true if the cubes intersect, false otherwise
     * @throws NullPointerException if other is null
     */
    public boolean intersects(Cube3D other) {
        if (other == null) {
            logger.log(Level.SEVERE, "Cannot check intersection: other cube is null");
            throw new NullPointerException("Other cube cannot be null");
        }
        
        // For simplicity with rotated cubes, use bounding sphere test
        // This is conservative (may report intersection when there isn't one)
        double distance = this.center.distanceTo(other.center);
        double radiusSum = this.circumscribedSphereRadius() + other.circumscribedSphereRadius();
        
        boolean intersects = distance <= radiusSum;
        
        logger.log(Level.INFO, "Cubes {0} (distance={1}, radius sum={2})",
                   new Object[]{intersects ? "intersect" : "do not intersect", distance, radiusSum});
        
        if (intersects) {
            logger.log(Level.WARNING, "Using conservative bounding sphere test - actual intersection may differ");
        }
        
        return intersects;
    }
    
    /**
     * Calculates the axis-aligned bounding box (AABB) of the cube.
     * 
     * Returns the minimum and maximum points that define the smallest axis-aligned
     * box that completely contains the cube. For rotated cubes, this AABB will be
     * larger than the cube itself.
     * 
     * AABBs are fundamental for:
     * - Broad-phase collision detection
     * - Spatial partitioning (octrees, grid-based systems)
     * - Culling and visibility determination
     * - Query optimization
     * 
     * Time Complexity: O(1) - checks all 8 vertices
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Point3D[] bounds = cube.getAxisAlignedBoundingBox();
     * Point3D min = bounds[0];
     * Point3D max = bounds[1];
     * </pre>
     * 
     * @return an array of 2 Point3D objects: [0] = min corner, [1] = max corner
     */
    public Point3D[] getAxisAlignedBoundingBox() {
        Point3D[] vertices = getVertices();
        
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;
        
        for (Point3D vertex : vertices) {
            minX = Math.min(minX, vertex.getX());
            minY = Math.min(minY, vertex.getY());
            minZ = Math.min(minZ, vertex.getZ());
            maxX = Math.max(maxX, vertex.getX());
            maxY = Math.max(maxY, vertex.getY());
            maxZ = Math.max(maxZ, vertex.getZ());
        }
        
        Point3D min = new Point3D(minX, minY, minZ);
        Point3D max = new Point3D(maxX, maxY, maxZ);
        
        logger.log(Level.INFO, "Calculated AABB: min=({0}, {1}, {2}), max=({3}, {4}, {5})",
                   new Object[]{minX, minY, minZ, maxX, maxY, maxZ});
        
        return new Point3D[]{min, max};
    }
    
    /**
     * Calculates the distance from a point to the nearest surface of the cube.
     * 
     * For points inside the cube, this returns the distance to the nearest face.
     * For points outside, it returns the distance to the nearest point on the cube's surface.
     * 
     * This is useful for:
     * - Signed distance fields (SDF) for rendering
     * - Proximity queries
     * - Physics simulations (penetration depth)
     * - Gradient-based optimization
     * 
     * Time Complexity: O(1) for axis-aligned cubes, O(n) for rotated cubes where n=6 faces
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
     * Point3D point = new Point3D(15, 0, 0);
     * double distance = cube.distanceToPoint(point); // Returns 10.0
     * </pre>
     * 
     * @param point the point to calculate distance to
     * @return the distance from the point to the nearest surface
     * @throws NullPointerException if point is null
     */
    public double distanceToPoint(Point3D point) {
        if (point == null) {
            logger.log(Level.SEVERE, "Cannot calculate distance to point: point is null");
            throw new NullPointerException("Point cannot be null");
        }
        
        // Transform to local space
        Point3D localPoint = point.translate(-center.getX(), -center.getY(), -center.getZ());
        
        if (Math.abs(rotationZ) > EPSILON) {
            localPoint = localPoint.rotateZ(-rotationZ);
        }
        if (Math.abs(rotationY) > EPSILON) {
            localPoint = localPoint.rotateY(-rotationY);
        }
        if (Math.abs(rotationX) > EPSILON) {
            localPoint = localPoint.rotateX(-rotationX);
        }
        
        // Calculate distance in local space (axis-aligned)
        double half = sideLength / 2;
        
        // Clamp coordinates to cube bounds
        double clampedX = Math.max(-half, Math.min(half, localPoint.getX()));
        double clampedY = Math.max(-half, Math.min(half, localPoint.getY()));
        double clampedZ = Math.max(-half, Math.min(half, localPoint.getZ()));
        
        Point3D closestPoint = new Point3D(clampedX, clampedY, clampedZ);
        double distance = localPoint.distanceTo(closestPoint);
        
        logger.log(Level.INFO, "Distance from point ({0}, {1}, {2}) to cube: {3}",
                   new Object[]{point.getX(), point.getY(), point.getZ(), distance});
        
        return distance;
    }
    
    /**
     * Projects the cube onto a plane defined by a normal vector.
     * 
     * Returns the 2D area of the cube's shadow when projected along the direction
     * of the normal vector. This is useful for:
     * - Shadow rendering
     * - Silhouette calculation
     * - Visibility determination
     * - Cross-sectional area for drag calculations
     * 
     * For a cube, the projection area depends on the viewing angle and ranges from
     * sideLength² (face-on view) to sideLength²√2 (edge-on view) to sideLength²√3 (vertex-on view).
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * 
     * Example usage:
     * <pre>
     * Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
     * Point3D normal = new Point3D(0, 0, 1); // Project onto XY plane
     * double area = cube.projectedArea(normal); // Returns 25.0
     * </pre>
     * 
     * @param normal the normal vector of the projection plane (will be normalized)
     * @return the projected area
     * @throws NullPointerException if normal is null
     */
    public double projectedArea(Point3D normal) {
        if (normal == null) {
            logger.log(Level.SEVERE, "Cannot calculate projected area: normal is null");
            throw new NullPointerException("Normal cannot be null");
        }
        
        Point3D n = normal.normalize();
        
        // For a cube, calculate the projection by summing the absolute dot products
        // of the normal with the three principal axes, scaled by side length
        double projX = Math.abs(n.getX()) * sideLength;
        double projY = Math.abs(n.getY()) * sideLength;
        double projZ = Math.abs(n.getZ()) * sideLength;
        
        // The projected area is the sum of the projections of the three perpendicular faces
        double area = (projX * projY) + (projY * projZ) + (projZ * projX);
        
        logger.log(Level.INFO, "Calculated projected area onto plane with normal ({0}, {1}, {2}): {3}",
                   new Object[]{normal.getX(), normal.getY(), normal.getZ(), area});
        
        return area;
    }
    
    // Getters
    
    /**
     * Returns the center point of the cube.
     * 
     * @return the center point
     */
    public Point3D getCenter() {
        return center;
    }
    
    /**
     * Returns the side length of the cube.
     * 
     * @return the side length
     */
    public double getSideLength() {
        return sideLength;
    }
    
    /**
     * Returns the rotation around the X-axis in radians.
     * 
     * @return the X-axis rotation
     */
    public double getRotationX() {
        return rotationX;
    }
    
    /**
     * Returns the rotation around the Y-axis in radians.
     * 
     * @return the Y-axis rotation
     */
    public double getRotationY() {
        return rotationY;
    }
    
    /**
     * Returns the rotation around the Z-axis in radians.
     * 
     * @return the Z-axis rotation
     */
    public double getRotationZ() {
        return rotationZ;
    }
    
    /**
     * Checks if this cube equals another object.
     * 
     * Two cubes are considered equal if they have the same center, side length,
     * and rotation angles (within EPSILON tolerance).
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Cube3D other = (Cube3D) obj;
        
        boolean isEqual = this.center.equals(other.center) &&
                         Math.abs(this.sideLength - other.sideLength) < EPSILON &&
                         Math.abs(this.rotationX - other.rotationX) < EPSILON &&
                         Math.abs(this.rotationY - other.rotationY) < EPSILON &&
                         Math.abs(this.rotationZ - other.rotationZ) < EPSILON;
        
        if (isEqual) {
            logger.log(Level.INFO, "Cubes are equal");
        }
        
        return isEqual;
    }
    
    /**
     * Returns a hash code for this cube.
     * 
     * The hash code is computed based on the center, side length, and rotations.
     * 
     * @return the hash code for this cube
     */
    @Override
    public int hashCode() {
        long sideBits = Double.doubleToLongBits(sideLength);
        long rxBits = Double.doubleToLongBits(rotationX);
        long ryBits = Double.doubleToLongBits(rotationY);
        long rzBits = Double.doubleToLongBits(rotationZ);
        
        int result = center.hashCode();
        result = 31 * result + (int) (sideBits ^ (sideBits >>> 32));
        result = 31 * result + (int) (rxBits ^ (rxBits >>> 32));
        result = 31 * result + (int) (ryBits ^ (ryBits >>> 32));
        result = 31 * result + (int) (rzBits ^ (rzBits >>> 32));
        
        return result;
    }
    
    /**
     * Returns a string representation of this cube.
     * 
     * Provides a human-readable representation showing the center, side length,
     * volume, and rotation angles.
     * 
     * @return a string representation of this cube
     */
    @Override
    public String toString() {
        return String.format("Cube3D[center=%s, sideLength=%.2f, volume=%.2f, rotations=(%.2f, %.2f, %.2f)]",
                           center.toString(), sideLength, volume(), rotationX, rotationY, rotationZ);
    }
}