package com.example.leaderboard;

import com.example.demo.repository.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class LeaderboardController
{
	@Autowired
	LeaderboardRepository leaderboardRepository;

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck()
	{
		return "{ \"isWorking\" : true }";
	}

	@GetMapping("/scores")
	public List<Leaderboard> getEmployees()
	{
		Iterable<Leaderboard> result = leaderboardRepository.findAll();
		List<Leaderboard> employeesList = new ArrayList<Leaderboard>();
		result.forEach(employeesList::add);
		return employeesList;
	}

	@GetMapping("/employee/{id}")
	public Optional<Leaderboard> getEmployee(@PathVariable String id)
	{
		Optional<Leaderboard> emp = leaderboardRepository.findById(id);
		return emp;
	}

	@PutMapping("/employee/{id}")
	public Optional<Leaderboard> updateEmployee(@RequestBody Leaderboard newEmployee, @PathVariable String id)
	{

		Optional<Leaderboard> optionalEmp = leaderboardRepository.findById(id);
		if (optionalEmp.isPresent()) {
			Leaderboard emp = optionalEmp.get();
			emp.setPosition(newEmployee.getPosition());
			emp.setScore(newEmployee.getScore());
			emp.setUser(newEmployee.getUser());
			leaderboardRepository.save(emp);
		}
		//Main.updateScore(id, score);

		return optionalEmp;
	}

	@DeleteMapping(value = "/leaderboard/{id}/{score}", produces = "application/json; charset=utf-8")
	public String deleteEmployee(@PathVariable String id, @PathVariable int score) {

		Boolean result = leaderboardRepository.existsById(id);
		leaderboardRepository.deleteById(id);
		return "{ \"success\" : "+ (result ? "true" : "false") +" }";
	}

	@PostMapping("/employee")
	public Leaderboard addEmployee(@RequestBody Leaderboard newEmployee)
	{
		String id = String.valueOf(new Random().nextInt());
		Leaderboard leaderboard = new Leaderboard();
		// Retrieving the user from hash set
		leaderboard.setUser(newEmployee.getUser());
		// Setting the position
		leaderboard.setPosition(newEmployee.getPosition());
		// Seting the score from the tuple
		leaderboard.setScore(newEmployee.getScore());
		leaderboardRepository.save(leaderboard);
		return leaderboard;
	}

}
