--Lista 1 PLC 2023.2
--Arthur Romaguera Lima - arl3

--1:
mdc :: Int -> Int -> Int
mdc a b
 | b == 0       = a
 | b > 0        = mdc b (mod a b)
 | otherwise    = mdc a (-b)


--2a:
--numDiv :: Integral a => a -> a -> a 



--2b:
numDivr :: Int -> Int -> Int
numDivr a b 
 | mod a b == 1     = 0
 | otherwise        = 1 + numDivr (div a b) b


--3:
unicos :: [Integer] -> [Integer]
unicos []            = []
unicos (x:xs)
 | elem x (unicos xs) = [ y | y <- (unicos xs), y /= x]
 | otherwise          = x : (unicos xs)


--4:
halve :: [a] -> ([a],[a])
halve []            = ([],[])
halve (x:xs)        = (iPar (x:xs), iImpar (x:xs))

iPar []     = []
iPar (x:xs) = x: iImpar xs

iImpar []      = []
iImpar (x:xs)  = iPar xs


--5:
remDiv :: Int -> [a] -> ([a],[a])
remDiv n x      = (take n x, drop n x)


