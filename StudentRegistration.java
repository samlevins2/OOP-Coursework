
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

public class Question2 extends JFrame {

    private JTextField firstNameField, lastNameField, emailField, confirmEmailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<Integer> yearBox, dayBox;
    private JComboBox<String> monthBox, departmentBox;
    private JRadioButton maleBtn, femaleBtn;
    private JTextArea outputArea;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Question2() {
        setTitle("New Student Registration Form");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        mainPanel.add(createFormPanel());
        mainPanel.add(createOutputPanel());
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 5, 5));

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        confirmEmailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Confirm Email:"));
        panel.add(confirmEmailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        // DOB
        panel.add(new JLabel("DOB:"));
        panel.add(createDOBPanel());

        // Gender
        panel.add(new JLabel("Gender:"));
        panel.add(createGenderPanel());

        // Department
        panel.add(new JLabel("Department:"));
        departmentBox = new JComboBox<>(new String[]{
                "Select", "Civil", "CSE", "Electrical", "E&C", "Mechanical"
        });
        panel.add(departmentBox);

        JButton submitBtn = new JButton("Submit");
        JButton cancelBtn = new JButton("Cancel");

        submitBtn.addActionListener(e -> handleSubmit());
        cancelBtn.addActionListener(e -> clearForm());

        panel.add(submitBtn);
        panel.add(cancelBtn);

        return panel;
    }

    private JPanel createDOBPanel() {
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        yearBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear - 60; y <= currentYear - 16; y++) {
            yearBox.addItem(y);
        }

        monthBox = new JComboBox<>(Month.values());
        dayBox = new JComboBox<>();

        updateDays();

        monthBox.addActionListener(e -> updateDays());
        yearBox.addActionListener(e -> updateDays());

        dobPanel.add(yearBox);
        dobPanel.add(monthBox);
        dobPanel.add(dayBox);

        return dobPanel;
    }

    private void updateDays() {
        dayBox.removeAllItems();
        int year = (Integer) yearBox.getSelectedItem();
        Month month = (Month) monthBox.getSelectedItem();
        int days = month.length(Year.isLeap(year));
        for (int d = 1; d <= days; d++) {
            dayBox.addItem(d);
        }
    }

    private JPanel createGenderPanel() {
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleBtn = new JRadioButton("Male");
        femaleBtn = new JRadioButton("Female");
        ButtonGroup group = new ButtonGroup();
        group.add(maleBtn);
        group.add(femaleBtn);

        genderPanel.add(maleBtn);
        genderPanel.add(femaleBtn);

        return genderPanel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Your Data is Below:"), BorderLayout.NORTH);
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        return panel;
    }

    private void handleSubmit() {
        String errors = validateForm();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, errors, "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String record = buildRecord();
        outputArea.setText(record);
        appendToCSV(record);
    }

    private String validateForm() {
        StringBuilder errors = new StringBuilder();

        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String confirmEmail = confirmEmailField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String confirmPass = new String(confirmPasswordField.getPassword()).trim();

        if (first.isEmpty()) errors.append("First name required\n");
        if (last.isEmpty()) errors.append("Last name required\n");

        if (!EMAIL_PATTERN.matcher(email).matches())
            errors.append("Invalid email\n");
        if (!email.equals(confirmEmail))
            errors.append("Emails do not match\n");

        if (!pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$"))
            errors.append("Password must be 8-20 chars with letter and digit\n");
        if (!pass.equals(confirmPass))
            errors.append("Passwords do not match\n");

        if (!maleBtn.isSelected() && !femaleBtn.isSelected())
            errors.append("Select gender\n");

        if (departmentBox.getSelectedIndex() == 0)
            errors.append("Select department\n");

        // Age check
        int year = (Integer) yearBox.getSelectedItem();
        Month month = (Month) monthBox.getSelectedItem();
        int day = (Integer) dayBox.getSelectedItem();

        LocalDate dob = LocalDate.of(year, month, day);
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 16 || age > 60)
            errors.append("Age must be 16-60\n");

        return errors.toString();
    }

    private String buildRecord() {
        String id = generateId();

        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        String gender = maleBtn.isSelected() ? "F".equals("X") ? "" : "M" : "F";
        if (femaleBtn.isSelected()) gender = "F";

        String dept = departmentBox.getSelectedItem().toString();
        int year = (Integer) yearBox.getSelectedItem();
        Month month = (Month) monthBox.getSelectedItem();
        int day = (Integer) dayBox.getSelectedItem();

        LocalDate dob = LocalDate.of(year, month, day);
        String email = emailField.getText().trim();

        return id + " | " + first + " " + last + " | " + gender + " | " +
                dept + " | " + dob + " | " + email;
    }

    private String generateId() {
        int year = LocalDate.now().getYear();
        Path path = Paths.get("students.csv");
        int counter = 1;

        if (Files.exists(path)) {
            try {
                long count = Files.lines(path)
                        .filter(l -> l.startsWith(String.valueOf(year)))
                        .count();
                counter = (int) count + 1;
            } catch (IOException ignored) {}
        }

        return year + "-" + String.format("%05d", counter);
    }

    private void appendToCSV(String record) {
        try (FileWriter fw = new FileWriter("students.csv", true)) {
            fw.write(record + System.lineSeparator());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File write error");
        }
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        confirmEmailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        maleBtn.setSelected(false);
        femaleBtn.setSelected(false);
        departmentBox.setSelectedIndex(0);
        outputArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Question2().setVisible(true));
    }
}
