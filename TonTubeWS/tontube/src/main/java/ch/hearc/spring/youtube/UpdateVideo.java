package ch.hearc.spring.youtube;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.common.collect.Lists;

import ch.hearc.spring.models.MyVideo;

public class UpdateVideo {

	private static YouTube youtube;
	
	public static Video update(MyVideo myVideo) throws IOException, Throwable, GoogleJsonResponseException {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        
        // Authorize the request.
        Credential credential = Auth.authorize(scopes, "updatevideo");

        // This object is used to make YouTube Data API requests.
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                .setApplicationName("youtube-cmdline-updatevideo-sample").build();

        // Prompt the user to enter the video ID of the video being updated.
        String videoId = myVideo.getId();
        System.out.println("You chose " + videoId + " to update.");

        // Call the YouTube Data API's youtube.videos.list method to
        // retrieve the resource that represents the specified video.
        YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet").setId(videoId);
        VideoListResponse listResponse = listVideosRequest.execute();

        // Since the API request specified a unique video ID, the API
        // response should return exactly one video. If the response does
        // not contain a video, then the specified video ID was not found.
        List<Video> videoList = listResponse.getItems();
        if (videoList.isEmpty()) {
            System.out.println("Can't find a video with ID: " + videoId);
        }

        // Extract the snippet from the video resource.
        Video video = videoList.get(0);
        VideoSnippet snippet = video.getSnippet();

        snippet.setTitle(myVideo.getTitle());
        snippet.setDescription(myVideo.getDescription());
        snippet.setTags(Arrays.asList(myVideo.getTags()));

        // Update the video resource by calling the videos.update() method.
        YouTube.Videos.Update updateVideosRequest = youtube.videos().update("snippet", video);
        return updateVideosRequest.execute();

	}
}
