update property set value=replace(value, '@myoscar.org', '') where name='MyOscarId';
update demographic set myOscarUserName=replace(myOscarUserName, '@myoscar.org', '');