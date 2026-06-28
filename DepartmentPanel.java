import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DepartmentPanel extends JPanel implements ActionListener {

    JTextField txtName = new JTextField(15);
    JTextField txtLocation = new JTextField(15);

    JButton insert = new JButton("Add Department");
    JButton search = new JButton("Search");
    JButton update = new JButton("Update");
    JButton delete = new JButton("Delete");

    DefaultTableModel tableModel;
    JTable departmentTable;

    public DepartmentPanel() {

        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLocation, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(insert); buttonPanel.add(search); buttonPanel.add(update); buttonPanel.add(delete);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        String[] data = {"Department Name", "Location"};
        tableModel = new DefaultTableModel(data, 0);
        departmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(departmentTable);

        insert.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        search.addActionListener(this);

        add(formPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        departmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = departmentTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtName.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtLocation.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtName.setEditable(false);
                }
            }
        });
        loadDepartmentData();
    }

    private void loadDepartmentData() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM department";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)){

            while (rs.next()) {
                String Dname = rs.getString("Dname");
                String location = rs.getString("location");


                tableModel.addRow(new Object[]{Dname, location});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
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
        String sql = "INSERT INTO department (Dname, location) VALUES (?, ?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)){

            pst.setString(1, txtName.getText());
            pst.setString(2, txtLocation.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Department Added Successfully!");

            loadDepartmentData();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Insert Error: " + ex.getMessage());
        }
    }

    private void executeUpdate() {
        String sql = "UPDATE department SET location = ? WHERE Dname = ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)){

            pst.setString(1, txtLocation.getText());
            pst.setString(2, txtName.getText());

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Department Updated Successfully!");
                loadDepartmentData();
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage());
        }
    }

    private void executeDelete() {
        String Dname = txtName.getText();
        String location = txtLocation.getText();
        if (Dname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are your sure you want to delete this department?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM department WHERE Dname = ? AND location = ?";

            try(Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, Dname);
                pst.setString(2,location);

                if (pst.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(this, "Department Deleted Successfully!");
                    loadDepartmentData();
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete Error: " + ex.getMessage());
            }
        }
    }

    private void executeSearch() {
        String Dname = txtName.getText().trim();

        if (Dname.isEmpty()) {
            loadDepartmentData();
            return;
        }
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM department WHERE Dname LIKE ?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + txtName.getText().trim() + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Dname = rs.getString("Dname");
                String location = rs.getString("location");


                tableModel.addRow(new Object[]{Dname, location});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtLocation.setText("");
        txtName.setEditable(true);
    }


}
