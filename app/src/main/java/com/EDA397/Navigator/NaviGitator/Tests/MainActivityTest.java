package com.EDA397.Navigator.NaviGitator.Tests;

import android.test.InstrumentationTestCase;

/**
 * Created by sajfer on 2014-04-03.
 */
public class MainActivityTest extends InstrumentationTestCase {

    public void testFirst() throws Exception {
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected, reality);
    }
    public void testSecond() throws Exception {
        final int a = 2;
        final int b = 2;
        assertEquals(a, b);
    }
}
