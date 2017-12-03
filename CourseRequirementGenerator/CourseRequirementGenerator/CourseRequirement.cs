using Newtonsoft.Json;
using System.Collections.Generic;

namespace CourseRequirementGenerator
{
    public class CourseRequirement
    {
        public int courseID { get { return course.id; } }

        [JsonIgnore]
        public string courseName { get { return course.name; } }

        [JsonIgnore]
        public Course course { get; set; }

        public string intensity { get; set; }
        public int timesPerWeek { get; set; }
        public int numTutors { get; set; }
        public List<CourseRequirementSpecifics> specifics { get; set; }
    }
    
    public class CourseRequirementSpecifics
    {
        [JsonIgnore]
        public Course course { get; set; }

        [JsonIgnore]
        public Shift shift { get; set; }

        public int shiftID { get { return shift.id; } }
        public int numTutors { get; set; }
    }
}
