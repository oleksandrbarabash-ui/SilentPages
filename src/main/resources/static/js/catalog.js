let allBooks = [];
let currentGenre = 'all';
let currentLanguage = 'all';

document.addEventListener('DOMContentLoaded', () => {
    const catalogDiv = document.getElementById('catalog');
    const searchInput = document.getElementById('searchInput');
    const searchBtn = document.getElementById('searchBtn');
    const resultsCount = document.getElementById('resultsCount');

    function checkAuth() {
        const token = localStorage.getItem('jwt_token');
        const userEmail = localStorage.getItem('user_email');
        const navActions = document.getElementById('navActions');

        if (token && userEmail) {
            navActions.innerHTML = `
                <a href="profile.html" style="text-decoration: none;">
                    <div class="user-profile">
                        <span class="user-avatar">👤</span>
                        <span class="user-email">${userEmail}</span>
                    </div>
                </a>
                <button id="logoutBtn" class="btn-logout">Вийти</button>
            `;

            document.getElementById('logoutBtn').addEventListener('click', () => {
                localStorage.removeItem('jwt_token');
                localStorage.removeItem('user_email');
                window.location.reload();
            });
        }
    }

    function loadBooks() {
        fetch('/api/books')
            .then(res => {
                if (!res.ok) throw new Error('Помилка сервера');
                return res.json();
            })
            .then(data => {
                allBooks = data.content || [];
                updateCatalog();
            })
            .catch(err => {
                catalogDiv.innerHTML = `<p style="color:red; font-weight:bold;">Не вдалося завантажити дані сайту.</p>`;
                resultsCount.innerText = 'Помилка';
            });
    }

    function loadGenres() {
        const genreList = document.getElementById('genreFilter');
        fetch('/api/genres')
            .then(res => res.json())
            .then(genres => {
                genres.forEach(genre => {
                    const li = document.createElement('li');
                    li.innerText = genre.name;
                    li.setAttribute('data-genre', genre.name);
                    li.addEventListener('click', () => {
                        document.querySelectorAll('#genreFilter li').forEach(el => el.classList.remove('active'));
                        li.classList.add('active');
                        currentGenre = genre.name;
                        updateCatalog();
                    });
                    genreList.appendChild(li);
                });
            });
    }

    function updateCatalog() {
        const searchWord = searchInput.value.toLowerCase();

        const filtered = allBooks.filter(book => {
            const matchesSearch = book.name.toLowerCase().includes(searchWord) || book.author.toLowerCase().includes(searchWord);
            const matchesGenre = (currentGenre === 'all' || book.genreName === currentGenre);
            const matchesLang = (currentLanguage === 'all' || book.language === currentLanguage);
            return matchesSearch && matchesGenre && matchesLang;
        });

        renderBooks(filtered);
    }

    function renderBooks(books) {
        catalogDiv.innerHTML = '';
        resultsCount.innerText = `Знайдено книг: ${books.length}`;

        if (books.length === 0) {
            catalogDiv.innerHTML = `<div style="grid-column: 1/-1; text-align:center; padding: 3rem; color:var(--text-muted);">За вашим запитом нічого не знайдено 🔍</div>`;
            return;
        }

        books.forEach(book => {
            const card = document.createElement('div');
            card.className = 'book-card';

            const statusText = book.statusName ? book.statusName.toLowerCase() : '';
            const isAvailable = statusText === 'в наявності' || statusText === 'в наличии' || statusText === 'available';
            const statusClass = isAvailable ? 'status-available' : 'status-borrowed';

            card.innerHTML = `
                <div>
                    <h3>${book.name}</h3>
                    <div class="book-author">${book.author}</div>
                </div>
                <div class="book-meta">
                    <div class="meta-row">
                        <span class="meta-label">Жанр:</span>
                        <span class="meta-value">${book.genreName || '—'}</span>
                    </div>
                    <div class="meta-row">
                        <span class="meta-label">Мова:</span>
                        <span class="meta-value">${book.language || '—'}</span>
                    </div>
                    <div class="meta-row">
                        <span class="meta-label">Статус:</span>
                        <span class="status-badge ${statusClass}">${book.statusName || '—'}</span>
                    </div>
                    
                    <a href="book.html?id=${book.id}" class="btn-submit" style="display: block; text-align: center; text-decoration: none; margin-top: 15px; padding: 8px;">
                        Детальніше
                    </a>
                </div>
            `;
            catalogDiv.appendChild(card);
        });
    }

    searchBtn.addEventListener('click', updateCatalog);
    searchInput.addEventListener('input', updateCatalog);

    document.querySelectorAll('#languageFilter li').forEach(li => {
        li.addEventListener('click', () => {
            document.querySelectorAll('#languageFilter li').forEach(el => el.classList.remove('active'));
            li.classList.add('active');
            currentLanguage = li.getAttribute('data-lang');
            updateCatalog();
        });
    });

    document.querySelector('[data-genre="all"]').addEventListener('click', function() {
        document.querySelectorAll('#genreFilter li').forEach(el => el.classList.remove('active'));
        this.classList.add('active');
        currentGenre = 'all';
        updateCatalog();
    });

    checkAuth();
    loadBooks();
    loadGenres();
});