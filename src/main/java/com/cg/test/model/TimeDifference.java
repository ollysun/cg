package com.cg.test.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TimeDifference implements Serializable {
    private long day;
    private long hours;
    private long minutes;
    private long seconds;
    private long difference;

    public TimeDifference() { }

    public TimeDifference(long difference, long day, long hours, long minutes, long seconds) {
        this.difference= difference;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }
}
