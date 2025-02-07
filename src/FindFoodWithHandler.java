import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FindFoodWithHandler implements Command {
    private static final String API_KEY = "cd21aa3d081f452183dacbf53585b853";

    @Override
    public void execute(String arguments) throws Exception {
        String ingredients = arguments.trim().replaceAll("\\s*,\\s*", ",");

        try {
            String foodName = getFoodName(ingredients);
            System.out.println("Suggested Food: " + foodName);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private String getFoodName(String ingredients) throws Exception {
        try {
            String urlString = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + ingredients + "&number=1&apiKey=" + API_KEY;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            if (jsonArray.length() == 0) {
                return "No food found!";
            }

            JSONObject recipeObj = jsonArray.getJSONObject(0);
            String recipeTitle = recipeObj.getString("title");
            return recipeTitle;

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }
}
