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

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/cuisine-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/cuisines.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");
      Cuisine newCuisine = new Cuisine(name);
      newCuisine.save(); //*** ADDED FOR DB VERSION ***

      model.put("template", "templates/cuisine-success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/cuisines/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("cuisines/:id/restaurants/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":id")));
      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine-restaurants-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/restaurants", (request, response) -> {
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

    get("/cuisines/:cuisine_id/restaurants/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Cuisine cuisine = Cuisine.find(Integer.parseInt(request.params(":cuisine_id")));
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));

      model.put("cuisine", cuisine);
      model.put("restaurant", restaurant);

      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id/restaurants/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params("id")));
      String title = request.queryParams("title");
      Cuisine cuisine = Cuisine.find(restaurant.getCuisineId());
      restaurant.update(title);
      String url = String.format("/cuisines/%d/restaurants/%d", cuisine.getId(), restaurant.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/cuisines/:cuisine_id/restaurants/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params("id")));
      Cuisine cuisine = Cuisine.find(restaurant.getCuisineId());
      restaurant.delete();
      model.put("cuisine", cuisine);
      model.put("template", "templates/cuisine.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());



  }


}
