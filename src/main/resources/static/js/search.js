const urlParams = new URLSearchParams(window.location.search);
const query = urlParams.get("query"); // Từ khóa tìm kiếm
const resultsContainer = document.getElementById("results"); // Vùng hiển thị kết quả

if (!query || query.trim() === "") {
  resultsContainer.innerHTML = "<p>Vui lòng nhập từ khóa để tìm kiếm.</p>";
} else {
  fetch(`/customer/sanpham/search?query=${encodeURIComponent(query)}&page=0&size=10`) // Điều chỉnh đúng API URL
    .then((response) => {
      if (!response.ok) {
        throw new Error("Lỗi khi lấy dữ liệu từ server");
      }
      return response.json(); // Chuyển dữ liệu JSON từ API thành object
    })
    .then((data) => {
      if (data.length === 0) {
        resultsContainer.innerHTML = "<p>Không tìm thấy sản phẩm nào.</p>";
      } else {
        const html = data
          .map(
            (item) => `
              <div class="product">
                  <img src="${item.imageUrl}" alt="${item.tenSanPham}" class="product-image"/>
                  <h3>${item.tenSanPham}</h3>
                  <p>Giá: ${item.giaBan} VND</p>
                  <p>Xuất xứ: ${item.xuatXu}</p>
              </div>
              <footer th:replace="~{views/layout/footer :: footer}"></footer>
          `
          )
          .join("");

        resultsContainer.innerHTML = html;
      }
    })
    .catch((error) => {
      console.error("Lỗi:", error);
      resultsContainer.innerHTML = "<p>Đã xảy ra lỗi, vui lòng thử lại sau.</p>";
    });
}
