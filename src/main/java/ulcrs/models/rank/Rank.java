package ulcrs.models.rank;

public enum Rank {

    REQUIRED("Required"),
    PREFER("Prefer"),
    WILLING("Willing");

    private String value;

    Rank(String value) {
        this.value = value;
    }
}
