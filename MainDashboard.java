import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame implements ActionListener {

    // Buttons
    JButton Departments = new JButton("Departments");
    JButton Instructors = new JButton("Instructors");
    JButton Students = new JButton("Students");
    JButton Courses = new JButton("Courses");

    // Panels-Labels
    JPanel sidebarPanel = new JPanel();
    JPanel contentPanel = new JPanel();
    JLabel titleLabel = new JLabel("Welcome to Modern Academy Portal", SwingConstants.CENTER);

//----------------------------------------------------------------------------------------------------------------------
    MainDashboard() {

        setTitle("Modern Academy - Dashboard");
        setPreferredSize(new Dimension(1000, 650));
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(true);

        sidebarPanel.setLayout(new GridLayout(6,1,10,15));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(30,20,30,20));

        JLabel topLabel = new JLabel("\u25B6 MENU", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sidebarPanel.add(topLabel);


        Departments.addActionListener(this);
        Instructors.addActionListener(this);
        Students.addActionListener(this);
        Courses.addActionListener(this);

        Departments.putClientProperty("JButton.buttonType", "toolBarButton");
        Instructors.putClientProperty("JButton.buttonType", "toolBarButton");
        Students.putClientProperty("JButton.buttonType", "toolBarButton");
        Courses.putClientProperty("JButton.buttonType", "toolBarButton");

        Departments.setHorizontalAlignment(SwingConstants.LEFT);
        Instructors.setHorizontalAlignment(SwingConstants.LEFT);
        Students.setHorizontalAlignment(SwingConstants.LEFT);
        Courses.setHorizontalAlignment(SwingConstants.LEFT);

        sidebarPanel.setBackground(new Color(30, 30, 30));
        sidebarPanel.setOpaque(true);

        contentPanel.setBackground(new Color(40, 40, 40));
        contentPanel.setOpaque(true);

        sidebarPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,0,1, new Color(45, 52, 64)),
                BorderFactory.createEmptyBorder(30,20,30,20)
        ));

        sidebarPanel.add(Departments);
        sidebarPanel.add(Students);
        sidebarPanel.add(Courses);
        sidebarPanel.add(Instructors);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        titleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        contentPanel.add(titleLabel, BorderLayout.CENTER);

        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }
//----------------------------------------------------------------------------------------------------------------------
    JButton activeButton = null;

    private void setActiveButton(JButton clickedButton) {
        if (activeButton != null) {
            activeButton.setBackground(null);
            activeButton.setForeground(UIManager.getColor("Button.foreground"));
            activeButton.setOpaque(false);
        }
        activeButton = clickedButton;
        activeButton.setBackground(new Color(16, 185, 129));
        activeButton.setForeground(Color.WHITE);
        activeButton.setOpaque(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JButton) {
            setActiveButton((JButton)e.getSource());
        }

        contentPanel.removeAll();

        if (e.getSource() == Students) {
            StudentPanel studentPanel = new StudentPanel();
            contentPanel.add(studentPanel, BorderLayout.CENTER);

        } else if (e.getSource() == Departments) {
            DepartmentPanel departmentPanel = new DepartmentPanel();
            contentPanel.add(departmentPanel, BorderLayout.CENTER);

        } else if (e.getSource() == Instructors) {
            InstructorPanel instructorPanel = new InstructorPanel();
            contentPanel.add(instructorPanel, BorderLayout.CENTER);

        } else if (e.getSource() == Courses) {
            CoursePanel coursePanel = new CoursePanel();
            contentPanel.add(coursePanel, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();

    }
//----------------------------------------------------------------------------------------------------------------------

}
