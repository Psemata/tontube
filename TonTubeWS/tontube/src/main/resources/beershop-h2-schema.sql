CREATE TABLE biere (
  id int(11) NOT NULL auto_increment,
  nom char(35) NOT NULL default '',
  prix float NOT NULL default '0.00',
  fabricant char(35) NOT NULL default '',
  stock int(11) NOT NULL default '0',
  PRIMARY KEY  (id)
);
CREATE TABLE panier (
	id int(11) NOT NULL auto_increment,
	date_creation DATE NOT NULL,
	PRIMARY KEY  (id)
);
CREATE TABLE panier_bieres (
	biere_id 	INT,
	panier_id INT,
    PRIMARY KEY (biere_id,panier_id),
    FOREIGN KEY (biere_id) REFERENCES biere(id),
    FOREIGN KEY (panier_id) REFERENCES panier (id)
);
CREATE TABLE evaluation (
	id int(11) NOT NULL auto_increment,
    biere_id INT,
    date_evaluation DATE NOT NULL,
    commentaire VARCHAR(1024),
    note int(3) UNSIGNED,
    PRIMARY KEY (id),
    FOREIGN KEY (biere_id) REFERENCES biere(id)
);