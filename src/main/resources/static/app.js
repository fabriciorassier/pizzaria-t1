const API = '';

function getToken() {
    return localStorage.getItem('token');
}

function authHeaders(json = true) {
    const headers = {
        Authorization: 'Bearer ' + getToken()
    };
    if (json) {
        headers['Content-Type'] = 'application/json';
    }
    return headers;
}

function showMessage(id, text, type = 'info') {
    const el = document.getElementById(id);
    if (!el) return;
    el.className = 'alert alert-' + type;
    el.textContent = text;
    el.classList.remove('d-none');
}

function ensureAuth() {
    if (!getToken()) {
        window.location.href = '/index.html';
    }
}
