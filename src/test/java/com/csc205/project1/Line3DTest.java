package com.csc205.project1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the Line3D class.
 * 
 * This test suite covers:
 * - Constructor validation and initialization
 * - Factory method creation (fromDirectionVector, fromPoints)
 * - Length calculation
 * - Direction and normalized direction vectors
 * - Midpoint calculation
 * - Parametric point evaluation
 * - Distance calculations (point-to-line, line-to-line)
 * - Closest point calculations
 * - Parallel and perpendicular line detection
 * - Angle calculations between lines
 * - Point containment tests
 * - Translation, scaling, and reversal operations
 * - Equality and hash code contracts
 * - Edge cases and boundary conditions
 * - Null pointer handling
 * - Degenerate line cases
 * - Numerical precision handling
 * 
 * Test Organization:
 * - Nested test classes group related functionality
 * - Parameterized tests verify behavior across multiple inputs
 * - Edge cases include zero-length attempts, parallel/perpendicular lines, and collinear points
 * 
 * @author Generated Example
 * @version 1.0
 */
@DisplayName("Line3D Tests")
public class Line3DTest {
    
    private static final double EPSILON = 1e-10;
    private static final double DELTA = 1e-9; // Tolerance for floating-point comparisons
    
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create line with valid start and end points")
        void testConstructor() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(5, 5, 5);
            
            Line3D line = new Line3D(start, end);
            
            assertEquals(start, line.getStart());
            assertEquals(end, line.getEnd());
        }
        
        @Test
        @DisplayName("Should throw exception for null start point")
        void testConstructorNullStart() {
            Point3D end = new Point3D(5, 5, 5);
            
            assertThrows(NullPointerException.class, () -> {
                new Line3D(null, end);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for null end point")
        void testConstructorNullEnd() {
            Point3D start = new Point3D(0, 0, 0);
            
            assertThrows(NullPointerException.class, () -> {
                new Line3D(start, null);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for degenerate line (same start and end)")
        void testConstructorDegenerateLine() {
            Point3D point = new Point3D(3, 3, 3);
            
            assertThrows(IllegalArgumentException.class, () -> {
                new Line3D(point, point);
            });
        }
        
        @Test
        @DisplayName("Should handle line with negative coordinates")
        void testConstructorNegativeCoordinates() {
            Point3D start = new Point3D(-5, -10, -15);
            Point3D end = new Point3D(-1, -2, -3);
            
            Line3D line = new Line3D(start, end);
            
            assertNotNull(line);
            assertEquals(start, line.getStart());
            assertEquals(end, line.getEnd());
        }
    }
    
    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {
        
        @Test
        @DisplayName("Should create line from direction vector")
        void testFromDirectionVector() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D direction = new Point3D(1, 0, 0);
            double length = 10.0;
            
            Line3D line = Line3D.fromDirectionVector(start, direction, length);
            
            assertEquals(start, line.getStart());
            assertEquals(10.0, line.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should normalize direction vector when creating line")
        void testFromDirectionVectorNormalization() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D direction = new Point3D(3, 4, 0); // Length 5
            double length = 10.0;
            
            Line3D line = Line3D.fromDirectionVector(start, direction, length);
            
            assertEquals(10.0, line.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null start in fromDirectionVector")
        void testFromDirectionVectorNullStart() {
            Point3D direction = new Point3D(1, 0, 0);
            
            assertThrows(NullPointerException.class, () -> {
                Line3D.fromDirectionVector(null, direction, 10.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for null direction in fromDirectionVector")
        void testFromDirectionVectorNullDirection() {
            Point3D start = new Point3D(0, 0, 0);
            
            assertThrows(NullPointerException.class, () -> {
                Line3D.fromDirectionVector(start, null, 10.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for non-positive length")
        void testFromDirectionVectorInvalidLength() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D direction = new Point3D(1, 0, 0);
            
            assertThrows(IllegalArgumentException.class, () -> {
                Line3D.fromDirectionVector(start, direction, 0);
            });
            
            assertThrows(IllegalArgumentException.class, () -> {
                Line3D.fromDirectionVector(start, direction, -5.0);
            });
        }
        
        @Test
        @DisplayName("Should create line from two points using fromPoints")
        void testFromPoints() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(4, 5, 6);
            
            Line3D line = Line3D.fromPoints(p1, p2);
            
            assertEquals(p1, line.getStart());
            assertEquals(p2, line.getEnd());
        }
    }
    
    @Nested
    @DisplayName("Length Tests")
    class LengthTests {
        
        @Test
        @DisplayName("Should calculate length correctly")
        void testLength() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(3, 4, 0);
            Line3D line = new Line3D(start, end);
            
            assertEquals(5.0, line.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should calculate length in 3D space")
        void testLength3D() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 6, 8);
            Line3D line = new Line3D(start, end);
            
            // Distance = sqrt((4-1)² + (6-2)² + (8-3)²) = sqrt(9 + 16 + 25) = sqrt(50)
            assertEquals(Math.sqrt(50), line.length(), DELTA);
        }
        
        @ParameterizedTest
        @CsvSource({
            "0, 0, 0, 1, 0, 0, 1.0",
            "0, 0, 0, 0, 1, 0, 1.0",
            "0, 0, 0, 0, 0, 1, 1.0",
            "0, 0, 0, 1, 1, 1, 1.732050808",
            "0, 0, 0, 3, 4, 0, 5.0",
            "1, 1, 1, 4, 5, 6, 7.071067812"
        })
        @DisplayName("Should calculate length for various lines")
        void testLengthParameterized(double x1, double y1, double z1, 
                                     double x2, double y2, double z2, 
                                     double expectedLength) {
            Point3D start = new Point3D(x1, y1, z1);
            Point3D end = new Point3D(x2, y2, z2);
            Line3D line = new Line3D(start, end);
            
            assertEquals(expectedLength, line.length(), 1e-6);
        }
    }
    
    @Nested
    @DisplayName("Direction Vector Tests")
    class DirectionTests {
        
        @Test
        @DisplayName("Should calculate direction vector correctly")
        void testGetDirection() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 6, 8);
            Line3D line = new Line3D(start, end);
            
            Point3D direction = line.getDirection();
            
            assertEquals(3.0, direction.getX(), DELTA);
            assertEquals(4.0, direction.getY(), DELTA);
            assertEquals(5.0, direction.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should calculate normalized direction vector")
        void testGetNormalizedDirection() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(3, 4, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D normalized = line.getNormalizedDirection();
            
            assertEquals(0.6, normalized.getX(), DELTA);
            assertEquals(0.8, normalized.getY(), DELTA);
            assertEquals(0.0, normalized.getZ(), DELTA);
            assertEquals(1.0, normalized.magnitude(), DELTA);
        }
        
        @Test
        @DisplayName("Normalized direction should have unit length")
        void testNormalizedDirectionUnitLength() {
            Point3D start = new Point3D(5, 10, 15);
            Point3D end = new Point3D(8, 14, 20);
            Line3D line = new Line3D(start, end);
            
            Point3D normalized = line.getNormalizedDirection();
            
            assertEquals(1.0, normalized.magnitude(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Midpoint Tests")
    class MidpointTests {
        
        @Test
        @DisplayName("Should calculate midpoint correctly")
        void testGetMidpoint() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            Point3D midpoint = line.getMidpoint();
            
            assertEquals(5.0, midpoint.getX(), DELTA);
            assertEquals(10.0, midpoint.getY(), DELTA);
            assertEquals(15.0, midpoint.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should calculate midpoint for negative coordinates")
        void testGetMidpointNegative() {
            Point3D start = new Point3D(-10, -20, -30);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            Point3D midpoint = line.getMidpoint();
            
            assertEquals(0.0, midpoint.getX(), DELTA);
            assertEquals(0.0, midpoint.getY(), DELTA);
            assertEquals(0.0, midpoint.getZ(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Parametric Point Tests")
    class ParametricPointTests {
        
        @Test
        @DisplayName("Should return start point for t=0")
        void testGetPointAtParameterZero() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            Point3D point = line.getPointAtParameter(0.0);
            
            assertEquals(start, point);
        }
        
        @Test
        @DisplayName("Should return end point for t=1")
        void testGetPointAtParameterOne() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            Point3D point = line.getPointAtParameter(1.0);
            
            assertEquals(end.getX(), point.getX(), DELTA);
            assertEquals(end.getY(), point.getY(), DELTA);
            assertEquals(end.getZ(), point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should return midpoint for t=0.5")
        void testGetPointAtParameterHalf() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            Point3D point = line.getPointAtParameter(0.5);
            Point3D midpoint = line.getMidpoint();
            
            assertEquals(midpoint.getX(), point.getX(), DELTA);
            assertEquals(midpoint.getY(), point.getY(), DELTA);
            assertEquals(midpoint.getZ(), point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle t values outside [0,1]")
        void testGetPointAtParameterOutsideRange() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            // t = 2 should give point at (20, 0, 0)
            Point3D point = line.getPointAtParameter(2.0);
            assertEquals(20.0, point.getX(), DELTA);
            
            // t = -1 should give point at (-10, 0, 0)
            Point3D pointNeg = line.getPointAtParameter(-1.0);
            assertEquals(-10.0, pointNeg.getX(), DELTA);
        }
        
        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.25, 0.5, 0.75, 1.0})
        @DisplayName("Should generate points along the line for various t values")
        void testGetPointAtParameterVariousValues(double t) {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(100, 100, 100);
            Line3D line = new Line3D(start, end);
            
            Point3D point = line.getPointAtParameter(t);
            
            assertEquals(100 * t, point.getX(), DELTA);
            assertEquals(100 * t, point.getY(), DELTA);
            assertEquals(100 * t, point.getZ(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Distance to Point Tests")
    class DistanceToPointTests {
        
        @Test
        @DisplayName("Should calculate perpendicular distance to point")
        void testDistanceToPoint() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(5, 3, 0);
            double distance = line.distanceToPoint(point);
            
            assertEquals(3.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should return zero distance for point on line")
        void testDistanceToPointOnLine() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(5, 0, 0);
            double distance = line.distanceToPoint(point);
            
            assertEquals(0.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should calculate distance to endpoint when projection is outside segment")
        void testDistanceToPointOutsideSegment() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(15, 0, 0);
            double distance = line.distanceToPoint(point);
            
            assertEquals(5.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should calculate distance in 3D space")
        void testDistanceToPoint3D() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(0, 0, 10);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(3, 4, 5);
            double distance = line.distanceToPoint(point);
            
            // Distance should be sqrt(3² + 4²) = 5
            assertEquals(5.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testDistanceToPointNull() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            assertThrows(NullPointerException.class, () -> {
                line.distanceToPoint(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Closest Point Tests")
    class ClosestPointTests {
        
        @Test
        @DisplayName("Should find closest point on line segment")
        void testClosestPointTo() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(5, 5, 0);
            Point3D closest = line.closestPointTo(point);
            
            assertEquals(5.0, closest.getX(), DELTA);
            assertEquals(0.0, closest.getY(), DELTA);
            assertEquals(0.0, closest.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should return start point when closest to start")
        void testClosestPointToStart() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(-5, 5, 0);
            Point3D closest = line.closestPointTo(point);
            
            assertEquals(start, closest);
        }
        
        @Test
        @DisplayName("Should return end point when closest to end")
        void testClosestPointToEnd() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(15, 5, 0);
            Point3D closest = line.closestPointTo(point);
            
            assertEquals(end.getX(), closest.getX(), DELTA);
            assertEquals(end.getY(), closest.getY(), DELTA);
            assertEquals(end.getZ(), closest.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should return point itself when point is on line")
        void testClosestPointToPointOnLine() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Point3D point = new Point3D(5, 0, 0);
            Point3D closest = line.closestPointTo(point);
            
            assertEquals(point, closest);
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testClosestPointToNull() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            assertThrows(NullPointerException.class, () -> {
                line.closestPointTo(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Line to Line Distance Tests")
    class LineToLineDistanceTests {
        
        @Test
        @DisplayName("Should calculate distance between parallel lines")
        void testShortestDistanceToParallelLines() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 5, 0), new Point3D(10, 5, 0));
            
            double distance = line1.shortestDistanceTo(line2);
            
            assertEquals(5.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should return zero for intersecting lines")
        void testShortestDistanceToIntersectingLines() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(5, -5, 0), new Point3D(5, 5, 0));
            
            double distance = line1.shortestDistanceTo(line2);
            
            assertEquals(0.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should calculate distance between skew lines")
        void testShortestDistanceToSkewLines() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 5, 5), new Point3D(10, 5, 5));
            
            double distance = line1.shortestDistanceTo(line2);
            
            // Distance should be sqrt(5² + 5²) = sqrt(50)
            assertEquals(Math.sqrt(50), distance, DELTA);
        }
        
        @Test
        @DisplayName("Should handle endpoint distances when segments don't overlap")
        void testShortestDistanceToNonOverlappingSegments() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(5, 0, 0));
            Line3D line2 = new Line3D(new Point3D(10, 0, 0), new Point3D(15, 0, 0));
            
            double distance = line1.shortestDistanceTo(line2);
            
            assertEquals(5.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should return zero for overlapping collinear segments")
        void testShortestDistanceToOverlappingSegments() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(5, 0, 0), new Point3D(15, 0, 0));
            
            double distance = line1.shortestDistanceTo(line2);
            
            assertEquals(0.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should be commutative")
        void testShortestDistanceCommutative() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 5, 5), new Point3D(10, 5, 5));
            
            double distance1 = line1.shortestDistanceTo(line2);
            double distance2 = line2.shortestDistanceTo(line1);
            
            assertEquals(distance1, distance2, DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null line")
        void testShortestDistanceToNull() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertThrows(NullPointerException.class, () -> {
                line.shortestDistanceTo(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Parallel Line Tests")
    class ParallelTests {
        
        @Test
        @DisplayName("Should detect parallel lines")
        void testIsParallelTo() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 5, 0), new Point3D(10, 5, 0));
            
            assertTrue(line1.isParallelTo(line2));
        }
        
        @Test
        @DisplayName("Should detect anti-parallel lines")
        void testIsParallelToAntiParallel() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(10, 5, 0), new Point3D(0, 5, 0));
            
            assertTrue(line1.isParallelTo(line2));
        }
        
        @Test
        @DisplayName("Should detect non-parallel lines")
        void testIsNotParallel() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 10, 0));
            
            assertFalse(line1.isParallelTo(line2));
        }
        
        @Test
        @DisplayName("Should be reflexive (line is parallel to itself)")
        void testIsParallelToReflexive() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 5, 3));
            
            assertTrue(line.isParallelTo(line));
        }
        
        @Test
        @DisplayName("Should be symmetric")
        void testIsParallelToSymmetric() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 5, 0), new Point3D(10, 5, 0));
            
            assertTrue(line1.isParallelTo(line2));
            assertTrue(line2.isParallelTo(line1));
        }
        
        @Test
        @DisplayName("Should throw exception for null line")
        void testIsParallelToNull() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertThrows(NullPointerException.class, () -> {
                line.isParallelTo(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Perpendicular Line Tests")
    class PerpendicularTests {
        
        @Test
        @DisplayName("Should detect perpendicular lines")
        void testIsPerpendicularTo() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 10, 0));
            
            assertTrue(line1.isPerpendicularTo(line2));
        }
        
        @Test
        @DisplayName("Should detect non-perpendicular lines")
        void testIsNotPerpendicular() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 10, 0));
            
            assertFalse(line1.isPerpendicularTo(line2));
        }
        
        @Test
        @DisplayName("Should detect perpendicular lines in 3D")
        void testIsPerpendicularTo3D() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(1, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 0, 1));
            
            assertTrue(line1.isPerpendicularTo(line2));
        }
        
        @Test
        @DisplayName("Should be symmetric")
        void testIsPerpendicularToSymmetric() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 10, 0));
            
            assertTrue(line1.isPerpendicularTo(line2));
            assertTrue(line2.isPerpendicularTo(line1));
        }
        
        @Test
        @DisplayName("Should not be perpendicular to itself")
        void testIsPerpendicularToSelf() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertFalse(line.isPerpendicularTo(line));
        }
        
        @Test
        @DisplayName("Should throw exception for null line")
        void testIsPerpendicularToNull() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertThrows(NullPointerException.class, () -> {
                line.isPerpendicularTo(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Angle Calculation Tests")
    class AngleTests {
        
        @Test
        @DisplayName("Should calculate 90 degree angle")
        void testAngleTo90Degrees() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 10, 0));
            
            double angle = line1.angleTo(line2);
            
            assertEquals(Math.PI / 2, angle, DELTA);
        }
        
        @Test
        @DisplayName("Should calculate 45 degree angle")
        void testAngleTo45Degrees() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 10, 0));
            
            double angle = line1.angleTo(line2);
            
            assertEquals(Math.PI / 4, angle, DELTA);
        }
        
        @Test
        @DisplayName("Should return zero angle for parallel lines")
        void testAngleToParallel() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 5, 0), new Point3D(10, 5, 0));
            
            double angle = line1.angleTo(line2);
            
            assertEquals(0.0, angle, DELTA);
        }
        
        @Test
        @DisplayName("Should return zero angle for anti-parallel lines (acute angle)")
        void testAngleToAntiParallel() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(10, 0, 0), new Point3D(0, 0, 0));
            
            double angle = line1.angleTo(line2);
            
            // Should return acute angle (0) not obtuse (π)
            assertEquals(0.0, angle, DELTA);
        }
        
        @Test
        @DisplayName("Should be symmetric")
        void testAngleToSymmetric() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 10, 0));
            
            double angle1 = line1.angleTo(line2);
            double angle2 = line2.angleTo(line1);
            
            assertEquals(angle1, angle2, DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null line")
        void testAngleToNull() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertThrows(NullPointerException.class, () -> {
                line.angleTo(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Point Containment Tests")
    class ContainmentTests {
        
        @Test
        @DisplayName("Should contain point on line segment")
        void testContainsPoint() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Point3D point = new Point3D(5, 0, 0);
            
            assertTrue(line.containsPoint(point));
        }
        
        @Test
        @DisplayName("Should contain start point")
        void testContainsStartPoint() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            assertTrue(line.containsPoint(start));
        }
        
        @Test
        @DisplayName("Should contain end point")
        void testContainsEndPoint() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            assertTrue(line.containsPoint(end));
        }
        
        @Test
        @DisplayName("Should not contain point off line")
        void testDoesNotContainPoint() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Point3D point = new Point3D(5, 5, 0);
            
            assertFalse(line.containsPoint(point));
        }
        
        @Test
        @DisplayName("Should not contain point on line extension")
        void testDoesNotContainPointOnExtension() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Point3D point = new Point3D(15, 0, 0);
            
            assertFalse(line.containsPoint(point));
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testContainsPointNull() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertThrows(NullPointerException.class, () -> {
                line.containsPoint(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Translation Tests")
    class TranslationTests {
        
        @Test
        @DisplayName("Should translate line correctly")
        void testTranslate() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Line3D translated = line.translate(5, 5, 5);
            
            assertEquals(5.0, translated.getStart().getX(), DELTA);
            assertEquals(5.0, translated.getStart().getY(), DELTA);
            assertEquals(5.0, translated.getStart().getZ(), DELTA);
            assertEquals(15.0, translated.getEnd().getX(), DELTA);
            assertEquals(5.0, translated.getEnd().getY(), DELTA);
            assertEquals(5.0, translated.getEnd().getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should preserve length after translation")
        void testTranslatePreservesLength() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 6, 8));
            double originalLength = line.length();
            
            Line3D translated = line.translate(10, 20, 30);
            
            assertEquals(originalLength, translated.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle negative translation")
        void testTranslateNegative() {
            Line3D line = new Line3D(new Point3D(10, 10, 10), new Point3D(20, 20, 20));
            Line3D translated = line.translate(-5, -5, -5);
            
            assertEquals(5.0, translated.getStart().getX(), DELTA);
            assertEquals(5.0, translated.getStart().getY(), DELTA);
            assertEquals(5.0, translated.getStart().getZ(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Scaling Tests")
    class ScalingTests {
        
        @Test
        @DisplayName("Should scale line from start point")
        void testScale() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D line = new Line3D(start, end);
            
            Line3D scaled = line.scale(2.0);
            
            assertEquals(start, scaled.getStart());
            assertEquals(20.0, scaled.getEnd().getX(), DELTA);
            assertEquals(20.0, scaled.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should scale with non-zero start point")
        void testScaleNonZeroStart() {
            Point3D start = new Point3D(5, 5, 5);
            Point3D end = new Point3D(15, 5, 5);
            Line3D line = new Line3D(start, end);
            
            Line3D scaled = line.scale(0.5);
            
            assertEquals(start, scaled.getStart());
            assertEquals(5.0, scaled.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for zero scale factor")
        void testScaleZero() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertThrows(IllegalArgumentException.class, () -> {
                line.scale(0.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for negative scale factor")
        void testScaleNegative() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            
            assertThrows(IllegalArgumentException.class, () -> {
                line.scale(-2.0);
            });
        }
    }
    
    @Nested
    @DisplayName("Reversal Tests")
    class ReversalTests {
        
        @Test
        @DisplayName("Should reverse line direction")
        void testReverse() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            Line3D reversed = line.reverse();
            
            assertEquals(end, reversed.getStart());
            assertEquals(start, reversed.getEnd());
        }
        
        @Test
        @DisplayName("Should preserve length after reversal")
        void testReversePreservesLength() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 6, 8));
            double originalLength = line.length();
            
            Line3D reversed = line.reverse();
            
            assertEquals(originalLength, reversed.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should reverse direction vector")
        void testReverseDirection() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D reversed = line.reverse();
            
            Point3D originalDir = line.getDirection();
            Point3D reversedDir = reversed.getDirection();
            
            assertEquals(-originalDir.getX(), reversedDir.getX(), DELTA);
            assertEquals(-originalDir.getY(), reversedDir.getY(), DELTA);
            assertEquals(-originalDir.getZ(), reversedDir.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Double reversal should return equivalent line")
        void testDoubleReverse() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(10, 20, 30);
            Line3D line = new Line3D(start, end);
            
            Line3D doubleReversed = line.reverse().reverse();
            
            assertEquals(line.getStart(), doubleReversed.getStart());
            assertEquals(line.getEnd(), doubleReversed.getEnd());
        }
    }
    
    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityTests {
        
        @Test
        @DisplayName("Should be equal for same start and end points")
        void testEquals() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 5, 6);
            
            Line3D line1 = new Line3D(start, end);
            Line3D line2 = new Line3D(start, end);
            
            assertEquals(line1, line2);
        }
        
        @Test
        @DisplayName("Should be reflexive")
        void testEqualsReflexive() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 5, 6));
            
            assertEquals(line, line);
        }
        
        @Test
        @DisplayName("Should be symmetric")
        void testEqualsSymmetric() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 5, 6);
            
            Line3D line1 = new Line3D(start, end);
            Line3D line2 = new Line3D(start, end);
            
            assertEquals(line1, line2);
            assertEquals(line2, line1);
        }
        
        @Test
        @DisplayName("Should be transitive")
        void testEqualsTransitive() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 5, 6);
            
            Line3D line1 = new Line3D(start, end);
            Line3D line2 = new Line3D(start, end);
            Line3D line3 = new Line3D(start, end);
            
            assertEquals(line1, line2);
            assertEquals(line2, line3);
            assertEquals(line1, line3);
        }
        
        @Test
        @DisplayName("Should not equal null")
        void testEqualsNull() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 5, 6));
            
            assertNotEquals(null, line);
        }
        
        @Test
        @DisplayName("Should not equal different type")
        void testEqualsDifferentType() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 5, 6));
            String notALine = "not a line";
            
            assertNotEquals(line, notALine);
        }
        
        @Test
        @DisplayName("Should not equal line with different points")
        void testNotEquals() {
            Line3D line1 = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 5, 6));
            Line3D line2 = new Line3D(new Point3D(7, 8, 9), new Point3D(10, 11, 12));
            
            assertNotEquals(line1, line2);
        }
        
        @Test
        @DisplayName("Should not equal reversed line")
        void testNotEqualsReversed() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 5, 6);
            
            Line3D line1 = new Line3D(start, end);
            Line3D line2 = new Line3D(end, start);
            
            assertNotEquals(line1, line2);
        }
        
        @Test
        @DisplayName("Should have same hash code for equal lines")
        void testHashCodeConsistency() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 5, 6);
            
            Line3D line1 = new Line3D(start, end);
            Line3D line2 = new Line3D(start, end);
            
            assertEquals(line1.hashCode(), line2.hashCode());
        }
        
        @Test
        @DisplayName("Should have consistent hash code across calls")
        void testHashCodeStable() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 5, 6));
            
            int hash1 = line.hashCode();
            int hash2 = line.hashCode();
            
            assertEquals(hash1, hash2);
        }
    }
    
    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        
        @Test
        @DisplayName("Should provide readable string representation")
        void testToString() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 5, 6));
            String str = line.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("Line3D"));
        }
        
        @Test
        @DisplayName("Should include length in string representation")
        void testToStringIncludesLength() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(3, 4, 0));
            String str = line.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("5") || str.contains("5.0") || str.contains("5.00"));
        }
    }
    
    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle very short lines")
        void testVeryShortLine() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(1e-5, 1e-5, 1e-5);
            
            Line3D line = new Line3D(start, end);
            
            assertTrue(line.length() > 0);
            assertTrue(line.length() < 1e-4);
        }
        
        @Test
        @DisplayName("Should handle very long lines")
        void testVeryLongLine() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(1e10, 1e10, 1e10);
            
            Line3D line = new Line3D(start, end);
            
            assertTrue(line.length() > 1e10);
        }
        
        @Test
        @DisplayName("Should handle lines in each axis direction")
        void testAxisAlignedLines() {
            Line3D xLine = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D yLine = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 10, 0));
            Line3D zLine = new Line3D(new Point3D(0, 0, 0), new Point3D(0, 0, 10));
            
            assertEquals(10.0, xLine.length(), DELTA);
            assertEquals(10.0, yLine.length(), DELTA);
            assertEquals(10.0, zLine.length(), DELTA);
            
            assertTrue(xLine.isPerpendicularTo(yLine));
            assertTrue(yLine.isPerpendicularTo(zLine));
            assertTrue(zLine.isPerpendicularTo(xLine));
        }
        
        @Test
        @DisplayName("Should handle diagonal lines")
        void testDiagonalLine() {
            Line3D line = new Line3D(new Point3D(0, 0, 0), new Point3D(1, 1, 1));
            
            assertEquals(Math.sqrt(3), line.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should maintain precision through multiple operations")
        void testPrecisionThroughOperations() {
            Line3D line = new Line3D(new Point3D(1, 2, 3), new Point3D(4, 6, 8));
            
            Line3D result = line
                .translate(10, 20, 30)
                .scale(2.0)
                .reverse()
                .reverse();
            
            assertNotNull(result);
            assertTrue(Double.isFinite(result.length()));
        }
        
        @Test
        @DisplayName("Should handle collinear points on different segments")
        void testCollinearPoints() {
            Line3D line1 = new Line3D(new Point3D(0, 0, 0), new Point3D(10, 0, 0));
            Line3D line2 = new Line3D(new Point3D(20, 0, 0), new Point3D(30, 0, 0));
            
            assertTrue(line1.isParallelTo(line2));
            assertEquals(10.0, line1.shortestDistanceTo(line2), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {
        
        @Test
        @DisplayName("Should not modify original line on translation")
        void testTranslationImmutability() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 5, 6);
            Line3D original = new Line3D(start, end);
            
            Line3D translated = original.translate(10, 10, 10);
            
            assertEquals(start, original.getStart());
            assertEquals(end, original.getEnd());
            assertNotEquals(original, translated);
        }
        
        @Test
        @DisplayName("Should not modify original line on scaling")
        void testScalingImmutability() {
            Point3D start = new Point3D(0, 0, 0);
            Point3D end = new Point3D(10, 0, 0);
            Line3D original = new Line3D(start, end);
            
            Line3D scaled = original.scale(2.0);
            
            assertEquals(10.0, original.length(), DELTA);
            assertEquals(20.0, scaled.length(), DELTA);
        }
        
        @Test
        @DisplayName("Should not modify original line on reversal")
        void testReversalImmutability() {
            Point3D start = new Point3D(1, 2, 3);
            Point3D end = new Point3D(4, 5, 6);
            Line3D original = new Line3D(start, end);
            
            Line3D reversed = original.reverse();
            
            assertEquals(start, original.getStart());
            assertEquals(end, original.getEnd());
            assertEquals(end, reversed.getStart());
            assertEquals(start, reversed.getEnd());
        }
    }
}