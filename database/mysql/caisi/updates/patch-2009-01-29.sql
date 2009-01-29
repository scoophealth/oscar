-- making sure the activity mark is 'active', not '1'
update program set programStatus='active' where programStatus='1';
