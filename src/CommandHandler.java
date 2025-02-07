public class CommandHandler {
    public void handleCommand(String command) throws Exception {
        try {
            if (command.startsWith("findFoodWith")) {
                String ingredients = command.substring("findFoodWith:".length()).trim();
                new FindFoodWithHandler().execute(ingredients);
            } else if (command.startsWith("findFoodCountry")) {
                String foodName = command.substring("findFoodCountry:".length()).trim();
                new FindFoodCountryHandler().execute(foodName);
            } else if (command.startsWith("findTheMostFamousFoodIn")) {
                String countryName = command.substring("findTheMostFamousFoodIn:".length()).trim();
                new FindTheMostFamousFoodInHandler().execute(countryName);
            } else if (command.startsWith("findEnergyOf")) {
                String foodName = command.substring("findEnergyOf:".length()).trim();
                new FindEnergyOfHandler().execute(foodName);
            } else if (command.startsWith("findRecipeOf")) {
                String foodName = command.substring("findRecipeOf:".length()).trim();
                new FindRecipeOfHandler().execute(foodName);
            } else {
                System.out.println("UnKnown command");
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
