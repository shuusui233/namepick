package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGroupPanel extends JPanel {
    private JLabel lbl1 = new JLabel("小组名：");
    private JLabel lbl2 = new JLabel("学生姓名：");
    private JLabel lbl3 = new JLabel("学生照片：");
    private JLabel lblPic = new JLabel("照片");
    private JLabel lbl4 = new JLabel("小组评分");
    private JTextField txtGroup = new JTextField();
    private JTextField txtStudent = new JTextField();
    private JTextField txtScore = new JTextField();
    private JButton btnChooseGroup = new JButton("随机小组");
    private JButton btnChooseStudent = new JButton("随机学生");
    private JButton btnAbsence = new JButton("缺勤");
    private JButton btnLeave = new JButton("请假");
    private JButton btnScore = new JButton("小组评分");
    Thread threadGroup = null;   // 随机抽取小组的线程
    Thread threadStudent = null;   // 随机抽取小组的线程
    private List<String> groupNames = new ArrayList<>(); // 存储所有小组名称
    private Random random = new Random();

    public RandomGroupPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "随机小组点名"));
        this.setLayout(null);
        this.add(lbl1);
        this.add(lbl2);
        this.add(lbl3);
        this.add(txtGroup);
        this.add(txtStudent);
        this.add(lblPic);
        this.add(btnChooseGroup);
        this.add(btnChooseStudent);
        this.add(btnAbsence);
        this.add(btnLeave);
        this.add(lbl4);
        this.add(txtScore);
        this.add(btnScore);

        lbl1.setBounds(50, 50, 100, 30);
        txtGroup.setBounds(50, 90, 100, 30);
        txtGroup.setEditable(false);
        btnChooseGroup.setBounds(50, 130, 100, 30);

        lbl4.setBounds(50, 190, 100, 30);
        txtScore.setBounds(50, 230, 100, 30);
        btnScore.setBounds(50, 270, 100, 30);

        lbl2.setBounds(220, 50, 100, 30);
        txtStudent.setBounds(220, 90, 130, 30);
        txtStudent.setEditable(false);
        lblPic.setBounds(220, 130, 130, 150);
        btnChooseStudent.setBounds(220, 300, 100, 30);
        btnAbsence.setBounds(220, 340, 60, 30);
        btnLeave.setBounds(290, 340, 60, 30);

        // 读取当前班级下的所有小组
        loadGroups();

        // 随机小组
        btnChooseGroup.addActionListener(e -> {
            if (groupNames.isEmpty()) {
                JOptionPane.showMessageDialog(this, "当前班级没有小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (e.getActionCommand().equals("停")) {
                btnChooseGroup.setText("随机小组");
                if (threadGroup != null) {
                    threadGroup.interrupt();
                    threadGroup = null;
                }
            } else {
                btnChooseGroup.setText("停");
                threadGroup = new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            // 随机选择一个小组
                            int index = random.nextInt(groupNames.size());
                            String groupName = groupNames.get(index);
                            SwingUtilities.invokeLater(() -> txtGroup.setText(groupName));
                            Thread.sleep(50); // 控制滚动速度
                        }
                    } catch (InterruptedException ex) {
                        // 线程被中断，不需要处理
                    }
                });
                threadGroup.start();
            }
        });

        // TODO 其他按钮功能保持不变
    }

    // 加载当前班级下的所有小组
    private void loadGroups() {
        groupNames.clear();
        if (!Constant.CLASS_PATH.isEmpty()) {
            File classDir = new File(Constant.FILE_PATH + File.separator + Constant.CLASS_PATH);
            File[] groups = classDir.listFiles(File::isDirectory);
            if (groups != null) {
                for (File group : groups) {
                    groupNames.add(group.getName());
                }
            }
        }
    }
}
