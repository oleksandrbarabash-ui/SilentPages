document.addEventListener('DOMContentLoaded', () => {
    // Отримуємо ID книги з URL
    const urlParams = new URLSearchParams(window.location.search);
    const bookId = urlParams.get('id');

    if (!bookId) {
        document.getElementById('bookDetails').innerHTML = '<h2 style="color: red; text-align: center;">Книгу не знайдено</h2>';
        return;
    }

    fetch(`/api/books/${bookId}`)
        .then(response => {
            if (!response.ok) throw new Error('Помилка завантаження');
            return response.json();
        })
        .then(book => {
            document.getElementById('bookTitle').innerText = book.name;
            document.getElementById('bookAuthor').innerText = book.author;
            document.getElementById('bookGenre').innerText = book.genreName || '—';
            document.getElementById('bookLanguage').innerText = book.language || '—';
            document.getElementById('bookPages').innerText = book.pages || '—';

            const statusEl = document.getElementById('bookStatus');
            statusEl.innerText = book.statusName || '—';

            const statusText = book.statusName ? book.statusName.toLowerCase() : '';

            if (statusText === 'в наявності' || statusText === 'в наличии' || statusText === 'available') {
                statusEl.className = 'status-badge status-available'; // Зелений
            } else {
                statusEl.className = 'status-badge status-borrowed'; // Червоний
            }
        })
        .catch(error => {
            console.error(error);
            document.getElementById('bookDetails').innerHTML = '<h2 style="color: red; text-align: center;">Помилка підключення до сервера</h2>';
        });
});