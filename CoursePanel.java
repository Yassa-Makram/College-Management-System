import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CoursePanel extends JPanel implements ActionListener {

    JTextField txtID = new JTextField(15);
    JTextField txtName = new JTextField(15);
    JTextField txtDuration = new JTextField(15);
    JTextField txtInstructorID = new JTextField(15);

    JButton insert = new JButton("Add Course");
    JButton search = new JButton("Search");
    JButton update = new JButton("Update");
    JButton delete = new JButton("Delete");

    DefaultTableModel tableModel;
    JTable courseTable;

    public CoursePanel() {

        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Course ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtID, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Duration:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDuration, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Instructor ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtInstructorID, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,10));
        buttonPanel.add(insert); buttonPanel.add(search); buttonPanel.add(update); buttonPanel.add(delete);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        String[] data = {"Course ID" ,"Name", "Duration", "Instructor ID"};
        tableModel = new DefaultTableModel(data, 0);
        courseTable = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(courseTable);

        insert.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        search.addActionListener(this);

        add(formPanel, BorderLayout.WEST);
        add(jScrollPane, BorderLayout.CENTER);


        courseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = courseTable.getSelectedRow();
                if (row != -1) {
                    txtID.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtDuration.setText(tableModel.getValueAt(row, 2).toString());
                    txtInstructorID.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                    txtID.setEditable(false);
                }
            }
        });

        loadCourseData();
    }
    private void loadCourseData() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM course";
        try(Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                String c_id = rs.getString("C_ID");
                String c_name = rs.getString("C_name");
                String duration = rs.getString("duration");
                String inst_id = rs.getString("Inst_ID");



                tableModel.addRow(new Object[]{c_id, c_name, duration, inst_id});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insert) executeInsert();
        else if (e.getSource() == update) executeUpdate();
        else if (e.getSource() == delete) executeDelete();
        else if (e.getSource() == search) executeSearch();
    }

    private void executeInsert() {
        String sql = "INSERT INTO course (C_ID, C_name, duration, Inst_ID) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, txtID.getText());
            pst.setString(2, txtName.getText());
            pst.setString(3, txtDuration.getText());
            pst.setString(4, txtInstructorID.getText());

            pst.executeUpdate();
            loadCourseData();
            clearFields();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void executeUpdate() {
        String sql = "UPDATE course SET C_name=?, duration=?, Inst_ID=? WHERE C_ID=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, txtName.getText());
            pst.setString(2, txtDuration.getText());
            pst.setString(3, txtInstructorID.getText());
            pst.setString(4, txtID.getText());

            pst.executeUpdate();
            loadCourseData();
            clearFields();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void executeDelete() {
        String sql = "DELETE FROM course WHERE C_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, txtID.getText());
            pst.executeUpdate();
            loadCourseData();
            clearFields();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void executeSearch() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM course WHERE C_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, txtID.getText().trim());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("C_ID"), rs.getString("C_name"), rs.getString("duration"), rs.getString("Inst_ID")});
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void clearFields() {
        txtID.setText("");
        txtName.setText("");
        txtDuration.setText("");
        txtInstructorID.setText("");
        txtID.setEditable(true);
    }
}
