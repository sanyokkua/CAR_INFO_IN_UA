package ua.kostenko.carinfo.importing.csv.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRecord {
    private String PERSON;  //PERSON                            person
    private Long REG_ADDR_KOATUU;   //REG_ADDR_KOATUU                           reg_addr_koatuu
    private Long OPER_CODE; //OPER_CODE                         oper_code
    private String OPER_NAME;   //OPER_NAME                         oper_name
    private String D_REG;   //D_REG                         d_reg
    private Long DEP_CODE;  //DEP_CODE                          dep_code
    private String DEP; //DEP                           dep
    private String BRAND;   //BRAND                         brand
    private String MODEL;   //MODEL                         model
    private Long MAKE_YEAR; //MAKE_YEAR                         make_year
    private String COLOR;   //COLOR                         color
    private String KIND;    //KIND                          kind
    private String BODY;    //BODY                          body
    private String PURPOSE; //PURPOSE                           purpose
    private String FUEL;    //FUEL                          fuel
    private Long CAPACITY;  //CAPACITY                          capacity
    private Long OWN_WEIGHT;    //OWN_WEIGHT                            own_weight
    private Long TOTAL_WEIGHT;  //TOTAL_WEIGHT                          total_weight
    private String N_REG_NEW;   //N_REG_NEW                         n_reg_new
}

/*
person
reg_addr_koatuu
oper_code
oper_name
d_reg
dep_code
dep
brand
model
make_year
color
kind
body
purpose
fuel
capacity
own_weight
total_weight
n_reg_new


PERSON = J
REG_ADDR_KOATUU = 3210600000
OPER_CODE = 315
OPER_NAME = Перереєстрація ТЗ на нов. власн. по договору укладеному в ТСЦ
D_REG = 08.02.2019
DEP_CODE = 12293
DEP = ТСЦ 8044
BRAND = VOLKSWAGEN
MODEL = JETTA
MAKE_YEAR = 2008
COLOR = СІРИЙ
KIND = ЛЕГКОВИЙ
BODY = СЕДАН-B
PURPOSE = ЗАГАЛЬНИЙ
FUEL = БЕНЗИН
CAPACITY = 1595
OWN_WEIGHT = 1275
TOTAL_WEIGHT = 1680
N_REG_NEW = АІ0843НР
* */