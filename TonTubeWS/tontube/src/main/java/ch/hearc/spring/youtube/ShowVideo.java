package ch.hearc.spring.youtube;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

public class ShowVideo {
    private static final String CLIENT_SECRETS= "youtube/client_secrets.json";

	private static YouTube youtube;
	
	public static VideoListResponse showAllVideos(String ids) throws IOException, Throwable, GoogleJsonResponseException {

                // Properties properties = new Properties();
                // try {
                //     InputStream in = ShowVideo.class.getResourceAsStream(CLIENT_SECRETS);
                //     properties.load(in);
        
                // } catch (IOException e) {
                //     System.err.println("There was an error reading " + CLIENT_SECRETS + ": " + e.getCause()
                //             + " : " + e.getMessage());
                //     System.exit(1);
                // }


                // This object is used to make YouTube Data API requests.
                youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY,  new HttpRequestInitializer() {
                        public void initialize(HttpRequest request) throws IOException {
                        }
                }).setApplicationName("tontube").build();

                YouTube.Videos.List request = youtube.videos().list("snippet,contentDetails,statistics");

                // String apiKey = properties.getProperty("youtube.apikey");
                //request.setKey(apiKey);
                request.setKey("AIzaSyBra5WDEENjKktwx3tJTTlXVR3d22xc_8Y");
                VideoListResponse response = request.setId(ids).execute();
                return response;
	}
}
