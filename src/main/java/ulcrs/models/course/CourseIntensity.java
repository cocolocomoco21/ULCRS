package ulcrs.models.course;

public enum CourseIntensity {

    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private String value;

    CourseIntensity(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static CourseIntensity fromString(String value) {
        for (CourseIntensity intensity : CourseIntensity.values()) {
            if (intensity.getValue().toLowerCase().equals(value.toLowerCase())) {
                return intensity;
            }
        }
        return null;
    }

    public int toWeightedValue() {
        // TODO this can be done better
        switch (this) {
            case LOW:
                return 1;
            case MEDIUM:
                return 3;
            case HIGH:
                return 6;
            default:
                return 0;
        }
    }
}
