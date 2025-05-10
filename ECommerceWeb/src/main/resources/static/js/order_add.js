document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('order-add-form');
    const orderadd = document.getElementById('order-add-btn');
    const returnbtn = document.getElementById('return-btn');
    fetch('/global')
        .then(response => response.json()) // 将响应转换为 JSON
        .then(data => {
            orderadd.addEventListener('click', function (event) {
                // 自动填充当前日期到订单日期字段
                const orderDateInput = document.getElementById('orderDate');
                const currentDate = new Date()// 获取当前日期
                orderDateInput.value = currentDate;

                event.preventDefault(); // 阻止表单默认提交行为

                const formData = new FormData(form); // 初始化 formData
                unitPrice = parseFloat(formData.get('unitPrice'));
                sum = parseFloat(formData.get('sum'));
                if (!Number.isInteger(sum) || sum <= 0) {
                    alert('数量必须为正整数!');
                    throw new Error('Sum must be a positive integer.');
                }
                // 获取表单数据
                const orderData = {
                    uid: data.id,
                    sid:parseFloat(formData.get('sid')),
                    gid: parseFloat(formData.get('gid')),
                    sum: parseFloat(formData.get('sum')),
                    totalprice: unitPrice * sum,
                    order_Date: currentDate,
                    status: "待付款"
                };

                // 发送 AJAX 请求到后端
                fetch('/user/order_add', { // 假设后端处理添加订单的 URL 是 /user/order_add
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(orderData)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // 处理后端返回的数据
                        alert('订单添加成功！');
                        window.location.href = '/user/shoppingMall'; // 跳转到商品购物页面
                    })
                    .catch(error => {
                        // 处理错误
                        console.error('There was a problem with the fetch operation:', error);
                        alert('订单添加失败，请稍后重试或联系管理员。');
                    });
            });
            returnbtn.addEventListener('click', function (event) {
                event.preventDefault(); // 阻止表单默认提交行为

                const formData = new FormData(form); // 初始化 formData
                const logData = {
                    uid: data.id,
                    action: "访问商品结束",
                    goodcategory: formData.get('goodtype'),
                    goodid: formData.get('gid')
                }
                fetch('/user/order_add/logout', { // 假设后端处理添加订单的 URL 是 /user/order_add
                    method: 'Post',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(logData)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log('Log action recorded successfully:', data);
                        window.location.href = '/user/shoppingMall';
                    })
                    .catch(error => {
                        console.error('log error:', error);
                        });

            });
        })
        .catch(error => {
            console.error('Error fetching user info:', error); // 打印获取用户信息时的错误
            alert('无法获取用户信息，请检查网络连接或联系管理员!');
        });

});