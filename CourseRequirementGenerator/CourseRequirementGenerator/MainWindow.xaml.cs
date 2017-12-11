using System;
using System.Collections.Generic;
using System.Linq;
using System.IO;
using System.Windows;
using Microsoft.Win32;
using Newtonsoft.Json;
using System.Collections.ObjectModel;

namespace CourseRequirementGenerator
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        static readonly List<string> intensityValues = new List<string>
        {
            "Low", "Medium", "High"
        };

        DropInData diData;
        ObservableCollection<CourseRequirement> requirements;
        ObservableCollection<CourseRequirementSpecifics> specifics;

        public MainWindow()
        {
            InitializeComponent();

            diData = new DropInData();
            requirements = new ObservableCollection<CourseRequirement>();
            specifics = new ObservableCollection<CourseRequirementSpecifics>();
            DataContext = this;
        }

        public List<string> IntensityValues
        {
            get { return intensityValues; }
        }

        public ObservableCollection<CourseRequirement> Requirements
        {
            get { return requirements; }
            set { requirements = value; }
        }

        public ObservableCollection<CourseRequirementSpecifics> Specifics
        {
            get { return specifics; }
            set { specifics = value; }
        }

        void LoadDropInData(object sender, RoutedEventArgs e)
        {
            var ofd = new OpenFileDialog();
            ofd.DefaultExt = ".json";
            ofd.Filter = "JSON | *.json";

            bool? result = ofd.ShowDialog();
            if (result != true)
                return;

            string fileName = ofd.FileName;
            string json = File.ReadAllText(fileName);

            diData = JsonConvert.DeserializeObject<DropInData>(json);

            foreach (var c in diData.courses)
            {
                requirements.Add(new CourseRequirement()
                {
                    course = c,
                    specifics = new List<CourseRequirementSpecifics>()
                });
            }

            courseIntensityComboBox.ItemsSource = diData.courses;
            courseSpecificComboBox.ItemsSource = diData.courses;
            courseWeekComboBox.ItemsSource = diData.courses;
            courseShiftComboBox.ItemsSource = diData.shifts;
        }

        void ExportDropInData(object sender, RoutedEventArgs e)
        {
            var sfd = new SaveFileDialog();
            sfd.FileName = "dropin-data";
            sfd.DefaultExt = ".json";
            sfd.Filter = "JSON File | *.json";

            bool? result = sfd.ShowDialog();
            if (result != true)
                return;

            string filename = sfd.FileName;

            // merge specifics list -> requirements list
            foreach (var specific in specifics)
            {
                foreach (var req in requirements)
                {
                    if (specific.course.id != req.courseID)
                        continue;

                    req.specifics.Add(specific);
                }
            }

            // write json out
            diData.requirements = Requirements;
            var json = JsonConvert.SerializeObject(diData);
            File.WriteAllText(filename, json);
        }
    }
}
