-- ------------------------------
-- User
-- ------------------------------

DROP TABLE IF EXISTS "user" CASCADE;

CREATE TABLE "user" (
  "id" BIGSERIAL, -- 'Идентификатор'
  "login" VARCHAR(64) NOT NULL,  -- 'Логин'
  "password" VARCHAR(64) NOT NULL, -- 'Пароль'
  PRIMARY KEY ("id"),
  UNIQUE ("login")
); -- 'Пользователь'

INSERT INTO "user" ("login", "password") values ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');


-- ------------------------------
-- User group
-- ------------------------------

DROP TABLE IF EXISTS "user_group" CASCADE;

CREATE TABLE  "user_group" (
  "id" BIGSERIAL , -- 'Идентификатор'
  "login" VARCHAR(45) NOT NULL,  -- 'Имя пользователя'
  "group_name" VARCHAR(45) NOT NULL,  -- 'Название группы'
  PRIMARY KEY ("id"),
  UNIQUE ("login", "group_name"),
  CONSTRAINT "fk_user_group__user" FOREIGN KEY ("login") REFERENCES "user" ("login")
);  -- 'Группа пользователя'

INSERT INTO "user_group" ("login", "group_name") values ('admin', 'ADMINISTRATORS');


-- ------------------------------
-- Locale
-- ------------------------------

DROP TABLE IF EXISTS "locale" CASCADE ;

CREATE TABLE "locale" (
  "id" INT,  -- 'Идентификатор локали'
  "locale" VARCHAR(2),  -- 'Код локали'
  "system" BOOLEAN,  -- 'Является ли локаль системной'
  PRIMARY KEY ("id"),
  UNIQUE ("locale")
);  -- 'Локаль'

INSERT INTO "locale" ("id", "locale", "system") VALUES (1, 'RU', true);
INSERT INTO "locale" ("id", "locale", "system") VALUES (2, 'UA', false);


-- ------------------------------
-- Update
-- ------------------------------

DROP TABLE IF EXISTS "update";

CREATE TABLE "update" (
  "id" BIGSERIAL,  -- 'Идентификатор'
  "version" VARCHAR(64) NOT NULL,  -- 'Версия'
  "date" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 'Дата обновления'
  PRIMARY KEY ("id")
);  -- 'Обновление базы данных'


-- ------------------------------
-- Entity
-- ------------------------------

DROP TABLE IF EXISTS "entity" CASCADE;

CREATE TABLE "entity" (
  "id" INT NOT NULL,  -- 'Идентификатор сущности'
  "name" VARCHAR(100) NOT NULL,  -- 'Название сущности'
  PRIMARY KEY ("id"),
  UNIQUE ("name")
);  -- 'Сущность'


DROP TABLE IF EXISTS entity_value_type CASCADE ;

CREATE TABLE "entity_value_type" (
  "id" INT NOT NULL,  -- 'Идентификатор типа значения атрибута'
  "value_type" VARCHAR(100) NOT NULL,  -- 'Тип значения атрибута'
  PRIMARY KEY  ("id")
);  -- 'Тип значения атрибута'


DROP TABLE IF EXISTS "entity_attribute" CASCADE;

CREATE TABLE "entity_attribute" (
  "id" BIGSERIAL,  -- 'Идентификатор'
  "entity_id" INT NOT NULL,  -- 'Идентификатор сущности'
  "entity_attribute_id" INT NOT NULL,  -- 'Идентификатор атрибута'
  "start_date" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 'Дата начала периода действия атрибута'
  "end_date" TIMESTAMP,  -- 'Дата окончания периода действия атрибута'
  "value_type_id" INT,  -- 'Тип значения атрибута'
  "reference_entity_id" INT,  -- 'Ссылка на сущность'
  "reference_entity_attribute_id" INT,  -- 'Ссылка на атрибут'
  PRIMARY KEY ("id"),
  UNIQUE ("entity_attribute_id", "entity_id"),
  CONSTRAINT "fk_entity_attribute__entity" FOREIGN KEY ("entity_id") REFERENCES "entity" ("id"),
  CONSTRAINT "fk_entity_attribute__entity_value_type" FOREIGN KEY ("value_type_id") REFERENCES entity_value_type ("id"),
  CONSTRAINT "fk_entity_attribute__entity__ref" FOREIGN KEY ("reference_entity_id") REFERENCES "entity" ("id")
);  -- 'Атрибут'


DROP TABLE IF EXISTS "entity_value";

CREATE TABLE "entity_value" (
  "id" BIGSERIAL,  -- 'Идентификатор'
  "entity_id" INT NOT NULL,  -- 'Идентификатор сущности'
  "entity_attribute_id" INT NULL,  -- 'Идентификатор атрибута'
  "locale_id" INT NOT NULL,  -- 'Идентификатор локали'
  "text" VARCHAR(1000),  -- 'Текстовое значение'
  PRIMARY KEY ("id"),
  UNIQUE ("entity_id", "entity_attribute_id", "locale_id"),
  CONSTRAINT "fk_entity_value__entity" FOREIGN KEY ("entity_id") REFERENCES "entity" ("id"),
  CONSTRAINT "fk_entity_value__entity_attribute" FOREIGN KEY ("entity_attribute_id", "entity_id")
    REFERENCES "entity_attribute" ("entity_attribute_id", "entity_id"),
  CONSTRAINT "fk_entity_value__locale" FOREIGN KEY ("locale_id") REFERENCES "locale" ("id")
);  -- 'Значения'


INSERT INTO "entity_value_type" ("id", "value_type") VALUES (1, 'boolean');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (2, 'number');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (3, 'decimal');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (4, 'text');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (5, 'date');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (6, 'reference');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (7, 'number_list');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (8, 'text_list');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (9, 'reference_list');


-- ------------------------------
-- Permission
-- ------------------------------

DROP TABLE IF EXISTS "permission";

CREATE TABLE "permission" (
  "id" BIGSERIAL ,  -- 'Идентификатор'
  "permission_id" BIGINT NOT NULL,  -- 'Идентификатор права доступа'
  "table" VARCHAR(64) NOT NULL,  -- 'Таблица'
  "entity" VARCHAR(64) NOT NULL,  -- 'Сущность'
  "object_id" BIGINT NOT NULL,  -- 'Идентификатор объекта'
  PRIMARY KEY ("id"),
  UNIQUE ("permission_id", "entity", "object_id")
);  -- 'Права доступа'


-- ------------------------------
-- Domain
-- ------------------------------

CREATE OR REPLACE PROCEDURE create_domain_tables (IN entityName VARCHAR(64),
                                                IN entityDescription VARCHAR(256))
LANGUAGE plpgsql
AS $$
BEGIN
    EXECUTE CONCAT('
        CREATE TABLE "', entityName, '"
        (
            "id"               BIGSERIAL,  -- ''Идентификатор''
            "object_id"        BIGINT NOT NULL,  -- ''Идентификатор объекта''
            "start_date"       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- ''Дата начала периода действия объекта''
            "end_date"         TIMESTAMP,  -- ''Дата окончания периода действия объекта''
            "status"           INT  NOT NULL DEFAULT 1,  -- ''Статус''
            "permission_id"    BIGINT,  -- ''Права доступа к объекту''
            "user_id"          BIGINT,  -- ''Идентифитактор пользователя''
            PRIMARY KEY ("id"),
            UNIQUE ("object_id", "start_date", "status"),
            CONSTRAINT "fk_', entityName, '__user" FOREIGN KEY ("user_id") REFERENCES "user" ("id")
        );');

    EXECUTE CONCAT('COMMENT ON TABLE ', entityName, ' IS ''', entityDescription, ''';');


    EXECUTE CONCAT('
        CREATE TABLE "', entityName, '_attribute"
        (
            "id"                  BIGSERIAL,  -- ''Идентификатор''
            "domain_id"           BIGINT NOT NULL,  -- ''Идентификатор домена''
            "entity_attribute_id" INT NOT NULL,  -- ''Идентификатор типа атрибута''
            "text"                VARCHAR(255),  -- ''Текст''
            "number"              BIGINT,  -- ''Число''
            "date"                TIMESTAMP,  -- ''Дата''
            "start_date"          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- ''Дата начала периода действия атрибута''
            "end_date"            TIMESTAMP,  -- ''Дата окончания периода действия атрибута''
            "status"              INT    NOT NULL DEFAULT 1,  -- ''Статус''
            "user_id"             BIGINT NULL,  -- ''Идентифитактор пользователя''
            PRIMARY KEY ("id"),
            CONSTRAINT "fk_', entityName, '_attribute__', entityName, '" FOREIGN KEY ("domain_id") REFERENCES "', entityName, '" ("id"),
            CONSTRAINT "fk_', entityName, '_attribute__user" FOREIGN KEY ("user_id") REFERENCES "user" ("id")
        );');

    EXECUTE CONCAT('COMMENT ON TABLE ', entityName, '_attribute IS ''', entityDescription, ' - Аттрибуты'';');


    EXECUTE CONCAT('
        CREATE TABLE "', entityName, '_value"
        (
            "id"           BIGSERIAL,  -- ''Идентификатор''
            "attribute_id" BIGINT NOT NULL,  -- ''Идентификатор атрибута''
            "locale_id"    INT,  -- ''Идентификатор локали''
            "text"         VARCHAR(1000),  -- ''Текстовое значение''
            "number"       BIGINT,  -- ''Числовое значение''
            PRIMARY KEY ("id"),
            UNIQUE ("attribute_id", "locale_id"),
            CONSTRAINT "fk_', entityName, '_value__', entityName, '_attribute" FOREIGN KEY ("attribute_id") REFERENCES "',
                entityName, '_attribute" ("id"),
            CONSTRAINT "fk_', entityName, '_value__locale" FOREIGN KEY ("locale_id") REFERENCES "locale" ("id")
        );');

    EXECUTE CONCAT('COMMENT ON TABLE ', entityName, '_value IS ''', entityDescription, ' - Значения атрибутов'';');
END;
$$;


CREATE OR REPLACE PROCEDURE create_entity(IN entityId BIGINT,
                                         IN entityName VARCHAR(64),
                                         IN entityDescriptionRU VARCHAR(128),
                                         IN entityDescriptionUA VARCHAR(128))
LANGUAGE plpgsql
AS $$
BEGIN
    EXECUTE CONCAT('INSERT INTO "entity" ("id", "name") VALUES (',entityId, ', ''', entityName, ''');');

    EXECUTE CONCAT('INSERT INTO "entity_value"("entity_id", "locale_id", "text") VALUES (', entityId,
        ', 1, ''', entityDescriptionRU, '''), (', entityId, ', 2, ''', entityDescriptionUA, ''');');
END;
$$;

CREATE OR REPLACE PROCEDURE create_attribute(IN entityId BIGINT,
                                            IN entityAttributeId BIGINT,
                                            IN valueTypeId BIGINT,
                                            IN entityDescriptionRU VARCHAR(128),
                                            IN entityDescriptionUA VARCHAR(128))
LANGUAGE plpgsql
AS $$
BEGIN
    EXECUTE CONCAT('INSERT INTO "entity_attribute"("entity_id", "entity_attribute_id", "value_type_id") VALUES (',
                                  entityId, ', ', entityAttributeId, ', ', valueTypeId, ');');

    EXECUTE CONCAT('INSERT INTO "entity_value"("entity_id", "entity_attribute_id", "locale_id", "text") VALUES (',
                                    entityId, ', ', entityAttributeId, ', 1, ''', entityDescriptionRU, '''), (',
                                    entityId, ', ', entityAttributeId, ', 2, ''', entityDescriptionUA, ''');');
END;
$$;


CREATE OR REPLACE PROCEDURE create_reference(IN entityId BIGINT,
                                             IN entityAttributeId BIGINT,
                                             IN referenceEntityId BIGINT,
                                             IN referenceEntityAttributeId BIGINT,
                                             IN entityDescriptionRU VARCHAR(128),
                                             IN entityDescriptionUA VARCHAR(128))
LANGUAGE plpgsql
AS $$
BEGIN
    EXECUTE CONCAT('INSERT INTO "entity_attribute"("entity_id", "entity_attribute_id", "value_type_id", ',
                                  '"reference_entity_id", "reference_entity_attribute_id") VALUES (', entityId, ', ',
                                  entityAttributeId, ', 6', ', ', referenceEntityId, ', ', referenceEntityAttributeId, ');');

    EXECUTE CONCAT('INSERT INTO "entity_value"("entity_id", "entity_attribute_id", "locale_id", "text") VALUES (',
                                    entityId, ', ', entityAttributeId, ', 1, ''', entityDescriptionRU, '''), (',
                                    entityId, ', ', entityAttributeId, ', 2, ''', entityDescriptionUA, ''');');
END;
$$;


CREATE OR REPLACE PROCEDURE create_domain(IN entityId BIGINT,
                                    IN entityName VARCHAR(64),
                                    IN entityDescriptionRU VARCHAR(128),
                                    IN entityDescriptionUA VARCHAR(128))
LANGUAGE plpgsql
AS $$
BEGIN
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '_value"');
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '_attribute"');
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '"');

    CALL create_domain_tables(entityName, entityDescriptionRU);
    CALL create_entity(entityId, entityName, entityDescriptionRU, entityDescriptionUA);
END;
$$;


-- ---------------------------
-- Setting
-- ---------------------------

CALL create_domain(0,'setting', 'Настройки', 'Налаштування');
CALL create_attribute(0, 1, 4, 'Значение', 'Значення');


-- ---------------------------
-- Address
-- ---------------------------

CALL create_domain(1,'country', 'Страна', 'Країна');
CALL create_attribute(1, 1, 8, 'Название', 'Назва');

CALL create_domain(2,'region', 'Регион', 'Регіон');
CALL create_reference(2, 1, 1, 1, 'Страна', 'Країна');
CALL create_attribute(2, 2, 8, 'Название', 'Назва');

CALL create_domain(3, 'city_type', 'Тип населённого пункта', 'Тип населеного пункту');
CALL create_attribute(3, 1, 8, 'Название', 'Назва');
CALL create_attribute(3, 2, 8, 'Краткое название', 'Коротка назва');

CALL create_domain(4,'city', 'Населённый пункт', 'Населений пункт');
CALL create_reference(4, 1, 2, 2, 'Регион', 'Регіон');
CALL create_reference(4, 2, 3, 1, 'Тип', 'Тип');
CALL create_attribute(4, 3, 8, 'Название', 'Назва');

CALL create_domain(5,'district', 'Район', 'Район');
CALL create_reference(5, 1, 4, 3, 'Населённый пункт', 'Населений пункт');
CALL create_attribute(5, 2, 8, 'Название', 'Назва');
CALL create_attribute(5, 3, 4, 'Код района', 'Код району');

CALL create_domain(6, 'street_type', 'Тип', 'Тип');
CALL create_attribute(6, 1, 8, 'Название', 'Назва');
CALL create_attribute(6, 2, 8, 'Краткое название', 'Коротка назва');

CALL create_domain(7,'street', 'Улица', 'Вулиця');
CALL create_reference(7, 1, 4, 3, 'Населённый пункт', 'Населений пункт');
CALL create_reference(7, 2, 6, 1, 'Тип улицы', 'Тип вулиці');
CALL create_attribute(7, 3, 8, 'Название', 'Назва');
CALL create_attribute(7, 4, 4, 'Код улицы', 'Код вулиці');

CALL create_domain(8,'building', 'Дом', 'Будинок');
CALL create_reference(8, 1, 5, 2, 'Район', 'Район');
CALL create_reference(8, 2, 7, 3, 'Улица', 'Вулиця');
CALL create_attribute(8, 3, 8, 'Номер дома', 'Номер будинку');
CALL create_attribute(8, 4, 8, 'Корпус', 'Корпус');
CALL create_attribute(8, 5, 8, 'Строение', 'Будова');
CALL create_attribute(8, 6, 4, 'Код дома', 'Код будинку');

CALL create_domain(9,'apartment', 'Квартира', 'Квартира');
CALL create_reference(9, 1, 8, 3, 'Дом', 'Будинок');
CALL create_attribute(9, 2, 8, 'Номер квартиры', 'Номер квартири');

CALL create_domain(10,'room', 'Комната', 'Кімната');
CALL create_reference(10, 1, 8, 3, 'Дом', 'Будинок');
CALL create_attribute(10, 2, 8, 'Номер комнаты', 'Номер кімнати');

-- ------------------------------
--  Company
-- ------------------------------

CALL create_domain(11, 'company', 'Компания', 'Компанія');
CALL create_reference(11, 1, 11, 2, 'Родительская компания', 'Батьківська компанія');
CALL create_attribute(11, 2, 8, 'Название', 'Назва');
CALL create_attribute(11, 3, 8, 'Краткое название', 'Коротка назва');
CALL create_attribute(11, 4, 4, 'ЕДРПОУ', 'ЄДРПОУ');
CALL create_attribute(11, 5, 4, 'Код компании', 'Код компанії');


-- ---------------------------
-- Matching
-- ---------------------------

CREATE OR REPLACE PROCEDURE create_matching(IN entityName VARCHAR(64), IN entityDescription VARCHAR(256))
LANGUAGE plpgsql
AS $$
BEGIN
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '_matching";');

    EXECUTE CONCAT('
        CREATE TABLE "', entityName, '_matching" (
          "id" BIGSERIAL,  -- ''Идентификатор соответствия''
          "object_id" BIGINT NOT NULL,  -- ''Идентификатор объекта''
          "parent_id" BIGINT,  -- ''Идентификатор родителя''
          "additional_parent_id" VARCHAR(64),  -- ''Дополнительный идентификатор родителя''
          "external_id" BIGINT,  -- ''Внешний идентификатор''
          "additional_external_id" VARCHAR(64),  -- ''Дополнительный внешний идентификатор''
          "name" VARCHAR(1000) NOT NULL,  -- ''Соответствие''
          "additional_name" VARCHAR(1000),  -- ''Дополнительное соответствие''
          "start_date" TIMESTAMP,  -- ''Дата начала актуальности''
          "end_date" TIMESTAMP,  -- ''Дата окончания актуальности''
          "company_id" BIGINT,  -- ''Идентификатор компании''
          "user_company_id" BIGINT,  -- ''Идентификатор компании пользователя''
          PRIMARY KEY ("id"),
          UNIQUE ("external_id", "additional_external_id", "company_id", "user_company_id"));');

    EXECUTE CONCAT('COMMENT ON TABLE ', entityName, '_matching IS ''', entityDescription, ' - Соответствия'';');
END;
$$;

CALL create_matching('country', 'Страны');
CALL create_matching('region', 'Регионы');
CALL create_matching('city_type', 'Тип населённого пункта');
CALL create_matching('city', 'Населённые пункты');
CALL create_matching('district', 'Районы');
CALL create_matching('street_type', 'Типы улиц');
CALL create_matching('street', 'Улица');
CALL create_matching('building', 'Дом');
CALL create_matching('apartment', 'Квартира');


-- ------------------------------
--  Sync
-- ------------------------------

DROP TABLE IF EXISTS "sync";
CREATE TABLE "sync"(
  "id" BIGSERIAL,  -- 'Идентификатор синхронизации',
  "parent_id" BIGINT,  -- 'Идентификатор родителя',
  "additional_parent_id" VARCHAR(64),  -- 'Дополнительный идентификатор родителя',
  "external_id" BIGINT NOT NULL,  -- 'Внешний идентификатор',
  "additional_external_id" VARCHAR(64),  -- 'Дополнительный внешний идентификатор',
  "name" VARCHAR(250) NOT NULL,  -- 'Название',
  "additional_name" VARCHAR(50),  -- 'Дополнительное название',
  "alt_name" VARCHAR(250),  -- 'Название в альтернативной локали',
  "alt_additional_name" VARCHAR(50),  -- 'Дополнительное название в альтернативной локали',
  "servicing_organization" BIGINT,  -- 'Обслуживающая организация',
  "balance_holder" BIGINT,  -- 'Балансодержатель',
  "date" TIMESTAMP NOT NULL,  -- 'Дата актуальности',
  "status" INT NOT NULL,  -- 'Статус синхронизации',
  "entity_id" INT NOT NULL,  -- 'Идентификатор сущности',
  PRIMARY KEY ("id"),
  CONSTRAINT "fk_sync__entity_id" FOREIGN KEY ("entity_id") REFERENCES "entity" ("id")
)  -- 'Синхронизация';
