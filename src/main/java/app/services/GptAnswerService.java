package app.services;

import app.security.Passwds;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class GptAnswerService {
    private final Passwds passwds;

    @Autowired
    public GptAnswerService(Passwds passwds) {
        this.passwds = passwds;
    }
    private static final String URL = "https://api.openai.com/v1/completions";
    private static final String MODEL_ID = "text-davinci-003";

    public String callOpenAI(String message) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .callTimeout(69, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        JsonObject json = new JsonObject();
        json.addProperty("model", MODEL_ID);
        json.addProperty("prompt", message);
        json.addProperty("temperature", 0.0);
        json.addProperty("max_tokens", 1200);

        RequestBody body = RequestBody.create(json.toString(), mediaType);

        Request request = new Request.Builder()
                .url(URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + passwds.getApi_key())
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body() != null ? response.body().string() : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String extractTextFromResponse(String jsonResponse) {
        Gson gson = new Gson();
        GptAnswerResponse response = gson.fromJson(jsonResponse, GptAnswerResponse.class);

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            GptAnswerChoice choice = response.getChoices().get(0);
            return choice.getText();
        }

        return null;
    }
}
