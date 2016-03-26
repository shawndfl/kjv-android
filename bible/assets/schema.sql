PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE android_metadata (locale TEXT);
INSERT INTO "android_metadata" VALUES('en_US');
CREATE TABLE "books" (
    "_id" INTEGER PRIMARY KEY,
    "name" TEXT,
    "chapterCount" INTEGER
);
INSERT INTO "books" VALUES(0,'Genesis',50);
INSERT INTO "books" VALUES(1,'Exodus',40);
INSERT INTO "books" VALUES(2,'Leviticus',27);
INSERT INTO "books" VALUES(3,'Numbers',36);
INSERT INTO "books" VALUES(4,'Deuteronomy',34);
INSERT INTO "books" VALUES(5,'Joshua',24);
INSERT INTO "books" VALUES(6,'Judges',21);
INSERT INTO "books" VALUES(7,'Ruth',4);
INSERT INTO "books" VALUES(8,'1 Samuel',31);
INSERT INTO "books" VALUES(9,'2 Samuel',24);
INSERT INTO "books" VALUES(10,'1 Kings',22);
INSERT INTO "books" VALUES(11,'2 Kings',25);
INSERT INTO "books" VALUES(12,'1 Chronicles',29);
INSERT INTO "books" VALUES(13,'2 Chronicles',36);
INSERT INTO "books" VALUES(14,'Ezra',10);
INSERT INTO "books" VALUES(15,'Nehemiah',13);
INSERT INTO "books" VALUES(16,'Esther',10);
INSERT INTO "books" VALUES(17,'Job',42);
INSERT INTO "books" VALUES(18,'Psalms',150);
INSERT INTO "books" VALUES(19,'Proverbs',31);
INSERT INTO "books" VALUES(20,'Ecclesiastes',12);
INSERT INTO "books" VALUES(21,'Song of Solomon',8);
INSERT INTO "books" VALUES(22,'Isaiah',66);
INSERT INTO "books" VALUES(23,'Jeremiah',52);
INSERT INTO "books" VALUES(24,'Lamentations',5);
INSERT INTO "books" VALUES(25,'Ezekiel',48);
INSERT INTO "books" VALUES(26,'Daniel',12);
INSERT INTO "books" VALUES(27,'Hosea',14);
INSERT INTO "books" VALUES(28,'Joel',3);
INSERT INTO "books" VALUES(29,'Amos',9);
INSERT INTO "books" VALUES(30,'Obadiah',1);
INSERT INTO "books" VALUES(31,'Jonah',4);
INSERT INTO "books" VALUES(32,'Micah',7);
INSERT INTO "books" VALUES(33,'Nahum',3);
INSERT INTO "books" VALUES(34,'Habakkuk',3);
INSERT INTO "books" VALUES(35,'Zephaniah',3);
INSERT INTO "books" VALUES(36,'Haggai',2);
INSERT INTO "books" VALUES(37,'Zechariah',14);
INSERT INTO "books" VALUES(38,'Malachi',4);
INSERT INTO "books" VALUES(39,'Matthew',28);
INSERT INTO "books" VALUES(40,'Mark',16);
INSERT INTO "books" VALUES(41,'Luke',24);
INSERT INTO "books" VALUES(42,'John',21);
INSERT INTO "books" VALUES(43,'Acts',28);
INSERT INTO "books" VALUES(44,'Romans',16);
INSERT INTO "books" VALUES(45,'1 Corinthians',16);
INSERT INTO "books" VALUES(46,'2 Corinthians',13);
INSERT INTO "books" VALUES(47,'Galatians',6);
INSERT INTO "books" VALUES(48,'Ephesians',6);
INSERT INTO "books" VALUES(49,'Philippians',4);
INSERT INTO "books" VALUES(50,'Colossians',4);
INSERT INTO "books" VALUES(51,'1 Thessalonians',5);
INSERT INTO "books" VALUES(52,'2 Thessalonians',3);
INSERT INTO "books" VALUES(53,'1 Timothy',6);
INSERT INTO "books" VALUES(54,'2 Timothy',4);
INSERT INTO "books" VALUES(55,'Titus',3);
INSERT INTO "books" VALUES(56,'Philemon',1);
INSERT INTO "books" VALUES(57,'Hebrews',13);
INSERT INTO "books" VALUES(58,'James',5);
INSERT INTO "books" VALUES(59,'1 Peter',5);
INSERT INTO "books" VALUES(60,'2 Peter',3);
INSERT INTO "books" VALUES(61,'1 John',5);
INSERT INTO "books" VALUES(62,'2 John',1);
INSERT INTO "books" VALUES(63,'3 John',1);
INSERT INTO "books" VALUES(64,'Jude',1);
INSERT INTO "books" VALUES(65,'Revelation',22);
CREATE TABLE BookLogs (
	_id INTEGER PRIMARY KEY,
	bookId INTEGER,
	chapter INTEGER,
	timeStamp DATETIME
);
CREATE TABLE "Highlight" (
	_id INTEGER PRIMARY KEY,
	bookId INTEGER,
	chapter INTEGER,
	verse INTEGER,
	state INTEGER,
	date DATETIME
 );

CREATE TABLE "properties" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "name" TEXT,
    "value" TEXT,
    "modify" DATETIME
);
CREATE TABLE "readstate" (
    "_id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "bookId" INT,
    "chapter" INT,
    "modify" DATETIME
);
COMMIT;
