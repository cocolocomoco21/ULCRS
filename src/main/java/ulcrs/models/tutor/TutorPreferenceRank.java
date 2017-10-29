package ulcrs.models.tutor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TutorPreferenceRank {

    REQUIRED("Required"),
    PREFER("Prefer"),
    WILLING("Willing");

    @Getter
    private String value;
}
