INSERT INTO User_Type(USER_TYPE_TYPE)
VALUES('Admin')
INSERT INTO User_Type VALUES('ProjectManager')
INSERT INTO User_Type VALUES('Technician')
INSERT INTO User_Type VALUES('SalesRepresentative')

INSERT INTO User_ VALUES('Michael Tonnesen','A', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Admin'), 'mt@wuav.dk', '11111111', 1, null)
INSERT INTO User_ VALUES('Torben Juhl','PM', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'ProjectManager'), 'tj@wuav.dk', '22222222', 1, null)
INSERT INTO User_ VALUES('Per Julius','T1', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Technician'), 'info@wuav.dk', '12345678', 1, null)
INSERT INTO User_ VALUES('Jesper Jensen','T2', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'Technician'), 'info@wuav.dk', '87654321', 1, null)
INSERT INTO User_ VALUES('Bo Aggerholm','SR', (SELECT DISTINCT User_Type_ID FROM User_Type WHERE USER_TYPE_TYPE = 'SalesRepresentative'), 'boa@wuav.dk', '12341234', 1, null)
GO

INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'T1'),'T1', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'A'), 'A', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'T2'),'T2', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'PM'), 'PM', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'SR'),'SR', '1', '$2a$16$SQKnctQxB89dhg7qDpjUpO')
GO

INSERT INTO Customer VALUES('Need Help co', 'Help Me street 1', 'needhelp@help.com', '91191191', '1234321', 'Corporate')

INSERT INTO Case_ VALUES('Conference room set up', 'They need to be able to...', (SELECT User_ID FROM User_ WHERE [User_Name] = 'PM'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'T1'), convert(date, '2023-9-14'))
GO

INSERT INTO Report VALUES('Conf room setup', 'Conf room need....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'T1'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 1, 'Open')
GO

INSERT INTO Report VALUES('Meeting Room Setup', 'Meeting Room needs....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'T1'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 1, 'Submitted For Review')
GO
INSERT INTO Report VALUES('Meeting Room Setup', 'Meeting Room needs....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'T1'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 1, 'Closed')
GO

INSERT INTO Technicians_Assigned_To_Case VALUES(4,1)

SELECT * FROM User_Type;
GO

SELECT * FROM User_;
GO

SELECT * FROM Case_;
GO

SELECT * FROM Report;
GO

GO