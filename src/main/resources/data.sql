------------------------------------------------------
-- Reservation:
------------------------------------------------------

INSERT INTO RESERVATION (SHIFT_ENUM, DATE) VALUES ('LUNCH', '2022-01-01');
INSERT INTO RESERVATION (SHIFT_ENUM, DATE) VALUES ('DINNER', '2022-04-17');

------------------------------------------------------
-- Customer:
------------------------------------------------------

INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER) VALUES ('Ugoberto','Volpis','3614989900');
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER) VALUES ('Giovanna','Ragozini','3156041798');
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER, RECOMMENDED_BY_ID) VALUES ('Amanto','Toffolon','3659775685', 1);
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER) VALUES ('Asiago','Caci','3136239998');
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER) VALUES ('Oriede','Guerrato','3961133337');
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER, RECOMMENDED_BY_ID) VALUES ('Marinetta','Catini','3491056102', 3);
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER) VALUES ('Contaldo','De Tomasi','3126783062');
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER) VALUES ('Agnesio','Peparaio','3406610365');
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER, RECOMMENDED_BY_ID) VALUES ('Fasma','Tommasetti','3503920765', 4);
INSERT INTO CUSTOMER (FIRST_NAME, LAST_NAME, MOBILE_NUMBER) VALUES ('Rinaldino','Ferrise','3382007212');

------------------------------------------------------
-- Employee:
------------------------------------------------------

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, CF, ROLE) VALUES ('Tilia','Teodosio','CCC111EEE222DDD3', 'Capo sala');
INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, CF, ROLE) VALUES ('Germanico','Cascino','CDC11XEEEA222DD3', 'Chef');
INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, CF, ROLE) VALUES ('Ameglia','Labieni','C2911332E22LADX3', 'Aiuto chef');
INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, CF, ROLE) VALUES ('Florianna','Trefoloni','CXC1142EE233DXX3', 'Cameriere');
INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, CF, ROLE) VALUES ('Dorinda','Clementino','C3411112E22LKDD3', 'Cameriere');

/*
------------------------------------------------------
-- Reservation_Customers:
------------------------------------------------------

INSERT INTO RESERVATION_CUSTOMERS (RESERVATION_ID, CUSTOMER_ID) VALUES (1, 2);
INSERT INTO RESERVATION_CUSTOMERS (RESERVATION_ID, CUSTOMER_ID) VALUES (1, 5);
INSERT INTO RESERVATION_CUSTOMERS (RESERVATION_ID, CUSTOMER_ID) VALUES (2, 6);
*/

------------------------------------------------------
-- Allergen:
------------------------------------------------------

INSERT INTO ALLERGEN (NAME) VALUES ('Latte');
INSERT INTO ALLERGEN (NAME) VALUES ('Glutine');
INSERT INTO ALLERGEN (NAME) VALUES ('Pesce');
INSERT INTO ALLERGEN (NAME) VALUES ('Mandorla');

------------------------------------------------------
-- Restaurant table:
------------------------------------------------------

INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (2);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (3);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (6);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (8);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (2);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (4);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (4);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (2);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (3);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (2);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (2);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (2);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (3);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (5);
INSERT INTO RESTAURANT_TABLE (CAPACITY) VALUES (8);