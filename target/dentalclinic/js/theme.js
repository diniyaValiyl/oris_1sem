class ThemeManager {
    constructor() {
        this.theme = this.getSavedTheme();
        this.init();
    }

    init() {
        this.applyTheme(this.theme);
        this.setupThemeToggle();
        this.setupSystemThemeListener();
    }

    getSavedTheme() {
        return localStorage.getItem('clinic-theme') || 'light';
    }

    applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('clinic-theme', theme);
        this.updateThemeButton(theme);
    }

    updateThemeButton(theme) {
        const toggleBtn = document.getElementById('theme-toggle');
        if (toggleBtn) {
            toggleBtn.textContent = theme === 'light' ? 'Ночь' : 'День';
            toggleBtn.setAttribute('aria-label',
                theme === 'light' ? 'Включить темную тему' : 'Включить светлую тему');
        }
    }

    setupThemeToggle() {
        const toggleBtn = document.getElementById('theme-toggle');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', () => this.toggleTheme());
        }
    }

    setupSystemThemeListener() {
        // Автоматическое переключение темы в зависимости от системных настроек
        if (window.matchMedia) {
            const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
            mediaQuery.addEventListener('change', (e) => {
                if (!localStorage.getItem('clinic-theme')) {
                    this.applyTheme(e.matches ? 'dark' : 'light');
                }
            });
        }
    }

    toggleTheme() {
        this.theme = this.theme === 'light' ? 'dark' : 'light';
        this.applyTheme(this.theme);

        // Анимация переключения
        this.animateThemeTransition();
    }

    animateThemeTransition() {
        document.documentElement.style.transition = 'all 0.5s ease';

        setTimeout(() => {
            document.documentElement.style.transition = '';
        }, 500);
    }

    // Для принудительной установки темы
    setTheme(theme) {
        if (['light', 'dark'].includes(theme)) {
            this.applyTheme(theme);
        }
    }

    // Получение текущей темы
    getCurrentTheme() {
        return this.theme;
    }
}

// Глобальный доступ к менеджеру тем
window.themeManager = new ThemeManager();