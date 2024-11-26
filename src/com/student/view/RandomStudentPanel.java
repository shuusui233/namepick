package com.student.view;

import com.student.util.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStudentPanel extends JPanel {
    private JLabel lbl2 = new JLabel("学生姓名：");
    private JLabel lbl3 = new JLabel("学生照片：");
    private JLabel lblPic = new JLabel("照片");
    private JTextField txtStudent = new JTextField();
    private JButton btnChooseStudent = new JButton("随机学生");
    private JButton btnAbsence = new JButton("缺勤");
    private JButton btnLeave = new JButton("请假");
    private JButton btnAnswer = new JButton("答题");
    Thread threadStudent = null;   // 随机抽取学生的线程
    private List<String[]> studentList = new ArrayList<>(); // 存储所有学生信息 [学号, 姓名]
    private Random random = new Random();

    public RandomStudentPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "随机学生点名"));
        this.setLayout(null);
        this.add(lbl2);
        this.add(lbl3);
        this.add(txtStudent);
        this.add(lblPic);
        this.add(btnChooseStudent);
        this.add(btnAbsence);
        this.add(btnLeave);
        this.add(btnAnswer);

        lbl2.setBounds(160, 50, 100, 30);
        txtStudent.setBounds(160, 90, 130, 30);
        txtStudent.setEditable(false);
        lblPic.setBounds(160, 130, 130, 150);
        btnChooseStudent.setBounds(160, 300, 130, 30);
        btnAbsence.setBounds(160, 340, 60, 30);
        btnLeave.setBounds(230, 340, 60, 30);
        btnAnswer.setBounds(300, 340, 60, 30);

        // 加载所有学生
        loadStudents();

        // 随机学生
        btnChooseStudent.addActionListener(e -> {
            if (studentList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "当前班级没有学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (e.getActionCommand().equals("停")) {
                btnChooseStudent.setText("随机学生");
                if (threadStudent != null) {
                    threadStudent.interrupt();
                    threadStudent = null;
                }
            } else {
                btnChooseStudent.setText("停");
                threadStudent = new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            // 随机选择一个学生
                            int index = random.nextInt(studentList.size());
                            String[] student = studentList.get(index);
                            SwingUtilities.invokeLater(() -> txtStudent.setText(student[1])); // 显示学生姓名
                            Thread.sleep(50); // 控制滚动速度
                        }
                    } catch (InterruptedException ex) {
                        // 线程被中断，不需要处理
                    }
                });
                threadStudent.start();
            }
        });

        // 缺勤
        btnAbsence.addActionListener(e -> {
            if (txtStudent.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "已记录缺勤", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 请假
        btnLeave.addActionListener(e -> {
            if (txtStudent.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "已记录请假", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 答题
        btnAnswer.addActionListener(e -> {
            if (txtStudent.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "回答正确", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // 加载当前班级下的所有学生
    private void loadStudents() {
        studentList.clear();
        if (!Constant.CLASS_PATH.isEmpty()) {
            File classDir = new File(Constant.FILE_PATH + File.separator + Constant.CLASS_PATH);
            File[] groups = classDir.listFiles(File::isDirectory);
            if (groups != null) {
                for (File group : groups) {
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
                                studentList.add(new String[]{studentId, studentName});
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
