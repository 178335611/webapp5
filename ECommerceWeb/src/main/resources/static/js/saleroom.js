

// 获取订单数据
async function fetchOrders() {
    try {
        // 获取全局数据（例如用户ID）
        const globalResponse = await fetch('/global');
        const globalData = await globalResponse.json();

        const uid = globalData.id; // 从全局数据中获取用户ID
        let type=globalData.type;

        // 使用获取到的用户ID发送POST请求
        let response;
        if(type==='user')
            response = await fetch(`/api/order_list/${uid}`);
        if(type==='seller')
            response = await fetch(`/api/order_list_seller/${uid}`);

        if (!response.ok) {
            throw new Error(`Network response was not ok: ${response.statusText}`);
        }
        const orders = await response.json();
        displayOrders(orders); // 显示空列表
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }
}

// 动态显示订单数据
function displayOrders(orders) {
    const tableBody = document.querySelector('#ordersTable tbody');
    tableBody.innerHTML = ''; // 清空表格内容
    const categorySummary = {
        "电子数码": 0,
        "服装配饰": 0,
        "家居生活": 0,
        "食品饮料": 0,
        "文艺作品": 0,
        "工具耗材": 0,
        "其他": 0
    };
    orders.forEach(order => {
        const category = order.good_type || "其他"; // 如果没有类别信息，默认归为“其他”
        if (categorySummary.hasOwnProperty(category)) {
            categorySummary[category] += order.totalprice; // 累加总价值
        } else {
            categorySummary["其他"] += order.totalprice; // 未知类别归为“其他”
        }
    });

    for (const category in categorySummary) {
        const totalValue = categorySummary[category];
        if (totalValue > 0) { // 只显示有订单的类别
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${category}</td>
                <td>${totalValue}</td>
            `;
            tableBody.appendChild(row);
        }
    }
    document.getElementById('loadingMessage').style.display = 'none'; // 隐藏加载提示
}
// 修改订单状态的函数
// 页面加载完成后立即获取订单数据
window.onload = fetchOrders;
