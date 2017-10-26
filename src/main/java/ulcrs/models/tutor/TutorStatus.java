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

}
