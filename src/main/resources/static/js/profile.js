document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt_token');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    document.getElementById('profileLogoutBtn').addEventListener('click', () => {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_email');
        window.location.href = 'index.html';
    });

    function loadUserProfile() {
        fetch('/api/users/me', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                if (res.status === 401 || res.status === 403) {
                    localStorage.removeItem('jwt_token');
                    window.location.href = 'login.html';
                    throw new Error('Сесія завершилась');
                }
                return res.json();
            })
            .then(user => {
                // Склеюємо ім'я та прізвище, які прийшли з Java
                const firstName = user.firstname || user.firstName || '';
                const lastName = user.lastname || user.lastName || '';
                let fullName = (firstName + ' ' + lastName).trim();

                if (!fullName) fullName = 'Не вказано';

                // Заповнюємо дані на сторінці
                document.getElementById('briefName').innerText = fullName !== 'Не вказано' ? fullName : 'Користувач';
                document.getElementById('briefEmail').innerText = user.email;

                document.getElementById('userId').innerText = user.id || '—';
                document.getElementById('userName').innerText = fullName; // Виводимо повне ім'я
                document.getElementById('userEmail').innerText = user.email;
                document.getElementById('userRole').innerText = user.roleName || user.role || 'Користувач';

                // УМОВНЕ ВІДОБРАЖЕННЯ КНОПКИ АДМІНА
                const role = (user.roleName || user.role || '').toLowerCase();
                if (role.includes('admin')) {
                    const adminBtnHtml = `
                        <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px dashed var(--border-color); text-align: center;">
                            <a href="admin.html" class="btn-submit" style="display: inline-block; text-decoration: none; background: #4f46e5;">
                                ⚙️ Перейти в Панель Адміністратора
                            </a>
                        </div>
                    `;
                    document.getElementById('tab-account').insertAdjacentHTML('beforeend', adminBtnHtml);
                }
            })
            .catch(err => console.error('Помилка завантаження профілю:', err));
    }

    const menuItems = document.querySelectorAll('.menu-item');
    const tabPanels = document.querySelectorAll('.tab-panel');

    menuItems.forEach(item => {
        item.addEventListener('click', () => {
            menuItems.forEach(el => el.classList.remove('active'));
            tabPanels.forEach(panel => panel.classList.remove('active'));

            item.classList.add('active');

            const targetTab = item.getAttribute('data-tab');
            document.getElementById(targetTab).classList.add('active');
        });
    });

    loadUserProfile();
});