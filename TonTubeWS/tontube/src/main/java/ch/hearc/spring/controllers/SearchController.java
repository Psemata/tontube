package ch.hearc.spring.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.spring.config.JDBConnection;
import ch.hearc.spring.youtube.SearchVideo;
import ch.hearc.spring.youtube.ShowVideo;

@RestController
public class SearchController {
    @GetMapping("api/search")
    public Map<String, Object> search(@RequestParam("query") String query) {
        Connection mysqlCon = JDBConnection.getInstance().getConnection();

        String result = "";
        Map<String, Map<String, Object>> mapVideo = new HashMap<String, Map<String, Object>>();
        List<SearchResult> searchResults;
        try {
            searchResults = SearchVideo.search(query);

            for (SearchResult video : searchResults) {
                result += video.getId().getVideoId() + ",";
                mapVideo.put(video.getId().getVideoId(), new HashMap<String, Object>());
            }
            
            result = result.substring(0, result.length() - 1);

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

        try {
            Statement statement = mysqlCon.createStatement();

            ResultSet rs = statement.executeQuery(
                    "SELECT users.id as userId, users.username,videos.id as videoId FROM `videos` Join users on users.id = videos.fk_user;");

            while (rs.next()) {
                // if map contains the video id
                if (mapVideo.containsKey(rs.getString("videoId"))) {
                    mapVideo.get(rs.getString("videoId")).put("username", rs.getString("username"));
                    mapVideo.get(rs.getString("videoId")).put("username_id", rs.getString("userId"));
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        VideoListResponse videoList;
        try {
            System.out.println(result);
            videoList = ShowVideo.showAllVideos(result);

            for (Video video : videoList.getItems()) {
                System.out.println(video.getId());
                mapVideo.get(video.getId()).put("id", video.getId());
                mapVideo.get(video.getId()).put("title", video.getSnippet().getTitle());
                mapVideo.get(video.getId()).put("description", video.getSnippet().getDescription());
                mapVideo.get(video.getId()).put("publishedAt", video.getSnippet().getPublishedAt().getValue());
                mapVideo.get(video.getId()).put("duration", video.getContentDetails().getDuration());
                mapVideo.get(video.getId()).put("url_thumbnail", video.getSnippet().getThumbnails().getMaxres().getUrl());
                mapVideo.get(video.getId()).put("like", video.getStatistics().getLikeCount());
                mapVideo.get(video.getId()).put("view_count", video.getStatistics().getViewCount());
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

        Map<String, Map<String, Object>> mapUser = new HashMap<String, Map<String, Object>>();
        try {
            Statement statement = mysqlCon.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM `users` WHERE username LIKE '%" + query + "%';");

            while (rs.next()) {
                mapUser.put(rs.getString("id"), new HashMap<String, Object>());
                mapUser.get(rs.getString("id")).put("id", rs.getString("id"));
                mapUser.get(rs.getString("id")).put("username", rs.getString("username"));
                mapUser.get(rs.getString("id")).put("email", rs.getString("email"));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("users", mapUser);
        map.put("videos", mapVideo);

        return map; // put map to return user and video !! Attention !! change the return type
    }

    @GetMapping("api/search/{id}")
	public Map<String, Map<String, Object>> searchUser(@PathVariable("id") String id ,@RequestParam("query") String query) {
		Connection mysqlCon = JDBConnection.getInstance().getConnection();
        
        String result = "";
        Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		
		try {
            List<SearchResult> searchResults;
			searchResults = SearchVideo.search(query);
			
			if(searchResults.isEmpty()) {
                map.put("error", new HashMap<String, Object>());
                map.get("error").put("search", "no result");
				return map;
			}

			for(SearchResult video : searchResults) {
                result+=video.getId().getVideoId()+",";
			}

            result = result.substring(0, result.length()-1);
            String[] tabId = result.split(",");
            result = "";

			Statement statement = mysqlCon.createStatement();

			ResultSet rs = statement.executeQuery("SELECT users.id as userId, users.username,videos.id as videoId FROM `videos` Join users on users.id = videos.fk_user where users.id ='" + id +"';");

			while(rs.next()) {
                if(Arrays.asList(tabId).contains(rs.getString("videoId"))){
                    result+=rs.getString("videoId")+",";
                    map.put(rs.getString("videoId"), new HashMap<String, Object>());
                    map.get(rs.getString("videoId")).put("username", rs.getString("username"));
                    map.get(rs.getString("videoId")).put("username_id", rs.getString("userId"));
                }
			}

            if(result.equals("")) {
                map.put("error", new HashMap<String, Object>());
                map.get("error").put("search", "no result");
				return map;
			}

            result = result.substring(0, result.length()-1);

            VideoListResponse videoList;
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

		} catch (SQLException e) {
			e.printStackTrace();
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
}
