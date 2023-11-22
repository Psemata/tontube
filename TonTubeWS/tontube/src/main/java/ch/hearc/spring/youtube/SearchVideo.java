package ch.hearc.spring.youtube;

import java.io.IOException;
import java.util.List;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;


public class SearchVideo {
    
   private static final String CLIENT_SECRETS= "youtube/client_secrets.json";

	private static YouTube youtube;

	public static List<SearchResult> search(String queryTerm) throws IOException, Throwable, GoogleJsonResponseException {
		youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY,  new HttpRequestInitializer() {
           public void initialize(HttpRequest request) throws IOException {
           }
        }).setApplicationName("tontube").build();

        YouTube.Search.List search = youtube.search().list("id");

        // Set your developer key from the {{ Google Cloud Console }} for
        // non-authenticated requests. See:
        // {{ https://cloud.google.com/console }}
        search.setKey("AIzaSyBra5WDEENjKktwx3tJTTlXVR3d22xc_8Y");
        search.setQ(queryTerm);
        search.setChannelId("UCT-D6YtA-sCq1O5bvpy61LA");

        // Restrict the search results to only include videos. See:
        // https://developers.google.com/youtube/v3/docs/search/list#type
        search.setType("video");

        // To increase efficiency, only retrieve the fields that the
        // application uses.
        search.setFields("items(id/kind,id/videoId)");
        search.setMaxResults(25L);

        // Call the API and print results.
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        
        return searchResultList;
	}
}
