package cloud.simple.hello;

import java.util.Random;
import java.util.concurrent.RecursiveTask;

/** 
 * ClassName: SumTask <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * 我们通过调整阈值（THRESHOLD），可以发现耗时是不一样的。
 * 实际应用中，如果需要分割的任务大小是固定的，可以经过测试，得到最佳阈值；
 * 如果大小不是固定的，就需要设计一个可伸缩的算法，来动态计算出阈值。
 * 如果子任务很多，效率并不一定会高
 */  
public class SumTask extends RecursiveTask<Integer>{

	/**
	 * 生成串行serialVersionUI
	 */  
	private static final long serialVersionUID = -6860694763557921865L;

	private static final int THRESHOLD = 500000;

	private long[] array;

	private int low;

	private int high;

	public SumTask(long[] array, int low, int high) {
		this.array = array;
		this.low = low;
		this.high = high;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		if (high - low <= THRESHOLD) {
			// 小于阈值则直接计算
			for (int i = low; i < high; i++) {
				sum += array[i];
			}
		} else {
			// 1. 一个大任务分割成两个子任务
			int mid = (low + high) >>> 1;
		SumTask left = new SumTask(array, low, mid);
		SumTask right = new SumTask(array, mid + 1, high);

		// 2. 分别计算
		left.fork();
		right.fork();

		// 3. 合并结果
		sum = left.join() + right.join();
		}
		return sum;
	}

//	public static void main(String[] args) throws ExecutionException, InterruptedException {
//		long[] array = genArray(1000000);
//
//		System.out.println(Arrays.toString(array));
//
//		// 1. 创建任务
//		SumTask sumTask = new SumTask(array, 0, array.length - 1);
//
//		long begin = System.currentTimeMillis();
//
//		// 2. 创建线程池
//		ForkJoinPool forkJoinPool = new ForkJoinPool();
//
//		// 3. 提交任务到线程池
//		forkJoinPool.submit(sumTask);
//
//		// 4. 获取结果
//		Integer result = sumTask.get();
//
//		long end = System.currentTimeMillis();
//
//		System.out.println(String.format("结果 %s 耗时 %sms", result, end - begin));
//	}

	private static long[] genArray(int size) {
		long[] array = new long[size];
		for (int i = 0; i < size; i++) {
			array[i] = new Random().nextLong();
		}
		return array;
	}
}
