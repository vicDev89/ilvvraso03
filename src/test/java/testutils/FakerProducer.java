package testutils;

import com.github.javafaker.Faker;
import de.berlin.htw.usws.model.*;

import java.util.Locale;
import java.util.Random;

public class FakerProducer {

    public static Product createFakeProduct() {

        Faker faker = new Faker(new Locale("de"));
         Product product = new Product();
         product.setSupermarket(Supermarket.EDEKA);
         product.setName(faker.food().ingredient());
         product.setPriceMin(Double.parseDouble(faker.commerce().price(0.0, 5.0).replace(",", ".")));
        product.setPriceMax(Double.parseDouble(faker.commerce().price(5.1, 10.0).replace(",", ".")));
        product.setIngredient(createFakeIngredient());
        return product;
    }

    public static Ingredient createFakeIngredient() {
        Faker faker = new Faker(new Locale("de"));
        Ingredient ingredient = new Ingredient();
        ingredient.setName(faker.food().ingredient());
        return ingredient;
    }

    public static Recipe createFakeRecipe() {
        Faker faker = new Faker(new Locale("de"));
        Recipe recipe = new Recipe();
        recipe.setId(faker.number().numberBetween(0L, 380000));
        recipe.setCookingTimeInMin(faker.number().numberBetween(30, 240));
        recipe.setPreparationTimeInMin(faker.number().numberBetween(30, 240));
        recipe.setRestingTimeInMin(faker.number().numberBetween(30, 240));
        recipe.setPreparation(faker.dragonBall().character());
        recipe.setDifficultyLevel(DifficultyLevel.values()[new Random().nextInt(DifficultyLevel.values().length)]);
        recipe.setPictureUrl(faker.internet().url());
        recipe.setRate(faker.number().randomDouble(2, 0, 5));
        recipe.setTitle(faker.book().title());
        return recipe;
    }
}
