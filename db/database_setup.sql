-- ============================================================
-- SCRIPT SQL - HOPITAL CENTRAL
-- Basé sur le Diagramme de Classes UML fourni
-- ============================================================

DROP DATABASE IF EXISTS hopital_central_db;
CREATE DATABASE hopital_central_db;
USE hopital_central_db;

-- 1. Table SERVICE 
-- Relation: Un service contient plusieurs employés
CREATE TABLE service (
    id_service INT AUTO_INCREMENT PRIMARY KEY,
    nomService VARCHAR(50) NOT NULL,
    etage INT NOT NULL
);

-- 2. Table EMPLOYE (Héritage Single Table)
-- Contient tous les acteurs: Medecin, Infirmier, Technicien, RH
CREATE TABLE employe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    salaire FLOAT NOT NULL,
    
    -- Colonne pour savoir quel est le rôle (Discriminator)
    type_employe ENUM('MEDECIN', 'INFIRMIER', 'TECHNICIEN', 'RH') NOT NULL,
    
    -- Attributs spécifiques (NULL si non concerné)
    specialite VARCHAR(50),  -- Pour MEDECIN
    grade VARCHAR(50),       -- Pour INFIRMIER
    domaine VARCHAR(50),     -- Pour TECHNICIEN
    -- RH n'a pas d'attribut spécifique dans le diagramme, c'est OK.
    
    -- Relation: Appartient à un Service
    id_service INT,
    FOREIGN KEY (id_service) REFERENCES service(id_service) ON DELETE SET NULL
);

-- 3. Table CONSULTATION
-- Relation: Gérée par un Médecin
CREATE TABLE consultation (
    id_consultation INT AUTO_INCREMENT PRIMARY KEY,
    date_consultation DATE NOT NULL,
    heure VARCHAR(10) NOT NULL, -- String dans le diagramme
    diagnostic TEXT,
    prescription TEXT,
    
    id_medecin INT NOT NULL,
    FOREIGN KEY (id_medecin) REFERENCES employe(id) ON DELETE CASCADE
);

-- 4. Table EXAMEN
-- Relation: Gérée par un Infirmier
CREATE TABLE examen (
    id_examen INT AUTO_INCREMENT PRIMARY KEY,
    typeExamen VARCHAR(100) NOT NULL,
    resultat TEXT,
    
    id_infirmier INT NOT NULL,
    FOREIGN KEY (id_infirmier) REFERENCES employe(id) ON DELETE CASCADE
);

-- 5. Table MACHINE
-- Relation: Traitée par un Technicien
CREATE TABLE machine (
    id_machine INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(50) UNIQUE NOT NULL,
    nom VARCHAR(50) NOT NULL,
    etat VARCHAR(50) DEFAULT 'Fonctionnel',
    
    id_technicien INT,
    FOREIGN KEY (id_technicien) REFERENCES employe(id) ON DELETE SET NULL
);

-- 6. Table CONGE
-- Relation: Demandé par Employé, Validé par RH
CREATE TABLE conge (
    id_conge INT AUTO_INCREMENT PRIMARY KEY,
    dateDebut DATE NOT NULL,
    dateFin DATE NOT NULL,
    statut VARCHAR(20) DEFAULT 'En attente',
    
    -- Qui demande ? (Tout employé)
    id_demandeur INT NOT NULL,
    FOREIGN KEY (id_demandeur) REFERENCES employe(id) ON DELETE CASCADE,
    
    -- Qui valide ? (Un RH)
    id_valideur INT,
    FOREIGN KEY (id_valideur) REFERENCES employe(id) ON DELETE SET NULL
);

-- ============================================================
-- JEU DE DONNÉES DE TEST (INSERT)
-- ============================================================

INSERT INTO service (nomService, etage) VALUES ('Cardiologie', 1), ('Informatique', 0), ('Administration', 2);

-- Ajout d'un RH
INSERT INTO employe (nom, prenom, login, password, salaire, type_employe, id_service) 
VALUES ('Directeur', 'Paul', 'admin', '1234', 4000, 'RH', 3);

-- Ajout d'un Médecin
INSERT INTO employe (nom, prenom, login, password, salaire, type_employe, specialite, id_service) 
VALUES ('House', 'Greg', 'house', '1234', 6000, 'MEDECIN', 'Diagnisticien', 1);

-- Ajout d'un Technicien
INSERT INTO employe (nom, prenom, login, password, salaire, type_employe, domaine, id_service) 
VALUES ('Robot', 'Elliot', 'tech', '1234', 2500, 'TECHNICIEN', 'Reseau', 2);