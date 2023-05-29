USE
master;
GO

DROP
DATABASE IF EXISTS OW_WUAV_x;
GO

CREATE
DATABASE OW_WUAV_x;
GO

USE OW_WUAV_x;
GO

CREATE TABLE User_
(
    User_ID             INT IDENTITY(1,1)       NOT NULL,
    User_Full_Name      NVARCHAR(100)           NOT NULL,
    User_Name           NVARCHAR(50)            NOT NULL,
    User_Type           INT                     NOT NULL,
    User_Email          NVARCHAR(250)           NOT NULL,
    User_tlf            NVARCHAR(30)            NOT NULL,
    User_Active         BIT                     NOT NULL,
    User_Img            VARBINARY(MAX),

    CONSTRAINT PK_USER_ID PRIMARY KEY (User_ID),
)
GO

CREATE TABLE Customer
(
    Customer_ID         INT IDENTITY(1,1)       NOT NULL,
    Customer_Name       NVARCHAR(100)           NOT NULL,
    Customer_Address    NVARCHAR(150)           NOT NULL,
    Customer_Mail       NVARCHAR(150)           NOT NULL,
    Customer_Tlf        NVARCHAR(30)            NOT NULL,
    Customer_CVR        INT,
    Customer_Type       NVARCHAR(20)            NOT NULL,

    CONSTRAINT PK_CUSTOMER_ID PRIMARY KEY (Customer_ID)
)
GO

CREATE TABLE User_Passwords
(
    Users_User_ID        INT                     NOT NULL,
    Users_Username      NVARCHAR(50)            NOT NULL,
    Users_Password      NVARCHAR(MAX)           NOT NULL,
    Users_Salt          NVARCHAR(MAX)           NOT NULL,

    CONSTRAINT FK_USERS_USER_ID FOREIGN KEY (Users_User_ID)
    REFERENCES User_ (User_ID),

)
GO

CREATE TABLE Case_
(
    Case_ID                 INT IDENTITY(1,1)     NOT NULL,
    Case_Name               NVARCHAR(100)         NOT NULL,
    Case_Description        NVARCHAR(750)         NOT NULL,
    Case_Contact_Person     NVARCHAR(30)          NOT NULL,
    Case_Customer_ID        INT                   NOT NULL,
    Case_Assigned_Tech_ID   INT,
    Case_Created_Date       DATE                  NOT NULL,
    Case_Closed_Date        DATE,
    Case_Days_To_Keep       INT,

    CONSTRAINT PK_CASE_ID PRIMARY KEY (Case_ID),

    CONSTRAINT FK_CASE_CUSTOMER_ID FOREIGN KEY (Case_Customer_ID)
    REFERENCES Customer (Customer_ID),

    CONSTRAINT FK_CASE_ASSIGNED_TECH_ID FOREIGN KEY (Case_Assigned_Tech_ID)
    REFERENCES User_ (User_ID)
)
GO

CREATE TABLE Report
(
    Report_ID               INT IDENTITY(1,1)   NOT NULL,
    Report_Name             NVARCHAR(100)       NOT NULL,
    Report_Description      NVARCHAR(750)       NOT NULL,
    Report_Assigned_Tech_ID INT                 NOT NULL,
    Report_Case_ID          INT                 NOT NULL,
    Report_Created_Date     DATE                NOT NULL,
    Report_Is_Active        NVARCHAR(30)        NOT NULL,

    CONSTRAINT PK_REPORT_ID PRIMARY KEY (Report_ID),

    CONSTRAINT FK_REPORT_CASE_ID FOREIGN KEY (Report_Case_ID)
    REFERENCES Case_ (Case_ID),

    CONSTRAINT FK_REPORT_ASSIGNED_TECH_ID FOREIGN KEY (Report_Assigned_Tech_ID)
    REFERENCES User_ (User_ID)
)
GO


CREATE TABLE Text_Or_Image_On_Report
(
    Text_Or_Image_On_Report_ID INT IDENTITY(1,1)    NOT NULL,
    Text_On_Report             NVARCHAR(500),
    Image_On_Report_Image      VARBINARY(MAX),
    Image_On_Report_Comment    NVARCHAR(150),
    Added_By_Tech              INT                  NOT NULL,
    Added_Date                 DATE                 NOT NULL,
    Added_Time                 TIME                 NOT NULL,

    CONSTRAINT PK_IMAGE_ON_REPORT_ID PRIMARY KEY (Text_Or_Image_On_Report_ID),

    CONSTRAINT FK_ADDED_BY_ID FOREIGN KEY (Added_By_Tech)
    REFERENCES User_ (User_ID)
)
GO

CREATE TABLE Text_And_Image_Report_Link
(
    Link_ID                    INT IDENTITY(1,1)    NOT NULL,
    Report_ID                  INT                  NOT NULL,
    Text_Or_Image_On_Report_ID INT                  NOT NULL,
    Text_Or_Image              NVARCHAR(5)          NOT NULL,
    Position_In_Report         INT                  NOT NULL,

    CONSTRAINT PK_LINK_ID PRIMARY KEY (Link_ID),

    CONSTRAINT FK_REPORT_IMAGE_ID FOREIGN KEY (Report_ID)
    REFERENCES Report (Report_ID) ON DELETE CASCADE,
    CONSTRAINT FK_TEXT_OR_IMAGE_REPORT_LINK_ID FOREIGN KEY (Text_Or_Image_On_Report_ID)
    REFERENCES Text_Or_Image_On_Report (Text_Or_Image_On_Report_ID) ON DELETE CASCADE,
)
GO

CREATE TABLE Technicians_Assigned_To_Case
(
    Technician_Case_Link_ID     INT IDENTITY(1,1)   NOT NULL,
    Technician_ID               INT                 NOT NULL,
    Case_ID                     INT                 NOT NULL,

    CONSTRAINT PK_TECH_CASE_LINK_ID PRIMARY KEY (Technician_Case_Link_ID),
    CONSTRAINT FK_Tech_ID FOREIGN KEY (Technician_ID)
    REFERENCES User_ (User_ID),

    CONSTRAINT FK_CASE_ASSIGNED_TO FOREIGN KEY (Case_ID)
    REFERENCES Case_ (Case_ID),

)
GO

CREATE TABLE Login_Details(
    Login_Details_ID                 INT IDENTITY(1,1)                       NOT NULL,
    No_Login_Details                 BIT                                     NOT NULL,
    Component                        NVARCHAR(50),
    Username                         NVARCHAR(50),
    Password                         NVARCHAR(100),
    Additional_Info                  NVARCHAR(250),
    Created_Date                     DATE                                    NOT NULL,
    Created_Time                     TIME                                    NOT NULL,
    Added_By_Tech                    INT                                     NOT NULL,

    CONSTRAINT PK_LOGIN_DETAILS_ID PRIMARY KEY(Login_Details_ID),
    CONSTRAINT FK_TECH_LOGIN_LINK FOREIGN KEY(Added_By_Tech)
    REFERENCES User_(User_ID),
)
GO

CREATE TABLE Login_Details_Report_Link(
    Login_Details_Report_Link_ID    INT IDENTITY(1,1)                       NOT NULL,
    Report_ID                       INT                                     NOT NULL,
    Login_Details_ID                INT                                     NOT NULL,

    CONSTRAINT PK_LOGIN_REPORT_LINK PRIMARY KEY(Login_Details_Report_Link_ID),

    CONSTRAINT FK_REPORT_LOGIN_LINK FOREIGN KEY(Report_ID)
    REFERENCES Report(Report_ID) ON DELETE CASCADE,
    CONSTRAINT FK_LOGIN_DETAILS_LINK FOREIGN KEY(Login_Details_ID)
    REFERENCES Login_Details(Login_Details_ID) ON DELETE CASCADE,
)
GO

CREATE TABLE DrawingTable
(
    ImageIconID                INT IDENTITY(1,1) NOT NULL,
    ImageIcon                  VARBINARY( MAX),
    IconDescription            NVARCHAR(50)
)
GO

CREATE TABLE User_Active_Cases_Link(
    User_Active_Cases_Link_ID       INT IDENTITY(1,1)                       NOT NULL,
    User_ID                         INT                                     NOT NULL,
    Case_ID                         INT                                     NOT NULL,

    CONSTRAINT PK_USER_ACTIVE_CASES_LINK PRIMARY KEY(User_Active_Cases_Link_ID),
    CONSTRAINT FK_USER_ID_LINK FOREIGN KEY(User_ID)
    REFERENCES User_(User_ID),
    CONSTRAINT FK_CASE_ID_LINK FOREIGN KEY(Case_ID)
    REFERENCES Case_(Case_ID),
)
GO

CREATE TABLE User_Customer_Link(
    User_Customer_Link_ID           INT IDENTITY(1,1)                       NOT NULL,
    User_ID                         INT                                     NOT NULL,
    Customer_ID                     INT                                     NOT NULL,

    CONSTRAINT PK_USER_CUSTOMER_LINK PRIMARY KEY(User_Customer_Link_ID),
    CONSTRAINT FK_USER_ID_CUSTOMER_LINK FOREIGN KEY(User_ID)
    REFERENCES User_(User_ID),
    CONSTRAINT FK_CUSTOMER_ID_LINK FOREIGN KEY(Customer_ID)
    REFERENCES Customer(Customer_ID),
)
GO

CREATE TABLE CableAndColor(
    Cable_ID                        INT IDENTITY(1,1)                        NOT NULL,
    Cable_Name                      NVARCHAR(30)                            NOT NULL,
    Color_Name                      NVARCHAR(30)                            NOT NULL,

    CONSTRAINT PK_CABLE_ID PRIMARY KEY(Cable_ID),
)
GO