document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('jwt_token');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    // 1. ЗАХИСТ МАРШРУТУ (Перевірка ролі)
    fetch('/api/users/me', {
        headers: { 'Authorization': `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(user => {
            // Якщо роль не містить слово "admin" (незалежно від регістру) — викидаємо
            const role = (user.roleName || user.role || '').toLowerCase();
            if (!role.includes('admin')) {
                alert('Помилка доступу: Ця сторінка тільки для адміністраторів!');
                window.location.href = 'index.html';
            }
        })
        .catch(() => window.location.href = 'login.html');

    // 2. ЗАВАНТАЖЕННЯ ЖАНРІВ В SELECT
    fetch('/api/genres')
        .then(res => res.json())
        .then(genres => {
            const genreSelect = document.getElementById('bookGenre');
            genreSelect.innerHTML = '';
            genres.forEach(g => {
                const option = document.createElement('option');
                option.value = g.id; // Відправляємо ID жанру
                option.innerText = g.name;
                genreSelect.appendChild(option);
            });
        });

    // 3. ОБРОБКА ФОРМИ ДОДАВАННЯ КНИГИ
    document.getElementById('addBookForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const alertBox = document.getElementById('adminAlert');

        // Збираємо DTO для відправки (адаптуй під свій бекенд, якщо він чекає інші поля)
        const newBook = {
            name: document.getElementById('bookName').value,
            author: document.getElementById('bookAuthor').value,
            language: document.getElementById('bookLanguage').value,
            pages: parseInt(document.getElementById('bookPages').value),
            genre: { id: document.getElementById('bookGenre').value },
            bookStatus: { id: document.getElementById('bookStatus').value }
        };

        try {
            const response = await fetch('/api/books', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newBook)
            });

            if (!response.ok) throw new Error('Не вдалося додати книгу. Перевірте логіку бекенду.');

            alertBox.textContent = 'Книгу успішно додано в базу!';
            alertBox.className = 'alert alert-success';
            alertBox.style.display = 'block';

            document.getElementById('addBookForm').reset(); // Очищаємо форму

        } catch (err) {
            alertBox.textContent = err.message;
            alertBox.className = 'alert alert-error';
            alertBox.style.display = 'block';
        }
    });
});