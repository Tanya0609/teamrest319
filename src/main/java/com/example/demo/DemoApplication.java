package com.example.demo;

import modules.Portfolio;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.sql.ResultSet;
import java.util.HashMap;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@SpringBootApplication
@RestController
public class DemoApplication {

	@GetMapping("/index")
	String home() {
		return "Welcome to Rebalance Fund Service System - Team REST!";
	}

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RequestMapping(value = "/portfolio/{id}" ,method = POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Portfolio> createInitialPreference(@PathVariable String id, @RequestBody Portfolio portfolio) {
        ArrayList<String> fundIds = new ArrayList<>();
        ArrayList<Integer> precentages = new ArrayList<>();
        if (portfolio != null) {
            portfolio.setId(id);
            // TODO: check fundIds are valid ids
            // TODO: check allocations sum up to 100
            // TODO: check devication is [0-5]
            portfolio.getAllocations().keySet().forEach(fundId ->{
                fundIds.add(fundId);
                precentages.add(portfolio.getAllocations().get(fundId).intValue());
            });
        }
        try
        {
            Connection conn = setUpConnection();
            if(conn == null){
                return new ResponseEntity<>(portfolio, HttpStatus.BAD_REQUEST);
            }
            // mysql insert statement for portfolio table
            String query = " insert into portfolio (id, deviation, type)"
                    + " values (?, ?, ?)";

            // create the mysql insert preparedstatement for portfolio
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, Integer.parseInt(portfolio.getId()));
            preparedStmt.setInt    (2, Integer.parseInt(portfolio.getDeviation()));
            preparedStmt.setString (3, portfolio.getType());
            preparedStmt.execute();

            // create the mysql insert preparedstatement for allocations
            for (String fundId : fundIds) {
                String allocationsInsertQuery = " insert into allocations (fund_id, portfolio_id, percentage)"
                        + " values (?, ?, ?)";
                PreparedStatement preparedStmtAllocation = conn.prepareStatement(allocationsInsertQuery);
                preparedStmtAllocation.setInt (1, Integer.parseInt(fundId));
                preparedStmtAllocation.setInt(2, Integer.parseInt(portfolio.getId()));
                preparedStmtAllocation.setInt    (3, portfolio.getAllocations().get(fundId));
                preparedStmtAllocation.execute();
            }

            conn.close();
        }catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }

	@RequestMapping(value = "/portfolio/{id}/deviation", method = PUT, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Integer>> updateDeviation(@PathVariable String id, @RequestBody Map<String, Integer> request) {
		int deviation = request.getOrDefault("deviation", -1);
		if (!request.containsKey("deviation") || deviation < 0 || deviation > 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try
		{
			// create a mysql database connection
			Connection conn = setUpConnection();

			// mysql update statement for portfolio table
			String query = "update portfolio set deviation = ? where id = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt   (1, deviation);
			preparedStmt.setInt(2, Integer.parseInt(id));
			preparedStmt.executeUpdate();
			preparedStmt.close();
			conn.close();
		}catch (SQLException e)
		{
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
		}
		return new ResponseEntity<>(request, HttpStatus.OK);
	}

    @RequestMapping(value = "/portfolio/{id}" ,method = GET)
    @ResponseBody
    public ResponseEntity<Portfolio> getInitialPreference(@PathVariable String id) {
        Portfolio portfolio = new Portfolio();
        // TODO: verify if portfolio id is a valid portfolio of the customer id past from header
        // TODO: If the portfolio id is not valid, return HttpStatus.BAD_REQUEST
        portfolio.setId(id);
        Connection conn = setUpConnection();
        if(conn == null){
            return new ResponseEntity<>(portfolio, HttpStatus.BAD_REQUEST);
        }
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT * from portfolio WHERE  id = " + id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String deviation = rs.getString(2);
                portfolio.setDeviation(deviation);
                String type = rs.getString(3);
                portfolio.setType(type);
            }

            PreparedStatement allocationsStatement = conn.prepareStatement("select * from allocations where portfolio_id =" +id);
            ResultSet data = allocationsStatement.executeQuery();

            HashMap<String,Integer> allocations = new HashMap<>();

            while (data.next()) {
                String fundId = data.getString(1);
                Integer percentage = Integer.parseInt(data.getString(3));
                allocations.put(fundId,percentage);
            }
            portfolio.setAllocations(allocations);
            conn.close();

        }catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

        return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }

    private Connection setUpConnection() {
		try {
			String myDriver = "com.mysql.jdbc.Driver";
			String myUrl = "jdbc:mysql://localhost:3306/hsbc_db_server";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "password");
			return conn;
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
		}

		return null;
	}
}