function closeNotification() {
  const notification = document.getElementById("notification");
  notification.style.display = "none"; // Ẩn thông báo
}

// Hiển thị thông báo tự động ẩn sau 5 giây (tuỳ chọn)
setTimeout(() => {
  const notification = document.getElementById("notification");
  if (notification) {
    notification.style.display = "none";
  }
}, 5000); // 5000ms = 5 giây
