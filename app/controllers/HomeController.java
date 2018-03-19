package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dboperations.DBConstants;
import dboperations.DbOperations;
import play.mvc.*;

import views.html.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        DbOperations db = new DbOperations();
        List<Map<String, Object>> banks = db.getBanks(DBConstants.getBanks, null);
        return ok(index.render("Banking Guru",banks));

        //return ok(index.render("Your new application is ready."));
        //return redirect("/home");
    }

}
