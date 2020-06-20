package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TimeSlotTypeEnum {
    /*MON_1_2(1, 1, 2),
    MON_3_5(1, 3, 5),
    MON_6_8(1, 6, 8),
    MON_9_10(1, 3, 5),
    MON_11_13(1, 11, 13),

    TUE_1_2(1, 1, 2),
    TUE_3_5(1, 3, 5),
    TUE_6_8(1, 6, 8),
    TUE_9_10(1, 3, 5),
    TUE_11_13(1, 11, 13),

    WED_1_2(1, 1, 2),
    WED_3_5(1, 3, 5),
    WED_6_8(1, 6, 8),
    WED_9_10(1, 3, 5),
    WED_11_13(1, 11, 13),

    THU_1_2(1, 1, 2),
    THU_3_5(1, 3, 5),
    THU_6_8(1, 6, 8),
    THU_9_10(1, 3, 5),
    THU_11_13(1, 11, 13),

    FRI_1_2(1, 1, 2),
    FRI_3_5(1, 3, 5),
    FRI_6_8(1, 6, 8),
    FRI_9_10(1, 3, 5),
    FRI_11_13(1, 11, 13),

    SAT_1_2(1, 1, 2),
    SAT_3_5(1, 3, 5),
    SAT_6_8(1, 6, 8),
    SAT_9_10(1, 3, 5),
    SAT_11_13(1, 11, 13),

    SUN_1_2(1, 1, 2),
    SUN_3_5(1, 3, 5),
    SUN_6_8(1, 6, 8),
    SUN_9_10(1, 3, 5),
    SUN_11_13(1, 11, 13);*/
	
	MON_1_2(1, 1, 2),
    MON_3_5(1, 3, 5),
    MON_6_8(1, 6, 8),
    MON_9_10(1, 9, 10),
    MON_11_13(1, 11, 13),
    
    TUE_1_2(2, 1, 2),
    TUE_3_5(2, 3, 5),
    TUE_6_8(2, 6, 8),
    TUE_9_10(2, 9, 10),
    TUE_11_13(2, 11, 13),

    WED_1_2(3, 1, 2),
    WED_3_5(3, 3, 5),
    WED_6_8(3, 6, 8),
    WED_9_10(3, 9, 10),
    WED_11_13(3, 11, 13),

    THU_1_2(4, 1, 2),
    THU_3_5(4, 3, 5),
    THU_6_8(4, 6, 8),
    THU_9_10(4, 9, 10),
    THU_11_13(4, 11, 13),

    FRI_1_2(5, 1, 2),
    FRI_3_5(5, 3, 5),
    FRI_6_8(5, 6, 8),
    FRI_9_10(5, 9, 10),
    FRI_11_13(5, 11, 13),

    SAT_1_2(6, 1, 2),
    SAT_3_5(6, 3, 5),
    SAT_6_8(6, 6, 8),
    SAT_9_10(6, 9, 10),
    SAT_11_13(6, 11, 13),

    SUN_1_2(7, 1, 2),
    SUN_3_5(7, 3, 5),
    SUN_6_8(7, 6, 8),
    SUN_9_10(7, 9, 10),
    SUN_11_13(7, 11, 13);

    private int dayOfWeek;
    private int start;
    private int end;

    TimeSlotTypeEnum(int dayOfWeek, int start, int end) {
        this.dayOfWeek = dayOfWeek;
        this.start = start;
        this.end = end;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSize() {
        return end - start + 1;
    }
}
