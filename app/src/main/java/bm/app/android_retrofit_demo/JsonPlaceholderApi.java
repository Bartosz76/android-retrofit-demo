package bm.app.android_retrofit_demo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceholderApi {

    /**
     * This is a way of connecting to an external API in Android via Retrofit.
     * This is a Retrofit's annotation and "post" is the endpoint - the URL used is
     * https://jsonplaceholder.typicode.com/posts
     * This is called "relative URL", which means I am providing just the end of the endpoint. The
     * entire, base URL will be provided elsewhere.
     * Call object encapsulates a single request and response.
     */
    @GET("posts")
    Call<List<Post>> getPosts();

}
