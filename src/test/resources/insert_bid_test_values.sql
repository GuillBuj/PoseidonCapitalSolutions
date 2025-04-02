use demotest;

INSERT INTO Bid (account, type, bid_quantity, ask_quantity, bid, ask, benchmark, bid_list_date, commentary, security, status, trader, book, creation_name, creation_date, revision_name, revision_date, deal_name, deal_type, source_list_id, side) 
VALUES 
('Account1', 'TypeA', 100.0, 95.0, 102.0, 98.0, 'Benchmark1', CURRENT_TIMESTAMP, 'Commentaire bid 1', 'Security1', 'Active', 'Trader1', 'Book1', 'Creator1', CURRENT_TIMESTAMP, 'Revisor1', CURRENT_TIMESTAMP, 'Deal1', 'DealType1', 'Source1', 'Buy');

INSERT INTO Bid (account, type, bid_quantity, ask_quantity, bid, ask, benchmark, bid_list_date, commentary, security, status, trader, book, creation_name, creation_date, revision_name, revision_date, deal_name, deal_type, source_list_id, side) 
VALUES 
('Account2', 'TypeB', 150.0, 145.0, 105.0, 100.0, 'Benchmark2', CURRENT_TIMESTAMP, 'Commentaire bid 2', 'Security2', 'Inactive', 'Trader2', 'Book2', 'Creator2', CURRENT_TIMESTAMP, 'Revisor2', CURRENT_TIMESTAMP, 'Deal2', 'DealType2', 'Source2', 'Sell');