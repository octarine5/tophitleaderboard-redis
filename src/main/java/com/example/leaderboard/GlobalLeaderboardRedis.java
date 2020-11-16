package com.example.leaderboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class GlobalLeaderboardRedis {
	private String globalLeaderboard = "globalRank";

	public void jedisInit(List<User> userList) {
		Jedis jedis = null;
		try {

			// Getting a connection from the pool
			jedis = JedisConfiguration.getPool().getResource();

			Map<String, Double> map = new HashMap<String, Double>();

			// For each user, creates a hash set and put in a map
			for (User user : userList) {
				map.put(user.getId(), 0.0);
				jedis.hset(user.getId(), "id", user.getId());
				jedis.hset(user.getId(), "username", user.getUsername());
				jedis.hset(user.getId(), "score", "0");
				System.out.println(user.getUsername() + user.getScore());
			}

			// Adding all the keys(userId) related to value(score) in our sorted list
			System.out.println(map.toString());
			if (map.size() != 0) jedis.zadd(globalLeaderboard, map);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (jedis != null) {
				// Closing connection to
				jedis.close();
			}
		}

	}

	public List<Leaderboard> getGlobalLeaderboardRedis() {
		Jedis jedis = null;
		List<Leaderboard> leaderboardList = new ArrayList<Leaderboard>();
		try {
			// Getting a connection from the pool
			jedis = JedisConfiguration.getPool().getResource();

			// The Tuple type has two values: key and score
			// zrevrange gets from the bigger score to the lower
			// It is being limited just for the first 100 positions
			// For all positions, pass 0 and -1
			Set<Tuple> tupleList = jedis.zrevrangeWithScores(globalLeaderboard, 0, 99);

			long position = 0;

			// Iterating over the tuples
			for (Tuple tuple : tupleList) {
				// When using zrevrange you need to handle the positions
				position++;
				Leaderboard leaderboard = new Leaderboard();
				// Retrieving the user from hash set
				leaderboard.setUser(this.getUserFromHashSet(tuple.getElement(), jedis));
				// Setting the position
				leaderboard.setPosition(position);
				// Seting the score from the tuple
				leaderboard.setScore(tuple.getScore());

				// Adding the LeaderboardList
				leaderboardList.add(leaderboard);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return leaderboardList;

	}

	public User getUserFromHashSet(String userId, Jedis jedis) {
		User user = null;

		try {
			jedis = JedisConfiguration.getPool().getResource();
			if (jedis.exists(userId)) {
				user = new User();
				user.setId(jedis.hget(userId, "id"));
				user.setUsername(jedis.hget(userId, "username"));
				user.setScore(Double.valueOf(jedis.hget(userId, "score")));

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return user;

	}

	public void updateScore(String userId, double score) {
		Jedis jedis = null;
		try {
			// Getting a connection from the pool
			jedis = JedisConfiguration.getPool().getResource();

			// Updating the user score with zadd. With zadd you overwrite the score.
			jedis.zadd(globalLeaderboard, score, userId);

			// With zincrby you add a value to the current score
			// jedis.zincrby(globalLeaderboard, score, userId);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}
	public void incrScore(String userId) {
		Jedis jedis = null;
		try {
			// Getting a connection from the pool
			jedis = JedisConfiguration.getPool().getResource();

			// Updating the user score with zadd. With zadd you overwrite the score.
			jedis.zincrby(globalLeaderboard, 1, userId);

			// With zincrby you add a value to the current score
			// jedis.zincrby(globalLeaderboard, score, userId);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}
	public Leaderboard getMyLeaderboardPosition(String userId) {

		Leaderboard leaderboard = null;
		Jedis jedis = null;

		try {
			// Getting a connection from the pool
			jedis = JedisConfiguration.getPool().getResource();

			// Getting the user properties from the hash set
			User user = this.getUserFromHashSet(userId, jedis);

			if (user != null) {
				// zrevrank retrieves the user position at the sorted set
				Long position = jedis.zrevrank(globalLeaderboard, userId);

				// Adding one to the position because it starts at 0
				if (position != null) {
					position = position + 1;
				}

				// zscore retrieves the user current score
				Double value = jedis.zscore(globalLeaderboard, userId);

				// If both values found, create a Leaderboard object
				if (position == null || value == null) {
					leaderboard = null;
				} else {
					leaderboard = new Leaderboard();
					leaderboard.setPosition(position);
					leaderboard.setScore(value);
					leaderboard.setUser(user);
				}

			}
		} catch (Exception ex) {

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return leaderboard;
	}
}
