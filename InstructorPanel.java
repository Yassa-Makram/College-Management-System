import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class InstructorPanel extends JPanel implements ActionListener {

    JTextField txtID = new JTextField(15);
    JTextField txtFname = new JTextField(15);
    JTextField txtLname = new JTextField(15);
    JTextField txtPhone = new JTextField(15);
    JTextField txtDeptName = new JTextField(15); // To link with department

    JButton insert = new JButton("Add Instructor");
    JButton search = new JButton("Search");
    JButton update = new JButton("Update");
    JButton delete = new JButton("Delete");

    DefaultTableModel tableModel;
    JTable instructorTable;

    public InstructorPanel() {

        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Instructor ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Instructor ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtID, gbc);

        //First Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtFname, gbc);

        //Last Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLname, gbc);

        //Phone
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDeptName, gbc);

        // Actions Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(insert); buttonPanel.add(search); buttonPanel.add(update); buttonPanel.add(delete);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        String[] data = {"ID" ,"First Name", "Last Name", "Phone", "Department"};
        tableModel = new DefaultTableModel(data, 0);
        instructorTable = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(instructorTable);

        insert.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        search.addActionListener(this);

        add(formPanel, BorderLayout.WEST);
        add(jScrollPane, BorderLayout.CENTER);

        instructorTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = instructorTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtID.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtFname.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtLname.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtPhone.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtDeptName.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    txtID.setEditable(false);
                }
            }
        });
        loadInstructorData();

    }
    private void loadInstructorData() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM instructor";
        try(Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                String id = rs.getString("Instructor_ID");
                String fname = rs.getString("Fname");
                String lname = rs.getString("Lname");
                String phone = rs.getString("phone");
                String Dname = rs.getString("Dept_Name");


                tableModel.addRow(new Object[]{id, fname, lname, phone, Dname});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == insert) {executeInsert();}
        else if (e.getSource() == update) {executeUpdate();}
        else if (e.getSource() == delete) {executeDelete();}
        else if (e.getSource() == search) {executeSearch();}
    }

    private void executeInsert() {
        String sql = "INSERT INTO instructor (Instructor_ID, Fname, Lname, phone, Dept_Name) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)){

            pst.setString(1, txtID.getText());
            pst.setString(2, txtFname.getText());
            pst.setString(3, txtLname.getText());
            pst.setString(4, txtPhone.getText());
            pst.setString(5, txtDeptName.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Instructor Added Successfully!");

            loadInstructorData();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Insert Error: " + ex.getMessage());
        }
    }

    private void executeUpdate() {
        String sql = "UPDATE instructor SET Fname = ?, Lname = ?, phone = ?, Dept_Name = ? WHERE Instructor_ID = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)){

            pst.setString(1, txtFname.getText());
            pst.setString(2, txtLname.getText());
            pst.setString(3, txtPhone.getText());
            pst.setString(4, txtDeptName.getText());
            pst.setString(5, txtID.getText());

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Department Updated Successfully!");
                loadInstructorData();
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage());
        }
    }

    private void executeDelete() {
        String id = txtID.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an instructor to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are your sure you want to delete this instructor?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM instructor WHERE Instructor_ID = ?";

            try(Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, id);
                if (pst.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Instructor Deleted Successfully!");
                    loadInstructorData();
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete Error: " + ex.getMessage());
            }
        }
    }

    private void executeSearch() {
        String id = txtID.getText().trim();

        if (id.isEmpty()) {
            loadInstructorData();
            return;
        }
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM instructor WHERE Instructor_ID = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, txtID.getText().trim());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                id = rs.getString("Instructor_ID");
                String fname = rs.getString("Fname");
                String lname = rs.getString("Lname");
                String phone = rs.getString("phone");
                String Dname = rs.getString("Dept_Name");


                tableModel.addRow(new Object[]{id, fname, lname, phone, Dname});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtID.setText("");
        txtFname.setText("");
        txtLname.setText("");
        txtPhone.setText("");
        txtDeptName.setText("");
        txtID.setEditable(true);
    }
}
