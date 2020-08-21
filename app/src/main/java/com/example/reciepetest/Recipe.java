package com.example.reciepetest;
import java.util.List;

public class Recipe{
    public String documentId;
    public String name;
    public String image;
    public String mainIngredient;
    public String instructions;
    public int calories;
    int servings;
    public List<String> ingredients;
    public List<String> tags;

    public Recipe() {
    }

    public Recipe(String documentId, String name, String image, String mainIngredient, String instructions, int calories, int servings, List<String> ingredients, List<String> tags) {
        this.documentId = documentId;
        this.name = name;
        this.image = image;
        this.mainIngredient = mainIngredient;
        this.instructions = instructions;
        this.calories = calories;
        this.servings = servings;
        this.ingredients = ingredients;
        this.tags = tags;
    }
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
