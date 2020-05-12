/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- ------------------------------
-- User
-- ------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE  `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор',
  `login` VARCHAR(64) NOT NULL COMMENT 'Логин',
  `password` VARCHAR(64) NOT NULL COMMENT 'Пароль',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_login` (`login`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Пользователь';

INSERT INTO `user`(login, password) value ('admin', sha2('admin', 256));


-- ------------------------------
-- User group
-- ------------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE  `user_group` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор',
  `login` VARCHAR(45) NOT NULL COMMENT 'Имя пользователя',
  `group_name` VARCHAR(45) NOT NULL COMMENT 'Название группы',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_unique` (`login`, `group_name`),
  CONSTRAINT `fk_user_group__user` FOREIGN KEY (`login`) REFERENCES `user` (`login`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Группа пользователя';

INSERT INTO `user_group` (login, group_name) value ('admin', 'ADMINISTRATORS');


-- ------------------------------
-- Locale
-- ------------------------------

DROP TABLE IF EXISTS `locale`;
CREATE TABLE `locale` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор локали',
  `locale` VARCHAR(2) NOT NULL COMMENT 'Код локали',
  `system` TINYINT(1) NOT NULL default 0 COMMENT 'Является ли локаль системной',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key_locale` (`locale`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Локаль';

INSERT INTO `locale`(`id`, `locale`, `system`) VALUES (1, 'RU', 1);
INSERT INTO `locale`(`id`, `locale`, `system`) VALUES (2, 'UA', 0);


-- ------------------------------
-- Update
-- ------------------------------

DROP TABLE IF EXISTS `update`;
CREATE TABLE `update` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор',
  `version` VARCHAR(64) NOT NULL COMMENT 'Версия',
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Дата обновления',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Обновление базы данных';


-- ------------------------------
-- Entity
-- ------------------------------

DROP TABLE IF EXISTS `entity`;
CREATE TABLE `entity` (
  `id` INT NOT NULL COMMENT 'Идентификатор сущности',
  `name` VARCHAR(100) NOT NULL COMMENT 'Название сущности',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_entity` (`name`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Сущность';

DROP TABLE IF EXISTS entity_value_type;
CREATE TABLE `entity_value_type` (
  `id` INT NOT NULL COMMENT 'Идентификатор типа значения атрибута',
  `value_type` VARCHAR(100) NOT NULL COMMENT 'Тип значения атрибута',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Тип значения атрибута';

DROP TABLE IF EXISTS `entity_attribute`;
CREATE TABLE `entity_attribute` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор',
  `entity_id` INT NOT NULL COMMENT 'Идентификатор сущности',
  `entity_attribute_id` INT NOT NULL COMMENT 'Идентификатор атрибута',
  `start_date` TIMESTAMP NOT NULL default CURRENT_TIMESTAMP COMMENT 'Дата начала периода действия атрибута',
  `end_date` TIMESTAMP NULL default NULL COMMENT 'Дата окончания периода действия атрибута',
  `value_type_id` INT COMMENT  'Тип значения атрибута',
  `reference_entity_id` INT COMMENT  'Ссылка на сущность',
  `reference_entity_attribute_id` INT COMMENT  'Ссылка на атрибут',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_unique` (`entity_attribute_id`, `entity_id`),
  KEY `key_entity_id` (`entity_id`),
  KEY `key_value_type_id` (`value_type_id`),
  KEY `key_reference_entity_id` (`reference_entity_id`),
  KEY `key_reference_entity_attribute_id` (`reference_entity_attribute_id`),
  CONSTRAINT `fk_entity_attribute__entity` FOREIGN KEY (`entity_id`) REFERENCES `entity` (`id`),
  CONSTRAINT `fk_entity_attribute__entity_value_type` FOREIGN KEY (`value_type_id`) REFERENCES entity_value_type (`id`),
  CONSTRAINT `fk_entity_attribute__entity__ref` FOREIGN KEY (`reference_entity_id`) REFERENCES `entity` (`id`),
  CONSTRAINT `fk_entity_attribute__entity_attribute__ref` FOREIGN KEY (`reference_entity_attribute_id`) REFERENCES `entity_attribute` (`entity_attribute_id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Атрибут';

DROP TABLE IF EXISTS `entity_value`;
CREATE TABLE `entity_value` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор',
  `entity_id` INT NOT NULL COMMENT 'Идентификатор сущности',
  `entity_attribute_id` INT NULL COMMENT 'Идентификатор атрибута',
  `locale_id` INT NOT NULL COMMENT 'Идентификатор локали',
  `text` VARCHAR(1000) COMMENT 'Текстовое значение',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_unique` (`entity_id`, `entity_attribute_id`, `locale_id`),
  KEY `key_entity_id` (`entity_id`),
  KEY `key_entity_attribute_id` (`entity_attribute_id`),
  KEY `key_locale` (`locale_id`),
  KEY `key_value` (`text`(128)),
  CONSTRAINT `fk_entity_value__entity` FOREIGN KEY (`entity_id`) REFERENCES `entity` (`id`),
  CONSTRAINT `fk_entity_value__entity_attribute` FOREIGN KEY (`entity_attribute_id`, `entity_id`)
    REFERENCES `entity_attribute` (`entity_attribute_id`, `entity_id`),
  CONSTRAINT `fk_entity_value__locale` FOREIGN KEY (`locale_id`) REFERENCES `locale` (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Значения';




INSERT INTO entity_value_type (id, value_type) VALUE (1, 'boolean');
INSERT INTO entity_value_type (id, value_type) VALUE (2, 'number');
INSERT INTO entity_value_type (id, value_type) VALUE (3, 'decimal');
INSERT INTO entity_value_type (id, value_type) VALUE (4, 'text');
INSERT INTO entity_value_type (id, value_type) VALUE (5, 'date');
INSERT INTO entity_value_type (id, value_type) VALUE (6, 'reference');
INSERT INTO entity_value_type (id, value_type) VALUE (7, 'number_list');
INSERT INTO entity_value_type (id, value_type) VALUE (8, 'text_list');
INSERT INTO entity_value_type (id, value_type) VALUE (9, 'reference_list');

-- ------------------------------
-- Permission
-- ------------------------------

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'Идентификатор',
  `permission_id` BIGINT NOT NULL COMMENT 'Идентификатор права доступа',
  `table` VARCHAR(64) NOT NULL COMMENT 'Таблица',
  `entity` VARCHAR(64) NOT NULL COMMENT 'Сущность',
  `object_id` BIGINT NOT NULL COMMENT 'Идентификатор объекта',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_unique` (`permission_id`, `entity`, `object_id`),
  KEY `key_permission_id` (`permission_id`),
  KEY `key_table` (`table`),
  KEY `key_entity` (`entity`),
  KEY `key_object_id` (`object_id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Права доступа';


-- ------------------------------
-- Procedure
-- ------------------------------

DELIMITER //

DROP PROCEDURE IF EXISTS createDomainTables;
CREATE PROCEDURE createDomainTables (IN entityName VARCHAR(64) CHARSET utf8,
                                     IN entityDescription VARCHAR(256) CHARSET utf8)
BEGIN
    SET @createDomain = CONCAT('
        CREATE TABLE `', entityName, '`
        (
            `id`               BIGINT NOT NULL AUTO_INCREMENT COMMENT ''Идентификатор'',
            `object_id`        BIGINT NOT NULL COMMENT ''Идентификатор объекта'',
            `start_date`       TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''Дата начала периода действия объекта'',
            `end_date`         TIMESTAMP  NULL     DEFAULT NULL COMMENT ''Дата окончания периода действия объекта'',
            `status`           INT  NOT NULL DEFAULT 1 COMMENT ''Статус'',
            `permission_id`    BIGINT NULL COMMENT ''Права доступа к объекту'',
            `user_id`          BIGINT NULL COMMENT ''Идентифитактор пользователя'',
            PRIMARY KEY (`id`),
            UNIQUE KEY `unique_object_id__status` (`object_id`, `status`),
            KEY `key_object_id` (`object_id`),
            KEY `key_start_date` (`start_date`),
            KEY `key_end_date` (`end_date`),
            KEY `key_status` (`status`),
            KEY `key_permission_id` (`permission_id`),
            CONSTRAINT `fk_', entityName, '__permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`permission_id`),
            CONSTRAINT `fk_', entityName, '__user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
        ) ENGINE = InnoDB
          CHARSET = utf8
          COLLATE = utf8_unicode_ci COMMENT ''', entityDescription, ''';');

    PREPARE QUERY FROM @createDomain; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @createAttribute = CONCAT('
        CREATE TABLE `', entityName, '_attribute`
        (
            `id`                  BIGINT NOT NULL AUTO_INCREMENT COMMENT ''Идентификатор'',
            `domain_id`           BIGINT NOT NULL COMMENT ''Идентификатор домена'',
            `entity_attribute_id` INT NOT NULL COMMENT ''Идентификатор типа атрибута'',
            `text`                VARCHAR(255) COMMENT ''Текст'',
            `number`              BIGINT COMMENT ''Число'',
            `date`                DATETIME COMMENT ''Дата'',
            `start_date`          TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''Дата начала периода действия атрибута'',
            `end_date`            TIMESTAMP  NULL     DEFAULT NULL COMMENT ''Дата окончания периода действия атрибута'',
            `status`              INT    NOT NULL DEFAULT 1 COMMENT ''Статус'',
            `user_id`             BIGINT NULL COMMENT ''Идентифитактор пользователя'',
            PRIMARY KEY (`id`),
            KEY `key_domain_id` (`domain_id`),
            KEY `key_entity_attribute_id` (`entity_attribute_id`),
            KEY `key_text` (`text`),
            KEY `key_number` (`number`),
            KEY `key_date` (`date`),
            KEY `key_start_date` (`start_date`),
            KEY `key_end_date` (`end_date`),
            KEY `key_status` (`status`),
            CONSTRAINT `fk_', entityName, '_attribute__', entityName, '` FOREIGN KEY (`domain_id`) REFERENCES `', entityName, '` (`id`),
            CONSTRAINT `fk_', entityName, '_attribute__user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
        ) ENGINE = InnoDB
          CHARSET = utf8
          COLLATE = utf8_unicode_ci COMMENT ''', entityDescription, ' - Аттрибуты'';');

    PREPARE QUERY FROM @createAttribute; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @createValue = CONCAT('
        CREATE TABLE `', entityName, '_value`
        (
            `id`           BIGINT NOT NULL AUTO_INCREMENT COMMENT ''Идентификатор'',
            `attribute_id` BIGINT NOT NULL COMMENT ''Идентификатор атрибута'',
            `locale_id`    INT COMMENT ''Идентификатор локали'',
            `text`         VARCHAR(1000) COMMENT ''Текстовое значение'',
            `number`       BIGINT COMMENT ''Числовое значение'',
            PRIMARY KEY (`id`),
            UNIQUE KEY `unique_id__locale` (`attribute_id`, `locale_id`),
            KEY `key_attribute_id` (`attribute_id`),
            KEY `key_locale` (`locale_id`),
            KEY `key_value` (`text`(128)),
            CONSTRAINT `fk_', entityName, '_value__', entityName, '_attribute` FOREIGN KEY (`attribute_id`) REFERENCES `',
                entityName, '_attribute` (`id`),
            CONSTRAINT `fk_', entityName, '_value__locale` FOREIGN KEY (`locale_id`) REFERENCES `locale` (`id`)
        ) ENGINE = InnoDB
          CHARSET = utf8
          COLLATE = utf8_unicode_ci COMMENT ''', entityDescription, ' - Значения атрибутов'';');

    PREPARE QUERY FROM @createValue; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;
END //

DROP PROCEDURE IF EXISTS createEntity;
CREATE PROCEDURE createEntity(IN entityId BIGINT,
                              IN entityName VARCHAR(64) CHARSET utf8,
                              IN entityDescriptionRU VARCHAR(128) CHARSET utf8,
                              IN entityDescriptionUA VARCHAR(128) CHARSET utf8)
BEGIN
    SET @insertEntity = CONCAT('INSERT INTO `entity` (`id`, `name`) VALUE (',entityId, ', ''', entityName, ''');');

    PREPARE QUERY FROM @insertEntity; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @insertEntityValue = CONCAT('INSERT INTO `entity_value`(`entity_id`, `locale_id`, `text`) VALUES (', entityId,
                                    ', 1, ''', entityDescriptionRU, '''), (', entityId, ', 2, ''', entityDescriptionUA, ''');');

    PREPARE QUERY FROM @insertEntityValue; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;
END //

DROP PROCEDURE IF EXISTS createAttribute;
CREATE PROCEDURE createAttribute(IN entityId BIGINT,
                                       IN entityAttributeId BIGINT, IN valueTypeId BIGINT,
                                       IN entityDescriptionRU VARCHAR(128) CHARSET utf8,
                                       IN entityDescriptionUA VARCHAR(128) CHARSET utf8)
BEGIN
    SET @insertAttribute = CONCAT('INSERT INTO `entity_attribute`(`entity_id`, `entity_attribute_id`, `value_type_id`) VALUES (',
                                  entityId, ', ', entityAttributeId, ', ', valueTypeId, ');');

    PREPARE QUERY FROM @insertAttribute; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @insertEntityValue = CONCAT('INSERT INTO `entity_value`(`entity_id`, `entity_attribute_id`, `locale_id`, `text`) VALUES (',
                                    entityId, ', ', entityAttributeId, ', 1, ''', entityDescriptionRU, '''), (',
                                    entityId, ', ', entityAttributeId, ', 2, ''', entityDescriptionUA, ''');');

    PREPARE QUERY FROM @insertEntityValue; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;
END //

DROP PROCEDURE IF EXISTS createReference;
CREATE PROCEDURE createReference(IN entityId BIGINT,
                                                    IN entityAttributeId BIGINT,
                                                    IN referenceEntityId BIGINT,
                                                    IN referenceEntityAttributeId BIGINT,
                                                    IN entityDescriptionRU VARCHAR(128) CHARSET utf8,
                                                    IN entityDescriptionUA VARCHAR(128) CHARSET utf8)
BEGIN
    SET @insertAttribute = CONCAT('INSERT INTO `entity_attribute`(`entity_id`, `entity_attribute_id`, `value_type_id`, ',
                                  '`reference_entity_id`, `reference_entity_attribute_id`) VALUES (', entityId, ', ',
                                  entityAttributeId, ', 6', ', ', referenceEntityId, ', ', referenceEntityAttributeId, ');');

    PREPARE QUERY FROM @insertAttribute; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @insertEntityValue = CONCAT('INSERT INTO `entity_value`(`entity_id`, `entity_attribute_id`, `locale_id`, `text`) VALUES (',
                                    entityId, ', ', entityAttributeId, ', 1, ''', entityDescriptionRU, '''), (',
                                    entityId, ', ', entityAttributeId, ', 2, ''', entityDescriptionUA, ''');');

    PREPARE QUERY FROM @insertEntityValue; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;
END //

DROP PROCEDURE IF EXISTS createDomain;
CREATE PROCEDURE createDomain(IN entityId BIGINT,
                                    IN entityName VARCHAR(64) CHARSET utf8,
                                    IN entityDescriptionRU VARCHAR(128) CHARSET utf8,
                                    IN entityDescriptionUA VARCHAR(128) CHARSET utf8)
BEGIN
    SET @dropValue = CONCAT('DROP TABLE IF EXISTS `', entityName, '_value`');
    PREPARE QUERY FROM @dropValue; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @dropAttribute = CONCAT('DROP TABLE IF EXISTS `', entityName, '_attribute`');
    PREPARE QUERY FROM @dropAttribute; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @dropTable = CONCAT('DROP TABLE IF EXISTS `', entityName, '`');
    PREPARE QUERY FROM @dropTable; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    CALL createDomainTables(entityName, entityDescriptionRU);
    CALL createEntity(entityId, entityName, entityDescriptionRU, entityDescriptionUA);
END //

DELIMITER ;


-- ---------------------------
-- Setting
-- ---------------------------

CALL createDomain(0,'setting', 'Настройки', 'Налаштування');
CALL createAttribute(0, 1, 4, 'Значение', 'Значення');


-- ---------------------------
-- Address
-- ---------------------------

CALL createDomain(1,'country', 'Страна', 'Країна');
CALL createAttribute(1, 1, 8, 'Название', 'Назва');

CALL createDomain(2,'region', 'Регион', 'Регіон');
CALL createReference(2, 1, 1, 1, 'Страна', 'Країна');
CALL createAttribute(2, 2, 8, 'Название', 'Назва');

CALL createDomain(3, 'city_type', 'Тип населённого пункта', 'Тип населеного пункту');
CALL createAttribute(3, 1, 8, 'Название', 'Назва');
CALL createAttribute(3, 2, 8, 'Краткое название', 'Коротка назва');

CALL createDomain(4,'city', 'Населённый пункт', 'Населений пункт');
CALL createReference(4, 1, 2, 2, 'Регион', 'Регіон');
CALL createReference(4, 2, 3, 1, 'Тип', 'Тип');
CALL createAttribute(4, 3, 8, 'Название', 'Назва');

CALL createDomain(5,'district', 'Район', 'Район');
CALL createReference(5, 1, 4, 3, 'Населённый пункт', 'Населений пункт');
CALL createAttribute(5, 2, 8, 'Название', 'Назва');
CALL createAttribute(5, 3, 4, 'Код района', 'Код району');

CALL createDomain(6, 'street_type', 'Тип', 'Тип');
CALL createAttribute(6, 1, 8, 'Название', 'Назва');
CALL createAttribute(6, 2, 8, 'Краткое название', 'Коротка назва');

CALL createDomain(7,'street', 'Улица', 'Вулиця');
CALL createReference(7, 1, 4, 3, 'Населённый пункт', 'Населений пункт');
CALL createReference(7, 2, 6, 1, 'Тип улицы', 'Тип вулиці');
CALL createAttribute(7, 3, 8, 'Название', 'Назва');
CALL createAttribute(7, 4, 4, 'Код улицы', 'Код вулиці');

CALL createDomain(8,'building', 'Дом', 'Будинок');
CALL createReference(8, 1, 5, 2, 'Район', 'Район');
CALL createReference(8, 2, 7, 3, 'Улица', 'Вулиця');
CALL createAttribute(8, 3, 8, 'Номер дома', 'Номер будинку');
CALL createAttribute(8, 4, 8, 'Корпус', 'Корпус');
CALL createAttribute(8, 5, 8, 'Строение', 'Будова');
CALL createAttribute(8, 6, 4, 'Код дома', 'Код будинку');

CALL createDomain(9,'apartment', 'Квартира', 'Квартира');
CALL createReference(9, 1, 8, 3, 'Дом', 'Будинок');
CALL createAttribute(9, 2, 8, 'Номер квартиры', 'Номер квартири');

CALL createDomain(10,'room', 'Комната', 'Кімната');
CALL createReference(10, 1, 8, 3, 'Дом', 'Будинок');
CALL createAttribute(10, 2, 8, 'Номер комнаты', 'Номер кімнати');

-- ------------------------------
--  Company
-- ------------------------------

CALL createDomain(11, 'company', 'Компания', 'Компанія');
CALL createReference(11, 1, 11, 2, 'Родительская компания', 'Батьківська компанія');
CALL createAttribute(11, 2, 8, 'Название', 'Назва');
CALL createAttribute(11, 3, 8, 'Краткое название', 'Коротка назва');
CALL createAttribute(11, 4, 4, 'ЕДРПОУ', 'ЄДРПОУ');
CALL createAttribute(11, 5, 4, 'Код компании', 'Код компанії');

-- ---------------------------
-- Matching
-- ---------------------------

DROP PROCEDURE IF EXISTS createMatching;

DELIMITER //

CREATE PROCEDURE createMatching(IN entityName VARCHAR(64) CHARSET utf8,
                                  IN entityDescription VARCHAR(256) CHARSET utf8)
BEGIN
    SET @dropMatching = CONCAT('DROP TABLE IF EXISTS `', entityName, '_matching`;');
    PREPARE QUERY FROM @dropMatching; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;

    SET @createMatching = CONCAT('
        CREATE TABLE `', entityName, '_matching` (
          `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT ''Идентификатор соответствия'',
          `object_id` BIGINT NOT NULL COMMENT ''Идентификатор объекта'',
          `parent_id` BIGINT COMMENT ''Идентификатор родителя'',
          `additional_parent_id` VARCHAR(64) COMMENT ''Дополнительный идентификатор родителя'',
          `external_id` BIGINT COMMENT ''Внешний идентификатор'',
          `additional_external_id` VARCHAR(64) COMMENT ''Дополнительный внешний идентификатор'',
          `name` VARCHAR(1000) NOT NULL COMMENT ''Соответствие'',
          `additional_name` VARCHAR(1000) COMMENT ''Дополнительное соответствие'',
          `start_date` DATETIME NOT NULL DEFAULT NOW() COMMENT ''Дата начала актуальности'',
          `end_date` DATETIME COMMENT ''Дата окончания актуальности'',
          `company_id` BIGINT COMMENT ''Идентификатор компании'',
          `user_company_id` BIGINT COMMENT ''Идентификатор компании пользователя'',
          PRIMARY KEY (`id`),
          UNIQUE KEY `unique_external_id` (`external_id`, `additional_external_id`, `company_id`, `user_company_id`),
          KEY `key_object_id` (`object_id`),
          KEY `key_parent_id` (`parent_id`),
          KEY `key_additional_parent_id` (`additional_parent_id`),
          KEY `key_external_id` (`external_id`),
          KEY `key_additional_external_id` (`additional_external_id`),
          KEY `key_name` (`name`),
          KEY `key_additional_name` (`additional_name`),
          KEY `key_start_date` (`start_date`),
          KEY `key_end_date` (`end_date`),
          KEY `key_company_id` (`company_id`),
          KEY `key_user_company_id` (`user_company_id`),
          CONSTRAINT `fk_', entityName, '_matching__', entityName, '` FOREIGN KEY (`object_id`) REFERENCES `', entityName, '` (`object_id`)
        ) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT ''', entityDescription, ' - Соответствия'';');

    PREPARE QUERY FROM @createMatching; EXECUTE QUERY; DEALLOCATE PREPARE QUERY;
END //

DELIMITER ;

CALL createMatching('country', 'Страны');
CALL createMatching('region', 'Регионы');
CALL createMatching('city_type', 'Тип населённого пункта');
CALL createMatching('city', 'Населённые пункты');
CALL createMatching('district', 'Районы');
CALL createMatching('street_type', 'Типы улиц');
CALL createMatching('street', 'Улица');
CALL createMatching('building', 'Дом');
CALL createMatching('apartment', 'Квартира');


-- ------------------------------
--  Sync
-- ------------------------------

DROP TABLE IF EXISTS `sync`;
CREATE TABLE `sync`(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Идентификатор синхронизации',
  `parent_id` BIGINT COMMENT 'Идентификатор родителя',
  `additional_parent_id` VARCHAR(64) COMMENT 'Дополнительный идентификатор родителя',
  `external_id` BIGINT NOT NULL COMMENT 'Внешний идентификатор',
  `additional_external_id` VARCHAR(64) COMMENT 'Дополнительный внешний идентификатор',
  `name` VARCHAR(250) NOT NULL COMMENT 'Название',
  `additional_name` VARCHAR(50) COMMENT 'Дополнительное название',
  `alt_name` VARCHAR(250) COMMENT 'Название в альтернативной локали',
  `alt_additional_name` VARCHAR(50) COMMENT 'Дополнительное название в альтернативной локали',
  `servicing_organization` BIGINT COMMENT 'Обслуживающая организация',
  `balance_holder` BIGINT COMMENT 'Балансодержатель',
  `date` DATETIME NOT NULL COMMENT 'Дата актуальности',
  `status` INT NOT NULL COMMENT 'Статус синхронизации',
  `entity_id` INT NOT NULL COMMENT 'Идентификатор сущности',
  PRIMARY KEY (`id`),
  KEY `key_parent_id` (`parent_id`),
  KEY `key_additional_parent_id` (`additional_parent_id`),
  KEY `key_external_id` (`external_id`),
  KEY `key_additional_external_id` (`additional_external_id`),
  KEY `key_name` (`name`),
  KEY `key_additional_name` (`additional_name`),
  KEY `key_servicing_organization` (`servicing_organization`),
  KEY `key_balance_holder` (`balance_holder`),
  KEY `key_date` (`date`),
  KEY `key_status` (`status`),
  KEY `key_entity_id` (`entity_id`),
  CONSTRAINT `fk_sync__entity_id` FOREIGN KEY (`entity_id`) REFERENCES `entity` (`id`)
) ENGINE=InnoDB CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT 'Синхронизация';


/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
