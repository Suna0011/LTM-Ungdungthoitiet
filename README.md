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

🌦️ WeatherApp – UDP Client/Server đa luồng (Java)
📌 Giới thiệu

WeatherApp là ứng dụng dự báo thời tiết được xây dựng bằng Java, gồm 2 phần:

WeatherServerMulti: server UDP đa luồng, tiếp nhận yêu cầu từ client, gọi API OpenWeatherMap và trả về dữ liệu.

WeatherClientGUIFull: client có giao diện đồ họa (Swing), cho phép người dùng:

Xem thời tiết hiện tại (nhiệt độ, trạng thái, icon).

Xem dự báo 5 ngày (trung bình nhiệt độ, hiển thị biểu đồ).

Xem lịch sử truy vấn thời tiết.

Ứng dụng phù hợp để học tập lập trình socket, đa luồng, GUI và xử lý API JSON.

⚙️ Công nghệ sử dụng

Java SE 8+

UDP Socket (DatagramSocket, DatagramPacket)

ExecutorService (đa luồng xử lý song song)

Swing (JFrame, JPanel, JTextArea, JButton, JLabel, …) để xây dựng giao diện

Regex & Stream API để parse JSON nhanh

OpenWeatherMap API để lấy dữ liệu thời tiết

🖼️ Hình ảnh minh họa

<img width="573" height="415" alt="image" src="https://github.com/user-attachments/assets/3be64552-d811-45a8-a05c-895f3e1537c8" />
<img width="675" height="387" alt="image" src="https://github.com/user-attachments/assets/b04398a8-f8cd-4dfd-a53b-99e2306676e7" />

(ảnh minh họa, bạn có thể thay bằng screenshot thực tế khi chạy chương trình)

🚀 Cài đặt và chạy thử
1. Yêu cầu

Cài đặt Java JDK 8+

Có kết nối mạng Internet

Tài khoản và API key của OpenWeatherMap

2. Clone project
git clone https://github.com/yourusername/WeatherApp.git
cd WeatherApp

3. Chạy Server

Biên dịch:

javac -d out src/tt/*.java


Chạy server:

java -cp out tt.WeatherServerMulti


Mặc định server chạy trên port 9999

4. Chạy Client GUI

Trong terminal khác:

java -cp out tt.WeatherClientGUIFull

5. Sử dụng

Nhập tên thành phố (ví dụ: Hanoi, London, Tokyo).

Chọn "Dự báo hiện tại" để xem thông tin thời tiết hiện tại.

Chọn "Xem biểu đồ 5 ngày" để hiển thị dự báo nhiệt độ trung bình.

Chọn "Xem lịch sử" để xem các lần truy vấn.

📬 Liên Hệ

Tác giả: [Tên bạn]

📧 Email: your.email@example.com

🌐 GitHub: https://github.com/yourusername
