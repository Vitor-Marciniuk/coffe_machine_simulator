package machine;

import java.util.*;

class StockedMachineMaterial{
    String materialName;
    String metricType;
    int stockedResource;

    public StockedMachineMaterial(String materialName, String metricType, int stockedResource) {
        this.materialName = materialName;
        this.metricType = metricType;
        this.stockedResource = stockedResource;
    }
}

class Recipe{
    String recipeName;
    ArrayList<Ingredient> recipeIngredients;
    int value;

    public Recipe(String recipeName, ArrayList<Ingredient> ingredients, int value){
        this.recipeName = recipeName;
        this.recipeIngredients = ingredients;
        this.value = value;
    }
}

class Ingredient{
    String ingredientName;
    String metricType;
    int quantityToMake;

    public Ingredient(String ingredientName, String metricType, int quantity) {
        this.ingredientName = ingredientName;
        this.metricType = metricType;
        this.quantityToMake = quantity;
    }
}

public class CoffeeMachine {
    static Scanner sc = new Scanner(System.in);
    static StringBuilder sb = new StringBuilder();
    static ArrayList<StockedMachineMaterial> stockedMachineMaterials = new ArrayList<>();
    static ArrayList<Recipe> recipes = new ArrayList<>();
    static ArrayList<Ingredient> ingredients = new ArrayList<>();
    static int money = 550;
    static boolean end = true;

    public static void main(String[] args) {
        initialize();
    }

    public static void initialize(){
        stockedMachinematerials();
        createRecipes();
        buyFillOrTake();
    }

    public static void stockedMachinematerials(){
        StockedMachineMaterial water = new StockedMachineMaterial("water", "ml", 400);
        stockedMachineMaterials.add(water);
        StockedMachineMaterial milk = new StockedMachineMaterial("milk", "ml", 540);
        stockedMachineMaterials.add(milk);
        StockedMachineMaterial coffeBeans = new StockedMachineMaterial("coffe beans", "grams", 120);
        stockedMachineMaterials.add(coffeBeans);
        StockedMachineMaterial disposableCups = new StockedMachineMaterial("disposable cups", "un", 9);
        stockedMachineMaterials.add(disposableCups);
    }

    public static ArrayList<Ingredient> createIngredientsForRecipe(
            int waterQuantity, int milkQuantity,int coffeBeansQuantity, int cupsQuantity){
        Ingredient water = new Ingredient("water", "ml", waterQuantity);
        ingredients.add(water);
        Ingredient milk = new Ingredient("milk", "ml", milkQuantity);
        ingredients.add(milk);
        Ingredient coffeBeans = new Ingredient("coffe beans", "g", coffeBeansQuantity);
        ingredients.add(coffeBeans);
        Ingredient disposableCups = new Ingredient("disposable cups", "un", cupsQuantity);
        ingredients.add(disposableCups);
        return ingredients;
    }

    public static void createRecipes(){
        recipes.add(new Recipe("espresso",
                new ArrayList<>(createIngredientsForRecipe(
                        250,
                        0,
                        16,
                        1)),
                4));
        ingredients.clear();

        recipes.add(new Recipe("latte",
                new ArrayList<>(createIngredientsForRecipe(
                        350,
                        75,
                        20,
                        1)),
                7));
        ingredients.clear();

        recipes.add(new Recipe("cappuccino",
                new ArrayList<>(createIngredientsForRecipe(
                        200,
                        100,
                        12,
                        1)),
                6));
        ingredients.clear();
    }

    public static void printMachineIngredients(){
        System.out.println("The coffe machine has:");
        for(StockedMachineMaterial material : stockedMachineMaterials){
            System.out.println(material.stockedResource + " " + material.metricType +" of "+ material.materialName);
        }
        System.out.println("$"+ money +" of money");
    }

    public static void fillMaterialToMachine(){
        for(StockedMachineMaterial material : stockedMachineMaterials){
            System.out.println("Write how many " + material.metricType + " of " + material.materialName
                    + " you want to add:");
            material.stockedResource = material.stockedResource + sc.nextInt();
        }
    }

    public static StockedMachineMaterial findStockedMachineMaterial(String ingredientName){
        return stockedMachineMaterials.stream()
                .filter(m -> m.materialName.equals(ingredientName))
                .findFirst()
                .orElse(null);
    }

    public static void selectOptionOfCoffe(){
        String option = sc.nextLine();
        try{
            int optionSelected = Integer.parseInt(option);
            validateCoffeOptionMaterial(recipes.get(optionSelected - 1));
        } catch (NumberFormatException e){
            selectAction();
        }
    }

    public static void buy(){
        int option = 1;
        sb = new StringBuilder();
        sb.append("What do you want to buy? ");
        for(Recipe recipe : recipes){
            sb.append(option + " - " + recipe.recipeName);
            if(recipes.size() == option)
                sb.append(":");
            else
                sb.append(", ");
            option++;
        }
        System.out.println(sb);
        selectOptionOfCoffe();
    }

    public static void fill(){
        fillMaterialToMachine();
    }

    public static void take(){
        System.out.println("I gave you $" + money);
        money = 0;
    }

    public static void exit(){
        end = !end;
    }

    public static void selectAction(){
        while (end) {
            System.out.println("Write action (buy, fill, take, remaining, exit): ");
            String action = sc.nextLine();
            switch (action){
                case "buy":
                    buy(); break;
                case "fill":
                    fill(); break;
                case "take":
                    take(); break;
                case "remaining":
                    printMachineIngredients(); break;
                case "exit":
                    exit(); break;
                default:
                    System.out.println("not recognized!"); break;
            }
        }
    }

    public static void buyFillOrTake(){
        selectAction();
    }

    public static void calculateSurplusAmountOfMaterial(ArrayList<Ingredient> ingredients){
        for(Ingredient ingredient : ingredients){
            StockedMachineMaterial stockedMaterial = findStockedMachineMaterial(ingredient.ingredientName);
            stockedMaterial.stockedResource = stockedMaterial.stockedResource - ingredient.quantityToMake;
        }
    }

    public static void validateCoffeOptionMaterial(Recipe recipe) {
        boolean hasNecessaryQuantity = true;
        for (Ingredient ingredient : recipe.recipeIngredients) {
            StockedMachineMaterial stockedMaterial
                    = findStockedMachineMaterial(ingredient.ingredientName);
            if (ingredient.quantityToMake > stockedMaterial.stockedResource)
                hasNecessaryQuantity = false;
        }

        if (hasNecessaryQuantity) {
            calculateSurplusAmountOfMaterial(recipe.recipeIngredients);
            money = money + recipe.value;
            System.out.println("I have enough resources, making you a coffee!");
        } else {
            System.out.println("Sorry, not enough resources!");
        }
    }
}
