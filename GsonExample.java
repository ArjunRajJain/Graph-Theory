import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GsonExample {
    public static String text = "{\n"
	+ "  \"cis\": [\n"
	+ "    {\n"
        + "      \"name\": \"CIS120\",\n"
        + "      \"instructor\": \"Dr. Weirich\",\n"
        + "      \"languages\": [\"OCaml\", \"Java\"]\n"
	+ "    },\n"
        + "    {\n"
        + "      \"name\": \"CIS121\",\n"
        + "      \"instructor\": \"Dr. Tannen\",\n"
        + "      \"languages\": [\"Java\"]\n"
        + "    }\n"
        + "  ]\n"
        + "}\n";

    public static void main(String args[]) {
	JsonParser parser = new JsonParser();
	JsonObject o = parser.parse(text).getAsJsonObject();
	for (JsonElement e : o.get("cis").getAsJsonArray()) {
	    JsonObject course = e.getAsJsonObject();
	    System.out.println("Course name: " + course.get("name").getAsString() + "\n");
	    System.out.println("Instructor: " + 
			       course.get("instructor").getAsString() + "\n");
	    System.out.println("Languages taught:\n");
	    for (JsonElement lang : course.get("languages").getAsJsonArray()) {
		System.out.println("  - " + lang.getAsString() + "\n");
	    }
	}
    }
}
