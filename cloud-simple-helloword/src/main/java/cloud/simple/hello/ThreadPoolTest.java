package cloud.simple.hello;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** 
 * ClassName: ThreadPoolTest <br/> 
 * Function: 4种线程池. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2016年12月16日 上午10:07:56 <br/> 
 * 
 * @author admin 
 * @version  
 */  
public class ThreadPoolTest {
	
//	public static void main(String[] args){
//		//1.创建一个可重用线程集合的线程池，以共享的无界队列方式来运行这些线程。
//		newFixed();
//		//2.创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
////		newCached();
//		//3.创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
////		newSingle();
//		//4.创建一个可安排在给定延迟后运行命令或者定期地执行的线程池。
////		newSchedule();
//	}
	
	
	/**
	 * 1.创建一个可重用线程集合的线程池，以共享的无界队列方式来运行这些线程。
	 * 
	 * 代码中，创建了一个基本大小与最大大小相同的线程池，容量为3，然后循环执行了4个任务，
	 * 由输出结果可以看到，前3个任务首先执行完，然后空闲下来的线程去执行第4个任务，
	 * 在FixedThreadPool中，有一个固定大小的池，如果当前需要执行的任务超过了池大小，
	 * 那么多于的任务等待状态，直到有空闲下来的线程执行任务，而当执行的任务小于池大小，空闲的线程也不会去销毁。
	 * */
	public static void newFixed(){
		int nThreads = 3;
		ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
		
		for(int i=1;i<5;i++){
			
			final int taskID = i;
			
			threadPool.execute(new Runnable() {
				
				public void run() {
					
					for (int i = 0; i < 5; i++) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("第" + taskID + "次任务的第" + i + "次执行");  
					}
				}
			});
		}
		threadPool.shutdown();//任务执行完毕，关闭线程池
	}
	
		//执行结果：
//		第2次任务的第0次执行
//		第1次任务的第0次执行
//		第3次任务的第0次执行
//		第3次任务的第1次执行
//		第1次任务的第1次执行
//		第2次任务的第1次执行
//		第2次任务的第2次执行
//		第1次任务的第2次执行
//		第3次任务的第2次执行
//		第1次任务的第3次执行
//		第3次任务的第3次执行
//		第2次任务的第3次执行
//		第2次任务的第4次执行
//		第1次任务的第4次执行
//		第3次任务的第4次执行
//		第4次任务的第0次执行
//		第4次任务的第1次执行
//		第4次任务的第2次执行
//		第4次任务的第3次执行
//		第4次任务的第4次执行

	
	/**
	 * 2.创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
	 * 
	 * 可见，4个任务是交替执行的，CachedThreadPool会创建一个缓存区，
	 * 将初始化的线程缓存起来，如果线程有可用的，就使用之前创建好的线程，
	 * 如果没有可用的，就新创建线程，终止并且从缓存中移除已有60秒未被使用的线程。
	 * */
	public static void newCached(){
		ExecutorService threadPool = Executors.newCachedThreadPool();//线程池的大小会根据执行的任务数动态分配
		
		for(int i=1;i<5;i++){
			
			final int taskID = i;
			
			threadPool.execute(new Runnable() {
				
				public void run() {
					
					for (int i = 0; i < 5; i++) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("第" + taskID + "次任务的第" + i + "次执行");  
					}
				}
			});
		}
		threadPool.shutdown();//任务执行完毕，关闭线程池
	}
	
		//执行结果
//		第3次任务的第0次执行
//		第1次任务的第0次执行
//		第2次任务的第0次执行
//		第4次任务的第0次执行
//		第4次任务的第1次执行
//		第2次任务的第1次执行
//		第1次任务的第1次执行
//		第3次任务的第1次执行
//		第3次任务的第2次执行
//		第4次任务的第2次执行
//		第1次任务的第2次执行
//		第2次任务的第2次执行
//		第1次任务的第3次执行
//		第3次任务的第3次执行
//		第4次任务的第3次执行
//		第2次任务的第3次执行
//		第2次任务的第4次执行
//		第1次任务的第4次执行
//		第3次任务的第4次执行
//		第4次任务的第4次执行

	
	/**
	 * 3.创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
	 * 
	 * 4个任务是顺序执行的，SingleThreadExecutor得到的是一个单个的线程，
	 * 这个线程会保证你的任务执行完成，如果当前线程意外终止，会创建一个新线程继续执行任务，
	 * 这和我们直接创建线程不同，也和newFixedThreadPool(1)不同
	 * */
	public static void newSingle(){
		ExecutorService threadPool = Executors.newSingleThreadExecutor();//创建单个线程的线程池，如果当前线程在继续执行任务
		
		for(int i=1;i<5;i++){
			
			final int taskID = i;
			
			threadPool.execute(new Runnable() {
				
				public void run() {
					
					for (int i = 0; i < 5; i++) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("第" + taskID + "次任务的第" + i + "次执行");  
					}
				}
			});
		}
		threadPool.shutdown();//任务执行完毕，关闭线程池
	}
	
		//执行结果
//		第1次任务的第0次执行
//		第1次任务的第1次执行
//		第1次任务的第2次执行
//		第1次任务的第3次执行
//		第1次任务的第4次执行
//		第2次任务的第0次执行
//		第2次任务的第1次执行
//		第2次任务的第2次执行
//		第2次任务的第3次执行
//		第2次任务的第4次执行
//		第3次任务的第0次执行
//		第3次任务的第1次执行
//		第3次任务的第2次执行
//		第3次任务的第3次执行
//		第3次任务的第4次执行
//		第4次任务的第0次执行
//		第4次任务的第1次执行
//		第4次任务的第2次执行
//		第4次任务的第3次执行
//		第4次任务的第4次执行

	
	/**
	 * 4.创建一个可安排在给定延迟后运行命令或者定期地执行的线程池。
	 * 
	 * ScheduledThreadPool可以定时的或延时的执行任务。
	 * */
	public static void newSchedule(){
		
		final int corePoolSize = 1; 
		
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(corePoolSize);//效果类似于Timer定时器
		
		//5秒后执行任务
		threadPool.schedule(new Runnable() {

			public void run() {
				System.out.println("爆炸");
			}
		}, 5, TimeUnit.SECONDS);

		//5秒后执行任务，之后每2秒执行一次
		threadPool.scheduleAtFixedRate(new Runnable() {

			public void run() {
				System.out.println("爆炸");
			}
		}, 5, 2, TimeUnit.SECONDS);
	}
	
		//执行结果
//		爆炸
//		爆炸
//		爆炸
//		爆炸
	
}
