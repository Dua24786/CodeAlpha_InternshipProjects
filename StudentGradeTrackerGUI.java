
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    private String name;
    private double grade;

    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }
}

public class StudentGradeTrackerGUI extends JFrame {

    private JTextField nameField;
    private JTextField gradeField;
    private JTextArea reportArea;

    private ArrayList<Student> students;

    public StudentGradeTrackerGUI() {
        students = new ArrayList<>();

        setTitle("Student Grade Tracker");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        JButton reportButton = new JButton("Generate Report");

        inputPanel.add(addButton);
        inputPanel.add(reportButton);

        // Report Area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add Student Button Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        // Generate Report Button Action
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    private void addStudent() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a student name.");
            return;
        }

        try {
            double grade = Double.parseDouble(gradeField.getText());

            students.add(new Student(name, grade));

            JOptionPane.showMessageDialog(this,
                    "Student added successfully!");

            nameField.setText("");
            gradeField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid grade.");
        }
    }

    private void generateReport() {
        if (students.isEmpty()) {
            reportArea.setText("No student data available.");
            return;
        }

        double total = 0;
        double highest = students.get(0).getGrade();
        double lowest = students.get(0).getGrade();

        StringBuilder report = new StringBuilder();

        report.append("===== STUDENT SUMMARY REPORT =====\n\n");
        report.append(String.format("%-20s %-10s\n",
                "Student Name", "Grade"));
        report.append("-------------------------------------\n");

        for (Student student : students) {
            double grade = student.getGrade();

            total += grade;

            if (grade > highest)
                highest = grade;

            if (grade < lowest)
                lowest = grade;

            report.append(String.format("%-20s %-10.2f\n",
                    student.getName(), grade));
        }

        double average = total / students.size();

        report.append("\n-------------------------------------\n");
        report.append(String.format("Average Score : %.2f\n", average));
        report.append(String.format("Highest Score : %.2f\n", highest));
        report.append(String.format("Lowest Score  : %.2f\n", lowest));

        reportArea.setText(report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentGradeTrackerGUI().setVisible(true);
        });
    }
}