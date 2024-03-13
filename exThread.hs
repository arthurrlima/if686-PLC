module Main where
import Control.Concurrent
import Control.Concurrent.MVar

threadA :: MVar Float -> MVar Float -> IO ()
threadA toSend toReceive
  = do putMVar toSend 72
       v <- takeMVar toReceive
       print v

threadB :: MVar Float -> MVar Float -> IO ()
threadB toReceive toSend
  = do z <- takeMVar toReceive
       putMVar toSend (1.5 * z)

main :: IO ()
main = do aMVar <- newEmptyMVar
          bMVar <- newEmptyMVar
          forkIO (threadA aMVar bMVar)
          forkIO (threadB aMVar bMVar)
          --threadDelay 1000
          return ()