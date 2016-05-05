import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import java.util.ArrayList;
import static spark.Spark.*;

public class App {

  public static void main (String[] args){
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";
// this will take us to homepage/index. User will be able to create new cuisine or add new restaurant to existing cuisine from database. tied to 'rootTest' in AppTest
    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will take us to 'new cuisine form'.
    get("/cuisines/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/cuisine-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will take us to a page that displays all cuisines in database. tied to 'cuisineIsDisplayedTest' in AppTEst.
    get("/cuisines", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/cuisines.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will submit 'cuisine form' and render a success page while inserting user inputted cuisine name into database. tied to cuisineIsCreatedTest in AppTest.
    post("/cuisines/success", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");
      Cuisine newCuisine = new Cuisine(name);
      newCuisine.save(); //*** ADDED FOR DB VERSION ***

      model.put("template", "templates/cuisine-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will show us all cuisines in database. tied to 'cuisineShowPageDiplayName' in AppTest.
    get("/cuisines/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will take us to 'add restaurant form' and take information inputted by user. tied to 'cuisineRestaurantsFormIsDisplayed' in AppTest.
    get("cuisines/:id/restaurants/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine-restaurants-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will save inormation user inputted for a new restaurant from 'add restaurant form' into database. Tied to 'allRestaurantsDisplayTitleOnCuisinePage' test in AppTest.
    post("/restaurants/success", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.queryParams("cuisine_id")));
      String title = request.queryParams("title");
      String address = request.queryParams("address");
      // ** THIS SECTION UPDATED FOR DB VERSION ***
      Restaurant newRestaurant = new Restaurant(title, address, cuisine.getId());
      newRestaurant.save();

      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine-restaurants-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will take us to a page that will display the individual restaurant details within the cuisine taken from id of that form submit. this is where the add review and display should occur as well. tied to 'restaurantShowPage' in AppTest.
    get("/cuisines/:cuisine_id/restaurants/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":cuisine_id")));
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      // Review review = Review.find(Integer.parseInt(request.params(":id")));
      // Review
      model.put("cuisine", cuisine);
      model.put("restaurant", restaurant);
      model.put("reviews", Review.all());
      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// this will update the restaurant title and instad of taking us to a new page, will simply update the name in database and redirect us to display the details page with updated restaurant name. Tied to 'restaurantUpdate' in AppTest
    post("/cuisines/:cuisine_id/restaurants/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.queryParams("restaurant_id")));
      String title = request.queryParams("title");
      Cuisine cuisine = Cuisine.find(restaurant.getCuisineId());
      String entry = request.queryParams("entry");
      Review newReview = new Review(entry, restaurant.getId());
      newReview.save();

      restaurant.update(title);
      String url = String.format("/cuisines/%d/restaurants/%d", cuisine.getId(), restaurant.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("restaurants/:restaurant_id/reviews", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params("restaurant_id")));
      Cuisine cuisine = Cuisine.find(restaurant.getCuisineId());
      String entry = request.queryParams("entry");
      Review newReview = new Review(entry, restaurant.getId());
      newReview.save();
      String url = String.format("/cuisines/%d/restaurants/%d", cuisine.getId(), restaurant.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
// this will delete the restaurant from the cuisine and render the cuisine list page with updated list (minus the one we jsut deleted). tied to 'restaurantDelete' in AppTest.
    post("/cuisines/:cuisine_id/restaurants/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params("id")));
      Cuisine cuisine = Cuisine.find(restaurant.getCuisineId());
      restaurant.delete();
      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/:cuisine_id/restaurants/:id/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      model.put("restaurant", restaurant);
      model.put("template", "templates/cuisine-restaurants-reviews-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id/restaurants/:id/success", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.queryParams("restaurant_id")));
      String entry = request.queryParams("entry");

      // ** THIS SECTION UPDATED FOR DB VERSION ***
      Review newReview = new Review(entry, restaurant.getId());
      newReview.save();

      model.put("restaurant", restaurant);
      model.put("template", "templates/cuisine-restaurants-reviews-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
// This part is meant to take information from the review textarea and throw it in at the bottom of that page. but its not working right. this should not post to "/restaurants".
  //   post("/restaurants", (request, response) -> {
  //     HashMap<String, Object> model = new HashMap<String, Object>();
  //     Restaurant restaurant = Restaurant.find(Integer.parseInt(request.queryParams("restaurant_id")));
  //     // String entry = request.queryParams("entry");
  //
  //     // ** THIS SECTION UPDATED FOR DB VERSION ***
  //     // Review newReview = new Review(entry, restaurant.getId());
  //     // newReview.save();
  //     // model.put("entry", entry);
  //     model.put("restaurant", restaurant);
  //     model.put("template", "templates/restaurant.vtl");
  //     return new ModelAndView(model, layout);
  //   }, new VelocityTemplateEngine());
  //
  // }

  }
}
