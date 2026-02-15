google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var data = google.visualization.arrayToDataTable([
        ['Khung giờ', 'Lượt xem', 'Đơn hàng', 'Truy cập', 'Tỷ lệ chuyển đổi'],
        ['0 giờ', 1200, 300, 800, 15],
        ['6 giờ', 1500, 400, 600, 18],
        ['12 giờ', 800, 250, 900, 12],
        ['18 giờ', 1700, 500, 1200, 20]
    ]);

    var options = {
        title: 'Biểu đồ Cột theo Khung Giờ',
        hAxis: {
            title: 'Giá trị',
        },
        vAxis: {
            title: 'Khung giờ'
        },
        series: {
            0: { color: '#242424' },
            1: { color: '#0D6EFD' },
            2: { color: '#D72228' },
            3: { color: '#1b9e77' }
        },
        legend: { position: 'top' },
    };

    var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}
