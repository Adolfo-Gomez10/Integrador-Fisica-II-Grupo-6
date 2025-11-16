package com.mycompany.app;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

public class CalculadoraServer {
    private HttpServer server;

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", this::root);
        server.createContext("/resistor", this::resistorHandler);
        server.createContext("/capacitor", this::capacitorHandler);
        server.setExecutor(null);
        server.start();
        System.out.println("HTTP server started at http://localhost:" + port);
    }

    private void root(HttpExchange ex) throws IOException {
        String html = "<html><body>"
                + "<h2>Calculadoras</h2>"
                + "<ul>"
                + "<li><a href=\"/resistor\">Resistencia (form)</a></li>"
                + "<li><a href=\"/capacitor\">Capacitor (form)</a></li>"
                + "</ul>"
                + "</body></html>";
        send(ex, html);
    }

    private void resistorHandler(HttpExchange ex) throws IOException {
        if ("GET".equalsIgnoreCase(ex.getRequestMethod())) {
            String html = "<html><body>"
                + "<h3>Resistencia 4 bandas</h3>"
                + "<form method='POST'>"
                + "Banda1: <input name='b1' value='brown'/><br/>"
                + "Banda2: <input name='b2' value='black'/><br/>"
                + "Multiplicador: <input name='m' value='red'/><br/>"
                + "Tolerancia: <input name='t' value='gold'/><br/>"
                + "<input type='submit' value='Calcular'/>"
                + "</form></body></html>";
            send(ex, html);
        } else {
            Map<String,String> params = parse(ex.getRequestURI(), new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            String res = CalculadoraResistencias.calculate4Band(params.getOrDefault("b1","brown"),
                    params.getOrDefault("b2","black"),
                    params.getOrDefault("m","red"),
                    params.getOrDefault("t","gold"));
            send(ex, "<html><body>Resultado: " + res + "<br/><a href=\"/\">Volver</a></body></html>");
        }
    }

    private void capacitorHandler(HttpExchange ex) throws IOException {
        if ("GET".equalsIgnoreCase(ex.getRequestMethod())) {
            String html = "<html><body>"
                + "<h3>Capacitor</h3>"
                + "<form method='POST'>"
                + "Actual (μF): <input name='cur' value='1.0'/><br/>"
                + "Objetivo (μF): <input name='tgt' value='2.0'/><br/>"
                + "Modo: <select name='mode'><option>PARALLEL</option><option>SERIES</option></select><br/>"
                + "<input type='submit' value='Calcular'/>"
                + "</form></body></html>";
            send(ex, html);
        } else {
            Map<String,String> params = parse(ex.getRequestURI(), new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            double cur = Double.parseDouble(params.getOrDefault("cur","0"));
            double tgt = Double.parseDouble(params.getOrDefault("tgt","0"));
            String mode = params.getOrDefault("mode","PARALLEL");
            double req = "PARALLEL".equalsIgnoreCase(mode) ? CalculadoraCapacitores.requiredParallel(cur, tgt) : CalculadoraCapacitores.requiredSeries(cur, tgt);
            send(ex, "<html><body>Resultado: " + CalculadoraCapacitores.fmt(req) + "<br/><a href=\"/\">Volver</a></body></html>");
        }
    }

    private Map<String,String> parse(URI uri, String body) {
        String query = uri.getQuery();
        String all = (query == null ? "" : query) + (body == null ? "" : (query == null ? "" : "&") + body);
        return java.util.Arrays.stream(all.split("&"))
            .map(s -> s.split("=",2))
            .filter(a -> a.length==2)
            .collect(Collectors.toMap(a -> decode(a[0]), a -> decode(a[1]), (a,b) -> b));
    }

    private String decode(String s) {
        return java.net.URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    private void send(HttpExchange ex, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    public void stop() {
        if (server != null) server.stop(0);
    }
}