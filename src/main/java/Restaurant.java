import java.util.List;
import org.sql2o.*;

public class Restaurant {
  private int id;
  private String title;
  private String address;
  private int cuisine_id;

  public Restaurant(String title, String address, int cuisine_id) {
    this.title = title;
    this.address = address;
    this.cuisine_id = cuisine_id;

  }

  public String getTitle() {
    return title;
  }

  public String getAddress() {
    return address;
  }

  public int getId() {
    return id;
  }

  public int getCuisineId() {
    return cuisine_id;
  }

  public static List<Restaurant> all() {
    String sql = "SELECT id, title, address, cuisine_id FROM restaurants";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Restaurant.class);
    }
  }

  @Override
  public boolean equals(Object otherRestaurant){
    if (!(otherRestaurant instanceof Restaurant)) {
      return false;
    } else {
      Restaurant newRestaurant = (Restaurant) otherRestaurant;
      return this.getTitle().equals(newRestaurant.getTitle()) &&
             this.getId() == newRestaurant.getId() &&
             this.getCuisineId() == newRestaurant.getCuisineId() &&
             this.getAddress().equals(newRestaurant.getAddress());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO restaurants(title, address, cuisine_id) VALUES (:title, :address, :cuisine_id)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("address", this.address)
        .addParameter("cuisine_id", this.cuisine_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Restaurant find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants WHERE id=:id";
      Restaurant restaurant = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Restaurant.class);
      return restaurant;
    }
  }

  public void update(String title) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE restaurants SET title = :title WHERE id = :id";
      con.createQuery(sql)
        .addParameter("title", title)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM restaurants WHERE id = :id;";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }


}
