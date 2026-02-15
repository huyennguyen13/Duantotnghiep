// Hàm cập nhật thời gian
function updateClock() {
    const timeElement = document.getElementById('time');
    const dateElement = document.getElementById('date');
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    // Cập nhật thời gian
    timeElement.textContent = `${hours}:${minutes}:${seconds}`;

    // Cập nhật ngày
    const daysOfWeek = [ 'Chủ Nhật', 'Thứ Hai', 'Thứ ba', 'Thứ tư', 'Thứ năm', 'Thứ sáu', 'Thứ bảy'];
    const day = daysOfWeek[now.getDay()];
    const date = String(now.getDate()).padStart(2, '0');
    const month = String(now.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    const year = now.getFullYear();
    dateElement.textContent = `${day}, ${date}-${month}-${year}`;
}

// Hàm lấy thời tiết từ API OpenWeatherMap và cập nhật icon
function getWeather() {
    const apiKey = '969977a8fde79c02378a692362e1e383';  // API key 
    const city = 'Can Tho';  // Tên thành phố
    const apiUrl = `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${apiKey}&units=metric`;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const weatherIconElement = document.getElementById('weather-icon');
            const iconCode = data.weather[0].icon;  // Lấy mã icon thời tiết
            const iconUrl = `http://openweathermap.org/img/wn/${iconCode}@2x.png`;
            
            weatherIconElement.src = iconUrl;
            weatherIconElement.style.display = 'block';  // Hiển thị icon
        })
        .catch(error => {
            console.error('Error fetching weather data:', error);
            document.getElementById('weather-icon').style.display = 'none';  // Ẩn icon nếu lỗi
        });
}

// Cập nhật thời gian mỗi giây
setInterval(updateClock, 1000);
updateClock();  // Gọi để cập nhật ngay khi tải trang

// Lấy thông tin thời tiết khi tải trang
getWeather();
