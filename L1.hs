--Lista 1 PLC 2023.2
--Arthur Romaguera Lima - arl3

mdc :: Int -> Int -> Int
mdc a b
 | b == 0       = a
 | b > 0        = mdc b (a `mod` b)
 | otherwise    = mdc a (-b)

