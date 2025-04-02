use demotest;

INSERT INTO RuleName (name, description, json, template, sql_str, sql_part) VALUES 
  ('Rule1', 'Description1', '{"param": 100}', 'Template1', 'SELECT * FROM table', 'WHERE field=1'),
  ('Rule2', 'Description2', '{"param": 200}', 'Template2', 'SELECT * FROM table', 'WHERE field=2');