INSERT INTO patient (id_patient, name, surname, birthyear) VALUES (1000, 'John', 'Doe', 1950);
INSERT INTO patient (id_patient, name, surname, birthyear) VALUES (2000, 'Alice', 'Smith', 1985);
INSERT INTO patient (id_patient, name, surname, birthyear) VALUES (3000, 'Bob', 'Johnson', 1995);

INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (1, 'Aspirin', 100, 1);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (2, 'Paracetamol', 80, 2);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (3, 'Ibuprofen', 120, 1);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (4, 'Amoxicillin', 150, 2);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (5, 'Loratadine', 50, 13);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (6, 'Metformin', 200, 2);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (7, 'Ciprofloxacin', 180,4);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (8, 'Simvastatin', 90, 7);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (9, 'Omeprazole', 110, 2);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (10, 'Losartan', 140, 1);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (11, 'Prednisone', 160, 3);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (12, 'Furosemide', 70, 2);
INSERT INTO medicine (id_medicine, name, price, daily_dosage) VALUES (13, 'Hydrochlorothiazide', 85, 5);

INSERT INTO prescription (id_prescription, date, price, discount, prescribed_patient) VALUES (1000, '2024-01-15', 20, false, 1000);
INSERT INTO prescription (id_prescription, date, price, discount, prescribed_patient) VALUES (2000, '2024-01-16', 25, true, 2000);
INSERT INTO prescription (id_prescription, date, price, discount, prescribed_patient) VALUES (3000, '2024-01-17', 15, false, 3000);

INSERT INTO medicine_prescription (id_medicine, id_prescription) VALUES (1000, 1);
INSERT INTO medicine_prescription (id_medicine, id_prescription) VALUES (2000, 2);
INSERT INTO medicine_prescription (id_medicine, id_prescription) VALUES (3000, 3);