import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FindFoodCountryHandler implements Command {
    @Override
    public void execute(String foodName) {
        try {
            String countryName = getFoodWithCountryNameFromOllama(foodName);
            System.out.println("The " + foodName + " is for: " + countryName);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private String getFoodWithCountryNameFromOllama(String foodName) throws Exception {
        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = "which country is " + foodName + " for ? Please answer in one word.";
            String requestBody = new JSONObject()
                    .put("model", "llama3.2")
                    .put("prompt", prompt)
                    .put("stream", false)
                    .toString();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            String countryName = jsonResponse.getString("response").trim();

            return countryName.replace(".", "");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

}
