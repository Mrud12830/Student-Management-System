import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentManagementGUI extends JFrame {

    private JButton addButton;
    private JButton viewButton;
    private JButton searchButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton exitButton;

    public StudentManagementGUI() {

        setTitle("StudentSphere - Student Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        JLabel title = new JLabel("StudentSphere", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel subtitle = new JLabel(
                "Student Record Management System",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.add(title);
        headingPanel.add(subtitle);

        addButton = new JButton("Add Student");
        viewButton = new JButton("View Students");
        searchButton = new JButton("Search Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");
        exitButton = new JButton("Exit");

        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        addButton.setFont(buttonFont);
        viewButton.setFont(buttonFont);
        searchButton.setFont(buttonFont);
        updateButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        JPanel buttonPanel = new JPanel(
                new GridLayout(3, 2, 15, 15)
        );

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);

        // ADD STUDENT
        addButton.addActionListener(e -> addStudent());
        // VIEW STUDENTS
        viewButton.addActionListener(e -> viewStudents());
        
        // SEARCH STUDENT
        searchButton.addActionListener(e -> searchStudent());

        // UPDATE STUDENT
        updateButton.addActionListener(e -> updateStudent());

        // DELETE STUDENT
       deleteButton.addActionListener(e -> deleteStudent());
        // EXIT
        exitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(headingPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }

    private void addStudent() {

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField courseField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Student ID:"));
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        panel.add(new JLabel("Course:"));
        panel.add(courseField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add New Student",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String course = courseField.getText();

                Connection con = DBConnection.getConnection();

                String sql =
                        "INSERT INTO students (id, name, age, course) VALUES (?, ?, ?, ?)";

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, id);
                pst.setString(2, name);
                pst.setInt(3, age);
                pst.setString(4, course);

                pst.executeUpdate();

                pst.close();
                con.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Student added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "ID and Age must be numbers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error adding student:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    private void viewStudents() {

    try {

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM students";

        PreparedStatement pst = con.prepareStatement(sql);

        ResultSet rs = pst.executeQuery();

        String[] columns = {"ID", "Name", "Age", "Course"};

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        while (rs.next()) {

            Object[] row = {
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("course")
            };

            model.addRow(row);
        }

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JFrame tableFrame = new JFrame("Student Records");

        tableFrame.setSize(600, 400);
        tableFrame.setLocationRelativeTo(this);

        tableFrame.add(scrollPane);

        tableFrame.setVisible(true);

        rs.close();
        pst.close();
        con.close();

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
            this,
            "Error loading students:\n" + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
private void searchStudent() {

    String input = JOptionPane.showInputDialog(
            this,
            "Enter Student ID:",
            "Search Student",
            JOptionPane.QUESTION_MESSAGE
    );

    if (input == null) {
        return;
    }

    try {

        int id = Integer.parseInt(input);

        Connection con = DBConnection.getConnection();

        String sql = "SELECT * FROM students WHERE id = ?";

        PreparedStatement pst = con.prepareStatement(sql);

        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {

            String details =
                    "Student Found!\n\n" +
                    "ID: " + rs.getInt("id") + "\n" +
                    "Name: " + rs.getString("name") + "\n" +
                    "Age: " + rs.getInt("age") + "\n" +
                    "Course: " + rs.getString("course");

            JOptionPane.showMessageDialog(
                    this,
                    details,
                    "Student Details",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Student not found.",
                    "Search Result",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        rs.close();
        pst.close();
        con.close();

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Please enter a valid numeric Student ID.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error searching student:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
private void updateStudent() {

    String input = JOptionPane.showInputDialog(
            this,
            "Enter Student ID to update:",
            "Update Student",
            JOptionPane.QUESTION_MESSAGE
    );

    if (input == null) {
        return;
    }

    try {

        int id = Integer.parseInt(input);

        Connection con = DBConnection.getConnection();

        String checkSql = "SELECT * FROM students WHERE id = ?";

        PreparedStatement checkPst = con.prepareStatement(checkSql);

        checkPst.setInt(1, id);

        ResultSet rs = checkPst.executeQuery();

        if (!rs.next()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student not found.",
                    "Update Student",
                    JOptionPane.WARNING_MESSAGE
            );

            rs.close();
            checkPst.close();
            con.close();

            return;
        }

        String oldName = rs.getString("name");
        int oldAge = rs.getInt("age");
        String oldCourse = rs.getString("course");

        rs.close();
        checkPst.close();

        JTextField nameField = new JTextField(oldName);
        JTextField ageField = new JTextField(String.valueOf(oldAge));
        JTextField courseField = new JTextField(oldCourse);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("New Name:"));
        panel.add(nameField);

        panel.add(new JLabel("New Age:"));
        panel.add(ageField);

        panel.add(new JLabel("New Course:"));
        panel.add(courseField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Update Student",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String newName = nameField.getText();
            int newAge = Integer.parseInt(ageField.getText());
            String newCourse = courseField.getText();

            String sql =
                    "UPDATE students SET name = ?, age = ?, course = ? WHERE id = ?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, newName);
            pst.setInt(2, newAge);
            pst.setString(3, newCourse);
            pst.setInt(4, id);

            pst.executeUpdate();

            pst.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Student updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        con.close();

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                this,
                "ID and Age must be numbers.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error updating student:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
private void deleteStudent() {

    String input = JOptionPane.showInputDialog(
            this,
            "Enter Student ID to delete:",
            "Delete Student",
            JOptionPane.QUESTION_MESSAGE
    );

    if (input == null) {
        return;
    }

    try {

        int id = Integer.parseInt(input);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete Student ID " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Connection con = DBConnection.getConnection();

        String sql = "DELETE FROM students WHERE id = ?";

        PreparedStatement pst = con.prepareStatement(sql);

        pst.setInt(1, id);

        int rows = pst.executeUpdate();

        if (rows > 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Student not found.",
                    "Delete Student",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        pst.close();
        con.close();

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Please enter a valid numeric Student ID.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
        );

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error deleting student:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

    public static void main(String[] args) {
        new StudentManagementGUI();
    }
}