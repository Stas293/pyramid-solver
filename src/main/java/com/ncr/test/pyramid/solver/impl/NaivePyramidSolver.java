package com.ncr.test.pyramid.solver.impl;

import com.ncr.test.pyramid.data.Pyramid;
import com.ncr.test.pyramid.solver.PyramidSolver;

/**
 * FIXED: The main issue was in the recursion termination condition.
 * When the algorithm reached the top row (row == 0), it was returning 0
 * instead of including the actual value at that position in the path sum.
 */
public class NaivePyramidSolver implements PyramidSolver {
    @Override
    public long pyramidMaximumTotal(Pyramid pyramid) {
        return getTotalAbove(pyramid.getRows() - 1, 0, pyramid);
    }

    private long getTotalAbove(int row, int column, Pyramid pyramid) {
        // FIXED: Include the value at the top row instead of returning 0
        // This was the main bug - the top row value was being ignored
        if (row == 0) return pyramid.get(row, column);

        int myValue = pyramid.get(row, column);
        long left = myValue + getTotalAbove(row - 1, column, pyramid);
        long right = myValue + getTotalAbove(row - 1, column + 1, pyramid);
        return Math.max(left, right);
    }
}