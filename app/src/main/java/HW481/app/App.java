/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package HW481.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

//import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.port;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App{
    public String getGreeting() {
        return "Hello world.";
    }

    public static boolean search(ArrayList<Integer> inputList, int input2AsInt, int input3AsInt) {
      if(inputList == null)
        return false;
      if(inputList.size()==0)
        return false;

      java.util.ArrayList<Integer> list = new java.util.ArrayList<>();
      list.add(input2AsInt + input3AsInt);
      list.add(input2AsInt - input3AsInt);
      list.add(input3AsInt - input2AsInt);
      list.add(input2AsInt * input3AsInt);
      if(input3AsInt != 0)
        list.add(input2AsInt / input3AsInt);
      if(input2AsInt != 0)
        list.add(input3AsInt / input2AsInt);
      list.add(input2AsInt);
      list.add(input3AsInt);

      for(int i=0;i<list.size();i++)
        if(inputList.contains(list.get(i)))
          return true;
      
      return false;
    }

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        Logger logger = LogManager.getLogger(App.class);
        //int port = Integer.parseInt(System.getenv("PORT"));
        //port(port);
        //logger.error("Current port number:" + port);

        get("/",
            (rq, rs) -> {
                Map<String, String> map = new HashMap<String, String>();
                return new ModelAndView(map, "main.mustache");
            },
            new MustacheTemplateEngine()); 

        post("/compute", (req, res) -> {
          //System.out.println(req.queryParams("input1"));
          //System.out.println(req.queryParams("input2"));

          String input1 = req.queryParams("input1");
          java.util.Scanner sc1 = new java.util.Scanner(input1);
          sc1.useDelimiter("[;\r\n]+");
          java.util.ArrayList<Integer> inputList = new java.util.ArrayList<>();
          
          while (sc1.hasNext())
          {
            int value=0;
          try{
            value = Integer.parseInt(sc1.next().replaceAll("\\s",""));
          }catch(Exception e){
            System.out.println(e.getMessage());
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "Could not be computed due to invalid input.");
            return new ModelAndView(map, "compute.mustache");  
          }
            inputList.add(value);
          }
          sc1.close();
          System.out.println(inputList);

          int input2AsInt=0;
          String input2 = req.queryParams("input2").replaceAll("\\s","");
          try{
            input2AsInt = Integer.parseInt(input2);
          }catch(Exception e){
            System.out.println(e.getMessage());
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "Could not be computed due to invalid input.");
            return new ModelAndView(map, "compute.mustache"); 
          }
          String input3 = req.queryParams("input3").replaceAll("\\s","");

          int input3AsInt = 0;
          try{
            input3AsInt = Integer.parseInt(input3);
          }catch(Exception e){
            System.out.println(e.getMessage());
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "Could not be computed due to invalid input.");
            return new ModelAndView(map, "compute.mustache");
          }

          boolean result = App.search(inputList, input2AsInt, input3AsInt);

          Map<String, String> map = new HashMap<String, String>();
          if(result)
            map.put("result", "The list contains one of the combinations of given two numbers");
          else
            map.put("result", "The list does NOT contain any combination of given two numbers");
          return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());


        get("/compute",
            (rq, rs) -> {
              Map<String, String> map = new HashMap<String, String>();
              map.put("result", "not computed yet!");
              return new ModelAndView(map, "compute.mustache");
            },
            new MustacheTemplateEngine());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    
}

