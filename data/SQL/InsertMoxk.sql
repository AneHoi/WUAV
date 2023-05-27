USE OW_WUAV_x;
GO

INSERT INTO User_ VALUES('Test','Test', 1, '34567', '23456', 1, null)
INSERT INTO User_ VALUES('Tech1','Tech1', 3, '12@12.12', '12121212', 3, null)
GO

INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'Test'), 'Test', '$2a$16$dQY0YELZUXi6qk/HL9gCd.mqkYyidX3AnFsOERgwjlc5n5ExXbUuK', '$2a$16$dQY0YELZUXi6qk/HL9gCd.')
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'Tech1'), 'Tech1', '$2a$16$FhTvlN.jdJYaRmYWCfHbvudtyyoayb9ve7vUEzGfMb4ap0VgZdFcy', '$2a$16$FhTvlN.jdJYaRmYWCfHbvu')
GO

INSERT INTO Customer VALUES('Need Help co', 'Help Me street 1', 'needhelp@help.com', '91191191', '1234321', 'Corporate')

INSERT INTO Case_ VALUES('Conference room set up', 'They need to be able to...', (SELECT User_ID FROM User_ WHERE [User_Name] = 'Test'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'Tech1'), convert(date, '2023-9-14'), null, null)
GO
INSERT INTO Case_ VALUES('Just an old case', 'They want to delete this case', (SELECT User_ID FROM User_ WHERE [User_Name] = 'Test'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'Tech1'), convert(date, '2003-9-14'), CONVERT(date, '2004-9-14'), 1461)
GO
INSERT INTO Case_ VALUES('Just another old case', 'They want to delete this case', (SELECT User_ID FROM User_ WHERE [User_Name] = 'Test'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'Tech1'), convert(date, '2015-9-14'), CONVERT(date, '2016-9-14'), 1461)
GO

INSERT INTO Report VALUES('Conf room setup', 'Conf room need....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'Tech1'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 'Open')
GO

INSERT INTO Report VALUES('Meeting Room Setup', 'Meeting Room needs....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'Tech1'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 'Submitted For Review')
GO
INSERT INTO Report VALUES('Meeting Room Setup', 'Meeting Room needs....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'Tech1'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 'Closed')
GO

INSERT INTO Technicians_Assigned_To_Case VALUES(2,1)

SELECT * FROM User_;
GO

SELECT * FROM Case_;
GO

SELECT * FROM Report;
GO

GO
USE master;
GO