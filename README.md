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
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

Ứng dụng Tra cứu Thời tiết Online

## 1. 🚀 Giới thiệu
Ứng dụng **Tra cứu Thời tiết Online** cho phép người dùng xem thông tin thời tiết theo vị trí (thành phố/tỉnh/thị trấn) một cách nhanh chóng và trực quan.  

Người dùng chỉ cần nhập tên địa điểm, hệ thống sẽ hiển thị:  
- 🌡️ Nhiệt độ hiện tại  
- 💧 Độ ẩm, 🌬️ tốc độ gió  
- 🌦️ Dự báo thời tiết trong ngày  
- ☀️🌧️ Mô tả trạng thái thời tiết (nắng, mưa, nhiều mây, …)  

Ứng dụng sử dụng dữ liệu từ **OpenWeatherMap API**, toàn bộ việc gọi API được thực hiện ở **Server**. Client gửi yêu cầu qua **TCP Socket** tới Server để lấy kết quả.

---

## 2. 🛠️ Công nghệ sử dụng
- **Ngôn ngữ:** Java  
  - Client: Java Swing (Desktop UI)  
  - Server: Java Socket (TCP)  
- **API:** [OpenWeatherMap API](https://openweathermap.org/api)  
- **Giao tiếp mạng:** TCP Socket (Client ↔ Server)  
- **Lưu trữ (tùy chọn):** SQLite/MySQL hoặc file JSON để ghi log lịch sử  

---

## 3. 📷 Hình ảnh minh họa

### 🖥️ Giao diện chính (Client)
👉 *Thêm ảnh chụp màn hình giao diện chính ở đây*  

### 📊 Kết quả tra cứu
👉 *Thêm ảnh chụp màn hình hiển thị kết quả tra cứu ở đây*  

### ⚙️ Server Console
👉 *Thêm ảnh chụp màn hình console của Server ở đây*  

---

## 4. 📥 Cài đặt & Chạy thử

### 1️⃣ Đăng ký API Key
- Truy cập [OpenWeatherMap](https://openweathermap.org/api)  
- Tạo tài khoản và lấy **API Key** miễn phí  

### 2️⃣ Cấu hình Project
Tạo 2 module chính:  
- **Server** (`WeatherServer.java`):  
  - Dùng `ServerSocket` để lắng nghe TCP  
  - Gọi OpenWeatherMap API để lấy dữ liệu thời tiết  
- **Client** (`WeatherClientGUI.java`):  
  - Dùng Swing để xây dựng giao diện  
  - Gửi request TCP đến Server và hiển thị kết quả  

### 3️⃣ Chạy chương trình

Biên dịch & chạy **Server** trước:
```sh
javac WeatherServer.java
java weatherapp.server.WeatherServer
Sau đó biên dịch & chạy Client:

sh
Sao chép mã
javac WeatherClientGUI.java
java weatherapp.client.WeatherClientGUI
5. 📚 Kiến trúc hệ thống
Client GUI (Swing):
Nhập địa điểm → gửi request TCP → nhận dữ liệu thời tiết → hiển thị giao diện

Server (Socket):
Nhận request từ Client → gọi OpenWeatherMap API → parse JSON → gửi lại kết quả

Giao thức TCP (text-based):

Client gửi: CITY: <tên_thành_phố>

Server trả: temperature, humidity, wind, description

6. 👨‍💻 Nhóm thực hiện
Sinh viên khoa Công nghệ Thông tin – Đại học Đại Nam

Hướng dẫn: AIoT Lab – Faculty of IT, DaiNam University
