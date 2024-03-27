-- Arthur Romaguera Lima

--1)
-- Verifica se um elemento ocorre duas vezes em sucessão
ocorreDuasVezes :: Int -> Int -> Int -> Bool
ocorreDuasVezes x y z = x == y && y /= z

-- Função principal que retorna os elementos que ocorrem duas ou mais vezes em sucessão
f :: [Int] -> [Int]
f (x:y:resto)
 | x == y       = x : f (y:resto)
 | otherwise    = f (y:resto)
f _ = []

--b)
-- Função principal que retorna os elementos que ocorrem duas ou mais vezes em sucessão
fb :: [Int] -> [Int]
fb xs = [x | (x, y) <- zip xs (drop 1 xs), x == y]
-- zip xs (drop 1 xs) cria uma lista de tuplas onde cada tupla consiste em um elemento da lista original xs e o elemento subsequente.
-- filtra apenas os elementos onde x é igual a y, o que significa que esses elementos ocorrem duas ou mais vezes em sucessão.

--2)
testaElementos :: (a -> Bool) -> [a] -> Bool
testaElementos p [] = True
testaElementos p (x : xs)
  | p x = testaElementos p xs
  | otherwise = False

-- b)
testaElementosb :: (a -> Bool) -> [a] -> Bool
testaElementosb p l = and (map p l)

-- c)
testaElementosc :: (a -> Bool) -> [a] -> Bool
testaElementosc p = foldr (\x acc -> p x && acc) True

--3)
sublistas :: [a] -> [[a]]
sublistas [] = [[]]  -- A lista vazia tem apenas uma sublista, que é ela mesma
sublistas (x:xs) = sublistas xs ++ map (x:) (sublistas xs)

--4)
--a)
poli :: Integer -> Integer -> Integer -> Integer -> Integer
poli a b c x = a * x^2 + b * x + c

--b)
listaPoli :: [(Integer, Integer, Integer)] -> [Integer -> Integer]
listaPoli = map (\(a, b, c) -> poli a b c)

--c)
appListaPoli :: [Integer -> Integer] -> [Integer] -> [Integer]
appListaPoli polinomios inteiros = map (\f -> map f inteiros) polinomios >>= id

--5)
data Pilha a = Pilha [a] deriving (Show)

-- Função para adicionar um elemento ao topo da pilha
push :: a -> Pilha a -> Pilha a
push x (Pilha xs) = Pilha (x:xs)

-- Função para remover o elemento do topo da pilha
pop :: Pilha a -> Pilha a
pop (Pilha []) = error "Pilha vazia: não é possível fazer pop"
pop (Pilha (_:xs)) = Pilha xs

-- Função para obter o elemento do topo da pilha sem removê-lo
top :: Pilha a -> a
top (Pilha []) = error "Pilha vazia: não é possível acessar o topo"
top (Pilha (x:_)) = x




