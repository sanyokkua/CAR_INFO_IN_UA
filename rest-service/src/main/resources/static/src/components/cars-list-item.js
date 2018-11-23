Vue.component('cars-list-item', {
    props: ['registration'],
    template: `
<div class="container">
    <ul v-if="registration" class="collapsible hoverable" >
        <li>
            <div class="collapsible-header">
                {{ registration.auto.carBrand }} {{ registration.auto.carModel }} {{ registration.registration.newRegistrationNumber }}
                <br/>
                {{ registration.registration.operationName }}
                <span class="new badge blue" data-badge-caption="">{{ registration.registration.registrationDate }}</span>
            </div>
            <div class="collapsible-body">
                <ul class="collapsible hoverable">
                    <li>
                        <div class="collapsible-header">
                            Автомобиль
                        </div>
                        <div class="collapsible-body container">
                            <table class="centered highlight blue hoverable">
                                <tr >
                                    <td>Бренд</td>
                                    <td>{{ registration.auto.carBrand }}</td>
                                </tr>
                                <tr >
                                    <td>Модель</td>
                                    <td>{{ registration.auto.carModel }}</td>
                                </tr>
                                <tr >
                                    <td>Год выпуска</td>
                                    <td>{{ registration.auto.carMakeYear }}</td>
                                </tr>
                                <tr >
                                    <td>Цвет</td>
                                    <td>{{ registration.auto.carColor }}</td>
                                </tr>
                                <tr >
                                    <td>Вид</td>
                                    <td>{{ registration.auto.carKind }}</td>
                                </tr>
                                <tr >
                                    <td>Цель</td>
                                    <td>{{ registration.auto.carPurpose }}</td>
                                </tr>
                                <tr >
                                    <td>Топливо</td>
                                    <td>{{ registration.auto.carFuel }}</td>
                                </tr>
                                <tr >
                                    <td>Обьем двигателя</td>
                                    <td>{{ registration.auto.carEngineCapacity }}</td>
                                </tr>
                                <tr >
                                    <td>Собственный вес</td>
                                    <td>{{ registration.auto.carOwnWeight }}</td>
                                </tr>
                                <tr >
                                    <td>Максимальный вес</td>
                                    <td>{{ registration.auto.carTotalWeight }}</td>
                                </tr>
                            </table>
                        </div>
                    </li>
                    <li>
                        <div class="collapsible-header">
                            Регистрация
                        </div>
                        <div class="collapsible-body container">
                            <table class="centered highlight lime hoverable">
                                <tr >
                                    <td>Персона</td>
                                    <td>{{ registration.registration.person }}</td>
                                </tr>
                                <tr >
                                    <td>Административный центр</td>
                                    <td>{{ registration.registration.administrativeObjectName }}</td>
                                </tr>
                                <tr >
                                    <td>Операция</td>
                                    <td>{{ registration.registration.operationName }}</td>
                                </tr>
                                <tr >
                                    <td>Дата регистрации</td>
                                    <td>{{ registration.registration.registrationDate }}</td>
                                </tr>
                                <tr >
                                    <td>Автомобильный номер</td>
                                    <td>{{ registration.registration.newRegistrationNumber }}</td>
                                </tr>
                                <tr >
                                    <td>Регион</td>
                                    <td>{{ registration.registration.registrationNumberRegionName.region }}</td>
                                </tr>
                            </table>
                        </div>
                    </li>
                    <li>
                        <div class="collapsible-header">
                            Сервисный центр
                        </div>
                        <div class="collapsible-body container">
                            <table class="centered highlight pink hoverable">
                                <tr >
                                    <td>Название</td>
                                    <td>{{ registration.serviceCenter.name }}</td>
                                </tr>
                                <tr >
                                    <td>Адрес</td>
                                    <td>{{ registration.serviceCenter.address }}</td>
                                </tr>
                                <tr >
                                    <td>Почта</td>
                                    <td>{{ registration.serviceCenter.email }}</td>
                                </tr>
                            </table>
                        </div>
                    </li>
                </ul>
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
</div>
`
});