package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;

public class ClassAddPanel extends JPanel {
    public ClassAddPanel() {
        this.setLayout(null);
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增班级"));
        JLabel lblName = new JLabel("班级名称：");
        JTextField txtName = new JTextField();
        JButton btnName = new JButton("确认");
        this.add(lblName);
        this.add(txtName);
        this.add(btnName);
        lblName.setBounds(200, 80, 100, 30);
        txtName.setBounds(200, 130, 200, 30);
        btnName.setBounds(200, 180, 100, 30);

        btnName.addActionListener(e -> {
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写班级名称", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // 创建根目录
                File rootDir = new File(Constant.FILE_PATH);
                if (!rootDir.exists()) {
                    rootDir.mkdir();
                }
                // 创建班级目录
                File classDir = new File(Constant.FILE_PATH + File.separator + txtName.getText());
                if (classDir.exists()) {
                    JOptionPane.showMessageDialog(this, "班级已存在", "", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (classDir.mkdir()) {
                    JOptionPane.showMessageDialog(this, "新增班级成功", "", JOptionPane.INFORMATION_MESSAGE);
                    txtName.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "新增班级失败", "", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
