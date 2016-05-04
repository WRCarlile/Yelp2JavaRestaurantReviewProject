import java.util.List;
import org.sql2o.*;

public class Review {
  private int id;
  private String entry;
  private int restaurant_id;

  public Review(String entry, int restaurant_id) {
    this.entry = entry;
    this.restaurant_id = restaurant_id;
  }

  public String getEntry() {
    return entry;
  }

  public int getId() {
    return id;
  }

  public int getRestaurantId() {
    return restaurant_id;
  }

  public static List<Review> all() {
    String sql = "SELECT id, entry, restaurant_id FROM reviews";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Review.class);
    }
  }

  @Override
  public boolean equals(Object otherReview){
    if (!(otherReview instanceof Review)) {
      return false;
    } else {
      Review newReview = (Review) otherReview;
      return this.getEntry().equals(newReview.getEntry()) &&
             this.getId() == newReview.getId() &&
             this.getRestaurantId() == newReview.getRestaurantId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO reviews(entry, restaurant_id) VALUES (:entry, :restaurant_id)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("entry", this.entry)
        .addParameter("restaurant_id", this.restaurant_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Review find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews WHERE id=:id";
      Review review = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Review.class);
      return review;
    }
  }

  public void update(String entry) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE reviews SET entry = :entry WHERE id = :id";
      con.createQuery(sql)
        .addParameter("entry", entry)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
}
