Vue.component('cars-list', {
    data: function () {
        return {
            searchText: '',
            registrationsList: []
        }
    },
    methods: {
        findByNumber: function () {
            const trimmedText = this.searchText.trim();
            if (trimmedText) {
                let vm = this;
                axios.get('http://localhost:8090/registrations/'+trimmedText,  { crossdomain: true })
                    .then(function (response) {
                        console.log("Data is found: " + response);
                        vm.registrationsList = response.data;
                    })
                    .catch(function (error) {
                        vm.registrationsList = [];
                        console.log(error);
                    })
            } else {
                this.registrationsList = [];
            }
        }
    },
    updated: function () {
        this.$nextTick(function () {
            // Code that will run only after the
            // entire view has been re-rendered
            M.AutoInit();
        })
    },
    template: `  
        <div class="center-align">
            <base-input v-model="searchText" placeholder="AX0000EX" @keydown.enter="findByNumber" />
            <ul v-if="registrationsList.length">
                <cars-list-item v-for="reg in registrationsList" v-bind:registration="reg" />
            </ul>
            <p v-else>
                Nothing was found
            </p>
        </div>
        `
});