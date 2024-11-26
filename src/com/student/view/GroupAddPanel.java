package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;

public class GroupAddPanel extends JPanel {
    public GroupAddPanel() {
        this.setLayout(null);
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增小组"));
        JLabel lblName = new JLabel("小组名称：");
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
                JOptionPane.showMessageDialog(this, "请填写小组名称", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // 检查是否选择了班级
                if (Constant.CLASS_PATH.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // 在班级目录下创建小组目录
                File groupDir = new File(Constant.FILE_PATH + File.separator + 
                                      Constant.CLASS_PATH + File.separator + 
                                      txtName.getText());
                
                // 检查小组是否已存在
                if (groupDir.exists()) {
                    JOptionPane.showMessageDialog(this, "小组已存在", "", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // 创建小组目录
                if (groupDir.mkdir()) {
                    JOptionPane.showMessageDialog(this, "新增小组成功", "", JOptionPane.INFORMATION_MESSAGE);
                    txtName.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "新增小组失败", "", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
