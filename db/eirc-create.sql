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

INSERT INTO "user" ("login", "password") values ('admin', '591414ddbf3a953b3a12b0dd02f34f378540bd73a66282d851011dbd96b0e0fb');


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
  "entity_id" INT NOT NULL REFERENCES "entity" ON DELETE CASCADE,  -- 'Идентификатор сущности'
  "entity_attribute_id" INT NOT NULL,  -- 'Идентификатор атрибута'
  "start_date" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 'Дата начала периода действия атрибута'
  "end_date" TIMESTAMP,  -- 'Дата окончания периода действия атрибута'
  "value_type_id" INT REFERENCES "entity_value_type",  -- 'Тип значения атрибута'
  "reference_entity_id" INT REFERENCES "entity",  -- 'Ссылка на сущность'
  "reference_entity_attribute_id" INT REFERENCES "entity_attribute",  -- 'Ссылка на атрибут'
  PRIMARY KEY ("id"),
  UNIQUE ("entity_attribute_id", "entity_id")
);  -- 'Атрибут'


DROP TABLE IF EXISTS "entity_value";

CREATE TABLE "entity_value" (
  "id" BIGSERIAL,  -- 'Идентификатор'
  "entity_id" INT NOT NULL REFERENCES "entity" ON DELETE CASCADE,  -- 'Идентификатор сущности'
  "entity_attribute_id" INT NULL,  -- 'Идентификатор атрибута'
  "locale_id" INT NOT NULL REFERENCES "locale",  -- 'Идентификатор локали'
  "text" VARCHAR(1000),  -- 'Текстовое значение'
  PRIMARY KEY ("id"),
  UNIQUE ("entity_id", "entity_attribute_id", "locale_id"),
  CONSTRAINT "fk_entity_value__entity_attribute" FOREIGN KEY ("entity_attribute_id", "entity_id")
    REFERENCES "entity_attribute" ("entity_attribute_id", "entity_id") ON DELETE CASCADE
);  -- 'Значения'


INSERT INTO "entity_value_type" ("id", "value_type") VALUES (1, 'boolean');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (2, 'number');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (3, 'decimal');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (4, 'text');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (5, 'date');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (6, 'reference');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (7, 'number_value');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (8, 'text_value');
INSERT INTO "entity_value_type" ("id", "value_type") VALUES (9, 'reference_value');


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
        CREATE TABLE "', entityName, '_id"
        (
            "object_id"        BIGSERIAL,  -- ''Идентификатор''
            "uuid"             UUID NOT NULL,  -- ''Уникальный идентификатор''
            "timestamp"        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- ''Время создания''
            PRIMARY KEY ("object_id")
        );');

    EXECUTE CONCAT('
        CREATE TABLE "', entityName, '"
        (
            "id"               BIGSERIAL,  -- ''Идентификатор''
            "object_id"        BIGINT NOT NULL REFERENCES "', entityName, '_id" ON DELETE CASCADE,  -- ''Идентификатор объекта''
            "start_date"       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- ''Дата начала периода действия объекта''
            "end_date"         TIMESTAMP,  -- ''Дата окончания периода действия объекта''
            "status"           INT  NOT NULL DEFAULT 1,  -- ''Статус''
            "permission_id"    BIGINT,  -- ''Права доступа к объекту''
            "user_id"          BIGINT REFERENCES "user",  -- ''Идентифитактор пользователя''
            PRIMARY KEY ("id")
        );');

    EXECUTE CONCAT('COMMENT ON TABLE ', entityName, ' IS ''', entityDescription, ''';');


    EXECUTE CONCAT('
        CREATE TABLE "', entityName, '_attribute"
        (
            "id"                  BIGSERIAL,  -- ''Идентификатор''
            "domain_id"           BIGINT NOT NULL REFERENCES "', entityName,'" ON DELETE CASCADE,  -- ''Идентификатор домена''
            "entity_attribute_id" INT NOT NULL,  -- ''Идентификатор типа атрибута''
            "text"                VARCHAR(255),  -- ''Текст''
            "number"              BIGINT,  -- ''Число''
            "date"                TIMESTAMP,  -- ''Дата''
            "start_date"          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- ''Дата начала периода действия атрибута''
            "end_date"            TIMESTAMP,  -- ''Дата окончания периода действия атрибута''
            "status"              INT    NOT NULL DEFAULT 1,  -- ''Статус''
            "user_id"             BIGINT NULL REFERENCES "user",  -- ''Идентифитактор пользователя''
            PRIMARY KEY ("id")
        );');

    EXECUTE CONCAT('COMMENT ON TABLE ', entityName, '_attribute IS ''', entityDescription, ' - Аттрибуты'';');


    EXECUTE CONCAT('
        CREATE TABLE "', entityName, '_value"
        (
            "id"           BIGSERIAL,  -- ''Идентификатор''
            "attribute_id" BIGINT NOT NULL REFERENCES "', entityName, '_attribute" ON DELETE CASCADE,  -- ''Идентификатор атрибута''
            "locale_id"    INT REFERENCES "locale",  -- ''Идентификатор локали''
            "text"         VARCHAR(1000),  -- ''Текстовое значение''
            "number"       BIGINT,  -- ''Числовое значение''
            PRIMARY KEY ("id"),
            UNIQUE ("attribute_id", "locale_id")
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
--
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '_value"');
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '_attribute"');
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '"');
    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '_matching";');
--

    EXECUTE CONCAT('DROP TABLE IF EXISTS "', entityName, '_id"');

    CALL create_domain_tables(entityName, entityDescriptionRU);
    CALL create_entity(entityId, entityName, entityDescriptionRU, entityDescriptionUA);
END;
$$;


-- ---------------------------
-- Setting
-- ---------------------------

CALL create_domain(0,'setting', 'Настройки', 'Налаштування');
CALL create_attribute(0, 1, 4, 'Значение', 'Значення');

-- ------------------------------
--  Company
-- ------------------------------

CALL create_domain(11, 'company', 'Компания', 'Компанія');
CALL create_reference(11, 1, 11, 2, 'Родительская компания', 'Батьківська компанія');
CALL create_attribute(11, 2, 8, 'Название', 'Назва');
CALL create_attribute(11, 3, 8, 'Краткое название', 'Коротка назва');
CALL create_attribute(11, 4, 4, 'ЕДРПОУ', 'ЄДРПОУ');
CALL create_attribute(11, 5, 4, 'Код', 'Код');

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

CALL create_domain(6, 'street_type', 'Тип улицы', 'Тип вулиці');
CALL create_attribute(6, 1, 8, 'Название', 'Назва');
CALL create_attribute(6, 2, 8, 'Краткое название', 'Коротка назва');

CALL create_domain(7,'street', 'Улица', 'Вулиця');
CALL create_reference(7, 1, 4, 3, 'Населённый пункт', 'Населений пункт');
CALL create_reference(7, 2, 6, 1, 'Тип', 'Тип');
CALL create_attribute(7, 3, 8, 'Название', 'Назва');

CALL create_domain(8,'building', 'Дом', 'Будинок');
CALL create_reference(8, 1, 5, 2, 'Район', 'Район');
CALL create_reference(8, 2, 7, 3, 'Улица', 'Вулиця');
CALL create_attribute(8, 3, 8, 'Номер', 'Номер');
CALL create_attribute(8, 4, 8, 'Корпус', 'Корпус');
CALL create_attribute(8, 5, 8, 'Строение', 'Будова');

CALL create_domain(9,'apartment', 'Квартира', 'Квартира');
CALL create_reference(9, 1, 8, 3, 'Дом', 'Будинок');
CALL create_attribute(9, 2, 8, 'Номер квартиры', 'Номер квартири');

CALL create_domain(10,'room', 'Комната', 'Кімната');
CALL create_reference(10, 1, 8, 3, 'Дом', 'Будинок');
CALL create_attribute(10, 2, 8, 'Номер', 'Номер');


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
          "object_id" BIGINT NOT NULL REFERENCES "', entityName, '_id" ON DELETE CASCADE,  -- ''Идентификатор объекта''
          "parent_id" BIGINT,  -- ''Идентификатор родителя''
          "additional_parent_id" BIGINT,  -- ''Дополнительный идентификатор родителя''
          "name" VARCHAR(1000) NOT NULL,  -- ''Соответствие''
          "additional_name" VARCHAR(1000),  -- ''Дополнительное соответствие''
          "number" BIGINT,  -- ''Номер''
          "code" VARCHAR(64),  -- ''Код''
          "start_date" TIMESTAMP,  -- ''Дата начала актуальности''
          "end_date" TIMESTAMP,  -- ''Дата окончания актуальности''
          "company_id" BIGINT,  -- ''Идентификатор компании''
          "user_company_id" BIGINT,  -- ''Идентификатор компании пользователя''
          "locale_id" INT REFERENCES "locale",  -- ''Идентификатор локали''
          PRIMARY KEY ("id"));');


    EXECUTE CONCAT('COMMENT ON TABLE ', entityName, '_matching IS ''', entityDescription, ' - Соответствия'';');
END;
$$;

CALL create_matching('company', 'Компании');

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
  "entity_id" INT NOT NULL REFERENCES "entity",  -- 'Идентификатор сущности',
  "parent_id" BIGINT,  -- 'Идентификатор родителя',
  "additional_parent_id" VARCHAR(64),  -- 'Дополнительный идентификатор родителя',
  "name" VARCHAR(250) NOT NULL,  -- 'Название',
  "additional_name" VARCHAR(50),  -- 'Дополнительное название',
  "alt_name" VARCHAR(250),  -- 'Название в альтернативной локали',
  "alt_additional_name" VARCHAR(50),  -- 'Дополнительное название в альтернативной локали',
  "external_id" BIGINT NOT NULL,  -- 'Код',
  "additional_external_id" VARCHAR(64),  -- 'Дополнительный код',
  "date" TIMESTAMP NOT NULL,  -- 'Дата актуальности',
  "servicing_organization" BIGINT,  -- 'Обслуживающая организация',
  "balance_holder" BIGINT,  -- 'Балансодержатель',
  "status" INT NOT NULL,  -- 'Статус синхронизации',
  PRIMARY KEY ("id")
);  -- 'Синхронизация';

-- ------------------------------
--  Domains
-- ------------------------------

CALL create_domain(12, 'service_type', 'Тип услуги', 'Тип послуги');
CALL create_attribute(12, 1, 8, 'Название', 'Назва');
CALL create_attribute(12, 2, 4, 'Код', 'Код');

CALL create_domain(13, 'payment_parameter', 'Параметр начислений', 'Параметр нарахувань');
CALL create_attribute(13, 1, 8, 'Название', 'Назва');
CALL create_attribute(13, 2, 4, 'Код', 'Код');

CALL create_domain(14, 'partner_type', 'Тип участника', 'Тип учасника');
CALL create_attribute(14, 1, 8, 'Название', 'Назва');
CALL create_attribute(14, 2, 4, 'Код', 'Код');

CALL create_domain(15, 'partner', 'Участник', 'Учасник');
CALL create_attribute(15, 1, 8, 'Название', 'Назва');
CALL create_attribute(15, 2, 8, 'Краткое название', 'Коротка назва');
CALL create_attribute(15, 3, 4, 'ЕДРПОУ', 'ЄДРПОУ');

CALL create_domain(16, 'contract_type', 'Тип договора', 'Тип договору');
CALL create_attribute(16, 1, 8, 'Название', 'Назва');
CALL create_attribute(16, 2, 4, 'Код', 'Код');

CALL create_domain(17, 'contract', 'Договор', 'Договір');
CALL create_attribute(17, 1, 8, 'Название', 'Назва');

CALL create_domain(18, 'document_type', 'Тип документа', 'Тип документа');
CALL create_attribute(18, 1, 8, 'Название', 'Назва');
CALL create_attribute(18, 2, 4, 'Код', 'Код');

CALL create_domain(19, 'data_type', 'Тип информации', 'Тип інформації');
CALL create_attribute(19, 1, 8, 'Название', 'Назва');
CALL create_attribute(19, 2, 4, 'Код', 'Код');

