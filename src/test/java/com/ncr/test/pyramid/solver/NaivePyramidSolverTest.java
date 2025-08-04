package com.ncr.test.pyramid.solver;

import com.ncr.test.pyramid.data.Pyramid;
import com.ncr.test.pyramid.data.PyramidGenerator;
import com.ncr.test.pyramid.data.impl.RandomPyramidGenerator;
import com.ncr.test.pyramid.solver.impl.NaivePyramidSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NaivePyramidSolver Tests")
class NaivePyramidSolverTest {

    private PyramidSolver solver;

    @BeforeEach
    void setUp() {
        solver = new NaivePyramidSolver();
    }

    @Test
    @DisplayName("Should handle sample data correctly")
    void solverHandlesSampleData() {
        Pyramid pyramid = new Pyramid(PyramidTestData.SAMPLE_DATA);
        assertEquals(PyramidTestData.SAMPLE_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                "Expected path: 3 + 6 + 6 + 9 = 24");
    }

    @Test
    @DisplayName("Should handle demo data correctly")
    void solverHandlesDemoData() {
        Pyramid pyramid = new Pyramid(PyramidTestData.DEMO_DATA);
        assertEquals(PyramidTestData.DEMO_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                "Expected path: 23 + 36 + 87 + 207 = 353");
    }

    @Test
    @DisplayName("Should handle single element pyramid")
    void solverHandlesSingleElement() {
        Pyramid pyramid = new Pyramid(PyramidTestData.SINGLE_ELEMENT);
        assertEquals(PyramidTestData.SINGLE_ELEMENT_EXPECTED, solver.pyramidMaximumTotal(pyramid));
    }

    @Test
    @DisplayName("Should handle two row pyramid")
    void solverHandlesTwoRows() {
        Pyramid pyramid = new Pyramid(PyramidTestData.TWO_ROW);
        assertEquals(PyramidTestData.TWO_ROW_EXPECTED, solver.pyramidMaximumTotal(pyramid),
                "Expected path: 5 + 20 = 25");
    }

    @Test
    @DisplayName("Should handle deterministic random data")
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
    }

    @ParameterizedTest
    @DisplayName("Should handle various known test cases")
    @MethodSource("provideKnownTestCases")
    void shouldHandleKnownTestCases(int[][] pyramidData, long expected, String description) {
        Pyramid pyramid = new Pyramid(pyramidData);
        assertEquals(expected, solver.pyramidMaximumTotal(pyramid), description);
    }

    static Stream<Arguments> provideKnownTestCases() {
        return Stream.of(
                Arguments.of(
                        PyramidTestData.SINGLE_ELEMENT,
                        PyramidTestData.SINGLE_ELEMENT_EXPECTED,
                        "Single element pyramid should return the element value"
                ),
                Arguments.of(
                        PyramidTestData.TWO_ROW,
                        PyramidTestData.TWO_ROW_EXPECTED,
                        "Two row pyramid: optimal path 5 + 20 = 25"
                ),
                Arguments.of(
                        PyramidTestData.ALL_ZEROS,
                        PyramidTestData.ALL_ZEROS_EXPECTED,
                        "All zeros pyramid should return 0"
                ),
                Arguments.of(
                        PyramidTestData.NEGATIVE_VALUES,
                        PyramidTestData.NEGATIVE_VALUES_EXPECTED,
                        "Negative values: should find least negative path"
                )
        );
    }

    @Test
    @DisplayName("Should not modify input pyramid")
    void shouldNotModifyInputPyramid() {
        int[][] originalData = {
                {1, 2},
                {3, 0}
        };
        int[][] dataCopy = {
                {1, 2},
                {3, 0}
        };

        Pyramid pyramid = new Pyramid(originalData);
        solver.pyramidMaximumTotal(pyramid);

        assertArrayEquals(dataCopy, originalData,
                "Solver should not modify the input pyramid data");
    }

    @Nested
    @DisplayName("Performance Considerations")
    class PerformanceTests {

        @Test
        @Timeout(value = 5)
        @DisplayName("Should complete small pyramids within reasonable time")
        void shouldCompleteSmallPyramidsReasonably() {
            PyramidGenerator generator = new RandomPyramidGenerator(6, 100);
            Pyramid pyramid = generator.generatePyramid();

            assertDoesNotThrow(() -> {
                long result = solver.pyramidMaximumTotal(pyramid);
                assertTrue(result > 0, "Result should be positive for positive-valued pyramid");
            });
        }

        @Test
        @DisplayName("Should handle moderate-sized pyramids (with patience)")
        void shouldHandleModeratePyramids() {
            PyramidGenerator generator = new RandomPyramidGenerator(8, 50);
            Pyramid pyramid = generator.generatePyramid();

            assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
                long result = solver.pyramidMaximumTotal(pyramid);
                assertTrue(result > 0, "Result should be positive");
            });
        }
    }

    @Test
    @DisplayName("Should produce consistent results across multiple runs")
    void shouldProduceConsistentResults() {
        Pyramid pyramid = new Pyramid(PyramidTestData.SAMPLE_DATA);

        long firstResult = solver.pyramidMaximumTotal(pyramid);
        long secondResult = solver.pyramidMaximumTotal(pyramid);
        long thirdResult = solver.pyramidMaximumTotal(pyramid);

        assertEquals(firstResult, secondResult, "Results should be consistent");
        assertEquals(secondResult, thirdResult, "Results should be consistent");
        assertEquals(PyramidTestData.SAMPLE_EXPECTED, firstResult, "Result should match expected value");
    }
}