INSERT INTO type (type_id, upper, medium, show_name) VALUES (1,'ЗАЩИТА_ИГРОКА','ЗАЩИТА_ПАХА','Защита паха')
ON CONFLICT (type_id) DO UPDATE
SET upper = excluded.upper,
    medium = excluded.medium,
    show_name = excluded.show_name;;
