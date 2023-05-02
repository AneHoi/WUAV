USE master;
GO

DROP DATABASE IF EXISTS OW_WUAV_x;
GO

CREATE DATABASE OW_WUAV_x;
GO

USE OW_WUAV_x;
GO

CREATE TABLE User_Type(
    User_Type_ID                INT IDENTITY(1,1)                       NOT NULL,
    USER_TYPE_TYPE              NVARCHAR(60)                            NOT NULL,

    CONSTRAINT PK_USER_TYPE PRIMARY KEY(User_Type_ID)
)
GO

CREATE TABLE User_(
    User_ID                     INT IDENTITY(1,1)                       NOT NULL,
    User_Full_Name              NVARCHAR(250)                           NOT NULL,
    User_Name                   NVARCHAR(250)                           NOT NULL,
    User_Type                   INT                                     NOT NULL,
    User_Email                  NVARCHAR(250)                           NOT NULL,
    User_tlf                    NVARCHAR(50)                            NOT NULL,
    User_Img                    VARBINARY(MAX),

    CONSTRAINT PK_USER_ID PRIMARY KEY(User_ID),

    CONSTRAINT FK_USER_TYPE FOREIGN KEY(User_Type)
    REFERENCES User_Type(User_Type_ID)
)
GO

CREATE TABLE Customer(
    Customer_ID                 INT IDENTITY(1,1)                       NOT NULL,
    Customer_Name               NVARCHAR(250)                           NOT NULL,
    Customer_Address             NVARCHAR(250)                           NOT NULL,
    Customer_Mail               NVARCHAR(250)                           NOT NULL,
    Customer_Tlf                NVARCHAR(50)                            NOT NULL,
    Customer_CVR                INT,
    Customer_Type               NVARCHAR(50)                            NOT NULL,

    CONSTRAINT PK_CUSTOMER_ID PRIMARY KEY(Customer_ID)
)
GO

CREATE TABLE User_Passwords(
    User_User_ID                INT                                     NOT NULL,
    User_User_Name              NVARCHAR(250)                           NOT NULL,
    Users_Password              NVARCHAR(MAX)                           NOT NULL,
    Users_Salt                  NVARCHAR(MAX),

    CONSTRAINT FK_USER_USER_ID FOREIGN KEY(User_User_ID)
    REFERENCES User_(User_ID),

)
GO

CREATE TABLE Case_(
    Case_ID                     INT IDENTITY(1,1)                       NOT NULL,
    Case_Name                   NVARCHAR(500)                           NOT NULL,
    Case_Description            NVARCHAR(750)                           NOT NULL,
    Case_Contact_Person         NVARCHAR(250)                           NOT NULL,
    Case_Customer_ID            INT                                     NOT NULL,
    Case_Assigned_Tech_ID       INT,
    Case_Created_Date           DATE                                    NOT NULL,

    CONSTRAINT PK_CASE_ID PRIMARY KEY(Case_ID),

    CONSTRAINT FK_CASE_CUSTOMER_ID FOREIGN KEY(Case_Customer_ID)
    REFERENCES Customer(Customer_ID),

    CONSTRAINT FK_CASE_ASSIGNED_TECH_ID FOREIGN KEY(Case_Assigned_Tech_ID)
    REFERENCES User_(User_ID)
)
GO

CREATE TABLE Report(
    Report_ID                     INT IDENTITY(1,1)                       NOT NULL,
    Report_Name                   NVARCHAR(500)                           NOT NULL,
    Report_Description            NVARCHAR(750)                           NOT NULL,
    Report_Assigned_Tech_ID       INT                                     NOT NULL,
    Report_Case_ID                INT                                     NOT NULL,
    Report_Created_Date           DATE                                    NOT NULL,
    Report_Log_ID                 INT,
    Report_Is_Active              BIT                                     NOT NULL,

    CONSTRAINT PK_REPORT_ID PRIMARY KEY(Report_ID),

    CONSTRAINT FK_REPORT_CASE_ID FOREIGN KEY(Report_Case_ID)
    REFERENCES Case_(Case_ID),

    CONSTRAINT FK_REPORT_ASSIGNED_TECH_ID FOREIGN KEY(Report_Assigned_Tech_ID)
    REFERENCES User_(User_ID)
)
GO

CREATE TABLE Addendum(
    Addendum_ID                     INT IDENTITY(1,1)                       NOT NULL,
    Addendum_Name                   NVARCHAR(500)                           NOT NULL,
    Addendum_Description            NVARCHAR(750)                           NOT NULL,
    Addendum_Assigned_Tech_ID       INT                                     NOT NULL,
    Addendum_Report_ID              INT                                     NOT NULL,
    Addendum_Created_Date           DATE                                    NOT NULL,
    Addendum_Log_ID                 INT                                     NOT NULL,
    Addendum_Is_Active              BIT                                     NOT NULL,

    CONSTRAINT PK_ADDENDUM_ID PRIMARY KEY(Addendum_ID),

    CONSTRAINT FK_ADDENDUM_REPORT_ID FOREIGN KEY(Addendum_Report_ID)
    REFERENCES Case_(Case_ID),

    CONSTRAINT FK_ADDENDUM_ASSIGNED_TECH_ID FOREIGN KEY(Addendum_Assigned_Tech_ID)
    REFERENCES User_(User_ID)
)
GO

CREATE TABLE Section(
    Section_ID                     INT IDENTITY(1,1)                       NOT NULL,
    Section_Title                  NVARCHAR(250)                           NOT NULL,
    Section_Sketch                 NVARCHAR(MAX),
    Section_Sketch_Comment         NVARCHAR(500),
    Section_Image                  NVARCHAR(MAX),
    Section_Image_Comment          NVARCHAR(500),
    Section_Description            NVARCHAR(MAX),
    Section_Made_By_Tech           INT                                     NOT NULL,
    Section_Report_ID              INT,
    Section_Addendum_ID            INT,

    CONSTRAINT PK_SECTION_ID PRIMARY KEY(Section_ID),

    CONSTRAINT FK_SECTION_MADE_BY_TECH FOREIGN KEY(Section_Made_By_Tech)
    REFERENCES User_(User_ID),

    CONSTRAINT FK_SECTION_REPORT_ID FOREIGN KEY(Section_Report_ID)
    REFERENCES Report(Report_ID),

    CONSTRAINT FK_SECTION_ADDENDUM_ID FOREIGN KEY(Section_Addendum_ID)
    REFERENCES Addendum(Addendum_ID)
)
GO