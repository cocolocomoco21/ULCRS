package ulcrs.models.shift;

import lombok.Data;

@Data
public class Shift {

    int id;
    DayOfWeek day;

    // TODO: representing time without date?
    int startHour;
    int startMinute;
    int endHour;
    int endMinute;
}
