ALTER TABLE historia_clinica
    ADD COLUMN antecedentes_personales VARCHAR(2000) NULL,
    ADD COLUMN antecedentes_familiares VARCHAR(2000) NULL,
    ADD COLUMN alergias VARCHAR(2000) NULL;