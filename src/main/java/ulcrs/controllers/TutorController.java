package ulcrs.controllers;

import static spark.Spark.get;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import ulcrs.models.course.Course;
import ulcrs.models.course.CourseIntensity;
import ulcrs.models.course.CourseRequirements;
import ulcrs.models.rank.Rank;
import ulcrs.models.shift.Shift;
import ulcrs.models.tutor.Tutor;
import ulcrs.models.tutor.TutorPreferences;
import ulcrs.models.tutor.TutorStatus;

public class TutorController extends BaseController {

    @Override
    public RouteGroup routes() {
        return () -> {
            get("", this::getTutorList, gson::toJson);
            get("/:id", this::getTutor, gson::toJson);
        };
    }

    private List<Tutor> getTutorList(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);

        // This is all just test data. Delete this eventually, but for now leave just for testing
        Shift shift0 = new Shift(0, DayOfWeek.MONDAY, OffsetTime.now(), OffsetTime.now().plusHours(3));
        Shift shift1 = new Shift(1, DayOfWeek.TUESDAY, OffsetTime.of(LocalTime.MIDNIGHT, ZoneOffset.MAX), OffsetTime.now().plusHours(3));
        Shift shift2 = new Shift(2, DayOfWeek.WEDNESDAY, OffsetTime.of(LocalTime.MIDNIGHT, ZoneOffset.MAX), OffsetTime.now().plusHours(3));

        Map shiftPref = new HashMap<Rank, Set<Shift>>();
        Set preferShiftSet = new HashSet<Shift>();
        Set willingShiftSet = new HashSet<Shift>();
        preferShiftSet.add(shift0);
        willingShiftSet.add(shift1);
        willingShiftSet.add(shift2);
        shiftPref.put(Rank.PREFER, preferShiftSet);
        shiftPref.put(Rank.WILLING, willingShiftSet);

        Course course0 = new Course(0, "CS 302", new CourseRequirements(preferShiftSet, 1, 3, CourseIntensity.MEDIUM));
        Course course1 = new Course(1, "CS 301", new CourseRequirements(preferShiftSet, 1, 2, CourseIntensity.LOW));
        Course course2 = new Course(2, "CS 577", new CourseRequirements(willingShiftSet, 2, 4, CourseIntensity.HIGH));

        Map coursePref = new HashMap<Rank, Set<Course>>();
        Set preferCourseSet = new HashSet<Course>();
        Set willingCourseSet = new HashSet<Course>();
        preferCourseSet.add(course0);
        preferCourseSet.add(course1);
        willingCourseSet.add(course2);

        coursePref.put(Rank.PREFER, preferCourseSet);
        coursePref.put(Rank.WILLING, willingCourseSet);

        Map shiftFreqPref = new HashMap<Rank, Integer>();
        shiftFreqPref.put(Rank.PREFER, 4);

        return Arrays.asList(
                new Tutor(4, "Bilbo", "Baggins", new TutorPreferences(coursePref, shiftPref, shiftFreqPref), TutorStatus.ACTIVE),
                new Tutor(9, "Spicy", "Memelord", new TutorPreferences(null, null, null), TutorStatus.GHOST)
        );
    }

    private Tutor getTutor(Request request, Response response) {
        response.type(CONTENT_TYPE_JSON);
        return new Tutor(Integer.valueOf(request.params(":id")), "Bilbo", "Baggins", new TutorPreferences(null, null, null), TutorStatus.INACTIVE);
    }
}
