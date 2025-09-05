<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   TRA CỨU THỜI TIẾT ONLINE
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

# Ứng dụng Tra cứu Thời tiết Online

## 1. Giới thiệu hệ thống
Ứng dụng **Tra cứu Thời tiết Online** cho phép người dùng xem thông tin thời tiết theo vị trí (thành phố/tỉnh/thị trấn) một cách nhanh chóng và trực quan.  

Người dùng chỉ cần nhập tên địa điểm, hệ thống sẽ hiển thị:  
- Nhiệt độ hiện tại  
- Độ ẩm, tốc độ gió  
- Dự báo thời tiết trong ngày  
- Mô tả trạng thái thời tiết (nắng, mưa, nhiều mây, ...)  

Ứng dụng sử dụng dữ liệu từ **OpenWeatherMap API**, toàn bộ việc gọi API được thực hiện ở **Server**. Client gửi yêu cầu qua **TCP Socket** tới Server để lấy kết quả.

---

## 2. Công nghệ sử dụng
- Ngôn ngữ: Java (Swing cho Client, Java Socket cho Server)  
- API: [OpenWeatherMap API](https://openweathermap.org/api)  
- Giao tiếp mạng: TCP Socket (Client ↔ Server)  
- Giao diện: Java Swing (Desktop App)  
- Cơ sở dữ liệu (tuỳ chọn): SQLite/MySQL hoặc lưu file JSON để ghi log lịch sử  

---

## 3. Hình ảnh chức năng

### Giao diện chính (Client)
Ô nhập tên thành phố và nút "Tra cứu"  
![Main GUI](https://via.placeholder.com/600x300.png?text=Weather+App+Main+UI)

### Kết quả tra cứu
Client nhận dữ liệu từ Server và hiển thị thông tin:  
- Nhiệt độ  
- Độ ẩm  
- Tốc độ gió  
- Trạng thái thời tiết  

![Result](https://via.placeholder.com/600x300.png?text=Weather+Result)

### Server Console
Server hiển thị log khi có Client kết nối và yêu cầu dữ liệu  

![Server](https://via.placeholder.com/600x300.png?text=Weather+Server+Log)

*(Ảnh demo có thể thay bằng screenshot thực tế sau khi chạy ứng dụng)*

---

## 4. Các bước cài đặt

### 1. Đăng ký API Key
- Truy cập [OpenWeatherMap](https://openweathermap.org/api)  
- Tạo tài khoản và lấy **API Key** miễn phí  

### 2. Cấu hình Project
- Tạo 2 module:  
  - Server: dùng `ServerSocket` để lắng nghe TCP, gọi API để lấy dữ liệu  
  - Client: dùng Swing, kết nối tới Server qua TCP để nhận dữ liệu  

### 3. Chạy chương trình
Chạy Server trước:
```sh
javac WeatherServer.java
java weatherapp.server.WeatherServer
