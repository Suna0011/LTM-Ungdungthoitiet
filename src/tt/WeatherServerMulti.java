package tt;

//WeatherServerMulti.java
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WeatherServerMulti {
 private static final String API_KEY = "db1caf75a5d6aced0988cb51e71468c1"; // <-- your key
 private static final int PORT = 9999;
 private static final int BUFFER_SIZE = 8192;
 private static final HistoryManager history = new HistoryManager("history.txt");
 private static final ExecutorService pool = Executors.newFixedThreadPool(8);

 public static void main(String[] args) throws Exception {
     DatagramSocket socket = new DatagramSocket(PORT);
     System.out.println("Weather UDP Multi-thread Server running on port " + PORT);

     byte[] buf = new byte[BUFFER_SIZE];
     while (true) {
         try {
             DatagramPacket packet = new DatagramPacket(buf, buf.length);
             socket.receive(packet);

             // copy data to avoid overwrite when reused buffer
             byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
             InetAddress clientAddr = packet.getAddress();
             int clientPort = packet.getPort();

             pool.submit(() -> handlePacket(data, clientAddr, clientPort, socket));
         } catch (Exception e) {
             System.err.println("Main receive error: " + e.getMessage());
         }
     }
 }

 private static void handlePacket(byte[] data, InetAddress clientAddr, int clientPort, DatagramSocket socket) {
     try {
         String req = new String(data, StandardCharsets.UTF_8).trim();
         System.out.println("Request from " + clientAddr + ":" + clientPort + " -> " + req);

         // Expect either "CURRENT:city" or "FORECAST:city"
         String[] parts = req.split(":", 2);
         if (parts.length != 2) {
             sendString(socket, clientAddr, clientPort, "ERROR:Yêu cầu không hợp lệ. Định dạng: CURRENT:city hoặc FORECAST:city");
             return;
         }
         String type = parts[0].toUpperCase();
         String city = parts[1].trim();

         if (type.equals("CURRENT")) {
             String current = getCurrentWeatherShort(city);
             // ghi lịch sử (timestamp, city, current)
             history.append(String.format("[%s] %s -> %s", nowStr(), city, singleLine(current)));
             sendString(socket, clientAddr, clientPort, current);
         } else if (type.equals("FORECAST")) {
             String forecastCsv = get5DayForecastAveragesCsv(city);
             if (forecastCsv.startsWith("ERROR:")) {
                 sendString(socket, clientAddr, clientPort, forecastCsv);
             } else {
                 // ghi lịch sử (timestamp, city, forecast request)
                 history.append(String.format("[%s] %s -> FORECAST", nowStr(), city));
                 sendString(socket, clientAddr, clientPort, forecastCsv);
             }
         } else {
             sendString(socket, clientAddr, clientPort, "ERROR:Loại yêu cầu không hỗ trợ. Dùng CURRENT hoặc FORECAST");
         }
     } catch (Exception e) {
         System.err.println("handlePacket error: " + e.getMessage());
     }
 }

 private static void sendString(DatagramSocket socket, InetAddress addr, int port, String msg) {
     try {
         byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
         DatagramPacket resp = new DatagramPacket(bytes, bytes.length, addr, port);
         socket.send(resp);
     } catch (Exception e) {
         System.err.println("sendString error: " + e.getMessage());
     }
 }

 private static String getCurrentWeatherShort(String city) {
     try {
         String urlStr = "https://api.openweathermap.org/data/2.5/weather?q="
                 + URLEncoder.encode(city, "UTF-8") + "&appid=" + API_KEY
                 + "&units=metric&lang=vi";
         String json = httpGet(urlStr);
         // quick parse for main.temp, weather[0].description, weather[0].icon
         String temp = extractFirst(json, "\"temp\":([0-9+\\-\\.]+)");
         String desc = extractFirst(json, "\"description\":\"([^\"]+)\"");
         String icon = extractFirst(json, "\"icon\":\"([^\"]+)\"");
         if (temp == null || desc == null) {
             return "ERROR:Không lấy được dữ liệu cho " + city;
         }
         // return multi-line string (client will display)
         return String.format("CURRENT|%s|%s|%s", city, temp, desc + "|" + (icon != null ? icon : ""));
     } catch (Exception e) {
         return "ERROR:Lỗi khi gọi API: " + e.getMessage();
     }
 }

 /** 
  * Returns CSV-like string: date=avgTemp;date=avgTemp;...
  * date format: YYYY-MM-DD
  */
 private static String get5DayForecastAveragesCsv(String city) {
     try {
         String urlStr = "https://api.openweathermap.org/data/2.5/forecast?q="
                 + URLEncoder.encode(city, "UTF-8") + "&appid=" + API_KEY
                 + "&units=metric&lang=vi";
         String json = httpGet(urlStr);

         // Extract pairs of dt_txt and temp using regex
         Pattern p = Pattern.compile("\"dt_txt\"\\s*:\\s*\"(\\d{4}-\\d{2}-\\d{2}) [0-9:]{8}\"[\\s\\S]*?\"temp\"\\s*:\\s*([0-9+\\-\\.]+)");
         Matcher m = p.matcher(json);

         Map<String, List<Double>> map = new LinkedHashMap<>();
         while (m.find()) {
             String date = m.group(1);
             double temp = Double.parseDouble(m.group(2));
             map.computeIfAbsent(date, k -> new ArrayList<>()).add(temp);
         }

         if (map.isEmpty()) return "ERROR:Không tìm thấy dữ liệu forecast cho " + city;

         // take first 5 dates
         List<String> keys = new ArrayList<>(map.keySet());
         int take = Math.min(5, keys.size());
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < take; i++) {
             String d = keys.get(i);
             List<Double> temps = map.get(d);
             double avg = temps.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
             if (i > 0) sb.append(";");
             sb.append(d).append("=").append(String.format(Locale.US, "%.2f", avg));
         }
         return sb.toString();
     } catch (Exception e) {
         return "ERROR:Lỗi khi gọi API forecast: " + e.getMessage();
     }
 }

 private static String httpGet(String urlStr) throws IOException {
     URL url = new URL(urlStr);
     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
     conn.setRequestMethod("GET");
     conn.setConnectTimeout(6000);
     conn.setReadTimeout(6000);
     try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
         return in.lines().collect(Collectors.joining("\n"));
     }
 }

 private static String extractFirst(String text, String regex) {
     Pattern p = Pattern.compile(regex);
     Matcher m = p.matcher(text);
     if (m.find()) return m.group(1);
     return null;
 }

 private static String nowStr() {
     return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 }

 private static String singleLine(String s) {
     return s.replace("\n", " / ");
 }
}