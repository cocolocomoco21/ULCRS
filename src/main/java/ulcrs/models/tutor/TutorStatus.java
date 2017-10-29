package ulcrs.models.tutor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TutorStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    GHOST("Ghost");

    @Getter
    private String value;
}
