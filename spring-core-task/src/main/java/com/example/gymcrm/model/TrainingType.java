package com.example.gymcrm.model;

import java.util.Arrays;

public enum TrainingType {
    CARDIO("Cardio"),
    STRENGTH("Strength"),
    FLEXIBILITY("Flexibility"),
    BALANCE("Balance"),
    HIIT("HIIT"),
    YOGA("Yoga"),
    PILATES("Pilates"),
    CROSSFIT("Crossfit");

    private final String displayName;

    TrainingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TrainingType fromString(String text) {
        return Arrays.stream(TrainingType.values()).filter(type -> type.displayName.equalsIgnoreCase(text) ||
                type.name().equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }
}