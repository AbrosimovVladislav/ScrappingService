INSERT INTO type (type_id, upper, medium, show_name)
VALUES (1,
        'ЗАЩИТА_ИГРОКА',
        'ЗАЩИТА_ПАХА',
        'Защита паха')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (2,
        'КОНЬКИ',
        'КОНЬКИ_РОЛИКОВЫЕ',
        'Коньки роликовые')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (3,
        'КОНЬКИ',
        'КОНЬКИ_ФИГУРНЫЕ',
        'Коньки фигурные')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (4,
        'ЗАЩИТА_ИГРОКА',
        'НАГРУДНИКИ',
        'Нагрудники')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (5,
        'ЗАЩИТА_ИГРОКА',
        'ЩИТКИ',
        'Щитки')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (6,
        'ЗАЩИТА_ИГРОКА',
        'ШЛЕМ',
        'Шлем')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (7,
        'КОНЬКИ',
        'КОНЬКИ_ХОККЕЙНЫЕ',
        'Коньки хоккейные')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (8,
        'ЗАЩИТА_ИГРОКА',
        'НАЛОКОТНИКИ',
        'Налокотники')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (9,
        'ЗАЩИТА_ИГРОКА',
        'ТРУСЫ',
        'Трусы')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (10,
        'ЗАЩИТА_ИГРОКА',
        'ВИЗОРЫ_И_МАСКИ',
        'Визоры и маски')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (11,
        'ЗАЩИТА_ИГРОКА',
        'ЗАЩИТА_ШЕИ',
        'Защита шеи')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (12,
        'ЗАЩИТА_ИГРОКА',
        'ПЕРЧАТКИ',
        'Перчатки')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (13,
        'ЗАЩИТА_ИГРОКА',
        'ЗАЩИТА_ЗАПЯСТЬЯ',
        'Защита запястья')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (14,
        'КЛЮШКИ',
        'ИГРОКА',
        'Клюшки игрока')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (15,
        'КЛЮШКИ',
        'ВРАТАРЯ',
        'Вратарские клюшки')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (16,
        'ОДЕЖДА',
        'ГАМАШИ',
        'Гамаши')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (17,
        'ОДЕЖДА',
        'ТРЕНИРОВОЧНЫЕ_МАЙКИ',
        'Тренировочные майки')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (18,
        'ОДЕЖДА',
        'НАТЕЛЬНОЕ_БЕЛЬЕ',
        'Нательное белье')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (19,
        'ВРАТАРСКАЯ_ФОРМА',
        'НАГРУДНИКИ',
        'Вратаские нагрудники')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (20,
        'ВРАТАРСКАЯ_ФОРМА',
        'ТРУСЫ',
        'Вратарские трусы')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (21,
        'ВРАТАРСКАЯ_ФОРМА',
        'КОНЬКИ',
        'Вратарские коньки')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (22,
        'ВРАТАРСКАЯ_ФОРМА',
        'ЩИТКИ',
        'Вратарские щитки')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (23,
        'ВРАТАРСКАЯ_ФОРМА',
        'ШЛЕМА_И_МАСКИ',
        'Вратарские шлема')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (24,
        'ВРАТАРСКАЯ_ФОРМА',
        'БЛИН_И_ЛОВУШКА',
        'Блин и ловушка')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (25,
        'ВРАТАРСКАЯ_ФОРМА',
        'АКСЕССУАРЫ',
        'Аксессуары для вратарей')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (26,
        'СУМКИ',
        'СУМКИ_ХОККЕЙНЫЕ',
        'Сумки хоккейные')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (27,
        'СУМКИ',
        'СУМКИ_ВРАТАРЯ',
        'Сумки вратаря')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (28,
        'СУМКИ',
        'СУМКИ_ДЛЯ_ПРИНАДЛЕЖНОСТЕЙ',
        'Сумки для принадлежностей')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (29,
        'АКСЕССУАРЫ',
        'АКСЕССУАРЫ_ДЛЯ_КОНЬКОВ',
        'Аксессуары для коньков')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (30,
        'АКСЕССУАРЫ',
        'АКСЕССУАРЫ_ДЛЯ_ШЛЕМА',
        'Аксессуары для шлема')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (31,
        'АКСЕССУАРЫ',
        'АКСЕССУАРЫ_ДЛЯ_КЛЮШЕК',
        'Аксессуары для клюшек')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, medium, show_name)
VALUES (32,
        'АКСЕССУАРЫ',
        'АКСЕССУАРЫ_ДЛЯ_ИГРОКА',
        'Аксессуары для игрока')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, show_name)
VALUES (33,
        'ХОККЕЙНАЯ_ШАЙБА',
        'Хоккейная шайба')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, show_name)
VALUES (34,
        'ХОККЕЙНЫЕ_ВОРОТА',
        'Хоккейные ворота')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
INSERT INTO type (type_id, upper, show_name)
VALUES (35,
        'РАЗНОЕ',
        'Разное')
ON CONFLICT (type_id) DO UPDATE
    SET upper     = excluded.upper,
        medium    = excluded.medium,
        show_name = excluded.show_name;;
