package com.ncr.test.pyramid.solver;

import com.ncr.test.pyramid.data.Pyramid;

/**
 *  Represents the API of a problem solver.
 *  Refer to {@link Pyramid} for problem description.
 */
public interface PyramidSolver {
    long pyramidMaximumTotal(Pyramid pyramid);
}
