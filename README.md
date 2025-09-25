<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
    ỨNG DỤNG TRA CỨU THỜI TIẾT ONLINE
</h2>
<div align="center">
    <p align="center">
        <img alt="AIoTLab Logo" width="170" src="https://github.com/user-attachments/assets/711a2cd8-7eb4-4dae-9d90-12c0a0a208a2" />
        <img alt="AIoTLab Logo" width="180" src="https://github.com/user-attachments/assets/dc2ef2b8-9a70-4cfa-9b4b-f6c2f25f1660" />
        <img alt="DaiNam University Logo" width="200" src="https://github.com/user-attachments/assets/77fe0fd1-2e55-4032-be3c-b1a705a1b574" />
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

---

## 1. Giới thiệu hệ thống

WeatherApp gồm 2 thành phần chính:

1. **WeatherServer**:  
   - Lắng nghe kết nối TCP trên cổng `50000`.  
   - Nhận tên thành phố từ client.  
   - Lấy dữ liệu thời tiết từ OpenWeatherMap API.  
   - Trả về JSON chứa thông tin thời tiết hiện tại và dự báo.  

2. **WeatherClientUI**:  
   - Giao diện Swing đẹp, gradient background theo loại thời tiết.  
   - Hiển thị nhiệt độ, trạng thái thời tiết, độ ẩm, gió, áp suất.  
   - Hiển thị dự báo 6 giờ gần nhất với icon trực quan.  
   - Tự động cập nhật dữ liệu mỗi 5 giây.

---

## 2. Công nghệ sử dụng

- **Java 11+**  
- **Swing** (GUI)  
- **TCP Socket** (Client ↔ Server)  
- **HttpClient** (Server gọi OpenWeatherMap API)  
- **JSON Processing** (org.json)  
- **FontAwesome Icons** (ikonli)  

---

## 3. Hình ảnh các chức năng

1. **Giao diện chính**  
![Main UI](./images/main_ui.png)

2. **Chọn thành phố và xem dự báo**  
![Forecast UI](./images/forecast_ui.png)

3. **Thông tin chi tiết**  
- Nhiệt độ, độ ẩm, gió, áp suất, trạng thái thời tiết.

---

<h2>4. Hướng dẫn cài đặt và sử dụng</h2>

<h3>Bước 1: Cài Java</h3>
<p>Cài đặt <strong>Java 11+</strong> từ 
  <a href="https://www.oracle.com/java/technologies/downloads/" target="_blank">Oracle Java Downloads</a>.
</p>

<h3>Bước 2: Cấu hình API Key</h3>
<p>Mở file <code>WeatherServer.java</code> và thay <code>API_KEY</code> bằng OpenWeatherMap API key của bạn:</p>

<pre><code>private static final String API_KEY = "YOUR_API_KEY";</code></pre>

<h3>Bước 3: Chạy Server</h3>
<ol>
  <li>Mở terminal hoặc IDE (Eclipse/IntelliJ).</li>
  <li>Chạy <code>WeatherServer.java</code>.</li>
  <li>Console sẽ hiển thị:
    <pre><code>🌤 Weather Server đang chạy tại cổng 12345</code></pre>
  </li>
</ol>

<h3>Bước 4: Chạy Client</h3>
<ol>
  <li>Chạy <code>WeatherClientUI.java</code>.</li>
  <li>Chọn thành phố từ combo box và nhấn nút tìm kiếm.</li>
  <li>GUI sẽ hiển thị thông tin thời tiết hiện tại và dự báo 6 giờ.</li>
</ol>

<h3>Bước 5: Kiểm tra</h3>
<ul>
  <li>Server log sẽ hiển thị client kết nối và thành phố được yêu cầu.</li>
  <li>Client tự động cập nhật dữ liệu mỗi 5 giây.</li>
</ul>

<p><em>⚠ Lưu ý:</em> Server cần có kết nối Internet để gọi API. Client chỉ kết nối được khi server đang chạy trên cùng host hoặc IP đúng.</p>

