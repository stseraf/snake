package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerPrinter;
import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Printer;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sanja on 16.02.14.
 */
public class LevelBuilderTest {

    private Dice dice;
    private LevelBuilder builder;
    private final int SIZE = 9;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    @Test
    public void testGetMask() throws Exception {
        builder = new LevelBuilder(4, dice);

        when(dice.next(9)).thenReturn(0,0, 1,2, 3,5, 8,8);

        builder.build();

        assertM("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼???☼???☼?? ☼" +
                "☼???☼???☼???☼" +
                "☼???☼???☼???☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼???☼ ??☼???☼" +
                "☼???☼???☼???☼" +
                "☼???☼???☼???☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼? ?☼???☼???☼" +
                "☼???☼???☼???☼" +
                "☼ ??☼???☼???☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void testGetBoard() throws Exception {
        builder = new LevelBuilder(0, dice);

        builder.build();

        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertA(builder.getBoard());
    }

    private void assertA(String board) {
        board = board.replaceAll("☼", "");
        int[][] arr = new int[SIZE][SIZE];

        LengthToXY xy = new LengthToXY(SIZE);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                arr[x][y] = Integer.valueOf("" + board.charAt(xy.getLength(x, y)));
            }
        }

        for (int i = 0; i < SIZE; i++) {
            assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", getY(i, arr).toString());
            assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", getX(i, arr).toString());
        }

        for (int tx = 0; tx <= 2; tx++) {
            for (int ty = 0; ty <= 2; ty++) {
                assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", getC(tx, ty, arr).toString());
            }
        }
    }

    public List<Integer> getY(int y, int[][] arr) {
        List<Integer> result = new LinkedList<Integer>();

        for (int x = 0; x < SIZE; x++) {
            result.add(arr[x][y]);
        }

        Collections.sort(result);
        return result;
    }

    public List<Integer> getX(int x, int[][] arr) {
        List<Integer> result = new LinkedList<Integer>();

        for (int y = 0; y < SIZE; y++) {
            result.add(arr[x][y]);
        }

        Collections.sort(result);
        return result;
    }

    public List<Integer> getC(int tx, int ty, int[][] arr) {
        List<Integer> result = new LinkedList<Integer>();

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int x = 3*tx + dx + 1;
                int y = 3*ty + dy + 1;

                result.add(arr[x][y]);
            }
        }

        Collections.sort(result);
        return result;
    }

    private void assertM(String expected) {
        assertEquals(LoderunnerTest.injectN(expected), LoderunnerTest.injectN(builder.getMask()));
    }

    private void assertB(String expected) {
        assertEquals(LoderunnerTest.injectN(expected), LoderunnerTest.injectN(builder.getBoard()));
    }
}
