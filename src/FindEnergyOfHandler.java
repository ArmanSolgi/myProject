import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FindEnergyOfHandler implements Command {
    private static final String API_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    private static final String APP_ID = "da13d288";
    private static final String API_KEY = "3607202ddcf29896d23953ba8b1fe798";

    @Override
    public void execute(String foodName) throws Exception {
        try {
            double calories = getCaloriesFor100g(foodName);

            if (calories != -1) {
                System.out.println("100g of " + foodName + " have " + calories + " cal.");
            } else {
                System.out.println("No calories found!");
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private double getCaloriesFor100g(String foodName) throws Exception {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("x-app-id", APP_ID);
            conn.setRequestProperty("x-app-key", API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"query\": \"" + foodName + " 100g\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("foods");
            JSONObject firstFood = jsonArray.getJSONObject(0);
            double calories = firstFood.getDouble("nf_calories");

            return calories;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1.0;
    }
}
