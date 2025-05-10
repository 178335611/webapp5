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