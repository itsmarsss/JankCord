package jankcord_admin.apihandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class GetMessages implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, List<String>> requestHeaders = exchange.getRequestHeaders();

        // Prepare the response
        StringBuilder response = new StringBuilder();
        response.append("<html><body>");

        response.append("<h1>Request Headers:</h1>");
        for (Map.Entry<String, List<String>> entry : requestHeaders.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();
            response.append("<p>").append(headerName).append(": ").append(headerValues).append("</p>");
        }

        response.append("</body></html>");

        // Set the response headers
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length());

        // Send the response
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.toString().getBytes());
        outputStream.close();
    }
}
