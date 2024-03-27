import Control.Concurrent
import Control.Concurrent.STM

type Buffer a = TVar [a]

newBuffer :: [a] -> IO (Buffer a)
newBuffer = newTVarIO

putBuffer :: Buffer a -> a -> STM ()
putBuffer buffer item = do
    items <- readTVar buffer
    writeTVar buffer (items ++ [item])

getBuffer :: Buffer a -> STM a
getBuffer buffer = do
    items <- readTVar buffer
    case items of
        [] -> retry
        (x:xs) -> do
            writeTVar buffer xs
            return x