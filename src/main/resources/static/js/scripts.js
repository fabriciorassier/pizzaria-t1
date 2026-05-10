window.Utils = (() => {
    const toLocalISO = (date) => {
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, "0");
        const d = String(date.getDate()).padStart(2, "0");
        return y + "-" + m + "-" + d;
    };

    const fmtMoney = (v) => {
        const n = Number(v);
        if (!Number.isFinite(n)) return "R$ 0,00";
        return n.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
    };

    const fmtDate = (v) => {
        if (!v) return "-";
        try {
            const d = new Date(v);
            if (isNaN(d.getTime())) return String(v);
            return d.toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "short" });
        } catch {
            return String(v);
        }
    };

    const todayISO = () => toLocalISO(new Date());
    const weekAgoISO = () => {
        const d = new Date();
        d.setDate(d.getDate() - 7);
        return toLocalISO(d);
    };

    const isEmail = (s) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(String(s || "").trim());

    const onlyDigits = (s) => String(s || "").replace(/\D+/g, "");

    const formatCpf = (s) => {
        const d = onlyDigits(s).slice(0, 11);
        return d
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    };

    const formatPhoneBr = (s) => {
        const d = onlyDigits(s).slice(0, 11);
        if (d.length <= 10) {
            return d
                .replace(/(\d{2})(\d)/, "($1) $2")
                .replace(/(\d{4})(\d)/, "$1-$2");
        }
        return d
            .replace(/(\d{2})(\d)/, "($1) $2")
            .replace(/(\d{5})(\d)/, "$1-$2");
    };

    const isValidCpf = (cpf) => {
        const d = onlyDigits(cpf);
        if (d.length !== 11 || /^(\d)\1{10}$/.test(d)) return false;
        let sum = 0;
        for (let i = 0; i < 9; i++) sum += Number(d[i]) * (10 - i);
        let check = (sum * 10) % 11;
        if (check === 10) check = 0;
        if (check !== Number(d[9])) return false;
        sum = 0;
        for (let i = 0; i < 10; i++) sum += Number(d[i]) * (11 - i);
        check = (sum * 10) % 11;
        if (check === 10) check = 0;
        return check === Number(d[10]);
    };

    const isValidPhoneBr = (phone) => {
        const d = onlyDigits(phone);
        return d.length === 10 || d.length === 11;
    };

    const escapeHtml = (s) => String(s ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

    return {
        fmtMoney,
        fmtDate,
        todayISO,
        weekAgoISO,
        isEmail,
        escapeHtml,
        onlyDigits,
        formatCpf,
        formatPhoneBr,
        isValidCpf,
        isValidPhoneBr
    };
})();

window.UI = (() => {
    let host;
    const ensureHost = () => {
        if (host) return host;
        host = document.createElement("div");
        host.className = "toast-host";
        document.body.appendChild(host);
        return host;
    };

    const toast = (msg, type = "info", ttl = 3800) => {
        ensureHost();
        const el = document.createElement("div");
        el.className = "toast " + type;
        el.textContent = msg;
        host.appendChild(el);
        setTimeout(() => {
            el.style.opacity = "0";
            el.style.transform = "translateX(20px)";
            el.style.transition = "all .3s";
            setTimeout(() => el.remove(), 300);
        }, ttl);
    };

    const success = (m) => toast(m, "success");
    const error = (m) => toast(m, "error", 5000);
    const info = (m) => toast(m, "info");

    const confirm = (msg, confirmText = "Confirmar", cancelText = "Cancelar") => new Promise((resolve) => {
        ensureHost();
        const el = document.createElement("div");
        el.className = "toast info interactive";

        const text = document.createElement("div");
        text.className = "toast-text";
        text.textContent = msg;

        const actions = document.createElement("div");
        actions.className = "toast-actions";

        const btnNo = document.createElement("button");
        btnNo.className = "toast-btn";
        btnNo.type = "button";
        btnNo.textContent = cancelText;

        const btnYes = document.createElement("button");
        btnYes.className = "toast-btn primary";
        btnYes.type = "button";
        btnYes.textContent = confirmText;

        actions.appendChild(btnNo);
        actions.appendChild(btnYes);
        el.appendChild(text);
        el.appendChild(actions);
        host.appendChild(el);

        const close = (result) => {
            el.remove();
            resolve(result);
        };

        btnNo.onclick = () => close(false);
        btnYes.onclick = () => close(true);
    });

    let overlay;
    const ensureOverlay = () => {
        if (overlay) return overlay;
        overlay = document.createElement("div");
        overlay.className = "loading-overlay";
        overlay.innerHTML = '<div class="loader"></div>';
        document.body.appendChild(overlay);
        return overlay;
    };

    let count = 0;
    const showLoading = () => {
        ensureOverlay();
        count++;
        overlay.classList.add("show");
    };
    const hideLoading = () => {
        count = Math.max(0, count - 1);
        if (count === 0 && overlay) overlay.classList.remove("show");
    };

    const renderNavbar = () => {
        const nav = document.querySelector(".nav");
        if (!nav) return;
        const path = location.pathname.split("/").pop() || "index.html";
        const user = window.Auth?.getUser();
        const links = [
            { href: "index.html", label: "Inicio" },
            { href: "cardapio.html", label: "Cardapio", auth: true },
            { href: "status.html", label: "Status do Pedido", auth: true },
            { href: "entregues.html", label: "Entregues" },
            { href: "entregues-cliente.html", label: "Por Cliente" }
        ];

        const linksHtml = links
            .filter((l) => !l.auth || user)
            .map((l) => '<a href="' + l.href + '" class="' + (path === l.href ? "active" : "") + '">' + l.label + "</a>")
            .join("");

        const userName = Utils.escapeHtml(user?.nome || "cliente");
        const userHtml = user
            ? '<div class="nav-user">Ola, <strong>' + userName + '</strong></div><button class="btn btn-ghost btn-sm" id="logoutBtn">Sair</button>'
            : '<a href="login.html" class="btn btn-ghost btn-sm">Entrar</a><a href="registrar.html" class="btn btn-primary btn-sm">Criar conta</a>';

        nav.innerHTML =
            '<div class="nav-inner">' +
            '<a class="brand" href="index.html"><img class="brand-logo" src="img/logo.svg" alt="Logo"> Pizzaria Forno</a>' +
            '<button class="nav-toggle" id="navToggle">Menu</button>' +
            '<div class="nav-links" id="navLinks">' + linksHtml + '</div>' +
            userHtml +
            '</div>';

        const lo = nav.querySelector("#logoutBtn");
        if (lo) lo.onclick = () => {
            window.Auth.logout();
            smoothNavigate("index.html");
        };

        const tg = nav.querySelector("#navToggle");
        if (tg) tg.onclick = () => nav.querySelector("#navLinks").classList.toggle("open");
    };

    return { toast, success, error, info, confirm, showLoading, hideLoading, renderNavbar };
})();

window.Auth = (() => {
    const TOKEN_KEY = "tp_token";
    const USER_KEY = "tp_user";

    const getToken = () => localStorage.getItem(TOKEN_KEY);
    const getUser = () => {
        try {
            return JSON.parse(localStorage.getItem(USER_KEY) || "null");
        } catch {
            return null;
        }
    };

    const setSession = (token, user) => {
        localStorage.setItem(TOKEN_KEY, token);
        localStorage.setItem(USER_KEY, JSON.stringify(user || {}));
        window.Estado?.clearLastOrder?.();
    };

    const logout = (notify = true) => {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        window.Estado?.clearLastOrder?.();
        if (notify && window.UI) UI.info("Sessao encerrada.");
    };

    const isAuthenticated = () => !!getToken();

    const requireAuth = () => {
        if (!isAuthenticated()) {
            sessionStorage.setItem("tp_redirect", location.pathname.split("/").pop() || "");
            smoothNavigate("login.html", true);
            return false;
        }
        return true;
    };

    return { getToken, getUser, setSession, logout, isAuthenticated, requireAuth };
})();

window.Estado = (() => {
    const CART_KEY = "tp_cart";
    const LAST_KEY = "tp_last_order";

    const getCart = () => {
        try {
            return JSON.parse(localStorage.getItem(CART_KEY) || "{}");
        } catch {
            return {};
        }
    };

    const saveCart = (c) => localStorage.setItem(CART_KEY, JSON.stringify(c));
    const clearCart = () => localStorage.removeItem(CART_KEY);

    const setQty = (produto, quantidade) => {
        const c = getCart();
        const id = produto.id;
        if (!quantidade || quantidade < 1) delete c[id];
        else {
            c[id] = {
                quantidade: Number(quantidade),
                descricao: produto.descricao,
                preco: Number(produto.preco) || 0
            };
        }
        saveCart(c);
    };

    const itemsArray = () => {
        const c = getCart();
        return Object.entries(c).map(([id, v]) => ({ produtoId: Number(id), ...v }));
    };

    const totalItens = () => itemsArray().reduce((s, i) => s + i.quantidade, 0);
    const totalPreco = () => itemsArray().reduce((s, i) => s + i.quantidade * (i.preco || 0), 0);

    const setLastOrder = (o) => sessionStorage.setItem(LAST_KEY, JSON.stringify(o));
    const getLastOrder = () => {
        try {
            return JSON.parse(sessionStorage.getItem(LAST_KEY) || "null");
        } catch {
            return null;
        }
    };

    const clearLastOrder = () => sessionStorage.removeItem(LAST_KEY);

    return { getCart, setQty, clearCart, itemsArray, totalItens, totalPreco, setLastOrder, getLastOrder, clearLastOrder };
})();

window.API = (() => {
    const BASE_URL = "";

    const messageFor = (status) => ({
        400: "Dados invalidos. Verifique o formulario.",
        401: "Sessao invalida ou expirada. Faca login novamente.",
        403: "Voce nao tem permissao para esta acao.",
        404: "Recurso nao encontrado.",
        409: "Conflito ao processar a requisicao.",
        500: "Erro interno do servidor. Tente novamente em instantes."
    }[status] || ("Erro " + status + " ao processar a requisicao."));

    async function request(path, { method = "GET", body, auth = false, query } = {}) {
        const headers = { Accept: "application/json" };
        if (body !== undefined) headers["Content-Type"] = "application/json";
        if (auth) {
            const token = window.Auth?.getToken();
            if (!token) {
                const e = new Error("Voce precisa estar autenticado.");
                e.status = 401;
                throw e;
            }
            headers.Authorization = "Bearer " + token;
        }

        let url = BASE_URL + path;
        if (query) {
            const q = new URLSearchParams(Object.entries(query).filter(([, v]) => v !== undefined && v !== null && v !== "")).toString();
            if (q) url += (url.includes("?") ? "&" : "?") + q;
        }

        UI.showLoading();
        let res;
        try {
            res = await fetch(url, { method, headers, body: body !== undefined ? JSON.stringify(body) : undefined });
        } catch (err) {
            UI.hideLoading();
            const e = new Error("Nao foi possivel conectar a API. Verifique se o back-end esta em http://localhost:8080.");
            e.cause = err;
            throw e;
        }
        UI.hideLoading();

        let data = null;
        const text = await res.text();
        if (text) {
            try {
                data = JSON.parse(text);
            } catch {
                data = text;
            }
        }

        if (!res.ok) {
            const apiMsg = data && (data.message || data.mensagem || data.error || data.erro);
            const e = new Error(apiMsg || messageFor(res.status));
            e.status = res.status;
            e.data = data;
            if (res.status === 401 && auth) window.Auth?.logout(false);
            throw e;
        }
        return data;
    }

    return {
        BASE_URL,
        request,
        registrar: (payload) => request("/clientes/registrar", { method: "POST", body: payload }),
        login: (payload) => request("/clientes/login", { method: "POST", body: payload }),
        cardapio: () => request("/cardapio", { auth: true }),
        submeterPedido: (payload) => request("/pedidos/submeter", { method: "POST", body: payload, auth: true }),
        statusPedido: (id) => request("/pedidos/" + encodeURIComponent(id) + "/status", { auth: true }),
        cancelarPedido: (id) => request("/pedidos/" + encodeURIComponent(id), { method: "DELETE", auth: true }),
        pagarPedido: (id) => request("/pedidos/" + encodeURIComponent(id) + "/pagar", { method: "POST", auth: true }),
        entregues: (inicio, fim) => request("/pedidos/entregues", { query: { inicio, fim } }),
        entreguesCliente: (cpf, inicio, fim) => request("/pedidos/cliente/" + encodeURIComponent(cpf) + "/entregues", { query: { inicio, fim } })
    };
})();

function renderTabelaDinamica(el, data, preferred) {
    const lista = Array.isArray(data) ? data : (data?.pedidos || data?.itens || []);
    if (!lista.length) {
        el.innerHTML = '<div class="empty-state"><div class="ico">-</div><p>Nenhum registro encontrado.</p></div>';
        return;
    }

    const normalized = lista.map((row) => {
        const copy = { ...row };
        if (copy.pedidoId == null && copy.id != null) copy.pedidoId = copy.id;
        return copy;
    });

    const unique = [];
    const seen = new Set();
    for (const row of normalized) {
        const key = [row.pedidoId, row.clienteCpf, row.dataPedido, row.custoTotal, row.status].join("|");
        if (seen.has(key)) continue;
        seen.add(key);
        unique.push(row);
    }

    const headers = {
        pedidoId: "ID PEDIDO",
        dataPedido: "DATA DO PEDIDO",
        custoTotal: "VALOR TOTAL",
        status: "STATUS",
        clienteCpf: "CPF DO CLIENTE"
    };

    const all = Array.from(new Set(unique.flatMap((o) => Object.keys(o))));
    const cols = preferred
        .filter((k) => all.includes(k))
        .concat(all.filter((k) => !preferred.includes(k) && k !== "id" && typeof unique[0][k] !== "object"));
    el.innerHTML =
        '<div class="table-wrap"><table><thead><tr>' + cols.map((c) => '<th>' + Utils.escapeHtml(headers[c] || c) + '</th>').join("") +
        '</tr></thead><tbody>' +
        unique.map((row) => '<tr>' + cols.map((c) => '<td>' + formatCell(c, row[c]) + '</td>').join("") + '</tr>').join("") +
        '</tbody></table></div><p class="muted center" style="margin-top:12px">' + unique.length + ' registro(s) encontrado(s).</p>';
}

function formatCell(col, val) {
    if (val === null || val === undefined || val === "") return '<span class="muted">-</span>';
    if (typeof val === "object") return Utils.escapeHtml(JSON.stringify(val));
    if (/total|preco|valor|custo|desconto|imposto/i.test(col) && !isNaN(val)) return Utils.fmtMoney(val);
    if (/data/i.test(col)) return Utils.escapeHtml(Utils.fmtDate(val));
    return Utils.escapeHtml(String(val));
}

function smoothNavigate(url, replace = false) {
    if (!url) return;
    document.body.classList.add("page-leave");
    setTimeout(() => {
        if (replace) location.replace(url);
        else location.href = url;
    }, 160);
}

const PUBLIC_PAGES = new Set(["", "index.html", "login.html", "registrar.html", "cadastro.html"]);

function isPublicPage(path) {
    return PUBLIC_PAGES.has(path || "");
}

function guardAuthForCurrentPage() {
    const path = location.pathname.split("/").pop() || "";
    if (isPublicPage(path)) return true;
    if (window.Auth?.isAuthenticated()) return true;
    sessionStorage.setItem("tp_redirect", path);
    smoothNavigate("login.html", true);
    return false;
}

function initCommon() {
    UI.renderNavbar();
    const yr = document.getElementById("yr");
    if (yr) yr.textContent = new Date().getFullYear();

    requestAnimationFrame(() => document.body.classList.add("page-ready"));

    document.addEventListener("click", (e) => {
        const a = e.target.closest("a[href]");
        if (!a) return;
        const href = a.getAttribute("href");
        if (!href || href.startsWith("#") || href.startsWith("mailto:") || href.startsWith("tel:")) return;
        if (a.target === "_blank" || a.hasAttribute("download")) return;
        const isExternal = /^https?:\/\//i.test(href) && !href.includes(location.host);
        if (isExternal) return;
        if (!href.endsWith(".html")) return;

        const targetPage = href.split("?")[0];
        if (!window.Auth?.isAuthenticated() && !isPublicPage(targetPage)) {
            e.preventDefault();
            sessionStorage.setItem("tp_redirect", targetPage);
            smoothNavigate("login.html", true);
            return;
        }

        e.preventDefault();
        smoothNavigate(href);
    });
}

function initLoginPage() {
    const form = document.getElementById("loginForm");
    const btn = document.getElementById("submitBtn");
    if (!form || !btn) return;

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const email = document.getElementById("email").value.trim();
        const senha = document.getElementById("senha").value;
        if (!Utils.isEmail(email)) return UI.error("Informe um e-mail valido.");
        if (senha.length < 4) return UI.error("Senha deve ter ao menos 4 caracteres.");

        btn.disabled = true;
        try {
            const res = await API.login({ email, senha });
            if (!res || !res.token) throw new Error("Resposta invalida do servidor.");
            Auth.setSession(res.token, { cpf: res.cpf, nome: res.nome, email });
            UI.success(res.mensagem || "Login realizado com sucesso!");
            const back = sessionStorage.getItem("tp_redirect");
            sessionStorage.removeItem("tp_redirect");
            setTimeout(() => smoothNavigate(back || "cardapio.html"), 500);
        } catch (err) {
            UI.error(err.message || "Falha ao entrar.");
        } finally {
            btn.disabled = false;
        }
    });
}

function initRegisterPage() {
    const form = document.getElementById("registerForm");
    const btn = document.getElementById("submitBtn");
    if (!form || !btn) return;

    const cpfInput = document.getElementById("cpf");
    const celularInput = document.getElementById("celular");

    cpfInput.addEventListener("input", () => {
        cpfInput.value = Utils.formatCpf(cpfInput.value);
    });
    celularInput.addEventListener("input", () => {
        celularInput.value = Utils.formatPhoneBr(celularInput.value);
    });

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const data = {
            cpf: Utils.onlyDigits(cpfInput.value),
            nome: document.getElementById("nome").value.trim(),
            celular: Utils.onlyDigits(celularInput.value),
            endereco: document.getElementById("endereco").value.trim(),
            email: document.getElementById("email").value.trim(),
            senha: document.getElementById("senha").value
        };

        if (!data.nome) return UI.error("Informe seu nome.");
        if (!data.cpf) return UI.error("Informe seu CPF.");
        if (!Utils.isValidCpf(data.cpf)) return UI.error("CPF invalido. Informe um CPF real.");
        if (!data.celular) return UI.error("Informe seu celular.");
        if (!Utils.isValidPhoneBr(data.celular)) return UI.error("Celular invalido. Use DDD + numero.");
        if (!Utils.isEmail(data.email)) return UI.error("Informe um e-mail valido.");
        if (!data.endereco) return UI.error("Informe seu endereco.");
        if (data.senha.length < 4) return UI.error("Senha deve ter ao menos 4 caracteres.");

        btn.disabled = true;
        try {
            const res = await API.registrar(data);
            UI.success((res && (res.mensagem || res.message)) || "Cadastro realizado!");
            setTimeout(() => smoothNavigate("login.html"), 800);
        } catch (err) {
            UI.error(err.message || "Falha ao cadastrar.");
        } finally {
            btn.disabled = false;
        }
    });
}

function initCardapioPage() {
    if (!document.getElementById("menuList")) return;
    if (!Auth.requireAuth()) return;

    const menuList = document.getElementById("menuList");
    const cartList = document.getElementById("cartList");
    const cartCount = document.getElementById("cartCount");
    const cartTotal = document.getElementById("cartTotal");
    const enderecoInp = document.getElementById("endereco");
    const submitBtn = document.getElementById("submitOrderBtn");

    let produtos = [];

    function renderMenu() {
        if (!produtos.length) {
            menuList.innerHTML = '<div class="empty-state"><div class="ico">-</div><p>Nenhum item disponivel no momento.</p></div>';
            return;
        }
        const cart = Estado.getCart();
        menuList.innerHTML = produtos.map((p) => {
            const qty = cart[p.id]?.quantidade || 0;
            const indisponivel = p.disponivel === false;
            return '<article class="menu-item ' + (indisponivel ? 'unavailable' : '') + '" data-id="' + p.id + '">' +
                '<div class="id">#' + Utils.escapeHtml(p.id) + '</div>' +
                '<h3>' + Utils.escapeHtml(p.descricao || "Item") + '</h3>' +
                '<div class="price">' + Utils.fmtMoney(p.preco) + '</div>' +
                '<div class="qty"><button data-act="dec" aria-label="Diminuir" ' + (indisponivel ? 'disabled' : '') + '>-</button>' +
                '<input type="number" min="0" value="' + qty + '" data-act="qty" ' + (indisponivel ? 'disabled' : '') + '>' +
                '<button data-act="inc" aria-label="Aumentar" ' + (indisponivel ? 'disabled' : '') + '>+</button></div>' +
                (indisponivel ? '<div class="menu-unavailable">Indisponivel por falta de ingredientes no estoque.</div>' : '') +
                '</article>';
        }).join("");

        menuList.querySelectorAll(".menu-item").forEach((card) => {
            const id = card.dataset.id;
            const prod = produtos.find((p) => String(p.id) === String(id));
            if (!prod || prod.disponivel === false) {
                const input = card.querySelector('input[data-act="qty"]');
                if (input) input.value = 0;
                return;
            }
            const inp = card.querySelector('input[data-act="qty"]');
            card.querySelector('[data-act="inc"]').onclick = () => {
                inp.value = (Number(inp.value) || 0) + 1;
                Estado.setQty(prod, Number(inp.value));
                renderCart();
            };
            card.querySelector('[data-act="dec"]').onclick = () => {
                inp.value = Math.max(0, (Number(inp.value) || 0) - 1);
                Estado.setQty(prod, Number(inp.value));
                renderCart();
            };
            inp.onchange = () => {
                const v = Math.max(0, Number(inp.value) || 0);
                inp.value = v;
                Estado.setQty(prod, v);
                renderCart();
            };
        });
    }

    function renderCart() {
        const items = Estado.itemsArray();
        if (!items.length) {
            cartList.innerHTML = '<div class="cart-empty">Seu carrinho esta vazio.<br>Escolha pizzas no cardapio.</div>';
        } else {
            cartList.innerHTML = items.map((i) => '<div class="cart-item"><div><div class="name">' + Utils.escapeHtml(i.descricao || ("Item " + i.produtoId)) + '</div><div class="meta">' + i.quantidade + ' x ' + Utils.fmtMoney(i.preco) + '</div></div><div><strong>' + Utils.fmtMoney(i.quantidade * (i.preco || 0)) + '</strong></div></div>').join("");
        }
        cartCount.textContent = Estado.totalItens();
        cartTotal.textContent = Utils.fmtMoney(Estado.totalPreco());
    }

    async function carregar() {
        try {
            const data = await API.cardapio();
            produtos = (Array.isArray(data) ? data : (data?.itens || data?.cardapio || [])).map((p) => ({
                id: p.id ?? p.produtoId ?? p.codigo,
                descricao: p.descricao ?? p.nome ?? p.titulo ?? "Item",
                preco: Number(p.preco ?? p.valor ?? 0),
                disponivel: p.disponivel !== false
            })).filter((p) => p.id !== undefined);
            const cart = Estado.getCart();
            let changed = false;
            produtos.forEach((p) => {
                if (p.disponivel !== false) return;
                if (cart[p.id]) {
                    delete cart[p.id];
                    changed = true;
                }
            });
            if (changed) {
                localStorage.setItem("tp_cart", JSON.stringify(cart));
                UI.info("Itens indisponiveis foram removidos do carrinho automaticamente.");
            }
            renderMenu();
            renderCart();
        } catch (err) {
            UI.error(err.message);
            menuList.innerHTML = '<div class="empty-state"><div class="ico">!</div><p>' + Utils.escapeHtml(err.message) + '</p></div>';
            if (err.status === 401) setTimeout(() => smoothNavigate("login.html"), 1200);
        }
    }

    submitBtn.addEventListener("click", async () => {
        const itens = Estado.itemsArray().map((i) => ({ produtoId: i.produtoId, quantidade: i.quantidade }));
        const enderecoEntrega = enderecoInp.value.trim();
        if (!itens.length) return UI.error("Adicione ao menos 1 item ao pedido.");
        if (!enderecoEntrega) return UI.error("Informe o endereco de entrega.");

        const ok = await UI.confirm("Deseja realizar este pedido agora?", "Realizar pedido", "Revisar");
        if (!ok) {
            UI.info("Pedido nao enviado.");
            return;
        }

        submitBtn.disabled = true;
        try {
            const res = await API.submeterPedido({ enderecoEntrega, itens });
            Estado.setLastOrder(res);
            Estado.clearCart();
            const id = res?.pedidoId ?? res?.id;
            UI.success((res?.mensagem || "Pedido enviado para aprovacao!") + (id ? (" Numero do pedido: #" + id) : ""));
            setTimeout(() => smoothNavigate("status.html" + (id ? ("?id=" + encodeURIComponent(id)) : "")), 700);
        } catch (err) {
            const detalhes = Array.isArray(err?.data?.detalhes) ? err.data.detalhes : [];
            if (detalhes.length) {
                UI.error((err.message || "Estoque insuficiente.") + " Itens: " + detalhes.join(" | "));
            } else {
                UI.error(err.message);
            }
            if (err.status === 401) setTimeout(() => smoothNavigate("login.html"), 1200);
        } finally {
            submitBtn.disabled = false;
        }
    });

    carregar();
}

function initStatusPage() {
    if (!document.getElementById("pedidoSection")) return;
    if (!Auth.requireAuth()) return;

    const idInp = document.getElementById("pedidoId");
    const buscarBtn = document.getElementById("buscarBtn");
    const sec = document.getElementById("pedidoSection");
    const poId = document.getElementById("poId");
    const poStatus = document.getElementById("poStatus");
    const poMsg = document.getElementById("poMsg");
    const poCustoItens = document.getElementById("poCustoItens");
    const poDesconto = document.getElementById("poDesconto");
    const poImposto = document.getElementById("poImposto");
    const poTotal = document.getElementById("poTotal");
    const poExtra = document.getElementById("poExtra");
    const pagarBtn = document.getElementById("pagarBtn");
    const cancelarBtn = document.getElementById("cancelarBtn");
    const atualizarBtn = document.getElementById("atualizarBtn");
    const lastOrderId = document.getElementById("lastOrderId");

    let pedidoAtual = null;

    const params = new URLSearchParams(location.search);
    const last = Estado.getLastOrder();
    const initialId = params.get("id") || last?.pedidoId || last?.id;
    if (initialId) {
        idInp.value = initialId;
        buscar();
    } else if (last) {
        renderPedido(last);
    }

    if (lastOrderId) {
        const shownId = initialId || last?.pedidoId || last?.id;
        lastOrderId.textContent = shownId ? ("Ultimo pedido criado: #" + shownId) : "";
    }

    buscarBtn.onclick = buscar;
    idInp.addEventListener("keydown", (e) => {
        if (e.key === "Enter") buscar();
    });

    pagarBtn.onclick = async () => {
        const id = pedidoAtual?.pedidoId ?? pedidoAtual?.id ?? idInp.value.trim();
        if (!id) return UI.error("Nenhum pedido carregado.");
        const ok = await UI.confirm("Confirmar pagamento do pedido #" + id + "?", "Pagar", "Nao pagar");
        if (!ok) {
            UI.info("Pagamento cancelado.");
            return;
        }
        try {
            const res = await API.pagarPedido(id);
            UI.success(res?.mensagem || "Pagamento processado!");
            renderPedido({ ...pedidoAtual, ...res, pedidoId: id });
            setTimeout(buscar, 500);
        } catch (err) {
            UI.error(err.message);
        }
    };

    cancelarBtn.onclick = async () => {
        const id = pedidoAtual?.pedidoId ?? pedidoAtual?.id ?? idInp.value.trim();
        if (!id) return UI.error("Nenhum pedido carregado.");
        const ok = await UI.confirm("Deseja cancelar o pedido #" + id + "?", "Cancelar pedido", "Manter pedido");
        if (!ok) {
            UI.info("Cancelamento abortado.");
            return;
        }
        try {
            const res = await API.cancelarPedido(id);
            UI.success(res?.mensagem || "Pedido cancelado.");
            setTimeout(buscar, 400);
        } catch (err) {
            UI.error(err.message);
        }
    };

    atualizarBtn.onclick = async () => {
        const ok = await UI.confirm("Atualizar o status agora?", "Atualizar", "Voltar");
        if (!ok) {
            UI.info("Atualizacao cancelada.");
            return;
        }
        UI.info("Atualizando status do pedido...");
        buscar();
    };

    async function buscar() {
        const id = idInp.value.trim();
        if (!id) return UI.error("Informe o ID do pedido.");
        try {
            const res = await API.statusPedido(id);
            renderPedido({ pedidoId: id, ...res });
        } catch (err) {
            UI.error(err.message);
            if (err.status === 404) {
                sec.classList.add("hidden-section");
                sec.style.display = "none";
            }
        }
    }

    function renderPedido(p) {
        pedidoAtual = p;
        sec.classList.remove("hidden-section");
        sec.style.display = "";
        poId.textContent = "#" + (p.pedidoId ?? p.id ?? "-");
        poMsg.textContent = p.mensagem || "";
        const status = (p.status || "-").toString();
        poStatus.textContent = status;
        poStatus.className = "status-badge " + status.toLowerCase().replace(/\s+/g, "-");
        poCustoItens.textContent = p.custoItens != null ? Utils.fmtMoney(p.custoItens) : "-";
        poDesconto.textContent = p.desconto != null ? Utils.fmtMoney(p.desconto) : "-";
        poImposto.textContent = p.imposto != null ? Utils.fmtMoney(p.imposto) : "-";
        poTotal.textContent = p.custoTotal != null ? Utils.fmtMoney(p.custoTotal) : "-";

        const itens = p.itens || p.produtos || [];
        let extra = "";
        if (Array.isArray(itens) && itens.length) {
            extra += '<div class="divider"></div><h3 style="margin-bottom:10px;font-size:1rem">Itens</h3><div class="table-wrap"><table><thead><tr><th>Produto</th><th>Qtd</th><th>Preco</th><th>Subtotal</th></tr></thead><tbody>' +
                itens.map((i) => {
                    const desc = i.descricao || i.nome || ("#" + (i.produtoId ?? i.id ?? "?"));
                    const q = i.quantidade ?? 1;
                    const pr = i.preco ?? i.valor ?? 0;
                    return '<tr><td>' + Utils.escapeHtml(desc) + '</td><td>' + q + '</td><td>' + Utils.fmtMoney(pr) + '</td><td>' + Utils.fmtMoney(q * pr) + '</td></tr>';
                }).join("") +
                '</tbody></table></div>';
        }
        poExtra.innerHTML = extra;
    }
}

function initEntreguesPage() {
    const inicio = document.getElementById("inicio");
    const fim = document.getElementById("fim");
    const btn = document.getElementById("buscarBtn");
    const out = document.getElementById("resultado");
    if (!inicio || !fim || !btn || !out) return;

    const hoje = Utils.todayISO();
    document.documentElement.lang = "pt-BR";
    inicio.setAttribute("lang", "pt-BR");
    fim.setAttribute("lang", "pt-BR");
    inicio.setAttribute("title", "Data inicial (pt-BR)");
    fim.setAttribute("title", "Data final (pt-BR)");
    configureDateInput(inicio, hoje);
    configureDateInput(fim, hoje);
    out.innerHTML = '<div class="empty-state"><div class="ico">-</div><p>Selecione um intervalo e clique em Buscar.</p></div>';

    btn.onclick = async () => {
        if (!inicio.value || !fim.value) return UI.error("Informe data inicial e final.");
        if (inicio.value > fim.value) return UI.error("Data inicial deve ser anterior a final.");
        try {
            UI.info("Buscando pedidos entregues...");
            const data = await API.entregues(inicio.value, fim.value);
            renderTabelaDinamica(out, data, ["pedidoId", "dataPedido", "custoTotal", "status", "clienteCpf"]);
        } catch (err) {
            UI.error(err.message);
            out.innerHTML = '<div class="empty-state"><div class="ico">!</div><p>' + Utils.escapeHtml(err.message) + '</p></div>';
        }
    };
}

function initEntreguesClientePage() {
    const cpf = document.getElementById("cpf");
    const inicio = document.getElementById("inicio");
    const fim = document.getElementById("fim");
    const btn = document.getElementById("buscarBtn");
    const usarMeuCpfBtn = document.getElementById("usarMeuCpfBtn");
    const out = document.getElementById("resultado");
    if (!cpf || !inicio || !fim || !btn || !out) return;

    const hoje = Utils.todayISO();
    document.documentElement.lang = "pt-BR";
    inicio.setAttribute("lang", "pt-BR");
    fim.setAttribute("lang", "pt-BR");
    inicio.setAttribute("title", "Data inicial (pt-BR)");
    fim.setAttribute("title", "Data final (pt-BR)");
    configureDateInput(inicio, hoje);
    configureDateInput(fim, hoje);
    const u = Auth.getUser();
    if (u?.cpf) cpf.value = u.cpf;
    out.innerHTML = '<div class="empty-state"><div class="ico">-</div><p>Informe CPF e periodo para buscar.</p></div>';

    if (usarMeuCpfBtn) {
        usarMeuCpfBtn.onclick = () => {
            const user = Auth.getUser();
            const value = Utils.onlyDigits(user?.cpf || "");
            if (!value) {
                UI.error("Nao foi possivel obter seu CPF da sessao.");
                return;
            }
            cpf.value = value;
            UI.success("CPF da sessao aplicado no filtro.");
        };
    }

    btn.onclick = async () => {
        if (!cpf.value.trim()) return UI.error("Informe o CPF.");
        if (!inicio.value || !fim.value) return UI.error("Informe data inicial e final.");
        if (inicio.value > fim.value) return UI.error("Data inicial deve ser anterior a final.");
        try {
            const cleanCpf = Utils.onlyDigits(cpf.value.trim());
            UI.info("Buscando pedidos entregues do cliente...");
            const data = await API.entreguesCliente(cleanCpf, inicio.value, fim.value);
            renderTabelaDinamica(out, data, ["pedidoId", "dataPedido", "custoTotal", "status", "clienteCpf"]);
        } catch (err) {
            UI.error(err.message);
            out.innerHTML = '<div class="empty-state"><div class="ico">!</div><p>' + Utils.escapeHtml(err.message) + '</p></div>';
        }
    };
}

function configureDateInput(input, defaultIsoDate) {
    if (window.flatpickr) {
        if (window.flatpickr.l10ns && window.flatpickr.l10ns.pt) {
            window.flatpickr.localize(window.flatpickr.l10ns.pt);
        }
        window.flatpickr(input, {
            locale: "pt",
            dateFormat: "Y-m-d",
            altInput: true,
            altInputClass: "input input-date",
            altFormat: "d/m/Y",
            defaultDate: defaultIsoDate,
            allowInput: false,
            disableMobile: true
        });
        return;
    }

    input.type = "date";
    input.value = defaultIsoDate;
}

document.addEventListener("DOMContentLoaded", () => {
    initCommon();
    if (!guardAuthForCurrentPage()) return;
    initLoginPage();
    initRegisterPage();
    initCardapioPage();
    initStatusPage();
    initEntreguesPage();
    initEntreguesClientePage();
});

