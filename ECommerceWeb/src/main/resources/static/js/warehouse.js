document.addEventListener("DOMContentLoaded", function() {
    fetch('/global')
        .then(response => response.json()) // 将响应转换为 JSON
        .then(data => {
            const goodsData = { // 构建要发送的数据对象
                sid: data.id, // 使用从后端获取的用户 ID

            };
            fetch(`/seller/warehouse/goods/${data.id}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    const goodsList = document.getElementById('goods-list');
                    if (!goodsList) {
                        console.error('Element with id "goods-list" not found');
                        return;
                    }
                    data.forEach(good => {
                        const item = document.createElement('div');
                        item.classList.add('lk-goods-item');
                        item.innerHTML = `
                    <div class="lk-goods-info">
                        <h3>${good.name}</h3>
                        <p>价格: ${good.price} 元</p>
                        ${good.sale < 1 ? `<p>折扣价: ${(good.price * good.sale).toFixed(2)} 元</p>` : ''}
                        <p>类别:${good.goodtype}</p>
                        <p>${good.description}</p>
                        <div class="lk-goods-actions">
                            <a href="/seller/warehouse/goodsedit/${good.id}" class="button-link">编辑</a>
                             <button class="button-link" onclick="deleteGood(${good.id})">删除</button>
                        </div>
                    </div>

                `;
                        goodsList.appendChild(item);
                    });
                })
                .catch(error => {
                    console.error('Error fetching goods:', error);
                });
            {
            }
        });



});
function deleteGood(id) {
    if (confirm('确定要删除该商品吗？')) {
        // 创建一个表单
        const form = document.createElement('form');
        form.method = 'POST'; // 使用POST方法模拟DELETE请求

        form.action = `/seller/warehouse/${id}`; // 指定后端处理删除的URL

        // 添加隐藏字段，用于标识这是一个删除操作
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = '_method';
        input.value = 'DELETE';
        form.appendChild(input);
        console.log('Form action:', form.action); // 调试信息
        console.log('Form method:', form.method); // 调试信息
        console.log('Form input:', input); // 调试信息
        // 将表单添加到文档中并提交
        document.body.appendChild(form);
        form.submit();
    }
}