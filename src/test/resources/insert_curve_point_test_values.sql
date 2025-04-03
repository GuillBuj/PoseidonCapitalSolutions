use demotest;

INSERT INTO CurvePoint (curve_id, term, value, creation_date) VALUES 
  (1, 1.0, 10.0, CURRENT_TIMESTAMP),
  (2, 2.0, 20.0, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 HOUR));