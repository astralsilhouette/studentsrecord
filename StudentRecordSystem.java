package Student;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentRecordSystem {
    private static List<Student> students = new ArrayList<>();
    private static JTextArea textArea;
    private static JTextField idField, nameField, ageField, searchField;
    private static JFrame frame;

    public static void main(String[] args) {
        loadStudents();  // Load students from file on startup

        // Set the look and feel to the system's default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Student Record System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(15);
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField(15);

        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View Students");
        JButton deleteButton = new JButton("Delete Student");
        JButton searchButton = new JButton("Search");
        JButton editButton = new JButton("Edit Student");
        JButton sortButton = new JButton("Sort by ID");

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(ageField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(viewButton, gbc);
        gbc.gridx = 2;
        inputPanel.add(deleteButton, gbc);
        gbc.gridx = 3;
        inputPanel.add(searchButton, gbc);
        gbc.gridx = 4;
        inputPanel.add(editButton, gbc);
        gbc.gridx = 5;
        inputPanel.add(sortButton, gbc);

        // Search field
        searchField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Search by ID:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 3;
        inputPanel.add(searchField, gbc);

        // Text area for displaying student records
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add components to frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Set a custom background color
        inputPanel.setBackground(new Color(240, 240, 255));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                int age;
                try {
                    age = Integer.parseInt(ageField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid age. Please enter a valid number.");
                    return;
                }
                Student student = new Student(id, name, age);
                students.add(student);
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                saveStudents();  // Save students to file
                JOptionPane.showMessageDialog(frame, "Student added successfully!");
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                for (Student student : students) {
                    textArea.append(student.toString() + "\n");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter Student ID to delete:");
                boolean found = false;
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getId().equals(id)) {
                        students.remove(i);
                        found = true;
                        saveStudents();  // Save updated list to file
                        JOptionPane.showMessageDialog(frame, "Student deleted successfully!");
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Student ID not found.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = searchField.getText();
                textArea.setText("");
                for (Student student : students) {
                    if (student.getId().equals(id)) {
                        textArea.append(student.toString() + "\n");
                        return;
                    }
                }
                textArea.append("Student ID not found.\n");
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter Student ID to edit:");
                for (Student student : students) {
                    if (student.getId().equals(id)) {
                        String newName = JOptionPane.showInputDialog("Enter new name:", student.getName());
                        String newAgeStr = JOptionPane.showInputDialog("Enter new age:", student.getAge());
                        try {
                            int newAge = Integer.parseInt(newAgeStr);
                            student.setName(newName);
                            student.setAge(newAge);
                            saveStudents();  // Save updated list to file
                            JOptionPane.showMessageDialog(frame, "Student updated successfully!");
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid age. Please enter a valid number.");
                        }
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Student ID not found.");
            }
        });

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                students.sort(Comparator.comparing(Student::getId));
                textArea.setText("");
                for (Student student : students) {
                    textArea.append(student.toString() + "\n");
                }
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    // Method to save students to a file
    private static void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load students from a file
    @SuppressWarnings("unchecked")
    private static void loadStudents() {
        File file = new File("students.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                students = (List<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
