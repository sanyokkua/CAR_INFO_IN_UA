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
    template: `<input type="text" class="input" :value="value" v-on="listeners">`
});