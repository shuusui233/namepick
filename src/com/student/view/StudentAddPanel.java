package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;

public class StudentAddPanel extends JPanel {
    public StudentAddPanel() {
        this.setLayout(null);
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增学生"));
        JLabel lblId = new JLabel("学号：");
        JTextField txtId = new JTextField();
        JLabel lblName = new JLabel("姓名：");
        JTextField txtName = new JTextField();
        JLabel lblGroup = new JLabel("小组:");
        JComboBox<String> cmbGroup = new JComboBox<>();
        JButton btnName = new JButton("确认");
        this.add(lblId);
        this.add(txtId);
        this.add(lblName);
        this.add(txtName);
        this.add(lblGroup);
        this.add(cmbGroup);
        this.add(btnName);
        lblId.setBounds(200, 60, 100, 30);
        txtId.setBounds(200, 100, 100, 30);
        lblName.setBounds(200, 140, 100, 30);
        txtName.setBounds(200, 180, 200, 30);
        lblGroup.setBounds(200, 220, 100, 30);
        cmbGroup.setBounds(200, 260, 100, 30);
        btnName.setBounds(200, 300, 100, 30);

        // 列举小组
        File classDir = new File(Constant.FILE_PATH + File.separator + Constant.CLASS_PATH);
        File[] groups = classDir.listFiles(File::isDirectory);
        cmbGroup.addItem("请选择小组");
        if (groups != null) {
            for (File group : groups) {
                cmbGroup.addItem(group.getName());
            }
        }

        btnName.addActionListener(e -> {
            if (txtId.getText() == null || txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写学号", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写学生姓名", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (cmbGroup.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "请选择小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 在小组目录下创建学生文件
            File studentFile = new File(Constant.FILE_PATH + File.separator + 
                                      Constant.CLASS_PATH + File.separator + 
                                      cmbGroup.getSelectedItem() + File.separator + 
                                      txtId.getText() + ".txt");
            
            // 检查学生是否已存在
            if (studentFile.exists()) {
                JOptionPane.showMessageDialog(this, "该学号已存在", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                // 创建文件并写入学生信息
                java.io.FileWriter writer = new java.io.FileWriter(studentFile);
                writer.write(txtName.getText()); // 将学生姓名写入文件
                writer.close();
                
                JOptionPane.showMessageDialog(this, "新增学生成功", "", JOptionPane.INFORMATION_MESSAGE);
                txtId.setText("");
                txtName.setText("");
                cmbGroup.setSelectedIndex(0);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "新增学生失败: " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
