package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.demo.GlobalLeaderboardRedis;
import com.example.demo.Leaderboard;
import com.example.demo.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@RestController
public class EmployeeController
{
	@Autowired
	EmployeeRepository employeeRepository;

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck()
	{
		return "{ \"isWorking\" : true }";
	}

	@GetMapping("/employees")
	public List<Employee> getEmployees()
	{
		Iterable<Employee> result = employeeRepository.findAll();
		List<Employee> employeesList = new ArrayList<Employee>();
		result.forEach(employeesList::add);
		return employeesList;
	}

	@GetMapping("/employee/{id}")
	public Optional<Employee> getEmployee(@PathVariable String id)
	{
		Optional<Employee> emp = employeeRepository.findById(id);
		return emp;
	}

	@PutMapping("/employee/{id}")
	public Optional<Employee> updateEmployee(@RequestBody Employee newEmployee, @PathVariable String id)
	{
		Optional<Employee> optionalEmp = employeeRepository.findById(id);
		if (optionalEmp.isPresent()) {
			Employee emp = optionalEmp.get();
			emp.setFirstName(newEmployee.getFirstName());
			emp.setLastName(newEmployee.getLastName());
			emp.setEmail(newEmployee.getEmail());
			employeeRepository.save(emp);
		}
		return optionalEmp;
	}
	@GetMapping("/getall")
	public List<Leaderboard> getall()
	{
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
	@PutMapping("/update/{userId}/{score}")
	public Optional<Leaderboard> updateEmployee(@PathVariable String userId, @PathVariable Double score)
	{
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();
		Main.updateScore(userId, score);
		return Optional.ofNullable(globalLeader.getMyLeaderboardPosition(userId));
	}
	@PostMapping("/add/{userId}/{score}")
	public Leaderboard addEmployee(@PathVariable String userId, @PathVariable Double score)
	{
		GlobalLeaderboardRedis globalLeader = new GlobalLeaderboardRedis();
		Main.adduser(userId, score);
		Main.updateScore(userId, score);
		return globalLeader.getMyLeaderboardPosition(userId);
	}

	@DeleteMapping(value = "/employee/{id}", produces = "application/json; charset=utf-8")
	public String deleteEmployee(@PathVariable String id) {
		Boolean result = employeeRepository.existsById(id);
		employeeRepository.deleteById(id);
		return "{ \"success\" : "+ (result ? "true" : "false") +" }";
	}

	@PostMapping("/employee")
	public Employee addEmployee(@RequestBody Employee newEmployee)
	{
		String id = String.valueOf(new Random().nextInt());
		Employee emp = new Employee(id, newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getEmail());
		employeeRepository.save(emp);
		return emp;
	}

	@PostMapping("/hit")
	public Employee inputEmployee(@RequestBody Employee newEmployee)
	{
		String id = String.valueOf(new Random().nextInt());
		Employee emp = new Employee(id, newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getEmail());
		employeeRepository.save(emp);
		return emp;
	}
}
