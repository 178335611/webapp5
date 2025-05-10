document.addEventListener("DOMContentLoaded", function() {

        fetch('/user/shoppingMall/goods')
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
                    <p>类别: ${good.goodtype}</p>
                    <p>${good.description}</p>
                    <div class="lk-goods-actions">
                        <a href="/user/order_add/${good.id}" class="button-link" id="${good.id}">提交订单</a>
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

//搜索功能
document.getElementById('search-form').addEventListener('submit',
    function(event) {
        event.preventDefault(); // 阻止表单默认提交行为

        const keywordInput = document.getElementById('keyword'); // 获取输入框
        const keyword = keywordInput.value.trim(); // 获取关键字并去除空格
        let url = '/api/goods/search';
        if (keyword) {
            url += `?keyword=${encodeURIComponent(keyword)}`; // 将关键字作为查询参数附加到 URL
        }

        fetch(url)
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
                // 清空旧内容
                goodsList.innerHTML = '';
                data.forEach(good => {
                    const item = document.createElement('div');
                    item.classList.add('lk-goods-item');
                    item.innerHTML = `
                <div class="lk-goods-info">
                    <h3>${good.name}</h3>
                    <p>价格: ${good.price} 元</p>
                    ${good.sale < 1 ? `<p>折扣价: ${(good.price * good.sale).toFixed(2)} 元</p>` : ''}
                    <p>类别: ${good.goodtype}</p>
                    <p>${good.description}</p>
                    <div class="lk-goods-actions">
                        <a href="/user/order_add/${good.id}" class="button-link" id="${good.id}">提交订单</a>
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


    });