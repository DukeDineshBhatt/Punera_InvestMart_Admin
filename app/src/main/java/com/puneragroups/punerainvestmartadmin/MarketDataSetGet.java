package com.puneragroups.punerainvestmartadmin;

public class MarketDataSetGet {

    String Name,State;
    String Value,fluc;
    public MarketDataSetGet() {

    }

    public MarketDataSetGet(String name, String state, String value, String fluc) {
        Name = name;
        State = state;
        Value = value;
        this.fluc = fluc;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getFluc() {
        return fluc;
    }

    public void setFluc(String fluc) {
        this.fluc = fluc;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
