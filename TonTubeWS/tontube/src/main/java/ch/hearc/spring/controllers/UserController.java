package ch.hearc.spring.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.spring.config.JDBConnection;
import ch.hearc.spring.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {
	PasswordEncoder e = new BCryptPasswordEncoder();
	
	@PostMapping("api/auth/login")
	public Map<String, String> login(@RequestBody User user) {
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
		try {
			Statement statement = mysqlCon.createStatement();
			String query = ("SELECT * FROM tokens JOIN users ON tokens.fk_user = users.id WHERE users.username='" + user.getUsername() + "';");
			ResultSet rs = statement.executeQuery(query);
			
			if(!rs.next()) {
				return Collections.singletonMap("error", "Le nom d'utilisateur ou le mot de passe est erroné");
			}
			
			if(!e.matches(user.getPassword(), rs.getString("password"))) {
				return Collections.singletonMap("error", "Le nom d'utilisateur ou le mot de passe est erroné");
			}
			
			return Collections.singletonMap("token", rs.getString("token"));
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.singletonMap("error", "Il y a eu une erreur dans le traitement de la requête");
		}
	}
	
	@PostMapping("api/auth/register")
	public Map<String, String> register(@RequestBody User user) {	
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
		try {
			Statement statement = mysqlCon.createStatement();
			String query = "INSERT INTO users (id, username, email, password) VALUES (NULL, '" +
					user.getUsername() + "','" + user.getEmail() + "','" + e.encode(user.getPassword()) + "');";
			statement.executeUpdate(query);
			String token = getJWTToken(user.getUsername());
			
			ResultSet rs = statement.executeQuery("SELECT id FROM users where email='" + user.getEmail() + "';");
			int id = 0;
			if(!rs.next()) {
				return Collections.singletonMap("error", "Il y a eu une erreur dans le traitement de la requête");
			}
			id = rs.getInt("id");
			
			query = "INSERT INTO `tokens` (`id`, `fk_user`, `token`) VALUES (NULL, '" + id + "','" + token + "');";
			statement.executeUpdate(query);
			
			return Collections.singletonMap("result", "L'utilisateur a bien été créé");
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.singletonMap("error", "Il y a eu une erreur dans le traitement de la requête");
		}
	}
	
	@GetMapping("api/auth/user")
	public Map<String, Object> showUser(@RequestHeader("Authorization") String token) {
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
		String username = "";
		String email = "";
		Boolean isAdmin = false;

		try {
			Statement statement = mysqlCon.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM users JOIN tokens on users.id = tokens.fk_user Where tokens.token='" + token.replace("Bearer ", "") + "';");
			if(!rs.next()) {
				HashMap<String, String> error = new HashMap<String,String>();
				error.put("error", "L'utilisateur n'existe pas");
				HashMap<String, Object> response = new HashMap<String,Object>();
				response.put("error", error);
				return response;				
			}
			username = rs.getString("username");
			email = rs.getString("email");	
			isAdmin = rs.getBoolean("isAdmin");
					
		} catch (Exception e) {
					
			e.printStackTrace();

			HashMap<String, String> error = new HashMap<String,String>();
			error.put("error", "Il y a eu une erreur dans le traitement de la requête");
			HashMap<String, Object> response = new HashMap<String,Object>();
			response.put("error", error);
			return response;
		}
		HashMap<String, Object> user = new HashMap<String, Object>();
		user.put("username", username);
		user.put("email", email);
		user.put("isAdmin", isAdmin);
		HashMap<String, Object> response = new HashMap<String,Object>();
		response.put("user", user);
		return response;
	}
	
	private String getJWTToken(String username) {
		String secretKey = "T0NTU83-H34RC";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(null)
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return token;
	}
}