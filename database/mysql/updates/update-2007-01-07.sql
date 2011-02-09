#Here is a new table for the report by template module

CREATE TABLE reportTemplates (
  templateid varchar(40) NOT NULL,
  templatetitle varchar(80) NOT NULL DEFAULT '',
  templatedescription text NOT NULL,
  templatesql text NOT NULL,
  templateparamxml text NOT NULL,
  active tinyint NOT NULL DEFAULT 1,
  PRIMARY KEY (templateid),
  KEY(active)
);