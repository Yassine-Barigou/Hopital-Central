-- =============================================
-- SEED DATA - Données de démonstration
-- =============================================

-- Mot de passe hashé pour "password123" (bcrypt)
-- En production, utilisez un vrai hash bcrypt

-- Employés (utilisateurs)
INSERT INTO employees (email, password_hash, first_name, last_name, role, department, phone) VALUES
('rh@hopital.fr', '$2b$10$example_hash_rh', 'Marie', 'Dupont', 'RH', 'Ressources Humaines', '01 23 45 67 89'),
('medecin@hopital.fr', '$2b$10$example_hash_med', 'Jean', 'Martin', 'Médecin', 'Cardiologie', '01 23 45 67 90'),
('infirmier@hopital.fr', '$2b$10$example_hash_inf', 'Sophie', 'Bernard', 'Infirmier', 'Urgences', '01 23 45 67 91'),
('technicien@hopital.fr', '$2b$10$example_hash_tech', 'Pierre', 'Durand', 'Technicien', 'Maintenance', '01 23 45 67 92'),
('cardio@hopital.fr', '$2b$10$example_hash_cardio', 'Claire', 'Moreau', 'Médecin', 'Cardiologie', '01 23 45 67 93'),
('pediatre@hopital.fr', '$2b$10$example_hash_ped', 'Luc', 'Petit', 'Médecin', 'Pédiatrie', '01 23 45 67 94'),
('urgences@hopital.fr', '$2b$10$example_hash_urg', 'Emma', 'Robert', 'Infirmier', 'Urgences', '01 23 45 67 95');

-- Patients
INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone, email, address, blood_type, allergies) VALUES
('Alice', 'Leroy', '1985-03-15', 'Femme', '06 11 22 33 44', 'alice.leroy@email.fr', '12 Rue de la Paix, Paris', 'A+', 'Pénicilline'),
('Marc', 'Simon', '1972-07-22', 'Homme', '06 22 33 44 55', 'marc.simon@email.fr', '45 Avenue Victor Hugo, Lyon', 'O-', NULL),
('Julie', 'Laurent', '1990-11-08', 'Femme', '06 33 44 55 66', 'julie.laurent@email.fr', '78 Boulevard Haussmann, Paris', 'B+', 'Aspirine'),
('Thomas', 'Michel', '1968-01-30', 'Homme', '06 44 55 66 77', 'thomas.michel@email.fr', '23 Rue du Commerce, Marseille', 'AB+', NULL),
('Camille', 'Garcia', '1995-05-12', 'Femme', '06 55 66 77 88', 'camille.garcia@email.fr', '56 Rue de Rivoli, Paris', 'A-', 'Latex');

-- Consultations
INSERT INTO consultations (patient_id, doctor_id, date, reason, diagnosis, prescription, status) VALUES
(1, 2, '2026-01-10 09:00:00', 'Douleurs thoraciques', 'Angine de poitrine légère', 'Trinitrine, repos', 'Terminée'),
(2, 2, '2026-01-10 10:30:00', 'Contrôle annuel', 'RAS', NULL, 'Terminée'),
(3, 5, '2026-01-11 14:00:00', 'Palpitations', NULL, NULL, 'Planifiée'),
(4, 2, '2026-01-11 15:30:00', 'Suivi hypertension', NULL, NULL, 'Planifiée'),
(5, 6, '2026-01-12 09:00:00', 'Vaccination', NULL, NULL, 'Planifiée');

-- Rendez-vous
INSERT INTO appointments (patient_id, doctor_id, date, duration_minutes, type, status) VALUES
(1, 2, '2026-01-15 09:00:00', 30, 'Suivi', 'Confirmé'),
(2, 5, '2026-01-15 10:00:00', 45, 'Examen', 'En attente'),
(3, 2, '2026-01-16 11:00:00', 30, 'Consultation', 'Confirmé'),
(4, 6, '2026-01-16 14:00:00', 30, 'Contrôle', 'En attente'),
(5, 2, '2026-01-17 09:30:00', 60, 'Bilan complet', 'Confirmé');

-- Équipements
INSERT INTO equipment (name, type, serial_number, location, status, purchase_date, next_maintenance_date) VALUES
('IRM Siemens Magnetom', 'Imagerie', 'IRM-2024-001', 'Bâtiment A - Salle 101', 'Fonctionnel', '2024-01-15', '2026-02-15'),
('Scanner GE Revolution', 'Imagerie', 'SCN-2023-042', 'Bâtiment A - Salle 102', 'Fonctionnel', '2023-06-20', '2026-01-20'),
('Échographe Philips', 'Imagerie', 'ECH-2024-015', 'Bâtiment B - Salle 205', 'En maintenance', '2024-03-10', '2026-01-11'),
('Défibrillateur Zoll', 'Urgences', 'DEF-2022-008', 'Urgences - Salle de réa', 'Fonctionnel', '2022-09-01', '2026-03-01'),
('Moniteur Cardiaque Philips', 'Cardiologie', 'MON-2024-033', 'Cardiologie - Salle 301', 'Fonctionnel', '2024-02-28', '2026-02-28'),
('Respirateur Dräger', 'Réanimation', 'RES-2023-011', 'Réanimation - Salle 401', 'Hors service', '2023-04-15', '2026-01-05');

-- Maintenance
INSERT INTO maintenance (equipment_id, technician_id, scheduled_date, type, priority, status, description) VALUES
(3, 4, '2026-01-11', 'Préventive', 'Normale', 'En cours', 'Calibration annuelle échographe'),
(6, 4, '2026-01-05', 'Corrective', 'Urgente', 'Planifiée', 'Remplacement valve défectueuse'),
(2, 4, '2026-01-20', 'Préventive', 'Normale', 'Planifiée', 'Maintenance trimestrielle scanner'),
(1, 4, '2026-02-15', 'Préventive', 'Basse', 'Planifiée', 'Vérification bobines IRM'),
(4, 4, '2026-03-01', 'Préventive', 'Haute', 'Planifiée', 'Test et remplacement batterie défibrillateur');
