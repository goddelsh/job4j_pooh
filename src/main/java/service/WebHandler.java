package service;

import broker.PoohBroker;
import broker.structs.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebHandler implements HttpHandler {

    final private PoohBroker<User, String, String> poohBroker;

    public WebHandler(PoohBroker<User, String, String> poohBroker) {
        this.poohBroker = poohBroker;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = this.computeForResponse(exchange);
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String computeForResponse(HttpExchange exchange) {
        String result = "";
        String mode = exchange.getRequestURI().toString().toLowerCase();
        String method = exchange.getRequestMethod().toUpperCase();
        if (mode.startsWith("/topic")) {
            if ("POST".equalsIgnoreCase(method)) {
                result = addTopic(exchange);
            } else {
                result = getTopic(exchange);
            }
        } else if (mode.startsWith("/queue")) {
            if ("POST".equalsIgnoreCase(method)) {
                result = addMessage(exchange);
            } else {
                result = getMessage(exchange);
            }
        } else if (mode.startsWith("/subscribe")) {
            result = subscribe(exchange);
        } else if (mode.startsWith("/unsubscribe")) {
            result = unsubscribe(exchange);
        }
        return result;
    }

    private String unsubscribe(HttpExchange exchange) {
        String result = "ERROR";
        String topic = getTopicFromURI(exchange.getRequestURI());
        if (topic != null) {
            this.poohBroker.unsubscribeToTopic(this.getUser(exchange), topic);
            result = "OK";
        }
        return result;
    }

    private String getTopicFromURI(URI requestURI) {
        String[] splitedURI = requestURI.toString().toLowerCase().split("/");
        String result = null;
        if (splitedURI.length > 2) {
            result = splitedURI[2];
        }
        return result;
    }

    private String subscribe(HttpExchange exchange) {
        String result = "ERROR";
        String topic = getTopicFromURI(exchange.getRequestURI());
        if (topic != null) {
            this.poohBroker.subscribeToTopic(this.getUser(exchange), topic);
            result = "OK";
        }
        return result;
    }

    private String getMessage(HttpExchange exchange) {
        Map<String, String> resultMap = null;
        String topic = getTopicFromURI(exchange.getRequestURI());
        if (topic != null) {
            String message = this.poohBroker.getMessage(topic);
            if (message != null) {
                resultMap = new HashMap<>();
                resultMap.put("queue", topic);
                resultMap.put("text", message);
            }
        }
        return resultMap != null ? new Gson().toJson(resultMap) : this.getError();
    }

    private String addMessage(HttpExchange exchange) {
        String result = "EROR";
        String body = this.readBody(exchange.getRequestBody());
        if (body != null) {
            this.poohBroker.addMessage(this.getTopicFromURI(exchange.getRequestURI()), body);
            result = "OK";
        }
        return result;
    }

    private String getTopic(HttpExchange exchange) {
        Map<String, String> resultMap = null;
        String topic = getTopicFromURI(exchange.getRequestURI());
        if (topic != null) {
            String message = this.poohBroker.getTopic(this.getUser(exchange), topic);
            if (message != null) {
                resultMap = new HashMap<>();
                resultMap.put("topic", topic);
                resultMap.put("text", message);
            }
        }
        return resultMap != null ? new Gson().toJson(resultMap) : this.getError();
    }

    private String getError() {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("status", "ERROR");
        return new Gson().toJson(errorMap);
    }

    private User getUser(HttpExchange exchange) {
        User user = new User(){
            @Override
            public String toString() {
                return exchange.getRequestHeaders().get("uuid").stream().findFirst().orElse(UUID.randomUUID().toString());
            }
        };
        return user;
    }

    private String addTopic(HttpExchange exchange) {
        String result = "EROR";
        String body = this.readBody(exchange.getRequestBody());
        if (body != null) {
            this.poohBroker.addTopic(this.getTopicFromURI(exchange.getRequestURI()), body);
            result = "OK";
        }
        return result;
    }


    private String readBody(InputStream requestBody) {
        String result = new String();
        try (BufferedReader streamReader = new BufferedReader(new InputStreamReader(requestBody, "utf-8"))) {
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            result = responseStrBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private Map<String, String> readAndParseBody(InputStream requestBody) {
        Map<String, String> result = null;
        try (BufferedReader streamReader = new BufferedReader(new InputStreamReader(requestBody, "utf-8"))) {
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            result = new Gson().fromJson(responseStrBuilder.toString(), Map.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
