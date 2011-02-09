create or replace view  v_lookuptable AS    select tableid as code, description, 
activeyn, 0 as displayorder, moduleid,table_name,treecode_length, isTree, tableid from app_lookuptable where activeyn=1;
