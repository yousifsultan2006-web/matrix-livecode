import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class SalamanderSearchTest {

    @Test
    public void canReach_PathExists() {
        char[][] enclosure = {
            {'.','.','.','.','.','.'},
            {'W','.','W','W','.','.'},
            {'.','.','W','.','.','W'},
            {'f','W','.','.','W','.'},
            {'W','.','W','s','.','.'},
        };
        boolean actual = SalamanderSearch.canReach(enclosure);
        assertTrue(actual);
    }

    @Test
    public void canReach_NoPath() {
        char[][] enclosure = {
            {'.','.','.','.','.','.'},
            {'W','W','W','W','s','.'},
            {'.','.','W','.','.','W'},
            {'f','W','.','.','W','.'},
            {'W','.','W','.','.','.'},
        };
        boolean actual = SalamanderSearch.canReach(enclosure);
        assertFalse(actual);
    }

    @Test
    public void testSalamanderLocation_centerOfGrid() {
        char[][] enclosure = {
            {'.', '.', '.'},
            {'.', 's', '.'},
            {'.', '.', '.'}
        };
        int[] expected = {1, 1};
        assertArrayEquals(expected, SalamanderSearch.salamanderLocation(enclosure));
    }

    @Test
    public void testSalamanderLocation_topLeftCorner() {
        char[][] enclosure = {
            {'s', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        int[] expected = {0, 0};
        assertArrayEquals(expected, SalamanderSearch.salamanderLocation(enclosure));
    }

    @Test
    public void testSalamanderLocation_notFound_throwsException() {
        char[][] enclosure = {
            {'.', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SalamanderSearch.salamanderLocation(enclosure);
        });
        assertEquals("Enclosure does not contain a salamander", exception.getMessage());
    }


    @Test
    public void testSalamanderLocation_at_1_2() {
        char[][] enclosure = {
            {'.', '.', '.'},
            {'.', '.', 's'},
            {'.', '.', '.'}
        };
        int[] expected = {1, 2};
        assertArrayEquals(expected, SalamanderSearch.salamanderLocation(enclosure));
    }

    @Test
    public void testPossibleMoves_allDirectionsOpen() {
        char[][] enclosure = {
            {'.', '.', '.'},
            {'.', 's', '.'},
            {'.', '.', '.'}
        };
        int[] location = {1, 1};
        List<int[]> moves = SalamanderSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(4, moves.size());
        assertTrue(moveSet.contains("0,1")); // up
        assertTrue(moveSet.contains("2,1")); // down
        assertTrue(moveSet.contains("1,0")); // left
        assertTrue(moveSet.contains("1,2")); // right
    }

    @Test
    public void testPossibleMoves_allDirectionsBlockedByWalls() {
        char[][] enclosure = {
            {'W', 'W', 'W'},
            {'W', 's', 'W'},
            {'W', 'W', 'W'}
        };
        int[] location = {1, 1};
        List<int[]> moves = SalamanderSearch.possibleMoves(enclosure, location);
        assertTrue(moves.isEmpty());
    }

    @Test
    public void testPossibleMoves_partialEdge() {
        char[][] enclosure = {
            {'s', '.', '.'}
        };
        int[] location = {0, 0};
        List<int[]> moves = SalamanderSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(1, moves.size());
        assertTrue(moveSet.contains("0,1")); // only right
    }

    @Test
    public void testPossibleMoves_oneOpen_twoWalls_oneEdge() {
        // Salamander at (1, 1)
        // Up is W, down is wall, left is edge (0), right is open
        char[][] enclosure = {
            {'W', 'W', 'W'},
            {'.', 's', '.'},
            {'W', 'W', 'W'}
        };
        int[] location = {1, 1};
        List<int[]> moves = SalamanderSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(2, moves.size());
        assertTrue(moveSet.contains("1,2"));
        assertTrue(moveSet.contains("1,0"));
    }

    @Test
    public void testPossibleMoves_blockedAboveByWall() {
        char[][] enclosure = {
            {'.', 'W', '.'},
            {'.', 's', '.'},
            {'.', '.', '.'}
        };
        int[] location = {1, 1};
        List<int[]> moves = SalamanderSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertFalse(moveSet.contains("0,1")); // up blocked
        assertTrue(moveSet.contains("2,1"));  // down open
        assertTrue(moveSet.contains("1,0"));  // left open
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedBelowByWall() {
        char[][] enclosure = {
            {'.', '.', '.'},
            {'.', 's', '.'},
            {'.', 'W', '.'}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(SalamanderSearch.possibleMoves(enclosure, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertFalse(moveSet.contains("2,1")); // down blocked
        assertTrue(moveSet.contains("1,0"));  // left open
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedLeftByWall() {
        char[][] enclosure = {
            {'.', '.', '.'},
            {'W', 's', '.'},
            {'.', '.', '.'}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(SalamanderSearch.possibleMoves(enclosure, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertTrue(moveSet.contains("2,1"));  // down open
        assertFalse(moveSet.contains("1,0")); // left blocked
        assertTrue(moveSet.contains("1,2"));  // right open
    }

    @Test
    public void testPossibleMoves_blockedRightByWall() {
        char[][] enclosure = {
            {'.', '.', '.'},
            {'.', 's', 'W'},
            {'.', '.', '.'}
        };
        int[] location = {1, 1};
        Set<String> moveSet = toSet(SalamanderSearch.possibleMoves(enclosure, location));

        assertTrue(moveSet.contains("0,1"));  // up open
        assertTrue(moveSet.contains("2,1"));  // down open
        assertTrue(moveSet.contains("1,0"));  // left open
        assertFalse(moveSet.contains("1,2")); // right blocked
    }

    @Test
    public void testPossibleMoves_topLeftCornerWithOneOpen() {
        char[][] enclosure = {
            {'s', 'W'},
            {'W', '.'}
        };
        int[] location = {0, 0};
        List<int[]> moves = SalamanderSearch.possibleMoves(enclosure, location);

        assertTrue(moves.isEmpty()); // surrounded by walls/edges
    }

    @Test
    public void testPossibleMoves_rightEdgeWithOpenLeft() {
        char[][] enclosure = {
            {'.', '.', 's'}
        };
        int[] location = {0, 2};
        List<int[]> moves = SalamanderSearch.possibleMoves(enclosure, location);
        Set<String> moveSet = toSet(moves);

        assertEquals(1, moves.size());
        assertTrue(moveSet.contains("0,1"));
    }


    private Set<String> toSet(List<int[]> list) {
        Set<String> set = new HashSet<>();
        for (int[] arr : list) {
            set.add(arr[0] + "," + arr[1]);
        }
        return set;
    }
}
