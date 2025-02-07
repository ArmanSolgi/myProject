import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// spoonacular Api

public class FindRecipeOfHandler implements Command {
    private final static String API_KEY = "cd21aa3d081f452183dacbf53585b853";

    @Override
    public void execute(String foodName) throws Exception {
        try {
            String urlSearch = "https://api.spoonacular.com/recipes/complexSearch?query=" + foodName + "&apiKey=" + API_KEY;

            String searchResponse = sendRequest(urlSearch);
            JSONObject searchJson = new JSONObject(searchResponse);
            JSONArray results = searchJson.getJSONArray("results");

            if (results.length() == 0) {
                System.out.println("No recipe found for :" + foodName);
                return;
            }

            int recipeId = results.getJSONObject(0).getInt("id");

            String recipeUrl = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + API_KEY;
            String recipeResponse = sendRequest(recipeUrl);
            JSONObject recipeJson = new JSONObject(recipeResponse);

            String title = recipeJson.getString("title");

            JSONArray ingredientsArray = recipeJson.getJSONArray("extendedIngredients");
            StringBuilder ingredients = new StringBuilder();

            for (int i = 0; i < ingredientsArray.length(); i++) {
                JSONObject ingredient = ingredientsArray.getJSONObject(i);
                ingredients.append("_ ").append(ingredient.getString("original")).append("\n");
            }

            JSONArray stepArray = recipeJson.getJSONArray("analyzedInstructions");
            StringBuilder instructions = new StringBuilder();

            if (stepArray.length() > 0) {
                JSONArray steps = stepArray.getJSONObject(0).getJSONArray("steps");
                for (int i = 0; i < steps.length(); i++) {
                    JSONObject step = steps.getJSONObject(i);
                    instructions.append((i + 1)).append(". ").append(step.getString("step")).append("\n");
                }
            } else {
                System.out.println("No instructions available!");
            }

            System.out.println("Recipe for " + title + ":\n");
            System.out.println("Ingredients:\n" + ingredients);
            System.out.println("Instructions: \n" + instructions);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private String sendRequest(String urlString) throws Exception {
        try {
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
            return stringBuilder.toString();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }
}
