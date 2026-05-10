-- Inserção dos clientes
MERGE INTO clientes KEY(cpf) VALUES ('9001', 'Huguinho Pato', '51985744566', 'Rua das Flores, 100', 'huguinho.pato@email.com', '123456');
MERGE INTO clientes KEY(cpf) VALUES ('9002', 'Luizinho Pato', '5199172079', 'Av. Central, 200', 'zezinho.pato@email.com', '123456');
MERGE INTO clientes KEY(cpf) VALUES ('1111', 'Teste Silva', '51999999999', 'Rua Teste 1', 'teste@email.com', '123456');

-- Inserção dos ingredientes
MERGE INTO ingredientes KEY(id) VALUES (1, 'Disco de pizza');
MERGE INTO ingredientes KEY(id) VALUES (2, 'Porcao de tomate');
MERGE INTO ingredientes KEY(id) VALUES (3, 'Porcao de mussarela');
MERGE INTO ingredientes KEY(id) VALUES (4, 'Porcao de presunto');
MERGE INTO ingredientes KEY(id) VALUES (5, 'Porcao de calabresa');
MERGE INTO ingredientes KEY(id) VALUES (6, 'Molho de tomate (200ml)');
MERGE INTO ingredientes KEY(id) VALUES (7, 'Porcao de azeitona');
MERGE INTO ingredientes KEY(id) VALUES (8, 'Porcao de oregano');
MERGE INTO ingredientes KEY(id) VALUES (9, 'Porcao de cebola');

-- Inserção dos itens de estoque
MERGE INTO itensEstoque KEY(id) VALUES (1, 30, 1);
MERGE INTO itensEstoque KEY(id) VALUES (2, 30, 2);
MERGE INTO itensEstoque KEY(id) VALUES (3, 30, 3);
MERGE INTO itensEstoque KEY(id) VALUES (4, 30, 4);
MERGE INTO itensEstoque KEY(id) VALUES (5, 30, 5);
MERGE INTO itensEstoque KEY(id) VALUES (6, 30, 6);
MERGE INTO itensEstoque KEY(id) VALUES (7, 30, 7);
MERGE INTO itensEstoque KEY(id) VALUES (8, 30, 8);
MERGE INTO itensEstoque KEY(id) VALUES (9, 30, 9);

-- Inserção das receitas
MERGE INTO receitas KEY(id) VALUES (1, 'Pizza calabresa');
MERGE INTO receitas KEY(id) VALUES (2, 'Pizza queijo e presunto');
MERGE INTO receitas KEY(id) VALUES (3, 'Pizza margherita');

-- Associação dos ingredientes à receita Pizza calabresa
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (1, 1);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (1, 6);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (1, 3);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (1, 5);
-- Associação dos ingredientes à receita Pizza queijo e presunto
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (2, 1);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (2, 6);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (2, 3);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (2, 4);
-- Associação dos ingredientes à receita Pizza margherita
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (3, 1);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (3, 6);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (3, 3);
MERGE INTO receita_ingrediente KEY(receita_id, ingrediente_id) VALUES (3, 8);

-- insercao dos produtos
MERGE INTO produtos KEY(id) VALUES (1,'Pizza calabresa',5500);
MERGE INTO produtos KEY(id) VALUES (2,'Pizza queijo e presunto',6000);
MERGE INTO produtos KEY(id) VALUES (3,'Pizza margherita',4000);

-- Associação dos produtos com as receitas
MERGE INTO produto_receita KEY(produto_id, receita_id) VALUES(1,1);
MERGE INTO produto_receita KEY(produto_id, receita_id) VALUES(2,2);
MERGE INTO produto_receita KEY(produto_id, receita_id) VALUES(3,3);

-- Insercao dos cardapios
MERGE INTO cardapios KEY(id) VALUES(1,'Cardapio de Agosto');
MERGE INTO cardapios KEY(id) VALUES(2,'Cardapio de Setembro');

-- Associação dos cardapios com os produtos
MERGE INTO cardapio_produto KEY(cardapio_id, produto_id) VALUES (1,1);
MERGE INTO cardapio_produto KEY(cardapio_id, produto_id) VALUES (1,2);
MERGE INTO cardapio_produto KEY(cardapio_id, produto_id) VALUES (1,3);

MERGE INTO cardapio_produto KEY(cardapio_id, produto_id) VALUES (2,1);
MERGE INTO cardapio_produto KEY(cardapio_id, produto_id) VALUES (2,3);

MERGE INTO cardapio_corrente KEY(id) VALUES (1, 1);