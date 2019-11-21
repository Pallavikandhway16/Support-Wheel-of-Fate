package com.airasia.android.Data;

import java.util.LinkedHashMap;
import java.util.List;

public class EngineersList {
    private List<LinkedHashMap<String, String>> engineers;

    public List<LinkedHashMap<String, String>> getEngineers() {
        return engineers;
    }

    public void setEngineers(List<LinkedHashMap<String, String>> engineers) {
        this.engineers = engineers;
    }

    public EngineersList(List<LinkedHashMap<String, String>> engineers) {
        this.engineers = engineers;
    }
}
