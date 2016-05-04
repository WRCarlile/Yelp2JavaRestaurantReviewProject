import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ReviewTest {

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
  public void Review_instantiatesCorrectly_true() {
    Review myReview = new Review("McDonalds review", 1);
    assertEquals(true, myReview instanceof Review);
  }

  // @Test
  // public void getTitle_restaurantInstantiatesWithTitle_String() {
  //   Review myReview = new Review("McDonalds", "123 First Ave", 1);
  //   assertEquals("McDonalds", myReview.getTitle());
  // }
  //
  // @Test
  // public void all_emptyAtFirst() {
  //   assertEquals(Review.all().size(), 0);
  // }
  //
  // @Test
  // public void equals_returnsTrueIfTitlesAretheSame() {
  //   Review firstReview = new Review("McDonalds", "123 First Ave", 1);
  //   Review secondReview = new Review("McDonalds", "123 First Ave", 1);
  //   assertTrue(firstReview.equals(secondReview));
  // }
  //
  // @Test
  // public void save_assignsIdToObject() {
  //   Review myReview = new Review("McDonalds", "123 First Ave", 1);
  //   myReview.save();
  //   Review savedReview = Review.all().get(0);
  //   assertEquals(myReview.getId(), savedReview.getId());
  // }
  //
  // @Test
  // public void find_findsReviewInDatabase_true() {
  //   Review myReview = new Review("McDonalds", "123 FirstAve", 1);
  //   myReview.save();
  //   Review savedReview = Review.find(myReview.getId());
  //   assertTrue(myReview.equals(savedReview));
  // }
  //
  // @Test
  // public void save_savesCuisineIdIntoDB_true() {
  //   Cuisine myCuisine = new Cuisine("Fast food");
  //   myCuisine.save();
  //   Review myReview = new Review("McDonalds", "123 FirstAve", myCuisine.getId());
  //   myReview.save();
  //   Review savedReview = Review.find(myReview.getId());
  //   assertEquals(savedReview.getCuisineId(), myCuisine.getId());
  // }
  // @Test
  // public void update_updatesReviewTitle_true() {
  //   Review myReview = new Review("McDonalds", "123 FirstAve", 1);
  //   myReview.save();
  //   myReview.update("Take a nap");
  //   assertEquals("Take a nap",Review.find(myReview.getId()).getTitle());
  // }
  //
  // @Test
  // public void delete_deletesReview_true() {
  //   Review myReview = new Review("McDonalds", "123 FirstAve", 1);
  //   myReview.save();
  //   int myReviewId = myReview.getId();
  //   myReview.delete();
  //   assertEquals(null, Review.find(myReviewId));
  // }

}
