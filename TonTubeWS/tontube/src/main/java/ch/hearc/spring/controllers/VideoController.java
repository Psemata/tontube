package ch.hearc.spring.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.spring.config.JDBConnection;
import ch.hearc.spring.models.MyVideo;
import ch.hearc.spring.youtube.ShowVideo;
import ch.hearc.spring.youtube.UpdateVideo;
import ch.hearc.spring.youtube.UploadVideo;

@RestController
public class VideoController {
	/**
	 * List every video in the database
	 * @return An array of every video on the channel
	 */
	@GetMapping(value = "/api/video")
	public Map<Integer, Map<String, Object>> showAllVideo() {
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
		
		int number = 1;
		String result = "";
		Map<Integer, Map<String, Object>> map = new HashMap<Integer, Map<String, Object>>();
		try {
			Statement statement = mysqlCon.createStatement();

			ResultSet rs = statement.executeQuery("SELECT users.id as userId, users.username,videos.id as videoId FROM `videos` Join users on users.id = videos.fk_user;");

			while(rs.next()) {
				System.out.println(rs.getString("videoId"));
				result += rs.getString("videoId") + ",";
				map.put(number, new HashMap<String, Object>());
				map.get(number).put("username", rs.getString("username"));
				map.get(number).put("username_id", rs.getString("userId"));
				number++;
			}
		} catch (SQLException e) {
					
			e.printStackTrace();
		}

		result = result.substring(0, result.length() - 1);
		
		System.out.println(result);

		number = 1;
		VideoListResponse videoList;
		try {
			videoList = ShowVideo.showAllVideos(result);

			for(Video video : videoList.getItems()) {
				System.out.println(	video.getId());
				map.get(number).put("id", video.getId());
				map.get(number).put("title", video.getSnippet().getTitle());
				map.get(number).put("description", video.getSnippet().getDescription());
				map.get(number).put("publishedAt", video.getSnippet().getPublishedAt().getValue());
				map.get(number).put("duration", video.getContentDetails().getDuration());
				map.get(number).put("url_thumbnail", video.getSnippet().getThumbnails().getMaxres().getUrl());
				map.get(number).put("like", video.getStatistics().getLikeCount());
				map.get(number).put("view_count", video.getStatistics().getViewCount());
				number++;
			}

		} catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }

		System.out.println(map);
		
		return map;
	}

	@RequestMapping(value = "/api/video/{id}")
	public Map<Integer, Map<String, Object>> showOneVideo(@PathVariable(value="id") String id) {
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
		
		int number = 1;
		Map<Integer, Map<String, Object>> map = new HashMap<Integer, Map<String, Object>>();
		try {
			Statement statement = mysqlCon.createStatement();

			ResultSet rs = statement.executeQuery("SELECT users.id as userId, users.username,videos.id as videoId FROM `videos` Join users on users.id = videos.fk_user WHERE videos.id = \'" + id + "\';");

			while(rs.next()) {
				map.put(number, new HashMap<String, Object>());
				map.get(number).put("username", rs.getString("username"));
				map.get(number).put("username_id", rs.getString("userId"));

			}
		} catch (SQLException e) {
					
			e.printStackTrace();
		}

		VideoListResponse videoList;
		try {
			videoList = ShowVideo.showAllVideos(id);

			for(Video video : videoList.getItems()) {
				map.get(number).put("id", video.getId());
				map.get(number).put("title", video.getSnippet().getTitle());
				map.get(number).put("description", video.getSnippet().getDescription());
				map.get(number).put("publishedAt", video.getSnippet().getPublishedAt().getValue());
				map.get(number).put("duration", video.getContentDetails().getDuration());
				map.get(number).put("url_thumbnail", video.getSnippet().getThumbnails().getMaxres().getUrl());
				map.get(number).put("like", video.getStatistics().getLikeCount());
				map.get(number).put("view_count", video.getStatistics().getViewCount());
			}

		} catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
		
		return map;
	}

	@PostMapping(value = "/api/video/{id}/delete")
	public Map<String, Object> deleteVideo(@PathVariable(value="id") String id, @RequestHeader("Authorization") String token) {
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
		

		try {
			Statement statement = mysqlCon.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM tokens join users on users.id = tokens.fk_user Where tokens.token='" + token.replace("Bearer ", "") + "';");
			if(!rs.next()) {
				return Collections.singletonMap("error", "L'utilisateur n'existe pas");
			}
			int idUser = rs.getInt("fk_user");

			boolean isAdmin =  rs.getBoolean("isAdmin");


			if(isAdmin){
				statement.executeUpdate("Delete FROM videos Where videos.id='" + id + "';");
			}else{
				statement.executeUpdate("DELETE videos FROM `videos` join users on users.id = videos.fk_user WHERE videos.id = '" + id + "' AND users.id = '" + idUser + "';");
			}
			
			System.out.println(rs);
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.singletonMap("return", "La vidéo a été supprimée");
	}
	
	
	@GetMapping(value = "/api/video/user/{username}")
	public Map<String, Map<String, Object>> showAllVideoOfUser(@PathVariable String username) {
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
		
		String result = "";
		System.out.println(username);
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		try {
			Statement statement = mysqlCon.createStatement();

			ResultSet rs1 = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "';");
			if(!rs1.next()) {		
				map.put("error", new HashMap<String, Object>());
				map.get("error").put("user", "L'utilisateur n'existe pas");
				return map;
			}
			
			ResultSet rs = statement.executeQuery("SELECT users.id as userId, videos.id as videoId, username FROM `videos` JOIN `users` On users.id = videos.fk_user WHERE users.id ='" + rs1.getInt("id") + "' ;");

			while(rs.next()) {
				result += rs.getString("videoId") + ",";
				map.put(rs.getString("videoId"), new HashMap<String, Object>());
				map.get(rs.getString("videoId")).put("username", rs.getString("username"));
				map.get(rs.getString("videoId")).put("username_id", rs.getString("userId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		VideoListResponse videoList;
		try {
			videoList = ShowVideo.showAllVideos(result);

			for(Video video : videoList.getItems()) {
				map.get(video.getId()).put("id", video.getId());
				map.get(video.getId()).put("title", video.getSnippet().getTitle());
				map.get(video.getId()).put("description", video.getSnippet().getDescription());
				map.get(video.getId()).put("publishedAt", video.getSnippet().getPublishedAt().getValue());
				map.get(video.getId()).put("duration", video.getContentDetails().getDuration());
				map.get(video.getId()).put("url_thumbnail", video.getSnippet().getThumbnails().getMaxres().getUrl());
				map.get(video.getId()).put("like", video.getStatistics().getLikeCount());
				map.get(video.getId()).put("view_count", video.getStatistics().getViewCount());
			}

		} catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }

		return map;
	}
	
	@PostMapping(value = "/api/video/upload")
	public Map<String, String> uploadVideo(@ModelAttribute MyVideo video, @RequestHeader("Authorization") String token) {
        try {
        	Connection mysqlCon = JDBConnection.getInstance().getConnection();
			Statement statement = mysqlCon.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT * FROM tokens Where tokens.token='" + token.replace("Bearer ", "") + "';");
			if(!rs.next()) {
				return Collections.singletonMap("error", "L'utilisateur n'existe pas");
			}
			int id = rs.getInt("fk_user");

			System.out.println("id: " + id);
			
            Video returnedVideo = UploadVideo.upload(video);
			
			System.out.println("id: " + id);

            // Print data about the newly inserted video from the API response.
            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + returnedVideo.getId());
            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());	
            
			String query = "INSERT INTO videos (id, fk_user) VALUES ('" +
					returnedVideo.getId() + "','" + id + "');";
			
			statement.executeUpdate(query);

        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
        	e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
        
        return Collections.singletonMap("token", "uploaded");
	}

    /**
     * Update a video to youtube.
     */
	@PostMapping(value = "/api/video/update")
    public Map<String, String> updateVideo(@ModelAttribute MyVideo video) {
        // This OAuth 2.0 access scope allows for full read/write access to the
        // authenticated user's account.
        try{
			Video returnedVideo = UpdateVideo.update(video);

			// Print data about the newly inserted video from the API response.
			System.out.println("\n================== Returned Video ==================\n");
			System.out.println("  - Id: " + returnedVideo.getId());
			System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
			System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());

        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }

        return Collections.singletonMap("token", "updated");
    }

   
}
