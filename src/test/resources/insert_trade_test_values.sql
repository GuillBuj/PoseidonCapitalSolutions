use demotest;

INSERT INTO Trade (account, type, buy_quantity, sell_quantity, buy_price, sell_price, trade_date, security, status, trader, benchmark, side) VALUES 
  ('Account1', 'TypeA', 100.0, 0.0, 102.0, 0.0, CURRENT_TIMESTAMP, 'Security1', 'Executed', 'Trader1', 'Benchmark1', 'Buy'),
  ('Account2', 'TypeB', 0.0, 200.0, 0.0, 198.0, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), 'Security2', 'Pending', 'Trader2', 'Benchmark2', 'Sell');