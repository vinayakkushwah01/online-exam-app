package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.myapp.utils.DBConnection;
import com.myapp.utils.User;

public class UserDao {
	public User getUser(String username, String password) {
		String query = "select * from users where username = ? and password = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
//				resultSet.next() will return true if the username and password exists
				User user = new User();
				user.setUserId(resultSet.getInt(1));
				user.setUsername(resultSet.getString(2));
				user.setPassword(resultSet.getString(3));
				user.setRole(resultSet.getString(4));
				return user;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getUserId(String userName) {
		String query = "select * from exams where username = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, userName);
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

	public void registerUser(User user) {
		String query = "insert into users(username, password, role) values(?,?,?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getRole());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
}
