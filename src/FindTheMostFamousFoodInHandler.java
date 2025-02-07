import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// llama3.2
public class FindTheMostFamousFoodInHandler implements Command {
    @Override
    public void execute(String countryName) throws Exception {
        try {
            String foodName = getFamousFoodInCountryFromOllama(countryName);
            System.out.println("The most famous food in " + countryName + " is: " + foodName);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private String getFamousFoodInCountryFromOllama(String countryName) throws Exception {
        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = "What is the most famous food in " + countryName + "? Please answer in one word.";
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
            String foodName = jsonResponse.getString("response").trim();

            return foodName.replace(".", "");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
