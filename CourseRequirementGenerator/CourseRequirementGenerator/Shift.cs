using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CourseRequirementGenerator
{
    public class Shift
    {
        public int id { get; set; }
        public DayOfWeek day { get; set; }
        public DateTime startTime { get; set; }
        public DateTime endTime { get; set; }
    }
}
