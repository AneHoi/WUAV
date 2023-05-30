USE OW_WUAV_x;
GO

INSERT INTO User_ VALUES('Michael Tonnesen','MT001', 1, 'info@wuav.dk', '75119191', 1, null)
INSERT INTO User_ VALUES('Per Julius','PJ001', 3, 'info@wuav.dk', '75119191', 1, null)
INSERT INTO User_ VALUES('Brian Jensen','BJ001', 3, 'info@wuav.dk', '75119191', 1, null)
INSERT INTO User_ VALUES('Jesper Jensen','JJ001', 3, 'info@wuav.dk', '75119191', 1, null)
GO

INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'MT001'), 'MT001', '$2a$16$Eia0n5ykUufigKXnNQOt1OJhHPf5FdEmJ5V0Qd7.jc6D1vvWOh1Sa', '$2a$16$Eia0n5ykUufigKXnNQOt1O')
GO
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'PJ001'), 'PJ001', '$2a$16$8mvMqTUaeWggoXQ9ioD0cOzBR9aVWhgf.jXV2IStPMLQIYtmG84zO', '$2a$16$8mvMqTUaeWggoXQ9ioD0cO')
GO
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'BJ001'), 'BJ001', '$2a$16$J/vV95s4pszYY/JAML1L9eIt8n85azPhC5xSMvKiuLphqzHX7GRKq', '$2a$16$J/vV95s4pszYY/JAML1L9e')
GO
INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = 'JJ001'), 'JJ001', '$2a$16$y44TKPM843W/83q6ePdRz.69qtfS.ejRgB.xZvmoYiwzNwgYubMFe', '$2a$16$y44TKPM843W/83q6ePdRz.')
GO

INSERT INTO Customer VALUES('Need Help co', 'Help Me street 1', 'needhelp@help.com', '91191191', '1234321', 'Corporate')

INSERT INTO Case_ VALUES('Conference room set up', 'They need to be able to...', (SELECT User_ID FROM User_ WHERE [User_Name] = 'MT001'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'BJ001'), convert(date, '2023-9-14'), null, null)
GO
INSERT INTO Case_ VALUES('Just an old case', 'They want to delete this case', (SELECT User_ID FROM User_ WHERE [User_Name] = 'MT001'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'JJ001'), convert(date, '2003-9-14'), CONVERT(date, '2004-9-14'), 1461)
GO
INSERT INTO Case_ VALUES('Just another old case', 'They want to delete this case', (SELECT User_ID FROM User_ WHERE [User_Name] = 'MT001'), 1, (SELECT User_ID FROM User_ Where [User_Name] = 'PJ001'), convert(date, '2015-9-14'), CONVERT(date, '2016-9-14'), 1461)
GO

INSERT INTO Report VALUES('Conf room setup', 'Conf room need....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'BJ001'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 'Open')
GO

INSERT INTO Report VALUES('Meeting Room Setup', 'Meeting Room needs....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'JJ001'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 'Submitted For Review')
GO
INSERT INTO Report VALUES('Meeting Room Setup', 'Meeting Room needs....', (SELECT DISTINCT User_ID FROM User_ WHERE [User_Name] = 'PJ001'), (SELECT DISTINCT Case_ID FROM Case_ WHERE Case_Name = 'Conference room set up'), convert(date, '2023-9-18'), 'Closed')
GO
INSERT INTO CableAndColor Values('HDMI', 'Blue')
GO
INSERT INTO CableAndColor Values('Ethernet', 'Brown')
GO
INSERT INTO CableAndColor Values('Power', 'Silver')
GO
INSERT INTO CableAndColor Values('Antenna', 'Yellow')
GO
INSERT INTO CableAndColor Values('Sound', 'Lavender')
GO

INSERT INTO Technicians_Assigned_To_Case VALUES(2,2)

SELECT * FROM User_;
GO

SELECT * FROM Case_;
GO

SELECT * FROM Report;
GO

SELECT * FROM CableAndColor;
GO

USE master;
GO