package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

class RecipeEntityMapperTest {

    private final RecipeEntityMapper mapper = Mappers.getMapper(RecipeEntityMapper.class);

    @Test
    void entityToDomain() {
        IngredientEntity tomato = new IngredientEntity(UUID.randomUUID(), "Tomato", "The big ones", Unit.PIECE, 5);
        RecipeEntity entity = new RecipeEntity(UUID.randomUUID(), "Lasagne", "Lecker", "lasagne.jpg", List.of(tomato));

        Recipe recipe = mapper.entityToDomain(entity);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(recipe.getId()).isEqualTo(entity.getId());
        softly.assertThat(recipe.getName()).isEqualTo("Lasagne");
        softly.assertThat(recipe.getDescription()).isEqualTo("Lecker");
        softly.assertThat(recipe.getImageUrl()).isEqualTo("lasagne.jpg");
        softly.assertThat(recipe.getIngredients()).hasSize(1);
        softly.assertThat(recipe.getIngredients().get(0).getName()).isEqualTo("Tomato");
        softly.assertThat(recipe.getIngredients().get(0).getUnit()).isEqualTo(Unit.PIECE);
        softly.assertThat(recipe.getIngredients().get(0).getAmount()).isEqualTo(5);
        softly.assertAll();
    }

    @Test
    void domainToEntity() {
        Ingredient flour = new Ingredient(UUID.randomUUID(), "Mehl", "Weissmehl", Unit.GRAMM, 500);
        Recipe recipe = new Recipe(UUID.randomUUID(), "Pizza", "Mit Kaese", "pizza.jpg", List.of(flour));

        RecipeEntity entity = mapper.domainToEntity(recipe);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(entity.getId()).isEqualTo(recipe.getId());
        softly.assertThat(entity.getName()).isEqualTo("Pizza");
        softly.assertThat(entity.getDescription()).isEqualTo("Mit Kaese");
        softly.assertThat(entity.getImageUrl()).isEqualTo("pizza.jpg");
        softly.assertThat(entity.getIngredients()).hasSize(1);
        softly.assertThat(entity.getIngredients().get(0).getName()).isEqualTo("Mehl");
        softly.assertThat(entity.getIngredients().get(0).getComment()).isEqualTo("Weissmehl");
        softly.assertThat(entity.getIngredients().get(0).getUnit()).isEqualTo(Unit.GRAMM);
        softly.assertAll();
    }

    @Test
    void recipeWithoutIngredients() {
        Recipe recipe = new Recipe(UUID.randomUUID(), "Wasser", "", "", null);

        RecipeEntity entity = mapper.domainToEntity(recipe);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entity.getName()).isEqualTo("Wasser");
            softly.assertThat(entity.getIngredients()).isNull();
        });
    }

    @Test
    void nullStaysNull() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(mapper.entityToDomain(null)).isNull();
            softly.assertThat(mapper.domainToEntity(null)).isNull();
        });
    }
}
