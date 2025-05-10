// 添加销售人员表单的事件监听器
document.getElementById('addSellerForm').addEventListener('submit', async (event) => {
    event.preventDefault(); // 阻止表单默认提交行为

    // 获取表单输入值
    const name = document.getElementById('sellerName').value;
    const password = document.getElementById('sellerPassword').value;

    try {
        // 发送 POST 请求添加销售人员
        const response = await fetch('/manager/sellers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, password })
        });

        if (response.ok) {
            alert('销售人员添加成功');
            // 重新加载销售人员列表
            await loadSellers();
        } else {
            const errorData = await response.json();
            alert('添加失败: ' + errorData.message);
        }
    } catch (error) {
        console.error('添加销售人员失败:', error);
        alert('添加销售人员失败: 网络错误');
    }
});

// 加载销售人员列表
async function loadSellers() {
    try {
        const response = await fetch('/manager/sellers');
        if (!response.ok) {
            throw new Error('加载销售人员失败');
        }
        const sellers = await response.json();
        displaySellers(sellers);
    } catch (error) {
        console.error('加载销售人员失败:', error);
        alert('加载销售人员失败: 网络错误');
    }
}

// 显示销售人员列表
function displaySellers(sellers) {
    const tableBody = document.querySelector('#sellersTable tbody');
    tableBody.innerHTML = ''; // 清空表格内容

    sellers.forEach(seller => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${seller.id}</td>
            <td>${seller.name}</td>
            <td>
                <button onclick="deleteSeller(${seller.id})">删除</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// 删除销售人员
async function deleteSeller(id) {
    if (confirm('确定要删除该销售人员吗？')) {
        try {
            const response = await fetch(`/manager/sellers/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('销售人员删除成功');
                // 重新加载销售人员列表
                loadSellers();
            } else {
                const errorData = await response.json();
                alert('删除失败: ' + errorData.message);
            }
        } catch (error) {
            console.error('删除销售人员失败:', error);
            alert('删除销售人员失败: 网络错误');
        }
    }
}

// 页面加载完成后立即加载销售人员列表
window.onload = loadSellers;