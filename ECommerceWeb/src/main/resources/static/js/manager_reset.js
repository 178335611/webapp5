
document.getElementById('resetPasswordForm').addEventListener('submit', async (event) => {
    event.preventDefault();
    const sellerId = document.getElementById('sellerId').value;
    const newPassword = document.getElementById('newPassword').value;

    try {
        const response = await fetch(`/manager/sellers/${sellerId}/reset-password`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ password: newPassword })
        });

        if (response.ok) {
            alert('销售人员登录口令重置成功');
        } else {
            const errorData = await response.json();
            alert('重置失败: ' + errorData.message);
        }
    } catch (error) {
        console.error('重置销售人员登录口令失败:', error);
        alert('重置销售人员登录口令失败: 网络错误');
    }
});