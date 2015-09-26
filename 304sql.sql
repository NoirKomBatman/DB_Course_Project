drop table Friends;
drop table Obtains;
drop table Achievements;
drop table Collects;
drop table ItemBelongs;
drop table Mentions;
drop table Owns;
drop table GameRemoves;
drop table GamePublishes;
drop table Developer;
drop table News;
drop table ValveDeveloper;
drop table BannedPlayer;
drop table Administrator;
drop table Player;



create table Player (
	playerID	int,
	name		varchar(50),
	email		varchar(50) not null,
	password	varchar(50) not null,
	userLvl		int,
	primary key(playerID),
    unique (email)
);


create table Administrator (	
	adminID		int,
	primary key(adminID),
	foreign key(adminID) references Player on delete cascade /* if player/base-class is deleted then sub-classes should be deleted */
);

create table BannedPlayer (
	playerID 	int,
	adminID 	int,
	banDate		date,
	banReason	varchar(50),
	primary key(playerID),
	foreign key(playerID) references Player on delete cascade,
	foreign key(adminID) references Administrator on delete cascade
);

create table ValveDeveloper (
	valveDevID		int,
	position		varchar(50),	
	primary key(valveDevID),
	foreign key(valveDevID) references Administrator(adminID) on delete cascade  /* if player/base-class is deleted then sub-classes should be deleted */
);

create table News (
	newsID		int,
	message		varchar(500),
	publishDate		date, 
	primary key(newsID)
);

create table Developer (
	name		varchar(50),
	primary key(name)
);
create table GamePublishes (
	appID		int,
	devName		varchar(50),
	primary key(appID),
	foreign key(devName) references Developer on delete set null
);

create table GameRemoves (
	appID		int,
	genre		varchar(20),
	price		float,
	isRemoved	char(1),
	title		varchar(50),
	developmentDate	date,
	rating		float,
	removeReason	varchar(50),
	valveDevID	int,
	primary key(appID),
	foreign key(appID) references GamePublishes on delete cascade, /* game must always has an appID */
	foreign key(valveDevID) references ValveDeveloper on delete set null /* if valve developer is deleted the game shouldn't be deleted */
);

create table Owns (
	playerID	int,
	appID		int,
	since		date,
	primary key(playerID, appID),
	foreign key(playerID) references Player on delete cascade,
	foreign key(appID) references GamePublishes on delete cascade
);

create table Mentions (
	newsID		int,
	appID		int,
	primary key(newsID, appID),
	foreign key(newsID) references News on delete cascade,
	foreign key(appID) references GamePublishes on delete cascade
);

create table ItemBelongs (
	itemID		varchar(100),
	appID		int,
	itemLvl		int,
	description	varchar(1000),
	primary key(itemID),
	foreign key(appID) references GamePublishes on delete set null
);

create table Collects (
	playerID	int,
	itemID		varchar(100),
	dateOfCollection		date,
	primary key(playerID, itemID),
	foreign key(playerID) references Player on delete cascade,
	foreign key(itemID) references ItemBelongs on delete cascade
);

create table Achievements (
	appID		int,
	achievementName		varchar(50),
	description		varchar(500),
	devName		varchar(50),
	primary key(appID, achievementName),
	foreign key(appID) references GamePublishes on delete cascade, /* achievement doesn't live without the game */
	foreign key(devName) references Developer on delete set null /* set devName to null */
);

create table Obtains (
	appID		int,
	playerID	int,
	achievementName		varchar(50),
	primary key(appID, playerID, achievementName),
	foreign key(playerID) references Player on delete cascade,
	foreign key(appID, achievementName) references Achievements on delete cascade
);

create table Friends (
	friendshipDate	char(10),
	playerID1		int,
	playerID2		int,
	primary key(playerID1, playerID2),
	foreign key(playerID1) references Player on delete cascade,
	foreign key(playerID2) references Player on delete cascade
);




-- now add in the Players
insert into Player values(1,'Jastine','jt@hotmail.com','korra',1);
insert into Player values(2,'Bolat','noire@mail.ru','arigatou',34);
insert into Player values(3,'Ali','ali@gmail.com','bahasa_indonesia',2);
insert into Player values(4,'Baber','babs@gmail.com','thats_what_she_said',99);
insert into Player values(5,'Robert','robert@gmail.com','edgy_memes',100);
insert into Player values(6,'Hai','hai@gmail.com','kitsilano',11);
insert into Player values(7,'Madina','madina@mail.ru','fassbender',5);
insert into Player values(8,'Nelson','mandela@gmail.com','jojos_adventures',30);
insert into Player values(9,'Daoko','daoko@goomail.jp','2.5dimension',22);
insert into Player values(10, 'Default_Ban','ban@ban.ban','banbanban',0);

-- player/admin/valveDeveloper
insert into Player values(11,'Boba Fett','boba@biuntyhunters.co','$$$money$$$',100);
insert into Player values(12,'Elon Musk','elonmuskoffice@spacex.com','paypalX',50);
insert into Player values(13,'Ex Machina','robot@hotmail.com','robot_robot',50);
insert into Player values(14,'Mad Max','max@wastelandmail.com','fury_road',99);
insert into Player values(15,'Darth Vader','anakin@hotmail.com','anakin',100);
insert into Player values(16,'Tidehunter','tide@hotmail.com','midmidmid',10);
insert into Player values(17,'Spy','spy@mann.co','scouts_mom',10);
insert into Player values(18,'Jessica','sjt@hotmail.com','jakarta',1);
insert into Player values(19,'Alzhik','alzhik@mail.ru','san_jose',11);
insert into Player values(20,'Maulen','maulen@mail.ru','vancity',1);


-- administrator
insert into Administrator values(10);
insert into Administrator values(11);
insert into Administrator values(12);
insert into Administrator values(13);
insert into Administrator values(14);
insert into Administrator values(15);
insert into Administrator values(16);
insert into Administrator values(17);
insert into Administrator values(18);
insert into Administrator values(19);
insert into Administrator values(20);


-- valveDeveloper
insert into ValveDeveloper values(16,'DOTA 2 Lead Designer');
insert into ValveDeveloper values(17,'TF2 Senior Intelligence Spy');
insert into ValveDeveloper values(18,'Lead Software Developer');
insert into ValveDeveloper values(19,'Project manager');
insert into ValveDeveloper values(20,'Senior Marketing Associate');

-- bannedPlayer
insert into BannedPlayer values(2,10,to_date('12-NOV-2012','DD-MM-YYYY'),'Hacking');
insert into BannedPlayer values(4,10,to_date('05-JAN-2015','DD-MM-YYYY'),'Pirating');
insert into BannedPlayer values(6,11,to_date('20-APR-2014','DD-MM-YYYY'),'Dota 2 market speculation');
insert into BannedPlayer values(8,18,to_date('08-FEB-2015','DD-MM-YYYY'),'Plays too much visual novels');

-- bannedPlayer where players are Administrators and ValveDeveloper
insert into BannedPlayer values(13,11,to_date('01-JUN-2015','DD-MM-YYYY'),'Robots are not allowed');



-- now add in the Developer
insert into Developer values('Square Enix'); 
insert into Developer values('Valve');
insert into Developer values('Bethesda Game Studio');
insert into Developer values('CD Projekt Red');
insert into Developer values('SEGA');
insert into Developer values('Rockstar North');
insert into Developer values('Paradox Development Studio');
insert into Developer values('Firaxis Games');
insert into Developer values('Dennaton Games');
insert into Developer values('Raven Software');

--now add in the GamePublishes
insert into GamePublishes values (1, 'Square Enix');
insert into GamePublishes values (2, 'Valve');
insert into GamePublishes values (3, 'Bethesda Game Studio');
insert into GamePublishes values (4, 'CD Projekt Red');
insert into GamePublishes values (5, 'SEGA');
insert into GamePublishes values (6, 'Paradox Development Studio');
insert into GamePublishes values (7, 'Firaxis Games');
insert into GamePublishes values (8, 'Rockstar North');
insert into GamePublishes values (9, 'Dennaton Games');
insert into GamePublishes values (10, 'Raven Software');
insert into GamePublishes values (11, 'Raven Software');
insert into GamePublishes values (12, 'Square Enix');
insert into GamePublishes values (13, 'Square Enix');
insert into GamePublishes values (14, 'Valve');
insert into GamePublishes values (15, 'Paradox Development Studio');
insert into GamePublishes values (16, 'Paradox Development Studio');
insert into GamePublishes values (17, 'Bethesda Game Studio');
insert into GamePublishes values (18, 'Bethesda Game Studio');
insert into GamePublishes values (19, 'SEGA');
insert into GamePublishes values (20, 'SEGA');

-- now add in the GameRemoves
insert into GameRemoves values (1, 'RPG', 17.99 , 'N', 'Final Fantasy XIII', TO_DATE('09-OCT-2014','DD-MM-YYYY'), 0.73, null, null);
insert into GameRemoves values (2, 'Moba', 0, 'N', 'Dota 2', TO_DATE('09-JUL-2013','DD-MM-YYYY'), 0.93, null, null);
insert into GameRemoves values (3, 'MMORPG', 69.99,'Y', 'Elder Scrolls Online', TO_DATE('04-APR-2014','DD-MM-YYYY'), 0.76, 'The game is too bad', 16);
insert into GameRemoves values (4, 'Open World', 54.99, 'N','The Witcher 3', TO_DATE('18-MAY-2015','DD-MM-YYYY'),0.92, null, null);
insert into GameRemoves values (5, 'Turn-based', 5.49, 'N','Valkyria Chronicles',TO_DATE('11-NOV-2014','DD-MM-YYYY'),0.94, null, null);
insert into GameRemoves values (6, 'Grand Strategy', 21.99, 'N', 'Europa Universalis IV', TO_DATE('13-AUG-2013','DD-MM-YYYY'), 0.93, null, null);
insert into GameRemoves values (7, 'Turn-based Strategy', 16.49, 'N', 'Sid Meiers Civilization V', TO_DATE('21-SEP-2014','DD-MM-YYYY'), 0.97, null, null);
insert into GameRemoves values (8, 'Open World', 67.48, 'Y', 'Grand Theft Auto V', TO_DATE('14-APR-2015','DD-MM-YYYY'), 0.80, 'Extreme Violence', 16);
insert into GameRemoves values (9, 'Action', 11.38, 'N', 'Hotline Miami 2', TO_DATE('10-MAR-2015','DD-MM-YYYY'), 0.89, null, null);
insert into GameRemoves values (10,'Action', 10.99, 'Y', ' Star Wars Jedi Knight', TO_DATE('16-SEP-13','DD-MM-YYYY'), 0.95, 'Game is too old', 16);
insert into GameRemoves values (11,'FPS', 48.74, 'N', 'Call of Duty: Advanced Warfare', TO_DATE('03-NOV-2014','DD-MM-YYYY'), 0.65, null, null );
insert into GameRemoves values (12,'Adventure', 12.49, 'N', 'Tomb Raider',TO_DATE('04-MAR-2014','DD-MM-YYYY'), 0.96, null, null);
insert into GameRemoves values (13,'JRPG', 11.89, 'Y', 'Final Fantasy VII', TO_DATE('04-JUL-2013','DD-MM-YYYY'), 0.94, 'License Issues', 17);
insert into GameRemoves values (14,'FPS',5.49, 'N', 'Team Fortress 2', TO_DATE('10-OCT-2014','DD-MM-YYYY'), 0.96, null, null);
insert into GameRemoves values (15,'Grand Strategy', 21.99, 'N', 'Crusader Kings II', TO_DATE('14-FEB-2012','DD-MM-YYYY'),0.93, null, null);
insert into GameRemoves values (16,'Grand Strategy', 5.49, 'Y', 'Victoria II', TO_DATE('30-AUG-2010','DD-MM-YYYY'),0.91, 'Violence', 17);
insert into GameRemoves values (17,'RPG', 5.49, 'N', 'Fallout 3', TO_DATE('28-OCT-2014','DD-MM-YYYY'), 0.84, null, null);
insert into GameRemoves values (18,'Stealth', 10.99,'N', 'Dishonored', TO_DATE('08-OCT-2012','DD-MM-YYYY'), 0.97, null, null);
insert into GameRemoves values (19,'Strategy', 10.99, 'N', 'Medieval II: Total War', TO_DATE('03-NOV-2014','DD-MM-YYYY'),0.95, null, null);
insert into GameRemoves values (20,'Strategy', 10.99, 'Y', 'Rome: Total War',TO_DATE('22-SEP-14','DD-MM-YYYY'),0.95,'Violence', 18);


--now add in the News
insert into News values(101,'FFXIII December Patch Date Announcement',TO_DATE('04-DEC-2014','DD-MM-YYYY'));
insert into News values(102,'Now available on Steam - Final Fantasy XIII',TO_DATE('09-OCT-2014','DD-MM-YYYY'));
insert into News values(103,'Dota 2 International 2015 has the biggest price pool in esports history', TO_DATE('05-JUN-2015','DD-MM-YYYY'));
insert into News values(104,'The Collectors Cache', TO_DATE('04-JUN-2015','DD-MM-YYYY'));
insert into News values(105,'Elder Scrolls Online to hold free weekend for Beta players', TO_DATE('13-APR-2015','DD-MM-YYYY'));
insert into News values(106,'Fraudulent Elder Scrolls Online keys will be deactivated today',TO_DATE('26-MAY-2015','DD-MM-YYYY'));
insert into News values(107,'The AI Wars Continue: Civilization V Battle Royale',TO_DATE('23-MAR-2015','DD-MM-YYYY'));
insert into News values(108,'Why people are making the AI fight itself in Civilization',TO_DATE('20-FEB-2015','DD-MM-YYYY'));
insert into News values(109,'The Steam Holiday Sale Day 2',TO_DATE('14-DEC-2014','DD-MM-YYYY'));
insert into News values(110,'Valkyria Chronicles has Launched!',TO_DATE('11-NOV-2014','DD-MM-YYYY'));

-- now add in the Mentions tables
insert into Mentions values (101, 1);
insert into Mentions values (102, 1);
insert into Mentions values (103, 2);
insert into Mentions values (104, 2);
insert into Mentions values (105, 3);
insert into Mentions values (106, 3);
insert into Mentions values (107, 7);
insert into Mentions values (108, 7);
insert into Mentions values (109, 5);
insert into Mentions values (110, 5);

-- now insert into owns
insert into Owns values(1,1, TO_DATE('23-DEC-2014','DD-MM-YYYY'));
insert into Owns values(1,2, TO_DATE('22-MAR-2014','DD-MM-YYYY'));
insert into Owns values(2,1, TO_DATE('21-DEC-2014','DD-MM-YYYY'));
insert into Owns values(2,7, TO_DATE('21-JAN-2015','DD-MM-YYYY'));
insert into Owns values(3,4, TO_DATE('25-APR-2015','DD-MM-YYYY'));
insert into Owns values(4,7, TO_DATE('26-DEC-14','DD-MM-YYYY'));
insert into Owns values(5,1, TO_DATE('27-DEC-2014','DD-MM-YYYY'));
insert into Owns values(6,2, TO_DATE('28-DEC-2014','DD-MM-YYYY'));
insert into Owns values(7,2, TO_DATE('22-FEB-2014','DD-MM-YYYY'));
insert into Owns values(8,2, TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(9,2, TO_DATE('22-JAN-2014','DD-MM-YYYY'));
insert into Owns values(11,1, TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(11,2, TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(12,15,TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(16,15,TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(16,19,TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(17,2, TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(17,5, TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(1,7,TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(3,7, TO_DATE('22-DEC-2014','DD-MM-YYYY'));
insert into Owns values(4,11,TO_DATE('22-DEC-2014','DD-MM-YYYY'));

-- now add in the achievements
-- appID, achievementName, achievementDescription, devname
insert into Achievements values(1,'Instrument of Fate','Took the first steps toward challenging an unjust fate.','Square Enix');
insert into Achievements values(1,'Instrument of Dissent','Survived the Purge to confront a greater peril.','Square Enix');
insert into Achievements values(1,'Instrument of Tragedy','Strode into dangers den and paid the consequences','Square Enix');
insert into Achievements values(1,'Instrument of Flight','Slipped through the net and lived to fight another day.','Square Enix');
insert into Achievements values(1,'Instrument of Vengeance','Resolved to be more than a victim of circumstance.','Square Enix');

insert into Achievements values(4,'Lilac and Gooseberries','','CD Projekt Red');
insert into Achievements values(4,'Butcher of Blaviken','Kill at least 5 opponents in under 10 seconds.','CD Projekt Red');
insert into Achievements values(4,'Lets Cook!','Learn 12 potion formulae.','CD Projekt Red');
insert into Achievements values(4,'Cant Touch This!','Kill 5 foes in a fight without taking damage (except for Toxicity) and without using the Quen Sign.','CD Projekt Red');
insert into Achievements values(4,'Bookworm','Read 30 books, journals or other documents','CD Projekt Red');

insert into Achievements values(5,'Gallian Medal of Honor','Complete the Chapter 9 battle "Kidnapping of Cordelia"','SEGA');
insert into Achievements values(5,'Fouzen Service Medal','Complete the Chapter 10 battle "Liberation of Fouzen"','SEGA');
insert into Achievements values(5,'Naggiar Service Medal','Complete the Chapter 14 battle "Showdown at Naggiar"','SEGA');
insert into Achievements values(5,'The Lance of Gallia','Kill an enemy ace','SEGA');
insert into Achievements values(5,'Excellence in Leadership','Learn every order','SEGA');

insert into Achievements values(7,'Say Hello to My Little Friend','Reach Ally Status with a City-State.','Firaxis Games');
insert into Achievements values(7,'Neighborhood Bully','Annex a previously puppetted City-State.','Firaxis Games');
insert into Achievements values(7,'Engineer','Build a Wonder.','Firaxis Games');
insert into Achievements values(7,'Moving on Up','Earn a Unit Promotion.','Firaxis Games');
insert into Achievements values(7,'Second City','Found a second city.','Firaxis Games');

insert into Achievements values(9,'FANTASTIC!','Unlock all the Fans','Dennaton Games');
insert into Achievements values(9,'SNAKE CHARMER','Unlock all snake masks','Dennaton Games');
insert into Achievements values(9,'KARMA','Die 1000 times','Dennaton Games');
insert into Achievements values(9,'FANATIC','Clear a level with each Fan','Dennaton Games');
insert into Achievements values(9,'COMBO BEGINNER','Perform a 5x combo','Dennaton Games');

insert into Achievements values(11,'Seoul Mates','','Raven Software');
insert into Achievements values(11,'Welcome to Atlas','','Raven Software');
insert into Achievements values(11,'Life in the Fast Lane','','Raven Software');
insert into Achievements values(11,'Radioactive','','Raven Software');
insert into Achievements values(11,'Born to Die','','Raven Software');

insert into Achievements values(12,'One Smart Cookie','Complete one optional tomb.','Square Enix');
insert into Achievements values(12,'Predator','Kill 50 enemies with the bow.','Square Enix');
insert into Achievements values(12,'Bookworm','Find 25% of all documents.','Square Enix');
insert into Achievements values(12,'Sharp Shooter','Perform 50 headshot kills in the single player campaign.','Square Enix');
insert into Achievements values(12,'Opportunist','Kill 25 unaware enemies.','Square Enix');

insert into Achievements values(14,'Head Of The Class','Play a complete round with every class.','Valve');
insert into Achievements values(14,'Nemesis','Get five revenge kills.','Valve');
insert into Achievements values(14,'Hard To Kill','Get five kills in a row without dying.','Valve');
insert into Achievements values(14,'Dynasty','Win 20 games.','Valve');
insert into Achievements values(14,'Hardcore','Accumulate 1000 total kills.','Valve');

insert into Achievements values(15,'The Marriage Game','Marry another character','Paradox Development Studio');
insert into Achievements values(15,'Full House','Sire five children','Paradox Development Studio');
insert into Achievements values(15,'Royal Blood','Achieve a score of 10,000','Paradox Development Studio');
insert into Achievements values(15,'Pilgrim','Go on a Christian Pilgrimage','Paradox Development Studio');
insert into Achievements values(15,'Until Death Do Us Part','Have your spouse assassinated','Paradox Development Studio');

insert into Achievements values(18,'Dishonored','Complete the Intro missions','Bethesda Game Studio');
insert into Achievements values(18,'Excommunication','Eliminate High Overseer Campbell','Bethesda Game Studio');
insert into Achievements values(18,'Child Care','Find Emily Kaldwin','Bethesda Game Studio');
insert into Achievements values(18,'Rogue','Assassinate 10 unaware enemies','Bethesda Game Studio');
insert into Achievements values(18,'Bodyguard','','Bethesda Game Studio');



-- now insert into friends
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),1, 2;
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),1, 3);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),1, 4);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),2, 7);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),2, 3);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),2, 4);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),3, 2);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),7, 1);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),4, 3);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),1, 11);
insert into Friends values (TO_DATE('22-SEP-2014','DD-MM-YYYY'),3, 12);
insert into Friends values (TO_DATE('22-DEC-2014','DD-MM-YYYY'),3, 16);
insert into Friends values (TO_DATE('22-DEC-14','DD-MM-YYYY'),3, 19);
insert into Friends values (TO_DATE('22-DEC-14','DD-MM-YYYY'),6, 13);
insert into Friends values (TO_DATE('22-DEC-14','DD-MM-YYYY'),6, 16);
insert into Friends values (TO_DATE('22-DEC-14','DD-MM-YYYY'),6, 9);



--itemID, appID, itemLvl, description
--Team Fortress 2
insert into ItemBelongs values('The Scottish Handshake', 2, 10, 'Your enemies will think youre making peace, right up until the terrifying moment that their hand is very seriously cut! Heres the trick: Its a broken bottle!');
insert into ItemBelongs values('The Hunstman', 2, 10, '');
insert into ItemBelongs values('The Wrangler', 2, 5, 'Take manual control of your Sentry Gu');
insert into ItemBelongs values('The Fast Learner', 2, 10, 'Read a book – or at least look like you can – with this vintage educational ensemble. Be the B.M.O.C. (Big Mouth on Campus) of your own make-believe Ivy League!');
insert into ItemBelongs values('Bills Hat', 2, 10, 'Left 4 You');
insert into ItemBelongs values('Magistrates Mullet', 2, 65, 'They say justice is blind. What they dont tell you is that justice is also invisible, and also less interested in justice than murder.');
insert into ItemBelongs values('Jarate', 2, 5, 'Coated enemies take mini-crits. Also handy for putting out a fire');
insert into ItemBelongs values('The Gunboats', 2, 10, '-60% blast damage from rocket jumps');
insert into ItemBelongs values('The Buff Banner', 2, 10, 'Provides an offensive buff that causes nearby team members to do mini-crits. Rage increases through damage done');
insert into ItemBelongs values('The Bazaar Bargain', 2, 10, 'Base charge rate decreased by 20%. Each scoped headshot decreases the weapons charge time. A scoped body shot or miss reduces the bonus.');

--DOTA 2
insert into ItemBelongs values('Inscribed Fireborn Odachi', 14, 100, 'Forged in the fires that bubbled up from the sea on the night your homeland sank beneath the waves!');
insert into ItemBelongs values('Auspicious Fire of the Exiled Ronin', 14, 75, 'The flames of the Ronin burn hot against all who oppose him.');
insert into ItemBelongs values('Genuine Triumph', 14, 500, 'Many will fight. Few will achieve immortality.');
insert into ItemBelongs values('Genuine Redhoof', 14, 100, 'The Redhooves of the Western Wails were long thought a thing of myth. The last survivors of an ancient army, their riders lost to sword and arrow, these hardy steeds returned to the wilds where they bred and grew more feral with each passing generation. Caught young, this fiery Redhoof shows all the courage of its warsteed ancestors.');
insert into ItemBelongs values('Pudgling', 14, 100, 'Theres them that says a well-fed lads a happy lad. They never met Pudge. If its a bite-sized snack for the road you want, hes got just the thing.');
insert into ItemBelongs values('Frozen Fiery Ward of Eki Bukaw', 14, 30, 'With this fiery ward, no secrets are safe, no location hidden, no weakness unexposed.');
insert into ItemBelongs values('Inscribed Kindred of the Iron Dragon', 14, 69, 'The blood of Slyrak flows strong through the Dragon Knight, and its transformative powers have long been a thing of legend. For a chosen few who have received dragon blood, the magic runs unusually thick, its effects magnified and distorted. In these rare recipients, the reach of the transformation is enhanced, drawing strength not from any single dragon but from the oldest ancestral strains, and a forgotten age when whole kingdoms shook with fear, and eyeless, armored dragons raged wars across the countryside.');
insert into ItemBelongs values('Inscribed Muh Keen Gun', 14, 90, 'Not content to kill things one bullet at a time, Sniper commissioned the best gunsmith in the land to construct him a firearm worthy of his talented trigger finger. Lovingly designed and crafted with all the skill and attention to detail befitting a master smithy of the keen, the weapon that resulted is truly one for the ages. Its a bit on the heavy side, but not when balanced against the weight of corpses it will produce. Hernia not included.');
insert into ItemBelongs values('Hammer of Thunderwraths Calling', 14, 10, 'Hammer of Thunderwraths Calling');
insert into ItemBelongs values('Cape of Thunderwraths Calling', 14, 10, 'Only those who are called in service to the Omniscient can wear a cape such as this.');


--playerID itemID dateOfCollection
insert into Collects values(1, 'The Scottish Handshake', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(1, 'Bills Hat', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(1, 'The Hunstman', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(1, 'The Fast Learner', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(1, 'The Gunboats', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(2, 'Genuine Triumph', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(3, 'The Scottish Handshake', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(4, 'Cape of Thunderwraths Calling', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(5, 'Inscribed Muh Keen Gun', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(6, 'The Buff Banner', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(7, 'The Gunboats', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(8, 'Pudgling', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(9, 'Pudgling', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(16, 'Auspicious Fire of the Exiled Ronin', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(17, 'The Scottish Handshake', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(18, 'Inscribed Fireborn Odachi', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(19, 'The Hunstman', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(20, 'The Gunboats', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(12, 'The Bazaar Bargain', to_date('01-AUG-2014','DD-MM-YYYY'));
insert into Collects values(14, 'Jarate', to_date('01-AUG-2014','DD-MM-YYYY'));

insert into Obtains values(1,1,'Instrument of Fate');
insert into Obtains values(1,1,'Instrument of Dissent');
insert into Obtains values(1,1,'Instrument of Tragedy');
insert into Obtains values(1,1,'Instrument of Flight');
insert into Obtains values(1,1,'Instrument of Vengeance');
insert into Obtains values(7,2,'Say Hello to My Little Friend');
insert into Obtains values(7,2,'Neighborhood Bully');
insert into Obtains values(7,2,'Engineer');
insert into Obtains values(7,2,'Moving on Up');
insert into Obtains values(7,2,'Second City');
insert into Obtains values(7,3,'Say Hello to My Little Friend');
insert into Obtains values(7,3,'Neighborhood Bully');
insert into Obtains values(7,3,'Engineer');
insert into Obtains values(7,3,'Moving on Up');
insert into Obtains values(7,3,'Second City');
insert into Obtains values(7,4,'Say Hello to My Little Friend');
insert into Obtains values(7,4,'Neighborhood Bully');
insert into Obtains values(7,4,'Engineer');
insert into Obtains values(7,4,'Moving on Up');
insert into Obtains values(7,4,'Second City');


