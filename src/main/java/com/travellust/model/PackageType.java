package com.travellust.model;

/**
 * Enum for package types
 */
public enum PackageType {
    ADVENTURE("Adventure"),
    LUXURY("Luxury"),
    CULTURAL("Cultural"),
    FAMILY("Family"),
    HONEYMOON("Honeymoon"),
    GROUP("Group"),
    SOLO("Solo"),
    BEACH("Beach"),
    MOUNTAIN("Mountain");
    
    private final String displayName;
    
    PackageType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 