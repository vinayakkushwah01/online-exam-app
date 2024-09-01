package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.myapp.utils.DBConnection;
import com.myapp.utils.Exam;

public class ExamDao {
	public ArrayList<Exam> getAllExams() {
		ArrayList<Exam> list = new ArrayList<>();
		String query = "select * from exams";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Exam exam = new Exam();
				exam.setId(resultSet.getInt(1));
				exam.setExamName(resultSet.getString(2));
				exam.setDescription(resultSet.getString(3));
				list.add(exam);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getExamId(String examName) {
		String query = "select * from exams where exam_name = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, examName);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				return resultSet.getInt(1);
			else
				return -1;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int addExam(Exam exam) {
		String query = "insert into exams(exam_name, description) values(?,?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, exam.getExamName());
			preparedStatement.setString(2, exam.getDescription());
			preparedStatement.executeUpdate();
			return getExamId(exam.getExamName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void deleteExam(int examId) {
		// TODO Auto-generated method stub
		String deleteQuestionsQuery = "DELETE FROM questions WHERE examId = ?;";
		String deleteResultsQuery = "DELETE FROM results WHERE exam_id = ?;";
		String deleteExamQuery = "DELETE FROM exams WHERE exam_id = ?;";

		try (Connection connection = DBConnection.getConnection()) {
			// Start transaction
			connection.setAutoCommit(false);

			// Delete from questions table
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuestionsQuery)) {
				preparedStatement.setInt(1, examId);
				preparedStatement.executeUpdate();
			}

			// Delete from results table
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteResultsQuery)) {
				preparedStatement.setInt(1, examId);
				preparedStatement.executeUpdate();
			}

			// Delete from exams table
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteExamQuery)) {
				preparedStatement.setInt(1, examId);
				preparedStatement.executeUpdate();
			}

			// Commit transaction
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try (Connection connection = DBConnection.getConnection()) {
				connection.rollback(); // Rollback transaction if any error occurs
			} catch (SQLException rollbackException) {
				rollbackException.printStackTrace();
			}
		}
	}
}
