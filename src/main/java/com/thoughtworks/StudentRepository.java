package com.thoughtworks;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private Connection connection;

    public StudentRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_examination_sys?useUnicode=true&characterEncoding=utf-8&serverTimezone=Hongkong", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(List<Student> students) {
        students.forEach(this::save);
    }

    public void save(Student student) {
        // TODO:
        String insert = "INSERT INTO student_jdbc (id, name, gender, admissionYear, birthday, classId) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(insert)) {
            preparedStatement.setString(1, student.getId());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getGender());
            preparedStatement.setInt(4, student.getAdmissionYear());
            preparedStatement.setDate(5, new Date(student.getBirthday().getTime()));
            preparedStatement.setString(6, student.getClassId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> query() {
        // TODO:
        try (Statement statement = this.connection.createStatement()) {
            String queryAllSql = "SELECT * FROM student_jdbc";
            ResultSet resultSet = statement.executeQuery(queryAllSql);
            return getQuery(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Student> queryByClassId(String classId) {
        // TODO:
        String queryByClassIdSql = "SELECT * FROM student_jdbc WHERE classId = ?";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(queryByClassIdSql)) {
            preparedStatement.setString(1, classId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getQuery(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void update(String id, Student student) {
        // TODO:
        String updateSql = "UPDATE student_jdbc SET name = ?, gender = ?, admissionYear = ?, birthday = ?, classId = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getGender());
            preparedStatement.setInt(3, student.getAdmissionYear());
            preparedStatement.setDate(4, new Date(student.getBirthday().getTime()));
            preparedStatement.setString(5, student.getClassId());
            preparedStatement.setString(6, student.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        // TODO:
        String deleteSql = "DELETE FROM student_jdbc WHERE id = ?";
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(deleteSql)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Student> getQuery(ResultSet resultSet) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String gender = resultSet.getString("gender");
            int admissionYear = resultSet.getInt("admissionYear");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            String birthday = dateFormat.format(resultSet.getDate("birthday"));

            String classId = resultSet.getString("classId");
            students.add(new Student(id, name, gender, admissionYear, birthday, classId));
        }
        return students;
    }
}

