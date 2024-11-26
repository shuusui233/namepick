package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;
import java.util.Enumeration;

public class ChangeClassPanel extends JScrollPane {
    JLabel infoLbl = new JLabel();

    public ChangeClassPanel(MainFrame mainFrame) {
        this.setBorder(new TitledBorder(new EtchedBorder(), "新选择班级"));
        int x = 160, y = 100;
        this.setLayout(null);
        // 读取目录获取班级
        File rootDir = new File(Constant.FILE_PATH);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        File[] files = rootDir.listFiles(File::isDirectory);
        
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(this, "请先创建班级", "", JOptionPane.INFORMATION_MESSAGE);
        } else {
            ButtonGroup btnGroup = new ButtonGroup();
            for (File file : files) {
                if (file.isDirectory()) {
                    JRadioButton classRadio = new JRadioButton(file.getName());
                    btnGroup.add(classRadio);
                    this.add(classRadio);
                    classRadio.setBounds(x, y, 200, 30);
                    y += 40;
                }
            }
            JButton btnChooseClass = new JButton("确认选择班级");
            this.add(btnChooseClass);
            btnChooseClass.setBounds(x, y, 120, 30);
            btnChooseClass.addActionListener(e -> {
                Enumeration<AbstractButton> elements = btnGroup.getElements();
                boolean isSelected = false;
                while (elements.hasMoreElements()) {
                    JRadioButton btn = (JRadioButton) elements.nextElement();
                    if (btn.isSelected()) {
                        isSelected = true;
                        mainFrame.setTitle(btn.getText());
                        Constant.CLASS_PATH = btn.getText();
                        infoLbl.setText("班级：" + btn.getText());
                        break;
                    }
                }
                if (isSelected) {
                    this.removeAll();
                    infoLbl.setBounds(160, 100, 200, 30);
                    this.add(infoLbl);
                    this.repaint();
                    this.validate();
                } else {
                    JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            this.repaint();
            this.validate();
        }
    }
}
