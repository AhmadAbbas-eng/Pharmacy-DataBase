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
Product_Name varchar(32) not null unique,/*ask Prof.Bassem */
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

create table C_Order(
Order_ID  int primary key,
Order_Date Date,
Order_Price real,
Order_Discount real,
Employee_ID int,
foreign key(Employee_ID) REFERENCES Employee(Employee_ID) ON UPDATE CASCADE
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

Create table Customer2Order(
Customer_NID varchar(16), #
Order_ID  int,
PRIMARY KEY(Customer_NID,Order_ID),
FOREIGN KEY (Customer_NID) REFERENCES Customer(Customer_NID)  ON UPDATE CASCADE,
FOREIGN KEY (Order_ID) REFERENCES C_Order(Order_ID)  ON UPDATE CASCADE
);

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

