-- MySQL dump 10.10
--
-- Host: localhost    Database: eloise_0_1_0_8
-- ------------------------------------------------------
-- Server version	5.0.22-Debian_0ubuntu6.06.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Application`
--

DROP TABLE IF EXISTS `Application`;
CREATE TABLE `Application` (
  `ApplicationID` varchar(50) NOT NULL default '',
  `Name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`ApplicationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Application`
--


/*!40000 ALTER TABLE `Application` DISABLE KEYS */;
LOCK TABLES `Application` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `Application` ENABLE KEYS */;

--
-- Table structure for table `ApplicationPermissionInstance`
--

DROP TABLE IF EXISTS `ApplicationPermissionInstance`;
CREATE TABLE `ApplicationPermissionInstance` (
  `ID` int(11) NOT NULL auto_increment,
  `OrganizationalRoleID` int(11) default NULL,
  `WorkItemRole` varchar(50) default NULL,
  `WorkItemRoleApplicationID` varchar(50) default NULL,
  `PermissionTypeID` varchar(50) default NULL,
  `PremissionTypeApplicationID` varchar(50) default NULL,
  `EntityType` varchar(50) default NULL,
  `EntitySuperType` varchar(50) default NULL,
  `UserID` varchar(255) default NULL,
  `ApplicationContext` varchar(50) default NULL,
  `EntityID` varchar(50) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK7669FCD4B24591C9` (`OrganizationalRoleID`),
  KEY `FK7669FCD44E8E225F` (`PermissionTypeID`,`PremissionTypeApplicationID`),
  KEY `FK7669FCD453D66A45` (`WorkItemRole`,`WorkItemRoleApplicationID`),
  KEY `FK7669FCD466C155BE` (`EntitySuperType`),
  KEY `FK7669FCD4C5BD1EC0` (`EntityType`),
  KEY `FK7669FCD4C2551F1B` (`UserID`),
  CONSTRAINT `FK7669FCD44E8E225F` FOREIGN KEY (`PermissionTypeID`, `PremissionTypeApplicationID`) REFERENCES `ApplicationPermissionType` (`ApplicationID`, `PermissionTypeID`),
  CONSTRAINT `FK7669FCD453D66A45` FOREIGN KEY (`WorkItemRole`, `WorkItemRoleApplicationID`) REFERENCES `WorkItemRoleType` (`WorkItemRole`, `ApplicationID`),
  CONSTRAINT `FK7669FCD466C155BE` FOREIGN KEY (`EntitySuperType`) REFERENCES `EntitySuperType` (`SuperTypeID`),
  CONSTRAINT `FK7669FCD4B24591C9` FOREIGN KEY (`OrganizationalRoleID`) REFERENCES `OrganizationalRole` (`OrganizationalRoleID`),
  CONSTRAINT `FK7669FCD4C2551F1B` FOREIGN KEY (`UserID`) REFERENCES `People` (`IDPeople`),
  CONSTRAINT `FK7669FCD4C5BD1EC0` FOREIGN KEY (`EntityType`) REFERENCES `EntityType` (`TypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ApplicationPermissionInstance`
--


/*!40000 ALTER TABLE `ApplicationPermissionInstance` DISABLE KEYS */;
LOCK TABLES `ApplicationPermissionInstance` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `ApplicationPermissionInstance` ENABLE KEYS */;

--
-- Table structure for table `ApplicationPermissionType`
--

DROP TABLE IF EXISTS `ApplicationPermissionType`;
CREATE TABLE `ApplicationPermissionType` (
  `ApplicationID` varchar(50) NOT NULL default '',
  `PermissionTypeID` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`ApplicationID`,`PermissionTypeID`),
  KEY `FKB3CDEEF978E6D915` (`ApplicationID`),
  CONSTRAINT `FKB3CDEEF978E6D915` FOREIGN KEY (`ApplicationID`) REFERENCES `Application` (`ApplicationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ApplicationPermissionType`
--


/*!40000 ALTER TABLE `ApplicationPermissionType` DISABLE KEYS */;
LOCK TABLES `ApplicationPermissionType` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `ApplicationPermissionType` ENABLE KEYS */;

--
-- Table structure for table `Area`
--

DROP TABLE IF EXISTS `Area`;
CREATE TABLE `Area` (
  `IDSettore` int(11) NOT NULL auto_increment,
  `visible` bit(1) default NULL,
  `IDEnte` int(11) NOT NULL,
  `Nome` varchar(255) default NULL,
  PRIMARY KEY  (`IDSettore`),
  KEY `FK1F44ADB7F25F8E` (`IDEnte`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Area`
--


/*!40000 ALTER TABLE `Area` DISABLE KEYS */;
LOCK TABLES `Area` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `Area` ENABLE KEYS */;

--
-- Table structure for table `Ente`
--

DROP TABLE IF EXISTS `Ente`;
CREATE TABLE `Ente` (
  `IDEnte` int(11) NOT NULL auto_increment,
  `Nome` varchar(255) default NULL,
  `visible` bit(1) default NULL,
  PRIMARY KEY  (`IDEnte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Ente`
--


/*!40000 ALTER TABLE `Ente` DISABLE KEYS */;
LOCK TABLES `Ente` WRITE;
INSERT INTO `Ente` VALUES (1,'Comune di Lastra a Signa',''),(2,'Servizi Esterni','');
UNLOCK TABLES;
/*!40000 ALTER TABLE `Ente` ENABLE KEYS */;

--
-- Table structure for table `EntitySuperType`
--

DROP TABLE IF EXISTS `EntitySuperType`;
CREATE TABLE `EntitySuperType` (
  `SuperTypeID` varchar(50) NOT NULL default '',
  `Name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`SuperTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `EntitySuperType`
--


/*!40000 ALTER TABLE `EntitySuperType` DISABLE KEYS */;
LOCK TABLES `EntitySuperType` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `EntitySuperType` ENABLE KEYS */;

--
-- Table structure for table `EntityType`
--

DROP TABLE IF EXISTS `EntityType`;
CREATE TABLE `EntityType` (
  `TypeID` varchar(50) NOT NULL default '',
  `SuperTypeID` varchar(50) NOT NULL default '',
  `Name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`TypeID`),
  KEY `FK1E6D19DD8D831E1C` (`SuperTypeID`),
  CONSTRAINT `FK1E6D19DD8D831E1C` FOREIGN KEY (`SuperTypeID`) REFERENCES `EntitySuperType` (`SuperTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `EntityType`
--


/*!40000 ALTER TABLE `EntityType` DISABLE KEYS */;
LOCK TABLES `EntityType` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `EntityType` ENABLE KEYS */;

--
-- Table structure for table `Orario`
--

DROP TABLE IF EXISTS `Orario`;
CREATE TABLE `Orario` (
  `IDOrario` int(11) NOT NULL auto_increment,
  `Descrizione` text,
  PRIMARY KEY  (`IDOrario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Orario`
--


/*!40000 ALTER TABLE `Orario` DISABLE KEYS */;
LOCK TABLES `Orario` WRITE;
INSERT INTO `Orario` VALUES (1,'Dal LunedÃ¬ al Sabato  8,30 -13,30; MartedÃ¬ e GiovedÃ¬  anche 15,15 -17,45'),(2,'MartedÃ¬ e GiovedÃ¬ 8,30 - 13,30 e 15,15 - 17,45'),(3,'Dal LunedÃ¬ al VenerdÃ¬ 8,30 - 13,30; MartedÃ¬ e GiovedÃ¬ anche 15,15 -17,45'),(4,'MartedÃ¬ e GiovedÃ¬ 8,30 -13,30 e 15,15 - 17,45'),(5,'MartedÃ¬ 8,30 -13,30 e 15,15 -17,45'),(6,'Dal MartedÃ¬ al VenerdÃ¬ 9 -19 non stop; sabato 9-13'),(7,'LunedÃ¬ e MercoledÃ¬ 8.30 -12,30; MartedÃ¬ 15-17; GiovedÃ¬ 15-18; VenerdÃ¬ su appuntamento'),(8,'MartedÃ¬ 15-18; VenerdÃ¬ 9,30 -12,30'),(9,'Su appuntamento'),(10,'LunedÃ¬,MercoledÃ¬ e VenerdÃ¬ 11-13; MartedÃ¬ e GiovedÃ¬ 18-20'),(11,''),(12,'MartedÃ¬ 9.30-12.30, GiovedÃ¬ 15-18'),(13,'Dal LunedÃ¬ al VenerdÃ¬ 9-12');
UNLOCK TABLES;
/*!40000 ALTER TABLE `Orario` ENABLE KEYS */;

--
-- Table structure for table `OrganizationalRole`
--

DROP TABLE IF EXISTS `OrganizationalRole`;
CREATE TABLE `OrganizationalRole` (
  `OrganizationalRoleID` int(11) NOT NULL auto_increment,
  `RoleTypeID` int(11) NOT NULL default '0',
  `IDUfficio` int(11) default NULL,
  `IDSettore` int(11) default NULL,
  `IDEnte` int(11) NOT NULL default '0',
  PRIMARY KEY  (`OrganizationalRoleID`),
  KEY `FK4B5FAA54FD639781` (`RoleTypeID`),
  KEY `FK4B5FAA54379C04DC` (`IDUfficio`),
  KEY `FK4B5FAA5435332048` (`IDSettore`),
  KEY `FK4B5FAA54B7F25F8E` (`IDEnte`),
  CONSTRAINT `FK4B5FAA5435332048` FOREIGN KEY (`IDSettore`) REFERENCES `Settore` (`IDSettore`),
  CONSTRAINT `FK4B5FAA54379C04DC` FOREIGN KEY (`IDUfficio`) REFERENCES `Ufficio` (`IDUfficio`),
  CONSTRAINT `FK4B5FAA54B7F25F8E` FOREIGN KEY (`IDEnte`) REFERENCES `Ente` (`IDEnte`),
  CONSTRAINT `FK4B5FAA54FD639781` FOREIGN KEY (`RoleTypeID`) REFERENCES `RoleType` (`RoleTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `OrganizationalRole`
--


/*!40000 ALTER TABLE `OrganizationalRole` DISABLE KEYS */;
LOCK TABLES `OrganizationalRole` WRITE;
INSERT INTO `OrganizationalRole` VALUES (1,1,NULL,4,1),(3,2,11,3,1),(4,2,13,8,1),(5,1,NULL,3,1),(6,1,NULL,7,1),(7,2,21,7,1),(8,2,12,8,1),(9,3,NULL,NULL,1),(10,2,15,5,1),(11,1,NULL,5,1),(12,2,16,5,1),(13,2,NULL,1,1),(14,2,14,4,1),(15,2,7,3,1),(16,2,8,3,1),(18,2,6,3,1),(19,2,NULL,8,1),(20,2,17,6,1),(21,2,10,3,1),(22,2,9,3,1),(23,2,22,8,1),(25,2,1,1,1),(26,2,2,1,1),(27,2,19,6,1),(28,4,NULL,NULL,1),(29,1,NULL,6,1),(30,1,NULL,8,1),(31,1,NULL,1,1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `OrganizationalRole` ENABLE KEYS */;

--
-- Table structure for table `People`
--

DROP TABLE IF EXISTS `People`;
CREATE TABLE `People` (
  `IDPeople` varchar(255) NOT NULL default '',
  `IDSede` int(11) default NULL,
  `IDOrario` int(11) default NULL,
  `Nome` varchar(64) default NULL,
  `Cognome` varchar(255) default NULL,
  `Password` varchar(255) default NULL,
  `Telefono` varchar(20) default NULL,
  `Fax` varchar(20) default NULL,
  `Email` varchar(255) default NULL,
  `ProfiloProfessionale` text,
  `TelefonoPrivato` varchar(20) default NULL,
  `Fax1` varchar(20) default NULL,
  `Email1` varchar(255) default NULL,
  `Cellulare` varchar(20) default NULL,
  `NumeroBreve` varchar(20) default NULL,
  `visible` bit(1) default NULL,
  PRIMARY KEY  (`IDPeople`),
  KEY `FK8E471EAFB8850FCF` (`IDSede`),
  KEY `FK8E471EAF16C8D730` (`IDOrario`),
  CONSTRAINT `FK8E471EAF16C8D730` FOREIGN KEY (`IDOrario`) REFERENCES `Orario` (`IDOrario`),
  CONSTRAINT `FK8E471EAFB8850FCF` FOREIGN KEY (`IDSede`) REFERENCES `Sede` (`IDSede`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `People`
--


/*!40000 ALTER TABLE `People` DISABLE KEYS */;
LOCK TABLES `People` WRITE;
INSERT INTO `People` VALUES ('abelli',NULL,11,'Annalisa','Belli',NULL,'055 32 70 118','055 32 70 115','annalisa.belli@comune.lastra-a-signa.fi.it','','','','pubblicaistruzione@comune.lastra-a-signa.fi.it','','',''),('abercigli',NULL,NULL,'Alessandra','Bercigli',NULL,'055 87 43 218','055 87 43 215','tributi@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('aboldrini',NULL,11,'Anna','Boldrini',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('abuffa',1,12,'Andrea','Buffa',NULL,'055 8743610','055 8722846','immigrazione@comune.lastra-a-signa.fi.it','','','','','','',''),('agiani',NULL,11,'Antonella','Giani',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('aguidi',NULL,11,'Andrea','Guidi',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('apettinicchio',NULL,NULL,'Antonio','Pettinicchio',NULL,'055 8743 284','055 87 43 257','mobilit@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('apinzi',NULL,11,'Angela','Pinzi',NULL,'055 87 43 258','055 87 43 270','urbanistica@comune.lastra-a-signa.fi.it','','','','','','',''),('asalvadori',NULL,11,'Angela','Salvadori',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('asantoro',NULL,11,'Assunta','Santoro',NULL,'055 3270112','','','','','','','','',''),('avannuzzi_innocenti',NULL,11,'Alessandra','Vannuzzi Innocenti',NULL,'055 87 43 238','055 87 43 215','alessandra.vannuzzi@comune.lastra-a-signa.fi.it','','','','tributi@comune.lastra-a-signa.fi.it','','',''),('avassallo',NULL,NULL,'Anna','Vassallo',NULL,'055 32 70 124','055 32 70 125','biblioteca@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('cbaccetti',6,9,'Cesare','Baccetti',NULL,'055 32 70 111','055 32 70 115','cesare.baccetti@comune.lastra-a-signa.fi.it','','055 32 70 110','','pubblicaistruzione@comune.lastra-a-signa.fi.it','348 52 74 213','323',''),('cdegl\'innocenti',NULL,11,'Clara','Degl\'Innocenti',NULL,'055 87 28 147','055 87 28 147','Ufficioassociatopensioni@comune.lastra-a-signa.fi.it','','','','','','',''),('cgallerini',NULL,11,'Cristina','Gallerini',NULL,'','','','','','','','','',''),('cgrimaldi',NULL,11,'Corrado','Grimaldi',NULL,'055 87 43 276','055 87 43 252','segretario@comune.lastra-a-signa.fi.it','','055 87 43 202','','','335 78 98 206','600',''),('cpancani',NULL,11,'Cristina','Pancani',NULL,'055 87 43 206','055 87 43 270','cristina.pancani@comune.lastra-a-signa.fi.it','','','','suapambiEnte@comune.lastra-a-signa.fi.it','','',''),('cpassaponti',NULL,11,'Chiara','Passaponti',NULL,'055 87 43 288','055 87 43 205','servizi.generali@comune.lastra-a-signa.fi.it','','','','','','',''),('cpoggi',NULL,11,'Carlo','Poggi',NULL,'055 87 43 251','055 87 43 239','carlo.poggi@comune.lastra-a-signa.fi.it','','055 87 43 208','','anagrafe@comune.lastra-a-signa.fi.it','348 08 37 391','676',''),('csetti',NULL,11,'Chiara','Setti',NULL,'055 87 43 204','055 87 43 270','edilizia@comune.lastra-a-signa.fi.it','','','','','','',''),('dbagnoli',NULL,11,'Davide','Bagnoli',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('dcicchiello',NULL,11,'Daniela','Cicchiello',NULL,'055 87 43 246','055 87 43 257','','','','','','','',''),('dguarducci',NULL,NULL,'Daniel','Guarducci',NULL,'055 87 43 277','055 87 43 286','ced@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,'348 04 33 649','636',''),('eciatti',NULL,11,'Elena','Ciatti',NULL,'055 87 43 266','055 87 43 212','elena.ciatti@comune.lastra-a-signa.fi.it','','','','ragioneria@comune.lastra-a-signa.fi.it','','',''),('efalleni',NULL,11,'Elena','Falleni',NULL,'055 32 70 117','055 32 70 115','elena.falleni@comune.lastra-a-signa.fi.it','','','','serviziosociale@comune.lastra-a-signa.fi.it','','',''),('efancelli',NULL,NULL,'Elena','Fancelli',NULL,'055 32 70 120','055 32 70 115','serviziosociale@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('egallo',NULL,11,'Enrico','Gallo',NULL,'055 87 27 284','055 32 70 115','scuolabus@comune.lastra-a-signa.fi.it','','','','','338 85 40 289','','\0'),('emegli',NULL,11,'Enzo','Megli',NULL,'055 87 43 293','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','',''),('esansone',NULL,11,'Emanuela','Sansone',NULL,'055 87 43 251','055 87 43 205','servizi.generali@comune.lastra-a-signa.fi.it','','','','','','',''),('fbagni',NULL,11,'Fabio','Bagni',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('fbattaglia',NULL,NULL,'Francesca','Battaglia',NULL,'055 87 43 289','055 87 43 239','anagrafe@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('fbini',NULL,11,'Fabiola','Bini',NULL,'055 32 70 124','055 32 70 125','fabiola.bini@comune.lastra-a-signa.fi.it','','','','biblioteca@comune.lastra-a-signa.fi.it','','',''),('fcianchi',NULL,11,'Fabio','Cianchi',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('fciccarello',6,9,'Francesca','Ciccarello',NULL,'055 3270120','','','','055 3270195','','','','',''),('fd\'ambrosi',NULL,NULL,'Filomena','D\'Ambrosi',NULL,'055 87 43 279','055 87 43 286','urp@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('ffilippini',NULL,11,'Franco','Filippini',NULL,'055 87 43 269','055 87 43 270','franco.filippini@comune.lastra-a-signa.fi.it','','','','urbanistica@comune.lastra-a-signa.fi.it','','',''),('fgiachetti',NULL,11,'Fabio','Giachetti',NULL,'055 87 27 284','055 32 70 115','scuolabus@comune.lastra-a-signa.fi.it','','','','','338 47 61 363','','\0'),('fgiusti',NULL,NULL,'Franca','Giusti',NULL,'055 87 43 213','055 87 43 212','personale@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('forsi',6,9,'Fabiana','Orsi',NULL,'055 3270119','','serviziosociale@comune.lastra-a-signa.fi.it','','055 3270195','','','','',''),('fpadariso',NULL,11,'Fernando','Padariso',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','348 52 74 215','324','\0'),('fpalumbo',NULL,NULL,'Francesco','Palumbo',NULL,'055 87 43 240','055 87 43 239','anagrafe@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('fpieragnoli',NULL,11,'Francesco','Pieragnoli',NULL,'055 87 43 293','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','',''),('fpoggi',1,9,'Fabrizio','Poggi',NULL,'055 87 43 232','055 87 43 265','Ufficiostampa@comune.lastra-a-signa.fi.it','','','','','338 63 15 041','',''),('frugi',NULL,11,'Franco','Rugi',NULL,'055 87 43 263','055 87 43 431','franco.rugi@comune.lastra-a-signa.fi.it','','','','poliziamunicipale@comune.lastra-a-signa.fi.it','','',''),('ftamburri',NULL,NULL,'Filippo','Tamburri',NULL,'055 87 43 297','055 87 43 270','suapambiEnte@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('ftofanari',NULL,11,'Fabrizio','Tofanari',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','348 52 74 216','325','\0'),('gbaldanzini',NULL,11,'Giulio','Baldanzini',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','348 52 74 218','327','\0'),('gciabatti',NULL,NULL,'Gianna','Ciabatti',NULL,'055 87 43 210','055 87 43 252','segreteria@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('ggrassi',NULL,11,'Giovanni','Grassi',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('gguarnieri',NULL,NULL,'Giovanna','Guarnieri',NULL,'055 87 43 227','055 87 43 286','economato@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('glicata_tissi',NULL,11,'Giovanni','Licata Tissi',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('gligrone',6,11,'Giovanna','Ligrone',NULL,'055 32 72 117','055 32 70 115','serviziosociale@comune.lastra-a-signa.fi.it','','','','','','',''),('gmisuri',NULL,11,'Giacomo','Misuri',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('gmomigli',4,11,'Giovanna','Momigli',NULL,'055 32 70 121','055 32 70 126','centro.sociale@comune.lastra-a-signa.fi.it','','','','','','',''),('grusso',NULL,11,'Giovanni','Russo',NULL,'055 87 27 284','055 32 70 115','scuolabus@comune.lastra-a-signa.fi.it','','','','','339 22 28 605','','\0'),('gtaccetti',NULL,11,'Giovanni','Taccetti',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('gzarroli',NULL,NULL,'Giulietta','Zarroli',NULL,'055 87 43 290','055 87 43 239','anagrafe@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('kmeini',NULL,11,'Katuscia','Meini',NULL,'055 87 43 204','055 87 43  270','urbanistica@comune.lastra-a-signa.fi.it','','','','','','',''),('kmolnar',NULL,11,'Katalin','Molnar',NULL,'','','','','','','','','',''),('lbetti',8,9,'Luca','Betti',NULL,'055 87 43 267','055 87 43 257','luca.betti@comune.lastra-a-signa.fi.it','','055 87 43 233','','lavoripubblici@comune.lastra-a-signa.fi.it','348 52 74 206','316',''),('lbianchi',1,1,'Liana','Bianchi',NULL,'055 87 43 611','055 87 22 946','','','','','','','',''),('lbimbi',NULL,11,'Luciano','Bimbi',NULL,'055 87 43 294','055 87 43 280','luciano.bimbi@comune.lastra-a-signa.fi.it','','','','urp@comune.lastra-a-signa.fi.it','339 32 59 647','601',''),('ldiegoli',NULL,11,'Loredana','Diegoli',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('lpancini',NULL,11,'Laura','Pancini',NULL,'055 87 43 276','055 87 43 252','laura.pancini@comune.lastra-a-signa.fi.it','','','','','','',''),('lpucci',NULL,NULL,'Luciano','Pucci',NULL,'055 87 43 209','055 87 43 286','urp@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('ltomaiuolo',NULL,11,'Lucia','Tomaiuolo',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('lvenditti',NULL,NULL,'Loreto','Venditti',NULL,'055 87 43 278','055 87 43 212','ragioneria@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('maidalenzi',4,11,'Maida','Lenzi',NULL,'055 32 70 121','055 32 70 126','centro.sociale@comune.lastra-a-signa.fi.it','','','','','','',''),('mbertini',NULL,11,'Michela','Bertini',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('mcaciagli',NULL,11,'Manuela','Caciagli',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('mcapecchi',1,9,'Marco','Capecchi',NULL,'055 87 43 217','055 87 43 216','marco.capecchi@comune.lastra-a-signa.fi.it','','055 8743203','','direttoregenerale@comune.lastra-a-signa.fi.it','329 41 06 132','602',''),('mcini',NULL,11,'Marco','Cini',NULL,'055 87 43 243','055 87 43 212','marco.cini@comune.lastra-a-signa.fi.it','','','','','','',''),('mcorzi',NULL,NULL,'Manuela','Corzi',NULL,'055 87 43 213','055 87 43 212','personale@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('mcusano',NULL,NULL,'Maria Paola','Cusano',NULL,'055 32 70 113','055 32 70 115','pubblicaistruzione@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('mdonati',NULL,11,'Mariagrazia','Donati',NULL,'055 87 21 786','055 87 25 750','dietista@comune.lastra-a-signa.fi.it','','','','','','','\0'),('mgargiulo',NULL,11,'Marina','Gargiulo',NULL,'055 87 43 268','055 87 43 270','marina.gargiulo@comune.lastra-a-signa.fi.it','','','','urbanistica@comune.lastra-a-signa.fi.it','','',''),('mgeri',NULL,11,'Marco','Geri',NULL,'055 87 43 281','055 87 43 216','marco.geri@comune.lastra-a-signa.fi.it','','','','segreteriasindaco@comune.lastra-a-signa.fi.it','','',''),('milvalenzi',NULL,11,'Milva','Lenzi',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('mlo_russo',NULL,NULL,'Mirella','Lo Russo',NULL,'055 87 43 211','055 87 43 252','segreteria@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('mmadiai',NULL,11,'Marina','Madiai',NULL,'055 8743224','055 8743212','','','','','','','',''),('mmani',NULL,11,'Massimiliano','Mani',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','348 52 74 216','325','\0'),('mmariotti',NULL,11,'Mauro','Mariotti',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','348 04 33 647','634','\0'),('mmasi',NULL,11,'Massimo','Masi',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('mpiscopo',1,7,'Maria Ausilia','Piscopo',NULL,'055 8743221','055 8722946','sol@comune.lastra-a-signa.fi.it','','','','','','',''),('mpratesi',4,11,'Miria','Pratesi',NULL,'055 32 70 121','055 32 70 126','centro.sociale@comune.lastra-a-signa.fi.it','','','','','','',''),('mserafini',NULL,11,'Marco','Serafini',NULL,'055 87 43 225','055 87 43 257','lavoripubblici@comune.lastra-a-signa.fi.it','','','','','348 52 74 207','317',''),('msicilia',NULL,NULL,'Marilena','Sicilia',NULL,'055 32 70 114','055 32 70 115','pubblicaistruzione@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('ncolzi',NULL,11,'Narrima','Colzi',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('nde_martino',7,11,'Nello','De Martino',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','340 91 91 145','630',''),('pbiancalani',NULL,11,'Piera','Biancalani',NULL,'055 87 23 668','055 87 23 668','asilocaci@comune.lastra-a-signa.fi.it','','','','','','','\0'),('pmomigli',NULL,11,'Paola','Momigli',NULL,'055 87 43 267','055 87 43 257','lavoripubblici@comune.lastra-a-signa.fi.it','','','','','','',''),('ppancani',NULL,11,'Patrizia','Pancani',NULL,'055 87 43 231','055 87 43 257','patrizia.pancani@comune.lastra-a-signa.fi.it','','','','lavoripubblici@comune.lastra-a-signa.fi.it','','',''),('proselli',NULL,11,'Paolo','Roselli',NULL,'055 87 43 262','055 87 43 431','paolo.roselli@comune.lastra-a-signa.fi.it','','','','poliziamunicipale@comune.lastra-a-signa.fi.it','348 52 74 317','326',''),('ptempestini',NULL,11,'Patrizia','Tempestini',NULL,'055 87 43 255','055 87 43 270','urbanistica@comune.lastra-a-signa.fi.it','','','','','','',''),('rcardini',NULL,11,'Roberto','Cardini',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('rcecconi',NULL,11,'Rinaldo','Cecconi',NULL,'055 87 21 783','055 87 22 230','info@villacaruso.it','','','','','','','\0'),('rdelvecchio',2,9,'Rosa','Delvecchio',NULL,'055 87 43 261','055 87 43 431','rosa.delvecchio@comune.lastra-a-signa.fi.it','','055 8743260','','poliziamunicipale@comune.lastra-a-signa.fi.it','348 08 37 372','675',''),('rfancelli',NULL,11,'Roberto','Fancelli',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('riachetti',NULL,11,'Renato','Iachetti',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','',''),('riannuzzi',NULL,11,'Rocco','Iannuzzi',NULL,'055 87 27 284','055 32 70 115','scuolabus@comune.lastra-a-signa.fi.it','','','','','339 22 28 320','','\0'),('rsquilloni',NULL,NULL,'Renzo','Squilloni',NULL,'055 87 43 217','055 87 43 216','segreteriasindaco@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,'348 52 74 199','329',''),('sbaldassini',NULL,11,'Sauro','Baldassini',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','348 36 14 362','681','\0'),('sbasoli',10,10,'Silvia','Bazoli',NULL,'055 8725770','055 8727933','Ufficioturistico@comune.lastra-a-signa.fi.it','','','','','','',''),('sbitossi',NULL,NULL,'Susanna','Bitossi',NULL,'055 87 43 222','055 87 43 270','suapambiEnte@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('scaverni',NULL,11,'Serena','Caverni',NULL,'055 87 43 278','055 87 43 212','serena.caverni@comune.lastra-a-signa.fi.it','','055 87 43 264','','ragioneria@comune.lastra-a-signa.fi.it','348 52 74 203','313',''),('sfrangioni',4,11,'Silvia','Frangioni',NULL,'055 32 70 121','055 32 70126','centro.sociale@comune.lastra-a-signa.fi.it','','','','','','',''),('sgalletti',NULL,NULL,'Stefania','Galletti',NULL,'055 87 43 215','055 87 43 215','servizi.generali@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('sgiovannini',NULL,11,'Stefano','Giovannini',NULL,'055 87 43 250','055 87 43 270','stefano.giovannini@comune.lastra-a-signa.fi.it','','','','suapambiEnte@comune.lastra-a-signa.fi.it','348 52 74 219','328',''),('smariani',NULL,11,'Sandra','Mariani',NULL,'055 87 43 261','055 87 43 431','poliziamunicipale@comune.lastra-a-signa.fi.it','','','','','','','\0'),('smarlazzi',NULL,11,'Sergio','Marlazzi',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('snicolosi',NULL,11,'Sebastiano','Nicolosi',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('spalli',NULL,NULL,'Susanna','Palli',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,'348 85 86 455','333',''),('staddei',1,9,'Susanna','Taddei',NULL,'055 8743245','055 8722946','susanna.taddei@comune.lastra-a-signa.fi.it','','','','pianificazione@comune.lastra-a-signa.fi.it','','',''),('svendramin',NULL,NULL,'Silvia','Vendramin',NULL,'055 87 43 223','055 87 43 286','urp@comune.lastra-a-signa.fi.it',NULL,NULL,NULL,NULL,NULL,NULL,''),('tgatta',NULL,11,'Tobia','Gatta',NULL,'055 87 43 298','055 87 23 038','manutenzione@comune.lastra-a-signa.fi.it','','','','','','','\0'),('vmancini',NULL,11,'Valerio','Mancini',NULL,'055 87 27 284','055 32 70 115','scuolabus@comune.lastra-a-signa.fi.it','','','','','338 41 95 195','','\0');
UNLOCK TABLES;
/*!40000 ALTER TABLE `People` ENABLE KEYS */;

--
-- Table structure for table `People2Ente`
--

DROP TABLE IF EXISTS `People2Ente`;
CREATE TABLE `People2Ente` (
  `IDEnte` int(11) NOT NULL default '0',
  `IDPeople` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`IDPeople`,`IDEnte`),
  KEY `FKA4FD56DDFE56AC5F` (`IDPeople`),
  KEY `FKA4FD56DDB7F25F8E` (`IDEnte`),
  CONSTRAINT `FKA4FD56DDB7F25F8E` FOREIGN KEY (`IDEnte`) REFERENCES `Ente` (`IDEnte`),
  CONSTRAINT `FKA4FD56DDFE56AC5F` FOREIGN KEY (`IDPeople`) REFERENCES `People` (`IDPeople`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `People2Ente`
--


/*!40000 ALTER TABLE `People2Ente` DISABLE KEYS */;
LOCK TABLES `People2Ente` WRITE;
INSERT INTO `People2Ente` VALUES (1,'mcapecchi');
UNLOCK TABLES;
/*!40000 ALTER TABLE `People2Ente` ENABLE KEYS */;

--
-- Table structure for table `People2OrganizationalRole`
--

DROP TABLE IF EXISTS `People2OrganizationalRole`;
CREATE TABLE `People2OrganizationalRole` (
  `IDPeople` varchar(255) NOT NULL default '',
  `OrganizationalRoleID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`IDPeople`,`OrganizationalRoleID`),
  KEY `FKEBC06EF7B24591C9` (`OrganizationalRoleID`),
  KEY `FKEBC06EF7FE56AC5F` (`IDPeople`),
  CONSTRAINT `FKEBC06EF7B24591C9` FOREIGN KEY (`OrganizationalRoleID`) REFERENCES `OrganizationalRole` (`OrganizationalRoleID`),
  CONSTRAINT `FKEBC06EF7FE56AC5F` FOREIGN KEY (`IDPeople`) REFERENCES `People` (`IDPeople`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `People2OrganizationalRole`
--


/*!40000 ALTER TABLE `People2OrganizationalRole` DISABLE KEYS */;
LOCK TABLES `People2OrganizationalRole` WRITE;
INSERT INTO `People2OrganizationalRole` VALUES ('cbaccetti',1),('abelli',4),('lbetti',5),('lbimbi',6),('lbimbi',7),('fbini',8),('mcapecchi',9),('eciatti',10),('scaverni',11),('efalleni',14),('ffilippini',15),('mgargiulo',16),('sgiovannini',18),('cpancani',21),('ppancani',22),('lpancini',23),('proselli',25),('frugi',26),('avannuzzi_innocenti',27),('fpoggi',28),('cpoggi',29),('cgrimaldi',30),('rdelvecchio',31);
UNLOCK TABLES;
/*!40000 ALTER TABLE `People2OrganizationalRole` ENABLE KEYS */;

--
-- Table structure for table `People2Settore`
--

DROP TABLE IF EXISTS `People2Settore`;
CREATE TABLE `People2Settore` (
  `IDSettore` int(11) NOT NULL default '0',
  `IDPeople` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`IDPeople`,`IDSettore`),
  KEY `FKDAA3806D35332048` (`IDSettore`),
  KEY `FKDAA3806DFE56AC5F` (`IDPeople`),
  CONSTRAINT `FKDAA3806D35332048` FOREIGN KEY (`IDSettore`) REFERENCES `Settore` (`IDSettore`),
  CONSTRAINT `FKDAA3806DFE56AC5F` FOREIGN KEY (`IDPeople`) REFERENCES `People` (`IDPeople`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `People2Settore`
--


/*!40000 ALTER TABLE `People2Settore` DISABLE KEYS */;
LOCK TABLES `People2Settore` WRITE;
INSERT INTO `People2Settore` VALUES (1,'dbagnoli'),(1,'emegli'),(1,'fcianchi'),(1,'fpieragnoli'),(1,'ltomaiuolo'),(1,'mmasi'),(1,'rcardini'),(1,'rdelvecchio'),(1,'riachetti'),(1,'sbaldassini'),(1,'smariani'),(3,'lbetti'),(4,'cbaccetti'),(5,'scaverni'),(6,'cpoggi'),(7,'lbimbi'),(8,'cgrimaldi');
UNLOCK TABLES;
/*!40000 ALTER TABLE `People2Settore` ENABLE KEYS */;

--
-- Table structure for table `People2Ufficio`
--

DROP TABLE IF EXISTS `People2Ufficio`;
CREATE TABLE `People2Ufficio` (
  `IDUfficio` int(11) NOT NULL default '0',
  `IDPeople` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`IDPeople`,`IDUfficio`),
  KEY `FK455A5852379C04DC` (`IDUfficio`),
  KEY `FK455A5852FE56AC5F` (`IDPeople`),
  CONSTRAINT `FK455A5852379C04DC` FOREIGN KEY (`IDUfficio`) REFERENCES `Ufficio` (`IDUfficio`),
  CONSTRAINT `FK455A5852FE56AC5F` FOREIGN KEY (`IDPeople`) REFERENCES `People` (`IDPeople`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `People2Ufficio`
--


/*!40000 ALTER TABLE `People2Ufficio` DISABLE KEYS */;
LOCK TABLES `People2Ufficio` WRITE;
INSERT INTO `People2Ufficio` VALUES (1,'proselli'),(2,'frugi'),(4,'mgeri'),(4,'rsquilloni'),(6,'ftamburri'),(6,'sgiovannini'),(7,'apinzi'),(7,'csetti'),(7,'ffilippini'),(7,'kmeini'),(7,'kmolnar'),(7,'ptempestini'),(8,'mgargiulo'),(8,'ptempestini'),(9,'dcicchiello'),(9,'pmomigli'),(9,'ppancani'),(10,'cpancani'),(10,'sbitossi'),(11,'apettinicchio'),(11,'fbagni'),(11,'fpadariso'),(11,'ftofanari'),(11,'gbaldanzini'),(11,'ggrassi'),(11,'glicata_tissi'),(11,'gmisuri'),(11,'gtaccetti'),(11,'ldiegoli'),(11,'mmani'),(11,'mmariotti'),(11,'mserafini'),(11,'nde_martino'),(11,'smarlazzi'),(11,'snicolosi'),(11,'spalli'),(11,'tgatta'),(12,'avassallo'),(12,'cgallerini'),(12,'fbini'),(13,'abelli'),(13,'aboldrini'),(13,'agiani'),(13,'asalvadori'),(13,'asantoro'),(13,'egallo'),(13,'fgiachetti'),(13,'fpoggi'),(13,'grusso'),(13,'mbertini'),(13,'mcaciagli'),(13,'mcusano'),(13,'mdonati'),(13,'milvalenzi'),(13,'msicilia'),(13,'ncolzi'),(13,'pbiancalani'),(13,'rcecconi'),(13,'riannuzzi'),(13,'vmancini'),(14,'efalleni'),(14,'efancelli'),(14,'fciccarello'),(14,'forsi'),(14,'gligrone'),(14,'gmomigli'),(14,'maidalenzi'),(14,'mpratesi'),(14,'sfrangioni'),(15,'eciatti'),(15,'gguarnieri'),(15,'lvenditti'),(15,'mmadiai'),(16,'fgiusti'),(16,'mcorzi'),(17,'fbattaglia'),(17,'fpalumbo'),(17,'gzarroli'),(18,'cpassaponti'),(18,'esansone'),(18,'sgalletti'),(19,'abercigli'),(19,'avannuzzi_innocenti'),(20,'dguarducci'),(21,'fd\'ambrosi'),(21,'lbianchi'),(21,'lpucci'),(21,'svendramin'),(22,'gciabatti'),(22,'lpancini'),(22,'mlo_russo'),(23,'staddei'),(24,'cdegl\'innocenti'),(25,'mcini'),(27,'abuffa'),(28,'sbasoli'),(29,'mpiscopo');
UNLOCK TABLES;
/*!40000 ALTER TABLE `People2Ufficio` ENABLE KEYS */;

--
-- Table structure for table `RoleType`
--

DROP TABLE IF EXISTS `RoleType`;
CREATE TABLE `RoleType` (
  `RoleTypeID` int(11) NOT NULL auto_increment,
  `Role` varchar(255) default NULL,
  PRIMARY KEY  (`RoleTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `RoleType`
--


/*!40000 ALTER TABLE `RoleType` DISABLE KEYS */;
LOCK TABLES `RoleType` WRITE;
INSERT INTO `RoleType` VALUES (1,'Responsabile Area'),(2,'Responsabile Servizio'),(3,'Direttore Generale'),(4,'Addetto Stampa');
UNLOCK TABLES;
/*!40000 ALTER TABLE `RoleType` ENABLE KEYS */;

--
-- Table structure for table `Sede`
--

DROP TABLE IF EXISTS `Sede`;
CREATE TABLE `Sede` (
  `IDSede` int(11) NOT NULL auto_increment,
  `Descrizione` text,
  PRIMARY KEY  (`IDSede`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Sede`
--


/*!40000 ALTER TABLE `Sede` DISABLE KEYS */;
LOCK TABLES `Sede` WRITE;
INSERT INTO `Sede` VALUES (1,'Piazza del Comune 17'),(2,'Piazza del Comune 11'),(4,'Via P.Togliatti 37'),(5,'Via del Leccio 1'),(6,'Via P.Togliatti 41'),(7,'Via del Piano 1'),(8,'Via dell\'Arione 9'),(9,'Comune di Scandicci'),(10,'Via L.Cadorna 1'),(11,'Via A.Gramsci 13');
UNLOCK TABLES;
/*!40000 ALTER TABLE `Sede` ENABLE KEYS */;

--
-- Table structure for table `Settore`
--

DROP TABLE IF EXISTS `Settore`;
CREATE TABLE `Settore` (
  `IDSettore` int(11) NOT NULL auto_increment,
  `IDEnte` int(11) NOT NULL default '0',
  `Nome` varchar(255) default NULL,
  `visible` bit(1) default NULL,
  PRIMARY KEY  (`IDSettore`),
  KEY `FKD997BD30B7F25F8E` (`IDEnte`),
  CONSTRAINT `FKD997BD30B7F25F8E` FOREIGN KEY (`IDEnte`) REFERENCES `Ente` (`IDEnte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Settore`
--


/*!40000 ALTER TABLE `Settore` DISABLE KEYS */;
LOCK TABLES `Settore` WRITE;
INSERT INTO `Settore` VALUES (1,1,'Staff - Polizia Municipale',''),(3,1,'Area 1 - Servizi tecnici ed al territorio',''),(4,1,'Area 2 - Servizi alla persona',''),(5,1,'Area 3 - Area Risorse',''),(6,1,'Area 4 - Servizi Demografici, servizio entrate e servizi generali',''),(7,1,'Area 5 - URP',''),(8,1,'Area 6 - Affari istituzionali, legali e contrattuali',''),(14,2,'Sportelli',''),(15,2,'Uffici','');
UNLOCK TABLES;
/*!40000 ALTER TABLE `Settore` ENABLE KEYS */;

--
-- Table structure for table `Ufficio`
--

DROP TABLE IF EXISTS `Ufficio`;
CREATE TABLE `Ufficio` (
  `IDUfficio` int(11) NOT NULL auto_increment,
  `IDSede` int(11) NOT NULL default '0',
  `IDSettore` int(11) NOT NULL default '0',
  `IDOrario` int(11) NOT NULL default '0',
  `Descrizione` text,
  `Telefono` varchar(255) default NULL,
  `Fax` varchar(255) default NULL,
  `EMail` varchar(255) default NULL,
  `Nome` varchar(255) default NULL,
  `Fax1` varchar(255) default NULL,
  `visible` bit(1) default NULL,
  PRIMARY KEY  (`IDUfficio`),
  KEY `FK444E9515B8850FCF` (`IDSede`),
  KEY `FK444E951535332048` (`IDSettore`),
  KEY `FK444E951516C8D730` (`IDOrario`),
  CONSTRAINT `FK444E951516C8D730` FOREIGN KEY (`IDOrario`) REFERENCES `Orario` (`IDOrario`),
  CONSTRAINT `FK444E951535332048` FOREIGN KEY (`IDSettore`) REFERENCES `Settore` (`IDSettore`),
  CONSTRAINT `FK444E9515B8850FCF` FOREIGN KEY (`IDSede`) REFERENCES `Sede` (`IDSede`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Ufficio`
--


/*!40000 ALTER TABLE `Ufficio` DISABLE KEYS */;
LOCK TABLES `Ufficio` WRITE;
INSERT INTO `Ufficio` VALUES (1,2,1,3,'','055 8720018','055 8743431','poliziamunicipale@comune.lastra-a-signa.fi.it','Polizia Amministrativa e Giudiziaria, Informazioni e Notifiche','055 8722946',''),(2,2,1,3,'','055 8720018','055 8743431','poliziamunicipale@comune.lastra-a-signa.fi.it','Controllo del territorio e viabilitÃ ','055 8722946',''),(4,1,7,4,'','055 8743281','055 8743216','segreteriasindaco@comune.lastra-a-signa.fi.it','Segreteria del sindaco','055 8722946',''),(6,8,3,2,'','055 8743297','055 8743270','suapambiente@comune.lastra-a-signa.fi.it','Ambiente','055 8743257',''),(7,8,3,4,'','055 8743255','055 8743270','edilizia@comune.lastra-a-signa.fi.it','Edilizia privata','055 8743257',''),(8,8,3,5,'','055 8743268','055 8743270','urbanistica@comune.lastra-a-signa.fi.it','Urbanistica','055 8743257',''),(9,8,3,4,'','055 8743246','055 8743257','lavoripubblici@comune.lastra-a-signa.fi.it','Servizio amministrativo per servizio tecnico','055 8743270',''),(10,8,3,4,'','055 8743222','055 8743270','suapambiEnte@comune.lastra-a-signa.fi.it','SUAP','055 8743257',''),(11,7,3,11,'','055 8743267','055 8743257','lavoripubblici@comune.lastra-a-signa.fi.it','Servizio tecnico','',''),(12,4,4,6,'','055 3270111','055 3270125','biblioteca@comune.lastra-a-signa.fi.it','AttivitÃ Â  culturali -  Biblioteca','055 3270126',''),(13,6,4,4,'','055 3270111','055 3270115','pubblicaistruzione@comune.lastra-a-signa.fi.it','Servizi educativi','055 3270125',''),(14,6,4,4,'','055 3270117','055 3270115','serviziosociale@comune.lastra-a-signa.fi.it','Servizi sociali','055 3270126',''),(15,1,5,4,'','055 8743278','055 8743212','ragioneria@comune.lastra-a-signa.fi.it','Ragioneria','055 8722946',''),(16,1,5,4,'','055 8743243','055 8743212','personale@comune.lastra-a-signa.fi.it','Risorse umane','055 8722946',''),(17,1,6,3,'','055 8743240','055 8743239','anagrafe@comune.lastra-a-signa.fi.it','Servizi Demografici','055 8722946',''),(18,1,6,4,'','055 8743251','055 8743205','servizi.generali@comune.lastra-a-signa.fi.it','Servizi Generali - Area 4','055 8722946',''),(19,1,6,4,'','055 8743238','055 8743215','tributi@comune.lastra-a-signa.fi.it','Entrate e tributi','055 8722946',''),(20,1,7,9,'','055 8743277','055 8722946','ced@comune.lastra-a-signa.fi.it','Servizi informatici','055 8743280',''),(21,1,7,1,'','055 87431','055 8722946','urp@comune.lastra-a-signa.fi.it','URP','055 8743280',''),(22,1,8,4,'','055 8743210','055 8743252','segreteria@comune.lastra-a-signa.fi.it','Servizi Generali - Area 6','055 8722946',''),(23,1,7,2,'','055 8743245','055 8722946','pianificazione@comune.lastra-a-signa.-fi.it','Pianificazione Strategica','',''),(24,5,5,2,'','055 8728147','055 8728147','Ufficioassociato@comune.lastra-a-signa.fi.it','Ufficio Associato Previdenza','',''),(25,9,5,11,'','055 7195256','055 7591472','m.cini@comune.scandicci.fi.it','Ufficio Associato Personale','',''),(26,1,14,9,'','055 8743221','055 8722946','sportellodonna@gmail.com','Sportello Donna','',''),(27,1,14,12,'','055 8743610','055 8722846','immigrazione@comune.lastra-a-signa.fi.it','Sportello Immigrazione','',''),(28,10,15,10,'','055 8725770','055 8727933','ufficioturistico@comune.lastra-a-signa.fi.it','Ufficio Turistico','',''),(29,1,14,7,'','055 8743221','055 8722946','sol@comune.lastra-a-signa.fi.it','Centro per l\'Impiego','',''),(30,11,15,13,'','055 8728092','055 8728092','','Pubbliche Affissioni','','');
UNLOCK TABLES;
/*!40000 ALTER TABLE `Ufficio` ENABLE KEYS */;

--
-- Table structure for table `WorkItemRoleType`
--

DROP TABLE IF EXISTS `WorkItemRoleType`;
CREATE TABLE `WorkItemRoleType` (
  `WorkItemRole` varchar(50) NOT NULL default '',
  `ApplicationID` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`WorkItemRole`,`ApplicationID`),
  KEY `FK399C2D478E6D915` (`ApplicationID`),
  CONSTRAINT `FK399C2D478E6D915` FOREIGN KEY (`ApplicationID`) REFERENCES `Application` (`ApplicationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `WorkItemRoleType`
--


/*!40000 ALTER TABLE `WorkItemRoleType` DISABLE KEYS */;
LOCK TABLES `WorkItemRoleType` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `WorkItemRoleType` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

