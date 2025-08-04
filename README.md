# Pyramid Maximum Path Sum

A Java implementation of algorithms to find the maximum path sum in an inverted pyramid structure, featuring both naive
recursive and optimized dynamic programming solutions.

## Problem Description

Find the maximum sum path from the bottom to the top in an inverted pyramid structure.

### Example

```
Input Pyramid:
    [5] [9] [8] [4]
      [6] [4] [5]
        [6] [7]
          [3]

Maximum Path: 3 → 6 → 6 → 9 = 24
```

### Movement Rules

- Start at the bottom of the pyramid (single element)
- In each step, move to one of two adjacent fields above:
    - From position `(row, col)` you can move to `(row-1, col)` or `(row-1, col+1)`
- Sum all values visited until reaching the top row
- Find the maximum possible sum among all valid paths

## Project Structure

```
src/
├── main/java/com/ncr/test/pyramid/
│   ├── OurProgram.java 
│   ├── YourProgram.java 
│   ├── data/
│   │   ├── Pyramid.java   
│   │   ├── PyramidGenerator.java 
│   │   └── impl/
│   │       └── RandomPyramidGenerator.java
│   ├── solver/
│   │   ├── PyramidSolver.java  
│   │   └── impl/
│   │       ├── NaivePyramidSolver.java      # O(2^n) recursive solution
│   │       └── YourSolver.java              # O(n²) DP solution
│   └── utils/
│       └── Util.java 
└── test/java/com/ncr/test/pyramid/
    └── solver/
        ├── PyramidTestData.java 
        ├── NaivePyramidSolverTest.java
        └── YourSolverTest.java
```

## Quick Start

### Prerequisites

- Java 17+
- Gradle 7.3+

### Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd pyramid-solver

# Build the project
./gradlew build

# Run the naive solver demo
./gradlew run

# Run tests
./gradlew test

# Run specific test suites
./gradlew test --tests "*NaivePyramidSolverTest"
./gradlew test --tests "*YourSolverTest"

# Run only fast tests (exclude slow stress tests)
./gradlew test -Dgroups="!slow"
```

## Algorithm Implementations

### 1. NaivePyramidSolver (Recursive)

**Approach:** Depth-first recursive exploration of all possible paths.

```java
public long pyramidMaximumTotal(Pyramid pyramid) {
    return getTotalAbove(pyramid.getRows() - 1, 0, pyramid);
}

private long getTotalAbove(int row, int column, Pyramid pyramid) {
    // Include the value at the top row instead of returning 0
    if (row == 0) return pyramid.get(row, column);

    int myValue = pyramid.get(row, column);
    long left = myValue + getTotalAbove(row - 1, column, pyramid);
    long right = myValue + getTotalAbove(row - 1, column + 1, pyramid);
    return Math.max(left, right);
}
```

**Characteristics:**

- **Time Complexity:** O(2^n) - exponential
- **Space Complexity:** O(n) - recursion stack

### 2. YourSolver (Dynamic Programming)

**Approach:** Bottom-up dynamic programming with memoization (All the descriptions and documentation is in com.ncr.test.pyramid.solver.impl.YourSolver)

```java
@Override
public long pyramidMaximumTotal(Pyramid pyramid) {
    int rows = pyramid.getRows();
    int[][] data = pyramid.getData();
    
    long[][] dp = new long[rows][rows];

    dp[rows - 1][0] = data[rows - 1][0];
    
    for (int row = rows - 2; row >= 0; row--) {
        for (int col = 0; col < rows - row; col++) {
            long currentValue = data[row][col];
            
            long maxFromBelow = calculateMaxFromBelow(rows, row, col, dp);

            dp[row][col] = currentValue + maxFromBelow;
        }
    }
    
    return Arrays.stream(dp[0])
            .limit(rows)
            .max()
            .orElse(0L);
}

private static long calculateMaxFromBelow(int rows, int row, int col, long[][] dp) {
    long maxFromBelow = Long.MIN_VALUE;

    int nextRowLength = rows - (row + 1);
    
    if (col > 0 && col - 1 < nextRowLength) {
        maxFromBelow = Math.max(maxFromBelow, dp[row + 1][col - 1]);
    }
    
    if (col < nextRowLength) {
        maxFromBelow = Math.max(maxFromBelow, dp[row + 1][col]);
    }
    return maxFromBelow;
}
```

**Characteristics:**

- **Time Complexity:** O(n²) - polynomial
- **Space Complexity:** O(n²) - DP table

### Movement Rules

**Forward Movement (Problem Perspective):**

```
From (row, col) → (row-1, col) OR (row-1, col+1)
```

**DP Dependencies (Algorithm Perspective):**

```
To reach (row, col) ← (row+1, col-1) OR (row+1, col)
```

## Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run performance tests only
./gradlew test --tests "*PerformanceTests"

# Skip slow stress tests
./gradlew test -Dgroups="!slow"

# Verbose test output
./gradlew test --info
```

### Test Features

- **JUnit 5** with modern testing features
- **Parameterized tests** for comprehensive coverage
- **Performance tests** with timeout assertions
- **Stress tests** for large datasets
- **Edge case validation** including negative values and zeros
- **Input integrity tests** ensuring solvers don't modify input data

## Development

### Building from Source

```bash
# Development build
./gradlew compileJava

# Clean and rebuild
./gradlew clean build
```

### Project Requirements Completed

This implementation addresses all original tasks:

#### Task 1: Project Revival

- Fixed and modernized `build.gradle` with JUnit 5
- Updated to Java 17 with modern dependencies
- Ensured project builds and runs correctly

#### Task 2: NaivePyramidSolver Analysis & Fix

- **Bug identified and fixed:** Recursion termination condition returned 0 instead of including top row value
- **Root cause:** `if (row == 0) return 0;` should be `if (row == 0) return pyramid.get(row, col);`
- **Evidence:** Comprehensive test suite proving correctness
- **Comments:** Detailed explanations of the fix and algorithm behavior

#### Task 3: Efficient YourSolver Implementation

- **Algorithm:** Bottom-up dynamic programming
- **Performance:** O(n²) time and space complexity
- **Features:** Handles large datasets (500+ rows) efficiently
- **Validation:** Passes all test cases including stress tests
- **Comments:** Extensive documentation explaining the DP approach