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
 * Comprehensive unit tests for the Point3D class.
 * 
 * This test suite covers:
 * - Constructor validation and initialization
 * - Factory method creation (spherical, cylindrical coordinates)
 * - Distance calculations (Euclidean, Manhattan, magnitude)
 * - Vector operations (dot product, cross product, normalization)
 * - Rotation operations (around X, Y, Z axes)
 * - Translation and scaling operations
 * - Geometric operations (midpoint)
 * - Equality and hash code contracts
 * - Edge cases and boundary conditions
 * - Null pointer handling
 * - Numerical precision and floating-point edge cases
 * 
 * Test Organization:
 * - Nested test classes group related functionality
 * - Parameterized tests verify behavior across multiple inputs
 * - Edge cases include zero vectors, perpendicular/parallel vectors, and numerical limits
 * 
 * @author Generated Example
 * @version 1.0
 */
@DisplayName("Point3D Tests")
public class Point3DTest {
    
    private static final double EPSILON = 1e-10;
    private static final double DELTA = 1e-9; // Tolerance for floating-point comparisons
    
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create point with specified coordinates")
        void testParameterizedConstructor() {
            Point3D point = new Point3D(3.0, 4.0, 5.0);
            
            assertEquals(3.0, point.getX(), DELTA);
            assertEquals(4.0, point.getY(), DELTA);
            assertEquals(5.0, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should create point at origin with default constructor")
        void testDefaultConstructor() {
            Point3D point = new Point3D();
            
            assertEquals(0.0, point.getX(), DELTA);
            assertEquals(0.0, point.getY(), DELTA);
            assertEquals(0.0, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle negative coordinates")
        void testNegativeCoordinates() {
            Point3D point = new Point3D(-5.5, -10.3, -7.8);
            
            assertEquals(-5.5, point.getX(), DELTA);
            assertEquals(-10.3, point.getY(), DELTA);
            assertEquals(-7.8, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle very large coordinates")
        void testLargeCoordinates() {
            Point3D point = new Point3D(1e100, 1e100, 1e100);
            
            assertEquals(1e100, point.getX(), DELTA * 1e100);
            assertEquals(1e100, point.getY(), DELTA * 1e100);
            assertEquals(1e100, point.getZ(), DELTA * 1e100);
        }
        
        @Test
        @DisplayName("Should handle very small coordinates")
        void testSmallCoordinates() {
            Point3D point = new Point3D(1e-100, 1e-100, 1e-100);
            
            assertEquals(1e-100, point.getX(), DELTA);
            assertEquals(1e-100, point.getY(), DELTA);
            assertEquals(1e-100, point.getZ(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {
        
        @Test
        @DisplayName("Should create point from spherical coordinates")
        void testFromSpherical() {
            // Test case: radius=10, theta=π/4, phi=π/3
            Point3D point = Point3D.fromSpherical(10.0, Math.PI / 4, Math.PI / 3);
            
            // Expected values calculated manually
            double expectedX = 10.0 * Math.sin(Math.PI / 3) * Math.cos(Math.PI / 4);
            double expectedY = 10.0 * Math.sin(Math.PI / 3) * Math.sin(Math.PI / 4);
            double expectedZ = 10.0 * Math.cos(Math.PI / 3);
            
            assertEquals(expectedX, point.getX(), DELTA);
            assertEquals(expectedY, point.getY(), DELTA);
            assertEquals(expectedZ, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should create point at origin from spherical with zero radius")
        void testFromSphericalZeroRadius() {
            Point3D point = Point3D.fromSpherical(0.0, Math.PI / 4, Math.PI / 3);
            
            assertEquals(0.0, point.getX(), DELTA);
            assertEquals(0.0, point.getY(), DELTA);
            assertEquals(0.0, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for negative radius in spherical")
        void testFromSphericalNegativeRadius() {
            assertThrows(IllegalArgumentException.class, () -> {
                Point3D.fromSpherical(-5.0, Math.PI / 4, Math.PI / 3);
            });
        }
        
        @Test
        @DisplayName("Should create point from cylindrical coordinates")
        void testFromCylindrical() {
            // Test case: radius=5, theta=π/6, z=10
            Point3D point = Point3D.fromCylindrical(5.0, Math.PI / 6, 10.0);
            
            double expectedX = 5.0 * Math.cos(Math.PI / 6);
            double expectedY = 5.0 * Math.sin(Math.PI / 6);
            double expectedZ = 10.0;
            
            assertEquals(expectedX, point.getX(), DELTA);
            assertEquals(expectedY, point.getY(), DELTA);
            assertEquals(expectedZ, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should create point from cylindrical with zero radius")
        void testFromCylindricalZeroRadius() {
            Point3D point = Point3D.fromCylindrical(0.0, Math.PI / 4, 5.0);
            
            assertEquals(0.0, point.getX(), DELTA);
            assertEquals(0.0, point.getY(), DELTA);
            assertEquals(5.0, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for negative radius in cylindrical")
        void testFromCylindricalNegativeRadius() {
            assertThrows(IllegalArgumentException.class, () -> {
                Point3D.fromCylindrical(-3.0, Math.PI / 4, 5.0);
            });
        }
    }
    
    @Nested
    @DisplayName("Distance Calculation Tests")
    class DistanceTests {
        
        @Test
        @DisplayName("Should calculate Euclidean distance correctly")
        void testDistanceTo() {
            Point3D p1 = new Point3D(0, 0, 0);
            Point3D p2 = new Point3D(3, 4, 0);
            
            assertEquals(5.0, p1.distanceTo(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should calculate distance in 3D space")
        void testDistanceTo3D() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(4, 6, 8);
            
            // Distance = sqrt((4-1)² + (6-2)² + (8-3)²) = sqrt(9 + 16 + 25) = sqrt(50)
            assertEquals(Math.sqrt(50), p1.distanceTo(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should return zero distance for same point")
        void testDistanceToSelf() {
            Point3D point = new Point3D(5, 5, 5);
            
            assertEquals(0.0, point.distanceTo(point), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null point in distanceTo")
        void testDistanceToNull() {
            Point3D point = new Point3D(1, 2, 3);
            
            assertThrows(NullPointerException.class, () -> {
                point.distanceTo(null);
            });
        }
        
        @Test
        @DisplayName("Should calculate Manhattan distance correctly")
        void testManhattanDistance() {
            Point3D p1 = new Point3D(0, 0, 0);
            Point3D p2 = new Point3D(3, 4, 5);
            
            assertEquals(12.0, p1.manhattanDistanceTo(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should calculate Manhattan distance with negative coordinates")
        void testManhattanDistanceNegative() {
            Point3D p1 = new Point3D(-2, -3, -4);
            Point3D p2 = new Point3D(2, 3, 4);
            
            assertEquals(18.0, p1.manhattanDistanceTo(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null point in manhattanDistanceTo")
        void testManhattanDistanceToNull() {
            Point3D point = new Point3D(1, 2, 3);
            
            assertThrows(NullPointerException.class, () -> {
                point.manhattanDistanceTo(null);
            });
        }
        
        @Test
        @DisplayName("Should calculate magnitude correctly")
        void testMagnitude() {
            Point3D point = new Point3D(3, 4, 0);
            
            assertEquals(5.0, point.magnitude(), DELTA);
        }
        
        @Test
        @DisplayName("Should return zero magnitude for origin")
        void testMagnitudeOrigin() {
            Point3D point = new Point3D();
            
            assertEquals(0.0, point.magnitude(), DELTA);
        }
        
        @ParameterizedTest
        @CsvSource({
            "1, 0, 0, 1.0",
            "0, 1, 0, 1.0",
            "0, 0, 1, 1.0",
            "1, 1, 1, 1.732050808",
            "3, 4, 0, 5.0",
            "2, 2, 1, 3.0"
        })
        @DisplayName("Should calculate magnitude for various points")
        void testMagnitudeParameterized(double x, double y, double z, double expected) {
            Point3D point = new Point3D(x, y, z);
            assertEquals(expected, point.magnitude(), 1e-6);
        }
    }
    
    @Nested
    @DisplayName("Rotation Tests")
    class RotationTests {
        
        @Test
        @DisplayName("Should rotate around X-axis by 90 degrees")
        void testRotateX90Degrees() {
            Point3D point = new Point3D(0, 1, 0);
            Point3D rotated = point.rotateX(Math.PI / 2);
            
            assertEquals(0.0, rotated.getX(), DELTA);
            assertEquals(0.0, rotated.getY(), DELTA);
            assertEquals(1.0, rotated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around X-axis by 180 degrees")
        void testRotateX180Degrees() {
            Point3D point = new Point3D(1, 1, 1);
            Point3D rotated = point.rotateX(Math.PI);
            
            assertEquals(1.0, rotated.getX(), DELTA);
            assertEquals(-1.0, rotated.getY(), DELTA);
            assertEquals(-1.0, rotated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around Y-axis by 90 degrees")
        void testRotateY90Degrees() {
            Point3D point = new Point3D(1, 0, 0);
            Point3D rotated = point.rotateY(Math.PI / 2);
            
            assertEquals(0.0, rotated.getX(), DELTA);
            assertEquals(0.0, rotated.getY(), DELTA);
            assertEquals(-1.0, rotated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around Y-axis by 180 degrees")
        void testRotateY180Degrees() {
            Point3D point = new Point3D(1, 1, 1);
            Point3D rotated = point.rotateY(Math.PI);
            
            assertEquals(-1.0, rotated.getX(), DELTA);
            assertEquals(1.0, rotated.getY(), DELTA);
            assertEquals(-1.0, rotated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around Z-axis by 90 degrees")
        void testRotateZ90Degrees() {
            Point3D point = new Point3D(1, 0, 0);
            Point3D rotated = point.rotateZ(Math.PI / 2);
            
            assertEquals(0.0, rotated.getX(), DELTA);
            assertEquals(1.0, rotated.getY(), DELTA);
            assertEquals(0.0, rotated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around Z-axis by 180 degrees")
        void testRotateZ180Degrees() {
            Point3D point = new Point3D(1, 1, 1);
            Point3D rotated = point.rotateZ(Math.PI);
            
            assertEquals(-1.0, rotated.getX(), DELTA);
            assertEquals(-1.0, rotated.getY(), DELTA);
            assertEquals(1.0, rotated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should return same point for zero rotation")
        void testZeroRotation() {
            Point3D point = new Point3D(3, 4, 5);
            
            Point3D rotatedX = point.rotateX(0);
            Point3D rotatedY = point.rotateY(0);
            Point3D rotatedZ = point.rotateZ(0);
            
            assertEquals(point, rotatedX);
            assertEquals(point, rotatedY);
            assertEquals(point, rotatedZ);
        }
        
        @Test
        @DisplayName("Should handle multiple rotations (composition)")
        void testMultipleRotations() {
            Point3D point = new Point3D(1, 0, 0);
            Point3D rotated = point.rotateZ(Math.PI / 2).rotateY(Math.PI / 2);
            
            // After Z rotation: (0, 1, 0)
            // After Y rotation: (0, 1, 0) stays same since Y doesn't affect Y coordinate much
            assertEquals(0.0, rotated.getX(), DELTA);
            assertEquals(1.0, rotated.getY(), DELTA);
            assertEquals(0.0, rotated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should maintain distance from origin after rotation")
        void testRotationPreservesMagnitude() {
            Point3D point = new Point3D(3, 4, 5);
            double originalMagnitude = point.magnitude();
            
            Point3D rotatedX = point.rotateX(Math.PI / 3);
            Point3D rotatedY = point.rotateY(Math.PI / 4);
            Point3D rotatedZ = point.rotateZ(Math.PI / 6);
            
            assertEquals(originalMagnitude, rotatedX.magnitude(), DELTA);
            assertEquals(originalMagnitude, rotatedY.magnitude(), DELTA);
            assertEquals(originalMagnitude, rotatedZ.magnitude(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Translation Tests")
    class TranslationTests {
        
        @Test
        @DisplayName("Should translate point correctly")
        void testTranslate() {
            Point3D point = new Point3D(1, 2, 3);
            Point3D translated = point.translate(4, 5, 6);
            
            assertEquals(5.0, translated.getX(), DELTA);
            assertEquals(7.0, translated.getY(), DELTA);
            assertEquals(9.0, translated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle negative translation")
        void testTranslateNegative() {
            Point3D point = new Point3D(10, 10, 10);
            Point3D translated = point.translate(-5, -5, -5);
            
            assertEquals(5.0, translated.getX(), DELTA);
            assertEquals(5.0, translated.getY(), DELTA);
            assertEquals(5.0, translated.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should return same point for zero translation")
        void testTranslateZero() {
            Point3D point = new Point3D(3, 4, 5);
            Point3D translated = point.translate(0, 0, 0);
            
            assertEquals(point, translated);
        }
    }
    
    @Nested
    @DisplayName("Scaling Tests")
    class ScalingTests {
        
        @Test
        @DisplayName("Should scale point uniformly")
        void testScale() {
            Point3D point = new Point3D(1, 2, 3);
            Point3D scaled = point.scale(2, 2, 2);
            
            assertEquals(2.0, scaled.getX(), DELTA);
            assertEquals(4.0, scaled.getY(), DELTA);
            assertEquals(6.0, scaled.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should scale point non-uniformly")
        void testScaleNonUniform() {
            Point3D point = new Point3D(2, 3, 4);
            Point3D scaled = point.scale(2, 3, 0.5);
            
            assertEquals(4.0, scaled.getX(), DELTA);
            assertEquals(9.0, scaled.getY(), DELTA);
            assertEquals(2.0, scaled.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle negative scaling")
        void testScaleNegative() {
            Point3D point = new Point3D(1, 2, 3);
            Point3D scaled = point.scale(-1, -1, -1);
            
            assertEquals(-1.0, scaled.getX(), DELTA);
            assertEquals(-2.0, scaled.getY(), DELTA);
            assertEquals(-3.0, scaled.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle zero scaling")
        void testScaleZero() {
            Point3D point = new Point3D(5, 5, 5);
            Point3D scaled = point.scale(0, 0, 0);
            
            assertEquals(0.0, scaled.getX(), DELTA);
            assertEquals(0.0, scaled.getY(), DELTA);
            assertEquals(0.0, scaled.getZ(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Dot Product Tests")
    class DotProductTests {
        
        @Test
        @DisplayName("Should calculate dot product correctly")
        void testDotProduct() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(4, 5, 6);
            
            // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
            assertEquals(32.0, p1.dotProduct(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should return zero for perpendicular vectors")
        void testDotProductPerpendicular() {
            Point3D p1 = new Point3D(1, 0, 0);
            Point3D p2 = new Point3D(0, 1, 0);
            
            assertEquals(0.0, p1.dotProduct(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should return negative for opposite direction vectors")
        void testDotProductOpposite() {
            Point3D p1 = new Point3D(1, 0, 0);
            Point3D p2 = new Point3D(-1, 0, 0);
            
            assertEquals(-1.0, p1.dotProduct(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should be commutative")
        void testDotProductCommutative() {
            Point3D p1 = new Point3D(2, 3, 4);
            Point3D p2 = new Point3D(5, 6, 7);
            
            assertEquals(p1.dotProduct(p2), p2.dotProduct(p1), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testDotProductNull() {
            Point3D point = new Point3D(1, 2, 3);
            
            assertThrows(NullPointerException.class, () -> {
                point.dotProduct(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Cross Product Tests")
    class CrossProductTests {
        
        @Test
        @DisplayName("Should calculate cross product correctly")
        void testCrossProduct() {
            Point3D p1 = new Point3D(1, 0, 0);
            Point3D p2 = new Point3D(0, 1, 0);
            Point3D cross = p1.crossProduct(p2);
            
            assertEquals(0.0, cross.getX(), DELTA);
            assertEquals(0.0, cross.getY(), DELTA);
            assertEquals(1.0, cross.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should return zero vector for parallel vectors")
        void testCrossProductParallel() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(2, 4, 6);
            Point3D cross = p1.crossProduct(p2);
            
            assertEquals(0.0, cross.getX(), DELTA);
            assertEquals(0.0, cross.getY(), DELTA);
            assertEquals(0.0, cross.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should be anti-commutative")
        void testCrossProductAntiCommutative() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(4, 5, 6);
            
            Point3D cross1 = p1.crossProduct(p2);
            Point3D cross2 = p2.crossProduct(p1);
            
            assertEquals(-cross1.getX(), cross2.getX(), DELTA);
            assertEquals(-cross1.getY(), cross2.getY(), DELTA);
            assertEquals(-cross1.getZ(), cross2.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should be perpendicular to both input vectors")
        void testCrossProductPerpendicular() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(4, 5, 6);
            Point3D cross = p1.crossProduct(p2);
            
            assertEquals(0.0, cross.dotProduct(p1), DELTA);
            assertEquals(0.0, cross.dotProduct(p2), DELTA);
        }
        
        @Test
        @DisplayName("Should follow right-hand rule")
        void testCrossProductRightHandRule() {
            Point3D i = new Point3D(1, 0, 0);
            Point3D j = new Point3D(0, 1, 0);
            Point3D k = i.crossProduct(j);
            
            assertEquals(0.0, k.getX(), DELTA);
            assertEquals(0.0, k.getY(), DELTA);
            assertEquals(1.0, k.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testCrossProductNull() {
            Point3D point = new Point3D(1, 2, 3);
            
            assertThrows(NullPointerException.class, () -> {
                point.crossProduct(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Normalization Tests")
    class NormalizationTests {
        
        @Test
        @DisplayName("Should normalize vector to unit length")
        void testNormalize() {
            Point3D point = new Point3D(3, 4, 0);
            Point3D normalized = point.normalize();
            
            assertEquals(0.6, normalized.getX(), DELTA);
            assertEquals(0.8, normalized.getY(), DELTA);
            assertEquals(0.0, normalized.getZ(), DELTA);
            assertEquals(1.0, normalized.magnitude(), DELTA);
        }
        
        @Test
        @DisplayName("Should maintain direction after normalization")
        void testNormalizeDirection() {
            Point3D point = new Point3D(5, 5, 5);
            Point3D normalized = point.normalize();
            
            // Check that normalized vector is in same direction
            double ratio = point.getX() / normalized.getX();
            assertEquals(point.getY() / normalized.getY(), ratio, DELTA);
            assertEquals(point.getZ() / normalized.getZ(), ratio, DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for zero vector normalization")
        void testNormalizeZeroVector() {
            Point3D point = new Point3D(0, 0, 0);
            
            assertThrows(ArithmeticException.class, () -> {
                point.normalize();
            });
        }
        
        @Test
        @DisplayName("Should handle near-zero vector normalization")
        void testNormalizeNearZero() {
            Point3D point = new Point3D(1e-11, 1e-11, 1e-11);
            
            assertThrows(ArithmeticException.class, () -> {
                point.normalize();
            });
        }
        
        @Test
        @DisplayName("Should normalize unit vector to itself")
        void testNormalizeUnitVector() {
            Point3D point = new Point3D(1, 0, 0);
            Point3D normalized = point.normalize();
            
            assertEquals(1.0, normalized.getX(), DELTA);
            assertEquals(0.0, normalized.getY(), DELTA);
            assertEquals(0.0, normalized.getZ(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Midpoint Tests")
    class MidpointTests {
        
        @Test
        @DisplayName("Should calculate midpoint correctly")
        void testMidpoint() {
            Point3D p1 = new Point3D(0, 0, 0);
            Point3D p2 = new Point3D(10, 20, 30);
            Point3D mid = p1.midpoint(p2);
            
            assertEquals(5.0, mid.getX(), DELTA);
            assertEquals(10.0, mid.getY(), DELTA);
            assertEquals(15.0, mid.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should return same point for identical points")
        void testMidpointIdentical() {
            Point3D p1 = new Point3D(5, 5, 5);
            Point3D p2 = new Point3D(5, 5, 5);
            Point3D mid = p1.midpoint(p2);
            
            assertEquals(5.0, mid.getX(), DELTA);
            assertEquals(5.0, mid.getY(), DELTA);
            assertEquals(5.0, mid.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle negative coordinates")
        void testMidpointNegative() {
            Point3D p1 = new Point3D(-10, -20, -30);
            Point3D p2 = new Point3D(10, 20, 30);
            Point3D mid = p1.midpoint(p2);
            
            assertEquals(0.0, mid.getX(), DELTA);
            assertEquals(0.0, mid.getY(), DELTA);
            assertEquals(0.0, mid.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should be commutative")
        void testMidpointCommutative() {
            Point3D p1 = new Point3D(3, 7, 11);
            Point3D p2 = new Point3D(9, 13, 17);
            
            Point3D mid1 = p1.midpoint(p2);
            Point3D mid2 = p2.midpoint(p1);
            
            assertEquals(mid1, mid2);
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testMidpointNull() {
            Point3D point = new Point3D(1, 2, 3);
            
            assertThrows(NullPointerException.class, () -> {
                point.midpoint(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityTests {
        
        @Test
        @DisplayName("Should be equal for same coordinates")
        void testEquals() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(1, 2, 3);
            
            assertEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should be reflexive")
        void testEqualsReflexive() {
            Point3D point = new Point3D(5, 5, 5);
            
            assertEquals(point, point);
        }
        
        @Test
        @DisplayName("Should be symmetric")
        void testEqualsSymmetric() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(1, 2, 3);
            
            assertEquals(p1, p2);
            assertEquals(p2, p1);
        }
        
        @Test
        @DisplayName("Should be transitive")
        void testEqualsTransitive() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(1, 2, 3);
            Point3D p3 = new Point3D(1, 2, 3);
            
            assertEquals(p1, p2);
            assertEquals(p2, p3);
            assertEquals(p1, p3);
        }
        
        @Test
        @DisplayName("Should not equal null")
        void testEqualsNull() {
            Point3D point = new Point3D(1, 2, 3);
            
            assertNotEquals(null, point);
        }
        
        @Test
        @DisplayName("Should not equal different type")
        void testEqualsDifferentType() {
            Point3D point = new Point3D(1, 2, 3);
            String notAPoint = "not a point";
            
            assertNotEquals(point, notAPoint);
        }
        
        @Test
        @DisplayName("Should not equal point with different coordinates")
        void testNotEquals() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(4, 5, 6);
            
            assertNotEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should have same hash code for equal points")
        void testHashCodeConsistency() {
            Point3D p1 = new Point3D(1, 2, 3);
            Point3D p2 = new Point3D(1, 2, 3);
            
            assertEquals(p1.hashCode(), p2.hashCode());
        }
        
        @Test
        @DisplayName("Should have consistent hash code across calls")
        void testHashCodeStable() {
            Point3D point = new Point3D(7, 8, 9);
            int hash1 = point.hashCode();
            int hash2 = point.hashCode();
            
            assertEquals(hash1, hash2);
        }
        
        @Test
        @DisplayName("Should handle near-equal points with epsilon tolerance")
        void testEqualsWithEpsilon() {
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(1.0 + 1e-11, 2.0 + 1e-11, 3.0 + 1e-11);
            
            assertEquals(p1, p2);
        }
    }
    
    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        
        @Test
        @DisplayName("Should provide readable string representation")
        void testToString() {
            Point3D point = new Point3D(1.5, 2.7, 3.9);
            String str = point.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("1.5") || str.contains("1.50"));
            assertTrue(str.contains("2.7") || str.contains("2.70"));
            assertTrue(str.contains("3.9") || str.contains("3.90"));
        }
        
        @Test
        @DisplayName("Should handle origin in string representation")
        void testToStringOrigin() {
            Point3D point = new Point3D();
            String str = point.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("0") || str.contains("0.0") || str.contains("0.00"));
        }
        
        @Test
        @DisplayName("Should handle negative values in string representation")
        void testToStringNegative() {
            Point3D point = new Point3D(-5, -10, -15);
            String str = point.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("-5") || str.contains("-5.0") || str.contains("-5.00"));
        }
    }
    
    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle very small differences in coordinates")
        void testVerySmallDifferences() {
            Point3D p1 = new Point3D(1.0, 2.0, 3.0);
            Point3D p2 = new Point3D(1.0000000001, 2.0000000001, 3.0000000001);
            
            // Should be equal within epsilon
            assertEquals(p1, p2);
        }
        
        @Test
        @DisplayName("Should handle extreme coordinate values")
        void testExtremeValues() {
            Point3D point = new Point3D(Double.MAX_VALUE / 2, Double.MAX_VALUE / 2, Double.MAX_VALUE / 2);
            
            assertNotNull(point);
            assertTrue(point.magnitude() > 0);
        }
        
        @Test
        @DisplayName("Should handle mixed positive and negative coordinates")
        void testMixedSignCoordinates() {
            Point3D point = new Point3D(-1, 2, -3);
            
            assertEquals(-1.0, point.getX(), DELTA);
            assertEquals(2.0, point.getY(), DELTA);
            assertEquals(-3.0, point.getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle operations on axis-aligned points")
        void testAxisAlignedOperations() {
            Point3D xAxis = new Point3D(5, 0, 0);
            Point3D yAxis = new Point3D(0, 5, 0);
            Point3D zAxis = new Point3D(0, 0, 5);
            
            // All should be perpendicular to each other
            assertEquals(0.0, xAxis.dotProduct(yAxis), DELTA);
            assertEquals(0.0, yAxis.dotProduct(zAxis), DELTA);
            assertEquals(0.0, zAxis.dotProduct(xAxis), DELTA);
        }
        
        @Test
        @DisplayName("Should maintain precision through multiple operations")
        void testPrecisionThroughOperations() {
            Point3D point = new Point3D(1, 2, 3);
            
            // Perform multiple operations
            Point3D result = point
                .rotateX(Math.PI / 4)
                .rotateY(Math.PI / 6)
                .rotateZ(Math.PI / 3)
                .translate(5, 5, 5)
                .scale(2, 2, 2);
            
            // Should still be a valid point
            assertNotNull(result);
            assertTrue(Double.isFinite(result.getX()));
            assertTrue(Double.isFinite(result.getY()));
            assertTrue(Double.isFinite(result.getZ()));
        }
        
        @Test
        @DisplayName("Should handle 360 degree rotation (full circle)")
        void testFullCircleRotation() {
            Point3D point = new Point3D(3, 4, 5);
            Point3D rotated = point.rotateX(2 * Math.PI);
            
            // Should return to approximately the same position
            assertEquals(point.getX(), rotated.getX(), 1e-6);
            assertEquals(point.getY(), rotated.getY(), 1e-6);
            assertEquals(point.getZ(), rotated.getZ(), 1e-6);
        }
    }
    
    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {
        
        @Test
        @DisplayName("Should not modify original point on rotation")
        void testRotationImmutability() {
            Point3D original = new Point3D(1, 2, 3);
            Point3D rotated = original.rotateX(Math.PI / 2);
            
            // Original should be unchanged
            assertEquals(1.0, original.getX(), DELTA);
            assertEquals(2.0, original.getY(), DELTA);
            assertEquals(3.0, original.getZ(), DELTA);
            
            // Rotated should be different
            assertNotEquals(original, rotated);
        }
        
        @Test
        @DisplayName("Should not modify original point on translation")
        void testTranslationImmutability() {
            Point3D original = new Point3D(1, 2, 3);
            Point3D translated = original.translate(5, 5, 5);
            
            assertEquals(1.0, original.getX(), DELTA);
            assertEquals(2.0, original.getY(), DELTA);
            assertEquals(3.0, original.getZ(), DELTA);
            
            assertNotEquals(original, translated);
        }
        
        @Test
        @DisplayName("Should not modify original point on scaling")
        void testScalingImmutability() {
            Point3D original = new Point3D(1, 2, 3);
            Point3D scaled = original.scale(2, 2, 2);
            
            assertEquals(1.0, original.getX(), DELTA);
            assertEquals(2.0, original.getY(), DELTA);
            assertEquals(3.0, original.getZ(), DELTA);
            
            assertNotEquals(original, scaled);
        }
        
        @Test
        @DisplayName("Should not modify original point on normalization")
        void testNormalizationImmutability() {
            Point3D original = new Point3D(3, 4, 0);
            Point3D normalized = original.normalize();
            
            assertEquals(3.0, original.getX(), DELTA);
            assertEquals(4.0, original.getY(), DELTA);
            assertEquals(0.0, original.getZ(), DELTA);
            
            assertNotEquals(original, normalized);
        }
    }
}