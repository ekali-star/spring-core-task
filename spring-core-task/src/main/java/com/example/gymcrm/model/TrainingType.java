package com.example.gymcrm.model;

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
        for (TrainingType type : TrainingType.values()) {
            if (type.displayName.equalsIgnoreCase(text) ||
                    type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}