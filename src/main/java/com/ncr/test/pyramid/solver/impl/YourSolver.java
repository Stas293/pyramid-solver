package com.ncr.test.pyramid.solver.impl;

import com.ncr.test.pyramid.data.Pyramid;
import com.ncr.test.pyramid.solver.PyramidSolver;

import java.util.Arrays;

/**
 * EFFICIENT IMPLEMENTATION: Bottom-up Dynamic Programming
 * <p>
 * Algorithm Overview:
 * This solver uses dynamic programming with a bottom-up approach to find the maximum
 * path sum from the bottom to any position in the top row of an inverted pyramid.
 * <p>
 * Time Complexity: O(n²) where n is the number of rows
 * - We visit each position in the pyramid exactly once
 * - Total positions = n + (n-1) + (n-2) + ... + 1 = n(n+1)/2 = O(n²)
 * <p>
 * Space Complexity: O(n²) for the DP memoization table
 * - Could be optimized to O(n) by only keeping previous row, but O(n²) is clearer
 * <p>
 * Movement Rules:
 * - Forward (bottom to top): From position (row, col) you can move to (row-1, col) or (row-1, col+1)
 * - DP perspective (reverse): To reach (row, col) you can come from (row+1, col-1) or (row+1, col)
 * <p>
 * Core DP Principle:
 * dp[row][col] = maximum sum to reach position (row, col) starting from the bottom
 * <p>
 * Example Pyramid:
 *     [5] [9] [8] [4]     dp: [24] [24] [23] [19]
 *       [6] [4] [5]    =>      [15] [14] [15]  
 *         [6] [7]               [9]  [10]
 *           [3]                   [3]
 * <p>
 * Optimal path: 3 → 6 → 6 → 9 = 24
 */
public class YourSolver implements PyramidSolver {

    @Override
    public long pyramidMaximumTotal(Pyramid pyramid) {
        int rows = pyramid.getRows();
        int[][] data = pyramid.getData();

        // Create DP table: dp[i][j] = maximum sum to reach position (i,j) from bottom
        // We allocate rows×rows to handle the triangular structure simply
        long[][] dp = new long[rows][rows];

        // Initialize base cases (smallest subproblems we can solve directly)
        // Base case: The bottom row contains exactly one element, located at position (rows-1, 0)
        // Since we start our path from the bottom, the maximum sum to reach the bottom element
        // from itself is simply the value of that element
        dp[rows - 1][0] = data[rows - 1][0];

        // Build solution bottom-up using previously computed results
        // Process each row from second-to-bottom moving up to the top
        // We start from rows-2 because rows-1 (bottom) is already handled as base case
        for (int row = rows - 2; row >= 0; row--) {
            // Process each valid position in the current row
            // Row i has (rows - i) valid positions: indices 0, 1, 2, ..., (rows-i-1)
            for (int col = 0; col < rows - row; col++) {
                long currentValue = data[row][col];

                // CORE DP RECURRENCE RELATION:
                // To find maximum sum to reach (row, col), we need to consider all
                // positions from which we can reach (row, col) and pick the best
                long maxFromBelow = calculateMaxFromBelow(rows, row, col, dp);

                dp[row][col] = currentValue + maxFromBelow;
            }
        }

        // Extract final answer from computed DP table
        // The answer is the maximum value among all positions in the top row
        // since we can end our path at any position in the top row
        return Arrays.stream(dp[0])
                .limit(rows)
                .max()
                .orElse(0L);
    }

    /**
     * Calculate the maximum sum from positions below that can reach the current position.
     * <p>
     * Movement rule analysis:
     * - To reach position (row, col), we can come from two possible positions:
     * 1. (row+1, col-1) - diagonal path from below-left
     * 2. (row+1, col)   - straight path from directly below
     * <p>
     * Boundary conditions:
     * - Position (row+1, col-1) exists only if col > 0 and col-1 is valid in next row
     * - Position (row+1, col) exists only if col is valid in next row
     * - Next row has (rows - (row+1)) = (rows - row - 1) valid positions
     *
     * @param dp   The memoization table containing previously computed maximum sums
     * @param row  Current row position (0-indexed from top)
     * @param col  Current column position (0-indexed from left)
     * @param rows Total number of rows in pyramid
     * @return Maximum sum from positions below that can reach current position
     */
    private static long calculateMaxFromBelow(int rows, int row, int col, long[][] dp) {
        long maxFromBelow = Long.MIN_VALUE;

        int nextRowLength = rows - (row + 1);

        // OPTION 1: Path from diagonal below-left position (nextRow, col-1)
        // This represents moving from (nextRow, col-1) to (row, col) via rule (row-1, col+1)
        if (col > 0 && col - 1 < nextRowLength) {
            maxFromBelow = Math.max(maxFromBelow, dp[row + 1][col - 1]);
        }

        // OPTION 2: Path from directly below position (nextRow, col)
        // This represents moving from (nextRow, col) to (row, col) via rule (row-1, col)
        if (col < nextRowLength) {
            maxFromBelow = Math.max(maxFromBelow, dp[row + 1][col]);
        }
        return maxFromBelow;
    }
}