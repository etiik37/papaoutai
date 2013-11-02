CREATE TABLE Terme ( 	id INT AUTO_INCREMENT PRIMARY KEY, 
											terme VARCHAR(255) NOT NULL);

CREATE TABLE Types ( 	id INT AUTO_INCREMENT PRIMARY KEY, 
														xpath VARCHAR(255),
														type VARCHAR(255) NOT NULL);

CREATE TABLE Documents (id INT AUTO_INCREMENT PRIMARY KEY, 
												num_doc INT NOT NULL, 
												auteur VARCHAR(255)NOT NULL,
												datePublication VARCHAR(10));

CREATE TABLE ContenirTerme(	id INT AUTO_INCREMENT PRIMARY KEY,
														idTerme INT NOT NULL, 
														idTypes INT NOT NULL,
														frequence INT NOT NULL,
														INDEX idTerme_ind (idTerme),
															FOREIGN KEY (idTerme) REFERENCES Terme(id),
														INDEX idTypes_ind(idTypes),
															FOREIGN KEY (idTypes) REFERENCES Types(id));

CREATE TABLE Situer(id INT AUTO_INCREMENT PRIMARY KEY,
										position INT NOT NULL,
										idContenir INT NOT NULL,
										INDEX idContenir_ind (idContenir),
											FOREIGN KEY (idContenir) REFERENCES ContenirTerme(id));

CREATE TABLE ContenirTypes(id INT AUTO_INCREMENT PRIMARY KEY,
																idDoc INT NOT NULL, 
																idTypes INT NOT NULL,
																INDEX idTerme_ind (idDoc),
																	FOREIGN KEY (idDoc) REFERENCES Documents(id),
																INDEX idTypes_ind(idTypes),
																	FOREIGN KEY (idTypes) REFERENCES Types(id));
