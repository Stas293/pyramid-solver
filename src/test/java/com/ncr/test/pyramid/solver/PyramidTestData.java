package com.ncr.test.pyramid.solver;

/**
 * Shared test data constants for all pyramid solver tests.
 * This ensures consistency between different solver implementations.
 */
public final class PyramidTestData {
    
    // Prevent instantiation
    private PyramidTestData() {}
    
    /**
     * Sample pyramid from the problem description
     * Expected maximum path: 3 + 6 + 6 + 9 = 24
     * Path: (3,0) → (2,0) → (1,0) → (0,1)
     */
    public static final int[][] SAMPLE_DATA = {
            { 5, 9, 8, 4 },
            { 6, 4, 5, 0 },
            { 6, 7, 0, 0 },
            { 3, 0, 0, 0 }
    };
    public static final long SAMPLE_EXPECTED = 24L;
    
    /**
     * Demo pyramid with larger values
     * Expected maximum path: 23 + 36 + 87 + 207 = 353
     * Path: (3,0) → (2,0) → (1,0) → (0,1)
     */
    public static final int[][] DEMO_DATA = {
            { 59, 207, 98, 95 },
            { 87,   1, 70,  0 },
            { 36,  41,  0,  0 },
            { 23,   0,  0,  0 }
    };
    public static final long DEMO_EXPECTED = 353L;
    
    /**
     * Single element pyramid
     * Expected maximum path: 42
     */
    public static final int[][] SINGLE_ELEMENT = {
            { 42 }
    };
    public static final long SINGLE_ELEMENT_EXPECTED = 42L;
    
    /**
     * Two row pyramid
     * Expected maximum path: 5 + 20 = 25
     * Path: (1,0) → (0,1)
     */
    public static final int[][] TWO_ROW = {
            { 10, 20 },
            {  5,  0 }
    };
    public static final long TWO_ROW_EXPECTED = 25L;
    
    /**
     * Pyramid with negative values
     * Expected maximum path: -6 + (-4) + (-1) = -11
     * Path: (2,0) → (1,0) → (0,0)
     */
    public static final int[][] NEGATIVE_VALUES = {
            { -1, -2, -3 },
            { -4, -5,  0 },
            { -6,  0,  0 }
    };
    public static final long NEGATIVE_VALUES_EXPECTED = -11L;
    
    /**
     * Pyramid with all zeros
     * Expected maximum path: 0 + 0 = 0
     */
    public static final int[][] ALL_ZEROS = {
            { 0, 0 },
            { 0, 0 }
    };
    public static final long ALL_ZEROS_EXPECTED = 0L;
    
    /**
     * Small pyramid for additional testing
     * Expected maximum path: 3 + 2 = 5
     * Path: (1,0) → (0,1)
     */
    public static final int[][] SMALL_PYRAMID = {
            { 1, 2 },
            { 3, 0 }
    };
    public static final long SMALL_PYRAMID_EXPECTED = 5L;
    
    /**
     * Random seed for deterministic testing
     * When using RandomPyramidGenerator with this seed and parameters (5 rows, range 99),
     * the expected result should be 398
     */
    public static final long RANDOM_TEST_SEED = 25321L;
    public static final int RANDOM_TEST_ROWS = 5;
    public static final int RANDOM_TEST_RANGE = 99;
    public static final long RANDOM_TEST_EXPECTED = 398L;
}