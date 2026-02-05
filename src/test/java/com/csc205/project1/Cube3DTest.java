// FAILS 3 TESTS

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
 * Comprehensive unit tests for the Cube3D class.
 * 
 * This test suite covers:
 * - Constructor validation and initialization
 * - Factory method creation (fromVertices, fromBounds)
 * - Geometric property calculations (volume, surface area, edge length, diagonals)
 * - Sphere calculations (circumscribed, inscribed)
 * - Vertex and edge generation
 * - Face center calculations
 * - Rotation operations (around X, Y, Z axes and arbitrary axes)
 * - Translation and scaling operations
 * - Point containment tests
 * - Cube intersection detection
 * - Axis-aligned bounding box calculations
 * - Distance to point calculations
 * - Projected area calculations
 * - Equality and hash code contracts
 * - Edge cases and boundary conditions
 * - Null pointer handling
 * - Degenerate cube cases
 * - Rotation composition and precision
 * 
 * Test Organization:
 * - Nested test classes group related functionality
 * - Parameterized tests verify behavior across multiple inputs
 * - Edge cases include zero/negative sizes, extreme rotations, and numerical limits
 * 
 * @author Generated Example
 * @version 1.0
 */
@DisplayName("Cube3D Tests")
public class Cube3DTest {
    
    private static final double EPSILON = 1e-10;
    private static final double DELTA = 1e-9; // Tolerance for floating-point comparisons
    
    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        
        @Test
        @DisplayName("Should create cube with valid center and side length")
        void testConstructor() {
            Point3D center = new Point3D(5, 5, 5);
            double sideLength = 10.0;
            
            Cube3D cube = new Cube3D(center, sideLength);
            
            assertEquals(center, cube.getCenter());
            assertEquals(sideLength, cube.getSideLength(), DELTA);
        }
        
        @Test
        @DisplayName("Should create axis-aligned cube with zero rotations")
        void testConstructorDefaultRotations() {
            Point3D center = new Point3D(0, 0, 0);
            Cube3D cube = new Cube3D(center, 5.0);
            
            assertEquals(0.0, cube.getRotationX(), DELTA);
            assertEquals(0.0, cube.getRotationY(), DELTA);
            assertEquals(0.0, cube.getRotationZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should create cube with specified rotations")
        void testConstructorWithRotations() {
            Point3D center = new Point3D(0, 0, 0);
            double rotX = Math.PI / 4;
            double rotY = Math.PI / 6;
            double rotZ = Math.PI / 3;
            
            Cube3D cube = new Cube3D(center, 5.0, rotX, rotY, rotZ);
            
            assertEquals(rotX, cube.getRotationX(), DELTA);
            assertEquals(rotY, cube.getRotationY(), DELTA);
            assertEquals(rotZ, cube.getRotationZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null center")
        void testConstructorNullCenter() {
            assertThrows(NullPointerException.class, () -> {
                new Cube3D(null, 5.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for zero side length")
        void testConstructorZeroSideLength() {
            Point3D center = new Point3D(0, 0, 0);
            
            assertThrows(IllegalArgumentException.class, () -> {
                new Cube3D(center, 0.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for negative side length")
        void testConstructorNegativeSideLength() {
            Point3D center = new Point3D(0, 0, 0);
            
            assertThrows(IllegalArgumentException.class, () -> {
                new Cube3D(center, -5.0);
            });
        }
        
        @Test
        @DisplayName("Should handle very small side length")
        void testConstructorVerySmallSideLength() {
            Point3D center = new Point3D(0, 0, 0);
            double sideLength = 1e-5;
            
            Cube3D cube = new Cube3D(center, sideLength);
            
            assertEquals(sideLength, cube.getSideLength(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle very large side length")
        void testConstructorVeryLargeSideLength() {
            Point3D center = new Point3D(0, 0, 0);
            double sideLength = 1e6;
            
            Cube3D cube = new Cube3D(center, sideLength);
            
            assertEquals(sideLength, cube.getSideLength(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {
        
        @Test
        @DisplayName("Should create cube from 8 vertices")
        void testFromVertices() {
            Point3D[] vertices = new Point3D[8];
            vertices[0] = new Point3D(0, 0, 0);
            vertices[1] = new Point3D(2, 0, 0);
            vertices[2] = new Point3D(2, 2, 0);
            vertices[3] = new Point3D(0, 2, 0);
            vertices[4] = new Point3D(0, 0, 2);
            vertices[5] = new Point3D(2, 0, 2);
            vertices[6] = new Point3D(2, 2, 2);
            vertices[7] = new Point3D(0, 2, 2);
            
            Cube3D cube = Cube3D.fromVertices(vertices);
            
            assertNotNull(cube);
            assertEquals(2.0, cube.getSideLength(), 0.1); // Approximate due to calculation
        }
        
        @Test
        @DisplayName("Should throw exception for null vertices array")
        void testFromVerticesNullArray() {
            assertThrows(NullPointerException.class, () -> {
                Cube3D.fromVertices(null);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for wrong number of vertices")
        void testFromVerticesWrongCount() {
            Point3D[] vertices = new Point3D[5];
            for (int i = 0; i < 5; i++) {
                vertices[i] = new Point3D(i, i, i);
            }
            
            assertThrows(IllegalArgumentException.class, () -> {
                Cube3D.fromVertices(vertices);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for null vertex in array")
        void testFromVerticesNullVertex() {
            Point3D[] vertices = new Point3D[8];
            for (int i = 0; i < 7; i++) {
                vertices[i] = new Point3D(i, i, i);
            }
            vertices[7] = null;
            
            assertThrows(NullPointerException.class, () -> {
                Cube3D.fromVertices(vertices);
            });
        }
        
        @Test
        @DisplayName("Should create cube from bounding box")
        void testFromBounds() {
            Point3D min = new Point3D(0, 0, 0);
            Point3D max = new Point3D(10, 10, 10);
            
            Cube3D cube = Cube3D.fromBounds(min, max);
            
            assertEquals(10.0, cube.getSideLength(), DELTA);
            assertEquals(5.0, cube.getCenter().getX(), DELTA);
            assertEquals(5.0, cube.getCenter().getY(), DELTA);
            assertEquals(5.0, cube.getCenter().getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should create cube with minimum dimension from non-cubic bounds")
        void testFromBoundsNonCubic() {
            Point3D min = new Point3D(0, 0, 0);
            Point3D max = new Point3D(10, 8, 12);
            
            Cube3D cube = Cube3D.fromBounds(min, max);
            
            assertEquals(8.0, cube.getSideLength(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null min bound")
        void testFromBoundsNullMin() {
            Point3D max = new Point3D(10, 10, 10);
            
            assertThrows(NullPointerException.class, () -> {
                Cube3D.fromBounds(null, max);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for null max bound")
        void testFromBoundsNullMax() {
            Point3D min = new Point3D(0, 0, 0);
            
            assertThrows(NullPointerException.class, () -> {
                Cube3D.fromBounds(min, null);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for invalid bounds")
        void testFromBoundsInvalid() {
            Point3D min = new Point3D(10, 10, 10);
            Point3D max = new Point3D(0, 0, 0);
            
            assertThrows(IllegalArgumentException.class, () -> {
                Cube3D.fromBounds(min, max);
            });
        }
    }
    
    @Nested
    @DisplayName("Volume Tests")
    class VolumeTests {
        
        @Test
        @DisplayName("Should calculate volume correctly")
        void testVolume() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            
            assertEquals(125.0, cube.volume(), DELTA);
        }
        
        @ParameterizedTest
        @CsvSource({
            "1.0, 1.0",
            "2.0, 8.0",
            "3.0, 27.0",
            "5.0, 125.0",
            "10.0, 1000.0"
        })
        @DisplayName("Should calculate volume for various side lengths")
        void testVolumeParameterized(double sideLength, double expectedVolume) {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), sideLength);
            
            assertEquals(expectedVolume, cube.volume(), DELTA);
        }
        
        @Test
        @DisplayName("Should calculate volume for very small cube")
        void testVolumeSmallCube() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 0.1);
            
            assertEquals(0.001, cube.volume(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Surface Area Tests")
    class SurfaceAreaTests {
        
        @Test
        @DisplayName("Should calculate surface area correctly")
        void testSurfaceArea() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 4.0);
            
            assertEquals(96.0, cube.surfaceArea(), DELTA);
        }
        
        @ParameterizedTest
        @CsvSource({
            "1.0, 6.0",
            "2.0, 24.0",
            "3.0, 54.0",
            "5.0, 150.0",
            "10.0, 600.0"
        })
        @DisplayName("Should calculate surface area for various side lengths")
        void testSurfaceAreaParameterized(double sideLength, double expectedArea) {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), sideLength);
            
            assertEquals(expectedArea, cube.surfaceArea(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Edge Length Tests")
    class EdgeLengthTests {
        
        @Test
        @DisplayName("Should calculate total edge length correctly")
        void testTotalEdgeLength() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 3.0);
            
            assertEquals(36.0, cube.totalEdgeLength(), DELTA);
        }
        
        @ParameterizedTest
        @ValueSource(doubles = {1.0, 2.0, 5.0, 10.0})
        @DisplayName("Should calculate total edge length for various side lengths")
        void testTotalEdgeLengthParameterized(double sideLength) {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), sideLength);
            
            assertEquals(12.0 * sideLength, cube.totalEdgeLength(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Diagonal Tests")
    class DiagonalTests {
        
        @Test
        @DisplayName("Should calculate space diagonal correctly")
        void testSpaceDiagonal() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 2.0);
            
            assertEquals(2.0 * Math.sqrt(3), cube.spaceDiagonal(), DELTA);
        }
        
        @ParameterizedTest
        @ValueSource(doubles = {1.0, 2.0, 5.0, 10.0})
        @DisplayName("Should calculate space diagonal for various side lengths")
        void testSpaceDiagonalParameterized(double sideLength) {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), sideLength);
            
            assertEquals(sideLength * Math.sqrt(3), cube.spaceDiagonal(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Sphere Radius Tests")
    class SphereRadiusTests {
        
        @Test
        @DisplayName("Should calculate circumscribed sphere radius correctly")
        void testCircumscribedSphereRadius() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 4.0);
            
            double expectedRadius = (4.0 * Math.sqrt(3)) / 2.0;
            assertEquals(expectedRadius, cube.circumscribedSphereRadius(), DELTA);
        }
        
        @Test
        @DisplayName("Should calculate inscribed sphere radius correctly")
        void testInscribedSphereRadius() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 6.0);
            
            assertEquals(3.0, cube.inscribedSphereRadius(), DELTA);
        }
        
        @Test
        @DisplayName("Inscribed sphere diameter should equal side length")
        void testInscribedSphereDiameter() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            assertEquals(cube.getSideLength(), cube.inscribedSphereRadius() * 2.0, DELTA);
        }
        
        @Test
        @DisplayName("Circumscribed sphere should contain all vertices")
        void testCircumscribedSphereContainsVertices() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Point3D[] vertices = cube.getVertices();
            double radius = cube.circumscribedSphereRadius();
            
            for (Point3D vertex : vertices) {
                double distance = cube.getCenter().distanceTo(vertex);
                assertEquals(radius, distance, DELTA);
            }
        }
    }
    
    @Nested
    @DisplayName("Vertex Tests")
    class VertexTests {
        
        @Test
        @DisplayName("Should generate 8 vertices")
        void testGetVerticesCount() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Point3D[] vertices = cube.getVertices();
            
            assertEquals(8, vertices.length);
        }
        
        @Test
        @DisplayName("Should generate vertices at correct distance from center")
        void testGetVerticesDistance() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D[] vertices = cube.getVertices();
            
            double expectedDistance = (10.0 * Math.sqrt(3)) / 2.0;
            
            for (Point3D vertex : vertices) {
                double distance = cube.getCenter().distanceTo(vertex);
                assertEquals(expectedDistance, distance, DELTA);
            }
        }
        
        @Test
        @DisplayName("Should generate axis-aligned vertices for unrotated cube")
        void testGetVerticesAxisAligned() {
            Cube3D cube = new Cube3D(new Point3D(5, 5, 5), 10.0);
            Point3D[] vertices = cube.getVertices();
            
            // All vertices should have coordinates that are center ± half side length
            for (Point3D vertex : vertices) {
                assertTrue(Math.abs(vertex.getX() - 5.0) - 5.0 < DELTA);
                assertTrue(Math.abs(vertex.getY() - 5.0) - 5.0 < DELTA);
                assertTrue(Math.abs(vertex.getZ() - 5.0) - 5.0 < DELTA);
            }
        }
        
        @Test
        @DisplayName("Should return non-null vertices")
        void testGetVerticesNotNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Point3D[] vertices = cube.getVertices();
            
            for (Point3D vertex : vertices) {
                assertNotNull(vertex);
            }
        }
    }
    
    @Nested
    @DisplayName("Edge Tests")
    class EdgeTests {
        
        @Test
        @DisplayName("Should generate 12 edges")
        void testGetEdgesCount() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Line3D[] edges = cube.getEdges();
            
            assertEquals(12, edges.length);
        }
        
        @Test
        @DisplayName("All edges should have same length as side length")
        void testGetEdgesLength() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 7.0);
            Line3D[] edges = cube.getEdges();
            
            for (Line3D edge : edges) {
                assertEquals(7.0, edge.length(), DELTA);
            }
        }
        
        @Test
        @DisplayName("Should return non-null edges")
        void testGetEdgesNotNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Line3D[] edges = cube.getEdges();
            
            for (Line3D edge : edges) {
                assertNotNull(edge);
            }
        }
    }
    
    @Nested
    @DisplayName("Face Center Tests")
    class FaceCenterTests {
        
        @Test
        @DisplayName("Should generate 6 face centers")
        void testGetFaceCentersCount() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Point3D[] faceCenters = cube.getFaceCenters();
            
            assertEquals(6, faceCenters.length);
        }
        
        @Test
        @DisplayName("Face centers should be at correct distance from cube center")
        void testGetFaceCentersDistance() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D[] faceCenters = cube.getFaceCenters();
            
            double expectedDistance = 5.0; // Half the side length
            
            for (Point3D faceCenter : faceCenters) {
                double distance = cube.getCenter().distanceTo(faceCenter);
                assertEquals(expectedDistance, distance, DELTA);
            }
        }
        
        @Test
        @DisplayName("Should return non-null face centers")
        void testGetFaceCentersNotNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Point3D[] faceCenters = cube.getFaceCenters();
            
            for (Point3D faceCenter : faceCenters) {
                assertNotNull(faceCenter);
            }
        }
    }
    
    @Nested
    @DisplayName("Rotation Tests")
    class RotationTests {
        
        @Test
        @DisplayName("Should rotate around X-axis")
        void testRotateX() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Cube3D rotated = cube.rotateX(Math.PI / 2);
            
            assertEquals(Math.PI / 2, rotated.getRotationX(), DELTA);
            assertEquals(0.0, rotated.getRotationY(), DELTA);
            assertEquals(0.0, rotated.getRotationZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around Y-axis")
        void testRotateY() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Cube3D rotated = cube.rotateY(Math.PI / 3);
            
            assertEquals(0.0, rotated.getRotationX(), DELTA);
            assertEquals(Math.PI / 3, rotated.getRotationY(), DELTA);
            assertEquals(0.0, rotated.getRotationZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around Z-axis")
        void testRotateZ() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Cube3D rotated = cube.rotateZ(Math.PI / 4);
            
            assertEquals(0.0, rotated.getRotationX(), DELTA);
            assertEquals(0.0, rotated.getRotationY(), DELTA);
            assertEquals(Math.PI / 4, rotated.getRotationZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should accumulate rotations")
        void testRotationAccumulation() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Cube3D rotated = cube.rotateX(Math.PI / 6).rotateY(Math.PI / 4).rotateZ(Math.PI / 3);
            
            assertEquals(Math.PI / 6, rotated.getRotationX(), DELTA);
            assertEquals(Math.PI / 4, rotated.getRotationY(), DELTA);
            assertEquals(Math.PI / 3, rotated.getRotationZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should preserve center after rotation")
        void testRotationPreservesCenter() {
            Point3D center = new Point3D(5, 10, 15);
            Cube3D cube = new Cube3D(center, 5.0);
            
            Cube3D rotated = cube.rotateX(Math.PI / 2);
            
            assertEquals(center, rotated.getCenter());
        }
        
        @Test
        @DisplayName("Should preserve side length after rotation")
        void testRotationPreservesSideLength() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 7.0);
            Cube3D rotated = cube.rotateX(Math.PI / 3).rotateY(Math.PI / 6).rotateZ(Math.PI / 4);
            
            assertEquals(7.0, rotated.getSideLength(), DELTA);
        }
        
        @Test
        @DisplayName("Should preserve volume after rotation")
        void testRotationPreservesVolume() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            double originalVolume = cube.volume();
            
            Cube3D rotated = cube.rotateX(Math.PI / 2);
            
            assertEquals(originalVolume, rotated.volume(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle full rotation (2π)")
        void testFullRotation() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Cube3D rotated = cube.rotateX(2 * Math.PI);
            
            assertEquals(2 * Math.PI, rotated.getRotationX(), DELTA);
        }
        
        @Test
        @DisplayName("Should rotate around arbitrary axis")
        void testRotateAroundAxis() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Point3D axis = new Point3D(1, 1, 1);
            
            Cube3D rotated = cube.rotateAroundAxis(axis, Math.PI / 4);
            
            assertNotNull(rotated);
            assertEquals(5.0, rotated.getSideLength(), 0.1);
        }
        
        @Test
        @DisplayName("Should throw exception for null axis")
        void testRotateAroundAxisNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            
            assertThrows(NullPointerException.class, () -> {
                cube.rotateAroundAxis(null, Math.PI / 4);
            });
        }
    }
    
    @Nested
    @DisplayName("Translation Tests")
    class TranslationTests {
        
        @Test
        @DisplayName("Should translate cube correctly")
        void testTranslate() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Cube3D translated = cube.translate(10, 20, 30);
            
            assertEquals(10.0, translated.getCenter().getX(), DELTA);
            assertEquals(20.0, translated.getCenter().getY(), DELTA);
            assertEquals(30.0, translated.getCenter().getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should preserve side length after translation")
        void testTranslatePreservesSideLength() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 7.0);
            Cube3D translated = cube.translate(5, 5, 5);
            
            assertEquals(7.0, translated.getSideLength(), DELTA);
        }
        
        @Test
        @DisplayName("Should preserve rotations after translation")
        void testTranslatePreservesRotations() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0, Math.PI / 4, Math.PI / 6, Math.PI / 3);
            Cube3D translated = cube.translate(10, 10, 10);
            
            assertEquals(Math.PI / 4, translated.getRotationX(), DELTA);
            assertEquals(Math.PI / 6, translated.getRotationY(), DELTA);
            assertEquals(Math.PI / 3, translated.getRotationZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle negative translation")
        void testTranslateNegative() {
            Cube3D cube = new Cube3D(new Point3D(10, 10, 10), 5.0);
            Cube3D translated = cube.translate(-5, -5, -5);
            
            assertEquals(5.0, translated.getCenter().getX(), DELTA);
            assertEquals(5.0, translated.getCenter().getY(), DELTA);
            assertEquals(5.0, translated.getCenter().getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle zero translation")
        void testTranslateZero() {
            Point3D center = new Point3D(5, 5, 5);
            Cube3D cube = new Cube3D(center, 5.0);
            Cube3D translated = cube.translate(0, 0, 0);
            
            assertEquals(center, translated.getCenter());
        }
    }
    
    @Nested
    @DisplayName("Scaling Tests")
    class ScalingTests {
        
        @Test
        @DisplayName("Should scale cube correctly")
        void testScale() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            Cube3D scaled = cube.scale(2.0);
            
            assertEquals(10.0, scaled.getSideLength(), DELTA);
        }
        
        @Test
        @DisplayName("Should preserve center after scaling")
        void testScalePreservesCenter() {
            Point3D center = new Point3D(5, 10, 15);
            Cube3D cube = new Cube3D(center, 5.0);
            
            Cube3D scaled = cube.scale(3.0);
            
            assertEquals(center, scaled.getCenter());
        }
        
        @Test
        @DisplayName("Should scale volume by factor cubed")
        void testScaleVolume() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            double originalVolume = cube.volume();
            
            Cube3D scaled = cube.scale(2.0);
            
            assertEquals(originalVolume * 8, scaled.volume(), DELTA);
        }
        
        @Test
        @DisplayName("Should scale surface area by factor squared")
        void testScaleSurfaceArea() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            double originalArea = cube.surfaceArea();
            
            Cube3D scaled = cube.scale(3.0);
            
            assertEquals(originalArea * 9, scaled.surfaceArea(), DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for zero scale factor")
        void testScaleZero() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            
            assertThrows(IllegalArgumentException.class, () -> {
                cube.scale(0.0);
            });
        }
        
        @Test
        @DisplayName("Should throw exception for negative scale factor")
        void testScaleNegative() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            
            assertThrows(IllegalArgumentException.class, () -> {
                cube.scale(-2.0);
            });
        }
    }
    
    @Nested
    @DisplayName("Point Containment Tests")
    class ContainmentTests {
        
        @Test
        @DisplayName("Should contain point at center")
        void testContainsPointAtCenter() {
            Point3D center = new Point3D(5, 5, 5);
            Cube3D cube = new Cube3D(center, 10.0);
            
            assertTrue(cube.containsPoint(center));
        }
        
        @Test
        @DisplayName("Should contain point inside cube")
        void testContainsPointInside() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D point = new Point3D(3, 3, 3);
            
            assertTrue(cube.containsPoint(point));
        }
        
        @Test
        @DisplayName("Should contain point on surface")
        void testContainsPointOnSurface() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D point = new Point3D(5, 0, 0); // On +X face
            
            assertTrue(cube.containsPoint(point));
        }
        
        @Test
        @DisplayName("Should contain vertices")
        void testContainsVertices() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D[] vertices = cube.getVertices();
            
            for (Point3D vertex : vertices) {
                assertTrue(cube.containsPoint(vertex), 
                    "Vertex at (" + vertex.getX() + ", " + vertex.getY() + ", " + vertex.getZ() + ") should be contained");
            }
        }
        
        @Test
        @DisplayName("Should not contain point outside cube")
        void testDoesNotContainPointOutside() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D point = new Point3D(10, 10, 10);
            
            assertFalse(cube.containsPoint(point));
        }
        
        @Test
        @DisplayName("Should not contain point far outside cube")
        void testDoesNotContainPointFarOutside() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D point = new Point3D(100, 100, 100);
            
            assertFalse(cube.containsPoint(point));
        }
        
        @Test
        @DisplayName("Should handle containment for rotated cube")
        void testContainsPointRotated() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Cube3D rotated = cube.rotateZ(Math.PI / 4);
            
            assertTrue(rotated.containsPoint(new Point3D(0, 0, 0))); // Center should still be contained
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testContainsPointNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            assertThrows(NullPointerException.class, () -> {
                cube.containsPoint(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Cube Intersection Tests")
    class IntersectionTests {
        
        @Test
        @DisplayName("Should detect intersection with overlapping cube")
        void testIntersects() {
            Cube3D cube1 = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Cube3D cube2 = new Cube3D(new Point3D(5, 0, 0), 10.0);
            
            assertTrue(cube1.intersects(cube2));
        }
        
        @Test
        @DisplayName("Should detect intersection with touching cube")
        void testIntersectsTouching() {
            Cube3D cube1 = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Cube3D cube2 = new Cube3D(new Point3D(10, 0, 0), 10.0);
            
            assertTrue(cube1.intersects(cube2)); // Conservative bounding sphere test
        }
        
        @Test
        @DisplayName("Should not detect intersection with separated cube")
        void testDoesNotIntersect() {
            Cube3D cube1 = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Cube3D cube2 = new Cube3D(new Point3D(50, 0, 0), 10.0);
            
            assertFalse(cube1.intersects(cube2));
        }
        
        @Test
        @DisplayName("Should detect intersection with contained cube")
        void testIntersectsContained() {
            Cube3D cube1 = new Cube3D(new Point3D(0, 0, 0), 20.0);
            Cube3D cube2 = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            assertTrue(cube1.intersects(cube2));
        }
        
        @Test
        @DisplayName("Intersection should be reflexive")
        void testIntersectsReflexive() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            assertTrue(cube.intersects(cube));
        }
        
        @Test
        @DisplayName("Intersection should be symmetric")
        void testIntersectsSymmetric() {
            Cube3D cube1 = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Cube3D cube2 = new Cube3D(new Point3D(5, 0, 0), 10.0);
            
            assertEquals(cube1.intersects(cube2), cube2.intersects(cube1));
        }
        
        @Test
        @DisplayName("Should throw exception for null cube")
        void testIntersectsNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            assertThrows(NullPointerException.class, () -> {
                cube.intersects(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Axis-Aligned Bounding Box Tests")
    class AABBTests {
        
        @Test
        @DisplayName("Should calculate AABB for axis-aligned cube")
        void testGetAxisAlignedBoundingBox() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D[] aabb = cube.getAxisAlignedBoundingBox();
            
            assertEquals(-5.0, aabb[0].getX(), DELTA);
            assertEquals(-5.0, aabb[0].getY(), DELTA);
            assertEquals(-5.0, aabb[0].getZ(), DELTA);
            assertEquals(5.0, aabb[1].getX(), DELTA);
            assertEquals(5.0, aabb[1].getY(), DELTA);
            assertEquals(5.0, aabb[1].getZ(), DELTA);
        }
        
        @Test
        @DisplayName("AABB should return min and max points")
        void testAABBReturnsMinMax() {
            Cube3D cube = new Cube3D(new Point3D(5, 10, 15), 10.0);
            Point3D[] aabb = cube.getAxisAlignedBoundingBox();
            
            assertEquals(2, aabb.length);
            assertNotNull(aabb[0]); // Min
            assertNotNull(aabb[1]); // Max
        }
        
        @Test
        @DisplayName("AABB min should be less than max in all dimensions")
        void testAABBMinLessThanMax() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D[] aabb = cube.getAxisAlignedBoundingBox();
            
            assertTrue(aabb[0].getX() < aabb[1].getX());
            assertTrue(aabb[0].getY() < aabb[1].getY());
            assertTrue(aabb[0].getZ() < aabb[1].getZ());
        }
        
        @Test
        @DisplayName("AABB should be larger for rotated cube")
        void testAABBRotated() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D[] aabbOriginal = cube.getAxisAlignedBoundingBox();
            
            Cube3D rotated = cube.rotateZ(Math.PI / 4);
            Point3D[] aabbRotated = rotated.getAxisAlignedBoundingBox();
            
            double originalVolume = (aabbOriginal[1].getX() - aabbOriginal[0].getX()) *
                                   (aabbOriginal[1].getY() - aabbOriginal[0].getY()) *
                                   (aabbOriginal[1].getZ() - aabbOriginal[0].getZ());
            
            double rotatedVolume = (aabbRotated[1].getX() - aabbRotated[0].getX()) *
                                  (aabbRotated[1].getY() - aabbRotated[0].getY()) *
                                  (aabbRotated[1].getZ() - aabbRotated[0].getZ());
            
            assertTrue(rotatedVolume >= originalVolume);
        }
    }
    
    @Nested
    @DisplayName("Distance to Point Tests")
    class DistanceToPointTests {
        
        @Test
        @DisplayName("Should calculate distance to point outside cube")
        void testDistanceToPoint() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D point = new Point3D(15, 0, 0);
            
            double distance = cube.distanceToPoint(point);
            
            assertEquals(10.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should return zero distance for point inside cube")
        void testDistanceToPointInside() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D point = new Point3D(2, 2, 2);
            
            double distance = cube.distanceToPoint(point);
            
            assertEquals(0.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should return zero distance for point at center")
        void testDistanceToPointAtCenter() {
            Point3D center = new Point3D(5, 5, 5);
            Cube3D cube = new Cube3D(center, 10.0);
            
            double distance = cube.distanceToPoint(center);
            
            assertEquals(0.0, distance, DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null point")
        void testDistanceToPointNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            assertThrows(NullPointerException.class, () -> {
                cube.distanceToPoint(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Projected Area Tests")
    class ProjectedAreaTests {
        
        @Test
        @DisplayName("Should calculate projected area along X-axis")
        void testProjectedAreaX() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D normal = new Point3D(1, 0, 0);
            
            double area = cube.projectedArea(normal);
            
            assertEquals(100.0, area, DELTA);
        }
        
        @Test
        @DisplayName("Should calculate projected area along Y-axis")
        void testProjectedAreaY() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D normal = new Point3D(0, 1, 0);
            
            double area = cube.projectedArea(normal);
            
            assertEquals(100.0, area, DELTA);
        }
        
        @Test
        @DisplayName("Should calculate projected area along Z-axis")
        void testProjectedAreaZ() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D normal = new Point3D(0, 0, 1);
            
            double area = cube.projectedArea(normal);
            
            assertEquals(100.0, area, DELTA);
        }
        
        @Test
        @DisplayName("Should throw exception for null normal")
        void testProjectedAreaNull() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            assertThrows(NullPointerException.class, () -> {
                cube.projectedArea(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityTests {
        
        @Test
        @DisplayName("Should be equal for same center, size, and rotations")
        void testEquals() {
            Point3D center = new Point3D(5, 5, 5);
            Cube3D cube1 = new Cube3D(center, 10.0, Math.PI / 4, Math.PI / 6, Math.PI / 3);
            Cube3D cube2 = new Cube3D(center, 10.0, Math.PI / 4, Math.PI / 6, Math.PI / 3);
            
            assertEquals(cube1, cube2);
        }
        
        @Test
        @DisplayName("Should be reflexive")
        void testEqualsReflexive() {
            Cube3D cube = new Cube3D(new Point3D(5, 5, 5), 10.0);
            
            assertEquals(cube, cube);
        }
        
        @Test
        @DisplayName("Should be symmetric")
        void testEqualsSymmetric() {
            Point3D center = new Point3D(5, 5, 5);
            Cube3D cube1 = new Cube3D(center, 10.0);
            Cube3D cube2 = new Cube3D(center, 10.0);
            
            assertEquals(cube1, cube2);
            assertEquals(cube2, cube1);
        }
        
        @Test
        @DisplayName("Should be transitive")
        void testEqualsTransitive() {
            Point3D center = new Point3D(5, 5, 5);
            Cube3D cube1 = new Cube3D(center, 10.0);
            Cube3D cube2 = new Cube3D(center, 10.0);
            Cube3D cube3 = new Cube3D(center, 10.0);
            
            assertEquals(cube1, cube2);
            assertEquals(cube2, cube3);
            assertEquals(cube1, cube3);
        }
        
        @Test
        @DisplayName("Should not equal null")
        void testEqualsNull() {
            Cube3D cube = new Cube3D(new Point3D(5, 5, 5), 10.0);
            
            assertNotEquals(null, cube);
        }
        
        @Test
        @DisplayName("Should not equal different type")
        void testEqualsDifferentType() {
            Cube3D cube = new Cube3D(new Point3D(5, 5, 5), 10.0);
            String notACube = "not a cube";
            
            assertNotEquals(cube, notACube);
        }
        
        @Test
        @DisplayName("Should not equal cube with different center")
        void testNotEqualsDifferentCenter() {
            Cube3D cube1 = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Cube3D cube2 = new Cube3D(new Point3D(5, 5, 5), 10.0);
            
            assertNotEquals(cube1, cube2);
        }
        
        @Test
        @DisplayName("Should not equal cube with different size")
        void testNotEqualsDifferentSize() {
            Point3D center = new Point3D(0, 0, 0);
            Cube3D cube1 = new Cube3D(center, 10.0);
            Cube3D cube2 = new Cube3D(center, 20.0);
            
            assertNotEquals(cube1, cube2);
        }
        
        @Test
        @DisplayName("Should not equal cube with different rotation")
        void testNotEqualsDifferentRotation() {
            Point3D center = new Point3D(0, 0, 0);
            Cube3D cube1 = new Cube3D(center, 10.0, 0, 0, 0);
            Cube3D cube2 = new Cube3D(center, 10.0, Math.PI / 4, 0, 0);
            
            assertNotEquals(cube1, cube2);
        }
        
        @Test
        @DisplayName("Should have same hash code for equal cubes")
        void testHashCodeConsistency() {
            Point3D center = new Point3D(5, 5, 5);
            Cube3D cube1 = new Cube3D(center, 10.0);
            Cube3D cube2 = new Cube3D(center, 10.0);
            
            assertEquals(cube1.hashCode(), cube2.hashCode());
        }
        
        @Test
        @DisplayName("Should have consistent hash code across calls")
        void testHashCodeStable() {
            Cube3D cube = new Cube3D(new Point3D(5, 5, 5), 10.0);
            
            int hash1 = cube.hashCode();
            int hash2 = cube.hashCode();
            
            assertEquals(hash1, hash2);
        }
    }
    
    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        
        @Test
        @DisplayName("Should provide readable string representation")
        void testToString() {
            Cube3D cube = new Cube3D(new Point3D(5, 5, 5), 10.0);
            String str = cube.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("Cube3D"));
        }
        
        @Test
        @DisplayName("Should include volume in string representation")
        void testToStringIncludesVolume() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            String str = cube.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("125") || str.contains("volume"));
        }
        
        @Test
        @DisplayName("Should include side length in string representation")
        void testToStringIncludesSideLength() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 7.0);
            String str = cube.toString();
            
            assertNotNull(str);
            assertTrue(str.contains("7") || str.contains("sideLength"));
        }
    }
    
    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Should handle very small cube")
        void testVerySmallCube() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 1e-5);
            
            assertTrue(cube.volume() > 0);
            assertTrue(cube.surfaceArea() > 0);
        }
        
        @Test
        @DisplayName("Should handle very large cube")
        void testVeryLargeCube() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 1e6);
            
            assertTrue(cube.volume() > 0);
            assertTrue(Double.isFinite(cube.volume()));
        }
        
        @Test
        @DisplayName("Should handle cube at origin")
        void testCubeAtOrigin() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Point3D[] vertices = cube.getVertices();
            
            // All vertices should be equidistant from origin
            double expectedDistance = (10.0 * Math.sqrt(3)) / 2.0;
            for (Point3D vertex : vertices) {
                assertEquals(expectedDistance, vertex.magnitude(), DELTA);
            }
        }
        
        @Test
        @DisplayName("Should handle cube far from origin")
        void testCubeFarFromOrigin() {
            Cube3D cube = new Cube3D(new Point3D(1e6, 1e6, 1e6), 10.0);
            
            assertEquals(10.0, cube.getSideLength(), DELTA);
            assertEquals(1000.0, cube.volume(), DELTA);
        }
        
        @Test
        @DisplayName("Should handle multiple sequential operations")
        void testMultipleOperations() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 5.0);
            
            Cube3D transformed = cube
                .rotateX(Math.PI / 6)
                .rotateY(Math.PI / 4)
                .rotateZ(Math.PI / 3)
                .translate(10, 20, 30)
                .scale(2.0);
            
            assertNotNull(transformed);
            assertEquals(10.0, transformed.getSideLength(), DELTA);
            assertEquals(10.0, transformed.getCenter().getX(), DELTA);
            assertEquals(20.0, transformed.getCenter().getY(), DELTA);
            assertEquals(30.0, transformed.getCenter().getZ(), DELTA);
        }
        
        @Test
        @DisplayName("Should maintain precision through complex transformations")
        void testPrecisionThroughTransformations() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            // Perform rotation and back
            Cube3D rotatedAndBack = cube
                .rotateX(Math.PI / 4)
                .rotateX(-Math.PI / 4);
            
            // Should be approximately back to original rotation
            assertEquals(0.0, rotatedAndBack.getRotationX(), 1e-6);
        }
        
        @Test
        @DisplayName("Should handle unit cube")
        void testUnitCube() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 1.0);
            
            assertEquals(1.0, cube.volume(), DELTA);
            assertEquals(6.0, cube.surfaceArea(), DELTA);
            assertEquals(12.0, cube.totalEdgeLength(), DELTA);
        }
    }
    
    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {
        
        @Test
        @DisplayName("Should not modify original cube on rotation")
        void testRotationImmutability() {
            Cube3D original = new Cube3D(new Point3D(5, 5, 5), 10.0);
            Cube3D rotated = original.rotateX(Math.PI / 2);
            
            assertEquals(0.0, original.getRotationX(), DELTA);
            assertEquals(Math.PI / 2, rotated.getRotationX(), DELTA);
        }
        
        @Test
        @DisplayName("Should not modify original cube on translation")
        void testTranslationImmutability() {
            Point3D center = new Point3D(0, 0, 0);
            Cube3D original = new Cube3D(center, 10.0);
            
            Cube3D translated = original.translate(10, 10, 10);
            
            assertEquals(center, original.getCenter());
            assertNotEquals(original.getCenter(), translated.getCenter());
        }
        
        @Test
        @DisplayName("Should not modify original cube on scaling")
        void testScalingImmutability() {
            Cube3D original = new Cube3D(new Point3D(0, 0, 0), 10.0);
            Cube3D scaled = original.scale(2.0);
            
            assertEquals(10.0, original.getSideLength(), DELTA);
            assertEquals(20.0, scaled.getSideLength(), DELTA);
        }
        
        @Test
        @DisplayName("Should return new arrays for vertices")
        void testVerticesArrayImmutability() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            Point3D[] vertices1 = cube.getVertices();
            Point3D[] vertices2 = cube.getVertices();
            
            assertNotSame(vertices1, vertices2);
        }
        
        @Test
        @DisplayName("Should return new arrays for edges")
        void testEdgesArrayImmutability() {
            Cube3D cube = new Cube3D(new Point3D(0, 0, 0), 10.0);
            
            Line3D[] edges1 = cube.getEdges();
            Line3D[] edges2 = cube.getEdges();
            
            assertNotSame(edges1, edges2);
        }
    }
}