Vue.component('cars-list', {
    data: function () {
        return {
            searchText: '',
            registrationsList: [],
            isNeedToShowSpinner: false,
            isEmptyResponse: false
        }
    },
    methods: {
        findByNumber: function () {
            const trimmedText = this.searchText.trim();
            if (trimmedText) {
                let vm = this;
                vm.registrationsList = [];
                vm.isNeedToShowSpinner = true;
                axios.get('/registrations/' + trimmedText)
                    .then(function (response) {
                        console.log("Data is found: " + response);
                        vm.isNeedToShowSpinner = false;
                        vm.registrationsList = response.data;
                        vm.searchText = '';
                        vm.isEmptyResponse = vm.registrationsList.length <= 0;
                    })
                    .catch(function (error) {
                        vm.registrationsList = [];
                        vm.isNeedToShowSpinner = false;
                        vm.isEmptyResponse = true;
                        console.log(error);
                    })
            } else {
                this.registrationsList = [];
                this.isNeedToShowSpinner = false;
                this.isEmptyResponse = true;
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
    <div v-if="registrationsList.length > 0">
        <ul >
            <cars-list-item v-for="reg in registrationsList" v-bind:registration="reg" />
        </ul>
    </div>
    <div v-if="isEmptyResponse">
        <p>Nothing was found</p>
    </div>
    <div v-if="isNeedToShowSpinner" class="preloader-wrapper big active">
        <div class="spinner-layer spinner-blue-only">
            <div class="circle-clipper left">
                <div class="circle"></div>
            </div>
            <div class="gap-patch">
                <div class="circle"></div>
            </div>
            <div class="circle-clipper right">
                <div class="circle"></div>
            </div>
        </div>
    </div>
</div>
        `
});