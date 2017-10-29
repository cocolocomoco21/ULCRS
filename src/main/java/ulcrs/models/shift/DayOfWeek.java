package ulcrs.models.shift;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DayOfWeek {

    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    @Getter
    private String value;
}
