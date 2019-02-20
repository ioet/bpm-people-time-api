package com.ioet.bpm.peopletime.timeevents.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeWorked {

    private String name;
    private long time;
}
