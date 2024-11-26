package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StudentListPanel extends JPanel {
    String[] headers = {"学号", "姓名", "小组"};
    JTable studentTable;
    JTextField txtId = new JTextField();
    JTextField txtName = new JTextField();
    JComboBox<String> cmbGroup = new JComboBox<>();
    JButton btnEdit = new JButton("修改");
    JButton btnDelete = new JButton("删除");

    public StudentListPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "学生列表"));
        this.setLayout(new BorderLayout());

        // 读取所有小组和学生
        List<String[]> studentList = new ArrayList<>();
        File classDir = new File(Constant.FILE_PATH + File.separator + Constant.CLASS_PATH);
        File[] groups = classDir.listFiles(File::isDirectory);
        
        // 添加小组到下拉框
        cmbGroup.addItem("请选择小组");
        if (groups != null) {
            for (File group : groups) {
                cmbGroup.addItem(group.getName());
                // 读取该小组下的所有学生文件
                File[] students = group.listFiles(f -> f.getName().endsWith(".txt"));
                if (students != null) {
                    for (File student : students) {
                        try {
                            String studentId = student.getName().replace(".txt", "");
                            // 读取文件内容获取学生姓名
                            java.util.Scanner scanner = new java.util.Scanner(student);
                            String studentName = scanner.hasNextLine() ? scanner.nextLine() : "";
                            scanner.close();
                            studentList.add(new String[]{studentId, studentName, group.getName()});
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        // 构建表格数据
        String[][] data = studentList.toArray(new String[0][]);

        DefaultTableModel tableModel = new DefaultTableModel(data, headers);
        studentTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(new JLabel("学号:"));
        btnPanel.add(txtId);
        txtId.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(new JLabel("姓名:"));
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(new JLabel("小组:"));
        btnPanel.add(cmbGroup);
        cmbGroup.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        this.add(btnPanel, BorderLayout.SOUTH);

        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                txtId.setText(data[selectedRow][0]);
                txtName.setText(data[selectedRow][1]);
                cmbGroup.setSelectedItem(data[selectedRow][2]);
            }
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || cmbGroup.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "请填写完整信息", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 获取旧文件和新文件路径
            File oldFile = new File(Constant.FILE_PATH + File.separator + 
                                  Constant.CLASS_PATH + File.separator + 
                                  data[selectedRow][2] + File.separator + 
                                  data[selectedRow][0] + ".txt");
            File newFile = new File(Constant.FILE_PATH + File.separator + 
                                  Constant.CLASS_PATH + File.separator + 
                                  cmbGroup.getSelectedItem() + File.separator + 
                                  txtId.getText() + ".txt");

            if (!oldFile.equals(newFile) && newFile.exists()) {
                JOptionPane.showMessageDialog(this, "该学号已存在", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                if (oldFile.renameTo(newFile)) {
                    // 重新写入学生姓名
                    java.io.FileWriter writer = new java.io.FileWriter(newFile);
                    writer.write(txtName.getText());
                    writer.close();
                    
                    JOptionPane.showMessageDialog(this, "修改成功", "", JOptionPane.INFORMATION_MESSAGE);
                    // 更新表格数据
                    data[selectedRow][0] = txtId.getText();
                    data[selectedRow][1] = txtName.getText();
                    data[selectedRow][2] = (String) cmbGroup.getSelectedItem();
                    ((DefaultTableModel) studentTable.getModel()).setDataVector(data, headers);
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败", "", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "修改失败: " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "确定要删除这个学生吗？", "", JOptionPane.YES_NO_OPTION) != 0) {
                return;
            }

            File studentFile = new File(Constant.FILE_PATH + File.separator + 
                                      Constant.CLASS_PATH + File.separator + 
                                      data[selectedRow][2] + File.separator + 
                                      data[selectedRow][0] + ".txt");
            
            if (studentFile.delete()) {
                JOptionPane.showMessageDialog(this, "删除成功", "", JOptionPane.INFORMATION_MESSAGE);
                // 刷新表格
                DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
                model.removeRow(selectedRow);
                txtId.setText("");
                txtName.setText("");
                cmbGroup.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
