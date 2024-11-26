package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class ClassListPanel extends JPanel {
    String[] headers = {"序号", "班级名称"};
    JTable classTable;
    JTextField txtName = new JTextField();
    JButton btnEdit = new JButton("修改");
    JButton btnDelete = new JButton("删除");

    public ClassListPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "班级列表"));
        this.setLayout(new BorderLayout());
        
        // 读取data目录下的所有文件夹作为班级
        File rootDir = new File(Constant.FILE_PATH);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        File[] classes = rootDir.listFiles(File::isDirectory);
        
        // 构建表格数据
        String[][] data;
        if (classes == null || classes.length == 0) {
            data = new String[0][2];
        } else {
            data = new String[classes.length][2];
            for (int i = 0; i < classes.length; i++) {
                data[i][0] = String.valueOf(i + 1);
                data[i][1] = classes[i].getName();
            }
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, headers);
        classTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        classTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(classTable);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(200, 30));
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        this.add(btnPanel, BorderLayout.SOUTH);

        classTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if(selectedRow >= 0) {
                txtName.setText(data[selectedRow][1]);
            }
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写班级名称", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // 获取旧的班级目录和新的班级名称
            File oldDir = new File(Constant.FILE_PATH + File.separator + data[selectedRow][1]);
            File newDir = new File(Constant.FILE_PATH + File.separator + txtName.getText());
            
            // 检查新名称是否已存在
            if (newDir.exists()) {
                JOptionPane.showMessageDialog(this, "班级名称已存在", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // 重命名目录
            if (oldDir.renameTo(newDir)) {
                JOptionPane.showMessageDialog(this, "修改成功", "", JOptionPane.INFORMATION_MESSAGE);
                // 刷新表格
                data[selectedRow][1] = txtName.getText();
                ((DefaultTableModel) classTable.getModel()).setDataVector(data, headers);
            } else {
                JOptionPane.showMessageDialog(this, "修改失败", "", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnDelete.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(JOptionPane.showConfirmDialog(this, "删除班级会把小组、学生和成绩都删除，您确定要删除这个班级？", "", JOptionPane.YES_NO_OPTION) != 0){
                return;
            }
            
            // 删除班级目录
            File classDir = new File(Constant.FILE_PATH + File.separator + data[selectedRow][1]);
            if (deleteDirectory(classDir)) {
                JOptionPane.showMessageDialog(this, "删除班级成功", "", JOptionPane.INFORMATION_MESSAGE);
                // 刷新表格
                DefaultTableModel model = (DefaultTableModel) classTable.getModel();
                model.removeRow(selectedRow);
                // 重新编号
                for (int i = 0; i < model.getRowCount(); i++) {
                    model.setValueAt(String.valueOf(i + 1), i, 0);
                }
                txtName.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "删除班级失败", "", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    // 递归删除目录及其内容
    private boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();
    }
}
