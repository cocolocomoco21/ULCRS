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
}
