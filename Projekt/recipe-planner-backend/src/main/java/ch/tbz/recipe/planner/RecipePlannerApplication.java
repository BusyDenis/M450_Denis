package ch.tbz.recipe.planner;

import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class RecipePlannerApplication {

	private final static String WIKIMEDIA = "https://upload.wikimedia.org/wikipedia/commons/thumb/";

	public static void main(String[] args) {
		SpringApplication.run(RecipePlannerApplication.class, args);
	}

	@Autowired
	RecipeEntityMapper mapper;

	@Bean
	CommandLineRunner init(RecipeRepository recipeRepository) {
		return args -> {
			// Demo-Rezepte mit echten Zutaten, damit die Einkaufsliste etwas zum Zusammenrechnen hat
			Stream.of(
					recipe("Lasagne", "Klassisch mit Hackfleisch und Béchamel.", "3/34/Lasagna_4.jpg/960px-Lasagna_4.jpg",
							ingredient("Hackfleisch", "Rind", Unit.GRAM, 500),
							ingredient("Tomaten", "", Unit.PIECE, 4),
							ingredient("Lasagneblätter", "", Unit.GRAM, 250),
							ingredient("Milch", "für die Béchamel", Unit.DECILITRE, 5),
							ingredient("Käse", "Mozzarella", Unit.GRAM, 200)),
					recipe("Spaghetti Bolognese", "Schnell gemacht, perfekt unter der Woche.",
							"3/3a/Liat_Portal_for_Foodie_Disorder_-_Spaghetti_Bolognese_with_vegetables.jpg/960px-Liat_Portal_for_Foodie_Disorder_-_Spaghetti_Bolognese_with_vegetables.jpg",
							ingredient("Spaghetti", "", Unit.GRAM, 500),
							ingredient("Hackfleisch", "", Unit.GRAM, 400),
							ingredient("tomaten ", "reif", Unit.PIECE, 3),
							ingredient("Zwiebel", "", Unit.PIECE, 1)),
					recipe("Pancakes", "Fluffig, zum Frühstück.", "4/40/Foodiesfeed.com_pouring-honey-on-pancakes-with-walnuts.jpg/960px-Foodiesfeed.com_pouring-honey-on-pancakes-with-walnuts.jpg",
							ingredient("Mehl", "", Unit.GRAM, 250),
							ingredient("Milch", "", Unit.DECILITRE, 4),
							ingredient("Eier", "", Unit.PIECE, 2)),
					recipe("Gemüsecurry", "Vegetarisch mit Kokosmilch und Reis.", "6/67/Mixed_vegetable_curry_1.jpg/960px-Mixed_vegetable_curry_1.jpg",
							ingredient("Kokosmilch", "", Unit.DECILITRE, 4),
							ingredient("Reis", "Basmati", Unit.GRAM, 300),
							ingredient("Zwiebel", "", Unit.PIECE, 2),
							ingredient("Tomaten", "aus der Dose", Unit.GRAM, 400))
			).forEach(recipe -> recipeRepository.save(mapper.domainToEntity(recipe)));
			recipeRepository.findAll().forEach(s -> log.info(s.toString()));
		};
	}

	private static Recipe recipe(String name, String description, String image, Ingredient... ingredients) {
		return new Recipe(UUID.randomUUID(), name, description, WIKIMEDIA + image, List.of(ingredients));
	}

	private static Ingredient ingredient(String name, String comment, Unit unit, int amount) {
		return new Ingredient(UUID.randomUUID(), name, comment, unit, amount);
	}
}
