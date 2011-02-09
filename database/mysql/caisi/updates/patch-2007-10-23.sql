# field to represent if there is a shortcut to this view in the main screen

alter table custom_filter add `shortcut` tinyint(1) default '0';