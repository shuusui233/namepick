package com.student.util;

import com.student.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    // 存储根目录路径
    public static final String FILE_PATH = "data";
    // 当前选中的班级路径
    public static String CLASS_PATH = "";
    // 当前班级的学生列表
    public static List<Student> students = new ArrayList<>();
}
