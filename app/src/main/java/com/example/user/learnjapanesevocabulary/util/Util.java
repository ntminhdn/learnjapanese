package com.example.user.learnjapanesevocabulary.util;

import java.util.Random;

/**
 * Created by User on 08/03/2017.
 */

public class Util {
    public static int getRandom(int from, int to) {
        if (from < to)
            return from + new Random().nextInt(Math.abs(to - from));
        return from - new Random().nextInt(Math.abs(to - from));
    }
}
