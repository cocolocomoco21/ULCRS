using System.Collections.Generic;

namespace CourseRequirementGenerator
{
    public class Tutor
    {
        public int id { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string role { get; set; }
        public List<int> prefCourses { get; set; }
        public List<int> willingCourses { get; set; }
        public List<int> prefShift { get; set; }
        public List<int> willingShift { get; set; }
        public int? prefShiftAmount { get; set; }
        public int? willingShiftAmount { get; set; }
    }
}
