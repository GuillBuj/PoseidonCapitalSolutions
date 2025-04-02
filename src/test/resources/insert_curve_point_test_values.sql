use demotest;

INSERT INTO CurvePoint (curve_id, term, value, creation_date) VALUES 
  (1, 1.0, 10.0, CURRENT_TIMESTAMP),
  (2, 2.0, 20.0, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 HOUR));CREATE TABLE CurvePoint (
  id INT NOT NULL AUTO_INCREMENT,
  CurveId tinyint,
  asOfDate TIMESTAMP,
  term DOUBLE ,
  value DOUBLE ,
  creationDate TIMESTAMP ,

  PRIMARY KEY (id)
);