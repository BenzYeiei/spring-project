package com.demo.firstProject.Controller.Image;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class SetName {

    private static LocalDateTime localDateTime;
    private static String newName;

    public static String getImageName(String name) {
        localDateTime = LocalDateTime.now();
        newName = localDateTime.getYear()
                + "-" + localDateTime.getMonthValue()
                + "-" + localDateTime.getDayOfMonth()
                + "T" + localDateTime.getHour()
                + "-" + localDateTime.getMinute()
                + "-" + localDateTime.getSecond()
                + "-" + localDateTime.getNano()
                + "N" + name;
        return newName;
    }

}
