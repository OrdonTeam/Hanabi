package com.ordonteam.hanabi.utils

import groovy.transform.CompileStatic

@CompileStatic
class Utils {
    static <T> T removeRandom(List<T> list) {
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        return list.remove(index)
    }
}
