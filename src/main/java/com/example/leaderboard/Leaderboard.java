package com.example.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

public class Leaderboard {

	private User user;
	private Long position;
	private Double score;

	public Long getPosition() {return position;}

	public void setPosition(Long position) {this.position = position;}

	public Double getScore() {return score;}

	public void setScore(Double score) {this.score = score;}

	public User getUser() {return user;}

	public void setUser(User user) {this.user = user;}

}
