package com.ncr.test.pyramid.data.impl;

import com.ncr.test.pyramid.data.Pyramid;
import com.ncr.test.pyramid.data.PyramidGenerator;

import java.util.Optional;
import java.util.Random;

/**
 * An example pyramid factory.
 */
public class RandomPyramidGenerator implements PyramidGenerator {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<Long> RND_SEED = Optional.empty();
    public  static void setRandSeed(long seed) {
        RND_SEED = Optional.of(seed);
    }

    private final int rows;
    private final int range;
    private final Random random = RND_SEED.map(Random::new).orElseGet(() -> {
        long time = System.currentTimeMillis();
        RND_SEED = Optional.of(time);
        return new Random(time);
    });

    public RandomPyramidGenerator(int rows, int range) {
        this.rows = rows;
        this.range = range;
    }

    @Override
    public Pyramid generatePyramid() {

        final int[][] data = new int[rows][rows];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < rows - row; col++) {
                data[row][col] = random.nextInt(range) + 1;
            }
        }
        return new Pyramid(data);
    }
}
