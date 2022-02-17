#show databases;
drop database if exists Pharmacy;
create database Pharmacy;
use  Pharmacy;
SET SQL_SAFE_UPDATES=0;
SET FOREIGN_KEY_CHECKS=1;

create table Name_Manu(
Product_Name varchar(32) PRIMARY KEY,
Product_Manufactrer varchar(32)
);

create table Product(
Product_ID int,
Product_Name varchar(32) not null unique,
Product_Price REAL not null,
Primary Key ( product_ID),
FOREIGN KEY (Product_name) References Name_Manu(Product_Name) ON UPDATE CASCADE
);


create table Supplier(
Supplier_ID  int PRIMARY KEY,
Supplier_Name varchar(32),
Supplier_Address varchar(32),
Supplier_Email varchar(64),
Supplier_Dues real default 0.0
);

CREATE TABLE Batch(
Product_ID  int,
Batch_Production_Date DATE,
Batch_Expiry_Date DATE,
Index(Batch_Production_Date),
index(Batch_Expiry_Date),
Batch_Amount int,
PRIMARY KEY(Product_ID ,Batch_Production_Date,Batch_Expiry_Date),
FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE Drug(
Product_ID int,
Drug_Scientific_Name varchar(64) not null,
Drug_Risk_Pregnency_Category varchar(2)
check ( Drug_Risk_Pregnency_Category IN ('A','B','C') ),
Drug_Dosage varchar(64),/*MG/DAY*/
Drug_Category varchar(32),# Antibiotics
Drug_Dosage_Form varchar(16),/* Tablet */
Drug_Pharmacetical_Category varchar(32), /* Controlled, Danger, non-*/
Primary key (Product_ID),
foreign key (Product_ID) references Product(Product_ID) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE Customer (
Customer_NID varchar(16) PRIMARY KEY,
Customer_Name varchar(32),
Customer_Debt REAL DEFAULT 0
);


CREATE TABLE Customer_Phone(
Customer_NID varchar(16),
Phone varchar(16),
PRIMARY KEY (Phone,Customer_NID),
Foreign Key (Customer_NID) REFERENCES Customer(Customer_NID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Employee(
Employee_ID int PRIMARY KEY,
Employee_Name varchar(32),
Employee_National_ID varchar(16),
Employee_Date_Of_Work DATE, 
Employee_Hourly_Paid REAL, 
Employee_password varchar(64),
isManager varchar(8),
isActive varchar(8)
);

CREATE TABLE Employee_Phone(
Employee_ID int,
Phone varchar(16),
PRIMARY KEY (Phone,Employee_ID),
Foreign Key (Employee_ID) REFERENCES Employee(Employee_ID) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE Work_Hours(
Employee_ID  int,
Worked_Month int
check (Worked_Month < 13 And Worked_Month>0),
Worked_Year int
check (Worked_Year>2000),
Employee_Worked_Hours REAL,
Employee_Hourly_Paid REAL,
PRIMARY KEY (Worked_Year,Worked_Month,Employee_ID),
Foreign Key (Employee_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE
);

CREATE TABLE Tax(
Tax_ID varchar(16) PRIMARY KEY,
Tax_Date Date, 
Tax_Value REAL
);

CREATE TABLE Payment(
Payment_ID  int primary key,
Payment_Date Date,
Payment_Amount REAL,
Payment_Method VARCHAR(16)
check (Payment_Method IN ("Cheque","Cash"))
);

CREATE TABLE Cheque(
Cheque_ID  int PRIMARY KEY,
Bank_Name varchar(32),
Date_Of_Writing DATE,
Due_Date_Of_Cashing DATE,
Payment_ID  int not null,
Manager_ID  int not null, 
foreign key(Payment_ID) REFERENCES Payment(Payment_ID) ON UPDATE CASCADE,
foreign key(Manager_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE
);

Create Table S_Order(
Order_ID  int PRIMARY KEY,
Date_Of_Order Date, 
Order_Cost REAL ,
Order_Discount REAL,#JAVA
Due_Date_For_Payment Date,
Supplier_ID  int ,
Manager_ID  int ,
Recieved_By  int ,
Recieved_Date Date,
foreign key(Supplier_ID) REFERENCES Supplier(Supplier_ID) ON UPDATE CASCADE,
foreign key(Manager_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE,
foreign key(Recieved_By) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE
);

create table S_Order_Batch(
Order_ID int,
Product_ID  int,
Batch_Production_Date DATE,
Batch_Expiry_Date DATE,
Batch_amount int,
primary key(Order_ID ,Product_ID ,Batch_Production_Date,Batch_Expiry_Date),
foreign key(Order_ID) REFERENCES S_Order(Order_ID) ON UPDATE CASCADE,
foreign key(Product_ID) REFERENCES Batch(Product_ID) ON UPDATE CASCADE,
foreign key(Batch_Production_Date) REFERENCES Batch(Batch_Production_Date) ON UPDATE CASCADE,
foreign key(Batch_Expiry_Date) REFERENCES Batch(Batch_Expiry_Date) ON UPDATE CASCADE
);
create table Supplier_Phone(
Supplier_ID  int,
Supplier_Phone varchar(16),
PRIMARY KEY (Supplier_ID,Supplier_Phone),
FOREIGN KEY(Supplier_ID) REFERENCES Supplier(Supplier_ID)  ON DELETE CASCADE ON UPDATE CASCADE
);
select * from customer;
create table C_Order(
Order_ID  int primary key,
Order_Date Date,
Order_Price real,
Order_Discount real,
Employee_ID int,
Customer_NID varchar(16) default 0,
FOREIGN KEY (Employee_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE,
FOREIGN KEY (Customer_NID) REFERENCES Customer(Customer_NID)  ON UPDATE CASCADE
);

create table C_Order_Batch(
Order_ID  int,
Product_ID int,
Batch_Production_Date DATE,
Batch_Expiry_Date DATE,
Order_amount int,
primary key(Order_ID ,Product_ID ,Batch_Production_Date,Batch_Expiry_Date),
foreign key(Order_ID) REFERENCES C_Order(Order_ID) ON UPDATE CASCADE,
foreign key(Product_ID) REFERENCES Batch(Product_ID) ON UPDATE CASCADE,
foreign key(Batch_Production_Date) REFERENCES Batch(Batch_Production_Date) ON UPDATE CASCADE,
foreign key(Batch_Expiry_Date) REFERENCES Batch(Batch_Expiry_Date) ON UPDATE CASCADE);

create table E_Salary(
Manager_ID  int,
Employee_ID  int,
Payment_ID  int,
Primary Key(Manager_ID ,Employee_ID ,Payment_ID ),
foreign key(Employee_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE ,
foreign key(Manager_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE,
foreign key(Payment_ID) REFERENCES Payment(Payment_ID) ON UPDATE CASCADE
);

create table Taxes_Payment(
Payment_ID  int,
Tax_ID varchar(16),
Manager_ID  int,
Primary Key(Manager_ID ,Tax_ID,Payment_ID ),
foreign key(Manager_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE,
foreign key(Payment_ID) REFERENCES Payment(Payment_ID) ON UPDATE CASCADE,
foreign key(Tax_ID) references Tax(Tax_ID)ON UPDATE CASCADE
);


create table Supplier_Payment(
Supplier_ID  int,
Manager_ID  int,
Payment_ID  int,
primary key(Supplier_ID ,Manager_ID,Payment_ID),
foreign key(Manager_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE,
foreign key(Payment_ID) REFERENCES Payment(Payment_ID) ON UPDATE CASCADE,
foreign key(Supplier_ID) REFERENCES Supplier(Supplier_ID) ON UPDATE CASCADE
);

create table Income(
Income_ID int primary key auto_increment, 
Income_amount int,
Income_Date date,
Employee_ID int,
Customer_NID varchar(16),
foreign key(Employee_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE,
foreign key(Customer_NID) REFERENCES Customer(Customer_NID) ON UPDATE CASCADE
);

create table Drug_Disposal(
Dispoal_ID int primary key auto_increment,
Disposal_amount int,
Disposal_date date,
Employee_ID int,
Payment_ID  int,
Product_ID int,
Batch_Production_Date DATE,
Batch_Expiry_Date DATE,
foreign key(Payment_ID) REFERENCES Payment(Payment_ID) ON UPDATE CASCADE,
foreign key(Employee_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE,
foreign key(Product_ID) REFERENCES Batch(Product_ID) ON UPDATE CASCADE,
foreign key(Batch_Production_Date) REFERENCES Batch(Batch_Production_Date) ON UPDATE CASCADE,
foreign key(Batch_Expiry_Date) REFERENCES Batch(Batch_Expiry_Date) ON UPDATE CASCADE
);
INSERT INTO `name_manu` VALUES ('ADCEF','Pharmacare plc'),('ALGONAL','Birzeit pharmaceutical company'),('Allow','Jerusalem Pharmaceuticals'),('Amycin T','Jerusalem Pharmaceuticals'),('BILTRICIDE','BAYER SCHERING PHARMA AG'),('CANDIVAST','Jerusalem Pharmaceuticals'),('Clobed','Jerusalem Pharmaceuticals'),('Diclofen','Jerusalem Pharmaceuticals'),('DOXORUBICIN','EBEWE PHARMA'),('ELYZOL VAG.PESSARIES','Alpharma aps'),('Ezomax','Pharmacare plc'),('FLATIDYL','Cairo eygept'),('FluAminic','Pharmacare plc'),('FLUPHENAZINE BPC','Birzeit pharmaceutical company'),('GLORION','Hikma'),('HYZAAR','MERCK SHARP'),('IPRAVENT RESPIRATOR','Birzeit pharmaceutical company'),('MAGNACEF','Ram pharmetical industrial '),('shamboo','Clear'),('SPIRIT VITACIN','Birzeit pharmaceutical company'),('Tailol','Pharmacare plc');
INSERT INTO `product` VALUES (1,'ALGONAL',5),(2,'BILTRICIDE',10),(3,'CANDIVAST',15),(4,'DOXORUBICIN',20),(5,'ELYZOL VAG.PESSARIES',25),(6,'FLATIDYL',27),(7,'GLORION',26),(8,'HYZAAR',16),(9,'MAGNACEF',75),(10,'Allow',96),(11,'Amycin T',15),(12,'Diclofen',25),(13,'Clobed',21),(14,'Ezomax',15),(15,'ADCEF',13),(16,'FluAminic',89),(17,'Tailol',5),(18,'FLUPHENAZINE BPC',78),(19,'IPRAVENT RESPIRATOR',75),(20,'SPIRIT VITACIN',15),(111,'shamboo',10);
INSERT INTO `drug` VALUES (1,'Codeine','A','150','Analgesics','Tablet ','Controlled'),(2,'Tylenol','A','250','Antacids','Powder','Danger'),(3,'Motrin','C','100','Antianxiety ','Liquid','Controlled'),(4,'Zithromax','C','50','Antiarrhythmics','Spray','Controlled'),(5,'Claritin','A','750','Antibacterials','cream','Danger'),(6,'Aspirin','A','825','Antibacterials','Tablet ','Danger'),(7,'Mucinex','B','575','Anticoagulants ','Powder','Controlled'),(8,'Vicodin','C','250','Anticonvulsants','Liquid','Controlled'),(9,'Lipitor','C','25','Anticonvulsants','Spray','Controlled'),(10,'Benadryl','B','230','Antidiarrheals','cream','non'),(11,'Mucinex','B','20','Antidiarrheals','Tablet ','non'),(12,'desloratadine','A','275','Antihypertensives','Powder','non'),(13,'Dextromethorphan','C','235','Antihypertensives','Liquid','non'),(14,'Dextromethorphan','B','575','Antacids','Spray','non'),(15,'Lipitor','A','250','Antianxiety ','cream','non'),(16,'Benadryl','B','25','Antiarrhythmics','Tablet ','non'),(17,'Mucinex','A','230','Antibacterials','Powder','non'),(18,'desloratadine','C','20','Antibacterials','Liquid','non'),(19,'Esomeprazole','C','275','Anticoagulants ','Spray','non'),(20,'Pseidoephedrine','A','235','Anticonvulsants','cream','non');
INSERT INTO `supplier` VALUES (1,'Jerusalem Pharmaceuticals','Al-Beriah','599',0),(2,'Pharmacare plc','Ramallah','598',0),(3,'Birzeit pharmaceutical company','Birzeit','597',0),(4,'Ram pharmetical industrial ','Al Ram','596',0),(5,'Al Salam Drugstore','Ramallah','22975687',0),(6,'Nazzal drugstore','Ramallah','2295255990',0);
INSERT INTO `supplier_phone` VALUES (1,'0591234567'),(2,'0591234560'),(2,'0591234561'),(3,'0591234562'),(4,'0591234563'),(5,'0591234564'),(6,'0591234565'),(6,'0591234566');
INSERT INTO `customer` VALUES ('0','sporadic sales',0),('406498130','Loor Wael',0),('406498131','Aseel Sabri',0),('406498132','Ahmad Abbas ',0),('406498133','Kareem Afaneh ',0),('406498134','Adam Abbas',0),('406498135','Ayman Sawalhi',0),('406498136','Wael Dirbas',0),('406498137','Naser Imad ',0),('406498138','Khalil Alwneh',0),('406498139','Ahmad Tayseer',0);
INSERT INTO `customer_phone` VALUES ('406498130','0591234570'),('406498131','0591234569'),('406498132','0591234568'),('406498133','0591234577'),('406498134','0591234565'),('406498134','0591234566'),('406498135','0591234564'),('406498136','0591234563'),('406498137','0591234562'),('406498138','0591234560'),('406498138','0591234561'),('406498139','0591234567');
INSERT INTO `employee` VALUES (-1,'none','0000000000','2016-09-01',0,'000000000',0,0),(1,'Yahya','1111111111','2016-09-01',30,'0ea1a525f3193d3','true','true'),(2,'Ahmad','2222222222','2018-09-01',25,'0d517b2202c536a','false','true'),(3,'Omar','3333333333','2020-10-10',20,'0f11b32743353f6','false','true');
INSERT INTO `employee_phone` VALUES (1,'0598141006'),(1,'0598151007'),(2,'0597161007'),(3,'0595731010');
