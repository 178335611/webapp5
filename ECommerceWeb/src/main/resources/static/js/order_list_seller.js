

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
    orders.forEach(order => {
        const row = document.createElement('tr');
        row.innerHTML = `
                    <td>${order.id}</td>
                    <td>${order.sum}</td>
                    <td>${order.good_name}</td>
                    <td>${order.good_type}</td>
                    <td>${order.totalprice}</td>
                    <td>${order.status}</td>
                    <td>${new Date(order.order_date).toLocaleString()}</td>
                `;
        // 创建修改状态按钮
        const statusButton = document.createElement('button');
        statusButton.textContent = '修改状态';
        statusButton.classList.add('status-button');
        statusButton.dataset.orderId = order.id; // 存储订单ID
        statusButton.addEventListener('click', () => updateOrderStatus(order.id)); // 绑定点击事件

        // 创建取消订单按钮
        const cancelButton = document.createElement('button');
        cancelButton.textContent = '取消订单';
        cancelButton.classList.add('cancel-button');
        cancelButton.dataset.orderId = order.id; // 存储订单ID
        cancelButton.addEventListener('click', () => cancelOrder(order.id)); // 绑定点击事件

        // 将按钮添加到行中
        const buttonCell = document.createElement('td');
        buttonCell.appendChild(statusButton);
        buttonCell.appendChild(cancelButton);
        row.appendChild(buttonCell);

        // 将行添加到表格中
        tableBody.appendChild(row);
    });
    document.getElementById('loadingMessage').style.display = 'none'; // 隐藏加载提示
}
// 修改订单状态的函数
async function updateOrderStatus(orderId) {

    // 发送POST请求更新订单状态
    const response = await fetch(`/api/order_status_seller/${orderId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderId)
    });

    if (!response.ok) {
        throw new Error(`Network response was not ok: ${response.statusText}`);
    }

    alert('订单状态已更新');
    // 重新加载订单列表
    fetchOrders();
}

async function cancelOrder(orderId) {
    try {
        // 发送POST请求取消订单
        const response = await fetch(`/api/cancel_order_seller/${orderId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderId)
        });

        if (!response.ok) {
            throw new Error(`Network response was not ok: ${response.statusText}`);
        }

        alert('订单已取消');
        // 重新加载订单列表
        fetchOrders();
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
        alert('取消订单失败，请稍后再试');
    }
}
// 页面加载完成后立即获取订单数据
window.onload = fetchOrders;
