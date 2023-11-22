package ch.hearc.spring.youtube;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;

import ch.hearc.spring.models.MyVideo;

public class UploadVideo {
	private static final String VIDEO_FILE_FORMAT = "video/*";

	private static YouTube youtube;
	
	public static Video upload(MyVideo video) throws IOException, Throwable, GoogleJsonResponseException {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");
	            // Authorize the request.
    	Credential credential = Auth.authorize(scopes, "uploadvideo");

        // This object is used to make YouTube Data API requests.
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                "youtube-cmdline-uploadvideo-sample").build();

        // Add extra information to the video before uploading.
        Video videoObjectDefiningMetadata = new Video();

        // Set the video to be publicly visible. This is the default
        // setting. Other supporting settings are "unlisted" and "private."
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus(video.getPrivacy());
        videoObjectDefiningMetadata.setStatus(status);

        // Most of the video's metadata is set on the VideoSnippet object.
        VideoSnippet snippet = new VideoSnippet();
        
        snippet.setTitle(video.getTitle());
        snippet.setDescription(video.getDescription());

        // Set the keyword tags that you want to associate with the video.
        List<String> tagsList = Arrays.asList(video.getTags());
        snippet.setTags(tagsList);

        // Add the completed snippet object to the video resource.
        videoObjectDefiningMetadata.setSnippet(snippet);

        InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, video.getFile().getInputStream());

        // Insert the video. The command sends three arguments. The first
        // specifies which information the API request is setting and which
        // information the API response should return. The second argument
        // is the video resource that contains metadata about the new video.
        // The third argument is the actual video content.
        YouTube.Videos.Insert videoInsert = youtube.videos()
                .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

        // Set the upload type and add an event listener.
        MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

        // Indicate whether direct media upload is enabled. A value of
        // "True" indicates that direct media upload is enabled and that
        // the entire media content will be uploaded in a single request.
        // A value of "False," which is the default, indicates that the
        // request will use the resumable media upload protocol, which
        // supports the ability to resume an upload operation after a
        // network interruption or other transmission failure, saving
        // time and bandwidth in the event of network failures.
        uploader.setDirectUploadEnabled(false);

        MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
            public void progressChanged(MediaHttpUploader uploader) throws IOException {
                switch (uploader.getUploadState()) {
                    case INITIATION_STARTED:
                        System.out.println("Initiation Started");
                        break;
                    case INITIATION_COMPLETE:
                        System.out.println("Initiation Completed");
                        break;
                    case MEDIA_IN_PROGRESS:
                        System.out.println("Upload in progress");
                        System.out.println("Upload percentage: " + uploader.getNumBytesUploaded());
                        break;
                    case MEDIA_COMPLETE:
                        System.out.println("Upload Completed!");
                        break;
                    case NOT_STARTED:
                        System.out.println("Upload Not Started!");
                        break;
                }
            }
        };
        uploader.setProgressListener(progressListener);
        
        // Call the API and upload the video.
        return videoInsert.execute();
	}
}
