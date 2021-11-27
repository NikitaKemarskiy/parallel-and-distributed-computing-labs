package com.nikita.helper;

import java.util.Random;
import java.util.Set;

public class RandomHelper {
    public static <T> T randomItemFromSet(Set<T> set) {
        int size = set.size();
        int index = new Random().nextInt(size);
        int counter = 0;

        for (T item : set) {
            if (counter == index) {
                return item;
            }

            counter++;
        }

        return null;
    }
}
