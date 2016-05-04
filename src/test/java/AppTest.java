import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }
  @Before
public void setUp() {
  DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/yelp2_test", null, null);
}

@After
public void tearDown() {
  try(Connection con = DB.sql2o.open()) {
    String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
    String deleteCuisinesQuery = "DELETE FROM cuisines *;";
    con.createQuery(deleteRestaurantsQuery).executeUpdate();
    con.createQuery(deleteCuisinesQuery).executeUpdate();
  }
}

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("To Do List!");
  }

  @Test
  public void cuisineIsCreatedTest() {
    goTo("http://localhost:4567/");

    fill("#name").with("Fast food");
    submit("#catBtn");
    assertThat(pageSource()).contains("Your cuisine has been saved.");
  }

  @Test
  public void cuisineIsDisplayedTest() {
    Cuisine myCuisine = new Cuisine("Fast food");
    myCuisine.save();
    String cuisinePath = String.format("http://localhost:4567/cuisines/%d", myCuisine.getId());
    goTo(cuisinePath);
    assertThat(pageSource()).contains("Fast food");
  }


   @Test
   public void cuisineShowPageDiplayName() {
     goTo("http://localhost:4567/cuisines/new");
     fill("#name").with("Household cheese");
     submit(".btn");
     click("a", withText("View cuisines"));
     click("a", withText("Household cheese"));
     assertThat(pageSource()).contains("Household cheese");
   }

   @Test
   public void cuisineRestaurantsFormIsDisplayed() {
     goTo("http://localhost:4567/cuisines/new");
     fill("#name").with("Shopping");
     submit(".btn");
     click("a", withText("View cuisines"));
     click("a", withText("Shopping"));
     click("a", withText("Add a new restaurant"));
     assertThat(pageSource()).contains("Add a restaurant to Shopping");
   }

   @Test
   public void allRestaurantsDisplayTitleOnCuisinePage() {
     Cuisine myCuisine = new Cuisine ("Fast food");
     myCuisine.save();
     Restaurant firstRestaurant = new Restaurant ("McDonalds", "555 Elm St", myCuisine.getId());
     firstRestaurant.save();
     Restaurant secondRestaurant = new Restaurant("Taco Bell", "555 Broadway St", myCuisine.getId());
     secondRestaurant.save();
     String cuisinePath = String.format("http://localhost:4567/cuisines/%d", myCuisine.getId());
     goTo(cuisinePath);
     assertThat(pageSource()).contains("McDonalds");
     assertThat(pageSource()).contains("Taco Bell");
   }

  @Test
  public void restaurantShowPage() {
    Cuisine myCuisine = new Cuisine("Fast Food");
    myCuisine.save();
    Restaurant myRestaurant = new Restaurant("McDonalds", "555 Elm St", myCuisine.getId());
    myRestaurant.save();
    String cuisinePath = String.format("http://localhost:4567/cuisines/%d", myCuisine.getId());
    goTo(cuisinePath);
    click("a", withText("McDonalds"));
    assertThat(pageSource()).contains("McDonalds");
    assertThat(pageSource()).contains("Return to Fast Food");
  }
  @Test
  public void restaurantUpdate() {
    Cuisine myCuisine = new Cuisine("Fast Food");
    myCuisine.save();
    Restaurant myRestaurant = new Restaurant("McDonalds", "555 Elm St", myCuisine.getId());
    myRestaurant.save();
    String restaurantPath = String.format("http://localhost:4567/cuisines/%d/restaurants/%d", myCuisine.getId(), myRestaurant.getId());
    goTo(restaurantPath);
    fill("#title").with("Burger King");
    submit("#update-restaurant");
    assertThat(pageSource()).contains("Burger King");
  }

  @Test
  public void restaurantDelete() {
    Cuisine myCuisine = new Cuisine("Fast Food");
    myCuisine.save();
    Restaurant myRestaurant = new Restaurant("McDonalds", "555 Elm St", myCuisine.getId());
    myRestaurant.save();
    String restaurantPath = String.format("http://localhost:4567/cuisines/%d/restaurants/%d", myCuisine.getId(), myRestaurant.getId());
    goTo(restaurantPath);
    submit("#delete-restaurant");
    assertEquals(0, Restaurant.all().size());
  }

}
