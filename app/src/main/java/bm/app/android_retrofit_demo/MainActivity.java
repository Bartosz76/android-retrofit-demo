package bm.app.android_retrofit_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);

        // The logic to connect to the external API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create()) // Retrofit employs GSON for conversion.
                .build();

        /**
         * It's an interface, so I can't make an object out of it. Retrofit to the rescue though.
         * So this is the way to "instanciate" an interface with the help of Retrofit.
         */
        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<List<Post>> call = jsonPlaceholderApi.getPosts(); //I can call the method as Retrofit creates an implementation of it.

        /**
         * Now to actually execute the call.
         * I am not calling .execute() because this method will run asynchronously, so it will be
         * executed on whatever thread I am currently on and if I try to have two network operations
         * on the main thread, I will get an exception because then .execute() will freeze my app.
         * .enqueue() -> I have basically an anonymous class with two methods for two possible
         * scenarios.
         */
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                // This is triggered when there is a response... HTTP 404 is also a response.
                // So I need to cover that scenario.
                if (!response.isSuccessful()) {
                    // If my request was not successful -> I am displaying an error code to see
                    // what exactly went wrong.
                    result.setText("Code: " + response.code());
                    return;
                }

                // .body() contains the List of results.
                List<Post> posts = response.body();
                // Loop through the list and create the final result as a String by adding the
                // value of each field to it one by one.
                for (Post post: posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n"; // Two line break here, because another object starts afterwards!

                    result.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // If there is an error -> I am displaying a message what went wrong.
                result.setText(t.getMessage());
            }
        });
    }
}