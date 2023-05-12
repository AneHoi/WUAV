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
    User_Active                 BIT                                     NOT NULL,
    User_Img                    VARBINARY(MAX),

    CONSTRAINT PK_USER_ID PRIMARY KEY(User_ID),

    CONSTRAINT FK_USER_TYPE FOREIGN KEY(User_Type)
    REFERENCES User_Type(User_Type_ID)
)
GO

CREATE TABLE Customer(
    Customer_ID                 INT IDENTITY(1,1)                       NOT NULL,
    Customer_Name               NVARCHAR(250)                           NOT NULL,
    Customer_Address            NVARCHAR(250)                           NOT NULL,
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


CREATE TABLE Text_Or_Image_On_Report(
    Text_Or_Image_On_Report_ID          INT IDENTITY(1,1)                       NOT NULL,
    Text_On_Report                      NVARCHAR(500),
    Image_On_Report_Image               VARBINARY(MAX),
    Image_On_Report_Comment             NVARCHAR(150),
    Added_By_Tech                       INT                                     NOT NULL,
    Added_Date                          DATE                                    NOT NULL,
    Added_Time                          TIME                                    NOT NULL,
    Log_ID                              INT,

    CONSTRAINT PK_IMAGE_ON_REPORT_ID PRIMARY KEY(Text_Or_Image_On_Report_ID),

    CONSTRAINT FK_ADDED_BY_ID FOREIGN KEY(Added_By_Tech)
    REFERENCES User_(User_ID)
)
GO

CREATE TABLE Text_And_Image_Report_Link(
    Link_ID                       INT IDENTITY(1,1)                         NOT NULL,
    Report_ID                     INT                                       NOT NULL,
    Text_Or_Image_On_Report_ID    INT                                       NOT NULL,
    Text_Or_Image                 NVARCHAR(5)                               NOT NULL,
    Position_In_Report            INT                                       NOT NULL,

    CONSTRAINT PK_LINK_ID PRIMARY KEY(Link_ID),

    CONSTRAINT FK_REPORT_IMAGE_ID FOREIGN KEY(Report_ID)
    REFERENCES Report(Report_ID),
    CONSTRAINT FK_TEXT_OR_IMAGE_REPORT_LINK_ID FOREIGN KEY(Text_Or_Image_On_Report_ID)
    REFERENCES Text_Or_Image_On_Report(Text_Or_Image_On_Report_ID) ON DELETE CASCADE,
)
GO

CREATE TABLE Technicians_Assigned_To_Case(
    Technician_ID                   INT                                     NOT NULL,
    Case_ID                         INT                                     NOT NULL,

    CONSTRAINT PK_TECH_ASSIGNED_ID PRIMARY KEY(Technician_ID,Case_ID),
    CONSTRAINT FK_Tech_ID FOREIGN KEY(Technician_ID)
    REFERENCES User_(User_ID),

    CONSTRAINT FK_CASE_ASSIGNED_TO FOREIGN KEY(Case_ID)
    REFERENCES Case_(Case_ID),

)
GO