import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class CuisineTest {

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

  @Test
  public void Cuisine_instantiatesCorrectly_true() {
    Cuisine myCuisine = new Cuisine("Fast food");
    assertEquals(true, myCuisine instanceof Cuisine);
  }

  @Test
  public void getName_cuisineInstantiatesWithName_String() {
    Cuisine myCuisine = new Cuisine("Fast food");
    assertEquals("Fast food", myCuisine.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Cuisine.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Cuisine firstCuisine = new Cuisine("Fast food");
    Cuisine secondCuisine = new Cuisine("Fast food");
    assertTrue(firstCuisine.equals(secondCuisine));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Cuisine myCuisine = new Cuisine("Fast food");
    myCuisine.save();
    assertTrue(Cuisine.all().get(0).equals(myCuisine));
  }

  @Test
  public void save_assignsIdToObject() {
    Cuisine myCuisine = new Cuisine("Fast food");
    myCuisine.save();
    Cuisine savedCuisine = Cuisine.all().get(0);
    assertEquals(myCuisine.getId(), savedCuisine.getId());
  }

  @Test
  public void find_findCuisineInDatabase_true() {
    Cuisine myCuisine = new Cuisine("Fast food");
    myCuisine.save();
    Cuisine savedCuisine = Cuisine.find(myCuisine.getId());
    assertTrue(myCuisine.equals(savedCuisine));
  }

  @Test
  public void getRestaurants_retrievesAllRestaurantsFromDatabase_restaurantsList() {
    Cuisine myCuisine = new Cuisine("Fast food");
    myCuisine.save();
    Restaurant firstRestaurant = new Restaurant("McDonalds", "123 First Ave", myCuisine.getId());
    firstRestaurant.save();
    Restaurant secondRestaurant = new Restaurant("Burger King", "456 Second Ave", myCuisine.getId());
    secondRestaurant.save();
    Restaurant[] restaurant = new Restaurant[] { firstRestaurant, secondRestaurant };
    assertTrue(myCuisine.getRestaurants().containsAll(Arrays.asList(restaurant)));
  }
}
