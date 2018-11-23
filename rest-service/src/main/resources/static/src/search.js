new Vue({
    el: '#app',
    data: {},
    template: `
<div id="app">
    <nav>
        <div class="nav-wrapper">
            <a href="#" class="brand-logo">Регистрации авто в Украине!</a>
            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="search">Search</a></li>
                <li><a href="statistics">Statistics</a></li>
                <li><a href="login">Login</a></li>
            </ul>
        </div>
    </nav>
    <cars-list/>
</div>
        `
});