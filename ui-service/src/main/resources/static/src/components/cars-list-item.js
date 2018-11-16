Vue.component('cars-list-item', {
    props: ['registration'],
    template: `
<ul v-if="registration" class="collapsible hoverable" >
    <li>
        <div class="collapsible-header">
            {{ registration.auto.carBrand }} {{ registration.auto.carModel }} - {{ registration.registration.newRegistrationNumber }}
            <span class="new badge">{{ registration.registration.registrationDate }}</span></div>
        <div class="collapsible-body">
            <div class="card blue hoverable">
                <div class="card-content"></div>
                <div class="card-tabs">
                    <ul class="tabs tabs-fixed-width">
                        <li class="tab"><a class="active" v-bind:href="'#tab-auto-'+ registration.registration.registrationDate">Автомобиль</a></li>
                        <li class="tab"><a v-bind:href="'#tab-registration-' + registration.registration.registrationDate">Регистрация</a></li>
                        <li class="tab"><a v-bind:href="'#tab-service-center-' + registration.registration.registrationDate">Сервисный центр</a></li>
                    </ul>
                </div>
                <div class="card-content grey lighten-4">
                    <div v-bind:id="'tab-auto-' + registration.registration.registrationDate">
                        <table class="responsive-table">
                            <tr class="hoverable">
                                <td>Бренд</td>
                                <td>{{ registration.auto.carBrand }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Модель</td>
                                <td>{{ registration.auto.carModel }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Год выпуска</td>
                                <td>{{ registration.auto.carMakeYear }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Цвет</td>
                                <td>{{ registration.auto.carColor }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Вид</td>
                                <td>{{ registration.auto.carKind }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Цель</td>
                                <td>{{ registration.auto.carPurpose }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Топливо</td>
                                <td>{{ registration.auto.carFuel }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Обьем двигателя</td>
                                <td>{{ registration.auto.carEngineCapacity }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Собственный вес</td>
                                <td>{{ registration.auto.carOwnWeight }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Максимальный вес</td>
                                <td>{{ registration.auto.carTotalWeight }}</td>
                            </tr>

                        </table>

                    </div>
                    <div v-bind:id="'tab-registration-' + registration.registration.registrationDate">

                        <table class="responsive-table">
                            <tr class="hoverable">
                                <td>Персона</td>
                                <td>{{ registration.registration.person }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Административный центр</td>
                                <td>{{ registration.registration.administrativeObjectName }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Операция</td>
                                <td>{{ registration.registration.operationName }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Дата регистрации</td>
                                <td>{{ registration.registration.registrationDate }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Автомобильный номер</td>
                                <td>{{ registration.registration.newRegistrationNumber }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Регион</td>
                                <td>{{ registration.registration.registrationNumberRegionName.region }}</td>
                            </tr>

                        </table>
                    </div>
                    <div v-bind:id="'tab-service-center-' + registration.registration.registrationDate">
                        <table class="responsive-table">
                            <tr class="hoverable">
                                <td>Название</td>
                                <td>{{ registration.serviceCenter.name }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Адрес</td>
                                <td>{{ registration.serviceCenter.address }}</td>
                            </tr>
                            <tr class="hoverable">
                                <td>Почта</td>
                                <td>{{ registration.serviceCenter.email }}</td>
                            </tr>

                        </table>
                    </div>
                </div>
            </div>
        </div>
    </li>
</ul>
<div v-else class="row">
    <div class="col s12 m5">
        <div class="card-panel teal">
            <span class="white-text">Error with parsing json
        </span>
        </div>
    </div>
</div>
`
});