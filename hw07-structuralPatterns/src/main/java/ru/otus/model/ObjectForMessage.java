package ru.otus.model;

import java.util.List;
import java.util.ArrayList;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    protected Object clone() {
        ObjectForMessage newOFM = new ObjectForMessage();
        newOFM.setData(new ArrayList<>(data));
        return newOFM;
    }
}
