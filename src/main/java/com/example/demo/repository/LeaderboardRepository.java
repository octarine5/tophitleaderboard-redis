package com.example.demo.repository;
import com.example.leaderboard.Leaderboard;
import org.springframework.data.repository.CrudRepository;
public interface LeaderboardRepository extends CrudRepository<Leaderboard, String> {

}
