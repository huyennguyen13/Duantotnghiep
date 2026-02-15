const body = document.querySelector("body"),
	nav = body.querySelector("nav"),
	modeToggle = body.querySelector(".dark-light"),
	searchToggle = body.querySelector(".searchToggle"),
	sidebarOpen = body.querySelector(".sidebarOpen"),
	siderbarClose = body.querySelector(".siderbarClose");
let getMode = localStorage.getItem("mode");
if (getMode && getMode === "dark-mode") {
	body.classList.add("dark");
}
// js code to toggle dark and light mode
modeToggle.addEventListener("click", () => {
	modeToggle.classList.toggle("active");
	body.classList.toggle("dark");
	// js code to keep user selected mode even page refresh or file reopen
	if (!body.classList.contains("dark")) {
		localStorage.setItem("mode", "light-mode");
	} else {
		localStorage.setItem("mode", "dark-mode");
	}
});
// js code to toggle search box
searchToggle.addEventListener("click", () => {
	searchToggle.classList.toggle("active");
});

//   js code to toggle sidebar
sidebarOpen.addEventListener("click", () => {
	nav.classList.add("active");
});
body.addEventListener("click", (e) => {
	let clickedElm = e.target;
	if (
		!clickedElm.classList.contains("sidebarOpen") &&
		!clickedElm.classList.contains("menu")
	) {
		nav.classList.remove("active");
	}
});

// Bảng tài khoản
function filterByCriteria() {
  const roleFilter = document.getElementById("roleFilter").value.trim();
  const statusFilter = document.getElementById("statusFilter").value.trim();
  const searchInput = document.getElementById("searchInput").value.trim().toLowerCase();
  const table = document.getElementById("myTable");
  const rows = table.getElementsByTagName("tr");
  let visibleRowCount = 0;

  for (let i = 1; i < rows.length; i++) {
    const roleTd = rows[i].getElementsByTagName("td")[3];
    const statusTd = rows[i].getElementsByTagName("td")[4];
    const nameTd = rows[i].getElementsByTagName("td")[0];

    if (roleTd && statusTd && nameTd) {
      const roleValue = roleTd.textContent.trim();
      const statusValue = statusTd.textContent.trim();
      const nameValue = nameTd.textContent.trim().toLowerCase();

      // Kiểm tra các điều kiện lọc
      const matchesRole = roleFilter === "Tất cả" || roleValue === roleFilter;
      const matchesStatus = statusFilter === "Tất cả" || statusValue === statusFilter;
      const matchesName = nameValue.includes(searchInput);

      if (matchesRole && matchesStatus && matchesName) {
        rows[i].style.display = ""; // Hiển thị hàng
        visibleRowCount++;
      } else {
        rows[i].style.display = "none"; // Ẩn hàng
      }
    }
  }

  // Hiển thị thông báo nếu không có kết quả
  const noResultMessage = document.getElementById("noResultMessage");
  if (visibleRowCount === 0) {
    if (!noResultMessage) {
      const message = document.createElement("tr");
      message.id = "noResultMessage";
      message.innerHTML = `<td colspan="5" class="text-center text-danger">Không tìm thấy kết quả phù hợp</td>`;
      table.querySelector("tbody").appendChild(message);
    }
  } else if (noResultMessage) {
    noResultMessage.remove();
  }
}




// xuất excel
// Hàm xuất bảng ra Excel
function exportTableToExcel(tableID, filename = '') {
	var table = document.getElementById(tableID);
	var wb = XLSX.utils.table_to_book(table, { sheet: "Sheet1" });
	XLSX.writeFile(wb, filename + '.xlsx');
}

// Hàm xác nhận trước khi xuất
function confirmExport(tableID, filename) {
	if (confirm("Bạn muốn xuất tệp Excel?")) {
		exportTableToExcel(tableID, filename);
	}
}

// Bảng Dịch vụ
function filterTable2() {
	var input, filter, table, tr, td, i, txtValue;
	input = document.getElementById("searchInput");
	filter = input.value.toUpperCase();
	table = document.getElementById("myTable");
	tr = table.getElementsByTagName("tr");

	for (i = 1; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td")[1];  // Lọc theo cột tên (cột 2)
		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(filter) > -1) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		}
	}
}
function filterTable3() {
	var input, filter, table, tr, td, i, txtValue;
	input = document.getElementById("searchInput");
	filter = input.value.toUpperCase();
	table = document.getElementById("myTable");
	tr = table.getElementsByTagName("tr");

	for (i = 1; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td")[5];  // Lọc theo cột tên (cột 2)
		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(filter) > -1) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		}
	}
}
function filterTable4() {
	var input, filter, table, tr, td, i, txtValue;
	input = document.getElementById("searchInput2");
	filter = input.value.toUpperCase();
	table = document.getElementById("myTable");
	tr = table.getElementsByTagName("tr");

	for (i = 1; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td")[6];  // Lọc theo cột tên (cột 2)
		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(filter) > -1) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		}
	}
}
//Bang hd
function filterTableHD() {
	const customerFilter = document.getElementById('customerFilter').value.toLowerCase();
	const dateFilter = document.getElementById('dateFilter').value; // Ngày được nhập từ bộ lọc
	const rows = document.querySelectorAll('#orderTable tbody tr');

	rows.forEach(row => {
		const customerName = row.cells[1].textContent.toLowerCase(); // Tên khách hàng
		const createdDateRaw = row.cells[7].textContent.trim(); // Ngày tạo (cột ngày tạo)

		// Định dạng lại ngày từ bảng thành YYYY-MM-DD
		const createdDate = new Date(createdDateRaw);
		const formattedCreatedDate = !isNaN(createdDate)
			? createdDate.toISOString().split('T')[0] // Chuyển thành YYYY-MM-DD
			: '';

		// Kiểm tra các điều kiện lọc
		const matchesCustomer = customerName.includes(customerFilter); // Lọc tên khách hàng
		const matchesDate = !dateFilter || formattedCreatedDate === dateFilter; // Lọc ngày

		// Hiển thị hàng nếu tất cả điều kiện thỏa mãn
		if (matchesCustomer && matchesDate) {
			row.style.display = ''; // Hiển thị hàng
		} else {
			row.style.display = 'none'; // Ẩn hàng
		}
	});
}



// ============Bảng sản phẩm==========
function filterByCriteriaSP() {
	const roleFilter = document.getElementById('roleFilter').value;
	const statusFilter = document.getElementById('statusFilter').value;
	const table = document.getElementById('myTable').getElementsByTagName('tbody')[0];
	const rows = table.getElementsByTagName('tr');

	for (let i = 0; i < rows.length; i++) {
		const typeCell = rows[i].getElementsByTagName('td')[5]; // Cột Loại sản phẩm
		const forCell = rows[i].getElementsByTagName('td')[6]; // Cột Dành cho
		const matchType = roleFilter === 'Tất cả' || typeCell.textContent.includes(roleFilter);
		const matchFor = statusFilter === 'Tất cả' || forCell.textContent.includes(statusFilter);

		rows[i].style.display = matchType && matchFor ? '' : 'none';
	}
}