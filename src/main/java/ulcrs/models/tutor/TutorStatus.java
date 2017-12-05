package ulcrs.models.tutor;

public enum TutorStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    GHOST("Ghost");

    private String value;

    TutorStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static TutorStatus fromULCRole(String value) {
        // All tutors received from the ULC should be considered Active - filtering for tutors to exclude from
        // scheduling is done separately
        return TutorStatus.ACTIVE;
    }
}
