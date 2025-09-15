package tt;

//WeatherClientGUIFull.java
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class WeatherClientGUIFull extends JFrame {
 private final JTextField txtCity = new JTextField();
 private final JButton btnCurrent = new JButton("Dự báo hiện tại");
 private final JButton btnForecast = new JButton("Xem biểu đồ 5 ngày");
 private final JButton btnHistory = new JButton("Xem lịch sử");
 private final JTextArea txtResult = new JTextArea();
 private final JLabel lblIcon = new JLabel();
 private DatagramSocket socket;
 private InetAddress serverAddr;
 private static final int SERVER_PORT = 9999;
 private static final int BUFFER_SIZE = 8192;

 public WeatherClientGUIFull() {
     setTitle("WeatherApp - UDP Client");
     setSize(600, 420);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setLocationRelativeTo(null);
     initUI();
     initSocket();
 }

 private void initUI() {
     // Top panel: input + buttons
     JPanel top = new JPanel(new BorderLayout(8,8));
     top.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
     top.add(new JLabel("Thành phố:"), BorderLayout.WEST);
     top.add(txtCity, BorderLayout.CENTER);

     JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     buttons.add(btnCurrent);
     buttons.add(btnForecast);
     buttons.add(btnHistory);
     top.add(buttons, BorderLayout.EAST);

     // Center: result + icon
     txtResult.setEditable(false);
     txtResult.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
     JScrollPane sc = new JScrollPane(txtResult);

     JPanel center = new JPanel(new BorderLayout());
     JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
     iconPanel.add(lblIcon);
     center.add(iconPanel, BorderLayout.WEST);
     center.add(sc, BorderLayout.CENTER);

     add(top, BorderLayout.NORTH);
     add(center, BorderLayout.CENTER);

     // Actions
     btnCurrent.addActionListener(e -> doCurrent());
     btnForecast.addActionListener(e -> doForecast());
     btnHistory.addActionListener(e -> showHistoryDialog());
 }

 private void initSocket() {
     try {
         socket = new DatagramSocket();
         serverAddr = InetAddress.getByName("localhost"); // change if server remote
     } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Lỗi khởi tạo socket: " + e.getMessage());
     }
 }

 private void doCurrent() {
     String city = txtCity.getText().trim();
     if (city.isEmpty()) { JOptionPane.showMessageDialog(this, "Nhập tên thành phố!"); return; }
     try {
         String request = "CURRENT:" + city;
         sendRequest(request);
         String resp = receiveResponse();
         if (resp.startsWith("ERROR:")) {
             txtResult.setText(resp);
             lblIcon.setIcon(null);
             return;
         }
         // expected format: CURRENT|city|temp|description|icon
         String[] parts = resp.split("\\|");
         if (parts.length >= 4 && parts[0].equals("CURRENT")) {
             String cityR = parts[1];
             String temp = parts[2];
             // parts[3] may contain "desc" or "desc|icon"
             String desc = "";
             String iconCode = null;
             if (parts.length >= 5) {
                 desc = parts[3];
                 iconCode = parts[4];
             } else {
                 String rest = parts[3];
                 // if parts[3] contains '|' splitted earlier, but we already split
                 desc = rest;
             }
             txtResult.setText(String.format("Thành phố: %s\nNhiệt độ: %s °C\nTrạng thái: %s", cityR, temp, desc));
             if (iconCode != null && !iconCode.isBlank()) {
                 setIconFromCode(iconCode);
             } else {
                 lblIcon.setIcon(null);
             }
         } else {
             txtResult.setText("Dữ liệu phản hồi không đúng: " + resp);
         }
     } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
     }
 }

 private void doForecast() {
     String city = txtCity.getText().trim();
     if (city.isEmpty()) { JOptionPane.showMessageDialog(this, "Nhập tên thành phố!"); return; }
     try {
         String request = "FORECAST:" + city;
         sendRequest(request);
         String resp = receiveResponse();
         if (resp.startsWith("ERROR:")) {
             txtResult.setText(resp);
             return;
         }
         // Format: YYYY-MM-DD=avg;YYYY-MM-DD=avg;...
         Map<String, Double> map = new LinkedHashMap<>();
         String[] pairs = resp.split(";");
         for (String p : pairs) {
             String[] kv = p.split("=");
             if (kv.length == 2) {
                 map.put(kv[0], Double.parseDouble(kv[1]));
             }
         }
         // show textual summary
         StringBuilder sb = new StringBuilder();
         sb.append("Biểu đồ dự báo 5 ngày (nhiệt độ trung bình):\n");
         map.forEach((d, t) -> sb.append(String.format("%s : %.2f °C\n", d, t)));
         txtResult.setText(sb.toString());

         // show chart in new frame
         ChartFrame cf = new ChartFrame("Dự báo 5 ngày - " + city, map);
         cf.setVisible(true);
     } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
     }
 }

 private void showHistoryDialog() {
     try {
         // Request server to not necessary; history is local file on server.
         // Simple approach: client reads history file directly if shared folder.
         // But we don't share file; so let's add a simple protocol to ask server to send history.
         // For simplicity and to avoid changing server, assume history.txt is accessible in same folder:
         File f = new File("history.txt");
         if (f.exists()) {
             List<String> lines = java.nio.file.Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
             JTextArea ta = new JTextArea();
             ta.setEditable(false);
             for (String l : lines) ta.append(l + "\n");
             JScrollPane sp = new JScrollPane(ta);
             sp.setPreferredSize(new Dimension(600, 300));
             JOptionPane.showMessageDialog(this, sp, "Lịch sử truy vấn", JOptionPane.INFORMATION_MESSAGE);
         } else {
             JOptionPane.showMessageDialog(this, "Không tìm thấy file history.txt trên client (nếu server chạy trên máy khác, hãy lấy file history từ server).");
         }
     } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Đọc lịch sử lỗi: " + e.getMessage());
     }
 }

 private void sendRequest(String req) throws IOException {
     byte[] b = req.getBytes(StandardCharsets.UTF_8);
     DatagramPacket p = new DatagramPacket(b, b.length, serverAddr, SERVER_PORT);
     socket.send(p);
 }

 private String receiveResponse() throws IOException {
     byte[] buf = new byte[BUFFER_SIZE];
     DatagramPacket p = new DatagramPacket(buf, buf.length);
     socket.setSoTimeout(8000);
     socket.receive(p);
     return new String(p.getData(), 0, p.getLength(), StandardCharsets.UTF_8);
 }

 private void setIconFromCode(String code) {
     // code example: "10d"
     try {
         String url = "http://openweathermap.org/img/wn/" + code + "@2x.png";
         BufferedImage img = ImageIO.read(new URL(url));
         if (img != null) {
             Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
             lblIcon.setIcon(new ImageIcon(scaled));
         } else {
             lblIcon.setIcon(null);
         }
     } catch (Exception e) {
         lblIcon.setIcon(null);
         System.err.println("Load icon error: " + e.getMessage());
     }
 }

 // small frame to host chart
 private static class ChartFrame extends JFrame {
     public ChartFrame(String title, Map<String, Double> data) {
         super(title);
         setSize(700, 400);
         setLocationRelativeTo(null);
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         add(new ChartPanel(data));
     }
 }

 // panel tự vẽ biểu đồ đường đơn giản
 private static class ChartPanel extends JPanel {
     private final Map<String, Double> data;
     private final List<String> labels;
     private final List<Double> values;

     public ChartPanel(Map<String, Double> data) {
         this.data = data;
         this.labels = new ArrayList<>(data.keySet());
         this.values = new ArrayList<>(data.values());
     }

     @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (values.isEmpty()) {
             g.drawString("Không có dữ liệu để vẽ", 10, 20);
             return;
         }
         Graphics2D g2 = (Graphics2D) g;
         int w = getWidth();
         int h = getHeight();
         int padding = 50;
         int left = padding;
         int right = w - padding;
         int top = padding;
         int bottom = h - padding;

         // axes
         g2.drawLine(left, bottom, right, bottom); // x axis
         g2.drawLine(left, bottom, left, top); // y axis

         double max = Collections.max(values);
         double min = Collections.min(values);
         double range = (max - min) == 0 ? 1 : (max - min);

         int n = values.size();
         int plotW = right - left;
         int plotH = bottom - top;
         // draw points and lines
         int prevX = -1, prevY = -1;
         for (int i = 0; i < n; i++) {
             int x = left + (int) ((double) i / (n - 1 == 0 ? 1 : (n - 1)) * plotW);
             double v = values.get(i);
             int y = bottom - (int) ((v - min) / range * plotH);
             g2.fillOval(x - 4, y - 4, 8, 8);
             if (prevX != -1) {
                 g2.drawLine(prevX, prevY, x, y);
             }
             prevX = x; prevY = y;
             // label x
             String lbl = labels.get(i);
             g2.drawString(lbl, x - 25, bottom + 20);
             // value label above point
             g2.drawString(String.format("%.1f°C", v), x - 18, y - 8);
         }
         // y ticks
         for (int t = 0; t <= 5; t++) {
             double val = min + (range * t / 5);
             int y = bottom - (int) ((val - min) / range * plotH);
             g2.drawLine(left - 5, y, left, y);
             g2.drawString(String.format("%.1f", val), left - 45, y + 4);
         }
     }
 }

 public static void main(String[] args) {
     // If running on Java 9+ and module issues, run with --add-modules java.desktop
     SwingUtilities.invokeLater(() -> {
         new WeatherClientGUIFull().setVisible(true);
     });
 }
}
