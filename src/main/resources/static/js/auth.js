document.addEventListener('DOMContentLoaded', () => {

    // --- ЛОГІКА ВХОДУ (LOGIN) ---
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const alertBox = document.getElementById('loginAlert');

            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email, password })
                });

                if (!response.ok) throw new Error('Невірна пошта або пароль');

                const data = await response.json();

                // Зберігаємо JWT токен у браузері!
                if (data.token) {
                    localStorage.setItem('jwt_token', data.token);
                    localStorage.setItem('user_email', email);
                    window.location.href = 'index.html'; // Перекидаємо на головну
                }
            } catch (err) {
                alertBox.textContent = err.message;
                alertBox.className = 'alert alert-error';
                alertBox.style.display = 'block';
            }
        });
    }
    // --- ЛОГІКА РЕЄСТРАЦІЇ (REGISTER) ---
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const firstname = document.getElementById('firstname').value;
            const lastname = document.getElementById('lastname').value;

            const phone = document.getElementById('phone').value;

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const alertBox = document.getElementById('regAlert');

            try {
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    // ВІДПРАВЛЯЄМО phone, ЯК ОЧІКУЄ JAVA
                    body: JSON.stringify({ firstname, lastname, phone, email, password })
                });

                if (!response.ok) {
                    const errData = await response.json().catch(() => null);
                    throw new Error(errData?.message || 'Помилка реєстрації. Перевірте введені дані.');
                }

                alertBox.textContent = 'Реєстрація успішна! Перенаправляємо на сторінку входу...';
                alertBox.className = 'alert alert-success';
                alertBox.style.display = 'block';

                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000);

            } catch (err) {
                alertBox.textContent = err.message;
                alertBox.className = 'alert alert-error';
                alertBox.style.display = 'block';
            }
        });
    }
});