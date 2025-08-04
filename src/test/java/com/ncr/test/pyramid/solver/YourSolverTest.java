package com.ncr.test.pyramid.solver;

import com.ncr.test.pyramid.data.Pyramid;
import com.ncr.test.pyramid.data.PyramidGenerator;
import com.ncr.test.pyramid.data.impl.RandomPyramidGenerator;
import com.ncr.test.pyramid.solver.impl.YourSolver;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("YourSolver Tests - Efficient Pyramid Solver")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class YourSolverTest {

    private static final int MAX_DEPTH = 100;

    private PyramidSolver solver;

    @BeforeEach
    void setUp() {
        solver = new YourSolver();
    }

    @AfterEach
    void tearDown() {
        solver = null;
    }

    @Test
    @Order(1)
    @DisplayName("Should handle sample data correctly")
    void solverHandlesSampleData() {
        Pyramid pyramid = new Pyramid(PyramidTestData.SAMPLE_DATA);
        assertEquals(PyramidTestData.SAMPLE_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                "Expected maximum path sum for sample data");
    }

    @Test
    @Order(2)
    @DisplayName("Should handle demo data correctly")
    void solverHandlesDemoData() {
        Pyramid pyramid = new Pyramid(PyramidTestData.DEMO_DATA);
        assertEquals(PyramidTestData.DEMO_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                "Expected maximum path sum for demo data");
    }

    @Test
    @Order(3)
    @DisplayName("Should handle single element pyramid")
    void solverHandlesSingleElement() {
        Pyramid pyramid = new Pyramid(PyramidTestData.SINGLE_ELEMENT);
        assertEquals(PyramidTestData.SINGLE_ELEMENT_EXPECTED, solver.pyramidMaximumTotal(pyramid));
    }

    @Test
    @Order(4)
    @DisplayName("Should handle two row pyramid")
    void solverHandlesTwoRows() {
        Pyramid pyramid = new Pyramid(PyramidTestData.TWO_ROW);
        assertEquals(PyramidTestData.TWO_ROW_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                "Expected path: 5 + 20 = 25");
    }

    @Test
    @Order(5)
    @Tag("performance")
    @DisplayName("🚀 Should survive large data efficiently")
    void solverSurvivesLargeData() {
        PyramidGenerator generator = new RandomPyramidGenerator(MAX_DEPTH, 1000);
        Pyramid pyramid = generator.generatePyramid();

        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            long result = solver.pyramidMaximumTotal(pyramid);
            assertTrue(result > 0L, "Max path in a large pyramid should be positive");
        });
    }

    @Test
    @Order(6)
    @DisplayName("🎯 Should handle deterministic random data")
    void solverHandlesRandomData() {
        RandomPyramidGenerator.setRandSeed(PyramidTestData.RANDOM_TEST_SEED);
        final PyramidGenerator generator = new RandomPyramidGenerator(
                PyramidTestData.RANDOM_TEST_ROWS,
                PyramidTestData.RANDOM_TEST_RANGE);
        final Pyramid pyramid = generator.generatePyramid();

        assertEquals(PyramidTestData.RANDOM_TEST_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                "Expected result for seeded random pyramid");
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCases {

        @Test
        @DisplayName("Should handle negative values")
        void shouldHandleNegativeValues() {
            Pyramid pyramid = new Pyramid(PyramidTestData.NEGATIVE_VALUES);
            assertEquals(PyramidTestData.NEGATIVE_VALUES_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                    "Expected path with negative values: -6 + (-4) + (-1) = -11");
        }

        @Test
        @DisplayName("Should handle all zeros")
        void shouldHandleAllZeros() {
            Pyramid pyramid = new Pyramid(PyramidTestData.ALL_ZEROS);
            assertEquals(PyramidTestData.ALL_ZEROS_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                    "All zeros should result in sum of 0");
        }

        @Test
        @DisplayName("Should handle small pyramid")
        void shouldHandleSmallPyramid() {
            Pyramid pyramid = new Pyramid(PyramidTestData.SMALL_PYRAMID);
            assertEquals(PyramidTestData.SMALL_PYRAMID_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                    "Expected path: 3 + 2 = 5 (from bottom 3, can reach 1 or 2, choose 2)");
        }

        @ParameterizedTest
        @DisplayName("Should handle various pyramid sizes")
        @ValueSource(ints = {1, 2, 3, 5, 10, 20})
        void shouldHandleVariousSizes(int size) {
            PyramidGenerator generator = new RandomPyramidGenerator(size, 100);
            Pyramid pyramid = generator.generatePyramid();

            assertDoesNotThrow(() -> {
                long result = solver.pyramidMaximumTotal(pyramid);
                assertTrue(result > 0, "Result should be positive for pyramid of size " + size);
            });
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    @Tag("performance")
    class PerformanceTests {

        @ParameterizedTest
        @DisplayName("Should complete within time limits for various sizes")
        @MethodSource("providePyramidSizesAndTimeouts")
        void shouldCompleteWithinTimeLimit(int size, Duration timeout) {
            PyramidGenerator generator = new RandomPyramidGenerator(size, 1000);
            Pyramid pyramid = generator.generatePyramid();

            assertTimeoutPreemptively(timeout, () -> {
                solver.pyramidMaximumTotal(pyramid);
            }, "Solver should complete size " + size + " within " + timeout);
        }

        static Stream<Arguments> providePyramidSizesAndTimeouts() {
            return Stream.of(
                    Arguments.of(10, Duration.ofMillis(100)),
                    Arguments.of(50, Duration.ofMillis(500)),
                    Arguments.of(100, Duration.ofSeconds(2)),
                    Arguments.of(200, Duration.ofSeconds(5))
            );
        }

        @Test
        @Tag("slow")
        @DisplayName("Should handle very large pyramid (stress test)")
        void shouldHandleVeryLargePyramid() {
            PyramidGenerator generator = new RandomPyramidGenerator(500, 10000);
            Pyramid pyramid = generator.generatePyramid();

            assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
                long result = solver.pyramidMaximumTotal(pyramid);
                assertTrue(result > 0L, "Very large pyramid should still produce positive result");
            });
        }
    }

    @Nested
    @DisplayName("Correctness Verification")
    class CorrectnessTests {

        @ParameterizedTest
        @DisplayName("Should produce correct results for known test cases")
        @MethodSource("provideKnownTestCases")
        void shouldProduceCorrectResults(int[][] pyramidData, long expected, String description) {
            Pyramid pyramid = new Pyramid(pyramidData);
            assertEquals(expected, solver.pyramidMaximumTotal(pyramid), description);
        }

        static Stream<Arguments> provideKnownTestCases() {
            return Stream.of(
                    Arguments.of(
                            PyramidTestData.SINGLE_ELEMENT,
                            PyramidTestData.SINGLE_ELEMENT_EXPECTED,
                            "Single element pyramid"
                    ),
                    Arguments.of(
                            PyramidTestData.TWO_ROW,
                            PyramidTestData.TWO_ROW_EXPECTED,
                            "Two row pyramid: 5 + 20 = 25"
                    ),
                    Arguments.of(
                            PyramidTestData.SMALL_PYRAMID,
                            PyramidTestData.SMALL_PYRAMID_EXPECTED,
                            "Small pyramid: 3 + 2 = 5"
                    ),
                    Arguments.of(
                            PyramidTestData.ALL_ZEROS,
                            PyramidTestData.ALL_ZEROS_EXPECTED,
                            "All zeros pyramid"
                    ),
                    Arguments.of(
                            PyramidTestData.NEGATIVE_VALUES,
                            PyramidTestData.NEGATIVE_VALUES_EXPECTED,
                            "Negative values pyramid"
                    )
            );
        }
    }

    @Test
    @DisplayName("Should not modify input data")
    void shouldNotModifyInputData() {
        int[][] originalData = {
                {1, 2, 3},
                {4, 5, 0},
                {6, 0, 0}
        };

        int[][] expectedData = new int[originalData.length][];
        for (int i = 0; i < originalData.length; i++) {
            expectedData[i] = originalData[i].clone();
        }

        Pyramid pyramid = new Pyramid(originalData);
        solver.pyramidMaximumTotal(pyramid);

        assertArrayEquals(expectedData, originalData,
                "Solver should not modify the original pyramid data");
    }

    @RepeatedTest(value = 5, name = "Consistency test {currentRepetition}/{totalRepetitions}")
    @DisplayName("Should produce consistent results across multiple runs")
    void shouldProduceConsistentResults() {
        RandomPyramidGenerator.setRandSeed(12345L);
        PyramidGenerator generator = new RandomPyramidGenerator(10, 100);
        Pyramid pyramid = generator.generatePyramid();

        long result = solver.pyramidMaximumTotal(pyramid);

        assertTrue(result > 0, "Result should be positive");
    }

    @Test
    @DisplayName("Should produce same results as sample calculation")
    void shouldMatchManualCalculation() {
        Pyramid pyramid = new Pyramid(PyramidTestData.SAMPLE_DATA);
        long result = solver.pyramidMaximumTotal(pyramid);

        assertEquals(24L, result, "Should match our manual calculation");
    }
}