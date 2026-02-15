let reloadTimeout; // Biến lưu trữ thời gian hủy lệnh reload

function reloadPage() {
  try {
    // Hiển thị biểu tượng tải
    document.getElementById("loading").style.display = "block";
    // Đặt thời gian 2 giây để thực hiện reload
    let reloadOperation = setTimeout(() => {
      if(location.href != "/assets/view/Admin.html"){
        location.href = "/assets/view/Admin.html"
      }else{
        location.replace(location.href);
      }
    }, 2000); // Tải lại sau 2 giây

    // Hủy lệnh reload sau 2 giây nếu chưa kịp thực hiện
    reloadTimeout = setTimeout(() => {
      clearTimeout(reloadOperation); // Hủy lệnh reload
      document.getElementById("loading").style.display = "none";
      document.getElementById("errorMessage").style.display = "block";
      document.getElementById("errorMessage").innerText =
        "Lệnh tải lại đã bị hủy sau 2 giây.";
    }, 2200); // Hủy sau 2 giây
  } catch (error) {
    document.getElementById("errorMessage").innerText =
      "Đã xảy ra lỗi khi tải lại trang: " + error.message;
    console.error("Lỗi khi tải lại trang:", error);
  }
}
