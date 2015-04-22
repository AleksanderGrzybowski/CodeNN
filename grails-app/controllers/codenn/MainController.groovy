package codenn

import grails.converters.JSON
import org.kelog.core.Language
import org.kelog.end.Client

class MainController {

    def index() {
    }

    def ask() {
        String snippet = params.snippet
        Client client = new Client();
        EnumMap<Language, Double> map = client.match(snippet)
        println map
        render map as JSON
    }
}
