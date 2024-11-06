
const xValues1 = ["Ordinary", "Body Oil", "Face Masks", "Make Up", "Hair Care", "Skincare", "Tool"];
const yValues1 = [1155, 349, 124, 434, 23, 343, 44];
const barColors1 = '#000';

new Chart("chart1", {
    type: "bar",
    data: {
    labels: xValues1,
    datasets: [{
        backgroundColor: barColors1,
        data: yValues1,
        hoverBorderWidth: 1,
        hoverBorderColor: '#000'
    }]
    },
    options:{
        title: {
            display: true,
            text: 'Số lượng các sản phẩm của danh mục đã tiêu thụ',
            fontSize: 14,
        },
        legend: {
            display: false
        }
    }
});
