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
}
