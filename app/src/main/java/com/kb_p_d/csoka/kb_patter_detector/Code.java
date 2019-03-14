package com.kb_p_d.csoka.kb_patter_detector;

public enum Code {
    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION("WRITE_EXTERNAL_STORAGE", 1),
    REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION("READ_EXTERNAL_STORAGE", 2),
    REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION("SYSTEM_ALERT_WINDOW", 3),
    STORAGE_PATH("KbPatternDetector", 4);

    private final String key;
    private final Integer value;

    Code(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
