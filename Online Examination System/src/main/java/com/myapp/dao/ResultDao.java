package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.myapp.utils.DBConnection;
import com.myapp.utils.ExamScore;
import com.myapp.utils.Result;
import com.myapp.utils.UserScore;

public class ResultDao {

	public int getResultId(int userId, int examId) {
		String query = "select * from results where user_id = ? and exam_id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, examId);
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

	public int addResult(Result result) {
		// TODO Auto-generated method stub
		String query = "insert into results(user_id, exam_id, score) values(?,?,?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, result.getUserId());
			preparedStatement.setInt(2, result.getExamId());
			preparedStatement.setInt(3, result.getScore());
			preparedStatement.executeUpdate();
			return getResultId(result.getUserId(), result.getExamId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void updateResult(Result result) {
		// TODO Auto-generated method stub
		String query = "update results set score = ? WHERE result_id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, result.getScore());
			preparedStatement.setInt(2, result.getResultId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ExamScore> getUserResults(int userId) {
		ArrayList<ExamScore> list = new ArrayList<ExamScore>();
		String query = "select e.exam_name, r.score from results r JOIN exams e on r.exam_id = e.exam_id where r.user_id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ExamScore examScore = new ExamScore();
				examScore.setExamName(resultSet.getString(1));
				examScore.setScore(resultSet.getInt(2));
				list.add(examScore);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<UserScore> getAdminScores(int examId) {
		ArrayList<UserScore> list = new ArrayList<UserScore>();
		String query = "select u.username, r.score from results r join users u on r.user_id = u.user_id where r.exam_id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, examId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				UserScore userScore = new UserScore();
				userScore.setUserName(resultSet.getString(1));
				userScore.setScore(resultSet.getInt(2));
				list.add(userScore);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
