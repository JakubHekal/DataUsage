package com.jakubhekal.datausage.model;

public class Package {
    private String name;
    private String packageName;
    private Long dataUsage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Long getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(Long dataUsage) {
        this.dataUsage = dataUsage;
    }


}
