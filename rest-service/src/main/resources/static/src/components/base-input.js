Vue.component('base-input', {
    props: ['value'],
    computed: {
        listeners: function () {
            return {
                // Pass all component listeners directly to input
                ...this.$listeners,
                // Override input listener to work with v-model
                input: event => this.$emit('input', event.target.value)
            }
        }
    },
    template: `
<nav>
    <div class="nav-wrapper">
        <div class="input-field">
            <input id="search" type="search" class="input-field" :value="value" v-on="listeners" required>
            <label class="label-icon" for="search"><i class="material-icons">search</i></label>
            <i class="material-icons">close</i>
        </div>
    </div>
</nav>
`
});