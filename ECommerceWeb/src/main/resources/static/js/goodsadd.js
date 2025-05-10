document.getElementById('goods-form').addEventListener('submit',
    function(event) {
    event.preventDefault(); // 阻止表单默认提交行为

    // 调用后端 API 获取用户信息
    fetch('/global')
        .then(response => response.json()) // 将响应转换为 JSON
        .then(data => {
            const formData = new FormData(this); // 获取表单数据
            const goodsData = { // 构建要发送的数据对象
                name: formData.get('name'),
                price: parseFloat(formData.get('price')),
                sale: parseFloat(formData.get('sale')),
                goodtype: formData.get('goodtype'),
                description: formData.get('description'),
                sid: data.id // 使用从后端获取的用户 ID
            };

            // 发送表单数据到后端
            fetch('/seller/warehouse/goodsadd', { // 假设后端接收表单数据的 API 路径是 /seller/warehouse/goodsadd
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json' // 设置请求头，表明发送的是 JSON 数据
                },
                body: JSON.stringify(goodsData) // 将数据对象转换为 JSON 字符串
            })
                .then(response => {
                    if (!response.ok) {
                        // 如果响应状态码不是 2xx，抛出错误
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(responseData => {
                    if (responseData) {
                        alert('商品添加成功');
                        window.location.href = '/seller/warehouse/goodsadd'; // 重定向到商品列表页面
                    } else {
                        // 如果服务器返回了失败信息，显示给用户
                        alert('添加商品失败: ' + ( '未知错误'));
                    }
                })
                .catch(error => {
                    // 捕获网络错误或其他错误，并显示给用户
                    console.error('Error:', error);
                    alert('添加商品失败，请检查网络连接或联系管理员。');
                });
        })
        .catch(error => {
            console.error('Error fetching user info:', error); // 打印获取用户信息时的错误
            alert('无法获取用户信息，请检查网络连接或联系管理员。');
        });
});