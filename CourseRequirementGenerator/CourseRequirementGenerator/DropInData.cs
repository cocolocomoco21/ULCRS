using System.Collections.ObjectModel;

namespace CourseRequirementGenerator
{
    public class DropInData
    {
        public DropInData()
        {
            courses = new ObservableCollection<Course>();
            shifts = new ObservableCollection<Shift>();
            tutors = new ObservableCollection<Tutor>();
            requirements = new ObservableCollection<CourseRequirement>();
        }

        public ObservableCollection<Course> courses { get; set; }
        public ObservableCollection<Shift> shifts { get; set; }
        public ObservableCollection<Tutor> tutors { get; set; }
        public ObservableCollection<CourseRequirement> requirements { get; set; }
    }
}
