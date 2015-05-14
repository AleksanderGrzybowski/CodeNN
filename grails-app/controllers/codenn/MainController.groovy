package codenn

import grails.converters.JSON
import org.kelog.end.Client

class MainController {

    def ask(String snippet) {
        def map

        try {
            if (snippet == null || snippet == "") {
                map = [success: false, message: "Put some text!"]
            } else {
                Client client = new Client()
                map = client.match(snippet) // handle errors!
                map = new HashMap(map) // back from EnumMap to kind-of-untyped one
                map.put('success', true)
            }
        } catch (Exception e) {
            map = [success: 'false', message: e.toString()]
        }
        render map as JSON
    }
}
