import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class RestaurantTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/yelp2_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
      String deleteCuisinesQuery = "DELETE FROM cuisines *;";
      con.createQuery(deleteReviewsQuery).executeUpdate();
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
      con.createQuery(deleteCuisinesQuery).executeUpdate();
    }
  }

  @Test
  public void Restaurant_instantiatesCorrectly_true() {
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 First Ave", 1);
    assertEquals(true, myRestaurant instanceof Restaurant);
  }

  @Test
  public void getTitle_restaurantInstantiatesWithTitle_String() {
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 First Ave", 1);
    assertEquals("McDonalds", myRestaurant.getTitle());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Restaurant.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfTitlesAretheSame() {
    Restaurant firstRestaurant = new Restaurant("McDonalds", "123 First Ave", 1);
    Restaurant secondRestaurant = new Restaurant("McDonalds", "123 First Ave", 1);
    assertTrue(firstRestaurant.equals(secondRestaurant));
  }

  @Test
  public void save_assignsIdToObject() {
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 First Ave", 1);
    myRestaurant.save();
    Restaurant savedRestaurant = Restaurant.all().get(0);
    assertEquals(myRestaurant.getId(), savedRestaurant.getId());
  }

  @Test
  public void find_findsRestaurantInDatabase_true() {
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 FirstAve", 1);
    myRestaurant.save();
    Restaurant savedRestaurant = Restaurant.find(myRestaurant.getId());
    assertTrue(myRestaurant.equals(savedRestaurant));
  }

  @Test
  public void save_savesCuisineIdIntoDB_true() {
    Cuisine myCuisine = new Cuisine("Fast food");
    myCuisine.save();
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 FirstAve", myCuisine.getId());
    myRestaurant.save();
    Restaurant savedRestaurant = Restaurant.find(myRestaurant.getId());
    assertEquals(savedRestaurant.getCuisineId(), myCuisine.getId());
  }
  @Test
  public void update_updatesRestaurantTitle_true() {
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 FirstAve", 1);
    myRestaurant.save();
    myRestaurant.update("Take a nap");
    assertEquals("Take a nap",Restaurant.find(myRestaurant.getId()).getTitle());
  }

  @Test
  public void delete_deletesRestaurant_true() {
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 FirstAve", 1);
    myRestaurant.save();
    int myRestaurantId = myRestaurant.getId();
    myRestaurant.delete();
    assertEquals(null, Restaurant.find(myRestaurantId));
  }

  @Test
  public void getReviews_retrievesAllReviewsFromDatabase_reviewsList() {
    Restaurant myRestaurant = new Restaurant("McDonalds", "123 FirstAve", 1);
    myRestaurant.save();
    Review firstReview = new Review("McDonalds review", myRestaurant.getId());
    firstReview.save();
    Review secondReview = new Review("Burger King review", myRestaurant.getId());
    secondReview.save();
    Review[] review = new Review[] { firstReview, secondReview };
    assertTrue(myRestaurant.getReviews().containsAll(Arrays.asList(review)));
  }

}
