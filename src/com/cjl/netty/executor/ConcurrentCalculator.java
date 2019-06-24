package com.cjl.netty.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author junlinchen
 * @version 1.0
 * @date 2019/6/24 15:49
 * @Des :并行计算数组的和
 */
public class ConcurrentCalculator {
    private ExecutorService exec;
    private int cpuCoreNumber;
    private List<Future<Long>> tasks = new ArrayList<Future<Long>>();

    public ConcurrentCalculator() {
        // 得到cpu内核的个数
        cpuCoreNumber = Runtime.getRuntime().availableProcessors();
        // 有几个cpu，则创建有几个线程的线程池
        exec = Executors.newFixedThreadPool(cpuCoreNumber);
    }

    public void close() {
        exec.shutdown();
    }

    public static void main(String[] args) {
        int[] numbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 10, 11 };
        ConcurrentCalculator calc = new ConcurrentCalculator();
        Long sum = calc.sum(numbers);
        System.out.println(sum);
        calc.close();
    }

    // 内部类，实现Callable接口，将其实例提交给Executor可执行
    class SumCalculator implements Callable<Long> {
        private int[] numbers;
        private int start;
        private int end;

        public SumCalculator(final int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        public Long call() throws Exception {
            Long sum = 0l;
            for (int i = start; i < end; i++) {
                sum += numbers[i];
            }

            return sum;
        }
    }


    public Long sum(final int[] numbers) {
        // 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor
        for (int i = 0; i < cpuCoreNumber; i++) {
            // 将数组分成多端，使用多个任务计算
            int increment = numbers.length / cpuCoreNumber + 1;
            int start = increment * i;
            int end = increment * i + increment;
            if (end > numbers.length) {
                end = numbers.length;
            }

            SumCalculator subCalc = new SumCalculator(numbers, start, end);
            // FutureTask<V>实现了Future<V>和Runable<V>
            FutureTask<Long> task = new FutureTask<Long>(subCalc);
            tasks.add(task);
            if (!exec.isShutdown()) {
                // 因为 FutureTask 实现了 Runnable，所以可将 FutureTask 提交给 Executor 执行。
                exec.submit(task);
            }
        }

        return getResult();
    }

    /**
     * 迭代每个只任务，获得部分和，相加返回
     *
     * @return
     */
    public Long getResult() {
        Long result = 0l;
        for (Future<Long> task : tasks) {
            try {
                // 如果计算未完成则阻塞  (Future中保存的是Callable的执行结果，可以使用get得到)
                Long subSum = task.get();
                result += subSum;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
