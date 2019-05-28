package com.sunchs.lyt.hospital.enums;

public enum OfficeTypeEnum {

    /**
     * 门诊科室
     */
    Outpatient(1, "门诊科室"),

    /**
     * 住院科室
     */
    Inpatient(2, "住院科室"),

    /**
     * 特殊科室
     */
    Special(4, "特殊科室");

    public int value;
    public String title;

    OfficeTypeEnum(int value, String title) {
        this.value = value;
        this.title = title;
    }
}
