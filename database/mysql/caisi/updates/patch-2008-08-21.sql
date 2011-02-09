alter table intake_node add eq_to_id int(10) default NULL;
update intake_node set eq_to_id=intake_node_id;

