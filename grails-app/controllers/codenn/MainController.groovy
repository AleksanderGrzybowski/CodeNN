package codenn
import grails.converters.JSON
import org.kelog.end.Client

class MainController {

    def ask(String snippet) {
        Client client = new Client();
	    def map;
	    try {
		    if (snippet == "") {
			    throw new RuntimeException("Snippet empty")
		    }

		    map = client.match(snippet) // handle errors!
		    map = new HashMap(map)
		    map.put('success', true)
	    } catch (Exception e) {
		    map = [success: 'false', message: e.toString()]
	    }
        render map as JSON
    }
}
