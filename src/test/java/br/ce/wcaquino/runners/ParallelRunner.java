package br.ce.wcaquino.runners;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelRunner extends BlockJUnit4ClassRunner {
    public ParallelRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        setScheduler(new TreadPoll());
    }

    private static class TreadPoll implements RunnerScheduler {
        private ExecutorService executorService;

        public TreadPoll() {
            this.executorService = Executors.newFixedThreadPool(2);
        }

        public void schedule(Runnable runnable) {
            executorService.submit(runnable);
        }

        public void finished() {
            try {
                executorService.shutdown();
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
