CREATE SCHEMA IF NOT EXISTS carinfo;

CREATE OR REPLACE VIEW carinfo.vehicle_view AS
SELECT vehicle_id, b.brand_name, m.model_name
FROM carinfo.vehicle v
         LEFT JOIN carinfo.model m ON m.model_id = v.model_id
         LEFT JOIN carinfo.brand b ON b.brand_id = v.brand_id;

CREATE OR REPLACE VIEW carinfo.record_view AS
SELECT id,
       registration_date,
       registration_number,
       brand_name,
       model_name,
       engine_capacity,
       fuel_type_name,
       make_year,
       own_weight,
       total_weight,
       color_name,
       body_type_name,
       kind_name,
       purpose_name,
       admin_obj_name,
       admin_obj_type,
       r.op_code,
       op_name,
       r.dep_code,
       dep_addr,
       dep_email,
       person_type
FROM carinfo.record r
         LEFT JOIN carinfo.admin_object ao ON ao.admin_obj_id = r.admin_obj_id
         LEFT JOIN carinfo.operation o ON o.op_code = r.op_code
         LEFT JOIN carinfo.department d ON d.dep_code = r.dep_code
         LEFT JOIN carinfo.kind k ON k.kind_id = r.kind_id
         LEFT JOIN carinfo.vehicle v ON v.vehicle_id = r.vehicle_id
         LEFT JOIN carinfo.color c ON c.color_id = r.color_id
         LEFT JOIN carinfo.body_type bt ON bt.body_type_id = r.body_type_id
         LEFT JOIN carinfo.purpose p ON p.purpose_id = r.purpose_id
         LEFT JOIN carinfo.fuel_type ft ON ft.fuel_type_id = r.fuel_type_id
         LEFT JOIN carinfo.brand b ON b.brand_id = v.brand_id
         LEFT JOIN carinfo.model m ON m.model_id = v.model_id;