import Control.Concurrent

data CountDownLatch = CountDownLatch (MVar Int)

newCountDownLatch :: Int -> IO CountDownLatch
newCountDownLatch n = do
    mvar <- newMVar n
    return (CountDownLatch mvar)

await :: CountDownLatch -> IO ()
await (CountDownLatch mvar) = do
    n <- takeMVar mvar
    if n == 0
        then putMVar mvar 0
        else do
            putMVar mvar (n-1)
            await (CountDownLatch mvar)

countDown :: CountDownLatch -> IO ()
countDown (CountDownLatch mvar) = do
    n <- takeMVar mvar
    putMVar mvar (n-1)

main :: IO ()
main = do
    latch <- newCountDownLatch 3
    putStrLn "Waiting for count down latch..."
    forkIO $ do
        putStrLn "Thread 1 started."
        countDown latch
        putStrLn "Thread 1 finished."
    forkIO $ do
        putStrLn "Thread 2 started."
        countDown latch
        putStrLn "Thread 2 finished."
    forkIO $ do
        putStrLn "Thread 3 started."
        countDown latch
        putStrLn "Thread 3 finished."
    await latch
    putStrLn "All threads finished, latch released."