package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Main.initRedis();

		while (1 == 1) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter your username: ");
			// get their input as a String
			String username = scanner.next();

			// prompt for their score
			System.out.print("Enter your score: ");

			// get the age as an int
			Double score = scanner.nextDouble();
			System.out.print("Enter your option: ");

			String option = scanner.next();
			if (option.equals("add")) {
				System.out.println(String.format("%s, your age is %4.2f", username, score));
				Main.adduser(username, score);
				Main.updateScore(username, score);

			}
			else if (option.equals("update")) {
				System.out.println(String.format("%s, your age is %4.2f", username, score));
				Main.updateScore(username, score);
			}
			else {
				Main.incrScore(username);
			}
			Main.printLeaderboard();
			System.out.println("//////////////////////////////");
			Main.printLeaderboardSingleUser(username);
		}


	}

	public static void initRedis() {
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();

		List<User> userList = new ArrayList<User>();
		User user1 = new User();
		user1.setId("John");
		user1.setUsername("John");

		User user2 = new User();
		user2.setId("Paul");
		user2.setUsername("Paul");

		User user3 = new User();
		user3.setId("George");
		user3.setUsername("George");

		User user4 = new User();
		user4.setId("Ringo");
		user4.setUsername("Ringo");

		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		userList.add(user4);
		globalLeader.jedisInit(userList);
	}

	public static void updateScore(String userId, double score) {
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();
		globalLeader.updateScore(userId, score);
	}
	public static void incrScore(String userId) {
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();
		globalLeader.incrScore(userId);
	}


	public static List<Leaderboard> printLeaderboard() {
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();
		List<Leaderboard> leaderboardList = new ArrayList<Leaderboard>();
		leaderboardList = globalLeader.getGlobalLeaderboardRedis();
		//List<String>
		for (Leaderboard leaderboard : leaderboardList) {
			if (leaderboard.getUser() == null) {
				System.out.println("Username: " + leaderboard.getUser() + " Score: " + leaderboard.getScore()
						+ " Position: " + leaderboard.getPosition());
				continue;
			}
			System.out.println("Username: " + leaderboard.getUser().getUsername() + " Score: " + leaderboard.getScore()
					+ " Position: " + leaderboard.getPosition());

		}
		return leaderboardList;
	}

	public static void adduser(String username, Double score) {
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();

		List<User> userList = new ArrayList<User>();
		User user1 = new User();
		//Random ran = new Random();
		user1.setScore(score);
		user1.setId(username);
		user1.setUsername(username);
		userList.add(user1);
		System.out.println(user1.toString());
		globalLeader.jedisInit(userList);
	}


	public static void printLeaderboardSingleUser(String userId) {
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();
		Leaderboard leaderboard = new Leaderboard();
		leaderboard = globalLeader.getMyLeaderboardPosition(userId);

		System.out.println("Username: " + leaderboard.getUser().getUsername() + " Score: " + leaderboard.getScore()
				+ " Position: " + leaderboard.getPosition());

	}

}
