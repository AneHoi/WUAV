INSERT INTO User_Type(USER_TYPE_TYPE)
VALUES('Admin')
INSERT INTO User_Type VALUES('Project Manager')
INSERT INTO User_Type VALUES('Technician')
INSERT INTO User_Type VALUES('Sales Representitiv')

INSERT INTO User_ VALUES('DEAFAULT','T1', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Technician'), 'Tech1@WUAV.dk', 'TLF = 12345678', null)
INSERT INTO User_ VALUES('DEAFAULT','A', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Admin'), 'Admin@WUAV.dk', 'TLF = 11111111', null)
INSERT INTO User_ VALUES('DEAFAULT','T2', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Technician'), 'Tech2@WUAV.dk', 'TLF = 87654321', null)
INSERT INTO User_ VALUES('DEAFAULT','PM', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Project Manager'), 'Manager@WUAV.dk', 'TLF = 22222222', null)
INSERT INTO User_ VALUES('DEAFAULT','S', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Sales Representitiv'), 'Sales@WUAV.dk', 'TLF = 12341234', null)
GO

INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'T1'),'T1', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'A'), 'A', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'T2'),'T2', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'PM'), 'PM', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'S'),'S', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
GO

INSERT INTO Customer VALUES('Need Help co', 'helpme street 1', 'needhelp@help.com', '91191191', '1234321', 'Buisness')

INSERT INTO Case_ VALUES('Confrens room set up', 'They need to be able to...', (SELECT User_ID FROM User_ WHERE [User_Name] = 'PM'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'T1'))
GO

INSERT INTO Report VALUES('Conf room setup', 'Conf room need....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'T1'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Confrens room set up'), convert(date, '2023-9-18'), 1, 1)
GO

INSERT INTO Section VALUES('TV and Intercom', null, null, null, null, 'TV set up........', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'T1'), (SELECT Report_ID FROM Report WHERE Report_Name = 'Conf room setup'), null)

SELECT * FROM User_Type;
GO

SELECT * FROM User_;
GO

SELECT * FROM Case_;
GO

SELECT * FROM Report;
GO

SELECT * FROM Section;
GO